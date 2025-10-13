# System Architecture


---

## 1. Overview

The Course Registration System is a command-line Java application that allows students to view courses, register, and manage enrollments.  
It is structured in **layered architecture**, separating concerns into five major packages:

- `edu.uca.app` – User interface and program entry points
- `edu.uca.model` – Domain models (Student, Course)
- `edu.uca.service` – Business logic and validation
- `edu.uca.repo` – Repository interfaces
- `edu.uca.repo.csv` – CSV-based repository implementations
- `edu.uca.util` – Configuration loading and utility helpers

---

## 2. Architectural Layers

### 2.1 App Layer (`edu.uca.app`)

**Responsibilities**
- Provides a CLI interface for users to interact with the system.
- Handles command input, menu display, and output formatting.
- Delegates all domain operations to the `RegistrationService`.

**Classes**
- `MainCli`: Starts the program and manages the main user interaction loop.

---

### 2.2 Model Layer (`edu.uca.model`)

**Responsibilities**
- Represents the domain entities: `Student` and `Course`.
- Contains no business or persistence logic.

**Classes**
- `Student`: Immutable record storing ID, name, and enrolled courses.
- `Course`: Immutable record storing course code, title, and capacity.

Both are implemented as `record`

---

### 2.3 Service Layer (`edu.uca.service`)

**Responsibilities**
- Implements the business logic of registration and course management.
- Validates input and enforces registration constraints

**Classes**
- `RegistrationService`: Central service class responsible for:
    - Registering and dropping students.
    - Listing courses and enrollment data.
    - Managing enrollment and waitlists.

The service currently depends on concrete CSV repository implementations for simplicity.

---

### 2.4 Repository Layer

#### Interfaces (`edu.uca.repo`)
Defines abstract contracts for data access and storage.

**Classes**
- `CourseRepository`
- `StudentRepository`
- `EnrollmentRepository`

These interfaces define operations for reading and writing course, student, and enrollment data.

#### Implementations (`edu.uca.repo.csv`)
Provides file-based persistence using CSV files.

**Classes**
- `CourseCsvRepository`
- `StudentCsvRepository`
- `EnrollmentCsvRepository`

---

### 2.5 Utility Layer (`edu.uca.util`)

**Responsibilities**
- Provides supporting functions

**Classes**
- `ConfigLoader`: Reads `application.properties` to load configuration values (like file paths).
- `Utilities`: Contains static helper methods for console formatting.

---

## 3. Package Diagram

The following diagram illustrates the relationship between the system’s packages:

![Package Diagram](Package%20Diagram.png)

