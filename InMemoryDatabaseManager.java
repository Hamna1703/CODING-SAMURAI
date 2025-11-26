import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory Database Manager - for testing without MySQL server
 * This version stores data in memory instead of requiring MySQL
 */
public class InMemoryDatabaseManager {
    private Map<String, Student> students = new HashMap<>();
    private Map<String, Book> books = new HashMap<>();
    private Map<String, BorrowRecord> borrowRecords = new HashMap<>();
    private static InMemoryDatabaseManager instance;
    
    private InMemoryDatabaseManager() {
        initializeSampleData();
    }
    
    public static InMemoryDatabaseManager getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabaseManager();
        }
        return instance;
    }
    
    private void initializeSampleData() {
        // Add sample students
        Student s1 = new Student("STU001", "Alice Johnson", "alice@email.com", "123 Main St", "555-0101", 5);
        Student s2 = new Student("STU002", "Bob Smith", "bob@email.com", "456 Oak Ave", "555-0102", 5);
        Student s3 = new Student("STU003", "Carol Davis", "carol@email.com", "789 Pine Rd", "555-0103", 5);
        
        students.put(s1.getStudentId(), s1);
        students.put(s2.getStudentId(), s2);
        students.put(s3.getStudentId(), s3);
        
        // Add sample books
        Book b1 = new Book("BK001", "Java Programming", "John Doe", "978-0134685991", "Programming", 3);
        Book b2 = new Book("BK002", "Database Systems", "Jane Smith", "978-0321197844", "Computer Science", 2);
        Book b3 = new Book("BK003", "The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Literature", 5);
        Book b4 = new Book("BK004", "To Kill a Mockingbird", "Harper Lee", "978-0061120084", "Literature", 4);
        Book b5 = new Book("BK005", "1984", "George Orwell", "978-0451524935", "Fiction", 3);
        
        books.put(b1.getBookId(), b1);
        books.put(b2.getBookId(), b2);
        books.put(b3.getBookId(), b3);
        books.put(b4.getBookId(), b4);
        books.put(b5.getBookId(), b5);
    }
    
    // Student operations
    public boolean addStudent(Student student) {
        if (students.containsKey(student.getStudentId())) {
            return false;
        }
        students.put(student.getStudentId(), student);
        return true;
    }
    
    public boolean updateStudent(Student student) {
        if (!students.containsKey(student.getStudentId())) {
            return false;
        }
        students.put(student.getStudentId(), student);
        return true;
    }
    
    public boolean deleteStudent(String studentId) {
        return students.remove(studentId) != null;
    }
    
    public Student getStudent(String studentId) {
        return students.get(studentId);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    // Book operations
    public boolean addBook(Book book) {
        if (books.containsKey(book.getBookId())) {
            return false;
        }
        books.put(book.getBookId(), book);
        return true;
    }
    
    public boolean updateBook(Book book) {
        if (!books.containsKey(book.getBookId())) {
            return false;
        }
        books.put(book.getBookId(), book);
        return true;
    }
    
    public boolean deleteBook(String bookId) {
        return books.remove(bookId) != null;
    }
    
    public Book getBook(String bookId) {
        return books.get(bookId);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    public List<Book> searchBooks(String keyword) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                               book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) ||
                               (book.getIsbn() != null && book.getIsbn().contains(keyword)))
                .collect(Collectors.toList());
    }
    
    // Borrow record operations
    public boolean addBorrowRecord(BorrowRecord record) {
        borrowRecords.put(record.getRecordId(), record);
        return true;
    }
    
    public boolean updateBorrowRecord(BorrowRecord record) {
        if (!borrowRecords.containsKey(record.getRecordId())) {
            return false;
        }
        borrowRecords.put(record.getRecordId(), record);
        return true;
    }
    
    public List<BorrowRecord> getBorrowRecordsByStudent(String studentId) {
        return borrowRecords.values().stream()
                .filter(record -> record.getStudentId().equals(studentId))
                .sorted((r1, r2) -> r2.getBorrowDate().compareTo(r1.getBorrowDate()))
                .collect(Collectors.toList());
    }
    
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecords.values().stream()
                .filter(record -> !record.isReturned() && record.isOverdue())
                .collect(Collectors.toList());
    }
    
    // Utility methods
    public int getTotalStudents() {
        return students.size();
    }
    
    public int getTotalBooks() {
        return books.size();
    }
    
    public void closeConnection() {
        // Nothing to close for in-memory storage
        System.out.println("In-memory database closed.");
    }
}