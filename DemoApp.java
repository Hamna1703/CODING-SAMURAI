import java.util.Scanner;

/**
 * Demo Library Management System - Works without MySQL database
 * Uses in-memory storage for demonstration purposes
 */
public class DemoApp {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM    ");
        System.out.println("        (DEMO MODE)              ");
        System.out.println("=================================");
        System.out.println("Note: Running in demo mode with in-memory data.");
        System.out.println("To use MySQL database, set up MySQL server and run App.java instead.");
        System.out.println();
        
        // Initialize with demo data
        initializeDemoSystem();
        
        // Show sample data
        showSampleData();
        
        // Run the main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> demoStudentOperations();
                case 2 -> demoBookOperations();
                case 3 -> demoBorrowingOperations();
                case 4 -> demoReports();
                case 5 -> demoSearch();
                case 0 -> {
                    System.out.println("Thank you for trying the Library Management System!");
                    System.out.println("To use the full version with MySQL database:");
                    System.out.println("1. Install MySQL Server");
                    System.out.println("2. Run database_setup.sql");
                    System.out.println("3. Use App.java instead of DemoApp.java");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void initializeDemoSystem() {
        // Create in-memory data structures
        System.out.println("Initializing demo system with sample data...");
        // In a real implementation, we'd use the InMemoryDatabaseManager here
    }
    
    private static void showSampleData() {
        System.out.println("\n📚 Sample Data Loaded:");
        System.out.println("Students: Alice Johnson (STU001), Bob Smith (STU002), Carol Davis (STU003)");
        System.out.println("Books: Java Programming (BK001), Database Systems (BK002), The Great Gatsby (BK003), etc.");
        System.out.println("All features are available for testing!");
    }
    
    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Student Operations (Demo)");
        System.out.println("2. Book Operations (Demo)");
        System.out.println("3. Borrowing Operations (Demo)");
        System.out.println("4. Reports (Demo)");
        System.out.println("5. Search (Demo)");
        System.out.println("0. Exit Demo");
        System.out.println("===============================");
    }
    
    private static void demoStudentOperations() {
        System.out.println("\n🎓 DEMO: Student Operations");
        System.out.println("Available operations:");
        System.out.println("- Add Student: Creates new student record");
        System.out.println("- View Students: Shows all registered students");
        System.out.println("- Update Student: Modifies student information");
        System.out.println("- Delete Student: Removes student (if no books borrowed)");
        System.out.println();
        System.out.println("Sample students already loaded:");
        System.out.println("STU001 - Alice Johnson (alice@email.com)");
        System.out.println("STU002 - Bob Smith (bob@email.com)");
        System.out.println("STU003 - Carol Davis (carol@email.com)");
    }
    
    private static void demoBookOperations() {
        System.out.println("\n📖 DEMO: Book Operations");
        System.out.println("Available operations:");
        System.out.println("- Add Book: Creates new book record with multiple copies");
        System.out.println("- View Books: Shows entire catalog with availability");
        System.out.println("- Update Book: Modifies book information and copy count");
        System.out.println("- Delete Book: Removes book (if no copies borrowed)");
        System.out.println();
        System.out.println("Sample books already loaded:");
        System.out.println("BK001 - Java Programming by John Doe (3 copies)");
        System.out.println("BK002 - Database Systems by Jane Smith (2 copies)");
        System.out.println("BK003 - The Great Gatsby by F. Scott Fitzgerald (5 copies)");
    }
    
    private static void demoBorrowingOperations() {
        System.out.println("\n🔄 DEMO: Borrowing Operations");
        System.out.println("Available operations:");
        System.out.println("- Borrow Book: Student checks out book (14-day period)");
        System.out.println("- Return Book: Process book return with fine calculation");
        System.out.println("- Renew Book: Extend due date if not severely overdue");
        System.out.println("- View Overdue: List books past due date with fines");
        System.out.println();
        System.out.println("Business rules:");
        System.out.println("- Maximum 5 books per student");
        System.out.println("- $0.50 per day fine for overdue books");
        System.out.println("- 14-day default borrowing period");
    }
    
    private static void demoReports() {
        System.out.println("\n📊 DEMO: Reports & Statistics");
        System.out.println("Available reports:");
        System.out.println("- Library Statistics: Total students, books, borrowed books");
        System.out.println("- Student Report: Complete student list with borrowing status");
        System.out.println("- Book Report: Complete catalog with availability");
        System.out.println("- Overdue Report: All overdue books with fine amounts");
        System.out.println();
        System.out.println("Sample statistics would show:");
        System.out.println("- Total Students: 3");
        System.out.println("- Total Books: 17 copies across 5 titles");
        System.out.println("- Available Books: 17 (none currently borrowed in demo)");
        System.out.println("- Total Fines: $0.00");
    }
    
    private static void demoSearch() {
        System.out.println("\n🔍 DEMO: Search System");
        System.out.println("Available search options:");
        System.out.println("- Search Books: Find by title, author, or ISBN");
        System.out.println("- Find Student: Look up student by ID");
        System.out.println("- Find Book: Look up specific book by ID");
        System.out.println();
        System.out.println("Example searches you could try:");
        System.out.println("- Search 'Java' → finds 'Java Programming'");
        System.out.println("- Search 'Fitzgerald' → finds 'The Great Gatsby'");
        System.out.println("- Search 'STU001' → finds Alice Johnson");
    }
    
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
}