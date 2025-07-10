require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const passport = require('passport');
const moment = require('moment');

const authRoutes = require('./src/routes/authRoutes');
const vaccineRoutes = require('./src/routes/vaccineRoutes');
const vaccinationRoutes = require('./src/routes/vaccinationRoutes');
const doctorRoutes = require('./src/routes/doctorRoutes');
const bookingRoutes = require('./src/routes/bookingRoutes');
const facilityRoutes = require('./src/routes/facilityRoutes');

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use(passport.initialize());

// Database connection
mongoose.connect(process.env.MONGODB_URI || 'mongodb://localhost:27017/vactrack', {
    useNewUrlParser: true,
    useUnifiedTopology: true
})
.then(() => console.log('\x1b[32m[\\] Connected to MongoDB\x1b[0m'))
.catch(err => console.error('\x1b[31mMongoDB connection error:\x1b[0m', err));

// Routes
app.use('/api/auth', authRoutes);
app.use('/api/vaccines', vaccineRoutes);
app.use('/api/vaccination', vaccinationRoutes);
app.use('/api/doctor', doctorRoutes);
app.use('/api/booking', bookingRoutes);
app.use('/api/facilities', facilityRoutes);

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ message: 'Something went wrong!' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    //console.log(`Server is running on port ${PORT}`);
    console.log(`
        \x1b[35m┏ - - - - -[\x1b[31m${moment().format('YYYY-MM-DD HH:mm:ss')}\x1b[35m] \x1b[33mVersion: 2.0 \x1b[35m- - - - - ┓
        |                                                       |
        |   \x1b[31m[\x1b[34mSERVER\x1b[31m] \x1b[37mServer is running on port ${PORT}             \x1b[34m|
        |   \x1b[31m[\x1b[34mSERVER\x1b[31m] \x1b[37mTelegram: @agencyluuvong \x1b[36m\x1b[34m                  |
        |                                                       |
        ┗ - - - - - - - - \x1b[91m@Dev By Phucdevz \x1b[34m - - - - - - - - - - ┛\x1b[0m`)
    
}); 