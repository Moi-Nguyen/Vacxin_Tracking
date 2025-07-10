# VacTrack User API Documentation

## 🔐 Authentication APIs

### 1. Đăng ký tài khoản
**Endpoint:** `POST /api/auth/register`  
**Headers:** Không cần  
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
  "phone": "0987654321"
}
```

**Response:**
```json
{
  "message": "User registered successfully",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "email": "user@example.com",
    "name": "John Doe",
    "role": "user"
  }
}
```

### 2. Đăng nhập
**Endpoint:** `POST /api/auth/login`  
**Headers:** Không cần  
**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "email": "user@example.com",
    "name": "John Doe",
    "fullName": "Johnathan Doe",
    "phone": "0987654321",
    "address": "123 Main St, District 1, HCMC",
    "dob": "1998-01-01",
    "role": "user"
  }
}
```

### 3. Quên mật khẩu - Gửi OTP
**Endpoint:** `POST /api/auth/request-reset`  
**Headers:** Không cần  
**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "OTP sent to your email"
}
```

### 4. Xác thực OTP
**Endpoint:** `POST /api/auth/verify-otp`  
**Headers:** Không cần  
**Request:**
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response:**
```json
{
  "message": "OTP verified successfully",
  "resetToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 5. Đặt mật khẩu mới
**Endpoint:** `POST /api/auth/set-new-password`  
**Headers:** `Authorization: Bearer <resetToken>`  
**Request:**
```json
{
  "newPassword": "newSecurePassword123"
}
```

**Response:**
```json
{
  "message": "Password has been reset successfully"
}
```

### 6. Đổi mật khẩu (khi đã đăng nhập)
**Endpoint:** `POST /api/auth/change-password`  
**Headers:** `Authorization: Bearer <token>`  
**Request:**
```json
{
  "currentPassword": "old_password",
  "newPassword": "new_password"
}
```

**Response:**
```json
{
  "message": "Password changed successfully"
}
```

### 7. Cập nhật thông tin cá nhân
**Endpoint:** `PUT /api/auth/profile`  
**Headers:** `Authorization: Bearer <token>`  
**Request:**
```json
{
  "fullName": "Johnathan Doe",
  "age": 26,
  "dob": "1998-01-01",
  "address": "456 New St, District 2, HCMC",
  "phone": "0987654321"
}
```

**Response:**
```json
{
  "message": "Profile updated successfully",
  "user": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "email": "user@example.com",
    "name": "John Doe",
    "fullName": "Johnathan Doe",
    "age": 26,
    "dob": "1998-01-01",
    "address": "456 New St, District 2, HCMC",
    "phone": "0987654321",
    "role": "user"
  }
}
```

## 💉 Vaccine APIs

### 8. Lấy danh sách vaccine
**Endpoint:** `GET /api/vaccines`  
**Headers:** `Authorization: Bearer <token>`  
**Query Parameters:**
- `search`: Tìm kiếm theo tên vaccine
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "vaccines": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d1",
      "name": "HPV Vaccine",
      "manufacturer": "Merck",
      "description": "Vaccine phòng ngừa ung thư cổ tử cung",
      "dosage": "3 mũi (0, 2, 6 tháng)",
      "storageTemperature": 2,
      "shelfLife": 36,
      "quantity": 100,
      "price": 500000,
      "sideEffects": ["Đau tại chỗ tiêm", "Sốt nhẹ"],
      "contraindications": ["Dị ứng với thành phần vaccine"],
      "isActive": true,
      "createdBy": {
        "id": "64f8a1b2c3d4e5f6a7b8c9d0",
        "name": "Admin",
        "email": "admin@example.com"
      }
    }
  ],
  "totalPages": 1,
  "currentPage": 1
}
```

### 9. Lấy chi tiết vaccine
**Endpoint:** `GET /api/vaccines/:id`  
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d1",
  "name": "HPV Vaccine",
  "manufacturer": "Merck",
  "description": "Vaccine phòng ngừa ung thư cổ tử cung và các bệnh liên quan đến HPV",
  "dosage": "3 mũi (0, 2, 6 tháng)",
  "storageTemperature": 2,
  "shelfLife": 36,
  "quantity": 100,
  "price": 500000,
  "sideEffects": ["Đau tại chỗ tiêm", "Sốt nhẹ", "Mệt mỏi"],
  "contraindications": ["Dị ứng với thành phần vaccine", "Đang mang thai"],
  "isActive": true,
  "createdBy": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d0",
    "name": "Admin",
    "email": "admin@example.com"
  }
}
```

## 🏥 Facility APIs

