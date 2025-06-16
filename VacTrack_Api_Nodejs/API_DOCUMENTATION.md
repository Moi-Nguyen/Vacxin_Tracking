# VacTrack API Documentation

## Authentication APIs
npm install passport-google-oauth20
### Register
- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "name": "John Doe",
    "fullName": "John Doe",
    "age": 25,
    "dob": "1999-01-01",
    "address": "123 Street",
    "phone": "0123456789",
    "role": "user" // optional, default: "user"
  }
  ```
- **Response**: 
  ```json
  {
    "message": "User registered successfully",
    "token": "jwt_token",
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "John Doe",
      "role": "user"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] New user registration attempt: user@example.com
  [INFO] User registered successfully: user@example.com
  ```

### Login
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Login successful",
    "token": "jwt_token",
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "John Doe",
      "role": "user"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Login attempt: user@example.com
  [INFO] Login successful: user@example.com
  ```

### Login with Google
- **URL**: `/api/auth/google`
- **Method**: `POST`
- **Body**:
  ```json
  {
    "token": "google_oauth_token"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Login successful",
    "token": "jwt_token",
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "John Doe",
      "role": "user"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Google OAuth login attempt
  [INFO] Google OAuth login successful: user@example.com
  ```

### Login with Facebook
- **URL**: `/api/auth/facebook`
- **Method**: `POST`
- **Body**:
  ```json
  {
    "token": "facebook_oauth_token"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Login successful",
    "token": "jwt_token",
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "John Doe",
      "role": "user"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Facebook OAuth login attempt
  [INFO] Facebook OAuth login successful: user@example.com
  ```

### Request Password Reset
- **URL**: `/api/auth/request-reset`
- **Method**: `POST`
- **Body**:
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response**:
  ```json
  {
    "message": "OTP sent to your email"
  }
  ```
- **Console Logs**:
  ```
  [INFO] Password reset requested for: user@example.com
  [INFO] OTP generated and sent to: user@example.com
  ```

### Verify OTP
- **URL**: `/api/auth/verify-otp`
- **Method**: `POST`
- **Body**:
  ```json
  {
    "email": "user@example.com",
    "otp": "123456"
  }
  ```
- **Response**:
  ```json
  {
    "message": "OTP verified successfully",
    "resetToken": "reset_token_for_password_change"
  }
  ```
- **Error Responses**:
  ```json
  {
    "error": "Invalid OTP"
  }
  ```
  ```json
  {
    "error": "OTP has expired"
  }
  ```
- **Console Logs**:
  ```
  [INFO] OTP verification attempt for: user@example.com
  [ERROR] Invalid OTP provided for: user@example.com
  [ERROR] OTP expired for: user@example.com
  [INFO] OTP verified successfully for: user@example.com
  ```

### Set New Password
- **URL**: `/api/auth/set-new-password`
- **Method**: `POST`
- **Headers**: `Authorization: Bearer reset_token`
- **Body**:
  ```json
  {
    "newPassword": "newSecurePassword123"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Password has been reset successfully"
  }
  ```
- **Error Responses**:
  ```json
  {
    "error": "Invalid or expired reset token"
  }
  ```
- **Console Logs**:
  ```
  [INFO] Password reset attempt for: user@example.com
  [ERROR] Invalid reset token provided
  [INFO] Password reset completed for: user@example.com
  ```

### Change Password
- **URL**: `/api/auth/change-password`
- **Method**: `POST`
- **Headers**: `Authorization: Bearer token`
- **Body**:
  ```json
  {
    "currentPassword": "old_password",
    "newPassword": "new_password"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Password changed successfully"
  }
  ```
- **Console Logs**:
  ```
  [INFO] Password change attempt for user: user@example.com
  [INFO] Password changed successfully for: user@example.com
  ```

### Update Profile
- **URL**: `/api/auth/profile`
- **Method**: `PUT`
- **Headers**: `Authorization: Bearer token`
- **Body**:
  ```json
  {
    "fullName": "John Doe",
    "age": 25,
    "dob": "1999-01-01",
    "address": "123 Street",
    "phone": "0123456789"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Profile updated successfully",
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "John Doe",
      "fullName": "John Doe",
      "age": 25,
      "dob": "1999-01-01",
      "address": "123 Street",
      "phone": "0123456789"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Profile update attempt for: user@example.com
  [INFO] Profile updated successfully for: user@example.com
  ```

