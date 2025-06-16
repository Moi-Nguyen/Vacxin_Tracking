const Vaccine = require('../models/Vaccine');

// Create new vaccine
exports.createVaccine = async (req, res) => {
    try {
        const vaccineData = {
            ...req.body,
            createdBy: req.user.userId
        };

        const vaccine = new Vaccine(vaccineData);
        await vaccine.save();

        res.status(201).json({
            message: 'Vaccine created successfully',
            vaccine
        });
    } catch (error) {
        res.status(500).json({ message: 'Error creating vaccine', error: error.message });
    }
};

// Get all vaccines
exports.getAllVaccines = async (req, res) => {
    try {
        const { search, page = 1, limit = 10 } = req.query;
        const query = {};

        if (search) {
            query.$text = { $search: search };
        }

        const vaccines = await Vaccine.find(query)
            .sort({ createdAt: -1 })
            .limit(limit * 1)
            .skip((page - 1) * limit)
            .populate('createdBy', 'name email');

        const count = await Vaccine.countDocuments(query);

        res.json({
            vaccines,
            totalPages: Math.ceil(count / limit),
            currentPage: page
        });
    } catch (error) {
        res.status(500).json({ message: 'Error fetching vaccines', error: error.message });
    }
};

// Get single vaccine
exports.getVaccine = async (req, res) => {
    try {
        const vaccine = await Vaccine.findById(req.params.id)
            .populate('createdBy', 'name email');

        if (!vaccine) {
            return res.status(404).json({ message: 'Vaccine not found' });
        }

        res.json(vaccine);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching vaccine', error: error.message });
    }
};

// Update vaccine
exports.updateVaccine = async (req, res) => {
    try {
        const vaccine = await Vaccine.findById(req.params.id);

        if (!vaccine) {
            return res.status(404).json({ message: 'Vaccine not found' });
        }

        // Check if user is the creator
        if (vaccine.createdBy.toString() !== req.user.userId) {
            return res.status(403).json({ message: 'Not authorized to update this vaccine' });
        }

        const updatedVaccine = await Vaccine.findByIdAndUpdate(
            req.params.id,
            req.body,
            { new: true, runValidators: true }
        );

        res.json({
            message: 'Vaccine updated successfully',
            vaccine: updatedVaccine
        });
    } catch (error) {
        res.status(500).json({ message: 'Error updating vaccine', error: error.message });
    }
};

// Delete vaccine
exports.deleteVaccine = async (req, res) => {
    try {
        const vaccine = await Vaccine.findById(req.params.id);

        if (!vaccine) {
            return res.status(404).json({ message: 'Vaccine not found' });
        }

        // Check if user is the creator
        if (vaccine.createdBy.toString() !== req.user.userId) {
            return res.status(403).json({ message: 'Not authorized to delete this vaccine' });
        }

        await vaccine.remove();

        res.json({ message: 'Vaccine deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Error deleting vaccine', error: error.message });
    }
};

// Toggle vaccine status
exports.toggleVaccineStatus = async (req, res) => {
    try {
        const vaccine = await Vaccine.findById(req.params.id);

        if (!vaccine) {
            return res.status(404).json({ message: 'Vaccine not found' });
        }

        // Check if user is the creator
        if (vaccine.createdBy.toString() !== req.user.userId) {
            return res.status(403).json({ message: 'Not authorized to update this vaccine' });
        }

        vaccine.isActive = !vaccine.isActive;
        await vaccine.save();

        res.json({
            message: `Vaccine ${vaccine.isActive ? 'activated' : 'deactivated'} successfully`,
            vaccine
        });
    } catch (error) {
        res.status(500).json({ message: 'Error updating vaccine status', error: error.message });
    }
}; 