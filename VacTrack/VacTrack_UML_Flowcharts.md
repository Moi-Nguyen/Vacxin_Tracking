# Sơ Đồ UML và Flowchart - Dự Án VacTrack

## 1. Sơ Đồ UML Class Diagram

```mermaid
classDiagram
    %% Data Models
    class User {
        +String id
        +String email
        +String name
        +String fullName
        +Int age
        +String dob
        +String address
        +String phone
        +String role
        +String photoUrl
    }

    class Appointment {
        +String id
        +String userId
        +String serviceName
        +String date
        +String time
        +AppointmentStatus status
        +Int price
        +Long createdAt
    }

    class AppointmentStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        COMPLETED
        CANCELLED
    }

    class Service {
        +String id
        +String name
        +String description
        +Int price
        +Int icon
    }

    class BookingRequest {
        +String userId
        +String serviceId
        +String facilityId
        +String date
        +String time
        +Int doseNumber
        +String notes
    }

    class BookingResponse {
        +Boolean success
        +String message
        +BookingDetail booking
    }

    class BookingDetail {
        +String id
        +String userId
        +String serviceId
        +String facilityId
        +String facilityName
        +String date
        +String time
        +String status
        +Int doseNumber
        +Int price
        +String paymentStatus
        +String doctorName
    }

    class AuthRequest {
        +String email
        +String password
    }

    class AuthResponse {
        +String token
        +User user
        +String message
    }

    %% ViewModels
    class AuthViewModel {
        -AuthRepository authRepository
        -MutableStateFlow~AuthState~ _authState
        +StateFlow~AuthState~ authState
        +login(email: String, password: String)
        +register(email: String, password: String, name: String, phone: String)
        +forgotPassword(email: String)
        +resetPassword(token: String, newPassword: String)
        -checkCurrentUser()
    }

    class AuthState {
        +Boolean isLoading
        +User user
        +Boolean isLoggedIn
        +String error
        +String message
        +String token
    }

    class HomeViewModel {
        -AppointmentRepository appointmentRepository
        -MutableStateFlow~HomeState~ _state
        +StateFlow~HomeState~ state
        +loadAppointments()
        +loadBookings()
        +toggleMenu()
    }

    class HomeState {
        +Boolean menuExpanded
        +Boolean isLoading
        +String error
        +List~Appointment~ appointments
        +List~BookingDetail~ bookings
    }

    class ServiceViewModel {
        -ServiceRepository serviceRepository
        -MutableStateFlow~ServiceState~ _serviceState
        +StateFlow~ServiceState~ serviceState
        +loadServices()
        +selectService(service: Service)
        +getServiceById(id: String)
    }

    class ServiceState {
        +List~Service~ services
        +Service selectedService
        +Boolean isLoading
        +String error
    }

    class AppointmentViewModel {
        -AppointmentRepository repository
        -MutableStateFlow~AppointmentState~ _state
        +StateFlow~AppointmentState~ state
        +updateName(name: String)
        +updateBirthday(birthday: String)
        +updatePhone(phone: String)
        +updateInsuranceId(insuranceId: String)
        +bookAppointment(token: String, userId: String, serviceId: String, facilityId: String, date: String, time: String)
        +fetchBookingHistory(token: String, userId: String)
    }

    class AppointmentState {
        +String name
        +String birthday
        +String phone
        +String insuranceId
        +Boolean isLoading
        +String error
        +String message
        +List~BookingDetail~ history
    }

    class SharedViewModel {
        -AuthRepository authRepository
        -MutableStateFlow~SharedState~ _sharedState
        +StateFlow~SharedState~ sharedState
        +setCurrentUser(user: User)
        +clearCurrentUser()
        +toggleTheme()
        +setTheme(isDark: Boolean)
        -checkCurrentUser()
    }

    class SharedState {
        +User currentUser
        +Boolean isDarkTheme
        +Boolean isLoggedIn
        +String token
        +String userId
        +String selectedServiceId
        +String selectedFacilityId
    }

    %% Repositories
    class AuthRepository {
        -FirebaseAuth firebaseAuth
        +loginWithEmail(email: String, password: String) Result~AuthResponse~
        +register(email: String, password: String, name: String, phone: String) Result~AuthResponse~
        +forgotPassword(email: String) Result~String~
        +resetPassword(token: String, newPassword: String) Result~String~
        +getCurrentUser() User?
        +logout()
    }

    class AppointmentRepository {
        -Retrofit retrofit
        -BookingApiService bookingApi
        +createAppointment(userId: String, serviceName: String, date: String, time: String, price: Int) Result~Appointment~
        +getAppointmentsByUserId(userId: String) Result~List~Appointment~~
        +updateAppointmentStatus(appointmentId: String, status: AppointmentStatus) Result~Appointment~
        +cancelAppointment(appointmentId: String) Result~Boolean~
        +bookAppointmentWithApi(token: String, request: BookingRequest) Result~BookingResponse~
        +getBookingHistory(token: String, userId: String) Result~List~BookingDetail~~
    }

    class ServiceRepository {
        +getServices() List~Service~
        +getServiceById(id: String) Service?
    }

    %% Relationships
    User ||--o{ Appointment : "has"
    User ||--o{ BookingDetail : "has"
    Service ||--o{ BookingDetail : "includes"
    AppointmentStatus ||--o{ Appointment : "defines"
    
    AuthViewModel --> AuthRepository : "uses"
    AuthViewModel --> AuthState : "manages"
    AuthViewModel --> User : "manages"
    AuthViewModel --> AuthResponse : "handles"
    
    HomeViewModel --> AppointmentRepository : "uses"
    HomeViewModel --> HomeState : "manages"
    HomeViewModel --> Appointment : "displays"
    HomeViewModel --> BookingDetail : "displays"
    
    ServiceViewModel --> ServiceRepository : "uses"
    ServiceViewModel --> ServiceState : "manages"
    ServiceViewModel --> Service : "manages"
    
    AppointmentViewModel --> AppointmentRepository : "uses"
    AppointmentViewModel --> AppointmentState : "manages"
    AppointmentViewModel --> BookingRequest : "creates"
    AppointmentViewModel --> BookingDetail : "fetches"
    
    SharedViewModel --> AuthRepository : "uses"
    SharedViewModel --> SharedState : "manages"
    SharedViewModel --> User : "manages"
    
    AuthRepository --> User : "manages"
    AuthRepository --> AuthResponse : "returns"
    AppointmentRepository --> Appointment : "manages"
    AppointmentRepository --> BookingDetail : "manages"
    ServiceRepository --> Service : "provides"
```

