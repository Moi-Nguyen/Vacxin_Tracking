const mongoose = require('mongoose');

const bookingSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    serviceId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Vaccine',
        required: true
    },
    facilityId: {
        type: String,
        required: true
    },
    facilityName: {
        type: String,
        required: true
    },
    date: {
        type: Date,
        required: true
    },
    time: {
        type: String,
        required: true
    },
    status: {
        type: String,
        enum: ['pending', 'confirmed', 'completed', 'cancelled'],
        default: 'pending'
    },
    doseNumber: {
        type: Number,
        default: 1
    },
    notes: {
        type: String
    },
    doctorId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    },
    price: {
        type: Number,
        default: 0
    },
    paymentStatus: {
        type: String,
        enum: ['pending', 'paid', 'refunded'],
        default: 'pending'
    }
}, {
    timestamps: true
});

// Indexes for better performance
bookingSchema.index({ userId: 1, date: -1 });
bookingSchema.index({ status: 1, date: 1 });
bookingSchema.index({ facilityId: 1, date: 1, time: 1 });

module.exports = mongoose.model('Booking', bookingSchema); 