const Booking = require('../models/Booking');
const User = require('../models/User');
const Vaccine = require('../models/Vaccine');
const Facility = require('../models/Facility');
const moment = require('moment-timezone');

// Tạo lịch hẹn mới
exports.createBooking = async (req, res) => {
    try {
        const { userId, serviceId, facilityId, date, time, doseNumber = 1, notes } = req.body;

        // Kiểm tra user tồn tại
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy người dùng' 
            });
        }

        // Kiểm tra vaccine tồn tại
        const vaccine = await Vaccine.findById(serviceId);
        if (!vaccine) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy dịch vụ tiêm chủng' 
            });
        }

        // Kiểm tra facility tồn tại
        const facility = await Facility.findById(facilityId);
        if (!facility) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy cơ sở y tế' 
            });
        }

        // Kiểm tra ngày giờ hợp lệ
        const bookingDate = moment.tz(date + ' ' + time, 'Asia/Ho_Chi_Minh');
        const now = moment.tz('Asia/Ho_Chi_Minh');
        
        if (bookingDate.isBefore(now)) {
            return res.status(400).json({ 
                success: false, 
                message: 'Không thể đặt lịch trong quá khứ' 
            });
        }

        // Kiểm tra xem đã có booking nào trong cùng thời gian chưa
        const existingBooking = await Booking.findOne({
            userId,
            date: {
                $gte: moment.tz(date, 'Asia/Ho_Chi_Minh').startOf('day').toDate(),
                $lte: moment.tz(date, 'Asia/Ho_Chi_Minh').endOf('day').toDate()
            },
            status: { $in: ['pending', 'confirmed'] }
        });

        if (existingBooking) {
            return res.status(400).json({ 
                success: false, 
                message: 'Bạn đã có lịch hẹn trong ngày này' 
            });
        }

        // Kiểm tra số lượng booking trong ngày tại facility
        const dailyBookings = await Booking.countDocuments({
            facilityId,
            date: {
                $gte: moment.tz(date, 'Asia/Ho_Chi_Minh').startOf('day').toDate(),
                $lte: moment.tz(date, 'Asia/Ho_Chi_Minh').endOf('day').toDate()
            },
            status: { $in: ['pending', 'confirmed'] }
        });

        if (dailyBookings >= facility.maxBookingsPerDay) {
            return res.status(400).json({ 
                success: false, 
                message: 'Cơ sở này đã đủ lịch hẹn trong ngày' 
            });
        }

        // Tạo booking mới
        const booking = new Booking({
            userId,
            serviceId,
            facilityId,
            facilityName: facility.name,
            date: bookingDate.toDate(),
            time,
            doseNumber,
            notes,
            price: vaccine.price || 0
        });

        await booking.save();

        // Populate thông tin để trả về
        await booking.populate('serviceId', 'name manufacturer');
        await booking.populate('userId', 'name email');

        res.status(201).json({
            success: true,
            message: 'Đặt lịch thành công',
            booking: {
                id: booking._id,
                userId: booking.userId._id,
                serviceId: booking.serviceId._id,
                facilityId: booking.facilityId,
                facilityName: booking.facilityName,
                date: moment(booking.date).format('YYYY-MM-DD'),
                time: booking.time,
                status: booking.status,
                doseNumber: booking.doseNumber,
                price: booking.price
            }
        });

    } catch (error) {
        console.error('Error creating booking:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi tạo lịch hẹn', 
            error: error.message 
        });
    }
};

// Lấy danh sách booking của user
exports.getUserBookings = async (req, res) => {
    try {
        const { userId } = req.params;
        const { status, page = 1, limit = 10 } = req.query;

        // Kiểm tra user tồn tại
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy người dùng' 
            });
        }

        // Xây dựng query
        const query = { userId };
        if (status) {
            query.status = status;
        }

        const skip = (parseInt(page) - 1) * parseInt(limit);
        
        const bookings = await Booking.find(query)
            .populate('serviceId', 'name manufacturer description')
            .populate('doctorId', 'name')
            .sort({ date: -1, createdAt: -1 })
            .skip(skip)
            .limit(parseInt(limit));

        const total = await Booking.countDocuments(query);

        const formattedBookings = bookings.map(booking => ({
            id: booking._id,
            serviceName: booking.serviceId.name,
            facilityName: booking.facilityName,
            date: moment(booking.date).format('YYYY-MM-DD'),
            time: booking.time,
            status: booking.status,
            doseNumber: booking.doseNumber,
            price: booking.price,
            paymentStatus: booking.paymentStatus,
            doctorName: booking.doctorId ? booking.doctorId.name : null
        }));

        res.json({
            success: true,
            bookings: formattedBookings,
            total,
            totalPages: Math.ceil(total / limit),
            currentPage: parseInt(page)
        });

    } catch (error) {
        console.error('Error getting user bookings:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi lấy danh sách lịch hẹn', 
            error: error.message 
        });
    }
};

