package quiz.application;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Score extends JFrame implements ActionListener {

    Score(String name, int score) {
        setBounds(400, 150, 750, 550);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        // Create a placeholder for score image
        JLabel image = new JLabel();
        image.setBounds(0, 200, 300, 250);
        image.setBackground(new Color(144, 238, 144));
        image.setOpaque(true);
        image.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setText("🎉");
        image.setFont(new Font("Arial", Font.BOLD, 72));
        add(image);
        
        JLabel heading = new JLabel("Thank you " + name + " for playing Simple Minds");
        heading.setBounds(45, 30, 700, 30);
        heading.setFont(new Font("Arial", Font.PLAIN, 26));
        heading.setForeground(new Color(30, 144, 254));
        add(heading);
        
        JLabel lblscore = new JLabel("Your score is " + score + " out of 100");
        lblscore.setBounds(350, 200, 300, 30);
        lblscore.setFont(new Font("Arial", Font.BOLD, 26));
        lblscore.setForeground(score >= 70 ? new Color(0, 128, 0) : score >= 40 ? new Color(255, 165, 0) : Color.RED);
        add(lblscore);
        
        // Performance message
        JLabel performance = new JLabel();
        performance.setBounds(350, 240, 300, 30);
        performance.setFont(new Font("Arial", Font.ITALIC, 18));
        if (score >= 80) {
            performance.setText("Excellent! 🌟");
            performance.setForeground(new Color(0, 128, 0));
        } else if (score >= 60) {
            performance.setText("Good job! 👍");
            performance.setForeground(new Color(255, 165, 0));
        } else if (score >= 40) {
            performance.setText("Keep trying! 📚");
            performance.setForeground(new Color(255, 165, 0));
        } else {
            performance.setText("Need more practice! 💪");
            performance.setForeground(Color.RED);
        }
        add(performance);
        
        JButton playAgain = new JButton("Play Again");
        playAgain.setBounds(320, 320, 120, 30);
        playAgain.setBackground(new Color(30, 144, 255));
        playAgain.setForeground(Color.WHITE);
        playAgain.addActionListener(this);
        add(playAgain);
        
        JButton exit = new JButton("Exit");
        exit.setBounds(460, 320, 120, 30);
        exit.setBackground(new Color(220, 20, 60));
        exit.setForeground(Color.WHITE);
        exit.addActionListener(e -> System.exit(0));
        add(exit);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Quiz Application - Results");
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        // Reset static variables in Quiz class
        Quiz.timer = 15;
        Quiz.ans_given = 0;
        Quiz.count = 0;
        Quiz.score = 0;
        new Login();
    }
}