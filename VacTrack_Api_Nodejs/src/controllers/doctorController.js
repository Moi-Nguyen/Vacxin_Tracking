const DoctorShift = require('../models/DoctorShift');
const VaccinationBooking = require('../models/VaccinationBooking');
const VaccinationHistory = require('../models/VaccinationHistory');
const User = require('../models/User');
const moment = require('moment-timezone');

// Lấy danh sách lịch hẹn trong ca trực
exports.getShiftBookings = async (req, res) => {
    try {
        if (req.user.role !== 'doctor') {
            return res.status(403).json({ message: 'Chỉ bác sĩ mới có quyền truy cập' });
        }

        const { date } = req.query;
        const shiftDate = date ? moment.tz(date, 'Asia/Ho_Chi_Minh').toDate() : moment.tz('Asia/Ho_Chi_Minh').toDate();

        // Tìm ca trực của bác sĩ
        const shift = await DoctorShift.findOne({
            doctorId: req.user.userId,
            shiftDate: {
                $gte: moment.tz(shiftDate, 'Asia/Ho_Chi_Minh').startOf('day').toDate(),
                $lte: moment.tz(shiftDate, 'Asia/Ho_Chi_Minh').endOf('day').toDate()
            },
            status: { $in: ['confirmed', 'completed'] }
        });

        if (!shift) {
            return res.status(404).json({ message: 'Không tìm thấy ca trực cho ngày này' });
        }

        // Lấy danh sách lịch hẹn trong ca trực
        const bookings = await VaccinationBooking.find({
            bookingDate: {
                $gte: shift.startTime,
                $lte: shift.endTime
            },
            status: { $in: ['pending', 'confirmed'] }
        })
        .populate('userId', 'name email phone fullName dob address')
        .populate('vaccineId', 'name manufacturer dosage')
        .sort({ bookingDate: 1 });

        res.json({
            shift,
            bookings
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy danh sách lịch hẹn', error: error.message });
    }
};

// Xác nhận hoàn thành tiêm chủng
exports.completeVaccination = async (req, res) => {
    try {
        if (req.user.role !== 'doctor') {
            return res.status(403).json({ message: 'Chỉ bác sĩ mới có quyền thực hiện' });
        }

        const { bookingId } = req.params;
        const { batchNumber, sideEffects, notes } = req.body;

        const currentTime = moment.tz('Asia/Ho_Chi_Minh').toDate();

        // Kiểm tra ca trực
        const shift = await DoctorShift.findOne({
            doctorId: req.user.userId,
            status: { $in: ['confirmed', 'completed'] },
            startTime: { $lte: currentTime },
            endTime: { $gte: currentTime }
        });

        if (!shift) {
            return res.status(403).json({ message: 'Bạn không có ca trực hiện tại' });
        }

        // Tìm lịch hẹn
        const booking = await VaccinationBooking.findById(bookingId);
        if (!booking) {
            return res.status(404).json({ message: 'Không tìm thấy lịch hẹn' });
        }

        // Kiểm tra xem lịch hẹn có trong ca trực không
        if (booking.bookingDate < shift.startTime || booking.bookingDate > shift.endTime) {
            return res.status(400).json({ message: 'Lịch hẹn không thuộc ca trực hiện tại' });
        }

        // Cập nhật trạng thái lịch hẹn
        booking.status = 'completed';
        await booking.save();

        // Tạo lịch sử tiêm chủng
        const vaccinationHistory = new VaccinationHistory({
            userId: booking.userId,
            vaccineId: booking.vaccineId,
            doseNumber: booking.doseNumber,
            vaccinationDate: booking.bookingDate,
            administeredBy: req.user.userId,
            batchNumber,
            sideEffects,
            notes
        });

        await vaccinationHistory.save();

        res.json({
            message: 'Xác nhận hoàn thành tiêm chủng thành công',
            booking,
            vaccinationHistory
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi xác nhận hoàn thành tiêm chủng', error: error.message });
    }
};

// Đăng ký ca trực
exports.registerShift = async (req, res) => {
    try {
        if (req.user.role !== 'doctor') {
            return res.status(403).json({ message: 'Chỉ bác sĩ mới có quyền thực hiện' });
        }

        const { shiftDate, shiftType } = req.body;
        const date = moment.tz(shiftDate, 'Asia/Ho_Chi_Minh').toDate();

        // Kiểm tra xem đã đăng ký ca này chưa
        const existingShift = await DoctorShift.findOne({
            doctorId: req.user.userId,
            shiftDate: date,
            shiftType
        });

        if (existingShift) {
            return res.status(400).json({ message: 'Bạn đã đăng ký ca này rồi' });
        }

        // Kiểm tra số lượng bác sĩ trong ca
        const doctorsInShift = await DoctorShift.countDocuments({
            shiftDate: date,
            shiftType,
            status: { $in: ['pending', 'confirmed'] }
        });

        if (doctorsInShift >= 10) {
            return res.status(400).json({ 
                message: 'Ca trực này đã đủ số lượng bác sĩ (tối đa 10 bác sĩ/ca)',
                currentDoctors: doctorsInShift
            });
        }

        // Tính thời gian bắt đầu và kết thúc
        let startTime, endTime;

        switch (shiftType) {
            case 'morning':
                startTime = moment.tz(date, 'Asia/Ho_Chi_Minh').set({ hour: 6, minute: 30 }).toDate();
                endTime = moment.tz(date, 'Asia/Ho_Chi_Minh').set({ hour: 11, minute: 30 }).toDate();
                break;
            case 'afternoon':
                startTime = moment.tz(date, 'Asia/Ho_Chi_Minh').set({ hour: 13, minute: 0 }).toDate();
                endTime = moment.tz(date, 'Asia/Ho_Chi_Minh').set({ hour: 17, minute: 0 }).toDate();
                break;
            case 'night':
                startTime = moment.tz(date, 'Asia/Ho_Chi_Minh').set({ hour: 17, minute: 0 }).toDate();
                endTime = moment.tz(date, 'Asia/Ho_Chi_Minh').add(1, 'day').set({ hour: 6, minute: 0 }).toDate();
                break;
            default:
                return res.status(400).json({ message: 'Loại ca không hợp lệ' });
        }

        const shift = new DoctorShift({
            doctorId: req.user.userId,
            shiftDate: date,
            shiftType,
            startTime,
            endTime
        });

        await shift.save();

        res.status(201).json({
            message: 'Đăng ký ca trực thành công',
            shift,
            currentDoctors: doctorsInShift + 1
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi đăng ký ca trực', error: error.message });
    }
};

// Lấy danh sách bác sĩ trong ca trực
exports.getDoctorsInShift = async (req, res) => {
    try {
        if (req.user.role !== 'doctor') {
            return res.status(403).json({ message: 'Chỉ bác sĩ mới có quyền truy cập' });
        }

        const { shiftDate, shiftType } = req.query;
        const date = moment.tz(shiftDate, 'Asia/Ho_Chi_Minh').toDate();

        const doctors = await DoctorShift.find({
            shiftDate: date,
            shiftType,
            status: { $in: ['pending', 'confirmed'] }
        })
        .populate('doctorId', 'name email phone')
        .sort({ createdAt: 1 });

        res.json({
            totalDoctors: doctors.length,
            maxDoctors: 10,
            doctors
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy danh sách bác sĩ trong ca', error: error.message });
    }
};

// Lấy danh sách ca trực của bác sĩ
exports.getMyShifts = async (req, res) => {
    try {
        if (req.user.role !== 'doctor') {
            return res.status(403).json({ message: 'Chỉ bác sĩ mới có quyền truy cập' });
        }

        const { startDate, endDate } = req.query;
        const query = { doctorId: req.user.userId };

        if (startDate && endDate) {
            query.shiftDate = {
                $gte: moment.tz(startDate, 'Asia/Ho_Chi_Minh').toDate(),
                $lte: moment.tz(endDate, 'Asia/Ho_Chi_Minh').toDate()
            };
        }

        const shifts = await DoctorShift.find(query)
            .sort({ shiftDate: 1, startTime: 1 });

        res.json(shifts);
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy danh sách ca trực', error: error.message });
    }
};

// Hủy ca trực
exports.cancelShift = async (req, res) => {
    try {
        if (req.user.role !== 'doctor') {
            return res.status(403).json({ message: 'Chỉ bác sĩ mới có quyền thực hiện' });
        }

        const { shiftId } = req.params;

        const shift = await DoctorShift.findOne({
            _id: shiftId,
            doctorId: req.user.userId
        });

        if (!shift) {
            return res.status(404).json({ message: 'Không tìm thấy ca trực' });
        }

        if (shift.status === 'completed') {
            return res.status(400).json({ message: 'Không thể hủy ca đã hoàn thành' });
        }

        const currentTime = moment.tz('Asia/Ho_Chi_Minh').toDate();
        if (shift.startTime <= currentTime) {
            return res.status(400).json({ message: 'Không thể hủy ca đã bắt đầu' });
        }

        shift.status = 'cancelled';
        await shift.save();

        res.json({
            message: 'Hủy ca trực thành công',
            shift
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi hủy ca trực', error: error.message });
    }
}; 