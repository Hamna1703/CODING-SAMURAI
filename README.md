# Library Management System

A comprehensive Java-based library management system that allows librarians to manage books, students, and borrowing operations efficiently.

## Features

### Student Management
- ✅ Add, update, delete, and view students
- ✅ Track student borrowing history
- ✅ Set borrowing limits per student
- ✅ View student details and current borrowed books

### Book Management
- ✅ Add, update, delete, and view books
- ✅ Track book availability and copies
- ✅ Organize books by genre, author, and ISBN
- ✅ Search books by title, author, or ISBN

### Borrowing System
- ✅ Book checkout and return operations
- ✅ Automatic due date calculation (14 days default)
- ✅ Book renewal functionality
- ✅ Overdue tracking with automatic fine calculation
- ✅ Prevent borrowing if limit exceeded

### Reporting & Statistics
- ✅ Library statistics dashboard
- ✅ Overdue books report
- ✅ Student and book reports
- ✅ Fine tracking and management

## System Architecture

### Core Classes
- **`App.java`** - Main application with console-based menu interface
- **`LibraryManager.java`** - Core business logic and service layer
- **`DatabaseManager.java`** - Database operations and connection management
- **`Student.java`** - Student entity model
- **`Book.java`** - Book entity model
- **`BorrowRecord.java`** - Borrowing transaction model

### Database Schema
- **students** - Student information and borrowing limits
- **books** - Book catalog with availability tracking
- **borrow_records** - Transaction history with due dates and fines

## Prerequisites

### Software Requirements
1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

2. **MySQL Server 8.0 or higher**
   - Download from [MySQL Official Site](https://dev.mysql.com/downloads/mysql/)

3. **MySQL JDBC Driver**
   - Download `mysql-connector-java-8.0.33.jar` or newer
   - Place in the `lib/` directory

### Development Environment
- **VS Code** with Java Extension Pack
- **MySQL Workbench** (optional, for database management)

## Installation & Setup

### 1. Database Setup
1. Install and start MySQL Server
2. Create database and tables:
   ```sql
   mysql -u root -p < database_setup.sql
   ```
   Or run the SQL commands in `database_setup.sql` manually

### 2. JDBC Driver Setup
1. Download MySQL JDBC Driver (Connector/J)
2. Copy `mysql-connector-java-X.X.XX.jar` to the `lib/` folder
3. VS Code should automatically detect and include it in the classpath

### 3. Database Configuration
Update database connection settings in `DatabaseManager.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost/librarydb";
private static final String DB_USERNAME = "root";
private static final String DB_PASSWORD = "your_password_here";
```

### 4. Compile and Run
```bash
# Navigate to project directory
cd StudentLibrary

# Compile (VS Code handles this automatically)
javac -cp "lib/*" -d bin src/*.java

# Run the application
java -cp "bin;lib/*" App
```

## Usage Guide

### Starting the Application
Run the main class `App` to launch the console interface:

```
=================================
   LIBRARY MANAGEMENT SYSTEM    
=================================

========== MAIN MENU ==========
1. Manage Students
2. Manage Books
3. Borrowing & Returns
4. Reports & Statistics
5. Search System
0. Exit
===============================
```

### Key Operations

#### Student Management
- **Add Student**: Enter student ID, name, email, address, and phone
- **Update Student**: Modify existing student information
- **View Details**: See student info and borrowing history
- **Remove Student**: Delete student (only if no unreturned books)

#### Book Management
- **Add Book**: Enter book details including copies available
- **Update Book**: Modify book information and copy counts
- **Search Books**: Find books by title, author, or ISBN
- **Remove Book**: Delete book (only if no borrowed copies)

#### Borrowing Operations
- **Borrow Book**: Student ID + Book ID → 14-day loan
- **Return Book**: Automatic fine calculation for overdue returns
- **Renew Book**: Extend due date (if not severely overdue)
- **View Overdue**: List all overdue books with fines

### Sample Data
The system includes pre-loaded sample data:
- **Students**: Alice Johnson, Bob Smith, Carol Davis
- **Books**: Java Programming, Database Systems, The Great Gatsby, etc.

## Database Schema Details

### Students Table
```sql
student_id (VARCHAR 50, PK)
student_name (VARCHAR 100)
email (VARCHAR 100, UNIQUE)
address (TEXT)
phone_number (VARCHAR 20)
max_borrow_limit (INT, default 5)
current_borrow_count (INT, default 0)
created_date (TIMESTAMP)
```

### Books Table
```sql
book_id (VARCHAR 50, PK)
title (VARCHAR 200)
author (VARCHAR 100)
isbn (VARCHAR 20, UNIQUE)
genre (VARCHAR 50)
total_copies (INT, default 1)
available_copies (INT, default 1)
created_date (TIMESTAMP)
```

### Borrow Records Table
```sql
record_id (VARCHAR 50, PK)
student_id (VARCHAR 50, FK)
book_id (VARCHAR 50, FK)
borrow_date (DATE)
due_date (DATE)
return_date (DATE, nullable)
is_returned (BOOLEAN, default false)
fine_amount (DECIMAL 10,2, default 0.00)
created_date (TIMESTAMP)
```

## Business Rules

### Borrowing Limits
- Default: 5 books per student
- Configurable per student
- Enforced during checkout

### Due Dates & Fines
- Default borrowing period: 14 days
- Fine rate: $0.50 per day overdue
- Automatic fine calculation on return

### Renewals
- Available if not overdue by more than 7 days
- Extends due date by specified number of days
- Prevents excessive renewals for severely overdue books

## Error Handling

The system includes comprehensive error handling for:
- Database connection failures
- Invalid student/book IDs
- Borrowing limit violations
- Book availability constraints
- Data validation errors

## Future Enhancements

Potential improvements for the system:
- **Web-based GUI** using Spring Boot + Thymeleaf
- **REST API** for mobile app integration
- **Email notifications** for due date reminders
- **Barcode scanning** support
- **Advanced reporting** with charts and graphs
- **User roles** (Admin, Librarian, Student)
- **Book reservations** system
- **Multi-branch** support

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Check MySQL server is running
   - Verify username/password in `DatabaseManager.java`
   - Ensure database `librarydb` exists

2. **ClassNotFoundException: com.mysql.cj.jdbc.Driver**
   - Download MySQL JDBC driver
   - Place in `lib/` folder
   - Restart VS Code

3. **SQLException: Table doesn't exist**
   - Run `database_setup.sql` to create tables
   - Check database name in connection string

4. **Compilation Errors**
   - Ensure JDK 17+ is installed
   - Check all source files are in `src/` directory
   - Verify JDBC driver is in `lib/` folder

## Project Structure

```
StudentLibrary/
├── src/
│   ├── App.java                 # Main application entry point
│   ├── LibraryManager.java      # Core business logic
│   ├── DatabaseManager.java     # Database operations
│   ├── Student.java             # Student entity
│   ├── Book.java               # Book entity
│   └── BorrowRecord.java       # Borrow transaction entity
├── lib/
│   └── mysql-connector-java-*.jar  # JDBC driver
├── bin/                        # Compiled classes (auto-generated)
├── database_setup.sql          # Database schema and sample data
└── README.md                   # This documentation
```

## Contributing

This is an educational project demonstrating object-oriented programming principles, database integration, and software architecture best practices in Java.

## License

This project is for educational purposes. Feel free to use and modify as needed.

---

**Note**: This system uses a console-based interface. For production use, consider implementing a modern web-based or desktop GUI interface.
