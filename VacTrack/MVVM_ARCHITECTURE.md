# VacTrack MVVM Architecture

## Tổng quan

Dự án VacTrack đã được refactor hoàn toàn để sử dụng kiến trúc **MVVM (Model-View-ViewModel)** chuẩn, giúp tách biệt logic nghiệp vụ khỏi UI và dễ dàng test, maintain.

## ✅ Trạng thái Refactor

### Đã hoàn thành:
- ✅ **Data Layer**: Models và Repositories
- ✅ **ViewModel Layer**: Tất cả ViewModels
- ✅ **UI Layer**: Tất cả màn hình đã được refactor sang MVVM
- ✅ **Navigation**: AppNavHostMVVM
- ✅ **MainActivity**: MainActivityMVVM
- ✅ **Auth Flow**: Login, Register, ForgotPassword, OTP, SetNewPassword
- ✅ **User Flow**: Profile, EditProfile, Home, Main, Appointment, Payment, BookingSuccess, TrackingBooking

## Cấu trúc thư mục MVVM

```
app/src/main/java/com/uth/vactrack/
├── data/
│   ├── model/           # Data classes và entities
│   │   ├── User.kt ✅
│   │   ├── Service.kt ✅
│   │   ├── Appointment.kt ✅
│   │   └── AuthRequest.kt ✅
│   └── repository/      # Repository layer
│       ├── AuthRepository.kt ✅
│       ├── ServiceRepository.kt ✅
│       └── AppointmentRepository.kt ✅
├── ui/
│   ├── viewmodel/       # ViewModel layer
│   │   ├── AuthViewModel.kt ✅
│   │   ├── ServiceViewModel.kt ✅
│   │   ├── AppointmentViewModel.kt ✅
│   │   ├── SharedViewModel.kt ✅
│   │   ├── EditProfileViewModel.kt ✅
│   │   ├── HomeViewModel.kt ✅
│   │   ├── PaymentViewModel.kt ✅
│   │   ├── BookingSuccessViewModel.kt ✅
│   │   ├── TrackingBookingViewModel.kt ✅
│   │   ├── ForgotPasswordViewModel.kt ✅
│   │   ├── OtpViewModel.kt ✅
│   │   └── SetNewPasswordViewModel.kt ✅
│   ├── UILogin/         # UI components (MVVM)
│   │   ├── LoginScreenMVVM.kt ✅
│   │   ├── RegisterScreenMVVM.kt ✅
│   │   ├── ForgotPasswordScreenMVVM.kt ✅
│   │   ├── OtpScreenMVVM.kt ✅
│   │   ├── SetNewPasswordScreenMVVM.kt ✅
│   │   └── AppNavHostMVVM.kt ✅
│   ├── UIUser/          # UI components (MVVM)
│   │   ├── MainScreenMVVM.kt ✅
│   │   ├── HomeScreenMVVM.kt ✅
│   │   ├── SelectServiceScreenMVVM.kt ✅
│   │   ├── ProfileScreenMVVM.kt ✅
│   │   ├── EditProfileScreenMVVM.kt ✅
│   │   ├── AppointmentScreenMVVM.kt ✅
│   │   ├── PaymentScreenMVVM.kt ✅
│   │   ├── BookingSuccessScreenMVVM.kt ✅
│   │   └── TrackingBookingScreenMVVM.kt ✅
│   └── theme/
├── MainActivityMVVM.kt  ✅
└── config/
    ├── AppConfig.kt
    └── SessionManager.kt
```

## Các Layer trong MVVM

### 1. Model Layer (Data) ✅

#### Data Models
- **User.kt**: Đại diện cho thông tin người dùng
- **Service.kt**: Đại diện cho các dịch vụ tiêm chủng
- **Appointment.kt**: Đại diện cho lịch hẹn
- **AuthRequest.kt**: Các request/response cho authentication

