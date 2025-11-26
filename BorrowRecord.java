import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * BorrowRecord class represents a book borrowing transaction
 */
public class BorrowRecord {
    private String recordId;
    private String studentId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private double fineAmount;
    
    // Constructor for new borrow record
    public BorrowRecord(String recordId, String studentId, String bookId, int borrowPeriodDays) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(borrowPeriodDays);
        this.isReturned = false;
        this.fineAmount = 0.0;
    }
    
    // Constructor for loading from database
    public BorrowRecord(String recordId, String studentId, String bookId, 
                       LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, 
                       boolean isReturned, double fineAmount) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
        this.fineAmount = fineAmount;
    }
    
    // Getters
    public String getRecordId() { return recordId; }
    public String getStudentId() { return studentId; }
    public String getBookId() { return bookId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return isReturned; }
    public double getFineAmount() { return fineAmount; }
    
    // Setters
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setReturned(boolean returned) { this.isReturned = returned; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
    
    // Business logic methods
    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.isReturned = true;
        calculateFine();
    }
    
    public void calculateFine() {
        if (isOverdue()) {
            long daysOverdue = getDaysOverdue();
            this.fineAmount = daysOverdue * 0.50; // $0.50 per day fine
        }
    }
    
    public String getFormattedBorrowDate() {
        return borrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public String getFormattedDueDate() {
        return dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public String getFormattedReturnDate() {
        return returnDate != null ? returnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "Not returned";
    }
    
    @Override
    public String toString() {
        return String.format("BorrowRecord{ID='%s', StudentID='%s', BookID='%s', BorrowDate='%s', DueDate='%s', Returned=%s, Fine=%.2f}", 
                           recordId, studentId, bookId, getFormattedBorrowDate(), getFormattedDueDate(), isReturned, fineAmount);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BorrowRecord record = (BorrowRecord) obj;
        return recordId != null ? recordId.equals(record.recordId) : record.recordId == null;
    }
    
    @Override
    public int hashCode() {
        return recordId != null ? recordId.hashCode() : 0;
    }
}