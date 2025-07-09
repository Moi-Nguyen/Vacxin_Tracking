# 🧹 VacTrack Project Cleanup Plan

## 📋 Tổng quan
Dọn dẹp dự án bằng cách xóa các file cũ không sử dụng sau khi đã refactor sang MVVM.

## 🗑️ Files có thể xóa (Old/Non-MVVM)

### 1. Main Activity Files
- ❌ `MainActivity.kt` - Thay thế bằng MainActivityMVVM.kt
- ❌ `UserActivity.kt` - Không cần thiết với Compose
- ❌ `LoginActivity.kt` - Thay thế bằng LoginScreenMVVM

### 2. Navigation Files
- ❌ `AppNavHost.kt` - Thay thế bằng AppNavHostMVVM.kt
- ❌ `UserNavHost.kt` - Đã tích hợp vào AppNavHostMVVM

### 3. Old UI Screens (Non-MVVM)
- ❌ `LoginScreen.kt` - Thay thế bằng LoginScreenMVVM.kt
- ❌ `RegisterScreen.kt` - Thay thế bằng RegisterScreenMVVM.kt
- ❌ `ForgotPasswordScreen.kt` - Thay thế bằng ForgotPasswordScreenMVVM.kt
- ❌ `OtpScreen.kt` - Thay thế bằng OtpScreenMVVM.kt
- ❌ `SetNewPasswordScreen.kt` - Thay thế bằng SetNewPasswordScreenMVVM.kt
- ❌ `ConfirmResetScreen.kt` - Không sử dụng

### 4. Old User Screens (Non-MVVM)
- ❌ `MainScreen.kt` - Thay thế bằng MainScreenMVVM.kt
- ❌ `HomeScreen.kt` - Thay thế bằng HomeScreenMVVM.kt
- ❌ `SelectServiceScreen.kt` - Thay thế bằng SelectServiceScreenMVVM.kt
- ❌ `SelectTimeAndSlotScreen.kt` - Thay thế bằng SelectTimeAndSlotScreenMVVM.kt
- ❌ `AppointmentScreen.kt` - Thay thế bằng AppointmentScreenMVVM.kt
- ❌ `PaymentScreen.kt` - Thay thế bằng PaymentScreenMVVM.kt
- ❌ `BookingSuccessScreen.kt` - Thay thế bằng BookingSuccessScreenMVVM.kt
- ❌ `ProfileScreen.kt` - Thay thế bằng ProfileScreenMVVM.kt
- ❌ `EditProfileScreen.kt` - Thay thế bằng EditProfileScreenMVVM.kt
- ❌ `TrackingBookingScreen.kt` - Thay thế bằng TrackingBookingScreenMVVM.kt

### 5. Unused Components
- ❌ `ui/components/BottomNavigationBar.kt` - Đã sử dụng từ MainScreen

### 6. Documentation Files (Có thể giữ lại)
- ✅ `MVVM_ARCHITECTURE.md` - Giữ lại để tham khảo
- ✅ `MVVM_REFACTOR_COMPLETE.md` - Giữ lại để tham khảo
- ✅ `UI_REFACTOR_FIXES.md` - Giữ lại để tham khảo

## 📊 Thống kê
- **Tổng số file sẽ xóa**: ~25 files
- **Tiết kiệm dung lượng**: Ước tính 50-100KB
- **Cải thiện maintainability**: Loại bỏ code trùng lặp

## ⚠️ Lưu ý trước khi xóa
1. Đảm bảo tất cả MVVM screens đã hoạt động ổn định
2. Kiểm tra AndroidManifest.xml không reference các file cũ
3. Backup dự án trước khi xóa
4. Test lại toàn bộ flow sau khi xóa

## 🎯 Lợi ích sau khi dọn dẹp
1. **Giảm confusion**: Chỉ còn MVVM files
2. **Dễ maintain**: Không có code trùng lặp
3. **Tăng performance**: Ít file hơn = build nhanh hơn
4. **Clean architecture**: Chỉ giữ lại code cần thiết 