## 2. Sơ Đồ UML Sequence Diagram - Quy Trình Đăng Nhập

```mermaid
sequenceDiagram
    participant U as User
    participant LS as LoginScreen
    participant AVM as AuthViewModel
    participant AR as AuthRepository
    participant API as Backend API
    participant SM as SessionManager

    U->>LS: Nhập email/password
    LS->>AVM: login(email, password)
    AVM->>AVM: setLoading(true)
    AVM->>AR: loginWithEmail(email, password)
    AR->>API: POST /api/auth/login
    API-->>AR: AuthResponse
    AR-->>AVM: Result<AuthResponse>
    alt Success
        AVM->>AVM: setUser(response.user)
        AVM->>AVM: setToken(response.token)
        AVM->>AVM: setLoggedIn(true)
        AVM-->>LS: AuthState(isLoggedIn=true)
        LS->>LS: Navigate to Home
    else Failure
        AVM->>AVM: setError(error.message)
        AVM-->>LS: AuthState(error=message)
        LS->>LS: Show error message
    end
```

## 3. Sơ Đồ UML Sequence Diagram - Quy Trình Đặt Lịch

```mermaid
sequenceDiagram
    participant U as User
    participant HS as HomeScreen
    participant SS as SelectServiceScreen
    participant TS as SelectTimeAndSlotScreen
    participant AS as AppointmentScreen
    participant PS as PaymentScreen
    participant BSS as BookingSuccessScreen
    participant AVM as AppointmentViewModel
    participant AR as AppointmentRepository
    participant API as Backend API

    U->>HS: Chọn "Book Appointment"
    HS->>SS: Navigate to SelectService
    U->>SS: Chọn service
    SS->>TS: Navigate with service info
    U->>TS: Chọn date/time
    TS->>AS: Navigate with booking details
    U->>AS: Nhập thông tin cá nhân
    AS->>AVM: bookAppointment(token, userId, serviceId, facilityId, date, time)
    AVM->>AR: bookAppointmentWithApi(token, request)
    AR->>API: POST /api/booking
    API-->>AR: BookingResponse
    AR-->>AVM: Result<BookingResponse>
    alt Success
        AVM-->>AS: Success message
        AS->>PS: Navigate to Payment
        U->>PS: Chọn payment method
        PS->>BSS: Navigate to Success
        BSS->>BSS: Show booking confirmation
    else Failure
        AVM-->>AS: Error message
        AS->>AS: Show error
    end
```

