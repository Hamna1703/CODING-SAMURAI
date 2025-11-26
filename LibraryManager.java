import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * LibraryManager is the main service class that handles all library operations
 */
public class LibraryManager {
    private DatabaseManager dbManager;
    private static final Logger LOGGER = Logger.getLogger(LibraryManager.class.getName());
    private static final int DEFAULT_BORROW_PERIOD = 14; // 14 days
    
    // Singleton pattern
    private static LibraryManager instance;
    
    private LibraryManager() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }
    
    // Student management methods
    public boolean addStudent(String studentId, String name, String email, String address, String phone) {
        try {
            if (getStudent(studentId) != null) {
                LOGGER.warning("Student with ID " + studentId + " already exists");
                return false;
            }
            
            Student student = new Student(studentId, name, email, address, phone, 5);
            boolean result = dbManager.addStudent(student);
            
            if (result) {
                LOGGER.info("Student added successfully: " + studentId);
            } else {
                LOGGER.warning("Failed to add student: " + studentId);
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding student", e);
            return false;
        }
    }
    
    public boolean updateStudent(String studentId, String name, String email, String address, String phone) {
        try {
            Student existingStudent = getStudent(studentId);
            if (existingStudent == null) {
                LOGGER.warning("Student with ID " + studentId + " not found");
                return false;
            }
            
            Student updatedStudent = new Student(studentId, name, email, address, phone, existingStudent.getMaxBorrowLimit());
            updatedStudent.setCurrentBorrowCount(existingStudent.getCurrentBorrowCount());
            
            boolean result = dbManager.updateStudent(updatedStudent);
            
            if (result) {
                LOGGER.info("Student updated successfully: " + studentId);
            } else {
                LOGGER.warning("Failed to update student: " + studentId);
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating student", e);
            return false;
        }
    }
    
    public boolean removeStudent(String studentId) {
        try {
            // Check if student has any unreturned books
            List<BorrowRecord> activeRecords = getActiveBorrowRecords(studentId);
            if (!activeRecords.isEmpty()) {
                LOGGER.warning("Cannot remove student " + studentId + " - has unreturned books");
                return false;
            }
            
            boolean result = dbManager.deleteStudent(studentId);
            
            if (result) {
                LOGGER.info("Student removed successfully: " + studentId);
            } else {
                LOGGER.warning("Failed to remove student: " + studentId);
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing student", e);
            return false;
        }
    }
    
    public Student getStudent(String studentId) {
        return dbManager.getStudent(studentId);
    }
    
    public List<Student> getAllStudents() {
        return dbManager.getAllStudents();
    }
    
    // Book management methods
    public boolean addBook(String bookId, String title, String author, String isbn, String genre, int totalCopies) {
        try {
            if (getBook(bookId) != null) {
                LOGGER.warning("Book with ID " + bookId + " already exists");
                return false;
            }
            
            Book book = new Book(bookId, title, author, isbn, genre, totalCopies);
            boolean result = dbManager.addBook(book);
            
            if (result) {
                LOGGER.info("Book added successfully: " + bookId);
            } else {
                LOGGER.warning("Failed to add book: " + bookId);
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding book", e);
            return false;
        }
    }
    
    public boolean updateBook(String bookId, String title, String author, String isbn, String genre, int totalCopies) {
        try {
            Book existingBook = getBook(bookId);
            if (existingBook == null) {
                LOGGER.warning("Book with ID " + bookId + " not found");
                return false;
            }
            
            // Calculate available copies based on the change in total copies
            int copiesDifference = totalCopies - existingBook.getTotalCopies();
            int newAvailableCopies = Math.max(0, existingBook.getAvailableCopies() + copiesDifference);
            
            Book updatedBook = new Book(bookId, title, author, isbn, genre, totalCopies);
            updatedBook.setAvailableCopies(newAvailableCopies);
            
            boolean result = dbManager.updateBook(updatedBook);
            
            if (result) {
                LOGGER.info("Book updated successfully: " + bookId);
            } else {
                LOGGER.warning("Failed to update book: " + bookId);
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating book", e);
            return false;
        }
    }
    
    public boolean removeBook(String bookId) {
        try {
            Book book = getBook(bookId);
            if (book == null) {
                LOGGER.warning("Book with ID " + bookId + " not found");
                return false;
            }
            
            // Check if any copies are currently borrowed
            if (book.getAvailableCopies() < book.getTotalCopies()) {
                LOGGER.warning("Cannot remove book " + bookId + " - some copies are currently borrowed");
                return false;
            }
            
            boolean result = dbManager.deleteBook(bookId);
            
            if (result) {
                LOGGER.info("Book removed successfully: " + bookId);
            } else {
                LOGGER.warning("Failed to remove book: " + bookId);
            }
            
            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing book", e);
            return false;
        }
    }
    
    public Book getBook(String bookId) {
        return dbManager.getBook(bookId);
    }
    
    public List<Book> getAllBooks() {
        return dbManager.getAllBooks();
    }
    
    public List<Book> searchBooks(String keyword) {
        return dbManager.searchBooks(keyword);
    }
    
    public List<Book> getAvailableBooks() {
        return getAllBooks().stream()
                .filter(book -> book.isAvailable())
                .collect(Collectors.toList());
    }
    
    // Borrowing and returning methods
    public String borrowBook(String studentId, String bookId) {
        return borrowBook(studentId, bookId, DEFAULT_BORROW_PERIOD);
    }
    
    public String borrowBook(String studentId, String bookId, int borrowPeriodDays) {
        try {
            // Validate student
            Student student = getStudent(studentId);
            if (student == null) {
                return "Error: Student not found";
            }
            
            // Check if student can borrow more books
            if (!student.canBorrowMore()) {
                return "Error: Student has reached maximum borrowing limit (" + student.getMaxBorrowLimit() + ")";
            }
            
            // Validate book
            Book book = getBook(bookId);
            if (book == null) {
                return "Error: Book not found";
            }
            
            // Check if book is available
            if (!book.isAvailable()) {
                return "Error: Book is not available for borrowing";
            }
            
            // Generate unique record ID
            String recordId = "BR" + UUID.randomUUID().toString().substring(0, 8);
            
            // Create borrow record
            BorrowRecord borrowRecord = new BorrowRecord(recordId, studentId, bookId, borrowPeriodDays);
            
            // Update book availability
            book.borrowBook();
            
            // Update student's borrow count
            student.borrowBook();
            
            // Save to database
            boolean bookUpdated = dbManager.updateBook(book);
            boolean studentUpdated = dbManager.updateStudent(student);
            boolean recordAdded = dbManager.addBorrowRecord(borrowRecord);
            
            if (bookUpdated && studentUpdated && recordAdded) {
                LOGGER.info("Book borrowed successfully: " + bookId + " by student " + studentId);
                return "Success: Book borrowed successfully. Due date: " + borrowRecord.getFormattedDueDate() + ". Record ID: " + recordId;
            } else {
                // Rollback changes if any operation failed
                book.returnBook();
                student.returnBook();
                return "Error: Failed to process borrowing transaction";
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error borrowing book", e);
            return "Error: " + e.getMessage();
        }
    }
    
    public String returnBook(String studentId, String bookId) {
        try {
            // Find the active borrow record
            List<BorrowRecord> borrowRecords = dbManager.getBorrowRecordsByStudent(studentId);
            BorrowRecord activeRecord = null;
            
            for (BorrowRecord record : borrowRecords) {
                if (record.getBookId().equals(bookId) && !record.isReturned()) {
                    activeRecord = record;
                    break;
                }
            }
            
            if (activeRecord == null) {
                return "Error: No active borrow record found for this book and student";
            }
            
            // Get student and book
            Student student = getStudent(studentId);
            Book book = getBook(bookId);
            
            if (student == null || book == null) {
                return "Error: Student or book not found";
            }
            
            // Process return
            activeRecord.returnBook();
            book.returnBook();
            student.returnBook();
            
            // Update database
            boolean recordUpdated = dbManager.updateBorrowRecord(activeRecord);
            boolean bookUpdated = dbManager.updateBook(book);
            boolean studentUpdated = dbManager.updateStudent(student);
            
            if (recordUpdated && bookUpdated && studentUpdated) {
                LOGGER.info("Book returned successfully: " + bookId + " by student " + studentId);
                
                String message = "Success: Book returned successfully on " + activeRecord.getFormattedReturnDate();
                if (activeRecord.getFineAmount() > 0) {
                    message += ". Fine amount: $" + String.format("%.2f", activeRecord.getFineAmount());
                }
                return message;
            } else {
                return "Error: Failed to process return transaction";
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error returning book", e);
            return "Error: " + e.getMessage();
        }
    }
    
    public List<BorrowRecord> getActiveBorrowRecords(String studentId) {
        return dbManager.getBorrowRecordsByStudent(studentId).stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toList());
    }
    
    public List<BorrowRecord> getBorrowHistory(String studentId) {
        return dbManager.getBorrowRecordsByStudent(studentId);
    }
    
    public List<BorrowRecord> getOverdueBooks() {
        return dbManager.getOverdueRecords();
    }
    
    // Renewal method
    public String renewBook(String studentId, String bookId, int additionalDays) {
        try {
            // Find the active borrow record
            List<BorrowRecord> borrowRecords = dbManager.getBorrowRecordsByStudent(studentId);
            BorrowRecord activeRecord = null;
            
            for (BorrowRecord record : borrowRecords) {
                if (record.getBookId().equals(bookId) && !record.isReturned()) {
                    activeRecord = record;
                    break;
                }
            }
            
            if (activeRecord == null) {
                return "Error: No active borrow record found for this book and student";
            }
            
            // Check if book can be renewed (not overdue by more than 7 days)
            if (activeRecord.getDaysOverdue() > 7) {
                return "Error: Book is too overdue to be renewed. Please return it and pay the fine.";
            }
            
            // Extend due date
            LocalDate newDueDate = activeRecord.getDueDate().plusDays(additionalDays);
            BorrowRecord renewedRecord = new BorrowRecord(
                activeRecord.getRecordId(),
                activeRecord.getStudentId(),
                activeRecord.getBookId(),
                activeRecord.getBorrowDate(),
                newDueDate,
                null,
                false,
                0.0
            );
            
            boolean updated = dbManager.updateBorrowRecord(renewedRecord);
            
            if (updated) {
                LOGGER.info("Book renewed successfully: " + bookId + " by student " + studentId);
                return "Success: Book renewed successfully. New due date: " + renewedRecord.getFormattedDueDate();
            } else {
                return "Error: Failed to renew book";
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error renewing book", e);
            return "Error: " + e.getMessage();
        }
    }
    
    // Statistics methods
    public LibraryStatistics getLibraryStatistics() {
        try {
            int totalStudents = dbManager.getTotalStudents();
            int totalBooks = dbManager.getTotalBooks();
            List<BorrowRecord> overdueRecords = getOverdueBooks();
            int overdueCount = overdueRecords.size();
            
            // Calculate total borrowed books (active records)
            int totalBorrowedBooks = 0;
            for (Student student : getAllStudents()) {
                totalBorrowedBooks += student.getCurrentBorrowCount();
            }
            
            // Calculate total fine amount
            double totalFines = overdueRecords.stream()
                    .mapToDouble(BorrowRecord::getFineAmount)
                    .sum();
            
            return new LibraryStatistics(totalStudents, totalBooks, totalBorrowedBooks, overdueCount, totalFines);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting library statistics", e);
            return new LibraryStatistics(0, 0, 0, 0, 0.0);
        }
    }
    
    // Inner class for statistics
    public static class LibraryStatistics {
        private final int totalStudents;
        private final int totalBooks;
        private final int totalBorrowedBooks;
        private final int overdueBooks;
        private final double totalFines;
        
        public LibraryStatistics(int totalStudents, int totalBooks, int totalBorrowedBooks, int overdueBooks, double totalFines) {
            this.totalStudents = totalStudents;
            this.totalBooks = totalBooks;
            this.totalBorrowedBooks = totalBorrowedBooks;
            this.overdueBooks = overdueBooks;
            this.totalFines = totalFines;
        }
        
        public int getTotalStudents() { return totalStudents; }
        public int getTotalBooks() { return totalBooks; }
        public int getTotalBorrowedBooks() { return totalBorrowedBooks; }
        public int getOverdueBooks() { return overdueBooks; }
        public double getTotalFines() { return totalFines; }
        public int getAvailableBooks() { return totalBooks - totalBorrowedBooks; }
        
        @Override
        public String toString() {
            return String.format("Library Statistics:\n" +
                               "- Total Students: %d\n" +
                               "- Total Books: %d\n" +
                               "- Available Books: %d\n" +
                               "- Borrowed Books: %d\n" +
                               "- Overdue Books: %d\n" +
                               "- Total Fines: $%.2f",
                               totalStudents, totalBooks, getAvailableBooks(), totalBorrowedBooks, overdueBooks, totalFines);
        }
    }
    
    // Cleanup method
    public void shutdown() {
        if (dbManager != null) {
            dbManager.closeConnection();
        }
    }
}