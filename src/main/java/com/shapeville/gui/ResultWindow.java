package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ResultWindow class displays the final results of a completed task.
 * It shows the score, performance metrics, and provides options for continuing or ending the session.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class ResultWindow extends JFrame {
    private final int score;
    private final int maxScore;
    private final String feedback;
    private final String taskName;
    private Timer animationTimer;
    private int currentScore = 0;
    private JLabel scoreLabel;
    private JPanel starsPanel;
    private JProgressBar scoreProgress;
    
    /**
     * Constructs a new ResultWindow with the specified task results.
     *
     * @param taskName the name of the completed task
     * @param score the score achieved
     * @param maxScore the maximum possible score
     * @param feedback detailed feedback about the performance
     */
    public ResultWindow(String taskName, int score, int maxScore, String feedback) {
        this.taskName = taskName;
        this.score = score;
        this.maxScore = maxScore;
        this.feedback = feedback;
        initializeUI();
        startScoreAnimation();
    }
    
    /**
     * Initializes the user interface components of the result window.
     */
    private void initializeUI() {
        setTitle("Shapeville - Task Results");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Task name
        JLabel taskLabel = new JLabel(taskName);
        taskLabel.setFont(new Font("Arial", Font.BOLD, 24));
        taskLabel.setHorizontalAlignment(SwingConstants.CENTER);
        taskLabel.setForeground(new Color(51, 51, 153));
        topPanel.add(taskLabel, BorderLayout.NORTH);
        
        // Score display panel
        JPanel scorePanel = new JPanel(new BorderLayout(10, 10));
        scorePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(new Color(51, 153, 255));
        
        scoreProgress = new JProgressBar(0, 100);
        scoreProgress.setValue(0);
        scoreProgress.setStringPainted(true);
        scoreProgress.setPreferredSize(new Dimension(0, 20));
        
        scorePanel.add(scoreLabel, BorderLayout.CENTER);
        scorePanel.add(scoreProgress, BorderLayout.SOUTH);
        
        topPanel.add(scorePanel, BorderLayout.CENTER);
        
        // Create stars rating panel
        starsPanel = createStarsPanel();
        topPanel.add(starsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create feedback panel
        JPanel feedbackPanel = new JPanel(new BorderLayout(10, 10));
        feedbackPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Detailed Feedback"
        ));
        
        // Create scrollable feedback area
        JPanel feedbackContent = new JPanel();
        feedbackContent.setLayout(new BoxLayout(feedbackContent, BoxLayout.Y_AXIS));
        
        // Add statistics
        if (taskName.equals("Shape Recognition")) {
            String[] lines = feedback.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    JLabel label = new JLabel(line);
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                    label.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
                    label.setAlignmentX(Component.LEFT_ALIGNMENT);
                    feedbackContent.add(label);
                }
            }
        } else {
            JTextArea feedbackArea = new JTextArea(feedback);
            feedbackArea.setEditable(false);
            feedbackArea.setWrapStyleWord(true);
            feedbackArea.setLineWrap(true);
            feedbackArea.setFont(new Font("Arial", Font.PLAIN, 14));
            feedbackArea.setMargin(new Insets(10, 10, 10, 10));
            feedbackContent.add(feedbackArea);
        }
        
        JScrollPane scrollPane = new JScrollPane(feedbackContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        feedbackPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add performance analysis
        JPanel analysisPanel = new JPanel();
        analysisPanel.setLayout(new BoxLayout(analysisPanel, BoxLayout.Y_AXIS));
        analysisPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        addAnalysisItem(analysisPanel, "Completion", getCompletionStatus());
        addAnalysisItem(analysisPanel, "Accuracy", getAccuracyStatus());
        addAnalysisItem(analysisPanel, "Performance", getPerformanceLevel());
        
        feedbackPanel.add(analysisPanel, BorderLayout.SOUTH);
        
        mainPanel.add(feedbackPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton continueButton = createStyledButton("Continue Learning", new Color(51, 153, 255));
        continueButton.addActionListener(e -> {
            dispose();
            com.shapeville.gui.UIManager.getInstance().showMainWindow();
        });
        
        JButton retryButton = createStyledButton("Try Again", new Color(255, 153, 51));
        retryButton.addActionListener(e -> {
            dispose();
            com.shapeville.gui.UIManager.getInstance().switchToTask(taskName);
        });
        
        JButton exitButton = createStyledButton("Exit", new Color(255, 51, 51));
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });
        
        buttonPanel.add(continueButton);
        buttonPanel.add(retryButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Creates a styled button with specified text and background color.
     *
     * @param text the button text
     * @param color the background color
     * @return the styled JButton
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }
    
    /**
     * Creates a panel displaying star ratings.
     *
     * @return the stars panel
     */
    private JPanel createStarsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setOpaque(false);
        // Create 5 gray stars initially
        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel("★");
            star.setFont(new Font("Dialog", Font.PLAIN, 32));
            star.setForeground(Color.LIGHT_GRAY);
            panel.add(star);
        }
        return panel;
    }
    
    /**
     * Updates the star display based on performance.
     *
     * @param starCount number of filled stars to display
     */
    private void updateStars(int starCount) {
        starsPanel.removeAll();
        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel("★");
            star.setFont(new Font("Dialog", Font.PLAIN, 32));
            star.setForeground(i < starCount ? Color.ORANGE : Color.LIGHT_GRAY);
            starsPanel.add(star);
        }
        starsPanel.revalidate();
        starsPanel.repaint();
    }
    
    /**
     * Starts the score animation sequence.
     */
    private void startScoreAnimation() {
        animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentScore < score) {
                    currentScore += 1;
                    scoreLabel.setText(String.format("%d/%d", currentScore, maxScore));
                    int percentageScore = maxScore > 0 ? (int)((double)currentScore / maxScore * 100) : 0;
                    scoreProgress.setValue(percentageScore);
                    
                    int stars = percentageScore >= 90 ? 5 : 
                               percentageScore >= 80 ? 4 : 
                               percentageScore >= 70 ? 3 : 
                               percentageScore >= 60 ? 2 : 1;
                    updateStars(stars);
                    
                    if (percentageScore >= 90) {
                        scoreProgress.setForeground(new Color(0, 153, 0));  // Dark green
                    } else if (percentageScore >= 80) {
                        scoreProgress.setForeground(new Color(0, 102, 204));  // Blue
                    } else if (percentageScore >= 70) {
                        scoreProgress.setForeground(new Color(255, 153, 0));  // Orange
                    } else {
                        scoreProgress.setForeground(new Color(255, 51, 51));  // Red
                    }
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        animationTimer.start();
    }
    
    /**
     * Adds an analysis item to the specified panel.
     *
     * @param panel the panel to add the item to
     * @param label the label text
     * @param value the value text
     */
    private void addAnalysisItem(JPanel panel, String label, String value) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelComponent = new JLabel(label + ": ");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        itemPanel.add(labelComponent);
        itemPanel.add(valueComponent);
        panel.add(itemPanel);
    }
    
    /**
     * Gets the completion status text based on score.
     *
     * @return the completion status text
     */
    private String getCompletionStatus() {
        double percentageScore = maxScore > 0 ? (double)score / maxScore * 100 : 0;
        if (percentageScore >= 90) return "Perfect";
        if (percentageScore >= 80) return "Excellent";
        if (percentageScore >= 70) return "Good";
        if (percentageScore >= 60) return "Satisfactory";
        return "Needs Practice";
    }
    
    /**
     * Gets the accuracy status text.
     *
     * @return the accuracy status text
     */
    private String getAccuracyStatus() {
        return String.format("%d/%d (Max: %d)", score, maxScore, maxScore);
    }
    
    /**
     * Gets the performance level text based on score.
     *
     * @return the performance level text
     */
    private String getPerformanceLevel() {
        double percentageScore = maxScore > 0 ? (double)score / maxScore * 100 : 0;
        if (percentageScore >= 90) return "S (Outstanding)";
        if (percentageScore >= 80) return "A (Excellent)";
        if (percentageScore >= 70) return "B (Good)";
        if (percentageScore >= 60) return "C (Pass)";
        return "D (Fail)";
    }
    
    /**
     * Cleans up resources when the window is disposed.
     */
    @Override
    public void dispose() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        super.dispose();
    }
}
