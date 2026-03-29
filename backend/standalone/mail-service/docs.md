# 📧 Mail Service - Hướng Dẫn Sử Dụng

## 1. 🚀 Hướng dẫn Service khác gọi API `/send`

### Lấy API Key

```yaml
# Trong file application-dev.yml
security:
    api-keys:
        - sk_user_dev_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
        - sk_event_dev_x7y8z9w0a1b2c3d4e5f6g7h8i9j0k1l2
```

### Gọi API từ Java/Spring Boot

```java
@Service
public class EmailClient {
    private final RestTemplate restTemplate;

    public void sendOtpEmail(String email, String userName, String otp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "sk_user_dev_...");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> request = Map.of(
            "to", email,
            "subject", "Mã xác thực OTP",
            "templateCode", "OTP",
            "templateData", Map.of(
                "userName", userName,
                "otp", otp,
                "expireMinutes", 5
            ),
            "async", true  // true = không đồng bộ, false = đồng bộ
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        restTemplate.postForObject(
            "http://localhost:8082/api/v1/mail/send",
            entity,
            EmailResponse.class
        );
    }
}
```

---

## 2. 🎨 Hướng dẫn Custom Template

### Cấu trúc thư mục

```
src/main/resources/templates/
├── email/
│   ├── otp.html           # Template OTP mặc định
│   ├── welcome.html       # Template Welcome
│   └── custom/            # Đặt template mới ở đây
│       └── my-template.html
└── fragments/             # Component dùng chung
```

### Bước 1: Tạo file HTML template

Tạo file: `src/main/resources/templates/email/custom/my-template.html`

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8" />
        <style>
            body {
                font-family: Arial;
                padding: 20px;
                background: #f4f4f4;
            }
            .container {
                max-width: 600px;
                margin: 0 auto;
                background: #fff;
                padding: 30px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>
                Xin chào
                <span th:text="${userName}">User</span>
                !
            </h1>
            <p th:text="${message}">Nội dung của bạn</p>
        </div>
    </body>
</html>
```

### Bước 2: Đăng ký template vào enum

Mở file: `src/main/java/com/ticket4u/mailservice/enums/TemplateType.java`

```java
@Getter
@RequiredArgsConstructor
public enum TemplateType {
    OTP("email/otp", "Xác thực OTP"),
    WELCOME("email/welcome", "Chào mừng"),

    // Thêm template mới
    MY_TEMPLATE("email/custom/my-template", "My Custom Template");

    private final String templatePath;
    private final String description;
}
```

### Bước 3: Build và test

```bash
# run project
sau đó truy cập
http://localhost:8082/swagger-ui.html
nhập api key và test API với payload:

{
    "to": "test@example.com",
    "subject": "Test Template",
    "templateCode": "MY_TEMPLATE",
    "templateData": {
      "userName": "Test User",
      "message": "Hello World!"
    }
```

### Thymeleaf Syntax cơ bản

```html
<!-- Hiển thị biến -->
<span th:text="${variableName}">Default</span>

<!-- Điều kiện -->
<div th:if="${isVip}">VIP Content</div>

<!-- Vòng lặp -->
<li th:each="item : ${items}" th:text="${item.name}"></li>

<!-- Link -->
<a th:href="@{https://example.com/verify?token={t}(t=${token})}">Verify</a>
```

### Template Variables

Template nhận **bất kỳ biến nào** từ `templateData`:

```json
{
    "templateData": {
        "userName": "Nguyễn Văn A",
        "otp": "123456",
        "customField": "Any value"
    }
}
```

```html
<p th:text="${userName}">User</p>
<p th:text="${otp}">000000</p>
<p th:text="${customField}">Value</p>
```

---

## 📞 Info

- **Port:** 8082
- **Swagger:** http://localhost:8082/swagger-ui.html
- **Rate Limit:** 60 req/min, 500 req/hour per API key
