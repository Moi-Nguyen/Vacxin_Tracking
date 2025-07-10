# VacTrack Doctor API Documentation

## 🔐 Xác thực & Đăng nhập

### 1. Đăng nhập bác sĩ
**Endpoint:** `POST /api/auth/login`
**Headers:** Không cần
**Request:**
```json
{
  "email": "doctor@example.com",
  "password": "yourpassword"
}
```
**Response:**
```json
{
  "message": "Login successful",
  "token": "<jwt_token>",
  "user": {
    "id": "...",
    "email": "doctor@example.com",
    "name": "Dr. Example",
    "role": "doctor"
  }
}
```
**Mô tả:** Đăng nhập tài khoản bác sĩ, nhận JWT token để truy cập các API khác.
**Quyền truy cập:** Doctor

---

## 🗓️ Lịch làm việc của bác sĩ

### 2. Xem lịch làm việc
**Endpoint:** `GET /api/doctor/shifts`
**Headers:**
- `Authorization: Bearer <token>`
**Response:**
```json
[
  {
    "id": "...",
    "shiftDate": "2024-06-01",
    "shiftType": "morning",
    "status": "confirmed",
    "startTime": "2024-06-01T07:00:00.000Z",
    "endTime": "2024-06-01T11:00:00.000Z"
  }
]
```
**Mô tả:** Lấy danh sách ca trực của bác sĩ hiện tại.
**Quyền truy cập:** Doctor

### 3. Cập nhật trạng thái ca trực
**Endpoint:** `PUT /api/doctor/shifts/:shiftId`
**Headers:**
- `Authorization: Bearer <token>`
**Request:**
```json
{
  "status": "completed"
}
```
**Response:**
```json
{
  "message": "Cập nhật trạng thái ca trực thành công",
  "shift": { ... }
}
```
**Mô tả:** Cập nhật trạng thái ca trực (pending/confirmed/completed/cancelled).
**Quyền truy cập:** Doctor

---

## 📋 Quản lý booking tiêm chủng

### 4. Xem danh sách booking của bác sĩ
**Endpoint:** `GET /api/booking/doctor/:doctorId`
**Headers:**
- `Authorization: Bearer <token>`
**Response:**
```json
[
  {
    "id": "...",
    "userId": "...",
    "facilityId": "...",
    "date": "2024-06-01",
    "time": "09:00",
    "status": "confirmed",
    "doseNumber": 1
  }
]
```
**Mô tả:** Lấy danh sách các booking mà bác sĩ phụ trách.
**Quyền truy cập:** Doctor

### 5. Cập nhật trạng thái booking
**Endpoint:** `PUT /api/booking/:id/status`
**Headers:**
- `Authorization: Bearer <token>`
**Request:**
```json
{
  "status": "completed"
}
```
**Response:**
```json
{
  "message": "Cập nhật trạng thái booking thành công",
  "booking": { ... }
}
```
**Mô tả:** Bác sĩ cập nhật trạng thái booking (completed/cancelled).
**Quyền truy cập:** Doctor

---

## 💉 Lịch sử tiêm chủng

### 6. Ghi nhận lịch sử tiêm chủng cho bệnh nhân
**Endpoint:** `POST /api/vaccination/history`
**Headers:**
- `Authorization: Bearer <token>`
**Request:**
```json
{
  "userId": "...",
  "vaccineId": "...",
  "date": "2024-06-01",
  "location": "Bệnh viện Gia Định",
  "doctorId": "...",
  "notes": "Không có phản ứng phụ"
}
```
**Response:**
```json
{
  "message": "Ghi nhận lịch sử tiêm chủng thành công",
  "history": { ... }
}
```
**Mô tả:** Bác sĩ ghi nhận một lần tiêm chủng cho bệnh nhân.
**Quyền truy cập:** Doctor

### 7. Xem lịch sử tiêm chủng của bệnh nhân
**Endpoint:** `GET /api/vaccination/history/user/:userId`
**Headers:**
- `Authorization: Bearer <token>`
**Response:**
```json
[
  {
    "id": "...",
    "vaccineId": "...",
    "date": "2024-06-01",
    "location": "Bệnh viện Gia Định",
    "doctorId": "...",
    "notes": "Không có phản ứng phụ"
  }
]
```
**Mô tả:** Xem lịch sử tiêm chủng của một bệnh nhân cụ thể.
**Quyền truy cập:** Doctor 