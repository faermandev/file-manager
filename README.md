# 📁 File Manager API

A REST API for file management built with Spring Boot, featuring JWT authentication, AWS S3 integration for file storage, and PostgreSQL for metadata persistence.

## 🚀 Features

- User registration and authentication with JWT
- File upload to AWS S3
- Secure file download via pre-signed URLs
- File listing per authenticated user
- File deletion from S3 and database
- Dockerized application with multi-stage build

## 🛠️ Tech Stack

- **Java 24**
- **Spring Boot 4.0.6**
- **Spring Security** — JWT-based authentication
- **Spring Data JPA** — database access
- **PostgreSQL** — metadata storage (NeonDB)
- **AWS S3** — file storage
- **AWS SDK v2** — S3 integration
- **JJWT** — JWT generation and validation
- **Lombok** — boilerplate reduction
- **Docker** — containerization
- **Maven** — dependency management

## 📋 Prerequisites

- Java 24
- Maven
- Docker
- AWS account with S3 bucket
- PostgreSQL database (or NeonDB)

## ⚙️ Configuration

Copy the example properties file and fill in your values:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

Fill in the following properties:

```properties
# Database
spring.datasource.url=jdbc:postgresql://<host>/<database>
spring.datasource.username=<username>
spring.datasource.password=<password>

# JWT
jwt.secret=<your-secret-key>
jwt.expiration=3600000

# AWS S3
aws.access-key=<your-access-key>
aws.secret-key=<your-secret-key>
aws.region=<your-region>
aws.s3.bucket=<your-bucket-name>
```

## 🏃 Running locally

### With Maven

```bash
./mvnw spring-boot:run
```

### With Docker Compose

Create a `.env` file in the project root:

```env
JWT_SECRET=your-secret-key
JWT_EXPIRATION=3600000
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key
AWS_REGION=sa-east-1
AWS_S3_BUCKET=your-bucket-name
```

Then run:

```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

## 📡 API Endpoints

### Auth

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/users` | Create user | ❌ |
| POST | `/auth/login` | Login and get JWT | ❌ |

### Files

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/files` | List user's files | ✅ |
| POST | `/files/upload` | Upload a file | ✅ |
| GET | `/files/{id}/download` | Get pre-signed download URL | ✅ |
| DELETE | `/files/{id}` | Delete a file | ✅ |

## 🔐 Authentication

All protected endpoints require a JWT token in the `Authorization` header:

```
Authorization: Bearer <token>
```

To get a token, make a `POST /auth/login` request with your credentials.

## 📝 Request & Response Examples

### Create user

**Request:**
```json
POST /users
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securepassword"
}
```

**Response:** `201 Created`
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
}
```

### Login

**Request:**
```json
POST /auth/login
{
    "email": "john@example.com",
    "password": "securepassword"
}
```

**Response:** `200 OK`
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer"
}
```

### Upload file

**Request:**
```
POST /files/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <your-file>
```

**Response:** `201 Created`
```json
{
    "id": 1,
    "originalName": "document.pdf",
    "url": "https://bucket.s3.amazonaws.com/uploads/uuid-document.pdf",
    "contentType": "application/pdf",
    "size": 12345,
    "uploadedAt": "2026-05-27T10:00:00"
}
```

### List files

**Request:**
```
GET /files
Authorization: Bearer <token>
```

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "originalName": "document.pdf",
        "url": "https://bucket.s3.amazonaws.com/uploads/uuid-document.pdf",
        "contentType": "application/pdf",
        "size": 12345,
        "uploadedAt": "2026-05-27T10:00:00"
    }
]
```

### Get download URL

**Request:**
```
GET /files/1/download
Authorization: Bearer <token>
```

**Response:** `200 OK`
```
https://bucket.s3.amazonaws.com/uploads/uuid-document.pdf?X-Amz-Algorithm=...
```

### Delete file

**Request:**
```
DELETE /files/1
Authorization: Bearer <token>
```

**Response:** `204 No Content`

## 🏗️ Project Structure

```
src/
└── main/
    └── java/
        └── com/faermandev/file_manager/
            ├── config/
            │   ├── AwsConfig.java
            │   └── SecurityConfig.java
            ├── controller/
            │   ├── AuthController.java
            │   ├── FileController.java
            │   └── UserController.java
            ├── dto/
            │   ├── CreateUserRequest.java
            │   ├── FileResponse.java
            │   ├── LoginRequest.java
            │   ├── LoginResponse.java
            │   └── UserResponse.java
            ├── entity/
            │   ├── File.java
            │   └── User.java
            ├── exception/
            │   ├── FileNotFoundException.java
            │   ├── GlobalExceptionHandler.java
            │   ├── InvalidCredentialsException.java
            │   ├── UnauthorizedFileAccessException.java
            │   └── UserNotFoundException.java
            ├── filter/
            │   └── JwtAuthenticationFilter.java
            ├── repository/
            │   ├── FileRepository.java
            │   └── UserRepository.java
            └── service/
                ├── AuthService.java
                ├── FileService.java
                ├── JwtService.java
                ├── S3Service.java
                └── UserService.java
```

## 🐳 Docker

### Build image

```bash
docker build -t file-manager .
```

### Build for AMD64 (for AWS EC2 deployment)

```bash
docker buildx build --platform linux/amd64 -t file-manager .
```

## ☁️ AWS Deployment

The application is deployed on AWS EC2 with the Docker image stored in AWS ECR.

### Push to ECR

```bash
# Authenticate Docker to ECR
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com

# Build, tag and push
docker buildx build --platform linux/amd64 -t file-manager .
docker tag file-manager:latest <account-id>.dkr.ecr.<region>.amazonaws.com/file-manager:latest
docker push <account-id>.dkr.ecr.<region>.amazonaws.com/file-manager:latest
```

### Run on EC2

```bash
docker run -d \
  --name file-manager-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="<database-url>" \
  -e SPRING_DATASOURCE_USERNAME="<username>" \
  -e SPRING_DATASOURCE_PASSWORD="<password>" \
  -e JWT_SECRET="<jwt-secret>" \
  -e JWT_EXPIRATION="3600000" \
  -e AWS_ACCESS_KEY="<access-key>" \
  -e AWS_SECRET_KEY="<secret-key>" \
  -e AWS_REGION="<region>" \
  -e AWS_S3_BUCKET="<bucket-name>" \
  <account-id>.dkr.ecr.<region>.amazonaws.com/file-manager:latest
```

## 📄 License

This project is for educational purposes.