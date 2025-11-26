# Quiz Application

A comprehensive Java Swing-based quiz application featuring a Java programming knowledge test.

## Features

- **User Authentication**: Enter your name to start the quiz
- **Rules Display**: Clear instructions before starting
- **Interactive Quiz**: 10 multiple-choice Java programming questions  
- **Timer System**: 15 seconds per question with visual countdown
- **50-50 Lifeline**: Remove two incorrect options (can be used once)
- **Score Calculation**: Points awarded for correct answers
- **Performance Feedback**: Score-based performance messages
- **Replay Functionality**: Option to play again or exit

## Project Structure

```
QuizApp/
├── src/
│   ├── App.java                           # Main launcher
│   └── quiz/application/
│       ├── QuizApplication.java           # Main application entry
│       ├── Login.java                     # Login screen
│       ├── Rules.java                     # Rules display
│       ├── Quiz.java                      # Quiz gameplay
│       └── Score.java                     # Results screen
├── icons/                                 # Image resources
├── bin/                                   # Compiled classes
├── lib/                                   # External libraries
└── README.md

```

## How to Run

### Method 1: Using the launcher
```bash
cd QuizApp/src
javac App.java quiz/application/*.java
java App
```

### Method 2: Direct execution
```bash
cd QuizApp/src
javac quiz/application/*.java
java quiz.application.QuizApplication
```

## Game Rules

1. **Question Format**: Multiple choice questions with 4 options each
2. **Time Limit**: 15 seconds per question
3. **Scoring**: 10 points per correct answer (100 points maximum)
4. **Lifeline**: 50-50 option removes two wrong answers (single use)
5. **Navigation**: Click "Next" to proceed, "Submit" for the final question
6. **Auto-advance**: Questions auto-advance when time expires

## Performance Ratings

- **80+ points**: Excellent! 🌟
- **60-79 points**: Good job! 👍  
- **40-59 points**: Keep trying! 📚
- **Below 40**: Need more practice! 💪

## Technologies Used

- **Language**: Java
- **GUI Framework**: Swing
- **Development Environment**: Compatible with any Java IDE

## System Requirements

- Java Development Kit (JDK) 8 or higher
- Operating System: Windows, macOS, or Linux
- Memory: Minimum 256MB RAM

## Quick Start (Windows)

Double-click `run-quiz.bat` to start the application immediately!

## Contributing

Feel free to contribute by:
- Adding new questions
- Improving the UI design
- Adding new features (categories, difficulty levels, etc.)
- Bug fixes and optimizations

## Future Enhancements

- [ ] Add different quiz categories
- [ ] Implement difficulty levels
- [ ] Add sound effects
- [ ] Store high scores
- [ ] Add question randomization
- [ ] Include explanations for answers

---
*Developed as a Java Swing educational project*
