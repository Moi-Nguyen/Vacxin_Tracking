const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

const userSchema = new mongoose.Schema({
    email: {
        type: String,
        required: true,
        unique: true,
        trim: true,
        lowercase: true
    },
    password: {
        type: String,
        required: function() {
            return !this.googleId && !this.facebookId;
        }
    },
    name: {
        type: String,
        required: true
    },
    fullName: {
        type: String
    },
    age: {
        type: Number
    },
    dob: {
        type: Date
    },
    address: {
        type: String
    },
    phone: {
        type: String
    },
    googleId: String,
    facebookId: String,
    resetPasswordToken: String,
    resetPasswordExpires: Date,
    otp: {
        code: String,
        expires: Date
    },
    isVerified: {
        type: Boolean,
        default: false
    },
    role: {
        type: String,
        enum: ['admin', 'doctor', 'user'],
        default: 'user'
    }
}, {
    timestamps: true
});

// Hash password before saving
userSchema.pre('save', async function(next) {
    if (!this.isModified('password')) return next();
    
    try {
        const salt = await bcrypt.genSalt(10);
        this.password = await bcrypt.hash(this.password, salt);
        next();
    } catch (error) {
        next(error);
    }
});

// Method to compare password
userSchema.methods.comparePassword = async function(candidatePassword) {
    return bcrypt.compare(candidatePassword, this.password);
};

module.exports = mongoose.model('User', userSchema); 