# VacTrack Admin API Documentation

## 🔐 Authentication APIs

### 1. Đăng nhập Admin
**Endpoint:** `POST /api/auth/login`  
**Headers:** Không cần  
**Request:**
```json
{
  "email": "admin@vactrack.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "email": "admin@vactrack.com",
    "name": "Admin",
    "fullName": "Administrator",
    "phone": "0965563567",
    "address": "TP HCM",
    "dob": "2005-07-04",
    "role": "admin"
  }
}
```

### 2. Tạo user mới (Admin only)
**Endpoint:** `POST /api/auth/create-user`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe",
  "fullName": "Johnathan Doe",
  "age": 25,
  "dob": "1998-01-01",
  "address": "123 Main St, District 1, HCMC",
  "phone": "0987654321",
  "role": "user"
}
```

**Response:**
```json
{
  "message": "User created successfully",
  "user": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d1",
    "email": "user@example.com",
    "name": "John Doe",
    "role": "user",
    "fullName": "Johnathan Doe",
    "age": 25,
    "dob": "1998-01-01",
    "address": "123 Main St, District 1, HCMC",
    "phone": "0987654321"
  }
}
```

### 3. Cập nhật thông tin user (Admin only)
**Endpoint:** `PUT /api/auth/users/:userId`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "fullName": "Johnathan Doe Updated",
  "age": 26,
  "dob": "1998-01-01",
  "address": "456 New St, District 2, HCMC",
  "phone": "0987654321",
  "email": "newemail@example.com",
  "role": "user"
}
```

**Response:**
```json
{
  "message": "User information updated successfully",
  "user": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d1",
    "email": "newemail@example.com",
    "name": "John Doe",
    "role": "user",
    "fullName": "Johnathan Doe Updated",
    "age": 26,
    "dob": "1998-01-01",
    "address": "456 New St, District 2, HCMC",
    "phone": "0987654321"
  }
}
```

### 4. Lấy danh sách tất cả users (Admin only)
**Endpoint:** `GET /api/auth/users`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Query Parameters:**
- `search`: Tìm kiếm theo tên hoặc email
- `role`: Lọc theo role (admin, doctor, user)
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "users": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d0",
      "email": "admin@vactrack.com",
      "name": "Admin",
      "role": "admin",
      "fullName": "Administrator",
      "age": 35,
      "dob": "2005-07-04",
      "address": "TP HCM",
      "phone": "0965563567"
    },
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d1",
      "email": "user@example.com",
      "name": "John Doe",
      "role": "user",
      "fullName": "Johnathan Doe",
      "age": 25,
      "dob": "1998-01-01",
      "address": "123 Main St, District 1, HCMC",
      "phone": "0987654321"
    }
  ],
  "total": 2,
  "totalPages": 1,
  "currentPage": 1
}
```

### 5. Lấy toàn bộ users không phân trang (Admin only)
**Endpoint:** `GET /api/auth/users/all`  
**Headers:** `Authorization: Bearer <admin_token>`

**Response:**
```json
[
  {
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "email": "admin@vactrack.com",
    "name": "Admin",
    "role": "admin",
    "fullName": "Administrator",
    "age": 35,
    "dob": "2005-07-04",
    "address": "TP HCM",
    "phone": "0965563567"
  }
]
```

## 💉 Vaccine Management APIs

### 6. Tạo vaccine mới (Admin only)
**Endpoint:** `POST /api/vaccines`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "name": "COVID-19 Vaccine",
  "manufacturer": "Pfizer",
  "description": "mRNA vaccine for COVID-19",
  "dosage": "2 doses, 21 days apart",
  "storageTemperature": -70,
  "shelfLife": 6,
  "quantity": 1000,
  "price": 0,
  "sideEffects": ["Fever", "Fatigue"],
  "contraindications": ["Severe allergic reactions"]
}
```

**Response:**
```json
{
  "message": "Vaccine created successfully",
  "vaccine": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d2",
    "name": "COVID-19 Vaccine",
    "manufacturer": "Pfizer",
    "description": "mRNA vaccine for COVID-19",
    "dosage": "2 doses, 21 days apart",
    "storageTemperature": -70,
    "shelfLife": 6,
    "quantity": 1000,
    "price": 0,
    "sideEffects": ["Fever", "Fatigue"],
    "contraindications": ["Severe allergic reactions"],
    "isActive": true,
    "createdBy": "64f8a1b2c3d4e5f6a7b8c9d0"
  }
}
```