## 4. Flowchart - Quy Trình Đăng Ký và Xác Thực

```mermaid
flowchart TD
    A[User mở app] --> B{Đã đăng nhập?}
    B -->|Yes| C[Chuyển đến HomeScreen]
    B -->|No| D[Hiển thị LoginScreen]
    
    D --> E{User chọn action}
    E -->|Đăng nhập| F[Nhập email/password]
    E -->|Đăng ký| G[Nhập thông tin đăng ký]
    E -->|Quên mật khẩu| H[Nhập email]
    
    F --> I[Gọi API login]
    I --> J{Login thành công?}
    J -->|Yes| K[Lưu token & user info]
    J -->|No| L[Hiển thị lỗi]
    K --> C
    L --> D
    
    G --> M[Gọi API register]
    M --> N{Register thành công?}
    N -->|Yes| O[Tự động đăng nhập]
    N -->|No| P[Hiển thị lỗi]
    O --> C
    P --> D
    
    H --> Q[Gọi API forgot password]
    Q --> R{Gửi email thành công?}
    R -->|Yes| S[Chuyển đến OTP Screen]
    R -->|No| T[Hiển thị lỗi]
    S --> U[Nhập OTP]
    U --> V[Gọi API verify OTP]
    V --> W{OTP đúng?}
    W -->|Yes| X[Chuyển đến Set New Password]
    W -->|No| Y[Hiển thị lỗi]
    X --> Z[Nhập mật khẩu mới]
    Z --> AA[Gọi API reset password]
    AA --> BB{Reset thành công?}
    BB -->|Yes| D
    BB -->|No| CC[Hiển thị lỗi]
```

## 5. Flowchart - Quy Trình Đặt Lịch Tiêm Chủng

```mermaid
flowchart TD
    A[User vào HomeScreen] --> B[Chọn "Book Appointment"]
    B --> C[SelectServiceScreen]
    C --> D{Chọn service}
    D --> E[SelectTimeAndSlotScreen]
    E --> F{Chọn date/time}
    F --> G[AppointmentScreen]
    G --> H[Nhập thông tin cá nhân]
    H --> I{Thông tin hợp lệ?}
    I -->|No| J[Hiển thị lỗi validation]
    J --> H
    I -->|Yes| K[Gọi API book appointment]
    K --> L{Booking thành công?}
    L -->|No| M[Hiển thị lỗi]
    M --> G
    L -->|Yes| N[PaymentScreen]
    N --> O{Chọn payment method}
    O --> P[Nhập thông tin thanh toán]
    P --> Q{Payment hợp lệ?}
    Q -->|No| R[Hiển thị lỗi payment]
    R --> P
    Q -->|Yes| S[Gọi API payment]
    S --> T{Payment thành công?}
    T -->|No| U[Hiển thị lỗi]
    U --> N
    T -->|Yes| V[BookingSuccessScreen]
    V --> W[Hiển thị xác nhận booking]
    W --> X[QR Code & thông tin chi tiết]
```

## 6. Flowchart - Quy Trình Tracking Booking

```mermaid
flowchart TD
    A[User vào TrackingBookingScreen] --> B[Load booking history]
    B --> C{Gọi API get booking history}
    C --> D{API thành công?}
    D -->|No| E[Hiển thị lỗi]
    D -->|Yes| F[Phân loại bookings]
    F --> G{Booking hiện tại}
    G --> H[Hiển thị current bookings]
    F --> I{Booking lịch sử}
    I --> J[Hiển thị history bookings]
    
    H --> K{User chọn booking}
    K --> L[Hiển thị chi tiết booking]
    L --> M{Booking status}
    M -->|PENDING| N[Hiển thị "Chờ xác nhận"]
    M -->|CONFIRMED| O[Hiển thị "Đã xác nhận"]
    M -->|COMPLETED| P[Hiển thị "Hoàn thành"]
    M -->|CANCELLED| Q[Hiển thị "Đã hủy"]
    
    N --> R{User muốn hủy?}
    R -->|Yes| S[Gọi API cancel booking]
    S --> T{Cancel thành công?}
    T -->|Yes| U[Cập nhật status]
    T -->|No| V[Hiển thị lỗi]
    U --> B
    V --> N
    R -->|No| W[Quay lại danh sách]
    W --> K
```

