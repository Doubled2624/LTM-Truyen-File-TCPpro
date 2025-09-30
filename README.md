<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   TRUYỀN FILE TCP
</h2>
<div align="center">
    <p align="center">
        <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="170"/>
        <img src="docs/fitdnu_logo.png" alt="AIoTLab Logo" width="180"/>
        <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>


## 📖 1. Giới thiệu hệ thống
Ứng dụng **Chat & Truyền File Client–Server** được phát triển bằng **Java** sử dụng giao thức **TCP**.  
Hệ thống cho phép nhiều người dùng giao tiếp **thời gian thực** và **chia sẻ file** qua mạng, giúp quản lý dữ liệu và hỗ trợ cộng tác nhanh chóng, hiệu quả.  

- **Server**: Trung tâm quản lý kết nối, tiếp nhận và phân phối tin nhắn/file.  
- **Client**: Giao diện để gửi/nhận tin nhắn, chọn và truyền file.  
- **Lưu trữ dữ liệu**: Lịch sử chat được lưu vào file văn bản (thay vì cơ sở dữ liệu) để triển khai đơn giản.  

---

## ⚙️ Chức năng chính

### 🖥️ Server
- Quản lý kết nối Client (đa luồng).  
- Phân phối tin nhắn & file giữa các Client.  
- Lưu lịch sử chat kèm timestamp.  
- Xóa toàn bộ lịch sử khi cần.  
- Xử lý lỗi & ngắt kết nối.  

### 💻 Client
- Kết nối đến Server qua IP + Port.  
- Gửi & nhận **tin nhắn**.  
- Gửi & nhận **file**.  
- Giao diện **Java Swing**: vùng hiển thị chat, ô nhập văn bản, nút gửi, chọn file.  
- Quản lý trạng thái: hiển thị khi mất kết nối, lỗi gửi/nhận.  

### 🌐 Hệ thống
- **TCP Socket**: sử dụng `ServerSocket` & `Socket`, hỗ trợ nhiều Client đồng thời.  
- **Trung gian quản lý dữ liệu**: tất cả tin nhắn & file đều đi qua Server.  
- **File I/O**: ghi lịch sử chat (append mode, `LocalDateTime`).  
- **Bảo mật & xử lý lỗi**: log tại Server, thông báo lỗi ở Client.  

---

## 🛠️ 2. Công nghệ sử dụng
- **Java Core**  
- **Multithreading (đa luồng)**  
- **Java Swing** (GUI)  
- **Java Sockets (TCP/IP)**  
- **File I/O**  

---

## 🚀 3. Hình ảnh các chức năng
- Giao diện Server.
<p align="center"> <img width="800" height="800" alt="image" src=image.png/> </p>
<p align="center"><i>Hình ảnh 1</i></p>

- Giao diện Client.

<p align="center"> <img width="800" height="800" alt="image" src=image-1.png/> </p>
<p align="center"><i>Hình ảnh 2</i></p>

- Giao diện Client gửi file.

<p align="center"> <img width="800" height="800" alt="image" src=image-2.png/> </p>
<p align="center"><i>Hình ảnh 3<-/i></p>

- Giao diện Server nhận file.
<p align="center"> <img width="800" height="800" alt="image" src=image-3.png/> </p>
<p align="center"><i>Hình ảnh 4</i></p>

## 📝 4. Hướng dẫn cài đặt và sử dụng

Project 1: Hệ thống truyền file qua TCP đơn giản
Project 2: Ứng dụng chat TCP đa luồng
Project 3: Hệ thống quản lý truyền tải dữ liệu mạng LAN
(Liệt kê các dự án hoặc ứng dụng bạn đã phát triển dựa trên nền tảng hoặc công nghệ tương tự)

⚙️5.Các bước cài đặt

Cài đặt JDK: Tải và cài đặt JDK 8 hoặc mới hơn từ trang chính thức của Oracle hoặc OpenJDK.
Tải source code: Clone hoặc tải mã nguồn của hệ thống từ repository.
Biên dịch và chạy Server:
Mở terminal hoặc IDE.
Chạy file Client.java.
Biên dịch và chạy Client:
Mở terminal hoặc IDE.
Chỉnh sửa đường dẫn file trong FileSender.java.
Chạy file Sever.java.
Kiểm tra kết nối: Đảm bảo server và client cùng mạng hoặc có thể kết nối qua IP và port đã cấu hình.
Truyền file: Thực hiện gửi file từ client, server sẽ nhận và lưu file.


📌 *Lưu ý: Có thể tùy chỉnh tên database, tài khoản admin, giao diện theo nhu cầu.*

## Thông tin liên hệ  
Họ tên: Bùi Tuấn Dương .  
Lớp: CNTT 16-03.  
Email: buibanh2k4@gmail.com.

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---

[def]: image.png
[def2]: image-1.png
[def3]: image-2.png
[def4]: image-3.png
