/**
 * Book class represents a book in the library management system
 */
public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    
    // Default constructor
    public Book() {
        this.isAvailable = true;
        this.totalCopies = 1;
        this.availableCopies = 1;
    }
    
    // Parameterized constructor
    public Book(String bookId, String title, String author, String isbn, String genre, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.isAvailable = totalCopies > 0;
    }
    
    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }
    public String getGenre() { return genre; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    
    // Setters
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setTotalCopies(int totalCopies) { 
        this.totalCopies = totalCopies;
        updateAvailability();
    }
    
    public void setAvailableCopies(int availableCopies) { 
        this.availableCopies = availableCopies;
        updateAvailability();
    }
    
    // Business logic methods
    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            updateAvailability();
            return true;
        }
        return false;
    }
    
    public void returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            updateAvailability();
        }
    }
    
    private void updateAvailability() {
        this.isAvailable = availableCopies > 0;
    }
    
    @Override
    public String toString() {
        return String.format("Book{ID='%s', Title='%s', Author='%s', ISBN='%s', Genre='%s', Available=%d/%d}", 
                           bookId, title, author, isbn, genre, availableCopies, totalCopies);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId != null ? bookId.equals(book.bookId) : book.bookId == null;
    }
    
    @Override
    public int hashCode() {
        return bookId != null ? bookId.hashCode() : 0;
    }
}