## Admin APIs

### Create User (Admin only)
- **URL**: `/api/auth/create-user`
- **Method**: `POST`
- **Headers**: `Authorization: Bearer token`
- **Body**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "name": "John Doe",
    "fullName": "John Doe",
    "age": 25,
    "dob": "1999-01-01",
    "address": "123 Street",
    "phone": "0123456789",
    "role": "user"
  }
  ```
- **Response**:
  ```json
  {
    "message": "User created successfully",
    "user": {
      "id": "user_id",
      "email": "user@example.com",
      "name": "John Doe",
      "role": "user",
      "fullName": "John Doe",
      "age": 25,
      "dob": "1999-01-01",
      "address": "123 Street",
      "phone": "0123456789"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Admin creating new user: user@example.com
  [INFO] New user created successfully by admin: user@example.com
  ```

### Update User (Admin only)
- **URL**: `/api/auth/users/:userId`
- **Method**: `PUT`
- **Headers**: `Authorization: Bearer token`
- **Body**:
  ```json
  {
    "fullName": "John Doe",
    "age": 25,
    "dob": "1999-01-01",
    "address": "123 Street",
    "phone": "0123456789",
    "email": "newemail@example.com",
    "role": "user"
  }
  ```
- **Response**:
  ```json
  {
    "message": "User information updated successfully",
    "user": {
      "id": "user_id",
      "email": "newemail@example.com",
      "name": "John Doe",
      "role": "user",
      "fullName": "John Doe",
      "age": 25,
      "dob": "1999-01-01",
      "address": "123 Street",
      "phone": "0123456789"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Admin updating user: user_id
  [INFO] User updated successfully by admin: user_id
  ```

### Get All Users (Admin only)
- **URL**: `/api/auth/users`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer token`
- **Query Parameters**:
  - `search`: Tìm kiếm theo tên hoặc email
  - `role`: Lọc theo role
  - `page`: Số trang (default: 1)
  - `limit`: Số item mỗi trang (default: 10)
- **Response**:
  ```json
  {
    "users": [
      {
        "id": "user_id",
        "email": "user@example.com",
        "name": "John Doe",
        "role": "user",
        "fullName": "John Doe",
        "age": 25,
        "dob": "1999-01-01",
        "address": "123 Street",
        "phone": "0123456789"
      }
    ],
    "total": 100,
    "totalPages": 10,
    "currentPage": 1
  }
  ```
- **Console Logs**:
  ```
  [INFO] Admin fetching users list
  [INFO] Users list fetched successfully
  ```

## Vaccination APIs

### Create Vaccination Record
- **URL**: `/api/vaccinations`
- **Method**: `POST`
- **Headers**: `Authorization: Bearer token`
- **Body**:
  ```json
  {
    "userId": "user_id",
    "vaccineId": "vaccine_id",
    "date": "2024-03-20",
    "location": "Hospital A",
    "doctorId": "doctor_id",
    "notes": "Optional notes"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Tạo lịch sử tiêm chủng thành công",
    "vaccination": {
      "id": "vaccination_id",
      "userId": "user_id",
      "vaccineId": "vaccine_id",
      "date": "2024-03-20",
      "location": "Hospital A",
      "doctorId": "doctor_id",
      "notes": "Optional notes"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Creating new vaccination record for user: user_id
  [INFO] Vaccination record created successfully: vaccination_id
  ```

### Get User Vaccinations
- **URL**: `/api/vaccinations/user/:userId`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer token`
- **Response**:
  ```json
  [
    {
      "id": "vaccination_id",
      "userId": "user_id",
      "vaccineId": {
        "name": "Vaccine Name",
        "manufacturer": "Manufacturer"
      },
      "date": "2024-03-20",
      "location": "Hospital A",
      "doctorId": {
        "name": "Doctor Name"
      },
      "notes": "Optional notes"
    }
  ]
  ```
- **Console Logs**:
  ```
  [INFO] Fetching vaccinations for user: user_id
  [INFO] Vaccinations fetched successfully for user: user_id
  ```

### Get Vaccination Detail
- **URL**: `/api/vaccinations/:id`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer token`
- **Response**:
  ```json
  {
    "id": "vaccination_id",
    "userId": {
      "name": "User Name",
      "email": "user@example.com"
    },
    "vaccineId": {
      "name": "Vaccine Name",
      "manufacturer": "Manufacturer"
    },
    "date": "2024-03-20",
    "location": "Hospital A",
    "doctorId": {
      "name": "Doctor Name"
    },
    "notes": "Optional notes"
  }
  ```
- **Console Logs**:
  ```
  [INFO] Fetching vaccination details: vaccination_id
  [INFO] Vaccination details fetched successfully: vaccination_id
  ```

### Update Vaccination
- **URL**: `/api/vaccinations/:id`
- **Method**: `PUT`
- **Headers**: `Authorization: Bearer token`
- **Body**:
  ```json
  {
    "date": "2024-03-21",
    "location": "Hospital B",
    "doctorId": "new_doctor_id",
    "notes": "Updated notes"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Cập nhật lịch sử tiêm chủng thành công",
    "vaccination": {
      "id": "vaccination_id",
      "date": "2024-03-21",
      "location": "Hospital B",
      "doctorId": "new_doctor_id",
      "notes": "Updated notes"
    }
  }
  ```
- **Console Logs**:
  ```
  [INFO] Updating vaccination record: vaccination_id
  [INFO] Vaccination record updated successfully: vaccination_id
  ```

### Delete Vaccination
- **URL**: `/api/vaccinations/:id`
- **Method**: `DELETE`
- **Headers**: `Authorization: Bearer token`
- **Response**:
  ```json
  {
    "message": "Xóa lịch sử tiêm chủng thành công"
  }
  ```
- **Console Logs**:
  ```
  [INFO] Deleting vaccination record: vaccination_id
  [INFO] Vaccination record deleted successfully: vaccination_id
  ```

## Admin Vaccination APIs

### Get All Vaccinations (Admin only)
- **URL**: `/api/vaccinations/admin/all`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer token`
- **Query Parameters**:
  - `search`: Tìm kiếm theo địa điểm
  - `startDate`: Ngày bắt đầu
  - `endDate`: Ngày kết thúc
  - `page`: Số trang (default: 1)
  - `limit`: Số item mỗi trang (default: 10)
- **Response**:
  ```json
  {
    "vaccinations": [
      {
        "id": "vaccination_id",
        "userId": {
          "name": "User Name",
          "email": "user@example.com"
        },
        "vaccineId": {
          "name": "Vaccine Name",
          "manufacturer": "Manufacturer"
        },
        "date": "2024-03-20",
        "location": "Hospital A",
        "doctorId": {
          "name": "Doctor Name"
        },
        "notes": "Optional notes"
      }
    ],
    "total": 100,
    "totalPages": 10,
    "currentPage": 1
  }
  ```
- **Console Logs**:
  ```
  [INFO] Admin fetching all vaccinations
  [INFO] All vaccinations fetched successfully
  ```

### Get Vaccination Stats (Admin only)
- **URL**: `/api/vaccinations/admin/stats`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer token`
- **Query Parameters**:
  - `startDate`: Ngày bắt đầu
  - `endDate`: Ngày kết thúc
- **Response**:
  ```json
  {
    "vaccineStats": [
      {
        "vaccineName": "Vaccine A",
        "count": 50
      }
    ],
    "monthlyStats": [
      {
        "month": 3,
        "year": 2024,
        "count": 30
      }
    ]
  }
  ```
- **Console Logs**:
  ```
  [INFO] Admin fetching vaccination statistics
  [INFO] Vaccination statistics fetched successfully
  ```

### Get Vaccinations By Vaccine (Admin only)
- **URL**: `/api/vaccinations/admin/vaccine/:vaccineId`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer token`
- **Query Parameters**:
  - `page`: Số trang (default: 1)
  - `limit`: Số item mỗi trang (default: 10)
- **Response**:
  ```json
  {
    "vaccinations": [
      {
        "id": "vaccination_id",
        "userId": {
          "name": "User Name",
          "email": "user@example.com"
        },
        "vaccineId": {
          "name": "Vaccine Name",
          "manufacturer": "Manufacturer"
        },
        "date": "2024-03-20",
        "location": "Hospital A",
        "doctorId": {
          "name": "Doctor Name"
        },
        "notes": "Optional notes"
      }
    ],
    "total": 100,
    "totalPages": 10,
    "currentPage": 1
  }
  ```
- **Console Logs**:
  ```
  [INFO] Admin fetching vaccinations for vaccine: vaccine_id
  [INFO] Vaccinations fetched successfully for vaccine: vaccine_id
  ``` 