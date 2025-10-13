# UCA Course Registration – Refactored

This is a refactored version of the original monolithic Java CLI application for managing student course registration at UCA. The system now follows a layered architecture with clear separation of concerns, modular design, and CSV-based persistence.

## Run
```bash
mvn -q -DskipTests package
java -jar target/course-registration-0.1.0.jar
```

## Documentation
All required documentation can be found in the docs\ directory at the root.
