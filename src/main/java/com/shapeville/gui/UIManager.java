package com.shapeville.gui;

import javax.swing.*;
import java.util.*;

/**
 * The UIManager class is responsible for managing the user interface, task states,
 * and user progress in the Shapeville application. It follows the Singleton pattern
 * to ensure only one instance exists throughout the application lifecycle.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class UIManager {
    private static UIManager instance;
    private MainWindow mainWindow;
    private JFrame currentWindow;
    private TaskWindow currentTaskWindow;
    
    /** Task state management */
    private Map<String, TaskStatus> taskStatusMap;
    private Map<String, Integer> taskScores;
    private Set<String> unlockedTasks;
    private int userLevel;
    private boolean fullFeaturesEnabled = false;  // Flag for full feature mode
    
    /** Basic tasks that are available from the start */
    private static final String[] BASIC_TASKS = {
        "形状识别", "角度识别", "面积计算", "圆形计算"
    };
    
    /** Advanced tasks that need to be unlocked */
    private static final String[] ADVANCED_TASKS = {
        "复合形状", "扇形计算"
    };
    
    private boolean initialized = false;
    private int sessionScore = 0;
    private String currentTask = null;
    
    /**
     * Private constructor to prevent direct instantiation.
     * Initializes all data structures and sets initial user level.
     */
    private UIManager() {
        taskStatusMap = new HashMap<>();
        taskScores = new HashMap<>();
        unlockedTasks = new HashSet<>();
        userLevel = 1;
        initializeTaskStatus();
    }
    
    /**
     * Initializes the status of all tasks.
     * Basic tasks are unlocked by default, while advanced tasks are locked.
     */
    private void initializeTaskStatus() {
        // Initialize basic tasks
        for (String task : BASIC_TASKS) {
            taskStatusMap.put(task, TaskStatus.UNLOCKED);
            unlockedTasks.add(task);
            taskScores.put(task, 0);
        }
        
        // Initialize advanced tasks (locked by default)
        for (String task : ADVANCED_TASKS) {
            taskStatusMap.put(task, TaskStatus.LOCKED);
            taskScores.put(task, 0);
        }
    }
    
    /**
     * Gets the singleton instance of UIManager.
     *
     * @return The singleton instance of UIManager
     */
    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }
    
    /**
     * Initializes the UI components. This method should be called only once.
     */
    public void initialize() {
        if (initialized) {
            return;  // Prevent multiple initialization
        }
        SwingUtilities.invokeLater(() -> {
            mainWindow = new MainWindow();
            currentWindow = mainWindow;
            showMainWindow();
            initialized = true;
        });
    }
    
    /**
     * Shows the main window and resets session data.
     */
    public void showMainWindow() {
        if (currentWindow != null && currentWindow != mainWindow) {
            currentWindow.dispose();
        }
        // Reset session score and current task when returning to main window
        sessionScore = 0;
        currentTask = null;
        mainWindow.setVisible(true);
        currentWindow = mainWindow;
        updateMainWindowStatus();
    }
    
    /**
     * Updates the main window's display status.
     */
    private void updateMainWindowStatus() {
        if (mainWindow != null) {
            mainWindow.updateTaskStatus(taskStatusMap);
            mainWindow.updateUserLevel(getUserLevelTitle());
            mainWindow.updateProgress(calculateOverallProgress());
        }
    }
    
    /**
     * Switches to a specific task if it's unlocked.
     *
     * @param taskName The name of the task to switch to
     */
    public void switchToTask(String taskName) {
        if (!isTaskUnlocked(taskName)) {
            return;
        }
        
        // Reset session score when switching tasks
        sessionScore = 0;
        currentTask = taskName;
        
        if (currentTaskWindow != null) {
            currentTaskWindow.dispose();
        }
        
        currentTaskWindow = new TaskWindow(taskName);
        String taskDescription = getTaskDescription(taskName);
        currentTaskWindow.setTaskDescription(taskDescription);
        currentTaskWindow.setVisible(true);
        currentWindow = currentTaskWindow;
        
        // Update task status to in progress
        taskStatusMap.put(taskName, TaskStatus.IN_PROGRESS);
        updateMainWindowStatus();
    }
    
    /**
     * Shows the result of a completed task and updates relevant statistics.
     *
     * @param taskName The name of the completed task
     * @param score The score achieved in the task
     * @param maxScore The maximum possible score for the task
     * @param feedback Feedback message for the user
     */
    public void showResult(String taskName, int score, int maxScore, String feedback) {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        
        System.out.println("Updating task status: " + taskName + ", score: " + score);
        
        // Update task status and score
        taskStatusMap.put(taskName, TaskStatus.COMPLETED);
        taskScores.put(taskName, Math.max(taskScores.getOrDefault(taskName, 0), score));
        
        // Update session score
        sessionScore = score;
        
        // Check if advanced tasks can be unlocked
        checkAndUnlockTasks();
        
        // Update user level
        updateUserLevel();
        
        // Update main window status
        if (mainWindow != null) {
            SwingUtilities.invokeLater(() -> {
                updateMainWindowStatus();
                System.out.println("Main window status updated");
            });
        }
        
        // Show result window
        ResultWindow resultWindow = new ResultWindow(taskName, score, maxScore, feedback);
        resultWindow.setVisible(true);
        currentWindow = resultWindow;
        
        System.out.println("Result window displayed, task status: " + taskStatusMap.get(taskName));
    }
    
    /**
     * Checks and updates the unlock status of advanced tasks.
     * In full feature mode, all tasks are unlocked.
     * In normal mode, advanced tasks are unlocked when all basic tasks score ≥ 70.
     */
    private void checkAndUnlockTasks() {
        // In full feature mode, all tasks are unlocked
        if (fullFeaturesEnabled) {
            for (String task : ADVANCED_TASKS) {
                taskStatusMap.put(task, TaskStatus.UNLOCKED);
                unlockedTasks.add(task);
            }
            return;
        }
        
        // Normal mode unlock logic
        boolean canUnlockAdvanced = true;
        
        // Check basic tasks completion and scores
        for (String task : BASIC_TASKS) {
            int score = taskScores.getOrDefault(task, 0);
            if (score < 70) {  // Each basic task needs at least 70 points
                canUnlockAdvanced = false;
                break;
            }
        }
        
        // Update advanced tasks status
        for (String task : ADVANCED_TASKS) {
            TaskStatus newStatus = canUnlockAdvanced ? TaskStatus.UNLOCKED : TaskStatus.LOCKED;
            taskStatusMap.put(task, newStatus);
            if (canUnlockAdvanced) {
                unlockedTasks.add(task);
            } else {
                unlockedTasks.remove(task); // Update unlock status when full mode is disabled
            }
        }
    }
    
    /**
     * Updates the user's level based on total score and completed tasks.
     */
    private void updateUserLevel() {
        int totalScore = calculateTotalScore();
        int completedTasks = countCompletedTasks();
        
        if (totalScore >= 540 && completedTasks >= 6) { // 90 points * 6 tasks
            userLevel = 4; // Expert
        } else if (totalScore >= 420 && completedTasks >= 5) { // 70 points * 6 tasks
            userLevel = 3; // Advanced
        } else if (totalScore >= 280 && completedTasks >= 4) { // 70 points * 4 tasks
            userLevel = 2; // Intermediate
        } else {
            userLevel = 1; // Beginner
        }
    }
    
    /**
     * Gets the title corresponding to the current user level.
     *
     * @return The user level title
     */
    private String getUserLevelTitle() {
        switch (userLevel) {
            case 4: return "Expert";
            case 3: return "Advanced Learner";
            case 2: return "Intermediate Learner";
            default: return "Beginner";
        }
    }
    
    /**
     * Checks if a task is unlocked.
     *
     * @param taskName The name of the task to check
     * @return true if the task is unlocked, false otherwise
     */
    public boolean isTaskUnlocked(String taskName) {
        if (fullFeaturesEnabled) {
            return true;
        }
        return unlockedTasks.contains(taskName);
    }
    
    /**
     * Gets the description for a specific task.
     *
     * @param taskName The name of the task
     * @return The task description
     */
    private String getTaskDescription(String taskName) {
        switch (taskName) {
            case "形状识别":
                return "Identify the 2D and 3D shapes shown below.\n" +
                       "Observe the features of the shapes and select the correct shape name.";
            case "角度识别":
                return "Observe the angle shown and determine if it's acute, right, obtuse, straight, or reflex.\n" +
                       "Remember:\n" +
                       "- Acute angle: less than 90 degrees\n" +
                       "- Right angle: exactly 90 degrees\n" +
                       "- Obtuse angle: greater than 90 degrees but less than 180 degrees\n" +
                       "- Straight angle: exactly 180 degrees\n" +
                       "- Reflex angle: greater than 180 degrees but less than 360 degrees\n\n" +
                       "Completion requirement: Successfully identify any 4 different types of angles.\n" +
                       "Each angle type has 3 attempts, scoring rules:\n" +
                       "- 1st correct attempt: 3 points\n" +
                       "- 2nd correct attempt: 2 points\n" +
                       "- 3rd correct attempt: 1 point";
            case "面积计算":
                return "In this task, you'll calculate the areas of four basic shapes: rectangle, parallelogram, triangle, and trapezoid.\n\n" +
                       "Task details:\n" +
                       "- System generates random input values between 1 and 20 (length, width, height, etc.)\n" +
                       "- You have 3 minutes to complete the calculations\n" +
                       "- Each shape has 3 attempts\n" +
                       "- Scoring rules:\n" +
                       "  · 1st correct attempt: 3 points\n" +
                       "  · 2nd correct attempt: 2 points\n" +
                       "  · 3rd correct attempt: 1 point\n\n" +
                       "Note:\n" +
                       "- Answers should be rounded to 1 decimal place\n" +
                       "- After 3 wrong attempts, the system will show the correct answer and calculation process\n" +
                       "- You can click \"Return to Home\" button to end the task at any time";
            case "圆形计算":
                return "In this task, you'll perform circle calculations using four different methods:\n\n" +
                       "1. Area from radius (A = πr²)\n" +
                       "2. Area from diameter (A = π(d/2)²)\n" +
                       "3. Circumference from radius (C = 2πr)\n" +
                       "4. Circumference from diameter (C = πd)\n\n" +
                       "Task details:\n" +
                       "- System generates random radius or diameter values between 1 and 20\n" +
                       "- You have 3 minutes to complete all four calculation methods\n" +
                       "- Each question has 3 attempts\n" +
                       "- Scoring rules:\n" +
                       "  · 1st correct attempt: 3 points\n" +
                       "  · 2nd correct attempt: 2 points\n" +
                       "  · 3rd correct attempt: 1 point\n" +
                       "- Zero points if all 3 attempts are wrong\n" +
                       "- Must complete all four calculation methods to finish the task\n\n" +
                       "Note:\n" +
                       "- Answers should be rounded to 1 decimal place\n" +
                       "- Use π = 3.14159\n" +
                       "- After 3 wrong attempts, the system will show the correct answer and calculation process";
            case "复合形状":
                return "In this task, you'll calculate the areas of shapes composed of multiple basic shapes.\n\n" +
                       "Task details:\n" +
                       "1. System provides 9 different compound shapes for practice\n" +
                       "2. Each shape includes detailed dimension information\n" +
                       "3. You need to:\n" +
                       "   - Analyze the component shapes\n" +
                       "   - Apply basic area formulas\n" +
                       "   - Calculate the total area\n\n" +
                       "Rules:\n" +
                       "- Each shape has 3 attempts\n" +
                       "- Scoring rules:\n" +
                       "  · 1st correct attempt: 3 points\n" +
                       "  · 2nd correct attempt: 2 points\n" +
                       "  · 3rd correct attempt: 1 point\n" +
                       "- Answers should be rounded to 1 decimal place\n" +
                       "- Detailed solution shown after 3 wrong attempts\n" +
                       "- Task ends after completing all 9 shapes or clicking return to home";
            case "扇形计算":
                return "Calculate the area and arc length of sectors.\n" +
                       "Remember: Sector area = (πr²×angle)/360°, Arc length = (2πr×angle)/360°";
            default:
                return "Loading task description...";
        }
    }
    
    /**
     * Calculates the overall progress percentage.
     *
     * @return Progress percentage (0-100)
     */
    private int calculateOverallProgress() {
        int totalTasks = BASIC_TASKS.length + ADVANCED_TASKS.length;
        int completedTasks = countCompletedTasks();
        int totalPossibleScore = totalTasks * 100;
        int currentTotalScore = calculateTotalScore();
        
        // Progress calculation considers both completed tasks and total score
        return (int) ((completedTasks * 50.0 / totalTasks) + (currentTotalScore * 50.0 / totalPossibleScore));
    }
    
    /**
     * Calculates the total score across all tasks.
     *
     * @return The total score
     */
    private int calculateTotalScore() {
        return taskScores.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Counts the number of completed tasks.
     *
     * @return The number of completed tasks
     */
    private int countCompletedTasks() {
        return (int) taskStatusMap.values().stream()
            .filter(status -> status == TaskStatus.COMPLETED)
            .count();
    }
    
    /**
     * Gets the current session score.
     *
     * @return The current session score
     */
    public int getSessionScore() {
        return sessionScore;
    }
    
    /**
     * Gets the name of the current task.
     *
     * @return The current task name
     */
    public String getCurrentTask() {
        return currentTask;
    }
    
    /**
     * Adds points to the current session score and updates the task's high score if necessary.
     *
     * @param points The points to add
     */
    public void addToSessionScore(int points) {
        this.sessionScore += points;
        updateMainWindowStatus();
        
        // Update total score record
        if (currentTask != null) {
            taskScores.put(currentTask, Math.max(taskScores.getOrDefault(currentTask, 0), sessionScore));
        }
    }
    
    /**
     * Gets a copy of the task scores map.
     *
     * @return A copy of the task scores map
     */
    public Map<String, Integer> getTaskScores() {
        return new HashMap<>(taskScores);
    }
    
    /**
     * Gets a copy of the task status map.
     *
     * @return A copy of the task status map
     */
    public Map<String, TaskStatus> getTaskStatusMap() {
        return new HashMap<>(taskStatusMap);
    }
    
    /**
     * Enables or disables the full features mode.
     *
     * @param enabled true to enable full features mode, false to disable
     */
    public void setFullFeaturesEnabled(boolean enabled) {
        this.fullFeaturesEnabled = enabled;
        if (enabled) {
            // Unlock all tasks in full feature mode
            for (String task : ADVANCED_TASKS) {
                taskStatusMap.put(task, TaskStatus.UNLOCKED);
                unlockedTasks.add(task);
            }
        } else {
            // Recheck task unlock status when returning to normal mode
            checkAndUnlockTasks();
        }
        // Update main window status
        updateMainWindowStatus();
    }
    
    /**
     * Enum representing the possible states of a task.
     */
    public enum TaskStatus {
        /** Task is locked and cannot be accessed */
        LOCKED,
        /** Task is unlocked and ready to start */
        UNLOCKED,
        /** Task is currently being attempted */
        IN_PROGRESS,
        /** Task has been completed */
        COMPLETED
    }
}