## 7. Sơ Đồ UML Component Diagram

```mermaid
graph TB
    subgraph "Presentation Layer"
        UI[UI Components]
        VM[ViewModels]
        Nav[Navigation]
    end
    
    subgraph "Domain Layer"
        Repo[Repositories]
        Model[Data Models]
        Config[App Config]
    end
    
    subgraph "Data Layer"
        API[Backend API]
        Local[Local Storage]
        Firebase[Firebase Auth]
    end
    
    UI --> VM
    VM --> Repo
    Repo --> API
    Repo --> Local
    Repo --> Firebase
    Nav --> UI
    Config --> Repo
    Model --> VM
    Model --> Repo
```

## 8. Sơ Đồ UML State Diagram - Trạng Thái Appointment

```mermaid
stateDiagram-v2
    [*] --> PENDING : Tạo booking
    PENDING --> CONFIRMED : Admin xác nhận
    PENDING --> CANCELLED : User hủy
    CONFIRMED --> COMPLETED : Hoàn thành tiêm
    CONFIRMED --> CANCELLED : User hủy
    COMPLETED --> [*] : Kết thúc
    CANCELLED --> [*] : Kết thúc
```

## 9. Sơ Đồ UML Activity Diagram - Quy Trình Tổng Thể

```mermaid
flowchart TD
    A[Khởi động app] --> B{User đã đăng nhập?}
    B -->|No| C[Login/Register Flow]
    B -->|Yes| D[HomeScreen]
    
    C --> E{Chọn action}
    E -->|Login| F[Đăng nhập]
    E -->|Register| G[Đăng ký]
    E -->|Forgot Password| H[Quên mật khẩu]
    
    F --> I{Login thành công?}
    I -->|Yes| D
    I -->|No| J[Hiển thị lỗi]
    J --> C
    
    G --> K{Register thành công?}
    K -->|Yes| D
    K -->|No| L[Hiển thị lỗi]
    L --> C
    
    H --> M[Gửi email reset]
    M --> N[Nhập OTP]
    N --> O[Đặt mật khẩu mới]
    O --> C
    
    D --> P{User chọn chức năng}
    P -->|Book Appointment| Q[Booking Flow]
    P -->|Track Booking| R[Tracking Flow]
    P -->|Profile| S[Profile Management]
    
    Q --> T[Chọn service]
    T --> U[Chọn thời gian]
    U --> V[Nhập thông tin]
    V --> W[Thanh toán]
    W --> X[Xác nhận booking]
    
    R --> Y[Load booking history]
    Y --> Z[Hiển thị trạng thái]
    
    S --> AA[View/Edit Profile]
    AA --> BB{Thay đổi thông tin?}
    BB -->|Yes| CC[Update profile]
    BB -->|No| D
    CC --> D
```

## 10. Sơ Đồ UML Package Diagram

```mermaid
graph TB
    subgraph "com.uth.vactrack"
        subgraph "ui"
            subgraph "UILogin"
                Login[LoginScreen]
                Register[RegisterScreen]
                Forgot[ForgotPasswordScreen]
                OTP[OtpScreen]
                SetPass[SetNewPasswordScreen]
            end
            
            subgraph "UIUser"
                Home[HomeScreen]
                Appointment[AppointmentScreen]
                Service[SelectServiceScreen]
                TimeSlot[SelectTimeAndSlotScreen]
                Payment[PaymentScreen]
                Success[BookingSuccessScreen]
                Tracking[TrackingBookingScreen]
                Profile[ProfileScreen]
                EditProfile[EditProfileScreen]
            end
            
            subgraph "viewmodel"
                AuthVM[AuthViewModel]
                HomeVM[HomeViewModel]
                ServiceVM[ServiceViewModel]
                AppointmentVM[AppointmentViewModel]
                SharedVM[SharedViewModel]
                PaymentVM[PaymentViewModel]
                TrackingVM[TrackingBookingViewModel]
                EditProfileVM[EditProfileViewModel]
            end
            
            subgraph "theme"
                Color[Color.kt]
                Theme[Theme.kt]
                Type[Type.kt]
            end
        end
        
        subgraph "data"
            subgraph "model"
                User[User.kt]
                Appointment[Appointment.kt]
                Service[Service.kt]
                Booking[BookingRequest.kt]
                Auth[AuthRequest.kt]
            end
            
            subgraph "repository"
                AuthRepo[AuthRepository.kt]
                AppointmentRepo[AppointmentRepository.kt]
                ServiceRepo[ServiceRepository.kt]
            end
        end
        
        subgraph "config"
            AppConfig[AppConfig.kt]
            SessionManager[SessionManager.kt]
        end
    end
    
    %% Relationships
    Login --> AuthVM
    Register --> AuthVM
    Home --> HomeVM
    Appointment --> AppointmentVM
    Service --> ServiceVM
    Payment --> PaymentVM
    Tracking --> TrackingVM
    Profile --> EditProfileVM
    
    AuthVM --> AuthRepo
    HomeVM --> AppointmentRepo
    ServiceVM --> ServiceRepo
    AppointmentVM --> AppointmentRepo
    
    AuthRepo --> AppConfig
    AppointmentRepo --> AppConfig
```

