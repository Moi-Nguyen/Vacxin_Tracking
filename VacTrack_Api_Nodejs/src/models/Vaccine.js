const mongoose = require('mongoose');

const vaccineSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        trim: true
    },
    manufacturer: {
        type: String,
        required: true,
        trim: true
    },
    description: {
        type: String,
        required: true
    },
    dosage: {
        type: String,
        required: true
    },
    storageTemperature: {
        type: Number,
        required: true
    },
    shelfLife: {
        type: Number, // in months
        required: true
    },
    quantity: {
        type: Number,
        required: true,
        default: 0
    },
    price: {
        type: Number,
        default: 0
    },
    sideEffects: [{
        type: String
    }],
    contraindications: [{
        type: String
    }],
    isActive: {
        type: Boolean,
        default: true
    },
    createdBy: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    }
}, {
    timestamps: true
});

// Index for faster searches
vaccineSchema.index({ name: 'text', manufacturer: 'text' });

module.exports = mongoose.model('Vaccine', vaccineSchema); 