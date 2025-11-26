package quiz.application;

import javax.swing.*;

/**
 * Quiz Application - A Java Swing-based quiz game
 * 
 * Features:
 * - User login with name validation
 * - Rules display before starting
 * - 10 Java programming questions
 * - Timer (15 seconds per question)
 * - 50-50 lifeline feature
 * - Score calculation and display
 * - Play again functionality
 * 
 * @author Quiz App Team
 * @version 1.0
 */
public class QuizApplication {
    
    public static void main(String[] args) {
        // Start the application
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
}