const express = require('express');
const router = express.Router();
const passport = require('passport');
const { body } = require('express-validator');
const authController = require('../controllers/authController');
const auth = require('../middleware/auth');

// Validation middleware
const registerValidation = [
    body('email').isEmail().withMessage('Please enter a valid email'),
    body('password').isLength({ min: 6 }).withMessage('Password must be at least 6 characters long'),
    body('name').notEmpty().withMessage('Name is required')
];

const loginValidation = [
    body('email').isEmail().withMessage('Please enter a valid email'),
    body('password').notEmpty().withMessage('Password is required')
];

// Admin create user with role
const createUserValidation = [
    body('email').isEmail().withMessage('Please enter a valid email'),
    body('password').isLength({ min: 6 }).withMessage('Password must be at least 6 characters long'),
    body('name').notEmpty().withMessage('Name is required'),
    body('role').optional().isIn(['admin', 'doctor', 'user']).withMessage('Invalid role')
];

// Routes
router.post('/register', registerValidation, authController.register);
router.post('/login', loginValidation, authController.login);
router.post('/request-reset', authController.requestPasswordReset);
router.post('/verify-otp', authController.verifyOtp);
router.post('/set-new-password', authController.setNewPassword);
router.post('/change-password', auth, authController.changePassword);
router.post('/create-user', auth, createUserValidation, authController.createUserByAdmin);

// Update profile
router.put('/profile', auth, authController.updateProfile);

// Admin update user information
router.put('/users/:userId', auth, authController.adminUpdateUser);

// Lấy danh sách tất cả user (chỉ admin)
router.get('/users', auth, authController.getAllUsers);

// Lấy toàn bộ user (không phân trang, chỉ admin)
router.get('/users/all', auth, authController.getAllUsersNoPaging);

// Google OAuth routes
router.get('/google',
    passport.authenticate('google', { scope: ['profile', 'email'] })
);

router.get('/google/callback',
    passport.authenticate('google', { session: false }),
    authController.googleCallback
);

// Facebook OAuth routes
router.get('/facebook',
    passport.authenticate('facebook', { scope: ['email'] })
);

router.get('/facebook/callback',
    passport.authenticate('facebook', { session: false }),
    authController.facebookCallback
);

module.exports = router; 