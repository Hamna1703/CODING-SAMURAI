/**
 * Student class represents a library user in the management system
 */
public class Student {
    private String studentId;
    private String studentName;
    private String email;
    private String address;
    private String phoneNumber;
    private int maxBorrowLimit;
    private int currentBorrowCount;
    
    // Default constructor
    public Student() {
        this.maxBorrowLimit = 5; // Default borrowing limit
        this.currentBorrowCount = 0;
    }
    
    // Parameterized constructor
    public Student(String studentId, String studentName, String email, String address) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.address = address;
        this.maxBorrowLimit = 5;
        this.currentBorrowCount = 0;
    }
    
    // Full constructor
    public Student(String studentId, String studentName, String email, String address, 
                   String phoneNumber, int maxBorrowLimit) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.maxBorrowLimit = maxBorrowLimit;
        this.currentBorrowCount = 0;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getMaxBorrowLimit() { return maxBorrowLimit; }
    public int getCurrentBorrowCount() { return currentBorrowCount; }
    
    // Setters
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setMaxBorrowLimit(int maxBorrowLimit) { this.maxBorrowLimit = maxBorrowLimit; }
    public void setCurrentBorrowCount(int currentBorrowCount) { this.currentBorrowCount = currentBorrowCount; }
    
    // Business logic methods
    public boolean canBorrowMore() {
        return currentBorrowCount < maxBorrowLimit;
    }
    
    public boolean borrowBook() {
        if (canBorrowMore()) {
            currentBorrowCount++;
            return true;
        }
        return false;
    }
    
    public void returnBook() {
        if (currentBorrowCount > 0) {
            currentBorrowCount--;
        }
    }
    
    public int getAvailableBorrowSlots() {
        return maxBorrowLimit - currentBorrowCount;
    }
    
    @Override
    public String toString() {
        return String.format("Student{ID='%s', Name='%s', Email='%s', Address='%s', Books Borrowed=%d/%d}", 
                           studentId, studentName, email, address, currentBorrowCount, maxBorrowLimit);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null ? studentId.equals(student.studentId) : student.studentId == null;
    }
    
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}