#### Repository Pattern
- **AuthRepository**: Xử lý authentication (login, register, social login)
- **ServiceRepository**: Quản lý danh sách dịch vụ
- **AppointmentRepository**: Quản lý lịch hẹn

### 2. ViewModel Layer ✅

#### ViewModels
- **AuthViewModel**: Quản lý state và logic authentication
- **ServiceViewModel**: Quản lý state và logic dịch vụ
- **AppointmentViewModel**: Quản lý state và logic lịch hẹn
- **SharedViewModel**: Quản lý state toàn cục (user, theme)
- **EditProfileViewModel**: Quản lý state và logic chỉnh sửa profile
- **HomeViewModel**: Quản lý state và logic màn hình home
- **PaymentViewModel**: Quản lý state và logic thanh toán
- **BookingSuccessViewModel**: Quản lý state và logic booking success
- **TrackingBookingViewModel**: Quản lý state và logic tracking booking
- **ForgotPasswordViewModel**: Quản lý state và logic quên mật khẩu
- **OtpViewModel**: Quản lý state và logic OTP
- **SetNewPasswordViewModel**: Quản lý state và logic đặt mật khẩu mới

#### State Management
Sử dụng **StateFlow** để reactive state management:
```kotlin
data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val message: String? = null
)
```

### 3. View Layer (UI) ✅

#### Compose UI với MVVM
- **LoginScreenMVVM**: Màn hình đăng nhập với MVVM
- **RegisterScreenMVVM**: Màn hình đăng ký với MVVM
- **ForgotPasswordScreenMVVM**: Màn hình quên mật khẩu với MVVM
- **OtpScreenMVVM**: Màn hình OTP với MVVM
- **SetNewPasswordScreenMVVM**: Màn hình đặt mật khẩu mới với MVVM
- **MainScreenMVVM**: Màn hình chính với MVVM
- **HomeScreenMVVM**: Màn hình home với MVVM
- **SelectServiceScreenMVVM**: Màn hình chọn dịch vụ với MVVM
- **ProfileScreenMVVM**: Màn hình profile với MVVM
- **EditProfileScreenMVVM**: Màn hình chỉnh sửa profile với MVVM
- **AppointmentScreenMVVM**: Màn hình lịch hẹn với MVVM
- **PaymentScreenMVVM**: Màn hình thanh toán với MVVM
- **BookingSuccessScreenMVVM**: Màn hình booking thành công với MVVM
- **TrackingBookingScreenMVVM**: Màn hình tracking booking với MVVM
- **AppNavHostMVVM**: Navigation với MVVM

## Lợi ích của MVVM

### 1. Separation of Concerns ✅
- **Model**: Data và business logic
- **View**: UI components
- **ViewModel**: State management và UI logic

### 2. Testability ✅
- ViewModels có thể test độc lập
- Repository pattern dễ mock
- UI logic tách biệt khỏi business logic

### 3. Maintainability ✅
- Code có cấu trúc rõ ràng
- Dễ dàng thêm tính năng mới
- Dễ dàng refactor

### 4. Reusability ✅
- ViewModels có thể reuse
- Repository pattern cho data access
- Shared state management

## Cách sử dụng

### 1. Trong Compose UI với MVVM
```kotlin
@Composable
fun LoginScreenMVVM(
    authViewModel: AuthViewModel = viewModel(),
    sharedViewModel: SharedViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    
    LaunchedEffect(authState) {
        if (authState.isLoggedIn) {
            // Navigate to main screen
        }
    }
    
    // UI components
}
```

### 2. Trong ViewModel
```kotlin
class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            // Call repository
        }
    }
}
```

### 3. Trong Repository
```kotlin
class AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            // API call logic
        }
    }
}
```

## Dependencies

### MVVM Dependencies ✅
```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
```

## Best Practices

### 1. State Management ✅
- Sử dụng **StateFlow** thay vì LiveData
- **collectAsStateWithLifecycle()** trong Compose
- Single source of truth cho state

### 2. Error Handling ✅
- **Result<T>** pattern trong Repository
- Error state trong ViewModel
- User-friendly error messages

