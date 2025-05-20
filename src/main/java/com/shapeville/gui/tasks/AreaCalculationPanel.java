package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.shapeville.game.AreaCalculation;
import com.shapeville.game.AreaCalculation.ShapeType;
import com.shapeville.gui.shapes.ShapeDrawer;

/**
 * A panel for area calculation tasks in the Shapeville application.
 * This panel allows users to practice calculating areas of different geometric shapes.
 * It provides an interactive interface with shape visualization, formula display,
 * and immediate feedback on user answers.
 *
 * Features:
 * - Interactive shape selection
 * - Visual representation of shapes
 * - Formula and parameter display
 * - Timed practice sessions
 * - Score tracking and feedback
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class AreaCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private AreaCalculation areaCalculation;
    private int currentShapeIndex = 0;
    private JLabel shapeLabel;
    private JLabel formulaLabel;
    private JLabel paramsLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private javax.swing.Timer timer;
    private JLabel timerLabel;
    private int remainingSeconds = 180; // 3 minutes = 180 seconds
    private boolean isEnding = false;
    private JPanel shapeDisplayPanel;
    private JLabel substitutionLabel;
    private JComboBox<String> shapeSelector;
    private Set<ShapeType> completedShapes;
    private Map<String, Double> currentParams;
    private int attemptCount = 0;
    private static final int MAX_ATTEMPTS = 3;
    private Map<ShapeType, Integer> shapeAttempts = new HashMap<>();

    /**
     * Constructs a new AreaCalculationPanel.
     * Initializes the area calculation game, timer, and UI components.
     */
    public AreaCalculationPanel() {
        super("Area Calculation");
        this.areaCalculation = new AreaCalculation();
        this.completedShapes = new HashSet<>();
        this.currentParams = new HashMap<>();
        initializeTimer();
        setupAreaUI();
    }

    /**
     * Initializes the countdown timer for the task.
     */
    private void initializeTimer() {
        timerLabel = new JLabel("Time Remaining: 3:00");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timer = new javax.swing.Timer(1000, e -> updateTimer());
    }

    /**
     * Updates the countdown timer display and handles time expiration.
     */
    private void updateTimer() {
        remainingSeconds--;
        if (remainingSeconds >= 0) {
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            timerLabel.setText(String.format("Time Remaining: %d:%02d", minutes, seconds));
        } else {
            timer.stop();
            endTask();
        }
    }

    /**
     * Initializes the basic UI layout.
     */
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
    }
    
    /**
     * Sets up the main UI components for the area calculation task.
     */
    private void setupAreaUI() {
        setLayout(new BorderLayout(10, 10));

        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Home button
        homeButton = new JButton("Home");
        homeButton.addActionListener(e -> endTask());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Timer
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerPanel.add(timerLabel);
        topPanel.add(timerPanel, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);

        // Create center panel with scroll pane
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // Shape selector dropdown
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        shapeSelector = new JComboBox<>(areaCalculation.getShapes().stream()
                .map(ShapeType::getEnglish)
                .toArray(String[]::new));
        shapeSelector.addActionListener(e -> {
            if (shapeSelector.getSelectedIndex() != -1) {
                currentShapeIndex = shapeSelector.getSelectedIndex();
                showSelectedShape();
            }
        });
        selectorPanel.add(new JLabel("Select a shape to calculate:"));
        selectorPanel.add(shapeSelector);
        centerPanel.add(selectorPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Shape display label
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Formula display label
        formulaLabel = new JLabel("", SwingConstants.CENTER);
        formulaLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(formulaLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Parameters display label
        paramsLabel = new JLabel("", SwingConstants.CENTER);
        paramsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        paramsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(paramsLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Substitution process label
        substitutionLabel = new JLabel("", SwingConstants.CENTER);
        substitutionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        substitutionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        substitutionLabel.setVisible(false);
        centerPanel.add(substitutionLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Answer input area
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(10);
        submitButton = new JButton("Submit Answer");
        
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("Enter area (1 decimal place):"));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Shape display panel
        shapeDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCurrentShape((Graphics2D) g);
            }
        };
        shapeDisplayPanel.setPreferredSize(new Dimension(400, 300));
        shapeDisplayPanel.setBackground(Color.WHITE);
        shapeDisplayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeDisplayPanel);

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Show initial shape
        showSelectedShape();
        
        // Start timer
        timer.start();
    }

    /**
     * Disables input controls for the current shape after completion.
     */
    private void lockCurrentShape() {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        setFeedback("Please select another shape to continue");
    }

    /**
     * Displays the currently selected shape and its calculation parameters.
     */
    private void showSelectedShape() {
        ShapeType shape = areaCalculation.getShapes().get(currentShapeIndex);
        
        if (completedShapes.contains(shape)) {
            setFeedback("This shape is completed. Please select another shape.");
            lockCurrentShape();
            return;
        }

        shapeLabel.setText("Calculate the area of " + shape.getEnglish());

        areaCalculation.generateParams(shape);
        currentParams.clear();
        currentParams.putAll(areaCalculation.getCurrentParams());

        formulaLabel.setText("Formula: " + areaCalculation.getFormula(shape));
        paramsLabel.setText("Parameters: " + areaCalculation.getParamsString());

        substitutionLabel.setVisible(false);
        attemptCount = 0;

        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        answerField.requestFocus();

        shapeDisplayPanel.repaint();
    }

    /**
     * Draws the current shape on the display panel.
     * @param g2d Graphics2D context for drawing
     */
    private void drawCurrentShape(Graphics2D g2d) {
        ShapeType shape = areaCalculation.getShapes().get(currentShapeIndex);
        int width = shapeDisplayPanel.getWidth();
        int height = shapeDisplayPanel.getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        
        boolean showDimensions = completedShapes.contains(shape) || attemptCount >= MAX_ATTEMPTS;
        
        switch (shape) {
            case RECTANGLE:
                ShapeDrawer.drawRectangle(g2d, centerX - 100, centerY - 75, 200, 150, currentParams, showDimensions);
                break;
            case PARALLELOGRAM:
                ShapeDrawer.drawParallelogram(g2d, centerX - 100, centerY - 75, 200, 150, 50, currentParams, showDimensions);
                break;
            case TRIANGLE:
                ShapeDrawer.drawTriangle(g2d, centerX, centerY, 200, 150, currentParams, showDimensions);
                break;
            case TRAPEZIUM:
                ShapeDrawer.drawTrapezium(g2d, centerX, centerY, 150, 200, 150, currentParams, showDimensions);
                break;
        }
    }

    /**
     * Handles the submission of an answer, validates it, and provides feedback.
     */
    @Override
    public void handleSubmit() {
        try {
            double answer = Double.parseDouble(answerField.getText());
            attemptCount++;
            ShapeType currentShape = areaCalculation.getShapes().get(currentShapeIndex);
            
            if (areaCalculation.checkAnswer(answer)) {
                completedShapes.add(currentShape);
                shapeAttempts.put(currentShape, attemptCount);
                substitutionLabel.setText(areaCalculation.getSubstitutionString(currentShape));
                substitutionLabel.setVisible(true);
                setFeedback("Correct! Please select another shape to continue. Complete all four shapes to finish.");
                shapeDisplayPanel.repaint();
                lockCurrentShape();
                
                if (completedShapes.size() >= areaCalculation.getShapes().size()) {
                    timer.stop();
                    endTask();
                }
            } else {
                if (attemptCount >= MAX_ATTEMPTS) {
                    substitutionLabel.setText(areaCalculation.getSubstitutionString(currentShape));
                    substitutionLabel.setVisible(true);
                    setFeedback("No more attempts. The correct answer is: " + String.format("%.1f", areaCalculation.getCorrectArea()) + 
                               "\nPlease select another shape to continue.");
                    shapeDisplayPanel.repaint();
                    completedShapes.add(currentShape);
                    shapeAttempts.put(currentShape, MAX_ATTEMPTS + 1);
                    lockCurrentShape();
                    
                    if (completedShapes.size() >= areaCalculation.getShapes().size()) {
                        timer.stop();
                        endTask();
                    }
                } else {
                    setFeedback("Incorrect. " + (MAX_ATTEMPTS - attemptCount) + " attempts remaining");
                }
            }
        } catch (NumberFormatException e) {
            setFeedback("Please enter a valid number (1 decimal place)");
        }
    }

    /**
     * Sets the feedback message in the parent window.
     * @param message The feedback message to display
     */
    @Override
    protected void setFeedback(String message) {
        if (parentWindow != null) {
            parentWindow.setFeedback(message);
        }
    }

    /**
     * Ends the current task and displays the final results.
     */
    @Override
    public void endTask() {
        if (!isEnding) {
            isEnding = true;
            timer.stop();
            int score = calculateScore();
            String feedback = getFeedback();
            if (parentWindow != null) {
                setFeedback(feedback);
                parentWindow.showResult(score, areaCalculation.getShapes().size() * 3);
            }
        }
    }

    /**
     * Calculates the final score based on completed shapes and number of attempts.
     * Scoring rules:
     * - Correct on first attempt: 3 points
     * - Correct on second attempt: 2 points
     * - Correct on third attempt: 1 point
     * - Failed all attempts: 0 points
     * 
     * @return The calculated score
     */
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        for (Map.Entry<ShapeType, Integer> entry : shapeAttempts.entrySet()) {
            int attempts = entry.getValue();
            if (attempts == 1) {
                totalScore += 3;  // First attempt correct
            } else if (attempts == 2) {
                totalScore += 2;  // Second attempt correct
            } else if (attempts == 3) {
                totalScore += 1;  // Third attempt correct
            }
            // No points if all attempts failed (attempts > 3)
        }
        return totalScore;
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
        int maxScore = areaCalculation.getShapes().size() * 3;

        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("Test completed!\nScore: %d (Maximum: %d)\n\n", score, maxScore));
        feedback.append("Area Calculation Test Statistics:\n");
        
        feedback.append(String.format("Completed shapes: %d\n", completedShapes.size()));
        feedback.append(String.format("Total shapes: %d\n", areaCalculation.getShapes().size()));
        
        double scorePercentage = maxScore > 0 ? (double)score / maxScore * 100 : 0;
        feedback.append("\n").append(scorePercentage >= 90 ? "Excellent! You're very proficient with these calculations!" :
                                   scorePercentage >= 80 ? "Great job! Keep practicing!" :
                                   scorePercentage >= 70 ? "Good effort! Keep practicing!" :
                                                         "More practice needed to improve calculation skills!");
        
        return feedback.toString();
    }

    /**
     * Starts the task by resetting the state and starting the timer.
     */
    @Override
    public void startTask() {
        reset();
        timer.start();
    }

    /**
     * Pauses the task by disabling UI components and stopping the timer.
     */
    @Override
    public void pauseTask() {
        if (timer != null) {
            timer.stop();
        }
        if (submitButton != null) {
            submitButton.setEnabled(false);
        }
        if (answerField != null) {
            answerField.setEnabled(false);
        }
        if (homeButton != null) {
            homeButton.setEnabled(false);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(false);
        }
    }

    /**
     * Resumes the task by enabling UI components and restarting the timer.
     */
    @Override
    public void resumeTask() {
        if (timer != null) {
            timer.start();
        }
        if (submitButton != null) {
            submitButton.setEnabled(true);
        }
        if (answerField != null) {
            answerField.setEnabled(true);
        }
        if (homeButton != null) {
            homeButton.setEnabled(true);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(true);
        }
    }

    /**
     * Resets the task to its initial state.
     */
    @Override
    public void reset() {
        currentShapeIndex = 0;
        attemptCount = 0;
        remainingSeconds = 180;
        isEnding = false;
        completedShapes.clear();
        currentParams.clear();
        shapeAttempts.clear();
        showSelectedShape();
        timer.restart();
    }
} 