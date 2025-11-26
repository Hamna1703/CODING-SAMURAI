import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager handles all database operations for the library management system
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost/librarydb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    
    // Singleton pattern
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        connect();
        if (connection != null) {
            createTablesIfNotExists();
        } else {
            LOGGER.warning("Database connection failed. Please install MySQL server and run database_setup.sql");
        }
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    // Database connection
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Updated driver
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            LOGGER.info("Database connection established successfully");
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC driver not found. Please download mysql-connector-j and place in lib/ folder", ex);
            connection = null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to connect to MySQL database. Please install MySQL server and create database 'librarydb'", ex);
            connection = null;
        }
    }
    
    // Create tables if they don't exist
    private void createTablesIfNotExists() {
        if (connection == null) {
            LOGGER.warning("Cannot create tables - no database connection");
            return;
        }
        try {
            // Create students table
            String createStudentsTable = """
                CREATE TABLE IF NOT EXISTS students (
                    student_id VARCHAR(50) PRIMARY KEY,
                    student_name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    address TEXT,
                    phone_number VARCHAR(20),
                    max_borrow_limit INT DEFAULT 5,
                    current_borrow_count INT DEFAULT 0,
                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            
            // Create books table
            String createBooksTable = """
                CREATE TABLE IF NOT EXISTS books (
                    book_id VARCHAR(50) PRIMARY KEY,
                    title VARCHAR(200) NOT NULL,
                    author VARCHAR(100) NOT NULL,
                    isbn VARCHAR(20) UNIQUE,
                    genre VARCHAR(50),
                    total_copies INT DEFAULT 1,
                    available_copies INT DEFAULT 1,
                    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            
            // Create borrow_records table
            String createBorrowRecordsTable = """
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
                    FOREIGN KEY (student_id) REFERENCES students(student_id),
                    FOREIGN KEY (book_id) REFERENCES books(book_id)
                )
                """;
            
            Statement stmt = connection.createStatement();
            stmt.execute(createStudentsTable);
            stmt.execute(createBooksTable);
            stmt.execute(createBorrowRecordsTable);
            stmt.close();
            
            LOGGER.info("Database tables created/verified successfully");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create database tables", ex);
        }
    }
    
    // Student operations
    public boolean addStudent(Student student) {
        if (connection == null) {
            LOGGER.warning("Cannot add student - no database connection");
            return false;
        }
        String sql = "INSERT INTO students (student_id, student_name, email, address, phone_number, max_borrow_limit) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, student.getStudentId());
            pst.setString(2, student.getStudentName());
            pst.setString(3, student.getEmail());
            pst.setString(4, student.getAddress());
            pst.setString(5, student.getPhoneNumber());
            pst.setInt(6, student.getMaxBorrowLimit());
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to add student", ex);
            return false;
        }
    }
    
    public boolean updateStudent(Student student) {
        if (connection == null) {
            LOGGER.warning("Cannot update student - no database connection");
            return false;
        }
        String sql = "UPDATE students SET student_name=?, email=?, address=?, phone_number=?, max_borrow_limit=? WHERE student_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, student.getStudentName());
            pst.setString(2, student.getEmail());
            pst.setString(3, student.getAddress());
            pst.setString(4, student.getPhoneNumber());
            pst.setInt(5, student.getMaxBorrowLimit());
            pst.setString(6, student.getStudentId());
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update student", ex);
            return false;
        }
    }
    
    public boolean deleteStudent(String studentId) {
        if (connection == null) {
            LOGGER.warning("Cannot delete student - no database connection");
            return false;
        }
        String sql = "DELETE FROM students WHERE student_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, studentId);
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete student", ex);
            return false;
        }
    }
    
    public Student getStudent(String studentId) {
        if (connection == null) {
            LOGGER.warning("Cannot get student - no database connection");
            return null;
        }
        String sql = "SELECT * FROM students WHERE student_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getInt("max_borrow_limit")
                );
                student.setCurrentBorrowCount(rs.getInt("current_borrow_count"));
                return student;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get student", ex);
        }
        return null;
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        if (connection == null) {
            LOGGER.warning("Cannot get students - no database connection");
            return students;
        }
        String sql = "SELECT * FROM students ORDER BY student_name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getInt("max_borrow_limit")
                );
                student.setCurrentBorrowCount(rs.getInt("current_borrow_count"));
                students.add(student);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get all students", ex);
        }
        return students;
    }
    
    // Book operations
    public boolean addBook(Book book) {
        if (connection == null) {
            LOGGER.warning("Cannot add book - no database connection");
            return false;
        }
        String sql = "INSERT INTO books (book_id, title, author, isbn, genre, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, book.getBookId());
            pst.setString(2, book.getTitle());
            pst.setString(3, book.getAuthor());
            pst.setString(4, book.getIsbn());
            pst.setString(5, book.getGenre());
            pst.setInt(6, book.getTotalCopies());
            pst.setInt(7, book.getAvailableCopies());
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to add book", ex);
            return false;
        }
    }
    
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, genre=?, total_copies=?, available_copies=? WHERE book_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getIsbn());
            pst.setString(4, book.getGenre());
            pst.setInt(5, book.getTotalCopies());
            pst.setInt(6, book.getAvailableCopies());
            pst.setString(7, book.getBookId());
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update book", ex);
            return false;
        }
    }
    
    public boolean deleteBook(String bookId) {
        String sql = "DELETE FROM books WHERE book_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, bookId);
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete book", ex);
            return false;
        }
    }
    
    public Book getBook(String bookId) {
        if (connection == null) {
            LOGGER.warning("Cannot get book - no database connection");
            return null;
        }
        String sql = "SELECT * FROM books WHERE book_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, bookId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Book book = new Book(
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("genre"),
                    rs.getInt("total_copies")
                );
                book.setAvailableCopies(rs.getInt("available_copies"));
                return book;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get book", ex);
        }
        return null;
    }
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        if (connection == null) {
            LOGGER.warning("Cannot get books - no database connection");
            return books;
        }
        String sql = "SELECT * FROM books ORDER BY title";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("genre"),
                    rs.getInt("total_copies")
                );
                book.setAvailableCopies(rs.getInt("available_copies"));
                books.add(book);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get all books", ex);
        }
        return books;
    }
    
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        if (connection == null) {
            LOGGER.warning("Cannot search books - no database connection");
            return books;
        }
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? ORDER BY title";
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                    rs.getString("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("genre"),
                    rs.getInt("total_copies")
                );
                book.setAvailableCopies(rs.getInt("available_copies"));
                books.add(book);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to search books", ex);
        }
        return books;
    }
    
    // Borrow record operations
    public boolean addBorrowRecord(BorrowRecord record) {
        String sql = "INSERT INTO borrow_records (record_id, student_id, book_id, borrow_date, due_date, is_returned, fine_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, record.getRecordId());
            pst.setString(2, record.getStudentId());
            pst.setString(3, record.getBookId());
            pst.setDate(4, Date.valueOf(record.getBorrowDate()));
            pst.setDate(5, Date.valueOf(record.getDueDate()));
            pst.setBoolean(6, record.isReturned());
            pst.setDouble(7, record.getFineAmount());
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to add borrow record", ex);
            return false;
        }
    }
    
    public boolean updateBorrowRecord(BorrowRecord record) {
        String sql = "UPDATE borrow_records SET return_date=?, is_returned=?, fine_amount=? WHERE record_id=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setDate(1, record.getReturnDate() != null ? Date.valueOf(record.getReturnDate()) : null);
            pst.setBoolean(2, record.isReturned());
            pst.setDouble(3, record.getFineAmount());
            pst.setString(4, record.getRecordId());
            
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update borrow record", ex);
            return false;
        }
    }
    
    public List<BorrowRecord> getBorrowRecordsByStudent(String studentId) {
        List<BorrowRecord> records = new ArrayList<>();
        if (connection == null) {
            LOGGER.warning("Cannot get borrow records - no database connection");
            return records;
        }
        String sql = "SELECT * FROM borrow_records WHERE student_id=? ORDER BY borrow_date DESC";
        
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                LocalDate returnDate = rs.getDate("return_date") != null ? 
                                     rs.getDate("return_date").toLocalDate() : null;
                
                BorrowRecord record = new BorrowRecord(
                    rs.getString("record_id"),
                    rs.getString("student_id"),
                    rs.getString("book_id"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("due_date").toLocalDate(),
                    returnDate,
                    rs.getBoolean("is_returned"),
                    rs.getDouble("fine_amount")
                );
                records.add(record);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get borrow records", ex);
        }
        return records;
    }
    
    public List<BorrowRecord> getOverdueRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        if (connection == null) {
            LOGGER.warning("Cannot get overdue records - no database connection");
            return records;
        }
        String sql = "SELECT * FROM borrow_records WHERE is_returned=FALSE AND due_date < CURDATE() ORDER BY due_date";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getString("record_id"),
                    rs.getString("student_id"),
                    rs.getString("book_id"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("due_date").toLocalDate(),
                    null,
                    false,
                    rs.getDouble("fine_amount")
                );
                records.add(record);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get overdue records", ex);
        }
        return records;
    }
    
    // Utility methods
    public int getTotalStudents() {
        if (connection == null) {
            LOGGER.warning("Cannot count students - no database connection");
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get total students count", ex);
        }
        return 0;
    }
    
    public int getTotalBooks() {
        if (connection == null) {
            LOGGER.warning("Cannot count books - no database connection");
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get total books count", ex);
        }
        return 0;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to close database connection", ex);
        }
    }
}