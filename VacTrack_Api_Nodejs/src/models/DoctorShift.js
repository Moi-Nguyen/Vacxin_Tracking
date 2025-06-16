const mongoose = require('mongoose');

const doctorShiftSchema = new mongoose.Schema({
    doctorId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    shiftDate: {
        type: Date,
        required: true
    },
    shiftType: {
        type: String,
        enum: ['morning', 'afternoon', 'night'],
        required: true
    },
    status: {
        type: String,
        enum: ['pending', 'confirmed', 'completed', 'cancelled'],
        default: 'pending'
    },
    startTime: {
        type: Date,
        required: true
    },
    endTime: {
        type: Date,
        required: true
    },
    notes: String,
    createdAt: {
        type: Date,
        default: Date.now
    }
});

// Tạo index để tìm kiếm nhanh
doctorShiftSchema.index({ doctorId: 1, shiftDate: 1, shiftType: 1 });
doctorShiftSchema.index({ shiftDate: 1, shiftType: 1, status: 1 });

module.exports = mongoose.model('DoctorShift', doctorShiftSchema); 