### 10. Lấy danh sách cơ sở y tế
**Endpoint:** `GET /api/facilities`  
**Headers:** Không cần  
**Query Parameters:**
- `search`: Tìm kiếm theo tên hoặc địa chỉ
- `isActive`: Lọc theo trạng thái hoạt động (true/false)
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "success": true,
  "facilities": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d2",
      "name": "Bệnh viện Gia Định",
      "address": "1 Nơ Trang Long, Quận Bình Thạnh, TP.HCM",
      "phone": "02838412629",
      "email": "info@bvgiadinh.org.vn",
      "description": "Bệnh viện đa khoa hạng I",
      "operatingHours": {
        "monday": { "open": "07:00", "close": "17:00" },
        "tuesday": { "open": "07:00", "close": "17:00" }
      },
      "location": {
        "latitude": 10.762622,
        "longitude": 106.660172
      },
      "isActive": true,
      "maxBookingsPerDay": 50
    }
  ],
  "total": 1,
  "totalPages": 1,
  "currentPage": 1
}
```

### 11. Lấy chi tiết cơ sở y tế
**Endpoint:** `GET /api/facilities/:id`  
**Headers:** Không cần

**Response:**
```json
{
  "success": true,
  "facility": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d2",
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
    "isActive": true,
    "maxBookingsPerDay": 50
  }
}
```

## 📅 Booking APIs

### 12. Tạo lịch hẹn mới
**Endpoint:** `POST /api/booking`  
**Headers:** `Authorization: Bearer <token>`  
**Request:**
```json
{
  "userId": "64f8a1b2c3d4e5f6a7b8c9d0",
  "serviceId": "64f8a1b2c3d4e5f6a7b8c9d1",
  "facilityId": "64f8a1b2c3d4e5f6a7b8c9d2",
  "date": "2024-07-01",
  "time": "09:00",
  "doseNumber": 1,
  "notes": "Ghi chú cho lịch hẹn"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đặt lịch thành công",
  "booking": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d3",
    "userId": "64f8a1b2c3d4e5f6a7b8c9d0",
    "serviceId": "64f8a1b2c3d4e5f6a7b8c9d1",
    "facilityId": "64f8a1b2c3d4e5f6a7b8c9d2",
    "facilityName": "Bệnh viện Gia Định",
    "date": "2024-07-01",
    "time": "09:00",
    "status": "pending",
    "doseNumber": 1,
    "price": 500000
  }
}
```

### 13. Lấy danh sách lịch hẹn của user
**Endpoint:** `GET /api/booking/user/:userId`  
**Headers:** `Authorization: Bearer <token>`  
**Query Parameters:**
- `status`: Lọc theo trạng thái (pending, confirmed, completed, cancelled)
- `page`: Số trang (default: 1)
- `limit`: Số item mỗi trang (default: 10)

**Response:**
```json
{
  "success": true,
  "bookings": [
    {
      "id": "64f8a1b2c3d4e5f6a7b8c9d3",
      "serviceName": "HPV Vaccine",
      "facilityName": "Bệnh viện Gia Định",
      "date": "2024-07-01",
      "time": "09:00",
      "status": "pending",
      "doseNumber": 1,
      "price": 500000,
      "paymentStatus": "pending",
      "doctorName": null
    }
  ],
  "total": 1,
  "totalPages": 1,
  "currentPage": 1
}
```

### 14. Lấy chi tiết lịch hẹn
**Endpoint:** `GET /api/booking/:id`  
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "booking": {
    "id": "64f8a1b2c3d4e5f6a7b8c9d3",
    "userId": {
      "id": "64f8a1b2c3d4e5f6a7b8c9d0",
      "name": "John Doe",
      "email": "user@example.com",
      "phone": "0987654321",
      "fullName": "Johnathan Doe"
    },
    "serviceId": {
      "id": "64f8a1b2c3d4e5f6a7b8c9d1",
      "name": "HPV Vaccine",
      "manufacturer": "Merck",
      "description": "Vaccine phòng ngừa ung thư cổ tử cung",
      "dosage": "3 mũi (0, 2, 6 tháng)"
    },
    "facilityId": "64f8a1b2c3d4e5f6a7b8c9d2",
    "facilityName": "Bệnh viện Gia Định",
    "date": "2024-07-01",
    "time": "09:00",
    "status": "pending",
    "doseNumber": 1,
    "notes": "Ghi chú cho lịch hẹn",
    "price": 500000,
    "paymentStatus": "pending",
    "doctorId": null,
    "createdAt": "2024-01-15T10:30:00.000Z"
  }
}
```

### 15. Hủy lịch hẹn
**Endpoint:** `DELETE /api/booking/:id`  
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "message": "Hủy lịch hẹn thành công"
}
```

## 📋 Vaccination History APIs

### 16. Lấy lịch sử tiêm chủng của user
**Endpoint:** `GET /api/vaccination/user/:userId`  
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
[
  {
    "id": "64f8a1b2c3d4e5f6a7b8c9d4",
    "userId": "64f8a1b2c3d4e5f6a7b8c9d0",
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
]
```

### 17. Lấy chi tiết lịch sử tiêm chủng
**Endpoint:** `GET /api/vaccination/:id`  
**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d4",
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
```

## 🔐 OAuth APIs

### 18. Đăng nhập Google
**Endpoint:** `GET /api/auth/google`  
**Headers:** Không cần

### 19. Đăng nhập Facebook
**Endpoint:** `GET /api/auth/facebook`  
**Headers:** Không cần

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
  "message": "Không có quyền truy cập"
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

### Booking Validation
- `userId`: Bắt buộc, phải là ObjectId hợp lệ
- `serviceId`: Bắt buộc, phải là ObjectId hợp lệ
- `facilityId`: Bắt buộc, phải là ObjectId hợp lệ
- `date`: Bắt buộc, định dạng ISO 8601 (YYYY-MM-DD)
- `time`: Bắt buộc, định dạng HH:MM
- `doseNumber`: Tùy chọn, số nguyên dương, mặc định là 1

### Profile Validation
- `fullName`: Tùy chọn, string
- `age`: Tùy chọn, số nguyên dương
- `dob`: Tùy chọn, định dạng YYYY-MM-DD
- `address`: Tùy chọn, string
- `phone`: Tùy chọn, string

## 🔒 Business Rules

1. **Booking Rules:**
   - Không thể đặt lịch trong quá khứ
   - User chỉ được đặt 1 lịch hẹn mỗi ngày
   - Cơ sở y tế có giới hạn số lượng booking mỗi ngày
   - User chỉ được hủy booking của mình

2. **Authentication Rules:**
   - Token JWT có hiệu lực 24 giờ
   - OTP có hiệu lực 10 phút
   - Reset token có hiệu lực 15 phút

3. **Data Access Rules:**
   - User chỉ được xem thông tin của mình
   - User chỉ được cập nhật profile của mình
   - User có thể xem tất cả vaccine và facility 