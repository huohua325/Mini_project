package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.SectorCalculation;
import com.shapeville.game.SectorCalculation.Sector;
import com.shapeville.gui.TaskWindow;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel for sector calculation exercises in the Shapeville application.
 * This panel provides an interactive interface for practicing sector area calculations
 * with visual representation and real-time feedback.
 *
 * Features:
 * - Visual representation of sectors
 * - Interactive input for area calculations
 * - Real-time feedback and scoring
 * - Multiple attempts per question
 * - Timed exercises (5 minutes per question)
 * - Detailed solution steps
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class SectorCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private static final int MAX_ATTEMPTS = 3;  // Maximum 3 attempts per question
    private static final int TIME_PER_QUESTION = 5 * 60; // 5 minutes time limit per question (seconds)
    private final SectorCalculation sectorCalculation;
    private int currentSectorIndex = 0;
    private JComboBox<String> sectorSelector;
    private JPanel sectorDisplayPanel;
    private JLabel sectorLabel;
    private JTextArea descriptionArea;
    private JTextField areaField;
    private JButton submitButton;
    private JTextArea areaSolutionArea;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private int attempts = 0;
    private boolean areaCorrect = false;
    private boolean arcLengthCorrect = false;
    private List<Boolean> correctAnswers;
    private int score = 0;
    private JButton nextButton;
    private Timer questionTimer;
    private int remainingTime;
    private JLabel timerLabel;

    /**
     * Constructs a new SectorCalculationPanel.
     * Initializes the sector calculation game and sets up the UI components.
     */
    public SectorCalculationPanel() {
        super("Sector Area Calculation");
        this.sectorCalculation = new SectorCalculation();
        this.correctAnswers = new ArrayList<>();
        initializeUI();
    }
    
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Add timer label
        timerLabel = new JLabel("Time Remaining: 5:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLUE);
        topPanel.add(timerLabel, BorderLayout.EAST);
        
        // Create sector selector
        List<Sector> sectors = sectorCalculation.getSectors();
        String[] sectorNames = new String[sectors.size()];
        for (int i = 0; i < sectors.size(); i++) {
            sectorNames[i] = "Sector " + (i + 1);
        }
        sectorSelector = new JComboBox<>(sectorNames);
        sectorSelector.addActionListener(e -> {
            currentSectorIndex = sectorSelector.getSelectedIndex();
            showCurrentSector();
        });
        topPanel.add(sectorSelector, BorderLayout.NORTH);
        
        // Set sector label
        sectorLabel = new JLabel("", SwingConstants.CENTER);
        sectorLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(sectorLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        // Create center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Create sector display panel
        sectorDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentSectorIndex >= 0 && currentSectorIndex < sectorCalculation.getSectors().size()) {
                    Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
                    sector.draw((Graphics2D) g, getWidth(), getHeight());
                }
            }
        };
        sectorDisplayPanel.setPreferredSize(new Dimension(400, 400));
        sectorDisplayPanel.setBackground(Color.WHITE);
        
        // Create right input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // Add area input section
        JPanel areaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        areaPanel.add(new JLabel("Area: "));
        areaField = new JTextField(10);
        areaPanel.add(areaField);
        inputPanel.add(areaPanel);
        
        // Add submit button
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(e -> handleSubmit());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(submitButton);
        
        // Add next question button
        nextButton = new JButton("Next Question");
        nextButton.addActionListener(e -> goToNextQuestion());
        nextButton.setVisible(false);
        buttonPanel.add(nextButton);
        inputPanel.add(buttonPanel);
        
        // Add feedback label
        feedbackLabel = new JLabel("");
        inputPanel.add(feedbackLabel);
        
        // Add solution area
        areaSolutionArea = new JTextArea(4, 30);
        areaSolutionArea.setEditable(false);
        areaSolutionArea.setLineWrap(true);
        areaSolutionArea.setWrapStyleWord(true);
        JScrollPane areaSolutionScroll = new JScrollPane(areaSolutionArea);
        areaSolutionScroll.setBorder(BorderFactory.createTitledBorder("Solution Steps"));
        inputPanel.add(areaSolutionScroll);
        
        // Add panels to main panel
        centerPanel.add(sectorDisplayPanel, BorderLayout.CENTER);
        centerPanel.add(inputPanel, BorderLayout.EAST);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Show first sector
        showCurrentSector();
    }
    
    /**
     * Draws the current sector on the display panel.
     * @param g The Graphics context to draw on
     */
    private void drawSector(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = sectorDisplayPanel.getWidth() / 2;
        int centerY = sectorDisplayPanel.getHeight() / 2;
        
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        int pixelRadius = Math.min(centerX, centerY) - 50;
        
        // Draw sector fill
        g2d.setColor(new Color(200, 200, 255));
        g2d.fillArc(centerX - pixelRadius, centerY - pixelRadius, 
                    pixelRadius * 2, pixelRadius * 2, 
                    0, -(int)sector.getAngle());
        
        // Draw sector border
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(centerX - pixelRadius, centerY - pixelRadius, 
                    pixelRadius * 2, pixelRadius * 2, 
                    0, -(int)sector.getAngle());
        g2d.drawLine(centerX, centerY, 
                    centerX + pixelRadius, centerY);
        g2d.drawLine(centerX, centerY, 
                    (int)(centerX + pixelRadius * Math.cos(Math.toRadians(-sector.getAngle()))),
                    (int)(centerY + pixelRadius * Math.sin(Math.toRadians(-sector.getAngle()))));
        
        // Label radius
        g2d.setColor(Color.BLACK);
        g2d.drawString("r = " + sector.getRadius(), centerX + pixelRadius/2, centerY - 10);
        
        // Label angle
        int angleX = centerX + pixelRadius/3;
        int angleY = centerY - pixelRadius/3;
        g2d.drawString(sector.getAngle() + "°", angleX, angleY);
    }
    
    /**
     * Displays the current sector and updates all related UI components.
     */
    private void showCurrentSector() {
        List<Sector> sectors = sectorCalculation.getSectors();
        if (currentSectorIndex < sectors.size()) {
            Sector sector = sectors.get(currentSectorIndex);
            sectorLabel.setText("Sector " + (currentSectorIndex + 1));
            areaField.setText("");
            areaField.setEnabled(true);
            submitButton.setEnabled(true);
            nextButton.setVisible(false);
            sectorSelector.setSelectedIndex(currentSectorIndex);
            areaSolutionArea.setText("Please calculate the sector's area");
            
            sectorDisplayPanel.repaint();
            startQuestionTimer();
        } else {
            endTask();
        }
    }
    
    /**
     * Marks the current question as completed and updates the UI accordingly.
     * @param correct Whether the question was answered correctly
     */
    private void completedCurrentQuestion(boolean correct) {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        correctAnswers.add(correct);
        addAttemptToList();
        
        sectorCalculation.addPracticed(currentSectorIndex);
        
        if (sectorCalculation.isComplete()) {
            endTask();
        } else {
            nextButton.setVisible(true);
        }
    }
    
    @Override
    public void handleSubmit() {
        String areaStr = areaField.getText().trim();
        
        try {
            double answer = Double.parseDouble(areaStr);
            incrementAttempts();
            
            Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
            double correctArea = sector.getCorrectArea();
            
            if (Math.abs(answer - correctArea) <= 0.1) {
                int currentAttempts = getAttempts();
                int points = currentAttempts == 1 ? 6 : 
                            currentAttempts == 2 ? 4 : 
                            currentAttempts == 3 ? 2 : 0;
                
                String feedback = String.format("Excellent! Correct answer!\nPoints earned: %d", points);
                setFeedback(feedback);
                
                areaSolutionArea.setText(sector.getSolution());
                
                areaField.setEnabled(false);
                submitButton.setEnabled(false);
                nextButton.setVisible(true);
                
                sectorCalculation.addPracticed(currentSectorIndex);
                
            } else if (!hasRemainingAttempts()) {
                String feedback = String.format("No more attempts.\nCorrect answer: %.1f %s²\nPoints earned: 0",
                    correctArea, sector.getUnit());
                setFeedback(feedback);
                
                areaSolutionArea.setText(sector.getSolution());
                
                areaField.setEnabled(false);
                submitButton.setEnabled(false);
                nextButton.setVisible(true);
                
                sectorCalculation.addPracticed(currentSectorIndex);
                
            } else {
                int remainingAttempts = getRemainingAttempts();
                int nextPoints = remainingAttempts == 2 ? 4 : 2;
                String feedback = String.format("Incorrect answer, please try again.\n%d attempts remaining, next correct answer worth %d points.", 
                                             remainingAttempts, nextPoints);
                setFeedback(feedback);
            }
        } catch (NumberFormatException e) {
            setFeedback("Please enter a valid number!");
        }
    }
    
    @Override
    public void reset() {
        currentSectorIndex = 0;
        resetTask();
        attemptsPerTask.clear();
        correctAnswers.clear();
        sectorCalculation.reset();
        score = 0;
        updateScoreLabel();
        showCurrentSector();
    }
    
    /**
     * Resets the current task state.
     */
    private void resetTask() {
        resetAttempts();
        areaCorrect = false;
        arcLengthCorrect = false;
        nextButton.setVisible(false);
    }
    
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        for (int i = 0; i < attemptsPerTask.size(); i++) {
            if (i < correctAnswers.size() && correctAnswers.get(i)) {
                int attempts = attemptsPerTask.get(i);
                if (attempts == 1) totalScore += 6;
                else if (attempts == 2) totalScore += 4;
                else if (attempts == 3) totalScore += 2;
            }
        }
        return totalScore;
    }
    
    @Override
    public void startTask() {
        currentSectorIndex = 0;
        showCurrentSector();
    }
    
    @Override
    public void pauseTask() {
        if (submitButton != null) {
            submitButton.setEnabled(false);
        }
        if (areaField != null) {
            areaField.setEnabled(false);
        }
        if (sectorSelector != null) {
            sectorSelector.setEnabled(false);
        }
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
    }
    
    @Override
    public void resumeTask() {
        if (submitButton != null) {
            submitButton.setEnabled(true);
        }
        if (areaField != null) {
            areaField.setEnabled(true);
        }
        if (sectorSelector != null) {
            sectorSelector.setEnabled(true);
        }
        if (questionTimer != null && !questionTimer.isRunning() && remainingTime > 0) {
            questionTimer.start();
        }
    }
    
    @Override
    public void endTask() {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        int totalScore = calculateScore();
        int maxScore = sectorCalculation.getSectors().size() * 6;
        
        String message = String.format("Practice completed!\nTotal score: %d/%d", totalScore, maxScore);
        JOptionPane.showMessageDialog(this, message, "Practice Complete", JOptionPane.INFORMATION_MESSAGE);
        
        if (parentWindow != null) {
            parentWindow.showResult(totalScore, maxScore);
        }
    }
    
    /**
     * Generates feedback based on the current score.
     * @param score The current score
     * @param maxScore The maximum possible score
     * @return A detailed feedback message
     */
    private String generateFeedback(int score, int maxScore) {
        double percentage = (double) score / maxScore * 100;
        StringBuilder feedback = new StringBuilder();
        
        feedback.append(String.format("Total score: %d/%d\n\n", score, maxScore));
        feedback.append("Detailed evaluation:\n");
        
        if (percentage >= 90) {
            feedback.append("Excellent! You have mastered sector area calculations.");
        } else if (percentage >= 80) {
            feedback.append("Very good! You have a strong understanding of sector calculations.");
        } else if (percentage >= 70) {
            feedback.append("Good! Keep practicing to improve further.");
        } else if (percentage >= 60) {
            feedback.append("Pass! More practice is recommended.");
        } else {
            feedback.append("Keep practicing! Review these formulas:\n");
            feedback.append("Area formula: A = (θ/360°) × πr²\n");
            feedback.append("Arc length formula: L = (θ/360°) × 2πr");
        }
        
        return feedback.toString();
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        return "Sector Area Calculation - Current Score: " + calculateScore();
    }

    /**
     * Gets the number of attempts made for the current question.
     * @return The current number of attempts
     */
    private int getAttempts() {
        return MAX_ATTEMPTS - getRemainingAttempts();
    }

    /**
     * Updates the feedback and score based on the answer correctness.
     * @param isCorrect Whether the answer was correct
     * @param isAreaCalculation Whether this was an area calculation (vs arc length)
     */
    private void updateFeedbackAndScore(boolean isCorrect, boolean isAreaCalculation) {
        String calculationType = isAreaCalculation ? "area" : "arc length";
        
        if (isCorrect) {
            setFeedback(calculationType + " calculation correct!");
            if (attempts == 1) {
                score += isAreaCalculation ? 6 : 4;
            } else if (attempts == 2) {
                score += isAreaCalculation ? 4 : 2;
            } else if (attempts == 3) {
                score += isAreaCalculation ? 2 : 1;
            }
        } else {
            attempts++;
            setFeedback(calculationType + " calculation incorrect, please try again. (" + 
                       (MAX_ATTEMPTS - attempts) + " attempts remaining)");
        }
        
        updateScoreLabel();
    }

    /**
     * Initializes the score display components.
     */
    private void initializeScoreComponents() {
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(scoreLabel);
    }
    
    /**
     * Updates the score display label.
     */
    private void updateScoreLabel() {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
        }
    }
    
    /**
     * Resets the score to zero.
     */
    private void resetScore() {
        score = 0;
        updateScoreLabel();
    }

    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        sectorLabel = new JLabel("", SwingConstants.CENTER);
        sectorLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        areaSolutionArea = new JTextArea();
        areaSolutionArea.setEditable(false);
        areaSolutionArea.setLineWrap(true);
        areaSolutionArea.setWrapStyleWord(true);
        
        areaField = new JTextField(10);
        
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(e -> handleSubmit());
        
        nextButton = new JButton("Next Question");
        nextButton.addActionListener(e -> goToNextQuestion());
        nextButton.setVisible(false);
        
        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        descriptionArea = new JTextArea(2, 40);
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBackground(new Color(240, 240, 240));
    }
    
    /**
     * Sets up the panel layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        List<Sector> sectors = sectorCalculation.getSectors();
        String[] sectorNames = new String[sectors.size()];
        for (int i = 0; i < sectors.size(); i++) {
            sectorNames[i] = "Sector " + (i + 1);
        }
        sectorSelector = new JComboBox<>(sectorNames);
        sectorSelector.addActionListener(e -> {
            if (!sectorCalculation.getPracticed().contains(sectorSelector.getSelectedIndex())) {
                currentSectorIndex = sectorSelector.getSelectedIndex();
                resetTask();
                showCurrentSector();
            }
        });
        selectorPanel.add(sectorSelector);
        selectorPanel.add(sectorLabel);
        selectorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(selectorPanel);
        
        sectorDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSector(g);
            }
        };
        sectorDisplayPanel.setPreferredSize(new Dimension(300, 300));
        sectorDisplayPanel.setBackground(Color.WHITE);
        sectorDisplayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(sectorDisplayPanel);
        
        JLabel formulaLabel = new JLabel("Remember: Sector area = (πr²×angle)/360°, Arc length = (2πr×angle)/360°");
        formulaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formulaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalStrut(20));
        mainContentPanel.add(formulaLabel);
        
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("Parameters"));
        descriptionScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalStrut(20));
        mainContentPanel.add(descriptionScroll);
        
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        inputPanel.add(new JLabel("Area: "));
        inputPanel.add(areaField);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalStrut(20));
        mainContentPanel.add(inputPanel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(buttonPanel);
        
        mainContentPanel.add(Box.createVerticalStrut(20));
        JLabel areaSolutionLabel = new JLabel("Area Calculation Steps:");
        areaSolutionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(areaSolutionLabel);
        JScrollPane areaSolutionScroll = new JScrollPane(areaSolutionArea);
        areaSolutionScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(areaSolutionScroll);
        
        mainContentPanel.add(Box.createVerticalStrut(20));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(feedbackLabel);
        mainContentPanel.add(Box.createVerticalStrut(5));
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(scoreLabel);
        
        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(mainScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Moves to the next question in the exercise.
     */
    private void goToNextQuestion() {
        do {
            currentSectorIndex++;
            if (currentSectorIndex >= sectorCalculation.getSectors().size()) {
                endTask();
                return;
            }
        } while (sectorCalculation.getPracticed().contains(currentSectorIndex));
        
        resetAttempts();
        showCurrentSector();
    }
    
    /**
     * Shows the solution for the current sector.
     */
    private void showSolutions() {
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        
        areaSolutionArea.setText(sector.getSolution());
        
        String feedback = String.format("Maximum attempts reached. Correct answers:\nArea: %.2f %s²",
                                   sector.getCorrectArea(), sector.getUnit());
        setFeedback(feedback);
        
        areaField.setEnabled(false);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
        
        sectorCalculation.addPracticed(currentSectorIndex);
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
     * Updates the timer display label.
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
            "Time's up for this question. Please review the solution steps and click Next to continue.",
            "Time Alert",
            JOptionPane.WARNING_MESSAGE);
        
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        String solution = sector.getSolution();
        if (solution == null || solution.isEmpty()) {
            solution = "No detailed solution steps available for this question";
        }
        areaSolutionArea.setText(solution);
        
        areaField.setEnabled(false);
        submitButton.setEnabled(false);
        nextButton.setVisible(true);
        
        setFeedback("Time's up! Correct answer: Area = " + String.format("%.1f", sector.getCorrectArea()));
    }
} 