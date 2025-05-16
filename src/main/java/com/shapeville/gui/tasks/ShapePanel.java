package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;
import com.shapeville.game.ShapeRecognition;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The ShapePanel class represents a panel for shape recognition tasks.
 * It allows users to identify both 2D and 3D shapes through an interactive interface.
 * The panel provides immediate feedback and tracks user performance.
 *
 * Features:
 * - Supports both 2D and 3D shape recognition
 * - Provides visual feedback for user answers
 * - Tracks progress and performance
 * - Implements adaptive difficulty based on user performance
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class ShapePanel extends BaseTaskPanel {
    private static final Logger logger = Logger.getLogger(ShapePanel.class.getName());
    private final ShapeRecognition shapeRecognition;
    private int currentShapeIndex = 0;
    private boolean is2DMode = true;
    private JLabel shapeLabel;
    private JLabel shapeImageLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private ArrayList<Shape2D> shapes2D;
    private ArrayList<Shape3D> shapes3D;
    private ArrayList<Integer> attemptsPerTask;
    private ArrayList<Boolean> correctAnswers;
    private Map<JComponent, Boolean> componentStates;
    
    /** Number of shapes to test for both 2D and 3D modes */
    private static final int SHAPES_PER_TEST = 4;
    private volatile boolean isEnding = false;
    
    /**
     * Constructs a new ShapePanel for shape recognition tasks.
     * Initializes the UI components and shape collections.
     */
    public ShapePanel() {
        super("Shape Recognition");
        
        this.shapeRecognition = new ShapeRecognition();
        shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
        shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
        attemptsPerTask = new ArrayList<>();
        correctAnswers = new ArrayList<>();
        componentStates = new HashMap<>();
        
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
        
        setupShapeUI();
    }
    
    /**
     * Initializes the shape collections by clearing existing lists and
     * adding shuffled shape values. Limits the number of shapes to SHAPES_PER_TEST.
     */
    private void initializeShapes() {
        shapes2D.clear();
        shapes3D.clear();
        attemptsPerTask.clear();
        correctAnswers.clear();
        
        shapes2D.addAll(Arrays.asList(Shape2D.values()));
        shapes3D.addAll(Arrays.asList(Shape3D.values()));
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
        
        if (shapes2D.size() > SHAPES_PER_TEST) {
            shapes2D = new ArrayList<>(shapes2D.subList(0, SHAPES_PER_TEST));
        }
        if (shapes3D.size() > SHAPES_PER_TEST) {
            shapes3D = new ArrayList<>(shapes3D.subList(0, SHAPES_PER_TEST));
        }
        
        currentShapeIndex = 0;
        isEnding = false;
        logger.info("Initialized shapes: " + SHAPES_PER_TEST + " shapes per test mode");
    }
    
    /**
     * Sets up the main UI components of the shape panel.
     * Creates and arranges the content panels.
     */
    private void setupShapeUI() {
        JPanel shapeContentPanel = new JPanel(new BorderLayout(10, 10));
        shapeContentPanel.add(createTopPanel(), BorderLayout.NORTH);
        shapeContentPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        add(shapeContentPanel, BorderLayout.CENTER);
        
        showCurrentShape();
    }
    
    /**
     * Implements the abstract method from BaseTaskPanel.
     * Sets up the basic layout for the panel.
     */
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout());
    }
    
    /**
     * Creates the top panel containing navigation and mode selection controls.
     * @return JPanel containing the top controls
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        homeButton = new JButton("Home");
        homeButton.addActionListener(e -> endTask());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addModeButton(modePanel, "2D Shapes", true);
        addModeButton(modePanel, "3D Shapes", false);
        topPanel.add(modePanel, BorderLayout.CENTER);
        
        shapeLabel = new JLabel("Identify the shape:", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(shapeLabel, BorderLayout.SOUTH);
        
        return topPanel;
    }
    
    /**
     * Adds a mode selection button to the specified panel.
     * @param panel The panel to add the button to
     * @param text The button text
     * @param is2D Whether this button is for 2D mode
     */
    private void addModeButton(JPanel panel, String text, boolean is2D) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            is2DMode = is2D;
            currentShapeIndex = 0;
            resetAttempts();
            initializeShapes();
            showCurrentShape();
        });
        panel.add(button);
    }
    
    /**
     * Creates the main content panel containing the shape image and answer input area.
     * @return JPanel containing the main content
     */
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        shapeImageLabel = new JLabel();
        shapeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        imagePanel.add(shapeImageLabel, BorderLayout.CENTER);
        
        JPanel answerPanel = createAnswerPanel();
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        
        gbc.gridx = 0;
        gbc.weightx = 0.6;
        contentPanel.add(imagePanel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        contentPanel.add(answerPanel, gbc);
        
        return contentPanel;
    }
    
    /**
     * Creates the answer input panel with text field and submit button.
     * @return JPanel containing the answer input components
     */
    private JPanel createAnswerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel inputLabel = new JLabel("Enter the shape name in English:");
        inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        answerField = new JTextField(20);
        answerField.setMaximumSize(new Dimension(200, 30));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        submitButton = new JButton("Submit Answer");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> handleSubmit());
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(inputLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(answerField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(submitButton);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }
    
    /**
     * Checks if the current shape index is valid and the task is not ending.
     * @return true if the index is valid and task is not ending
     */
    private boolean isValidShapeIndex() {
        ArrayList<?> currentShapes = is2DMode ? shapes2D : shapes3D;
        return !isEnding && currentShapeIndex >= 0 && currentShapeIndex < currentShapes.size();
    }
    
    /**
     * Gets the current shape object based on the current mode.
     * @return Shape2D or Shape3D object at the current index
     */
    private Object getCurrentShape() {
        return is2DMode ? shapes2D.get(currentShapeIndex) : shapes3D.get(currentShapeIndex);
    }
    
    /**
     * Checks if the given shape has already been correctly identified.
     * @param shape the shape to check
     * @return true if the shape has been identified, false otherwise
     */
    private boolean isShapeAlreadyIdentified(Object shape) {
        return is2DMode ? 
                shapeRecognition.isTypeIdentified2D((Shape2D)shape) :
                shapeRecognition.isTypeIdentified3D((Shape3D)shape);
    }
    
    /**
     * Updates the display with the current shape information.
     * @param shape the shape to display
     */
    private void updateShapeDisplay(Object shape) {
            String chinese = is2DMode ? ((Shape2D)shape).getChinese() : ((Shape3D)shape).getChinese();
            String english = is2DMode ? ((Shape2D)shape).getEnglish() : ((Shape3D)shape).getEnglish();
            
        shapeLabel.setText("Identify this " + (is2DMode ? "2D" : "3D") + " shape:");
            displayShapeImage(english.toLowerCase() + ".png", is2DMode);
            answerField.setText("");
            answerField.requestFocus();
            
        String remainingMessage = shapeRecognition.getRemainingTypesMessage(is2DMode);
        String englishMessage = remainingMessage.replace("还需要识别", "Need to identify")
                                              .replace("种不同的", "more different ")
                                              .replace("形状", "shapes")
                                              .replace("已完成所有形状类型的识别！", "All shape types have been identified!");
        updateFeedback(englishMessage);
    }
    
    /**
     * Displays the shape image with proper scaling.
     * @param imageName the name of the image file
     * @param is2D whether the shape is 2D or 3D
     */
    private void displayShapeImage(String imageName, boolean is2D) {
        Path imagePath = getImagePath(imageName, is2D);
        try {
            File imageFile = imagePath.toFile();
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
                int maxSize = 160;
                
                double scale = Math.min((double)maxSize / img.getWidth(), (double)maxSize / img.getHeight());
                int newWidth = (int)(img.getWidth() * scale);
                int newHeight = (int)(img.getHeight() * scale);
                
                Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                shapeImageLabel.setIcon(new ImageIcon(scaledImg));
                shapeImageLabel.setPreferredSize(new Dimension(maxSize + 20, maxSize + 20));
            } else {
                shapeImageLabel.setIcon(null);
                shapeImageLabel.setText("Image not found: " + imagePath);
                logger.warning("Image file not found: " + imagePath);
            }
        } catch (IOException e) {
            shapeImageLabel.setIcon(null);
            shapeImageLabel.setText("Failed to load image: " + e.getMessage());
            logger.log(Level.SEVERE, "Failed to load image: " + imagePath, e);
        }
    }
    
    /**
     * Gets the path to the shape image file.
     * @param imageName the name of the image file
     * @param is2D whether the shape is 2D or 3D
     * @return Path to the image file
     */
    private Path getImagePath(String imageName, boolean is2D) {
        return Paths.get(System.getProperty("user.dir"))
                   .resolve("shapeville")
                   .resolve("src")
                   .resolve("main")
                   .resolve("resources")
                   .resolve("images")
                   .resolve(is2D ? "2d" : "3d")
                   .resolve(imageName);
    }
    
    /**
     * Updates the feedback message in the UI.
     * @param message the feedback message to display
     */
    private void updateFeedback(String message) {
        if (message == null || message.isEmpty()) {
            shapeLabel.setText("Identify the shape:");
        } else {
            shapeLabel.setText(message);
    }
    }
    
    /**
     * Handles the submission of an answer.
     * Validates the answer and provides appropriate feedback.
     */
    @Override
    public void handleSubmit() {
        try {
        String answer = answerField.getText().trim().toLowerCase();
            Object shape = getCurrentShape();
            String correctAnswer = getCorrectAnswer(shape);
            boolean correct = checkAnswer(shape, answer);
            
            handleAnswerResult(correct, correctAnswer);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error handling submit", e);
            updateFeedback("Error processing answer: " + e.getMessage());
        }
    }
    
    /**
     * Gets the correct answer for the given shape.
     * @param shape the shape to get the answer for
     * @return the correct English name of the shape
     */
    private String getCorrectAnswer(Object shape) {
        return is2DMode ? 
            ((Shape2D)shape).getEnglish().toLowerCase() :
                                        ((Shape3D)shape).getEnglish().toLowerCase();
    }
    
    /**
     * Checks if the provided answer is correct.
     * @param shape the shape being identified
     * @param answer the user's answer
     * @return true if the answer is correct, false otherwise
     */
    private boolean checkAnswer(Object shape, String answer) {
        return is2DMode ? 
            shapeRecognition.check2DAnswer((Shape2D)shape, answer) :
                                   shapeRecognition.check3DAnswer((Shape3D)shape, answer);
    }
    
    /**
     * Handles the result of an answer submission.
     * @param correct whether the answer was correct
     * @param correctAnswer the correct answer
     */
    private void handleAnswerResult(boolean correct, String correctAnswer) {
        if (correct) {
            updateFeedback("Correct! Well done!");
            handleCorrectAnswer();
        } else {
            int remainingAttempts = 3 - attemptsPerTask.get(currentShapeIndex);
            if (remainingAttempts > 0) {
                updateFeedback("Incorrect. Try again! (" + remainingAttempts + " attempts left)");
            } else {
                handleMaxAttemptsReached(correctAnswer);
            }
        }
    }
    
    /**
     * Handles a correct answer submission.
     */
    private void handleCorrectAnswer() {
        updateFeedback("Correct! Well done!");
            correctAnswers.add(true);
            addAttemptToList();
            currentShapeIndex++;
            resetAttempts();
            
        if (isTaskComplete()) {
                endTask();
        } else {
            showCurrentShape();
        }
    }
    
    /**
     * Handles when maximum attempts are reached without a correct answer.
     * @param correctAnswer the correct answer to show
     */
    private void handleMaxAttemptsReached(String correctAnswer) {
        updateFeedback("The correct answer was: " + correctAnswer);
        currentShapeIndex++;
        Timer timer = new Timer(2000, e -> showCurrentShape());
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Checks if the current task is complete.
     * @return true if all shapes have been identified, false otherwise
     */
    private boolean isTaskComplete() {
        return (is2DMode && shapeRecognition.is2DComplete()) || 
               (!is2DMode && shapeRecognition.is3DComplete());
        }
    
    /**
     * Calculates the score based on the number of attempts per correct answer.
     * @return the total score
     */
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        int size = Math.min(attemptsPerTask.size(), correctAnswers.size());
        for (int i = 0; i < size; i++) {
            if (correctAnswers.get(i)) {
                int attempts = attemptsPerTask.get(i);
                if (attempts == 1) totalScore += 3;
                else if (attempts == 2) totalScore += 2;
                else if (attempts == 3) totalScore += 1;
            }
        }
        return totalScore;
    }
    
    /**
     * Gets the feedback message for the task completion.
     * @return the formatted feedback message
     */
    @Override
    public String getFeedback() {
        int score = calculateScore();
        int maxScore = attemptsPerTask.size() * 3;

        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("Test completed!\nScore: %d (Maximum: %d)\n\n", score, maxScore));
        feedback.append("Shape Recognition Test Statistics:\n");
        
        int totalAttempted = attemptsPerTask.size();
        int correctWithinThree = 0;
        
        for (int i = 0; i < attemptsPerTask.size(); i++) {
            if (correctAnswers.get(i)) {
                correctWithinThree++;
            }
        }
        
        feedback.append(String.format("Total shapes attempted: %d\n", totalAttempted));
        feedback.append(String.format("Correct answers: %d\n", correctWithinThree));
        
        double scorePercentage = maxScore > 0 ? (double)score / maxScore * 100 : 0;
        feedback.append("\n").append(scorePercentage >= 90 ? "Excellent! You're very familiar with these shapes!" :
                                   scorePercentage >= 80 ? "Great job! Keep practicing!" :
                                   scorePercentage >= 70 ? "Good effort! Keep practicing!" :
                                                         "More practice needed to improve recognition skills!");
        
        return feedback.toString();
    }
    
    /**
     * Ends the current task and displays the results.
     */
    @Override
    public void endTask() {
        if (!isEnding) {
            isEnding = true;
                cleanup();
                if (parentWindow != null) {
                int score = calculateScore();
                int maxScore = attemptsPerTask.size() * 3;
                parentWindow.showResult(score, maxScore);
                }
        }
    }
    
    /**
     * Resets the task to its initial state.
     */
    @Override
    public void reset() {
        currentShapeIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        correctAnswers.clear();
        is2DMode = true;
        shapeRecognition.reset();
        initializeShapes();
        showCurrentShape();
        updateFeedback(shapeRecognition.getRemainingTypesMessage(is2DMode));
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
        saveComponentStates();
        submitButton.setEnabled(false);
        answerField.setEnabled(false);
        homeButton.setEnabled(false);
    }
    
    /**
     * Resumes the task by restoring UI component states.
     */
    @Override
    public void resumeTask() {
        restoreComponentStates();
    }
    
    /**
     * Gets the current score.
     * @return the current score
     */
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    /**
     * Cleans up resources when the task ends.
     */
    @Override
    public void cleanup() {
        super.cleanup();
        if (shapeImageLabel != null && shapeImageLabel.getIcon() != null) {
            ImageIcon icon = (ImageIcon) shapeImageLabel.getIcon();
            icon.getImage().flush();
            shapeImageLabel.setIcon(null);
        }
    }
    
    /**
     * Saves the current state of UI components.
     */
    private void saveComponentStates() {
        componentStates.clear();
        componentStates.put(submitButton, submitButton.isEnabled());
        componentStates.put(answerField, answerField.isEnabled());
        componentStates.put(homeButton, homeButton.isEnabled());
    }
    
    /**
     * Restores the saved state of UI components.
     */
    private void restoreComponentStates() {
        componentStates.forEach((component, enabled) -> component.setEnabled(enabled));
    }
    
    /**
     * Displays the current shape and updates the UI accordingly.
     * If there are no more shapes to show, ends the task.
     */
    private void showCurrentShape() {
        if (!isValidShapeIndex()) {
            endTask();
            return;
        }

        Object currentShape = getCurrentShape();
        if (currentShape == null || isShapeAlreadyIdentified(currentShape)) {
            currentShapeIndex++;
            if (isValidShapeIndex()) {
                showCurrentShape();
            } else {
                endTask();
            }
            return;
        }

        updateShapeDisplay(currentShape);
        answerField.setText("");
        answerField.requestFocus();
    }
    
    /**
     * Adds the current attempt count to the list.
     */
    private void addAttemptToList() {
        if (currentShapeIndex >= attemptsPerTask.size()) {
            attemptsPerTask.add(1);
        } else {
            attemptsPerTask.set(currentShapeIndex, attemptsPerTask.get(currentShapeIndex) + 1);
        }
    }
    
    /**
     * Resets the attempts for the current shape.
     */
    private void resetAttempts() {
        if (currentShapeIndex >= attemptsPerTask.size()) {
            attemptsPerTask.add(0);
        } else {
            attemptsPerTask.set(currentShapeIndex, 0);
        }
    }
} 