### 3. Coroutines ✅
- **viewModelScope** cho ViewModel
- **Dispatchers.IO** cho network calls
- Proper exception handling

### 4. Dependency Injection ✅
- Repository injection vào ViewModel
- ViewModel injection vào Compose
- Shared ViewModel cho global state

## Migration Guide

### Từ cũ sang MVVM ✅

1. **Tạo Data Models** ✅
   - Chuyển đổi data classes
   - Định nghĩa entities

2. **Tạo Repositories** ✅
   - Tách logic API calls
   - Implement Repository pattern

3. **Tạo ViewModels** ✅
   - State management
   - Business logic

4. **Cập nhật UI** ✅
   - Sử dụng StateFlow
   - Observe state changes

## Testing

### ViewModel Testing
```kotlin
@Test
fun `login with valid credentials should update state`() {
    // Given
    val email = "test@example.com"
    val password = "password"
    
    // When
    viewModel.login(email, password)
    
    // Then
    assertTrue(viewModel.authState.value.isLoading)
}
```

### Repository Testing
```kotlin
@Test
fun `login should return success result`() = runTest {
    // Given
    val email = "test@example.com"
    val password = "password"
    
    // When
    val result = repository.loginWithEmail(email, password)
    
    // Then
    assertTrue(result.isSuccess)
}
```

## Files đã được tạo/refactor

### Data Layer ✅
- `data/model/User.kt`
- `data/model/Service.kt`
- `data/model/Appointment.kt`
- `data/model/AuthRequest.kt`
- `data/repository/AuthRepository.kt`
- `data/repository/ServiceRepository.kt`
- `data/repository/AppointmentRepository.kt`

### ViewModel Layer ✅
- `ui/viewmodel/AuthViewModel.kt`
- `ui/viewmodel/ServiceViewModel.kt`
- `ui/viewmodel/AppointmentViewModel.kt`
- `ui/viewmodel/SharedViewModel.kt`
- `ui/viewmodel/EditProfileViewModel.kt`
- `ui/viewmodel/HomeViewModel.kt`
- `ui/viewmodel/PaymentViewModel.kt`
- `ui/viewmodel/BookingSuccessViewModel.kt`
- `ui/viewmodel/TrackingBookingViewModel.kt`
- `ui/viewmodel/ForgotPasswordViewModel.kt`
- `ui/viewmodel/OtpViewModel.kt`
- `ui/viewmodel/SetNewPasswordViewModel.kt`

### UI Layer ✅
- `ui/UILogin/LoginScreenMVVM.kt`
- `ui/UILogin/RegisterScreenMVVM.kt`
- `ui/UILogin/ForgotPasswordScreenMVVM.kt`
- `ui/UILogin/OtpScreenMVVM.kt`
- `ui/UILogin/SetNewPasswordScreenMVVM.kt`
- `ui/UILogin/AppNavHostMVVM.kt`
- `ui/UIUser/MainScreenMVVM.kt`
- `ui/UIUser/HomeScreenMVVM.kt`
- `ui/UIUser/SelectServiceScreenMVVM.kt`
- `ui/UIUser/ProfileScreenMVVM.kt`
- `ui/UIUser/EditProfileScreenMVVM.kt`
- `ui/UIUser/AppointmentScreenMVVM.kt`
- `ui/UIUser/PaymentScreenMVVM.kt`
- `ui/UIUser/BookingSuccessScreenMVVM.kt`
- `ui/UIUser/TrackingBookingScreenMVVM.kt`
- `MainActivityMVVM.kt`

## Kết luận

Dự án VacTrack đã được refactor hoàn toàn sang kiến trúc MVVM chuẩn. Tất cả các màn hình đã được tách biệt logic nghiệp vụ khỏi UI, sử dụng StateFlow để quản lý state, và tuân thủ các best practices của MVVM. Dự án hiện tại có cấu trúc rõ ràng, dễ maintain, test và mở rộng. 