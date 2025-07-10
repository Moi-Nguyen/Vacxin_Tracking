const Facility = require('../models/Facility');

// Tạo cơ sở y tế mới
exports.createFacility = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ 
                success: false, 
                message: 'Chỉ admin mới có quyền tạo cơ sở y tế' 
            });
        }

        const facilityData = {
            ...req.body,
            createdBy: req.user.userId
        };

        const facility = new Facility(facilityData);
        await facility.save();

        res.status(201).json({
            success: true,
            message: 'Tạo cơ sở y tế thành công',
            facility
        });
    } catch (error) {
        console.error('Error creating facility:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi tạo cơ sở y tế', 
            error: error.message 
        });
    }
};

// Lấy danh sách tất cả cơ sở y tế
exports.getAllFacilities = async (req, res) => {
    try {
        const { search, page = 1, limit = 10, isActive } = req.query;
        const query = {};

        if (search) {
            query.$text = { $search: search };
        }

        if (isActive !== undefined) {
            query.isActive = isActive === 'true';
        }

        const skip = (parseInt(page) - 1) * parseInt(limit);
        
        const facilities = await Facility.find(query)
            .populate('createdBy', 'name email')
            .sort({ createdAt: -1 })
            .skip(skip)
            .limit(parseInt(limit));

        const total = await Facility.countDocuments(query);

        res.json({
            success: true,
            facilities,
            total,
            totalPages: Math.ceil(total / limit),
            currentPage: parseInt(page)
        });
    } catch (error) {
        console.error('Error getting facilities:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi lấy danh sách cơ sở y tế', 
            error: error.message 
        });
    }
};

// Lấy chi tiết cơ sở y tế
exports.getFacilityById = async (req, res) => {
    try {
        const facility = await Facility.findById(req.params.id)
            .populate('createdBy', 'name email');

        if (!facility) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy cơ sở y tế' 
            });
        }

        res.json({
            success: true,
            facility
        });
    } catch (error) {
        console.error('Error getting facility:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi lấy thông tin cơ sở y tế', 
            error: error.message 
        });
    }
};

// Cập nhật cơ sở y tế
exports.updateFacility = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ 
                success: false, 
                message: 'Chỉ admin mới có quyền cập nhật cơ sở y tế' 
            });
        }

        const facility = await Facility.findById(req.params.id);
        if (!facility) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy cơ sở y tế' 
            });
        }

        const updatedFacility = await Facility.findByIdAndUpdate(
            req.params.id,
            req.body,
            { new: true, runValidators: true }
        ).populate('createdBy', 'name email');

        res.json({
            success: true,
            message: 'Cập nhật cơ sở y tế thành công',
            facility: updatedFacility
        });
    } catch (error) {
        console.error('Error updating facility:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi cập nhật cơ sở y tế', 
            error: error.message 
        });
    }
};

// Xóa cơ sở y tế
exports.deleteFacility = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ 
                success: false, 
                message: 'Chỉ admin mới có quyền xóa cơ sở y tế' 
            });
        }

        const facility = await Facility.findById(req.params.id);
        if (!facility) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy cơ sở y tế' 
            });
        }

        await facility.deleteOne();

        res.json({
            success: true,
            message: 'Xóa cơ sở y tế thành công'
        });
    } catch (error) {
        console.error('Error deleting facility:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi xóa cơ sở y tế', 
            error: error.message 
        });
    }
};

// Toggle trạng thái cơ sở y tế
exports.toggleFacilityStatus = async (req, res) => {
    try {
        // Kiểm tra quyền admin
        if (req.user.role !== 'admin') {
            return res.status(403).json({ 
                success: false, 
                message: 'Chỉ admin mới có quyền thay đổi trạng thái cơ sở y tế' 
            });
        }

        const facility = await Facility.findById(req.params.id);
        if (!facility) {
            return res.status(404).json({ 
                success: false, 
                message: 'Không tìm thấy cơ sở y tế' 
            });
        }

        facility.isActive = !facility.isActive;
        await facility.save();

        res.json({
            success: true,
            message: `Cơ sở y tế đã ${facility.isActive ? 'kích hoạt' : 'vô hiệu hóa'} thành công`,
            facility
        });
    } catch (error) {
        console.error('Error toggling facility status:', error);
        res.status(500).json({ 
            success: false, 
            message: 'Lỗi khi thay đổi trạng thái cơ sở y tế', 
            error: error.message 
        });
    }
}; 