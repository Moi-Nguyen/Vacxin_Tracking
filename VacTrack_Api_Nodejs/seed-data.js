require('dotenv').config();
const mongoose = require('mongoose');
const Facility = require('./src/models/Facility');
const Vaccine = require('./src/models/Vaccine');
const User = require('./src/models/User');

// Kết nối database
mongoose.connect(process.env.MONGODB_URI || 'mongodb://localhost:27017/vactrack', {
    useNewUrlParser: true,
    useUnifiedTopology: true
});

// Dữ liệu mẫu cho Facility
const facilities = [
    {
        name: "Bệnh viện Gia Định",
        address: "1 Nơ Trang Long, Quận Bình Thạnh, TP.HCM",
        phone: "02838412629",
        email: "info@bvgiadinh.org.vn",
        description: "Bệnh viện đa khoa hạng I",
        operatingHours: {
            monday: { open: "07:00", close: "17:00" },
            tuesday: { open: "07:00", close: "17:00" },
            wednesday: { open: "07:00", close: "17:00" },
            thursday: { open: "07:00", close: "17:00" },
            friday: { open: "07:00", close: "17:00" },
            saturday: { open: "07:00", close: "12:00" },
            sunday: { open: "08:00", close: "12:00" }
        },
        location: {
            latitude: 10.762622,
            longitude: 106.660172
        },
        maxBookingsPerDay: 50
    },
    {
        name: "Bệnh viện Thành phố",
        address: "125 Lý Chính Thắng, Quận 3, TP.HCM",
        phone: "02839305818",
        email: "info@bvthanhpho.org.vn",
        description: "Bệnh viện đa khoa trung tâm",
        operatingHours: {
            monday: { open: "08:00", close: "18:00" },
            tuesday: { open: "08:00", close: "18:00" },
            wednesday: { open: "08:00", close: "18:00" },
            thursday: { open: "08:00", close: "18:00" },
            friday: { open: "08:00", close: "18:00" },
            saturday: { open: "08:00", close: "12:00" },
            sunday: { open: "08:00", close: "12:00" }
        },
        location: {
            latitude: 10.7829,
            longitude: 106.7000
        },
        maxBookingsPerDay: 40
    },
    {
        name: "Trung tâm Y tế Dự phòng Quận 1",
        address: "59 Nguyễn Thị Minh Khai, Quận 1, TP.HCM",
        phone: "02838224041",
        email: "ytdpq1@tphcm.gov.vn",
        description: "Trung tâm y tế dự phòng",
        operatingHours: {
            monday: { open: "07:30", close: "16:30" },
            tuesday: { open: "07:30", close: "16:30" },
            wednesday: { open: "07:30", close: "16:30" },
            thursday: { open: "07:30", close: "16:30" },
            friday: { open: "07:30", close: "16:30" },
            saturday: { open: "07:30", close: "11:30" },
            sunday: { open: "08:00", close: "11:00" }
        },
        location: {
            latitude: 10.7769,
            longitude: 106.7009
        },
        maxBookingsPerDay: 30
    }
];

