package com.shapeville.gui.tasks;

import com.shapeville.gui.TaskWindow;
import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for task panels in the Shapeville application.
 * This class provides common functionality for all task panels, including:
 * - Attempt tracking and scoring
 * - Task lifecycle management
 * - UI feedback handling
 * - Parent window communication
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public abstract class BaseTaskPanel extends JPanel implements TaskPanelInterface {
    protected Timer timer;
    protected int attempts;
    protected String taskName;
    protected java.util.List<Integer> attemptsPerTask;
    protected TaskWindow parentWindow;
    
    /**
     * Constructs a new BaseTaskPanel with the specified task name.
     * Initializes attempt tracking and sets up the basic UI layout.
     *
     * @param taskName The name of the task
     */
    public BaseTaskPanel(String taskName) {
        this.taskName = taskName;
        this.attempts = 0;
        this.attemptsPerTask = new java.util.ArrayList<>();
        setLayout(new BorderLayout());
        initializeUI();
    }
    
    /**
     * Sets the parent window for this task panel.
     * The parent window is used for feedback and result display.
     *
     * @param window The parent TaskWindow instance
     */
    public void setParentWindow(TaskWindow window) {
        this.parentWindow = window;
    }
    
    // Abstract methods that subclasses must implement
    /**
     * Initializes the user interface components.
     * Must be implemented by subclasses to set up their specific UI elements.
     */
    public abstract void initializeUI();

    /**
     * Handles the submission of answers or solutions.
     * Must be implemented by subclasses to process user input.
     */
    public abstract void handleSubmit();

    /**
     * Resets the task panel to its initial state.
     * Must be implemented by subclasses to clear their specific state.
     */
    public abstract void reset();

    /**
     * Calculates the current score for the task.
     * Must be implemented by subclasses to implement their scoring logic.
     *
     * @return The calculated score
     */
    protected abstract int calculateScore();
    
    // TaskPanelInterface implementations
    @Override
    public void startTask() {
        reset();
    }
    
    @Override
    public void pauseTask() {
        // Default implementation, can be overridden by subclasses
    }
    
    @Override
    public void resumeTask() {
        // Default implementation, can be overridden by subclasses
    }
    
    @Override
    public void endTask() {
        cleanup();
        if (parentWindow != null) {
            parentWindow.showResult(getScore(), calculateMaxScore());
        }
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        int score = calculateScore();
        int maxScore = calculateMaxScore();
        return String.format("Score: %d (Total: %d points)", score, maxScore);
    }
    
    /**
     * Sets feedback message in the parent window.
     * 
     * @param message The feedback message to display
     */
    protected void setFeedback(String message) {
        if (parentWindow != null) {
            parentWindow.setFeedback(message);
        } else {
            System.out.println("Warning: Cannot set feedback, parentWindow is null");
        }
    }
    
    /**
     * Appends additional feedback to the existing message.
     * 
     * @param message The additional feedback message to append
     */
    protected void appendFeedback(String message) {
        if (parentWindow != null) {
            parentWindow.appendFeedback(message);
        } else {
            System.out.println("Warning: Cannot append feedback, parentWindow is null");
        }
    }
    
    /**
     * Increments the number of attempts for the current task.
     */
    protected void incrementAttempts() {
        attempts++;
    }
    
    /**
     * Resets the number of attempts to zero.
     */
    protected void resetAttempts() {
        attempts = 0;
    }
    
    /**
     * Adds the current number of attempts to the attempts history list.
     */
    protected void addAttemptToList() {
        attemptsPerTask.add(attempts);
    }
    
    /**
     * Gets the number of remaining attempts allowed.
     * @return The number of remaining attempts (out of 3)
     */
    protected int getRemainingAttempts() {
        return 3 - attempts;
    }
    
    /**
     * Checks if there are any remaining attempts allowed.
     * @return true if there are attempts remaining, false otherwise
     */
    protected boolean hasRemainingAttempts() {
        return attempts < 3;
    }
    
    /**
     * Gets the list of attempts made for each completed task.
     * @return The list of attempts per task
     */
    public java.util.List<Integer> getAttemptsPerTask() {
        return attemptsPerTask;
    }
    
    /**
     * Performs cleanup operations when the task ends.
     * Default implementation that can be overridden by subclasses.
     */
    protected void cleanup() {
        // Default implementation, can be overridden by subclasses
    }
    
    /**
     * Calculates the maximum possible score for the task.
     * @return The maximum score (3 points per task)
     */
    protected int calculateMaxScore() {
        return attemptsPerTask.size() * 3;  // 3 points maximum per task
    }
} 