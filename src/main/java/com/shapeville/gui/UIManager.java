package com.shapeville.gui;

import javax.swing.*;
import java.util.*;

public class UIManager {
    private static UIManager instance;
    private MainWindow mainWindow;
    private JFrame currentWindow;
    private TaskWindow currentTaskWindow;
    
    // 任务状态管理
    private Map<String, TaskStatus> taskStatusMap;
    private Map<String, Integer> taskScores;
    private Set<String> unlockedTasks;
    private int userLevel;
    
    private static final String[] BASIC_TASKS = {
        "形状识别", "角度识别", "面积计算", "圆形计算"
    };
    
    private static final String[] ADVANCED_TASKS = {
        "复合形状", "扇形计算"
    };
    
    private boolean initialized = false;
    
    private UIManager() {
        taskStatusMap = new HashMap<>();
        taskScores = new HashMap<>();
        unlockedTasks = new HashSet<>();
        userLevel = 1;
        initializeTaskStatus();
    }
    
    private void initializeTaskStatus() {
        // 初始化基础任务
        for (String task : BASIC_TASKS) {
            taskStatusMap.put(task, TaskStatus.UNLOCKED);
            unlockedTasks.add(task);
            taskScores.put(task, 0);
        }
        
        // 初始化高级任务（默认锁定）
        for (String task : ADVANCED_TASKS) {
            taskStatusMap.put(task, TaskStatus.LOCKED);
            taskScores.put(task, 0);
        }
    }
    
    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }
    
    public void initialize() {
        if (initialized) {
            return;  // 防止重复初始化
        }
        SwingUtilities.invokeLater(() -> {
            mainWindow = new MainWindow();
            currentWindow = mainWindow;
            showMainWindow();
            updateMainWindowStatus();
            initialized = true;
        });
    }
    
    public void showMainWindow() {
        if (currentWindow != null && currentWindow != mainWindow) {
            currentWindow.dispose();
        }
        mainWindow.setVisible(true);
        currentWindow = mainWindow;
        updateMainWindowStatus();
    }
    
    private void updateMainWindowStatus() {
        if (mainWindow != null) {
            mainWindow.updateTaskStatus(taskStatusMap);
            mainWindow.updateUserLevel(getUserLevelTitle());
            mainWindow.updateProgress(calculateOverallProgress());
        }
    }
    
    public void switchToTask(String taskName) {
        if (!isTaskUnlocked(taskName)) {
            showTaskLockedMessage(taskName);
            return;
        }
        
        if (currentTaskWindow != null) {
            currentTaskWindow.dispose();
        }
        
        currentTaskWindow = new TaskWindow(taskName);
        String taskDescription = getTaskDescription(taskName);
        currentTaskWindow.setTaskDescription(taskDescription);
        currentTaskWindow.setVisible(true);
        currentWindow = currentTaskWindow;
        
        // 更新任务状态为进行中
        taskStatusMap.put(taskName, TaskStatus.IN_PROGRESS);
        updateMainWindowStatus();
    }
    
    public void showResult(String taskName, int score, String feedback) {
        // 更新任务分数和状态
        int previousScore = taskScores.getOrDefault(taskName, 0);
        if (score > previousScore) {
            taskScores.put(taskName, score);
        }
        
        // 更新任务状态
        taskStatusMap.put(taskName, score >= 60 ? TaskStatus.COMPLETED : TaskStatus.UNLOCKED);
        
        // 检查是否解锁新任务
        checkAndUnlockTasks();
        
        // 更新用户等级
        updateUserLevel();
        
        // 显示结果窗口
        SwingUtilities.invokeLater(() -> {
            ResultWindow resultWindow = new ResultWindow(taskName, score, feedback);
            resultWindow.setVisible(true);
            
            // 更新主窗口状态并显示
            showMainWindow();
            updateMainWindowStatus();
        });
    }
    
    private void checkAndUnlockTasks() {
        // 检查是否可以解锁高级任务
        boolean canUnlockAdvanced = true;
        for (String task : BASIC_TASKS) {
            if (taskScores.getOrDefault(task, 0) < 70) {
                canUnlockAdvanced = false;
                break;
            }
        }
        
        if (canUnlockAdvanced) {
            for (String task : ADVANCED_TASKS) {
                if (taskStatusMap.get(task) == TaskStatus.LOCKED) {
                    taskStatusMap.put(task, TaskStatus.UNLOCKED);
                    unlockedTasks.add(task);
                }
            }
        }
    }
    
    private void updateUserLevel() {
        int totalScore = calculateTotalScore();
        int completedTasks = countCompletedTasks();
        
        if (totalScore >= 540 && completedTasks >= 6) { // 90分 * 6个任务
            userLevel = 4; // 专家
        } else if (totalScore >= 420 && completedTasks >= 5) { // 70分 * 6个任务
            userLevel = 3; // 高级
        } else if (totalScore >= 300 && completedTasks >= 4) { // 60分 * 5个任务
            userLevel = 2; // 中级
        } else {
            userLevel = 1; // 初学者
        }
    }
    
    private String getUserLevelTitle() {
        switch (userLevel) {
            case 4: return "专家";
            case 3: return "高级学习者";
            case 2: return "中级学习者";
            default: return "初学者";
        }
    }
    
    private boolean isTaskUnlocked(String taskName) {
        return unlockedTasks.contains(taskName);
    }
    
    private void showTaskLockedMessage(String taskName) {
        JOptionPane.showMessageDialog(
            mainWindow,
            "需要完成所有基础任务并获得至少70分才能解锁此任务！",
            taskName + " - 未解锁",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private String getTaskDescription(String taskName) {
        switch (taskName) {
            case "形状识别":
                return "请识别下面显示的2D和3D形状。\n" +
                       "观察形状的特征，选择正确的形状名称。";
            case "角度识别":
                return "请观察下面的角度，判断它是锐角、直角还是钝角。\n" +
                       "记住：小于90度是锐角，等于90度是直角，大于90度是钝角。";
            case "面积计算":
                return "计算给定形状的面积。\n" +
                       "使用正确的公式，注意单位。";
            case "圆形计算":
                return "计算圆的周长和面积。\n" +
                       "记住：周长 = 2πr，面积 = πr²";
            case "复合形状":
                return "计算由多个基本形状组成的复合形状的面积。\n" +
                       "提示：可以将复合形状分解成基本形状。";
            case "扇形计算":
                return "计算扇形的面积和弧长。\n" +
                       "记住：扇形面积 = (πr²×角度)/360°，弧长 = (2πr×角度)/360°";
            default:
                return "任务说明加载中...";
        }
    }
    
    private void updateProgress(int progress) {
        if (mainWindow != null) {
            mainWindow.updateProgress(progress);
        }
    }
    
    private int calculateOverallProgress() {
        int totalTasks = BASIC_TASKS.length + ADVANCED_TASKS.length;
        int completedTasks = countCompletedTasks();
        int totalPossibleScore = totalTasks * 100;
        int currentTotalScore = calculateTotalScore();
        
        // 进度计算考虑完成的任务数和总分
        return (int) ((completedTasks * 50.0 / totalTasks) + (currentTotalScore * 50.0 / totalPossibleScore));
    }
    
    private int calculateTotalScore() {
        return taskScores.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    private int countCompletedTasks() {
        return (int) taskStatusMap.values().stream()
            .filter(status -> status == TaskStatus.COMPLETED)
            .count();
    }
    
    // 任务状态枚举
    public enum TaskStatus {
        LOCKED,        // 未解锁
        UNLOCKED,      // 已解锁
        IN_PROGRESS,   // 进行中
        COMPLETED      // 已完成
    }
}