// Lấy chi tiết booking
exports.getBookingDetail = async (req, res) => {
    try {
        const { id } = req.params;

        const booking = await Booking.findById(id)
            .populate('userId', 'name email phone fullName')
            .populate('serviceId', 'name manufacturer description dosage')
            .populate('doctorId', 'name email');

        if (!booking) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy lịch hẹn' 
            });
        }

        res.json({
            success: true,
            booking: {
                id: booking._id,
                userId: booking.userId,
                serviceId: booking.serviceId,
                facilityId: booking.facilityId,
                facilityName: booking.facilityName,
                date: moment(booking.date).format('YYYY-MM-DD'),
                time: booking.time,
                status: booking.status,
                doseNumber: booking.doseNumber,
                notes: booking.notes,
                price: booking.price,
                paymentStatus: booking.paymentStatus,
                doctorId: booking.doctorId,
                createdAt: booking.createdAt
            }
        });

    } catch (error) {
        console.error('Error getting booking detail:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi lấy chi tiết lịch hẹn', 
            error: error.message 
        });
    }
};

// Cập nhật trạng thái booking
exports.updateBookingStatus = async (req, res) => {
    try {
        const { id } = req.params;
        const { status, notes } = req.body;

        const booking = await Booking.findById(id);
        if (!booking) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy lịch hẹn' 
            });
        }

        // Kiểm tra quyền (chỉ admin hoặc doctor mới được cập nhật)
        if (req.user.role !== 'admin' && req.user.role !== 'doctor') {
            return res.status(403).json({ 
                success: false, 
                message: 'Không có quyền cập nhật lịch hẹn' 
            });
        }

        booking.status = status;
        if (notes) booking.notes = notes;
        if (req.user.role === 'doctor') booking.doctorId = req.user.userId;

        await booking.save();

        res.json({
            success: true,
            message: 'Cập nhật trạng thái lịch hẹn thành công',
            booking: {
                id: booking._id,
                status: booking.status,
                notes: booking.notes
            }
        });

    } catch (error) {
        console.error('Error updating booking status:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi cập nhật trạng thái lịch hẹn', 
            error: error.message 
        });
    }
};

// Hủy booking
exports.cancelBooking = async (req, res) => {
    try {
        const { id } = req.params;

        const booking = await Booking.findById(id);
        if (!booking) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy lịch hẹn' 
            });
        }

        // Kiểm tra quyền (user chỉ được hủy booking của mình, admin có thể hủy mọi booking)
        if (req.user.role !== 'admin' && booking.userId.toString() !== req.user.userId) {
            return res.status(403).json({ 
                success: false, 
                message: 'Không có quyền hủy lịch hẹn này' 
            });
        }

        // Kiểm tra xem có thể hủy không
        if (booking.status === 'completed') {
            return res.status(400).json({ 
                success: false, 
                message: 'Không thể hủy lịch hẹn đã hoàn thành' 
            });
        }

        booking.status = 'cancelled';
        await booking.save();

        res.json({
            success: true,
            message: 'Hủy lịch hẹn thành công'
        });

    } catch (error) {
        console.error('Error canceling booking:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi hủy lịch hẹn', 
            error: error.message 
        });
    }
};

// Lấy tất cả booking (cho admin)
exports.getAllBookings = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ 
                success: false, 
                message: 'Không có quyền truy cập' 
            });
        }

        const { page = 1, limit = 10, status, facilityId, startDate, endDate } = req.query;
        const query = {};

        if (status) query.status = status;
        if (facilityId) query.facilityId = facilityId;
        if (startDate || endDate) {
            query.date = {};
            if (startDate) query.date.$gte = moment.tz(startDate, 'Asia/Ho_Chi_Minh').startOf('day').toDate();
            if (endDate) query.date.$lte = moment.tz(endDate, 'Asia/Ho_Chi_Minh').endOf('day').toDate();
        }

        const skip = (parseInt(page) - 1) * parseInt(limit);
        
        const bookings = await Booking.find(query)
            .populate('userId', 'name email phone')
            .populate('serviceId', 'name manufacturer')
            .populate('doctorId', 'name')
            .sort({ date: -1, createdAt: -1 })
            .skip(skip)
            .limit(parseInt(limit));

        const total = await Booking.countDocuments(query);

        res.json({
            success: true,
            bookings,
            total,
            totalPages: Math.ceil(total / limit),
            currentPage: parseInt(page)
        });

    } catch (error) {
        console.error('Error getting all bookings:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi lấy danh sách lịch hẹn', 
            error: error.message 
        });
    }
}; 