### 7. Cập nhật vaccine (Admin only)
**Endpoint:** `PUT /api/vaccines/:id`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "name": "COVID-19 Vaccine Updated",
  "manufacturer": "Pfizer-BioNTech",
  "description": "Updated mRNA vaccine for COVID-19",
  "dosage": "2 doses, 21 days apart",
  "storageTemperature": -70,
  "shelfLife": 6,
  "quantity": 1500,
  "price": 0,
  "sideEffects": ["Fever", "Fatigue", "Headache"],
  "contraindications": ["Severe allergic reactions", "Pregnancy"]
}
```

**Response:**
```json
{
  "message": "Vaccine updated successfully",
  "vaccine": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d2",
    "name": "COVID-19 Vaccine Updated",
    "manufacturer": "Pfizer-BioNTech",
    "description": "Updated mRNA vaccine for COVID-19",
    "dosage": "2 doses, 21 days apart",
    "storageTemperature": -70,
    "shelfLife": 6,
    "quantity": 1500,
    "price": 0,
    "sideEffects": ["Fever", "Fatigue", "Headache"],
    "contraindications": ["Severe allergic reactions", "Pregnancy"],
    "isActive": true
  }
}
```

### 8. Xóa vaccine (Admin only)
**Endpoint:** `DELETE /api/vaccines/:id`  
**Headers:** `Authorization: Bearer <admin_token>`

**Response:**
```json
{
  "message": "Vaccine deleted successfully"
}
```

### 9. Toggle trạng thái vaccine (Admin only)
**Endpoint:** `PUT /api/vaccines/:id/toggle`  
**Headers:** `Authorization: Bearer <admin_token>`

**Response:**
```json
{
  "message": "Vaccine deactivated successfully",
  "vaccine": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d2",
    "isActive": false
  }
}
```

## 🏥 Facility Management APIs

### 10. Tạo cơ sở y tế mới (Admin only)
**Endpoint:** `POST /api/facilities`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "name": "Bệnh viện Gia Định",
  "address": "1 Nơ Trang Long, Quận Bình Thạnh, TP.HCM",
  "phone": "02838412629",
  "email": "info@bvgiadinh.org.vn",
  "description": "Bệnh viện đa khoa hạng I",
  "operatingHours": {
    "monday": { "open": "07:00", "close": "17:00" },
    "tuesday": { "open": "07:00", "close": "17:00" },
    "wednesday": { "open": "07:00", "close": "17:00" },
    "thursday": { "open": "07:00", "close": "17:00" },
    "friday": { "open": "07:00", "close": "17:00" },
    "saturday": { "open": "07:00", "close": "12:00" },
    "sunday": { "open": "08:00", "close": "12:00" }
  },
  "location": {
    "latitude": 10.762622,
    "longitude": 106.660172
  },
  "maxBookingsPerDay": 50
}
```

**Response:**
```json
{
  "success": true,
  "message": "Tạo cơ sở y tế thành công",
  "facility": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d3",
    "name": "Bệnh viện Gia Định",
    "address": "1 Nơ Trang Long, Quận Bình Thạnh, TP.HCM",
    "phone": "02838412629",
    "email": "info@bvgiadinh.org.vn",
    "description": "Bệnh viện đa khoa hạng I",
    "operatingHours": { ... },
    "location": {
      "latitude": 10.762622,
      "longitude": 106.660172
    },
    "isActive": true,
    "maxBookingsPerDay": 50,
    "createdBy": "64f8a1b2c3d4e5f6a7b8c9d0"
  }
}
```

