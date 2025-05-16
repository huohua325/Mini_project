package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import com.shapeville.gui.tasks.*;
import com.shapeville.gui.tasks.TaskPanelInterface;

/**
 * The TaskWindow class represents the main window for different geometric tasks in Shapeville.
 * It provides a graphical user interface for various geometric calculations and exercises.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class TaskWindow extends JFrame {
    private String taskName;
    private JTextArea taskDescription;
    private JPanel inputPanel;
    private JTextArea feedbackArea;
    private TaskPanelInterface currentTask;
    
    /**
     * Constructs a new TaskWindow with the specified task name.
     *
     * @param taskName The name of the task to be displayed
     */
    public TaskWindow(String taskName) {
        this.taskName = taskName;
        initializeUI();
        setupTask();
    }
    
    /**
     * Initializes the user interface components of the window.
     * Sets up the main panel with task description, input area, and feedback sections.
     */
    private void initializeUI() {
        setTitle("Shapeville - " + taskName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create task description area
        taskDescription = new JTextArea();
        taskDescription.setEditable(false);
        taskDescription.setWrapStyleWord(true);
        taskDescription.setLineWrap(true);
        taskDescription.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskDescription);
        scrollPane.setPreferredSize(new Dimension(750, 100));
        mainPanel.add(scrollPane, BorderLayout.NORTH);
        
        // Create input panel
        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Create feedback area
        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setLineWrap(true);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(750, 100));
        
        // Create control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(feedbackScroll, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Sets up the specific task panel based on the task name.
     * Creates and initializes the appropriate task panel for the selected task.
     */
    private void setupTask() {
        if (taskName.equals("形状识别")) {  
            currentTask = new ShapePanel();
            inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
            if (currentTask instanceof BaseTaskPanel) {
                ((BaseTaskPanel) currentTask).setParentWindow(this);
            }
        } else if (taskName.equals("角度识别")) { 
            currentTask = new AngleCalculationPanel();
            inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
            if (currentTask instanceof BaseTaskPanel) {
                ((BaseTaskPanel) currentTask).setParentWindow(this);
            }
        } else if (taskName.equals("面积计算")) {  
            currentTask = new AreaCalculationPanel();
            inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
            if (currentTask instanceof BaseTaskPanel) {
                ((BaseTaskPanel) currentTask).setParentWindow(this);
            }
        } else if (taskName.equals("圆形计算")) {
            currentTask = new CircleCalculationPanel();
            inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
            if (currentTask instanceof BaseTaskPanel) {
                ((BaseTaskPanel) currentTask).setParentWindow(this);
            }
        } else if (taskName.equals("复合形状")) {
            currentTask = new CompoundShapeCalculationPanel();
            inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
            if (currentTask instanceof BaseTaskPanel) {
                ((BaseTaskPanel) currentTask).setParentWindow(this);
            }
        } else if (taskName.equals("扇形计算")) {
            currentTask = new SectorCalculationPanel();
            inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
            if (currentTask instanceof BaseTaskPanel) {
                ((BaseTaskPanel) currentTask).setParentWindow(this);
            }
        }
    }
    
    /**
     * Sets the task description text in the description area.
     *
     * @param description The description text to be displayed
     */
    public void setTaskDescription(String description) {
        taskDescription.setText(description);
    }
    
    /**
     * Sets the feedback text in the feedback area.
     *
     * @param feedback The feedback text to be displayed
     */
    public void setFeedback(String feedback) {
        feedbackArea.setText(feedback);
    }
    
    /**
     * Appends additional feedback text to the existing feedback.
     *
     * @param feedback The additional feedback text to append
     */
    public void appendFeedback(String feedback) {
        feedbackArea.append(feedback + "\n");
    }
    
    /**
     * Displays the task result with score and generates appropriate feedback.
     *
     * @param score The score achieved in the task
     * @param maxScore The maximum possible score for the task
     */
    public void showResult(int score, int maxScore) {
        System.out.println("TaskWindow showing result - Task: " + taskName + ", Score: " + score + "/" + maxScore);
        String feedback = generateFeedback(score, maxScore);
        UIManager.getInstance().showResult(taskName, score, maxScore, feedback);
    }
    
    /**
     * Generates feedback text based on the achieved score.
     *
     * @param score The score achieved in the task
     * @param maxScore The maximum possible score for the task
     * @return The generated feedback text
     */
    private String generateFeedback(int score, int maxScore) {
        double percentage = (double) score / maxScore * 100;
        String stars = "★".repeat((int) (percentage / 20));
        String rating;
        
        if (percentage >= 90) {
            rating = "Excellent";
        } else if (percentage >= 80) {
            rating = "Good";
        } else if (percentage >= 60) {
            rating = "Pass";
        } else {
            rating = "Need more practice";
        }
        
        return String.format("Score: %d/%d\nRating: %s\n%s", score, maxScore, rating, stars);
    }
    
    /**
     * Cleans up resources when the task window is closed.
     */
    public void cleanup() {
        if (currentTask != null) {
            currentTask.pauseTask();
        }
    }
    
    /**
     * Overrides the dispose method to ensure proper cleanup before closing.
     */
    @Override
    public void dispose() {
        cleanup();
        super.dispose();
    }
}