// Dữ liệu mẫu cho Vaccine (cập nhật với trường price)
const vaccines = [
    {
        name: "HPV Vaccine",
        manufacturer: "Merck",
        description: "Vaccine phòng ngừa ung thư cổ tử cung và các bệnh liên quan đến HPV",
        dosage: "3 mũi (0, 2, 6 tháng)",
        storageTemperature: 2,
        shelfLife: 36,
        quantity: 100,
        price: 500000,
        sideEffects: [
            "Đau tại chỗ tiêm",
            "Sốt nhẹ",
            "Mệt mỏi",
            "Đau đầu"
        ],
        contraindications: [
            "Dị ứng với thành phần vaccine",
            "Đang mang thai",
            "Suy giảm miễn dịch nặng"
        ]
    },
    {
        name: "COVID-19 Vaccine (Pfizer)",
        manufacturer: "Pfizer-BioNTech",
        description: "Vaccine phòng ngừa COVID-19",
        dosage: "2 mũi (0, 21 ngày)",
        storageTemperature: -70,
        shelfLife: 6,
        quantity: 200,
        price: 0,
        sideEffects: [
            "Đau tại chỗ tiêm",
            "Sốt",
            "Mệt mỏi",
            "Đau cơ"
        ],
        contraindications: [
            "Dị ứng với thành phần vaccine",
            "Phản ứng dị ứng nghiêm trọng với mũi tiêm trước"
        ]
    },
    {
        name: "Influenza Vaccine",
        manufacturer: "Sanofi Pasteur",
        description: "Vaccine phòng ngừa cúm mùa",
        dosage: "1 mũi hàng năm",
        storageTemperature: 2,
        shelfLife: 12,
        quantity: 150,
        price: 200000,
        sideEffects: [
            "Đau tại chỗ tiêm",
            "Sốt nhẹ",
            "Mệt mỏi"
        ],
        contraindications: [
            "Dị ứng với trứng",
            "Dị ứng với thành phần vaccine"
        ]
    },
    {
        name: "Hepatitis B Vaccine",
        manufacturer: "GSK",
        description: "Vaccine phòng ngừa viêm gan B",
        dosage: "3 mũi (0, 1, 6 tháng)",
        storageTemperature: 2,
        shelfLife: 36,
        quantity: 80,
        price: 150000,
        sideEffects: [
            "Đau tại chỗ tiêm",
            "Sốt nhẹ",
            "Mệt mỏi"
        ],
        contraindications: [
            "Dị ứng với thành phần vaccine",
            "Đang bị bệnh cấp tính"
        ]
    }
];

async function seedData() {
    try {
        console.log('Bắt đầu thêm dữ liệu mẫu...');

        // Tạo admin user nếu chưa có
        const adminUser = await User.findOne({ role: 'admin' });
        let adminId;
        
        if (!adminUser) {
            const admin = new User({
                email: 'admin@vactrack.com',
                password: 'admin123',
                name: 'Admin',
                fullName: 'Administrator',
                role: 'admin',
                isVerified: true
            });
            const savedAdmin = await admin.save();
            adminId = savedAdmin._id;
            console.log('Đã tạo admin user:', savedAdmin.email);
        } else {
            adminId = adminUser._id;
            console.log('Admin user đã tồn tại:', adminUser.email);
        }

        // Thêm facilities
        console.log('Đang thêm facilities...');
        for (const facilityData of facilities) {
            const existingFacility = await Facility.findOne({ name: facilityData.name });
            if (!existingFacility) {
                const facility = new Facility({
                    ...facilityData,
                    createdBy: adminId
                });
                await facility.save();
                console.log('Đã tạo facility:', facility.name);
            } else {
                console.log('Facility đã tồn tại:', existingFacility.name);
            }
        }

        // Thêm vaccines
        console.log('Đang thêm vaccines...');
        for (const vaccineData of vaccines) {
            const existingVaccine = await Vaccine.findOne({ name: vaccineData.name });
            if (!existingVaccine) {
                const vaccine = new Vaccine({
                    ...vaccineData,
                    createdBy: adminId
                });
                await vaccine.save();
                console.log('Đã tạo vaccine:', vaccine.name);
            } else {
                console.log('Vaccine đã tồn tại:', existingVaccine.name);
            }
        }

        console.log('Hoàn thành thêm dữ liệu mẫu!');
        console.log('\nDữ liệu đã được tạo:');
        console.log('- Admin user: admin@vactrack.com / admin123');
        console.log('- 3 facilities');
        console.log('- 4 vaccines với giá khác nhau');

    } catch (error) {
        console.error('Lỗi khi thêm dữ liệu mẫu:', error);
    } finally {
        mongoose.connection.close();
        console.log('Đã đóng kết nối database');
    }
}

// Chạy script
seedData(); 