### 11. Cập nhật cơ sở y tế (Admin only)
**Endpoint:** `PUT /api/facilities/:id`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "name": "Bệnh viện Gia Định Updated",
  "address": "1 Nơ Trang Long, Quận Bình Thạnh, TP.HCM",
  "phone": "02838412629",
  "email": "info@bvgiadinh.org.vn",
  "description": "Bệnh viện đa khoa hạng I - Updated",
  "maxBookingsPerDay": 60
}
```

**Response:**
```json
{
  "success": true,
  "message": "Cập nhật cơ sở y tế thành công",
  "facility": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d3",
    "name": "Bệnh viện Gia Định Updated",
    "address": "1 Nơ Trang Long, Quận Bình Thạnh, TP.HCM",
    "phone": "02838412629",
    "email": "info@bvgiadinh.org.vn",
    "description": "Bệnh viện đa khoa hạng I - Updated",
    "maxBookingsPerDay": 60
  }
}
```

### 12. Xóa cơ sở y tế (Admin only)
**Endpoint:** `DELETE /api/facilities/:id`  
**Headers:** `Authorization: Bearer <admin_token>`

**Response:**
```json
{
  "success": true,
  "message": "Xóa cơ sở y tế thành công"
}
```

### 13. Toggle trạng thái cơ sở y tế (Admin only)
**Endpoint:** `PUT /api/facilities/:id/toggle`  
**Headers:** `Authorization: Bearer <admin_token>`

**Response:**
```json
{
  "success": true,
  "message": "Cơ sở y tế đã vô hiệu hóa thành công",
  "facility": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d3",
    "isActive": false
  }
}
```

## 📅 Booking Management APIs

### 14. Lấy tất cả lịch hẹn (Admin only)
**Endpoint:** `GET /api/booking/admin/all`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Query Parameters:**
- `status`: Lọc theo trạng thái (pending, confirmed, completed, cancelled)
- `facilityId`: Lọc theo cơ sở y tế
- `startDate`: Ngày bắt đầu (YYYY-MM-DD)
- `endDate`: Ngày kết thúc (YYYY-MM-DD)
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "success": true,
  "bookings": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d4",
      "userId": {
        "id": "64f8a1b2c3d4e5f6a7b8c9d1",
        "name": "John Doe",
        "email": "user@example.com",
        "phone": "0987654321"
      },
      "serviceId": {
        "id": "64f8a1b2c3d4e5f6a7b8c9d2",
        "name": "HPV Vaccine",
        "manufacturer": "Merck"
      },
      "facilityId": "64f8a1b2c3d4e5f6a7b8c9d3",
      "facilityName": "Bệnh viện Gia Định",
      "date": "2024-07-01T09:00:00.000Z",
      "time": "09:00",
      "status": "pending",
      "doseNumber": 1,
      "price": 500000,
      "paymentStatus": "pending",
      "doctorId": null,
      "createdAt": "2024-01-15T10:30:00.000Z"
    }
  ],
  "total": 1,
  "totalPages": 1,
  "currentPage": 1
}
```

### 15. Cập nhật trạng thái lịch hẹn (Admin only)
**Endpoint:** `PUT /api/booking/:id/status`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "status": "confirmed",
  "notes": "Đã xác nhận lịch hẹn"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Cập nhật trạng thái lịch hẹn thành công",
  "booking": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d4",
    "status": "confirmed",
    "notes": "Đã xác nhận lịch hẹn"
  }
}
```

## 📊 Vaccination Management APIs

### 16. Lấy tất cả lịch sử tiêm chủng (Admin only)
**Endpoint:** `GET /api/vaccination/admin/all`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Query Parameters:**
- `search`: Tìm kiếm theo địa điểm
- `startDate`: Ngày bắt đầu
- `endDate`: Ngày kết thúc
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "vaccinations": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d5",
      "userId": {
        "name": "John Doe",
        "email": "user@example.com"
      },
      "vaccineId": {
        "name": "HPV Vaccine",
        "manufacturer": "Merck"
      },
      "date": "2024-01-15T09:00:00.000Z",
      "location": "Bệnh viện Gia Định",
      "doctorId": {
        "name": "Dr. Nguyễn Văn A"
      },
      "notes": "Tiêm mũi 1"
    }
  ],
  "total": 1,
  "totalPages": 1,
  "currentPage": 1
}
```

### 17. Lấy thống kê tiêm chủng (Admin only)
**Endpoint:** `GET /api/vaccination/admin/stats`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Query Parameters:**
- `startDate`: Ngày bắt đầu
- `endDate`: Ngày kết thúc

**Response:**
```json
{
  "vaccineStats": [
    {
      "vaccineName": "HPV Vaccine",
      "count": 50
    },
    {
      "vaccineName": "COVID-19 Vaccine",
      "count": 100
    }
  ],
  "monthlyStats": [
    {
      "month": 1,
      "year": 2024,
      "count": 30
    },
    {
      "month": 2,
      "year": 2024,
      "count": 45
    }
  ]
}
```

### 18. Lấy tiêm chủng theo vaccine (Admin only)
**Endpoint:** `GET /api/vaccination/admin/vaccine/:vaccineId`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Query Parameters:**
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "vaccinations": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d5",
      "userId": {
        "name": "John Doe",
        "email": "user@example.com"
      },
      "vaccineId": {
        "name": "HPV Vaccine",
        "manufacturer": "Merck"
      },
      "date": "2024-01-15T09:00:00.000Z",
      "location": "Bệnh viện Gia Định",
      "doctorId": {
        "name": "Dr. Nguyễn Văn A"
      },
      "notes": "Tiêm mũi 1"
    }
  ],
  "total": 1,
  "totalPages": 1,
  "currentPage": 1
}
```

## 📋 Vaccination History Management APIs

### 19. Tạo lịch sử tiêm chủng (Admin only)
**Endpoint:** `POST /api/vaccination`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "userId": "64f8a1b2c3d4e5f6a7b8c9d1",
  "vaccineId": "64f8a1b2c3d4e5f6a7b8c9d2",
  "date": "2024-01-15",
  "location": "Bệnh viện Gia Định",
  "doctorId": "64f8a1b2c3d4e5f6a7b8c9d6",
  "notes": "Tiêm mũi 1"
}
```

