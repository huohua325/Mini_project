package com.shapeville.gui.tasks;

import com.shapeville.gui.shapes.CircleDrawer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.border.EmptyBorder;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel for circle-related calculations in the Shapeville application.
 * This panel allows users to practice calculating circle areas and circumferences
 * through an interactive interface with visual circle representation.
 *
 * Features:
 * - Interactive calculation selection (area/circumference)
 * - Visual representation of circles
 * - Real-time feedback
 * - Multiple attempts for each calculation
 * - Score tracking and timed exercises
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class CircleCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private JComboBox<String> calculationType;
    private JLabel valueLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel timerLabel;
    private JLabel formulaLabel;
    private JPanel drawingPanel;
    private CircleDrawer circleDrawer;
    
    private Timer questionTimer;
    private static final int TIME_PER_QUESTION = 3 * 60; // 3 minutes time limit per question (in seconds)
    private int remainingTime;
    private int attempts = 0;
    private double currentValue;
    private boolean isRadius;
    private double correctAnswer;
    private Random random = new Random();
    private DecimalFormat df = new DecimalFormat("0.0");
    private int score = 0;
    private int completedCalculations = 0;
    private Set<String> completedTypes = new HashSet<>();
    private static final int TOTAL_TYPES = 4; // Total calculation types
    private boolean isArea;
    
    /**
     * Constructs a new CircleCalculationPanel.
     * Initializes the circle calculation game and sets up the UI components.
     */
    public CircleCalculationPanel() {
        super("Circle Calculation");
        System.out.println("CircleCalculationPanel constructor called");
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setupCircleUI();
    }
    
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
    }
    
    /**
     * Sets up the main UI components for the circle calculation task.
     * Creates and arranges all necessary UI elements including the circle display,
     * control panel, and input components.
     */
    private void setupCircleUI() {
        initializeComponents();
        setupLayout();
        startNewCalculation();
    }
    
    /**
     * Initializes all UI components with their default values and settings.
     */
    private void initializeComponents() {
        String[] types = {"Area Calculation", "Circumference Calculation"};
        calculationType = new JComboBox<>(types);
        calculationType.addActionListener(e -> startNewCalculation());
        
        valueLabel = new JLabel();
        answerField = new JTextField(10);
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(e -> handleSubmit());
        
        timerLabel = new JLabel("Time Remaining: 3:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLUE);
        
        formulaLabel = new JLabel();
        formulaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (circleDrawer != null) {
                    circleDrawer.draw(g, getWidth(), getHeight());
                }
            }
        };
        drawingPanel.setPreferredSize(new Dimension(300, 250));
        drawingPanel.setBackground(Color.WHITE);
        
        circleDrawer = new CircleDrawer();
    }
    
    /**
     * Sets up the layout of all UI components in the panel.
     */
    private void setupLayout() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(calculationType);
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);
        
        drawingPanel.setPreferredSize(new Dimension(500, 500));
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(valueLabel);
        bottomPanel.add(answerField);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(formulaLabel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);
    }
    
    /**
     * Starts a new calculation by generating new values and updating the UI.
     */
    private void startNewCalculation() {
        attempts = 0;
        isRadius = random.nextBoolean();
        isArea = random.nextBoolean();
        currentValue = 1 + random.nextInt(20);
        
        String currentType = getCalculationType();
        while (completedTypes.contains(currentType)) {
            isRadius = random.nextBoolean();
            isArea = random.nextBoolean();
            currentType = getCalculationType();
        }
        
        calculationType.setSelectedItem(isArea ? "Area Calculation" : "Circumference Calculation");
        String valueType = isRadius ? "Radius" : "Diameter";
        valueLabel.setText("Given " + valueType + ": " + currentValue + " ");
        
        updateFormulaDisplay();
        
        circleDrawer.setValues(currentValue, isRadius);
        drawingPanel.repaint();
        
        calculateCorrectAnswer();
        
        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        
        startQuestionTimer();
    }
    
    /**
     * Starts the timer for the current question.
     */
    private void startQuestionTimer() {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        remainingTime = TIME_PER_QUESTION;
        updateTimerLabel();
        
        questionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                updateTimerLabel();
                
                if (remainingTime <= 0) {
                    questionTimer.stop();
                    handleTimeUp();
                }
            }
        });
        questionTimer.start();
    }
    
    /**
     * Updates the timer label with the current remaining time.
     */
    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        
        if (remainingTime < 60) {
            timerLabel.setForeground(Color.RED);
        } else {
            timerLabel.setForeground(Color.BLUE);
        }
        
        timerLabel.setText(String.format("Time Remaining: %d:%02d", minutes, seconds));
    }
    
    /**
     * Handles the case when time runs out for the current question.
     */
    private void handleTimeUp() {
        JOptionPane.showMessageDialog(this,
            "Time's up! Moving to the next question.",
            "Time Alert",
            JOptionPane.WARNING_MESSAGE);
        
        String formattedAnswer = df.format(correctAnswer);
        String currentType = getCalculationType();
        completedTypes.add(currentType);
        
        setFeedback("Time's up! The correct answer was: " + formattedAnswer + "\n" +
                     "Completed " + completedTypes.size() + "/" + TOTAL_TYPES + " calculation types.");
        
        if (completedTypes.size() >= TOTAL_TYPES) {
            endTask();
        } else {
            Timer delayTimer = new Timer(2000, e -> startNewCalculation());
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    
    /**
     * Gets the current calculation type as a string.
     * @return The current calculation type
     */
    private String getCalculationType() {
        return (isRadius ? "Radius" : "Diameter") + (isArea ? "Area" : "Circumference");
    }
    
    /**
     * Updates the formula display based on the current calculation type.
     */
    private void updateFormulaDisplay() {
        boolean isArea = calculationType.getSelectedItem().toString().equals("Area Calculation");
        if (isArea) {
            if (isRadius) {
                formulaLabel.setText("<html>Area Formula:<br>A = πr²<br>r = " + currentValue + "<br>(Use π = 3.14)</html>");
            } else {
                formulaLabel.setText("<html>Area Formula:<br>A = π(d/2)²<br>d = " + currentValue + "<br>(Use π = 3.14)</html>");
            }
        } else {
            if (isRadius) {
                formulaLabel.setText("<html>Circumference Formula:<br>C = 2πr<br>r = " + currentValue + "<br>(Use π = 3.14)</html>");
            } else {
                formulaLabel.setText("<html>Circumference Formula:<br>C = πd<br>d = " + currentValue + "<br>(Use π = 3.14)</html>");
            }
        }
    }
    
    /**
     * Calculates the correct answer for the current problem.
     */
    private void calculateCorrectAnswer() {
        boolean isArea = calculationType.getSelectedItem().toString().equals("Area Calculation");
        if (isArea) {
            if (isRadius) {
                correctAnswer = 3.14 * currentValue * currentValue;
            } else {
                double radius = currentValue / 2;
                correctAnswer = 3.14 * radius * radius;
            }
        } else {
            if (isRadius) {
                correctAnswer = 2 * 3.14 * currentValue;
            } else {
                correctAnswer = 3.14 * currentValue;
            }
        }
    }
    
    /**
     * Moves to the next question or ends the task if all types are completed.
     */
    private void moveToNextQuestion() {
        if (completedTypes.size() >= TOTAL_TYPES) {
            endTask();
        } else {
            Timer delayTimer = new Timer(2000, e -> startNewCalculation());
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    
    @Override
    public void handleSubmit() {
        try {
            double userAnswer = Double.parseDouble(answerField.getText());
            attempts++;
            incrementAttempts();
            
            if (Math.abs(userAnswer - correctAnswer) < 0.1) {
                if (questionTimer != null && questionTimer.isRunning()) {
                    questionTimer.stop();
                }
                
                String currentType = getCalculationType();
                completedTypes.add(currentType);
                score += (4 - attempts);
                attemptsPerTask.add(attempts);
                
                setFeedback("Correct! Points earned: " + (4 - attempts) + "\n" +
                           "Completed " + completedTypes.size() + "/" + TOTAL_TYPES + " calculation types.");
                
                moveToNextQuestion();
            } else {
                if (attempts >= 3) {
                    if (questionTimer != null && questionTimer.isRunning()) {
                        questionTimer.stop();
                    }
                    
                    String formattedAnswer = df.format(correctAnswer);
                    String currentType = getCalculationType();
                    completedTypes.add(currentType);
                    attemptsPerTask.add(attempts);
                    
                    setFeedback("Too many incorrect attempts. The correct answer was: " + formattedAnswer + "\n" +
                              "Completed " + completedTypes.size() + "/" + TOTAL_TYPES + " calculation types.");
                    
                    moveToNextQuestion();
                } else {
                    setFeedback("Incorrect. " + (3 - attempts) + " attempts remaining.");
                }
            }
        } catch (NumberFormatException e) {
            setFeedback("Please enter a valid number!");
        }
    }
    
    @Override
    public void cleanup() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
    }
    
    @Override
    public void reset() {
        score = 0;
        completedCalculations = 0;
        completedTypes.clear();
        attemptsPerTask.clear();
        if (questionTimer != null) {
            questionTimer.stop();
        }
        startNewCalculation();
    }
    
    @Override
    public int calculateScore() {
        return score;
    }

    @Override
    public void startTask() {
        reset();
    }

    @Override
    public void pauseTask() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
    }

    @Override
    public void resumeTask() {
        if (questionTimer != null && remainingTime > 0) {
            questionTimer.start();
        }
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
    }

    @Override
    public void endTask() {
        System.out.println("CircleCalculationPanel endTask called");
        cleanup();
        if (parentWindow != null) {
            System.out.println("Showing result - Score: " + calculateScore() + "/12");
            parentWindow.showResult(calculateScore(), 12);
        } else {
            System.out.println("Warning: parentWindow is null!");
        }
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getFeedback() {
        StringBuilder feedback = new StringBuilder();
        feedback.append("Circle Calculation Exercise Results:\n\n");
        
        int taskNum = 1;
        for (Integer attempts : attemptsPerTask) {
            feedback.append(String.format("Task %d: %d attempts\n", taskNum++, attempts));
        }
        
        feedback.append("\nCompleted calculation types: ").append(completedTypes.size())
                .append("/").append(TOTAL_TYPES).append("\n");
        feedback.append("Total score: ").append(score).append("/12 points\n\n");
        
        if (score >= 10) {
            feedback.append("Excellent! You have mastered circle area and circumference calculations.");
        } else if (score >= 6) {
            feedback.append("Good job! Keep practicing to improve further.");
        } else {
            feedback.append("More practice needed. Review the formulas for circle area and circumference.");
        }
        
        return feedback.toString();
    }
} 