const express = require('express');
const router = express.Router();
const { body } = require('express-validator');
const doctorController = require('../controllers/doctorController');
const auth = require('../middleware/auth');

// Validation middleware
const shiftValidation = [
    body('shiftDate').isISO8601().withMessage('Ngày không hợp lệ'),
    body('shiftType').isIn(['morning', 'afternoon', 'night']).withMessage('Loại ca không hợp lệ')
];

const completeVaccinationValidation = [
    body('batchNumber').notEmpty().withMessage('Số lô vaccine là bắt buộc'),
    body('sideEffects').optional().isArray().withMessage('Tác dụng phụ phải là mảng')
];

// Routes
router.get('/shifts/bookings', auth, doctorController.getShiftBookings);
router.post('/shifts', auth, shiftValidation, doctorController.registerShift);
router.get('/shifts', auth, doctorController.getMyShifts);
router.delete('/shifts/:shiftId', auth, doctorController.cancelShift);
router.post('/bookings/:bookingId/complete', auth, completeVaccinationValidation, doctorController.completeVaccination);
router.get('/shifts/doctors', auth, doctorController.getDoctorsInShift);

module.exports = router; 