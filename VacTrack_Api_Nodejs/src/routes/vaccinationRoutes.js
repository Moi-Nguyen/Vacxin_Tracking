const express = require('express');
const router = express.Router();
const { body } = require('express-validator');
const vaccinationController = require('../controllers/vaccinationController');
const auth = require('../middleware/auth');

// Validation middleware
const vaccinationValidation = [
    body('userId').notEmpty().withMessage('User ID is required'),
    body('vaccineId').notEmpty().withMessage('Vaccine ID is required'),
    body('date').isISO8601().withMessage('Invalid date'),
    body('location').notEmpty().withMessage('Location is required'),
    body('doctorId').notEmpty().withMessage('Doctor ID is required')
];

// Routes
router.post('/', auth, vaccinationValidation, vaccinationController.createVaccination);
router.get('/user/:userId', auth, vaccinationController.getUserVaccinations);
router.get('/:id', auth, vaccinationController.getVaccinationDetail);
router.put('/:id', auth, vaccinationController.updateVaccination);
router.delete('/:id', auth, vaccinationController.deleteVaccination);

// Admin routes
router.get('/admin/all', auth, vaccinationController.getAllVaccinations);
router.get('/admin/stats', auth, vaccinationController.getVaccinationStats);
router.get('/admin/vaccine/:vaccineId', auth, vaccinationController.getVaccinationsByVaccine);

module.exports = router; 