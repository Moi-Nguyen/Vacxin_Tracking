const VaccinationHistory = require('../models/VaccinationHistory');
const User = require('../models/User');
const Vaccine = require('../models/Vaccine');

// Tạo lịch sử tiêm chủng mới
exports.createVaccination = async (req, res) => {
    try {
        const { userId, vaccineId, date, location, doctorId, notes } = req.body;

        // Kiểm tra user tồn tại
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: 'Không tìm thấy người dùng' });
        }

        // Kiểm tra vaccine tồn tại
        const vaccine = await Vaccine.findById(vaccineId);
        if (!vaccine) {
            return res.status(404).json({ message: 'Không tìm thấy vaccine' });
        }

        // Tạo lịch sử tiêm chủng mới
        const vaccination = new VaccinationHistory({
            userId,
            vaccineId,
            date,
            location,
            doctorId,
            notes
        });

        await vaccination.save();

        res.status(201).json({
            message: 'Tạo lịch sử tiêm chủng thành công',
            vaccination
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi tạo lịch sử tiêm chủng', error: error.message });
    }
};

// Lấy danh sách lịch sử tiêm chủng của một người dùng
exports.getUserVaccinations = async (req, res) => {
    try {
        const { userId } = req.params;
        const vaccinations = await VaccinationHistory.find({ userId })
            .populate('vaccineId', 'name manufacturer')
            .populate('doctorId', 'name')
            .sort({ date: -1 });

        res.json(vaccinations);
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy lịch sử tiêm chủng', error: error.message });
    }
};

// Cập nhật lịch sử tiêm chủng
exports.updateVaccination = async (req, res) => {
    try {
        const { id } = req.params;
        const { date, location, doctorId, notes } = req.body;

        const vaccination = await VaccinationHistory.findById(id);
        if (!vaccination) {
            return res.status(404).json({ message: 'Không tìm thấy lịch sử tiêm chủng' });
        }

        // Cập nhật thông tin
        if (date) vaccination.date = date;
        if (location) vaccination.location = location;
        if (doctorId) vaccination.doctorId = doctorId;
        if (notes !== undefined) vaccination.notes = notes;

        await vaccination.save();

        res.json({
            message: 'Cập nhật lịch sử tiêm chủng thành công',
            vaccination
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi cập nhật lịch sử tiêm chủng', error: error.message });
    }
};

// Xóa lịch sử tiêm chủng
exports.deleteVaccination = async (req, res) => {
    try {
        const { id } = req.params;

        const vaccination = await VaccinationHistory.findById(id);
        if (!vaccination) {
            return res.status(404).json({ message: 'Không tìm thấy lịch sử tiêm chủng' });
        }

        await vaccination.deleteOne();

        res.json({ message: 'Xóa lịch sử tiêm chủng thành công' });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi xóa lịch sử tiêm chủng', error: error.message });
    }
};

// Lấy chi tiết một lịch sử tiêm chủng
exports.getVaccinationDetail = async (req, res) => {
    try {
        const { id } = req.params;

        const vaccination = await VaccinationHistory.findById(id)
            .populate('vaccineId', 'name manufacturer')
            .populate('doctorId', 'name')
            .populate('userId', 'name email');

        if (!vaccination) {
            return res.status(404).json({ message: 'Không tìm thấy lịch sử tiêm chủng' });
        }

        res.json(vaccination);
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy chi tiết lịch sử tiêm chủng', error: error.message });
    }
};

// Lấy tất cả lịch sử tiêm chủng (cho admin)
exports.getAllVaccinations = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Không có quyền truy cập' });
        }

        const { page = 1, limit = 10, search = '', startDate, endDate } = req.query;
        const query = {};

        // Tìm kiếm theo tên người dùng hoặc địa điểm
        if (search) {
            query.$or = [
                { location: { $regex: search, $options: 'i' } }
            ];
        }

        // Lọc theo khoảng thời gian
        if (startDate || endDate) {
            query.date = {};
            if (startDate) query.date.$gte = new Date(startDate);
            if (endDate) query.date.$lte = new Date(endDate);
        }

        const skip = (parseInt(page) - 1) * parseInt(limit);
        const vaccinations = await VaccinationHistory.find(query)
            .populate('userId', 'name email')
            .populate('vaccineId', 'name manufacturer')
            .populate('doctorId', 'name')
            .skip(skip)
            .limit(parseInt(limit))
            .sort({ date: -1 });

        const total = await VaccinationHistory.countDocuments(query);

        res.json({
            vaccinations,
            total,
            totalPages: Math.ceil(total / limit),
            currentPage: parseInt(page)
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy danh sách lịch sử tiêm chủng', error: error.message });
    }
};

// Lấy thống kê tiêm chủng (cho admin)
exports.getVaccinationStats = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Không có quyền truy cập' });
        }

        const { startDate, endDate } = req.query;
        const query = {};

        // Lọc theo khoảng thời gian
        if (startDate || endDate) {
            query.date = {};
            if (startDate) query.date.$gte = new Date(startDate);
            if (endDate) query.date.$lte = new Date(endDate);
        }

        // Thống kê theo vaccine
        const vaccineStats = await VaccinationHistory.aggregate([
            { $match: query },
            {
                $group: {
                    _id: '$vaccineId',
                    count: { $sum: 1 }
                }
            },
            {
                $lookup: {
                    from: 'vaccines',
                    localField: '_id',
                    foreignField: '_id',
                    as: 'vaccine'
                }
            },
            {
                $project: {
                    vaccineName: { $arrayElemAt: ['$vaccine.name', 0] },
                    count: 1
                }
            }
        ]);

        // Thống kê theo tháng
        const monthlyStats = await VaccinationHistory.aggregate([
            { $match: query },
            {
                $group: {
                    _id: {
                        year: { $year: '$date' },
                        month: { $month: '$date' }
                    },
                    count: { $sum: 1 }
                }
            },
            {
                $project: {
                    _id: 0,
                    month: '$_id.month',
                    year: '$_id.year',
                    count: 1
                }
            },
            { $sort: { year: 1, month: 1 } }
        ]);

        res.json({
            vaccineStats,
            monthlyStats
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy thống kê tiêm chủng', error: error.message });
    }
};

// Lấy danh sách lịch sử tiêm chủng theo vaccine (cho admin)
exports.getVaccinationsByVaccine = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ message: 'Không có quyền truy cập' });
        }

        const { vaccineId } = req.params;
        const { page = 1, limit = 10 } = req.query;

        const skip = (parseInt(page) - 1) * parseInt(limit);
        const vaccinations = await VaccinationHistory.find({ vaccineId })
            .populate('userId', 'name email')
            .populate('vaccineId', 'name manufacturer')
            .populate('doctorId', 'name')
            .skip(skip)
            .limit(parseInt(limit))
            .sort({ date: -1 });

        const total = await VaccinationHistory.countDocuments({ vaccineId });

        res.json({
            vaccinations,
            total,
            totalPages: Math.ceil(total / limit),
            currentPage: parseInt(page)
        });
    } catch (error) {
        res.status(500).json({ message: 'Lỗi khi lấy danh sách lịch sử tiêm chủng theo vaccine', error: error.message });
    }
}; 