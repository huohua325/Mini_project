package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.CompoundShapeCalculation;
import com.shapeville.game.CompoundShapeCalculation.CompoundShape;
import com.shapeville.gui.shapes.ShapeRenderer;
import com.shapeville.gui.UIManager;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel for compound shape calculations in the Shapeville application.
 * This panel allows users to practice calculating areas of compound shapes
 * through an interactive interface with visual shape representation.
 *
 * Features:
 * - Interactive shape selection
 * - Visual representation of compound shapes
 * - Real-time feedback
 * - Multiple attempts for each calculation
 * - Score tracking and timed exercises
 * - Detailed solution steps
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class CompoundShapeCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private static final int MAX_ATTEMPTS = 3;  // Maximum 3 attempts per question
    private static final int TIME_PER_QUESTION = 5 * 60; // 5 minutes time limit per question (seconds)
    private final CompoundShapeCalculation compoundCalculation;
    private int currentShapeIndex = 0;
    private List<Boolean> correctAnswers;  // Records whether each question was answered correctly
    private JLabel shapeLabel;
    private JTextArea descriptionArea;
    private JTextField answerField;
    private JButton submitButton;
    private JTextArea solutionArea;
    private JComboBox<String> shapeSelector;
    private ShapeDisplayPanel shapeDisplayPanel;
    private Timer questionTimer;
    private int remainingTime;
    private JLabel timerLabel;
    private JButton nextButton;
    
    /**
     * Constructs a new CompoundShapeCalculationPanel.
     * Initializes the compound shape calculation game and sets up the UI components.
     */
    public CompoundShapeCalculationPanel() {
        super("Compound Shape Calculation");
        try {
            this.compoundCalculation = new CompoundShapeCalculation();
            this.correctAnswers = new ArrayList<>();
            
            if (this.compoundCalculation.getShapes().isEmpty()) {
                throw new IllegalStateException("Shape list is empty");
            }
            
            initializeUI();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "Failed to initialize compound shapes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
    
    @Override
    public void initializeUI() {
        if (compoundCalculation == null || compoundCalculation.getShapes().isEmpty()) {
            setLayout(new BorderLayout());
            JLabel errorLabel = new JLabel("No compound shapes available", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(errorLabel, BorderLayout.CENTER);
            return;
        }
        
        // Initialize basic components
        submitButton = new JButton("Submit Answer");
        answerField = new JTextField(15);
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        descriptionArea = new JTextArea(3, 30);
        solutionArea = new JTextArea(4, 30);
        nextButton = new JButton("Next Question");
        nextButton.setVisible(false);
        nextButton.addActionListener(e -> goToNextQuestion());
        
        // Set layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Add timer label
        timerLabel = new JLabel("Time Remaining: 5:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLUE);
        topPanel.add(timerLabel, BorderLayout.EAST);
        
        // Create shape selector
        List<CompoundShape> shapes = compoundCalculation.getShapes();
        String[] shapeNames = shapes.stream()
                                  .map(CompoundShape::getName)
                                  .toArray(String[]::new);
        shapeSelector = new JComboBox<>(shapeNames);
        shapeSelector.addActionListener(e -> {
            if (!compoundCalculation.getPracticed().contains(shapeSelector.getSelectedIndex())) {
                currentShapeIndex = shapeSelector.getSelectedIndex();
                resetAttempts();
                showCurrentShape();
            }
        });
        topPanel.add(shapeSelector, BorderLayout.NORTH);
        
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(shapeLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Create center panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Create shape display panel
        shapeDisplayPanel = new ShapeDisplayPanel();
        
        // Create display container
        JPanel displayContainer = new JPanel(new BorderLayout());
        displayContainer.setBorder(BorderFactory.createTitledBorder("Shape Display"));
        displayContainer.setBackground(Color.WHITE);
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(shapeDisplayPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        Dimension displaySize = new Dimension(500, 400);
        shapeDisplayPanel.setPreferredSize(displaySize);
        displayContainer.setPreferredSize(displaySize);
        
        displayContainer.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(displayContainer, BorderLayout.CENTER);
        
        // Create info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // Set description area
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setBackground(new Color(240, 240, 240));
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("Shape Description"));
        infoPanel.add(descriptionScroll);
        
        // Create answer input area
        JPanel answerPanel = new JPanel();
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("Enter area (1 decimal place): "));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        answerPanel.add(nextButton);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(answerPanel);
        
        // Set solution area
        solutionArea.setEditable(false);
        solutionArea.setWrapStyleWord(true);
        solutionArea.setLineWrap(true);
        solutionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        solutionArea.setBackground(new Color(240, 240, 240));
        JScrollPane solutionScroll = new JScrollPane(solutionArea);
        solutionScroll.setBorder(BorderFactory.createTitledBorder("Solution Steps"));
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(solutionScroll);
        
        centerPanel.add(infoPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
        
        showCurrentShape();
        
        setFeedback("Please enter your answer and click Submit.");
        
        setVisible(true);
        revalidate();
        repaint();
    }
    
    /**
     * Displays the current shape and updates all related UI components.
     */
    private void showCurrentShape() {
        List<CompoundShape> shapes = compoundCalculation.getShapes();
        
        if (currentShapeIndex < shapes.size()) {
            CompoundShape shape = shapes.get(currentShapeIndex);
            
            shapeLabel.setText(shape.getName());
            descriptionArea.setText(shape.getDescription());
            solutionArea.setText("");
            solutionArea.setVisible(true);
            answerField.setText("");
            answerField.setEnabled(true);
            answerField.requestFocus();
            submitButton.setEnabled(true);
            nextButton.setVisible(false);
            shapeSelector.setSelectedIndex(currentShapeIndex);
            
            if (shapeDisplayPanel != null) {
                shapeDisplayPanel.setCurrentShape(shape.getRenderer());
                shapeDisplayPanel.setVisible(true);
                shapeDisplayPanel.revalidate();
                shapeDisplayPanel.repaint();
                
                Container parent = shapeDisplayPanel.getParent();
                if (parent != null) {
                    parent.revalidate();
                    parent.repaint();
                }
            }
            
            startQuestionTimer();
        } else {
            endTask();
        }
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
            "Time's up! Please review the solution steps and click Next to continue.",
            "Time Alert",
            JOptionPane.WARNING_MESSAGE);
        
        CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
        String solution = shape.getSolution();
        if (solution == null || solution.isEmpty()) {
            solution = "No detailed solution steps available for this question";
        }
        solutionArea.setText(solution);
        solutionArea.revalidate();
        solutionArea.repaint();
        
        completedCurrentQuestion(false);
    }
    
    /**
     * Marks the current question as completed and prepares for the next question.
     * @param correct Whether the question was answered correctly
     */
    private void completedCurrentQuestion(boolean correct) {
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        correctAnswers.add(correct);
        
        CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
        String feedback = "";
        if (!correct) {
            feedback = "Time's up or three incorrect attempts. The correct answer is: " + 
                      String.format("%.1f", shape.getCorrectArea()) + 
                      "\nPoints earned: 0\nLet's review the solution steps:";
        } else {
            int currentAttempts = getAttempts();
            int points = currentAttempts == 1 ? 6 : 
                        currentAttempts == 2 ? 4 : 
                        currentAttempts == 3 ? 2 : 0;
            feedback = String.format("Correct answer!\nPoints earned: %d\nLet's review the solution steps:", points);
        }
        setFeedback(feedback);
        
        String solution = shape.getSolution();
        if (solution == null || solution.isEmpty()) {
            solution = "No detailed solution steps available for this question";
        }
        solutionArea.setText(solution);
        
        solutionArea.revalidate();
        solutionArea.repaint();
        
        compoundCalculation.addPracticed(currentShapeIndex);
        addAttemptToList();
        
        if (compoundCalculation.isComplete()) {
            JOptionPane.showMessageDialog(this, 
                "You have completed all compound shape exercises.", 
                "Task Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            endTask();
            return;
        }
        
        submitButton.setEnabled(false);
        answerField.setEnabled(false);
        nextButton.setVisible(true);
    }
    
    /**
     * Moves to the next question in the exercise.
     */
    private void goToNextQuestion() {
        do {
            currentShapeIndex++;
            if (currentShapeIndex >= compoundCalculation.getShapes().size()) {
                endTask();
                return;
            }
        } while (compoundCalculation.getPracticed().contains(currentShapeIndex));
        
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
        nextButton.setVisible(false);
        resetAttempts();
        
        showCurrentShape();
    }
    
    /**
     * Internal panel for displaying compound shapes.
     */
    private static class ShapeDisplayPanel extends JPanel {
        private ShapeRenderer currentShape;
        private static final int MARGIN = 40;
        
        public ShapeDisplayPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(500, 400));
            setMinimumSize(new Dimension(500, 400));
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setOpaque(true);
        }
        
        public void setCurrentShape(ShapeRenderer shape) {
            this.currentShape = shape;
            
            if (shape != null) {
                int width = Math.max(500, getPreferredSize().width);
                int height = Math.max(400, getPreferredSize().height);
                setPreferredSize(new Dimension(width, height));
            }
            
            revalidate();
            repaint();
        }
        
        @Override
        public Dimension getPreferredSize() {
            if (currentShape != null) {
                return new Dimension(500 + 2 * MARGIN, 400 + 2 * MARGIN);
            }
            return new Dimension(500, 400);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            
            if (currentShape != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                try {
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    int drawingWidth = getWidth() - 2 * MARGIN;
                    int drawingHeight = getHeight() - 2 * MARGIN;
                    
                    g2d.translate(MARGIN, MARGIN);
                    currentShape.draw(g2d, drawingWidth, drawingHeight);
                    currentShape.drawDimensions(g2d, drawingWidth, drawingHeight);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    g2d.dispose();
                }
            } else {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.PLAIN, 16));
                String message = "No shape to display";
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(message)) / 2;
                int y = (getHeight() + fm.getHeight()) / 2;
                g.drawString(message, x, y);
            }
        }
    }
    
    @Override
    public void handleSubmit() {
        String answerStr = answerField.getText().trim();
        
        try {
            double answer = Double.parseDouble(answerStr);
            incrementAttempts();
            
            if (compoundCalculation.checkAnswer(currentShapeIndex, answer)) {
                int currentAttempts = getAttempts();
                int points = currentAttempts == 1 ? 6 : 
                            currentAttempts == 2 ? 4 : 
                            currentAttempts == 3 ? 2 : 0;
                String feedback = String.format("Excellent! Correct answer!\nPoints earned: %d", points);
                setFeedback(feedback);
                
                CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
                String solution = shape.getSolution();
                if (solution == null || solution.isEmpty()) {
                    solution = "No detailed solution steps available for this question";
                }
                solutionArea.setText(solution);
                solutionArea.revalidate();
                solutionArea.repaint();
                
                submitButton.setEnabled(false);
                answerField.setEnabled(false);
                
                completedCurrentQuestion(true);
                
            } else if (!hasRemainingAttempts()) {
                CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
                String feedback = "No more attempts. The correct answer is: " + 
                                String.format("%.1f", shape.getCorrectArea()) + 
                                "\nPoints earned: 0\nLet's review the solution steps:";
                setFeedback(feedback);
                
                String solution = shape.getSolution();
                if (solution == null || solution.isEmpty()) {
                    solution = "No detailed solution steps available for this question";
                }
                solutionArea.setText(solution);
                solutionArea.revalidate();
                solutionArea.repaint();
                
                submitButton.setEnabled(false);
                answerField.setEnabled(false);
                
                completedCurrentQuestion(false);
                
            } else {
                int remainingAttempts = getRemainingAttempts();
                int nextPoints = remainingAttempts == 2 ? 4 : 2;
                String feedback = String.format("Incorrect answer. Try again.\n%d attempts remaining, next correct answer worth %d points.", 
                                        remainingAttempts, nextPoints);
                setFeedback(feedback);
            }
        } catch (NumberFormatException e) {
            setFeedback("Please enter a valid number!");
        }
    }
    
    @Override
    public void reset() {
        currentShapeIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        correctAnswers.clear();
        compoundCalculation.reset();
        showCurrentShape();
    }
    
    /**
     * Gets the number of attempts made for the current question.
     * @return The current number of attempts
     */
    private int getAttempts() {
        return MAX_ATTEMPTS - getRemainingAttempts();
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
        currentShapeIndex = 0;
        showCurrentShape();
    }
    
    @Override
    public void pauseTask() {
        if (submitButton != null) {
            submitButton.setEnabled(false);
        }
        if (answerField != null) {
            answerField.setEnabled(false);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(false);
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
        if (answerField != null) {
            answerField.setEnabled(true);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(true);
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
        
        if (parentWindow != null) {
            int score = calculateScore();
            int maxScore = compoundCalculation.getShapes().size() * 6;
            
            parentWindow.showResult(score, maxScore);
        }
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        return "Compound Shape Calculation - Current Score: " + calculateScore();
    }
}
