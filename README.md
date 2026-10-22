# 🚀 Full Stack Resume Builder

A full-stack web application that enables users to create, manage, and customize professional resumes with an easy-to-use interface. Built using React.js and Spring Boot, the application provides secure authentication, resume management, cloud storage, and payment integration.

## 📌 Project Overview

The Resume Builder simplifies the process of creating professional resumes by providing a centralized platform where users can manage their resume information and access resume-building features.

## ✨ Features

- 🔐 **User Authentication** – Secure user registration and login.
- 📝 **Resume Management** – Create, save, and manage resumes.
- 🎨 **Responsive UI** – Interactive frontend built with React.js.
- ☁️ **Cloudinary Integration** – Cloud-based file storage.
- 💳 **Razorpay Integration** – Payment gateway integration for premium features.
- 🔗 **REST APIs** – Communication between frontend and backend.
- 🗄️ **Database Management** – MySQL for persistent data storage.
- ⚡ **Spring Boot Backend** – Robust backend services and business logic.


## 🛠️ Tech Stack

**Frontend**
- React.js
- JavaScript
- HTML5
- CSS3

**Backend**
- Java
- Spring Boot
- Spring Data JPA
- REST APIs
- Maven

**Database & Integrations**
- MySQL
- Cloudinary
- Razorpay

## 🏗️ Application Architecture

```text
React.js Frontend
       |
       | REST API / HTTP
       v
Spring Boot Backend
       |
       +---- MySQL Database
       |
       +---- Cloudinary
       |
       +---- Razorpay Payment Gateway
```

## ⚙️ Installation and Setup

### Prerequisites

Make sure you have installed:

- Java JDK (version compatible with your project)
- Node.js and npm
- MySQL Server
- Maven
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
cd YOUR_REPOSITORY
```

### 2. Backend Setup

Navigate to the Spring Boot backend directory.

Configure your database and integration credentials in `application.properties` or `application.yml`.

Example configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/resume_builder
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Set the required environment variables for your database, Cloudinary, and Razorpay credentials.

Run the backend:

```bash
./mvnw spring-boot:run
```

On Windows, use:

```bash
mvnw.cmd spring-boot:run
```

### 3. Frontend Setup

Navigate to the frontend directory containing `package.json`.

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

If your frontend uses Create React App instead of Vite, use:

```bash
npm start
```

### 4. Access the Application

Open the local URL displayed by your frontend development server, typically:

```text
http://localhost:5173
```

The Spring Boot backend commonly runs on:

```text
http://localhost:8080
```

Adjust these URLs according to your configuration.

## 🔑 Environment Variables

Configure the following values using environment variables or a local configuration file that is excluded from Git.

| Variable | Purpose |
|---|---|
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| Cloudinary credentials | Cloud storage integration |
| Razorpay API keys | Payment gateway integration |

**Security:** Never commit database passwords, API secrets, private keys, or production credentials to GitHub.

## 📂 Project Structure

```text
Resume-Builder/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── mvnw
├── frontend/
│   ├── src/
│   ├── public/
│   └── package.json
├── .gitignore
└── README.md
```

*Note: Update this structure to match your actual repository. If your frontend and backend are stored in separate repositories or directories, adjust the paths accordingly.*

## 🔮 Future Enhancements

- Multiple professional resume templates.
- PDF resume generation and download.
- AI-powered resume suggestions.
- ATS-friendly resume optimization.
- Resume preview and customization.
- Additional premium features.

## 🤝 Contributing

Contributions, suggestions, and feedback are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Submit a pull request.

## 👨‍💻 Author

**Vibhinna Shrivastava**

GitHub: [Visit My GitHub Profile](https://github.com/vibhinnaashrivastava)

## 📄 License

This project is available for educational and portfolio purposes. Add a suitable open-source license if you intend to permit reuse and redistribution.

---

⭐ If you find this project useful, consider giving the repository a star!

# ResumeCraft frontend
React + Vite + Tailwind + Axios starter for the Resume Builder Spring Boot backend.

Run: copy `.env.example` to `.env`, then `npm install`, then `npm run dev`.

Endpoints used: `/auth/resume/register`, `/auth/resume/login-user`, `/auth/resume/profile`, `/auth/resume/resend-verification`, `/resume/create-resume`, `/resume/get-resume`, `/resume/{resumeId}`, `/api/templates/get-templates`, `/api/payment/create-order`, `/api/payment/verify-order`, `/api/email/send-resume`.

Check response JSON and DTO property names against your actual backend. Razorpay checkout requires the public keyId in the create-order response; never expose the secret key. CORS must allow `http://localhost:5173`.
