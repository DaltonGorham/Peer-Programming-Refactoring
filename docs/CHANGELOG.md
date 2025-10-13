# Change Log

---
## 1. Software Structure
Migrated the monolithic Main class from the original code to a layered architecture with the following layers:

- model
- repo
- service
- app
- util

Interfaces for repositories provide a contract for writing to CSV files:
- StudentRepository
- CouresRepository
- EnrollmentRepository

## 2. Coding Practices
Made the program more SOLID:
- Single Responsibility (multiple classes)
- Open/Closed
- Liskov Substitution
- Interface Segregation
- Dependency Inversion

Centralized input validation for email, course capacity -- code now throws ValidationException for invalid inputs. These exceptions are caught and erorr messages are displayed.
Improved logging capapilities with Logger

## 3. Persistence
Three classes handle all of the parsing and writing to CSV files for students, courses, and enrollments:
- StudentCsvRepository
- CourseCsvRepository
- EnrollmentCsvRepository

Missing files are now created on startup