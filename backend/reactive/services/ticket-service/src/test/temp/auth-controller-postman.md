# 1. LOGIN
curl -X POST http://localhost:8080/api/v1/auth/login \
-H "Content-Type: application/json" \
-H "User-Agent: PostmanRuntime/7.26.8" \
-d '{
"email": "customer@gmail.com",
"password": "DefaultP4$$",
"rememberMe": true
}'

# 2. REGISTER WITH EMAIL
curl -X POST http://localhost:8080/api/v1/auth/register \
-H "Content-Type: application/json" \
-d '{
"email": "test@gmail.com",
"password": "Test123!",
"fullName": "Nguyen Van A",
"phoneNumber": "0901234567"
}'