## 11. Sơ Đồ UML Deployment Diagram

```mermaid
graph TB
    subgraph "Mobile Device"
        subgraph "Android App"
            UI[UI Layer]
            VM[ViewModel Layer]
            Repo[Repository Layer]
        end
        
        subgraph "Local Storage"
            SharedPref[SharedPreferences]
            RoomDB[Room Database]
        end
    end
    
    subgraph "Cloud Infrastructure"
        subgraph "Backend Services"
            API[Rest API Server]
            Auth[Authentication Service]
            Booking[Booking Service]
            Payment[Payment Service]
        end
        
        subgraph "External Services"
            Firebase[Firebase Auth]
            Email[Email Service]
            SMS[SMS Service]
        end
        
        subgraph "Database"
            MySQL[(MySQL Database)]
            Redis[(Redis Cache)]
        end
    end
    
    %% Connections
    UI --> VM
    VM --> Repo
    Repo --> API
    Repo --> Firebase
    Repo --> SharedPref
    
    API --> Auth
    API --> Booking
    API --> Payment
    API --> MySQL
    API --> Redis
    
    Auth --> Firebase
    Booking --> Email
    Payment --> SMS
```

## 12. Sơ Đồ UML Use Case Diagram

```mermaid
graph TB
    subgraph "Actors"
        User[User/Patient]
        Admin[Admin/Doctor]
        System[System]
    end
    
    subgraph "Use Cases"
        UC1[Đăng ký tài khoản]
        UC2[Đăng nhập]
        UC3[Quên mật khẩu]
        UC4[Xem danh sách dịch vụ]
        UC5[Đặt lịch tiêm chủng]
        UC6[Chọn thời gian và slot]
        UC7[Thanh toán]
        UC8[Theo dõi booking]
        UC9[Quản lý profile]
        UC10[Xem lịch sử tiêm chủng]
        UC11[Hủy booking]
        UC12[Xác nhận booking]
        UC13[Gửi thông báo]
    end
    
    %% User relationships
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC5
    User --> UC6
    User --> UC7
    User --> UC8
    User --> UC9
    User --> UC10
    User --> UC11
    
    %% Admin relationships
    Admin --> UC12
    Admin --> UC13
    
    %% System relationships
    System --> UC13
    System --> UC2
    System --> UC3
```

