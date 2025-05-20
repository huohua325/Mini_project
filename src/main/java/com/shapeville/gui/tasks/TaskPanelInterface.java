package com.shapeville.gui.tasks;

/**
 * Interface defining the core functionality for task panels in the Shapeville application.
 * This interface provides methods for managing the lifecycle of a task panel and
 * retrieving task status information.
 *
 * Task panels implementing this interface should handle:
 * - Task lifecycle (start, pause, resume, end)
 * - Score tracking and reporting
 * - User feedback generation
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public interface TaskPanelInterface {
    /**
     * Starts the task and initializes necessary components.
     * This method should be called when the task panel is first displayed
     * or when restarting a task.
     */
    void startTask();

    /**
     * Pauses the current task.
     * This method should temporarily suspend any ongoing operations,
     * timers, or user interactions.
     */
    void pauseTask();

    /**
     * Resumes a paused task.
     * This method should restore the task state and resume any
     * suspended operations or timers.
     */
    void resumeTask();

    /**
     * Ends the current task and performs cleanup.
     * This method should handle final score calculation, resource cleanup,
     * and any necessary state updates.
     */
    void endTask();

    /**
     * Gets the current score for the task.
     * @return The current score value
     */
    int getScore();

    /**
     * Gets feedback about the task's current state or completion.
     * @return A string containing feedback about the task
     */
    String getFeedback();
} 