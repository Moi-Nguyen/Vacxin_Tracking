const express = require('express');
const router = express.Router();
const { body } = require('express-validator');
const vaccineController = require('../controllers/vaccineController');
const auth = require('../middleware/auth');

// Validation middleware
const vaccineValidation = [
    body('name').notEmpty().withMessage('Vaccine name is required'),
    body('manufacturer').notEmpty().withMessage('Manufacturer is required'),
    body('description').notEmpty().withMessage('Description is required'),
    body('dosage').notEmpty().withMessage('Dosage is required'),
    body('storageTemperature').isNumeric().withMessage('Storage temperature must be a number'),
    body('shelfLife').isNumeric().withMessage('Shelf life must be a number')
];

// Routes
router.post('/', auth, vaccineValidation, vaccineController.createVaccine);
router.get('/', vaccineController.getAllVaccines);
router.get('/:id', vaccineController.getVaccine);
router.put('/:id', auth, vaccineValidation, vaccineController.updateVaccine);
router.delete('/:id', auth, vaccineController.deleteVaccine);
router.patch('/:id/toggle-status', auth, vaccineController.toggleVaccineStatus);

module.exports = router; 