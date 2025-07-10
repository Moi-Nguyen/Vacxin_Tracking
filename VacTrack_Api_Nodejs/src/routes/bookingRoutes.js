const express = require('express');
const router = express.Router();
const { body } = require('express-validator');
const bookingController = require('../controllers/bookingController');
const auth = require('../middleware/auth');

// Validation middleware
const createBookingValidation = [
    body('userId').notEmpty().withMessage('User ID là bắt buộc'),
    body('serviceId').notEmpty().withMessage('Service ID là bắt buộc'),
    body('facilityId').notEmpty().withMessage('Facility ID là bắt buộc'),
    body('date').isISO8601().withMessage('Ngày không hợp lệ'),
    body('time').matches(/^([01]?[0-9]|2[0-3]):[0-5][0-9]$/).withMessage('Thời gian không hợp lệ (HH:MM)'),
    body('doseNumber').optional().isInt({ min: 1 }).withMessage('Số mũi tiêm phải là số nguyên dương')
];

const updateBookingValidation = [
    body('status').optional().isIn(['pending', 'confirmed', 'completed', 'cancelled']).withMessage('Trạng thái không hợp lệ')
];

// Booking routes
router.post('/', auth, createBookingValidation, bookingController.createBooking);
router.get('/user/:userId', auth, bookingController.getUserBookings);
router.get('/:id', auth, bookingController.getBookingDetail);
router.put('/:id/status', auth, updateBookingValidation, bookingController.updateBookingStatus);
router.delete('/:id', auth, bookingController.cancelBooking);

// Admin routes
router.get('/admin/all', auth, bookingController.getAllBookings);

module.exports = router; 