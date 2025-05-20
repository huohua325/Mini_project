package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.AngleCalculation;
import javax.swing.SpinnerNumberModel;

/**
 * A panel for angle recognition tasks in the Shapeville application.
 * This panel allows users to practice identifying different types of angles
 * through an interactive interface with visual angle representation.
 *
 * Features:
 * - Interactive angle adjustment using a spinner
 * - Visual representation of angles
 * - Real-time angle visualization
 * - Multiple attempts for each angle type
 * - Score tracking and feedback
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class AngleCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private final AngleCalculation angleCalculation;
    private int currentAngle;
    private JLabel angleLabel;
    private JPanel angleDisplayPanel;
    private JComboBox<String> angleTypeComboBox;
    private JButton submitButton;
    private JButton homeButton;
    private JSpinner angleSpinner;
    private boolean isEnding = false;
    
    /**
     * Constructs a new AngleCalculationPanel.
     * Initializes the angle calculation game and sets up the UI components.
     */
    public AngleCalculationPanel() {
        super("Angle Recognition");
        this.angleCalculation = new AngleCalculation();
        initializeUI();
        setupAngleUI();
    }
    
    /**
     * Initializes the basic UI layout.
     * Only sets up the basic layout without complex initialization.
     */
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
    }
    
    /**
     * Sets up the main UI components for the angle calculation task.
     * Creates and arranges all necessary UI elements including the angle display,
     * control panel, and input components.
     */
    private void setupAngleUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Home button
        homeButton = new JButton("Home");
        homeButton.addActionListener(e -> endTask());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Create angle display panel
        angleDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAngle(g);
            }
        };
        angleDisplayPanel.setPreferredSize(new Dimension(600, 400));
        angleDisplayPanel.setBackground(Color.WHITE);
        
        // Create bottom control panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Create angle input spinner
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 1, 359, 10);
        angleSpinner = new JSpinner(spinnerModel);
        JLabel spinnerLabel = new JLabel("Enter angle (1-359 degrees):");
        angleSpinner.setPreferredSize(new Dimension(80, 25));
        
        // Create angle type selection combo box
        angleTypeComboBox = new JComboBox<>(angleCalculation.getAngleTypes());
        angleTypeComboBox.setPreferredSize(new Dimension(120, 25));
        
        // Create submit button
        submitButton = new JButton("Set Angle");
        submitButton.addActionListener(e -> handleSubmit());
        
        // Add spinner value change listener
        angleSpinner.addChangeListener(e -> {
            currentAngle = (Integer) angleSpinner.getValue();
            angleDisplayPanel.repaint();
        });
        
        // Add components to bottom panel
        bottomPanel.add(spinnerLabel);
        bottomPanel.add(angleSpinner);
        bottomPanel.add(angleTypeComboBox);
        bottomPanel.add(submitButton);
        
        // Add panels to main panel
        add(topPanel, BorderLayout.NORTH);
        add(angleDisplayPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Initialize display
        updateAngleDisplay();
    }
    
    /**
     * Draws the angle on the display panel.
     * @param g Graphics context for drawing
     */
    private void drawAngle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = angleDisplayPanel.getWidth() / 2;
        int centerY = angleDisplayPanel.getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 50;
        
        // Set line style
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // Draw horizontal reference line (0 degrees)
        g2d.drawLine(centerX - radius, centerY, centerX + radius, centerY);
        
        // Draw current angle line
        double angleRad = Math.toRadians(-currentAngle);
        int endX = centerX + (int)(radius * Math.cos(angleRad));
        int endY = centerY + (int)(radius * Math.sin(angleRad));
        g2d.drawLine(centerX, centerY, endX, endY);
        
        // Draw angle arc
        int arcRadius = radius / 3;
        g2d.setStroke(new BasicStroke(1));
        g2d.drawArc(centerX - arcRadius, centerY - arcRadius, 
                    arcRadius * 2, arcRadius * 2, 
                    0, currentAngle);
    }
    
    /**
     * Shows or hides input controls based on the game state.
     * @param show true to show angle input controls, false to show type selection controls
     */
    private void showInputControls(boolean show) {
        angleSpinner.setEnabled(show);
        angleTypeComboBox.setEnabled(!show);
        submitButton.setText(show ? "Set Angle" : "Submit Answer");
        if (show) {
            setFeedback(angleCalculation.getRemainingTypesMessage());
        }
    }
    
    /**
     * Updates the angle display with current values.
     */
    private void updateAngleDisplay() {
        currentAngle = (Integer) angleSpinner.getValue();
        angleDisplayPanel.repaint();
        resetAttempts();
        showInputControls(true);
    }
    
    /**
     * Handles the submission of an angle or angle type.
     */
    @Override
    public void handleSubmit() {
        if (angleSpinner.isEnabled()) {
            // User is inputting angle
            currentAngle = (Integer) angleSpinner.getValue();
            String correctType = angleCalculation.getAngleType(currentAngle);
            
            // Check if this type has already been identified
            if (angleCalculation.isTypeIdentified(correctType)) {
                setFeedback("This angle type has already been correctly identified. Please try another type!\n" + 
                           angleCalculation.getRemainingTypesMessage());
                return;
            }
            
            // Lock angle input, enable type selection
            showInputControls(false);
            setFeedback("Please select the type of this angle");
            
        } else {
            // User is answering angle type
            String selectedType = (String) angleTypeComboBox.getSelectedItem();
            String correctType = angleCalculation.getAngleType(currentAngle);
            
            incrementAttempts();
            angleCalculation.incrementTotalQuestions();
            
            if (angleCalculation.checkAnswer(currentAngle, selectedType)) {
                // Correct answer
                angleCalculation.addIdentifiedType(correctType);
                addAttemptToList();
                setFeedback("Correct!\n" + angleCalculation.getRemainingTypesMessage());
                
                if (angleCalculation.isTaskComplete()) {
                    endTask();
                    return;
                }
                // Reset to new angle input state
                updateAngleDisplay();
                
            } else if (!hasRemainingAttempts()) {
                // No more attempts
                addAttemptToList();
                setFeedback("Maximum attempts reached. The correct answer is: " + correctType + 
                           "\nPlease try identifying a new angle type\n" + 
                           angleCalculation.getRemainingTypesMessage());
                
                // Reset to new angle input state
                updateAngleDisplay();
                
            } else {
                setFeedback("Incorrect. Try again. " + getRemainingAttempts() + " attempts remaining.");
            }
        }
    }
    
    /**
     * Resets the task to its initial state.
     */
    @Override
    public void reset() {
        resetAttempts();
        attemptsPerTask.clear();
        angleSpinner.setValue(10);
        updateAngleDisplay();
    }
    
    /**
     * Starts the task.
     */
    @Override
    public void startTask() {
        reset();
    }
    
    /**
     * Pauses the task by disabling UI components.
     */
    @Override
    public void pauseTask() {
        if (submitButton != null) submitButton.setEnabled(false);
        if (angleTypeComboBox != null) angleTypeComboBox.setEnabled(false);
        if (homeButton != null) homeButton.setEnabled(false);
    }
    
    /**
     * Resumes the task by enabling UI components.
     */
    @Override
    public void resumeTask() {
        if (submitButton != null) submitButton.setEnabled(true);
        if (angleTypeComboBox != null) angleTypeComboBox.setEnabled(true);
        if (homeButton != null) homeButton.setEnabled(true);
    }
    
    /**
     * Ends the task and displays the final results.
     */
    @Override
    public void endTask() {
        if (!isEnding) {
            isEnding = true;
            cleanup();
            if (parentWindow != null) {
                parentWindow.showResult(calculateScore(), angleCalculation.getMaxPossibleScore());
            }
        }
    }
    
    /**
     * Calculates the final score based on attempts.
     * @return The calculated score
     */
    @Override
    protected int calculateScore() {
        return angleCalculation.calculateScore(attemptsPerTask);
    }
    
    /**
     * Gets the current score.
     * @return The current score
     */
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    /**
     * Generates feedback message based on task performance.
     * @return The feedback message
     */
    @Override
    public String getFeedback() {
        int score = calculateScore();
        int maxScore = angleCalculation.getMaxPossibleScore();
        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("Test completed!\nScore: %d (Maximum: %d)\n\n", score, maxScore));
        feedback.append("Angle Recognition Test Statistics:\n");
        
        int totalAttempted = attemptsPerTask.size();
        int correctWithinThree = 0;
        int perfectCount = 0;
        
        for (int attempts : attemptsPerTask) {
            if (attempts <= 3) {
                correctWithinThree++;
                if (attempts == 1) perfectCount++;
            }
        }
        
        feedback.append(String.format("Total angles attempted: %d\n", totalAttempted));
        feedback.append(String.format("Correct within 3 attempts: %d\n", correctWithinThree));
        feedback.append(String.format("Perfect answers (first try): %d\n", perfectCount));
        
        double scorePercentage = (double) score / maxScore * 100;
        feedback.append("\n").append(scorePercentage >= 90 ? "Excellent! You're very proficient at identifying angles!" :
                                   scorePercentage >= 80 ? "Great job! Keep practicing!" :
                                   scorePercentage >= 70 ? "Good effort! Keep practicing!" :
                                                         "More practice needed to improve your angle recognition skills!");
        
        return feedback.toString();
    }
} 