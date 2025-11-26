-- Library Management System Database Setup
-- Run this script to create the required database and tables

-- Create database
CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(50) PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    address TEXT,
    phone_number VARCHAR(20),
    max_borrow_limit INT DEFAULT 5,
    current_borrow_count INT DEFAULT 0,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create books table
CREATE TABLE IF NOT EXISTS books (
    book_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    genre VARCHAR(50),
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create borrow_records table
CREATE TABLE IF NOT EXISTS borrow_records (
    record_id VARCHAR(50) PRIMARY KEY,
    student_id VARCHAR(50),
    book_id VARCHAR(50),
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    is_returned BOOLEAN DEFAULT FALSE,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
);

-- Insert sample data for testing
INSERT IGNORE INTO students (student_id, student_name, email, address, phone_number) VALUES 
('STU001', 'Alice Johnson', 'alice@email.com', '123 Main St', '555-0101'),
('STU002', 'Bob Smith', 'bob@email.com', '456 Oak Ave', '555-0102'),
('STU003', 'Carol Davis', 'carol@email.com', '789 Pine Rd', '555-0103');

INSERT IGNORE INTO books (book_id, title, author, isbn, genre, total_copies, available_copies) VALUES 
('BK001', 'Java Programming', 'John Doe', '978-0134685991', 'Programming', 3, 3),
('BK002', 'Database Systems', 'Jane Smith', '978-0321197844', 'Computer Science', 2, 2),
('BK003', 'The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'Literature', 5, 5),
('BK004', 'To Kill a Mockingbird', 'Harper Lee', '978-0061120084', 'Literature', 4, 4),
('BK005', '1984', 'George Orwell', '978-0451524935', 'Fiction', 3, 3);

-- Create indexes for better performance
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_borrow_records_student ON borrow_records(student_id);
CREATE INDEX idx_borrow_records_book ON borrow_records(book_id);
CREATE INDEX idx_borrow_records_date ON borrow_records(borrow_date);
CREATE INDEX idx_borrow_records_due ON borrow_records(due_date);

COMMIT;

SELECT 'Database setup completed successfully!' as Status;