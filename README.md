# 💉 Hệ thống Quản lý Tiêm chủng - Vacxin Tracking System

> 🧠 Một nền tảng mã nguồn mở mạnh mẽ giúp người dân, bác sĩ và cơ sở y tế quản lý dữ liệu tiêm chủng, nhắc lịch và theo dõi hồ sơ sức khỏe một cách thông minh và hiệu quả.

![GitHub repo size](https://img.shields.io/github/repo-size/Moi-Nguyen/Vacxin_Tracking?color=blue)
![GitHub stars](https://img.shields.io/github/stars/Moi-Nguyen/Vacxin_Tracking?style=social)
![Python](https://img.shields.io/badge/Python-3.9%2B-blue)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Trạng_thái-Đang_phát_triển-success)
![Contributions](https://img.shields.io/badge/Đóng_góp-Chào_mừng-orange)

---

## 🧭 Table of Contents
- [Overview](#-overview)
- [Key Features](#-key-features)
- [Tech Stack](#️-tech-stack)
- [Feature Details](#-feature-details)
- [Installation & Usage](#-installation--usage)
- [Project Structure](#️-project-structure)
- [Screenshots](#-screenshots)
- [Project Goals](#-project-goals)
- [Contributors & Supervisor](#-contributors--supervisor)
- [Contact](#-contact)
- [License](#-license)

---

## Giới thiệu
**Vacxin Tracking System (VacTrack)** là một ứng dụng web hiện đại hỗ trợ quản lý và theo dõi tiêm chủng.  
Hệ thống cho phép người dân dễ dàng đặt lịch, theo dõi hồ sơ y tế cá nhân, nhận thông báo nhắc lịch tiêm và giúp bác sĩ, cơ sở y tế quản lý bệnh nhân một cách hiệu quả.

> *Giải pháp y tế thông minh — vì sức khỏe cộng đồng.*

---

## Tính năng nổi bật
 Quản lý hồ sơ và lịch sử tiêm chủng  
 Đặt lịch tiêm và nhận thông báo tự động  
 Dashboard thống kê trực quan  
 Hệ thống phân quyền người dùng (User / Doctor / Admin)  
 API mở cho tích hợp hệ thống khác  
 Bảo mật đăng nhập, quên mật khẩu, xác thực hai bước  

---

## Công nghệ sử dụng

| Thành phần | Công nghệ |
|:------------|:-----------|
| **Ngôn ngữ chính** | Python 3.9+ |
| **Framework Backend** | Flask / FastAPI |
| **Frontend** | HTML, CSS, Bootstrap, JavaScript |
| **Cơ sở dữ liệu** | SQLite / MySQL |
| **Thư viện hỗ trợ** | Pandas, Plotly, Matplotlib |
| **Quản lý mã nguồn** | Git & GitHub |
| **Triển khai** | Localhost / Docker-ready |

---

##  Mô tả chi tiết các tính năng

###  Người dùng (User)
#### 1. Đặt lịch và quản lý lịch tiêm
- Đặt lịch tiêm tại các cơ sở y tế đã đăng ký.  
- Tìm kiếm cơ sở tiêm chủng gần nhất.  
- Quản lý lịch hẹn và xem lịch sử tiêm chủng cá nhân.  
- Xem các dịch vụ tiêm chủng, tư vấn, và chương trình phù hợp.  
  ![image](https://github.com/user-attachments/assets/b22c6803-fe69-4de1-9293-6299827af17c)

#### 2. Hồ sơ y tế và theo dõi sức khỏe
- Lưu trữ hồ sơ y tế, lịch sử tiêm và kết quả xét nghiệm.  
- Theo dõi sức khỏe và các cảnh báo liên quan.  
- Cập nhật thông tin cá nhân, bảo hiểm y tế.  
  ![image](https://github.com/user-attachments/assets/73670746-7660-4538-95c7-b5ce4c923362)

---

###  Bác sĩ (Doctor)
- Quản lý danh sách lịch hẹn trong ngày.  
- Truy cập hồ sơ bệnh nhân, ghi chú và chẩn đoán.  
- Cập nhật kết quả khám, kê đơn, hủy hoặc xác nhận lịch hẹn.  
- Tìm kiếm bệnh nhân nhanh chóng theo tên hoặc ID.  
  ![image](https://github.com/user-attachments/assets/dfc259ed-cf81-45f7-8e4e-e30da217e02b)

---

###  Quản trị viên (Admin)
- Quản lý danh sách người dùng, bác sĩ, và cơ sở y tế.  
- Phân quyền truy cập và giám sát hệ thống.  
- Quản lý dữ liệu, thống kê và hoạt động người dùng.  
  ![image](https://github.com/user-attachments/assets/6a69fb2f-e7f8-4ea7-8ba0-cf9c3c0e62fa)

---

##  Hướng dẫn cài đặt và sử dụng

###  Clone dự án
```bash
git clone https://github.com/Moi-Nguyen/Vacxin_Tracking.git
cd Vacxin_Tracking

```
```bash
python -m venv venv
source venv/bin/activate   # macOS/Linux
venv\Scripts\activate      # Windows
```
```bash
python app.py
```

```
Vacxin_Tracking/
├── app.py                # File chạy chính
├── config.py             # Cấu hình hệ thống
├── static/               # CSS, JS, hình ảnh
├── templates/            # Giao diện HTML (Flask)
├── database/             # CSDL SQLite/MySQL
├── requirements.txt      # Danh sách thư viện
└── README.md             # Tài liệu dự án

```

| Trang chính                                                                               | Dashboard                                                                                 |
| ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| ![image](https://github.com/user-attachments/assets/62983d09-7b18-4dcd-bd79-1bbce8b6f1a3) | ![image](https://github.com/user-attachments/assets/127594fb-ffd0-4461-b0cc-95c32730b975) |

Mục tiêu dự án

VacTrack hướng tới việc trở thành một nền tảng hỗ trợ tiêm chủng và quản lý hồ sơ y tế toàn diện, tiện lợi và an toàn.
Giúp người dân dễ dàng tiếp cận dịch vụ y tế, giảm tải thủ tục hành chính và nâng cao hiệu quả quản lý cho các cơ sở y tế.

 “Công nghệ vì sức khỏe cộng đồng – Vaccinate Smart, Live Better.”

 Thành viên và Giảng viên hướng dẫn

## Sinh viên thực hiện:

 Nguyễn Đức Lượng — 077205012208

 Nguyễn Trường Phục — 08205013224

 Nguyễn Phạm Thiên Phước — 052205012221

## Giảng viên hướng dẫn:

 ThS. Trương Quang Tuấn

## Liên hệ

📧 Email hỗ trợ: support@vactrack.com

🌐 GitHub: Moi-Nguyen
