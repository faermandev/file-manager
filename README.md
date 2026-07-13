# 📁 File Manager API

> A production-ready REST API for secure file management, built with **Java** and **Spring Boot**. It handles user authentication with JWT, stores files on **AWS S3**, persists metadata in **PostgreSQL**, and is fully containerized with **Docker** and deployed on **AWS EC2**.

<p>
  <img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" alt="Spring Security"/>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/AWS_S3-569A31?style=flat-square&logo=amazons3&logoColor=white" alt="AWS S3"/>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker"/>
</p>

---

## 📋 Overview

File Manager API lets authenticated users upload, list, download, and delete files. Files themselves live in an **AWS S3 bucket**, while their metadata (name, size, owner, S3 key, timestamps) is stored in **PostgreSQL**. Downloads are served through **time-limited pre-signed URLs**, so files are never exposed publicly.

---

## ✨ Features

- 🔐 **JWT authentication** with Spring Security (register / login)
- ⬆️ **File upload** to AWS S3 with metadata persisted in PostgreSQL
- 🔗 **Secure download** via time-limited pre-signed URLs
- 📄 **File listing** scoped to the authenticated user
- 🗑️ **File deletion** from both S3 and the database
- 🐳 **Dockerized** with a multi-stage build
- ☁️ **Deployed on AWS EC2**, with the image versioned in AWS ECR

---

## 🛠 Tech Stack

| Layer | Technologies |
| ----- | ------------ |
| **Language** | Java 17 |
| **Framework** | Spring Boot, Spring Web, Spring Security |
| **Persistence** | Spring Data JPA, PostgreSQL |
| **Storage** | AWS S3 (AWS SDK) |
| **Auth** | JWT |
| **Infra** | Docker (multi-stage build), AWS EC2, AWS ECR |

---

## 🏗 Architecture

```
Client
  │  (JWT in Authorization header)
  ▼
Spring Boot REST API  ──────────►  PostgreSQL   (file metadata)
  │
  └──────────────────────────────►  AWS S3      (file storage)
                                        │
                                        └──►  Pre-signed URL returned to client
```

The API stores only **metadata** in PostgreSQL; the file bytes go to S3. On download, the API generates a short-lived pre-signed URL instead of streaming the file itself.

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose
- An AWS account with an S3 bucket and IAM credentials

### 1. Clone the repository

```bash
git clone https://github.com/faermandev/file-manager.git
cd file-manager
```

### 2. Configure environment variables

Create a `.env` file in the project root (see the table below).

### 3. Run with Docker Compose

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`.

### 4. Run locally without Docker (optional)

```bash
./mvnw spring-boot:run
```

---

## 🔑 Environment Variables

| Variable | Description | Example |
| -------- | ----------- | ------- |
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/filemanager` |
| `DB_USERNAME` | Database user | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | Secret key used to sign JWTs | `your-secret-key` |
| `JWT_EXPIRATION` | Token expiration in ms | `86400000` |
| `AWS_ACCESS_KEY` | AWS IAM access key | `AKIA...` |
| `AWS_SECRET_KEY` | AWS IAM secret key | `...` |
| `AWS_REGION` | AWS region of the bucket | `us-east-1` |
| `AWS_S3_BUCKET` | S3 bucket name | `file-manager-bucket` |

> ⚠️ Never commit real credentials. Keep `.env` in your `.gitignore`.

---

## 📡 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth |
| ------ | -------- | ----------- | ---- |
| `POST` | `/auth/register` | Create a new user | ❌ |
| `POST` | `/auth/login` | Authenticate and receive a JWT | ❌ |

### Files

| Method | Endpoint | Description | Auth |
| ------ | -------- | ----------- | ---- |
| `POST` | `/files` | Upload a file (multipart) | ✅ |
| `GET` | `/files` | List the authenticated user's files | ✅ |
| `GET` | `/files/{id}/download` | Get a pre-signed download URL | ✅ |
| `DELETE` | `/files/{id}` | Delete a file from S3 and the database | ✅ |

> Authenticated requests require the header: `Authorization: Bearer <token>`

### Example: upload a file

```bash
curl -X POST http://localhost:8080/files \
  -H "Authorization: Bearer <token>" \
  -F "file=@document.pdf"
```

### Example: get a download URL

```bash
curl -X GET http://localhost:8080/files/1/download \
  -H "Authorization: Bearer <token>"
```

---

## 🐳 Docker

The project uses a **multi-stage build**: the first stage compiles the application with Maven, and the second stage runs a lightweight JRE image with only the built JAR — keeping the final image small.

```bash
# Build the image
docker build -t file-manager .

# Run the container
docker run -p 8080:8080 --env-file .env file-manager
```

---

## 📦 Deployment

The application is deployed on **AWS EC2**. The Docker image is built, tagged, and pushed to **AWS ECR**, then pulled and run on the EC2 instance.

```bash
# Authenticate Docker with ECR
aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account>.dkr.ecr.<region>.amazonaws.com

# Tag and push
docker tag file-manager:latest <account>.dkr.ecr.<region>.amazonaws.com/file-manager:latest
docker push <account>.dkr.ecr.<region>.amazonaws.com/file-manager:latest
```

---

## 👤 Author

**David Faerman** — Computer Science student @ PUC-Rio

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat-square&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/david-faerman-df/)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=flat-square&logo=github&logoColor=white)](https://github.com/faermandev)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=flat-square&logo=gmail&logoColor=white)](mailto:faermandev@gmail.com)