## 13. Sơ Đồ UML Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    USERS {
        string id PK
        string email UK
        string name
        string fullName
        int age
        string dob
        string address
        string phone
        string role
        string photoUrl
        timestamp createdAt
        timestamp updatedAt
    }
    
    SERVICES {
        string id PK
        string name
        string description
        int price
        string icon
        boolean isActive
        timestamp createdAt
    }
    
    FACILITIES {
        string id PK
        string name
        string address
        string phone
        string email
        boolean isActive
        timestamp createdAt
    }
    
    APPOINTMENTS {
        string id PK
        string userId FK
        string serviceId FK
        string facilityId FK
        string date
        string time
        string status
        int price
        string notes
        timestamp createdAt
        timestamp updatedAt
    }
    
    BOOKINGS {
        string id PK
        string appointmentId FK
        string paymentMethod
        string paymentStatus
        int amount
        timestamp paymentDate
        timestamp createdAt
    }
    
    PAYMENTS {
        string id PK
        string bookingId FK
        string method
        string status
        int amount
        string transactionId
        timestamp createdAt
    }
    
    USERS ||--o{ APPOINTMENTS : "has"
    SERVICES ||--o{ APPOINTMENTS : "includes"
    FACILITIES ||--o{ APPOINTMENTS : "located_at"
    APPOINTMENTS ||--o| BOOKINGS : "creates"
    BOOKINGS ||--o| PAYMENTS : "has"
```

## 14. Sơ Đồ UML Communication Diagram - Booking Flow

```mermaid
sequenceDiagram
    participant U as User
    participant HS as HomeScreen
    participant SS as SelectServiceScreen
    participant TS as SelectTimeAndSlotScreen
    participant AS as AppointmentScreen
    participant PS as PaymentScreen
    participant BSS as BookingSuccessScreen
    participant AVM as AppointmentViewModel
    participant AR as AppointmentRepository
    participant API as Backend API
    participant SM as SharedViewModel

    Note over U,SM: Booking Flow Communication
    
    U->>HS: 1. Chọn "Book Appointment"
    HS->>SS: 2. Navigate with user context
    SS->>SM: 3. Store selected service
    U->>SS: 4. Chọn service
    SS->>TS: 5. Navigate with service & user info
    TS->>SM: 6. Store selected time/slot
    U->>TS: 7. Chọn date/time
    TS->>AS: 8. Navigate with booking details
    AS->>AVM: 9. Load user profile
    AVM->>AR: 10. Get user data
    AR->>API: 11. Fetch user info
    API-->>AR: 12. User data
    AR-->>AVM: 13. User profile
    AVM-->>AS: 14. Display form
    U->>AS: 15. Submit booking info
    AS->>AVM: 16. Validate & submit
    AVM->>AR: 17. Create booking
    AR->>API: 18. POST /api/booking
    API-->>AR: 19. Booking response
    AR-->>AVM: 20. Booking result
    AVM-->>AS: 21. Success/Error
    AS->>PS: 22. Navigate to payment
    PS->>BSS: 23. Navigate to success
    BSS->>SM: 24. Update booking status
```

## 15. Sơ Đồ UML Timing Diagram - Authentication Flow

```mermaid
sequenceDiagram
    participant U as User
    participant UI as LoginScreen
    participant VM as AuthViewModel
    participant Repo as AuthRepository
    participant API as Backend API
    participant Local as Local Storage

    Note over U,Local: Authentication Timing Flow
    
    U->>UI: Enter credentials
    UI->>VM: login(email, password)
    VM->>VM: setLoading(true)
    VM->>Repo: loginWithEmail(email, password)
    Repo->>API: POST /auth/login
    API->>API: Validate credentials
    API->>API: Generate token
    API-->>Repo: AuthResponse
    Repo-->>VM: Result<AuthResponse>
    VM->>VM: setLoading(false)
    VM->>VM: setUser(response.user)
    VM->>VM: setToken(response.token)
    VM->>Local: Save user data
    VM->>Local: Save token
    VM-->>UI: AuthState(isLoggedIn=true)
    UI->>UI: Navigate to Home
```

---

## Tóm Tắt Các Sơ Đồ

### 1. **Class Diagram**: Mô tả cấu trúc các class, interface và mối quan hệ giữa chúng
### 2. **Sequence Diagram**: Mô tả luồng tương tác giữa các component theo thời gian
### 3. **Flowchart**: Mô tả logic nghiệp vụ và quy trình xử lý
### 4. **Component Diagram**: Mô tả cấu trúc hệ thống và các module
### 5. **State Diagram**: Mô tả các trạng thái và chuyển đổi trạng thái
### 6. **Activity Diagram**: Mô tả quy trình nghiệp vụ tổng thể
### 7. **Package Diagram**: Mô tả cấu trúc package và module
### 8. **Deployment Diagram**: Mô tả cấu trúc triển khai hệ thống
### 9. **Use Case Diagram**: Mô tả các chức năng và người dùng
### 10. **ERD**: Mô tả cấu trúc cơ sở dữ liệu
### 11. **Communication Diagram**: Mô tả tương tác giữa các object
### 12. **Timing Diagram**: Mô tả timing của các sự kiện

Các sơ đồ này cung cấp cái nhìn toàn diện về kiến trúc, luồng dữ liệu và tương tác trong ứng dụng VacTrack, giúp hiểu rõ cách hệ thống hoạt động và hỗ trợ việc phát triển, bảo trì và mở rộng ứng dụng. 