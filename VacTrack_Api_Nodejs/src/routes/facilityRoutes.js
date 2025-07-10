const express = require('express');
const router = express.Router();
const { body } = require('express-validator');
const facilityController = require('../controllers/facilityController');
const auth = require('../middleware/auth');

// Validation middleware
const facilityValidation = [
    body('name').notEmpty().withMessage('Tên cơ sở y tế là bắt buộc'),
    body('address').notEmpty().withMessage('Địa chỉ là bắt buộc'),
    body('phone').notEmpty().withMessage('Số điện thoại là bắt buộc'),
    body('email').isEmail().withMessage('Email không hợp lệ'),
    body('maxBookingsPerDay').optional().isInt({ min: 1 }).withMessage('Số lượng booking tối đa phải là số nguyên dương')
];

// Facility routes
router.post('/', auth, facilityValidation, facilityController.createFacility);
router.get('/', facilityController.getAllFacilities);
router.get('/:id', facilityController.getFacilityById);
router.put('/:id', auth, facilityValidation, facilityController.updateFacility);
router.delete('/:id', auth, facilityController.deleteFacility);
router.put('/:id/toggle', auth, facilityController.toggleFacilityStatus);

module.exports = router; 