**Response:**
```json
{
  "message": "Tạo lịch sử tiêm chủng thành công",
  "vaccination": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d5",
    "userId": "64f8a1b2c3d4e5f6a7b8c9d1",
    "vaccineId": "64f8a1b2c3d4e5f6a7b8c9d2",
    "date": "2024-01-15T00:00:00.000Z",
    "location": "Bệnh viện Gia Định",
    "doctorId": "64f8a1b2c3d4e5f6a7b8c9d6",
    "notes": "Tiêm mũi 1"
  }
}
```

### 20. Cập nhật lịch sử tiêm chủng (Admin only)
**Endpoint:** `PUT /api/vaccination/:id`  
**Headers:** `Authorization: Bearer <admin_token>`  
**Request:**
```json
{
  "date": "2024-01-16",
  "location": "Bệnh viện Thành phố",
  "doctorId": "64f8a1b2c3d4e5f6a7b8c9d7",
  "notes": "Tiêm mũi 1 - Updated"
}
```

**Response:**
```json
{
  "message": "Cập nhật lịch sử tiêm chủng thành công",
  "vaccination": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d5",
    "date": "2024-01-16T00:00:00.000Z",
    "location": "Bệnh viện Thành phố",
    "doctorId": "64f8a1b2c3d4e5f6a7b8c9d7",
    "notes": "Tiêm mũi 1 - Updated"
  }
}
```

### 21. Xóa lịch sử tiêm chủng (Admin only)
**Endpoint:** `DELETE /api/vaccination/:id`  
**Headers:** `Authorization: Bearer <admin_token>`

**Response:**
```json
{
  "message": "Xóa lịch sử tiêm chủng thành công"
}
```

## ❌ Error Responses

### 401 Unauthorized
```json
{
  "message": "No token, authorization denied"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Chỉ admin mới có quyền thực hiện"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Không tìm thấy dữ liệu"
}
```

### 400 Bad Request
```json
{
  "success": false,
  "message": "Dữ liệu không hợp lệ"
}
```

### 500 Server Error
```json
{
  "success": false,
  "message": "Lỗi server",
  "error": "Error details"
}
```

## 📝 Validation Rules

### User Creation/Update Validation
- `email`: Bắt buộc, định dạng email hợp lệ, unique
- `password`: Bắt buộc, tối thiểu 6 ký tự
- `name`: Bắt buộc, string
- `role`: Tùy chọn, enum: ['admin', 'doctor', 'user']
- `fullName`: Tùy chọn, string
- `age`: Tùy chọn, số nguyên dương
- `dob`: Tùy chọn, định dạng YYYY-MM-DD
- `address`: Tùy chọn, string
- `phone`: Tùy chọn, string

### Vaccine Creation/Update Validation
- `name`: Bắt buộc, string
- `manufacturer`: Bắt buộc, string
- `description`: Bắt buộc, string
- `dosage`: Bắt buộc, string
- `storageTemperature`: Bắt buộc, number
- `shelfLife`: Bắt buộc, number
- `quantity`: Bắt buộc, number
- `price`: Tùy chọn, number, mặc định 0
- `sideEffects`: Tùy chọn, array of strings
- `contraindications`: Tùy chọn, array of strings

### Facility Creation/Update Validation
- `name`: Bắt buộc, string
- `address`: Bắt buộc, string
- `phone`: Bắt buộc, string
- `email`: Bắt buộc, định dạng email hợp lệ
- `maxBookingsPerDay`: Tùy chọn, số nguyên dương, mặc định 50

## 🔒 Business Rules

1. **Admin Privileges:**
   - Admin có thể tạo, cập nhật, xóa tất cả users
   - Admin có thể tạo, cập nhật, xóa tất cả vaccines
   - Admin có thể tạo, cập nhật, xóa tất cả facilities
   - Admin có thể xem tất cả bookings và vaccinations
   - Admin có thể cập nhật trạng thái booking

2. **User Management Rules:**
   - Admin không thể thay đổi role của admin khác
   - Email phải unique trong hệ thống
   - Password được hash trước khi lưu

3. **Data Management Rules:**
   - Vaccine có thể được vô hiệu hóa thay vì xóa
   - Facility có thể được vô hiệu hóa thay vì xóa
   - Tất cả thao tác đều được log

4. **Security Rules:**
   - Tất cả API đều yêu cầu admin token
   - Token JWT có hiệu lực 24 giờ
   - Input validation nghiêm ngặt 