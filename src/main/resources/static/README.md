# Spring Boot & Angular Project Generator

This project allows you to dynamically generate Angular services from your Spring Boot backend based on custom table definitions. It also manages project settings and configurations for easy integration.

## 🔧 **Features**
- Generates complete Angular services dynamically.
- CRUD operations auto-generated for each custom entity.
- Supports project settings including database, build type, and dependencies.
- Generates a ZIP archive with a complete project structure.

## 🚀 **Getting Started**

### Prerequisites
- Java 17 or higher
- MySQL
- Node.js and Angular CLI

### Backend Setup (Spring Boot)
1. Clone the repository.
2. Configure `application.properties` for your MySQL connection.
3. Run the application:
   ```bash
   ./mvnw spring-boot:run

