const User = require('../models/User');
const jwt = require('jsonwebtoken');
const nodemailer = require('nodemailer');
const crypto = require('crypto');
const mongoose = require('mongoose');

// Configure nodemailer
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASS
    }
});

// Register new user
exports.register = async (req, res) => {
    try {
        const { email, password, name, fullName, age, dob, address, phone } = req.body;
        let role = req.body.role || 'user';

        // Nếu có xác thực (token), kiểm tra quyền
        if (req.user) {
            // Nếu không phải admin mà cố tạo admin
            if (role === 'admin' && req.user.role !== 'admin') {
                return res.status(403).json({ message: 'Only admin can create another admin.' });
            }
        } else {
            // Nếu không có token, không cho phép tạo admin
            if (role === 'admin') {
                role = 'user';
            }
        }

        // Kiểm tra user đã tồn tại
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({ message: 'User already exists' });
        }

        // Tạo user mới
        const user = new User({
            email,
            password,
            name,
            fullName,
            age,
            dob,
            address,
            phone,
            role
        });

        await user.save();

        // Generate JWT token
        const token = jwt.sign(
            { userId: user._id, role: user.role },
            process.env.JWT_SECRET,
            { expiresIn: '24h' }
        );

        res.status(201).json({
            message: 'User registered successfully',
            token,
            user: {
                id: user._id,
                email: user.email,
                name: user.name,
                role: user.role
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error registering user', error: error.message });
    }
};

// Login user
exports.login = async (req, res) => {
    try {
        const { email, password } = req.body;

        // Find user
        const user = await User.findOne({ email });
        if (!user) {
            return res.status(401).json({ message: 'Invalid credentials' });
        }

        // Check password
        const isMatch = await user.comparePassword(password);
        if (!isMatch) {
            return res.status(401).json({ message: 'Invalid credentials' });
        }

        // Generate JWT token
        const token = jwt.sign(
            { userId: user._id, role: user.role },
            process.env.JWT_SECRET,
            { expiresIn: '24h' }
        );

        // Log thông tin user đăng nhập
        const clientIP = req.ip || req.connection.remoteAddress;
        console.log('\x1b[32m%s\x1b[0m', '=== User Login Success ===');
        console.log('User:', {
            id: user._id.toString(),
            email: user.email,
            name: user.name,
            role: user.role
        });
        console.log('IP Address:', clientIP);
        console.log('Time:', new Date().toLocaleString());
        console.log('\x1b[32m%s\x1b[0m', '========================');

        res.json({
            message: 'Login successful',
            token,
            user: {
                id: user._id,
                email: user.email,
                name: user.name,
                fullName: user.fullName,
                phone: user.phone,
                address: user.address,
                dob: user.dob ? user.dob.toISOString().split('T')[0] : null,
                role: user.role
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error logging in', error: error.message });
    }
};

// Request password reset
exports.requestPasswordReset = async (req, res) => {
    try {
        const { email } = req.body;
        const user = await User.findOne({ email });

        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        // Generate OTP
        const otp = Math.floor(100000 + Math.random() * 900000).toString();
        user.otp = {
            code: otp,
            expires: new Date(Date.now() + 10 * 60 * 1000) // 10 minutes
        };
        await user.save();

        // Send OTP email
        await transporter.sendMail({
            to: email,
            subject: 'Mã Xác Thực Đặt Lại Mật Khẩu - VacTrack',
            text: `Xin chào,
        
        Bạn đã yêu cầu đặt lại mật khẩu trên hệ thống VacTrack – Hệ Thống Tiêm Chủng Số 1 Việt Nam.
        
        Vui lòng sử dụng mã xác thực (OTP) sau để hoàn tất quá trình:
        
        Mã OTP: ${otp}
        
        Mã này có hiệu lực trong vòng 10 phút. Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email hoặc liên hệ với bộ phận hỗ trợ của chúng tôi.
        
        Trân trọng,
        Đội ngũ hỗ trợ VacTrack`
        });
        

        res.json({ message: 'OTP sent to your email' });
        console.log('\x1b[32m[\x1b[0m\x1b[31m\\\x1b[0m\x1b[32m]\x1b[0m OTP sent to ' + email + ' --> ' + otp);

    } catch (error) {
        res.status(500).json({ message: 'Error sending OTP', error: error.message });
        console.log(`\x1b[32m[\x1b[0m\x1b[31m\\\x1b[0m\x1b[32m]\x1b[0m Error sending OTP --> ${email}`);
    }
};

// Verify OTP
exports.verifyOtp = async (req, res) => {
    try {
        const { email, otp } = req.body;
        console.log(`[INFO] OTP verification attempt for: ${email}`);

        const user = await User.findOne({ email });
        if (!user) {
            console.log(`[ERROR] User not found: ${email}`);
            return res.status(404).json({ error: 'User not found' });
        }

        if (!user.otp || !user.otp.code) {
            console.log(`[ERROR] No OTP found for: ${email}`);
            return res.status(400).json({ error: 'No OTP found. Please request a new OTP' });
        }

        if (user.otp.code !== otp) {
            console.log(`[ERROR] Invalid OTP provided for: ${email}`);
            return res.status(400).json({ error: 'Invalid OTP' });
        }

        if (user.otp.expires < new Date()) {
            console.log(`[ERROR] OTP expired for: ${email}`);
            return res.status(400).json({ error: 'OTP has expired. Please request a new OTP' });
        }

        // Generate reset token
        const resetToken = jwt.sign(
            { 
                userId: user._id,
                email: user.email,
                purpose: 'password_reset'
            },
            process.env.JWT_SECRET,
            { expiresIn: '15m' } // Token expires in 15 minutes
        );

        // Clear OTP after successful verification
        user.otp = undefined;
        await user.save();

        console.log(`[INFO] OTP verified successfully for: ${email}`);
        res.json({ 
            message: 'OTP verified successfully',
            resetToken 
        });
    } catch (error) {
        console.log(`[ERROR] Error verifying OTP: ${error.message}`);
        res.status(500).json({ error: 'Error verifying OTP' });
    }
};

// Set new password
exports.setNewPassword = async (req, res) => {
    try {
        const { newPassword } = req.body;
        const token = req.header('Authorization')?.replace('Bearer ', '');

        if (!token) {
            return res.status(401).json({ error: 'No reset token provided' });
        }

        if (!newPassword || newPassword.length < 6) {
            return res.status(400).json({ error: 'New password must be at least 6 characters long' });
        }

        // Verify reset token
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        if (decoded.purpose !== 'password_reset') {
            return res.status(401).json({ error: 'Invalid reset token' });
        }

        const user = await User.findById(decoded.userId);
        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }

        if (user.email !== decoded.email) {
            return res.status(401).json({ error: 'Invalid reset token' });
        }

        // Update password
        user.password = newPassword;
        await user.save();

        console.log(`[INFO] Password reset completed for: ${user.email}`);
        res.json({ message: 'Password has been reset successfully' });
    } catch (error) {
        if (error.name === 'JsonWebTokenError' || error.name === 'TokenExpiredError') {
            return res.status(401).json({ error: 'Invalid or expired reset token' });
        }
        console.log(`[ERROR] Error setting new password: ${error.message}`);
        res.status(500).json({ error: 'Error setting new password' });
    }
};

// Change password
exports.changePassword = async (req, res) => {
    try {
        const { currentPassword, newPassword } = req.body;
        const user = await User.findById(req.user.userId);

        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        // Verify current password
        const isMatch = await user.comparePassword(currentPassword);
        if (!isMatch) {
            return res.status(401).json({ message: 'Current password is incorrect' });
        }

        // Update password
        user.password = newPassword;
        await user.save();

        res.json({ message: 'Password changed successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Error changing password', error: error.message });
    }
};

// Google OAuth callback
exports.googleCallback = async (req, res) => {
    try {
        const { profile } = req;
        let user = await User.findOne({ googleId: profile.id });

        if (!user) {
            user = await User.findOne({ email: profile.emails[0].value });
            if (user) {
                user.googleId = profile.id;
                await user.save();
            } else {
                user = await User.create({
                    email: profile.emails[0].value,
                    name: profile.displayName,
                    googleId: profile.id,
                    isVerified: true
                });
            }
        }

        const token = jwt.sign(
            { userId: user._id },
            process.env.JWT_SECRET,
            { expiresIn: '24h' }
        );

        res.json({
            message: 'Google login successful',
            token,
            user: {
                id: user._id,
                email: user.email,
                name: user.name
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error in Google authentication', error: error.message });
    }
};

// Facebook OAuth callback
exports.facebookCallback = async (req, res) => {
    try {
        const { profile } = req;
        let user = await User.findOne({ facebookId: profile.id });

        if (!user) {
            user = await User.findOne({ email: profile.emails[0].value });
            if (user) {
                user.facebookId = profile.id;
                await user.save();
            } else {
                user = await User.create({
                    email: profile.emails[0].value,
                    name: profile.displayName,
                    facebookId: profile.id,
                    isVerified: true
                });
            }
        }

        const token = jwt.sign(
            { userId: user._id },
            process.env.JWT_SECRET,
            { expiresIn: '24h' }
        );

        res.json({
            message: 'Facebook login successful',
            token,
            user: {
                id: user._id,
                email: user.email,
                name: user.name
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error in Facebook authentication', error: error.message });
    }
};

// Update user profile (for users to update their own profile)
exports.updateProfile = async (req, res) => {
    try {
        const userId = req.user.userId;
        const { fullName, age, dob, address, phone, email } = req.body;
        
        // Không cho phép sửa email
        if (email) {
            return res.status(400).json({ message: 'Email cannot be updated.' });
        }

        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        // Nếu user login Facebook mà chưa có email, bắt buộc nhập email
        if (user.facebookId && (!user.email || user.email === '')) {
            if (!req.body.email || req.body.email === '') {
                return res.status(400).json({ message: 'Email is required for Facebook users.' });
            }
            user.email = req.body.email;
        }

        if (fullName !== undefined) user.fullName = fullName;
        if (age !== undefined) user.age = age;
        if (dob !== undefined) user.dob = dob;
        if (address !== undefined) user.address = address;
        if (phone !== undefined) user.phone = phone;

        await user.save();

        res.json({
            message: 'Profile updated successfully',
            user: {
                id: user._id,
                email: user.email,
                name: user.name,
                fullName: user.fullName,
                age: user.age,
                dob: user.dob,
                address: user.address,
                phone: user.phone
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error updating profile', error: error.message });
    }
};

// Admin update user information
exports.adminUpdateUser = async (req, res) => {
    try {
        // Check if the requester is admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Only admin can update user information' });
        }

        const { userId } = req.params;
        const { fullName, age, dob, address, phone, email, role } = req.body;

        // Validate userId
        if (!userId || !mongoose.Types.ObjectId.isValid(userId)) {
            return res.status(400).json({ message: 'Invalid user ID format' });
        }

        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        // Check if email is being changed and if it's already in use
        if (email && email !== user.email) {
            const existingUser = await User.findOne({ email });
            if (existingUser) {
                return res.status(400).json({ message: 'Email is already in use' });
            }
        }

        // Update user information
        const updateFields = {};
        if (fullName !== undefined) updateFields.fullName = fullName;
        if (age !== undefined) updateFields.age = age;
        if (dob !== undefined) updateFields.dob = dob;
        if (address !== undefined) updateFields.address = address;
        if (phone !== undefined) updateFields.phone = phone;
        if (email !== undefined) updateFields.email = email;
        if (role !== undefined) {
            // Validate role
            if (!['admin', 'doctor', 'user'].includes(role)) {
                return res.status(400).json({ message: 'Invalid role. Must be admin, doctor, or user' });
            }
            // Prevent changing role of another admin
            if (user.role === 'admin' && role !== 'admin' && req.user._id.toString() !== user._id.toString()) {
                return res.status(403).json({ message: 'Cannot change role of another admin' });
            }
            updateFields.role = role;
        }

        // Update user with validated fields
        const updatedUser = await User.findByIdAndUpdate(
            userId,
            { $set: updateFields },
            { new: true, runValidators: true }
        ).select('-password');

        if (!updatedUser) {
            return res.status(404).json({ message: 'User not found after update' });
        }

        console.log(`[INFO] Admin ${req.user.email} updated user ${updatedUser.email}`);
        res.json({
            message: 'User information updated successfully',
            user: updatedUser
        });
    } catch (error) {
        console.log(`[ERROR] Error updating user: ${error.message}`);
        res.status(500).json({ message: 'Error updating user information', error: error.message });
    }
};

// Lấy danh sách tất cả user (chỉ admin, có phân trang và tìm kiếm)
exports.getAllUsers = async (req, res) => {
    try {
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Forbidden: Only admin can view all users.' });
        }
        const { search = '', role, page = 1, limit = 10 } = req.query;
        const query = {};
        if (search) {
            query.$or = [
                { name: { $regex: search, $options: 'i' } },
                { fullName: { $regex: search, $options: 'i' } },
                { email: { $regex: search, $options: 'i' } }
            ];
        }
        if (role) {
            query.role = role;
        }
        const skip = (parseInt(page) - 1) * parseInt(limit);
        const users = await User.find(query)
            .select('-password')
            .skip(skip)
            .limit(parseInt(limit))
            .sort({ createdAt: -1 });
        const total = await User.countDocuments(query);
        res.json({
            users,
            total,
            totalPages: Math.ceil(total / limit),
            currentPage: parseInt(page)
        });
    } catch (error) {
        res.status(500).json({ message: 'Error fetching users', error: error.message });
    }
};

// Lấy toàn bộ user (không phân trang, chỉ admin)
exports.getAllUsersNoPaging = async (req, res) => {
    try {
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Forbidden: Only admin can view all users.' });
        }
        const users = await User.find().select('-password');
        res.json(users);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching users', error: error.message });
    }
};

// Admin create user with role
exports.createUserByAdmin = async (req, res) => {
    try {
        // Check if the requester is admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Only admin can create users with roles' });
        }

        const { email, password, name, fullName, age, dob, address, phone, role } = req.body;

        // Validate role
        if (role && !['admin', 'doctor', 'user'].includes(role)) {
            return res.status(400).json({ message: 'Invalid role. Must be admin, doctor, or user' });
        }

        // Check if user already exists
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(400).json({ message: 'User already exists' });
        }

        // Create new user
        const user = new User({
            email,
            password,
            name,
            fullName,
            age,
            dob,
            address,
            phone,
            role: role || 'user',
            isVerified: true // Auto verify users created by admin
        });

        await user.save();

        res.status(201).json({
            message: 'User created successfully',
            user: {
                id: user._id,
                email: user.email,
                name: user.name,
                role: user.role,
                fullName: user.fullName,
                age: user.age,
                dob: user.dob,
                address: user.address,
                phone: user.phone
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Error creating user', error: error.message });
    }
}; 