package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import com.shapeville.gui.UIManager.TaskStatus;

/**
 * The MainWindow class represents the main graphical user interface of the Shapeville application.
 * It provides a central hub for accessing various geometric learning tasks and displays user progress.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class MainWindow extends JFrame {
    private JProgressBar progressBar;
    private JPanel buttonPanel;
    private JButton homeButton;
    private JButton endButton;
    private JLabel levelLabel;
    private Map<String, JButton> taskButtons;
    private Map<String, Boolean> taskCompletionStatus;
    private boolean fullFeaturesEnabled = false;  
    private JToggleButton featureToggleButton;
    
    /** Task display names mapping */
    private static final Map<String, String> TASK_DISPLAY_NAMES = Map.of(
        "形状识别", "Shape Recognition",
        "角度识别", "Angle Recognition",
        "面积计算", "Area Calculation",
        "圆形计算", "Circle Calculation",
        "复合形状", "Compound Shapes",
        "扇形计算", "Sector Calculation"
    );
    
    /**
     * Constructs a new MainWindow and initializes the user interface components.
     */
    public MainWindow() {
        JOptionPane.setDefaultLocale(new Locale("en", "US"));
        taskButtons = new HashMap<>();
        taskCompletionStatus = new HashMap<>();
        initializeUI();
    }
    
    /**
     * Initializes the user interface components of the main window.
     * Sets up the layout, buttons, progress bar, and other UI elements.
     */
    private void initializeUI() {
        setTitle("Shapeville");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Create welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Shapeville Geometry Learning Park!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(51, 51, 153));
        
        // Create level label
        levelLabel = new JLabel("Current Level: Beginner", SwingConstants.RIGHT);
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(levelLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create task button panel
        buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Add task buttons
        addTaskButton("形状识别", "Identify 2D and 3D shapes (Basic Level)", "basic");
        addTaskButton("角度识别", "Identify angle types and measurements (Basic Level)", "basic");
        addTaskButton("面积计算", "Calculate areas of basic shapes (Basic Level)", "basic");
        addTaskButton("圆形计算", "Calculate circle circumference and area (Basic Level)", "basic");
        addTaskButton("复合形状", "Calculate areas of compound shapes (Advanced Level)", "advanced");
        addTaskButton("扇形计算", "Calculate sector area and arc length (Advanced Level)", "advanced");
        
        // Create center panel (includes task buttons and description)
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Create task description panel
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Task Description"));
        descriptionPanel.setPreferredSize(new Dimension(0, 100));
        JLabel descriptionLabel = new JLabel("<html>Complete basic tasks to unlock advanced tasks<br>Each task has its own score and star rating</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionPanel.add(descriptionLabel);
        centerPanel.add(descriptionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Create bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        // Create progress panel
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Learning Progress"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(0, 25));
        int totalTasks = com.shapeville.gui.UIManager.getInstance().getTotalTaskCount();
        progressBar.setString(String.format("Total Score: 0 points (Completed: 0/%d)", totalTasks));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // Create control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setupControlPanel(controlPanel);
        
        bottomPanel.add(progressPanel, BorderLayout.CENTER);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Initialize task status
        initializeTaskStatus();
    }
    
    /**
     * Sets up the control panel with home, feature toggle, and end session buttons.
     *
     * @param controlPanel The panel to add the control buttons to
     */
    private void setupControlPanel(JPanel controlPanel) {
        homeButton = createStyledButton("Home", new Color(51, 153, 255));
        endButton = createStyledButton("End Session", new Color(255, 51, 51));
        
        // Create feature toggle button
        featureToggleButton = new JToggleButton("Full Features");
        featureToggleButton.setFont(new Font("Arial", Font.BOLD, 14));
        featureToggleButton.setForeground(Color.WHITE);
        featureToggleButton.setBackground(new Color(128, 0, 128));
        featureToggleButton.setFocusPainted(false);
        featureToggleButton.setBorderPainted(false);
        featureToggleButton.setOpaque(true);
        
        featureToggleButton.addActionListener(e -> {
            fullFeaturesEnabled = featureToggleButton.isSelected();
            com.shapeville.gui.UIManager.getInstance().setFullFeaturesEnabled(fullFeaturesEnabled);
            
            if (fullFeaturesEnabled) {
                featureToggleButton.setText("Normal Mode");
                featureToggleButton.setBackground(new Color(0, 128, 128));
                JOptionPane.showMessageDialog(
                    this,
                    "Switched to Full Features Mode: All tasks are unlocked",
                    "Mode Switch",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                featureToggleButton.setText("Full Features");
                featureToggleButton.setBackground(new Color(128, 0, 128));
                JOptionPane.showMessageDialog(
                    this,
                    "Switched to Normal Mode: Advanced tasks require 70 points to unlock",
                    "Mode Switch",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        homeButton.addActionListener(e -> com.shapeville.gui.UIManager.getInstance().showMainWindow());
        endButton.addActionListener(e -> handleEndSession());
        
        controlPanel.add(homeButton);
        controlPanel.add(featureToggleButton);
        controlPanel.add(endButton);
    }
    
    /**
     * Creates a styled button with the specified text and background color.
     *
     * @param text The button text
     * @param color The background color
     * @return The styled JButton
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
     * Adds a task button to the button panel with specified properties.
     *
     * @param text The button text
     * @param tooltip The tooltip text
     * @param difficulty The task difficulty level ("basic" or "advanced")
     */
    private void addTaskButton(String text, String tooltip, String difficulty) {
        JButton button = new JButton(TASK_DISPLAY_NAMES.get(text));
        
        if ("advanced".equals(difficulty)) {
            String advancedTooltip = String.format("<html>%s<br><br>" +
                "<font color='red'>Advanced Task - Currently Locked</font><br>" +
                "Unlock Condition: Complete all basic tasks with at least 70 points<br>" +
                "Basic Tasks: Shape Recognition, Angle Recognition, Area Calculation, Circle Calculation</html>", 
                tooltip);
            button.setToolTipText(advancedTooltip);
            button.setBackground(new Color(255, 204, 153));
            button.setForeground(new Color(153, 51, 0));
        } else {
            button.setToolTipText("<html>" + tooltip + "</html>");
            button.setBackground(new Color(204, 229, 255));
            button.setForeground(new Color(0, 51, 153));
        }
        
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(button.getBackground().darker(), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        button.addActionListener(e -> {
            if ("advanced".equals(difficulty) && !canAccessAdvancedTasks()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please complete all basic tasks to unlock advanced tasks!",
                    "Notice",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            com.shapeville.gui.UIManager.getInstance().switchToTask(text);
        });
        
        buttonPanel.add(button);
        taskButtons.put(text, button);
    }
    
    /**
     * Initializes the completion status for all tasks.
     */
    private void initializeTaskStatus() {
        taskCompletionStatus.put("形状识别", false);
        taskCompletionStatus.put("角度识别", false);
        taskCompletionStatus.put("面积计算", false);
        taskCompletionStatus.put("圆形计算", false);
        taskCompletionStatus.put("复合形状", false);
        taskCompletionStatus.put("扇形计算", false);
        updateProgress();
    }
    
    /**
     * Checks if advanced tasks can be accessed based on basic task completion.
     *
     * @return true if advanced tasks can be accessed, false otherwise
     */
    private boolean canAccessAdvancedTasks() {
        if (fullFeaturesEnabled) {
            return true;
        }
        return taskCompletionStatus.get("形状识别") &&
               taskCompletionStatus.get("角度识别") &&
               taskCompletionStatus.get("面积计算") &&
               taskCompletionStatus.get("圆形计算");
    }
    
    /**
     * Marks a task as completed and updates the UI accordingly.
     *
     * @param taskName The name of the completed task
     */
    public void setTaskCompleted(String taskName) {
        taskCompletionStatus.put(taskName, true);
        JButton button = taskButtons.get(taskName);
        if (button != null) {
            button.setBackground(new Color(204, 255, 204));
        }
        updateProgress();
        updateLevel();
    }
    
    /**
     * Updates the progress display based on task completion and scores.
     */
    public void updateProgress() {
        Map<String, TaskStatus> taskStatusMap = com.shapeville.gui.UIManager.getInstance().getTaskStatusMap();
        Map<String, Integer> taskScores = com.shapeville.gui.UIManager.getInstance().getTaskScores();
        
        int totalScore = taskScores.values().stream().mapToInt(Integer::intValue).sum();
        int completedTasks = (int) taskScores.values().stream().filter(score -> score > 0).count();
        int totalTasks = com.shapeville.gui.UIManager.getInstance().getTotalTaskCount();
        int progress = totalTasks > 0 ? (completedTasks * 100) / totalTasks : 0;
        
        progressBar.setValue(progress);
        progressBar.setString(String.format("Total Score: %d points (Completed: %d/%d)", 
            totalScore, completedTasks, totalTasks));
            
        // Update task button status
        taskStatusMap.forEach((taskName, status) -> {
            JButton button = taskButtons.get(taskName);
            if (button != null) {
                updateButtonStatus(button, status);
            }
        });
    }
    
    /**
     * Updates the user's level based on completed tasks.
     */
    private void updateLevel() {
        int completedTasks = 0;
        for (Boolean completed : taskCompletionStatus.values()) {
            if (completed) completedTasks++;
        }
        
        if (completedTasks >= taskCompletionStatus.size()) {
            levelLabel.setText("Current Level: Expert");
        } else if (completedTasks >= 4) {
            levelLabel.setText("Current Level: Advanced");
        } else if (completedTasks >= 2) {
            levelLabel.setText("Current Level: Intermediate");
        } else {
            levelLabel.setText("Current Level: Beginner");
        }
    }
    
    /**
     * Updates the status of all task buttons based on their current state.
     *
     * @param taskStatusMap Map containing the current status of all tasks
     */
    public void updateTaskStatus(Map<String, TaskStatus> taskStatusMap) {
        taskStatusMap.forEach((taskName, status) -> {
            JButton button = taskButtons.get(taskName);
            if (button != null) {
                SwingUtilities.invokeLater(() -> {
                    updateButtonStatus(button, status);
                });
            }
        });
        updateProgress();
    }
    
    /**
     * Updates the visual status of a task button.
     *
     * @param button The button to update
     * @param status The new status of the task
     */
    private void updateButtonStatus(JButton button, TaskStatus status) {
        String currentTooltip = button.getToolTipText();
        
        switch (status) {
            case LOCKED:
                button.setEnabled(false);
                button.setBackground(Color.GRAY);
                break;
            
            case UNLOCKED:
                button.setEnabled(true);
                button.setBackground(new Color(51, 153, 255));
                if (currentTooltip != null && currentTooltip.contains("Currently Locked")) {
                    button.setToolTipText(currentTooltip.replace("<font color='red'>Advanced Task - Currently Locked</font><br>", ""));
                }
                break;
            
            case IN_PROGRESS:
                button.setEnabled(true);
                button.setBackground(new Color(255, 165, 0));
                break;
            
            case COMPLETED:
                button.setEnabled(true);
                button.setBackground(new Color(34, 139, 34));
                break;
        }
    }
    
    /**
     * Gets the English display name for a task.
     *
     * @param taskName The internal task name
     * @return The English display name
     */
    private String getDisplayName(String taskName) {
        return TASK_DISPLAY_NAMES.getOrDefault(taskName, taskName);
    }
    
    /**
     * Updates the displayed user level.
     *
     * @param levelTitle The new level title to display
     */
    public void updateUserLevel(String levelTitle) {
        if (levelLabel != null) {
            levelLabel.setText("Current Level: " + levelTitle);
        }
    }
    
    /**
     * Updates the progress bar value and text.
     *
     * @param progress The new progress value (0-100)
     */
    public void updateProgress(int progress) {
        if (progressBar != null) {
            progressBar.setValue(progress);
            progressBar.setString(progress + "%");
        }
    }
    
    /**
     * Handles the end session action, showing a summary dialog and exit confirmation.
     */
    private void handleEndSession() {
        String progressText = progressBar.getString();
        int score = 0;
        
        try {
            if (progressText != null && progressText.contains("Total Score:")) {
                String[] parts = progressText.split("points")[0].split(":");
                if (parts.length > 1) {
                    score = Integer.parseInt(parts[1].trim());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error parsing score: " + ex.getMessage());
        }
        
        String encouragement;
        if (score >= 90) {
            encouragement = "Excellent! You're a geometry genius!";
        } else if (score >= 70) {
            encouragement = "Well done! Keep up the great work!";
        } else if (score >= 50) {
            encouragement = "Good job! You'll do even better next time!";
        } else {
            encouragement = "Thanks for participating! Every practice helps you improve!";
        }
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            String.format("In this session, you earned %d points!\n%s\n\nAre you sure you want to end the session?", score, encouragement),
            "Session Summary",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
