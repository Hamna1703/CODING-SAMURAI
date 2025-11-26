import java.util.List;
import java.util.Scanner;

/**
 * Library Management System - Main Application
 * A comprehensive system for managing books, students, and borrowing records
 */
public class App {
    private static LibraryManager libraryManager;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM    ");
        System.out.println("=================================");
        
        // Initialize the library manager
        libraryManager = LibraryManager.getInstance();
        
        // Add some sample data for testing
        initializeSampleData();
        
        // Run the main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageBooks();
                case 3 -> manageBorrowing();
                case 4 -> viewReports();
                case 5 -> searchSystem();
                case 0 -> {
                    System.out.println("Shutting down Library Management System...");
                    libraryManager.shutdown();
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
        System.out.println("Thank you for using Library Management System!");
    }
    
    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Books");
        System.out.println("3. Borrowing & Returns");
        System.out.println("4. Reports & Statistics");
        System.out.println("5. Search System");
        System.out.println("0. Exit");
        System.out.println("===============================");
    }
    
    // Student Management
    private static void manageStudents() {
        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Remove Student");
            System.out.println("5. View Student Details");
            System.out.println("0. Back to Main Menu");
            System.out.println("===============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> updateStudent();
                case 4 -> removeStudent();
                case 5 -> viewStudentDetails();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addStudent() {
        System.out.println("\n--- Add New Student ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        
        if (libraryManager.addStudent(studentId, name, email, address, phone)) {
            System.out.println("✅ Student added successfully!");
        } else {
            System.out.println("❌ Failed to add student. Check if ID already exists.");
        }
    }
    
    private static void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = libraryManager.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.printf("%-10s %-20s %-25s %-15s %-10s%n", 
                            "ID", "Name", "Email", "Phone", "Books");
            System.out.println("=".repeat(85));
            
            for (Student student : students) {
                System.out.printf("%-10s %-20s %-25s %-15s %-10s%n",
                                student.getStudentId(),
                                student.getStudentName(),
                                student.getEmail(),
                                student.getPhoneNumber() != null ? student.getPhoneNumber() : "N/A",
                                student.getCurrentBorrowCount() + "/" + student.getMaxBorrowLimit());
            }
        }
    }
    
    private static void updateStudent() {
        System.out.println("\n--- Update Student ---");
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine();
        
        Student existingStudent = libraryManager.getStudent(studentId);
        if (existingStudent == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        
        System.out.println("Current details: " + existingStudent);
        System.out.print("Enter new name (current: " + existingStudent.getStudentName() + "): ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) name = existingStudent.getStudentName();
        
        System.out.print("Enter new email (current: " + existingStudent.getEmail() + "): ");
        String email = scanner.nextLine();
        if (email.trim().isEmpty()) email = existingStudent.getEmail();
        
        System.out.print("Enter new address (current: " + existingStudent.getAddress() + "): ");
        String address = scanner.nextLine();
        if (address.trim().isEmpty()) address = existingStudent.getAddress();
        
        System.out.print("Enter new phone (current: " + existingStudent.getPhoneNumber() + "): ");
        String phone = scanner.nextLine();
        if (phone.trim().isEmpty()) phone = existingStudent.getPhoneNumber();
        
        if (libraryManager.updateStudent(studentId, name, email, address, phone)) {
            System.out.println("✅ Student updated successfully!");
        } else {
            System.out.println("❌ Failed to update student.");
        }
    }
    
    private static void removeStudent() {
        System.out.println("\n--- Remove Student ---");
        System.out.print("Enter Student ID to remove: ");
        String studentId = scanner.nextLine();
        
        Student student = libraryManager.getStudent(studentId);
        if (student == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        
        System.out.println("Student to remove: " + student);
        System.out.print("Are you sure you want to remove this student? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            if (libraryManager.removeStudent(studentId)) {
                System.out.println("✅ Student removed successfully!");
            } else {
                System.out.println("❌ Failed to remove student. Check if they have unreturned books.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
    }
    
    private static void viewStudentDetails() {
        System.out.println("\n--- Student Details ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = libraryManager.getStudent(studentId);
        if (student == null) {
            System.out.println("❌ Student not found.");
            return;
        }
        
        System.out.println("\n" + student);
        
        // Show borrowing history
        List<BorrowRecord> history = libraryManager.getBorrowHistory(studentId);
        if (!history.isEmpty()) {
            System.out.println("\nBorrowing History:");
            System.out.printf("%-15s %-12s %-12s %-12s %-8s %-8s%n", 
                            "Book ID", "Borrow Date", "Due Date", "Return Date", "Returned", "Fine");
            System.out.println("=".repeat(75));
            
            for (BorrowRecord record : history) {
                System.out.printf("%-15s %-12s %-12s %-12s %-8s $%-7.2f%n",
                                record.getBookId(),
                                record.getFormattedBorrowDate(),
                                record.getFormattedDueDate(),
                                record.getFormattedReturnDate(),
                                record.isReturned() ? "Yes" : "No",
                                record.getFineAmount());
            }
        }
    }
    
    // Book Management
    private static void manageBooks() {
        while (true) {
            System.out.println("\n===== BOOK MANAGEMENT =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Update Book");
            System.out.println("4. Remove Book");
            System.out.println("5. View Available Books");
            System.out.println("0. Back to Main Menu");
            System.out.println("============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewAllBooks();
                case 3 -> updateBook();
                case 4 -> removeBook();
                case 5 -> viewAvailableBooks();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addBook() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        int copies = getIntInput("Enter number of copies: ");
        
        if (libraryManager.addBook(bookId, title, author, isbn, genre, copies)) {
            System.out.println("✅ Book added successfully!");
        } else {
            System.out.println("❌ Failed to add book. Check if ID already exists.");
        }
    }
    
    private static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = libraryManager.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.printf("%-10s %-25s %-20s %-15s %-15s %-10s%n", 
                            "ID", "Title", "Author", "Genre", "ISBN", "Available");
            System.out.println("=".repeat(100));
            
            for (Book book : books) {
                System.out.printf("%-10s %-25s %-20s %-15s %-15s %-10s%n",
                                book.getBookId(),
                                truncate(book.getTitle(), 25),
                                truncate(book.getAuthor(), 20),
                                truncate(book.getGenre(), 15),
                                book.getIsbn(),
                                book.getAvailableCopies() + "/" + book.getTotalCopies());
            }
        }
    }
    
    private static void updateBook() {
        System.out.println("\n--- Update Book ---");
        System.out.print("Enter Book ID to update: ");
        String bookId = scanner.nextLine();
        
        Book existingBook = libraryManager.getBook(bookId);
        if (existingBook == null) {
            System.out.println("❌ Book not found.");
            return;
        }
        
        System.out.println("Current details: " + existingBook);
        System.out.print("Enter new title (current: " + existingBook.getTitle() + "): ");
        String title = scanner.nextLine();
        if (title.trim().isEmpty()) title = existingBook.getTitle();
        
        System.out.print("Enter new author (current: " + existingBook.getAuthor() + "): ");
        String author = scanner.nextLine();
        if (author.trim().isEmpty()) author = existingBook.getAuthor();
        
        System.out.print("Enter new ISBN (current: " + existingBook.getIsbn() + "): ");
        String isbn = scanner.nextLine();
        if (isbn.trim().isEmpty()) isbn = existingBook.getIsbn();
        
        System.out.print("Enter new genre (current: " + existingBook.getGenre() + "): ");
        String genre = scanner.nextLine();
        if (genre.trim().isEmpty()) genre = existingBook.getGenre();
        
        System.out.print("Enter new total copies (current: " + existingBook.getTotalCopies() + "): ");
        String copiesStr = scanner.nextLine();
        int copies = copiesStr.trim().isEmpty() ? existingBook.getTotalCopies() : Integer.parseInt(copiesStr);
        
        if (libraryManager.updateBook(bookId, title, author, isbn, genre, copies)) {
            System.out.println("✅ Book updated successfully!");
        } else {
            System.out.println("❌ Failed to update book.");
        }
    }
    
    private static void removeBook() {
        System.out.println("\n--- Remove Book ---");
        System.out.print("Enter Book ID to remove: ");
        String bookId = scanner.nextLine();
        
        Book book = libraryManager.getBook(bookId);
        if (book == null) {
            System.out.println("❌ Book not found.");
            return;
        }
        
        System.out.println("Book to remove: " + book);
        System.out.print("Are you sure you want to remove this book? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("yes")) {
            if (libraryManager.removeBook(bookId)) {
                System.out.println("✅ Book removed successfully!");
            } else {
                System.out.println("❌ Failed to remove book. Check if copies are currently borrowed.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
    }
    
    private static void viewAvailableBooks() {
        System.out.println("\n--- Available Books ---");
        List<Book> books = libraryManager.getAvailableBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books available for borrowing.");
        } else {
            System.out.printf("%-10s %-25s %-20s %-15s %-10s%n", 
                            "ID", "Title", "Author", "Genre", "Available");
            System.out.println("=".repeat(85));
            
            for (Book book : books) {
                System.out.printf("%-10s %-25s %-20s %-15s %-10s%n",
                                book.getBookId(),
                                truncate(book.getTitle(), 25),
                                truncate(book.getAuthor(), 20),
                                truncate(book.getGenre(), 15),
                                book.getAvailableCopies());
            }
        }
    }
    
    // Borrowing Management
    private static void manageBorrowing() {
        while (true) {
            System.out.println("\n===== BORROWING & RETURNS =====");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Renew Book");
            System.out.println("4. View Overdue Books");
            System.out.println("5. View Student's Borrowed Books");
            System.out.println("0. Back to Main Menu");
            System.out.println("================================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> renewBook();
                case 4 -> viewOverdueBooks();
                case 5 -> viewStudentBorrowedBooks();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void borrowBook() {
        System.out.println("\n--- Borrow Book ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        String result = libraryManager.borrowBook(studentId, bookId);
        System.out.println(result.startsWith("Success") ? "✅ " + result : "❌ " + result);
    }
    
    private static void returnBook() {
        System.out.println("\n--- Return Book ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        String result = libraryManager.returnBook(studentId, bookId);
        System.out.println(result.startsWith("Success") ? "✅ " + result : "❌ " + result);
    }
    
    private static void renewBook() {
        System.out.println("\n--- Renew Book ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        int additionalDays = getIntInput("Enter additional days (default 14): ");
        if (additionalDays <= 0) additionalDays = 14;
        
        String result = libraryManager.renewBook(studentId, bookId, additionalDays);
        System.out.println(result.startsWith("Success") ? "✅ " + result : "❌ " + result);
    }
    
    private static void viewOverdueBooks() {
        System.out.println("\n--- Overdue Books ---");
        List<BorrowRecord> overdueBooks = libraryManager.getOverdueBooks();
        
        if (overdueBooks.isEmpty()) {
            System.out.println("No overdue books found.");
        } else {
            System.out.printf("%-15s %-15s %-12s %-12s %-10s %-8s%n", 
                            "Student ID", "Book ID", "Due Date", "Days Late", "Fine", "Status");
            System.out.println("=".repeat(80));
            
            for (BorrowRecord record : overdueBooks) {
                System.out.printf("%-15s %-15s %-12s %-12d $%-8.2f %-8s%n",
                                record.getStudentId(),
                                record.getBookId(),
                                record.getFormattedDueDate(),
                                record.getDaysOverdue(),
                                record.getFineAmount(),
                                "Overdue");
            }
        }
    }
    
    private static void viewStudentBorrowedBooks() {
        System.out.println("\n--- Student's Borrowed Books ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        List<BorrowRecord> activeRecords = libraryManager.getActiveBorrowRecords(studentId);
        
        if (activeRecords.isEmpty()) {
            System.out.println("No books currently borrowed by this student.");
        } else {
            System.out.printf("%-15s %-12s %-12s %-8s%n", 
                            "Book ID", "Borrow Date", "Due Date", "Status");
            System.out.println("=".repeat(50));
            
            for (BorrowRecord record : activeRecords) {
                String status = record.isOverdue() ? "OVERDUE" : "OK";
                System.out.printf("%-15s %-12s %-12s %-8s%n",
                                record.getBookId(),
                                record.getFormattedBorrowDate(),
                                record.getFormattedDueDate(),
                                status);
            }
        }
    }
    
    // Reports and Statistics
    private static void viewReports() {
        while (true) {
            System.out.println("\n===== REPORTS & STATISTICS =====");
            System.out.println("1. Library Statistics");
            System.out.println("2. Student Report");
            System.out.println("3. Book Report");
            System.out.println("4. Overdue Report");
            System.out.println("0. Back to Main Menu");
            System.out.println("=================================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> showLibraryStatistics();
                case 2 -> showStudentReport();
                case 3 -> showBookReport();
                case 4 -> showOverdueReport();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void showLibraryStatistics() {
        System.out.println("\n--- Library Statistics ---");
        LibraryManager.LibraryStatistics stats = libraryManager.getLibraryStatistics();
        System.out.println(stats);
    }
    
    private static void showStudentReport() {
        System.out.println("\n--- Student Report ---");
        viewAllStudents();
    }
    
    private static void showBookReport() {
        System.out.println("\n--- Book Report ---");
        viewAllBooks();
    }
    
    private static void showOverdueReport() {
        System.out.println("\n--- Overdue Report ---");
        viewOverdueBooks();
    }
    
    // Search System
    private static void searchSystem() {
        while (true) {
            System.out.println("\n===== SEARCH SYSTEM =====");
            System.out.println("1. Search Books");
            System.out.println("2. Find Student");
            System.out.println("3. Find Book by ID");
            System.out.println("0. Back to Main Menu");
            System.out.println("==========================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> searchBooks();
                case 2 -> findStudent();
                case 3 -> findBook();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.print("Enter search keyword (title, author, or ISBN): ");
        String keyword = scanner.nextLine();
        
        List<Book> results = libraryManager.searchBooks(keyword);
        
        if (results.isEmpty()) {
            System.out.println("No books found matching the keyword: " + keyword);
        } else {
            System.out.println("Search results for: " + keyword);
            System.out.printf("%-10s %-25s %-20s %-15s %-10s%n", 
                            "ID", "Title", "Author", "ISBN", "Available");
            System.out.println("=".repeat(85));
            
            for (Book book : results) {
                System.out.printf("%-10s %-25s %-20s %-15s %-10s%n",
                                book.getBookId(),
                                truncate(book.getTitle(), 25),
                                truncate(book.getAuthor(), 20),
                                book.getIsbn(),
                                book.getAvailableCopies() + "/" + book.getTotalCopies());
            }
        }
    }
    
    private static void findStudent() {
        System.out.println("\n--- Find Student ---");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = libraryManager.getStudent(studentId);
        if (student != null) {
            System.out.println("Student found: " + student);
        } else {
            System.out.println("❌ Student not found.");
        }
    }
    
    private static void findBook() {
        System.out.println("\n--- Find Book ---");
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        Book book = libraryManager.getBook(bookId);
        if (book != null) {
            System.out.println("Book found: " + book);
        } else {
            System.out.println("❌ Book not found.");
        }
    }
    
    // Initialize some sample data for testing
    private static void initializeSampleData() {
        // Add sample students
        libraryManager.addStudent("STU001", "Alice Johnson", "alice@email.com", "123 Main St", "555-0101");
        libraryManager.addStudent("STU002", "Bob Smith", "bob@email.com", "456 Oak Ave", "555-0102");
        libraryManager.addStudent("STU003", "Carol Davis", "carol@email.com", "789 Pine Rd", "555-0103");
        
        // Add sample books
        libraryManager.addBook("BK001", "Java Programming", "John Doe", "978-0134685991", "Programming", 3);
        libraryManager.addBook("BK002", "Database Systems", "Jane Smith", "978-0321197844", "Computer Science", 2);
        libraryManager.addBook("BK003", "The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Literature", 5);
        libraryManager.addBook("BK004", "To Kill a Mockingbird", "Harper Lee", "978-0061120084", "Literature", 4);
        libraryManager.addBook("BK005", "1984", "George Orwell", "978-0451524935", "Fiction", 3);
    }
    
    // Utility methods
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return input.isEmpty() ? 0 : Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static String truncate(String str, int length) {
        if (str == null) return "";
        return str.length() <= length ? str : str.substring(0, length - 3) + "...";
    }
}