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
<p align="center"><i>Hình ảnh 3</i></p>

- Giao diện Server nhận file.
<p align="center"> <img width="800" height="800" alt="image" src=image-3.png/> </p>
<p align="center"><i>Hình ảnh 4</i></p>

## 📝 4. Hướng dẫn cài đặt và sử dụng

### 6.1. Yêu cầu
- **JDK 8+** (khuyến nghị JDK 17/21)
- IDE (Eclipse/IntelliJ/VS Code + Extension Java) hoặc dòng lệnh.

### 6.2. Clone nguồn
```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
### 6.3. Chạy bằng IDE
Import project (Java Project).

Chạy Server trước: server.ServerMain

Cấu hình IP/Port hiển thị trên server (mặc định 0.0.0.0:5555).

Chạy Client: client.ClientMain

Nhập Server IP (ví dụ 127.0.0.1 nếu cùng máy) và Port (ví dụ 5555) → Connect.

### 6.4. Chạy bằng dòng lệnh
bash
Sao chép mã
# Từ thư mục gốc
javac -d out src/server/*.java src/client/*.java
# Server
java -cp out server.ServerMain 5555
# Client (kết nối tới 127.0.0.1:5555)
java -cp out client.ClientMain 127.0.0.1 5555
### 6.5. Gửi/nhận file
Client bấm 📎 Send File → chọn tệp → thấy progress.

Bên nhận sẽ hiện hộp thoại Chấp nhận → chọn Accept để lưu, Decline để hủy.

### 6.6. Lỗi thường gặp & Cách xử lý
Connection refused: Server chưa chạy/Port sai → chạy server & kiểm tra port.

Address already in use: Cổng đã bị chiếm → đổi port (vd 5556) hoặc kill tiến trình cũ.

Firewall chặn: Cho phép Java/port qua tường lửa.

Khác mạng LAN: Dùng IP thật của server (ipconfig) và mở port trên router nếu cần.

📌 *Lưu ý: Có thể tùy chỉnh tên database, tài khoản admin, giao diện theo nhu cầu.*

## Thông tin liên hệ  
Họ tên: Bùi Tuấn Dương .  
Lớp: CNTT 16-03.  
Email: buibanh2k4@gmail.com.

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---

[def]: ![alt text](image.png)
[def2]:![alt text](image-1.png)
[def3]: ![alt text](image-2.png)
[def4]: ![alt text](image-3.png)
