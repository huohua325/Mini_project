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
    private boolean fullFeaturesEnabled = false;  // 添加完整功能模式标志
    
    private static final String[] BASIC_TASKS = {
        "形状识别", "角度识别", "面积计算", "圆形计算"
    };
    
    private static final String[] ADVANCED_TASKS = {
        "复合形状", "扇形计算"
    };
    
    private boolean initialized = false;
    
    private int sessionScore = 0;
    private String currentTask = null;
    
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
        // 返回主窗口时重置会话分数和当前任务
        sessionScore = 0;
        currentTask = null;
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
        
        // 切换任务时重置会话分数
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
        
        // 更新任务状态为进行中
        taskStatusMap.put(taskName, TaskStatus.IN_PROGRESS);
        updateMainWindowStatus();
    }
    
    public void showResult(String taskName, int score, int maxScore, String feedback) {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        
        System.out.println("正在更新任务状态: " + taskName + ", 分数: " + score);
        
        // 更新任务状态和分数
        taskStatusMap.put(taskName, TaskStatus.COMPLETED);
        taskScores.put(taskName, Math.max(taskScores.getOrDefault(taskName, 0), score));
        
        // 更新会话分数
        sessionScore = score;
        
        // 检查是否可以解锁高级任务
        checkAndUnlockTasks();
        
        // 更新用户等级
        updateUserLevel();
        
        // 更新主窗口状态
        if (mainWindow != null) {
            SwingUtilities.invokeLater(() -> {
                updateMainWindowStatus();
                System.out.println("主窗口状态已更新");
            });
        }
        
        // 显示结果窗口
        ResultWindow resultWindow = new ResultWindow(taskName, score, maxScore, feedback);
        resultWindow.setVisible(true);
        currentWindow = resultWindow;
        
        System.out.println("结果窗口已显示，任务状态：" + taskStatusMap.get(taskName));
    }
    
    private void checkAndUnlockTasks() {
        // 在完整功能模式下，所有任务都解锁
        if (fullFeaturesEnabled) {
            for (String task : ADVANCED_TASKS) {
                taskStatusMap.put(task, TaskStatus.UNLOCKED);
                unlockedTasks.add(task);
            }
            return;
        }
        
        // 正常模式下的解锁逻辑
        boolean canUnlockAdvanced = true;
        int totalBasicScore = 0;
        
        // 检查基础任务的完成情况和总分
        for (String task : BASIC_TASKS) {
            int score = taskScores.getOrDefault(task, 0);
            totalBasicScore += score;
            if (score < 70) {  // 每个基础任务至少需要70分
                canUnlockAdvanced = false;
                break;
            }
        }
        
        // 更新高级任务的状态
        for (String task : ADVANCED_TASKS) {
        if (canUnlockAdvanced) {
                if (taskStatusMap.get(task) == TaskStatus.LOCKED) {
                    taskStatusMap.put(task, TaskStatus.UNLOCKED);
                    unlockedTasks.add(task);
                }
            } else {
                if (!unlockedTasks.contains(task)) {
                    taskStatusMap.put(task, TaskStatus.LOCKED);
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
    
    public boolean isTaskUnlocked(String taskName) {
        // 在完整功能模式下，所有任务都可访问
        if (fullFeaturesEnabled) {
            return true;
        }
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
                return "请观察下面的角度，判断它是锐角、直角、钝角、平角还是优角。\n" +
                       "记住：\n" +
                       "- 锐角：小于90度\n" +
                       "- 直角：等于90度\n" +
                       "- 钝角：大于90度且小于180度\n" +
                       "- 平角：等于180度\n" +
                       "- 优角：大于180度且小于360度\n\n" +
                       "通关条件：成功识别出任意4种不同类型的角度即可完成任务。\n" +
                       "每种角度有3次答题机会，答对得分规则：\n" +
                       "- 第1次答对：3分\n" +
                       "- 第2次答对：2分\n" +
                       "- 第3次答对：1分";
            case "面积计算":
                return "在本任务中，你需要计算四种基本形状的面积：矩形、平行四边形、三角形和梯形。\n\n" +
                       "任务说明：\n" +
                       "- 系统会随机生成1到20之间的输入值（如长度、宽度、高度等）\n" +
                       "- 你有3分钟的时间来完成计算\n" +
                       "- 每个形状有3次答题机会\n" +
                       "- 答对得分规则：\n" +
                       "  · 第1次答对：3分\n" +
                       "  · 第2次答对：2分\n" +
                       "  · 第3次答对：1分\n\n" +
                       "注意：\n" +
                       "- 答案需要保留1位小数\n" +
                       "- 答错3次后，系统会显示正确答案和计算过程\n" +
                       "- 你可以随时点击\"返回主页\"按钮结束任务";
            case "圆形计算":
                return "在本任务中，你需要完成圆形的周长和面积计算，包括以下四种计算方式：\n\n" +
                       "1. 已知半径求面积 (A = πr²)\n" +
                       "2. 已知直径求面积 (A = π(d/2)²)\n" +
                       "3. 已知半径求周长 (C = 2πr)\n" +
                       "4. 已知直径求周长 (C = πd)\n\n" +
                       "任务说明：\n" +
                       "- 系统会随机生成1到20之间的半径或直径值\n" +
                       "- 你有3分钟的时间完成所有四种计算方式\n" +
                       "- 每道题有3次答题机会\n" +
                       "- 答对得分规则：\n" +
                       "  · 第1次答对：3分\n" +
                       "  · 第2次答对：2分\n" +
                       "  · 第3次答对：1分\n" +
                       "- 如果某道题3次都答错，该题得0分\n" +
                       "- 需要完成所有四种计算方式才能结束任务\n\n" +
                       "注意：\n" +
                       "- 答案需要保留1位小数\n" +
                       "- π取3.14159\n" +
                       "- 答错3次后，系统会显示正确答案和计算过程";
            case "复合形状":
                return "在本任务中，你需要计算由多个基本形状组成的复合形状的面积。\n\n" +
                       "任务说明：\n" +
                       "1. 系统会提供9个不同的复合形状供你练习\n" +
                       "2. 每个形状都标注了详细的尺寸信息\n" +
                       "3. 你需要：\n" +
                       "   - 分析形状的组成部分\n" +
                       "   - 运用基本形状的面积公式\n" +
                       "   - 计算最终的总面积\n\n" +
                       "规则说明：\n" +
                       "- 每个形状有3次答题机会\n" +
                       "- 答对得分规则：\n" +
                       "  · 第1次答对：3分\n" +
                       "  · 第2次答对：2分\n" +
                       "  · 第3次答对：1分\n" +
                       "- 答案需要保留1位小数\n" +
                       "- 3次答错后会显示详细解答\n" +
                       "- 完成所有9个形状或点击返回主页可结束任务";
            case "扇形计算":
                return "计算扇形的面积和弧长。\n" +
                       "记住：扇形面积 = (πr²×角度)/360°，弧长 = (2πr×角度)/360°";
            default:
                return "任务说明加载中...";
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
    
    public int getSessionScore() {
        return sessionScore;
    }
    
    public String getCurrentTask() {
        return currentTask;
    }
    
    public void addToSessionScore(int points) {
        this.sessionScore += points;
        updateMainWindowStatus();
        
        // 同时更新总分记录
        if (currentTask != null) {
            taskScores.put(currentTask, Math.max(taskScores.getOrDefault(currentTask, 0), sessionScore));
        }
    }
    
    public Map<String, Integer> getTaskScores() {
        return new HashMap<>(taskScores);
    }
    
    public Map<String, TaskStatus> getTaskStatusMap() {
        return new HashMap<>(taskStatusMap);
    }
    
    public void setFullFeaturesEnabled(boolean enabled) {
        this.fullFeaturesEnabled = enabled;
        if (enabled) {
            // 在完整功能模式下，解锁所有任务
            for (String task : ADVANCED_TASKS) {
                taskStatusMap.put(task, TaskStatus.UNLOCKED);
                unlockedTasks.add(task);
            }
        } else {
            // 切换回正常模式，重新检查任务解锁状态
            checkAndUnlockTasks();
        }
        // 更新主窗口状态
        updateMainWindowStatus();
    }
    
    // 任务状态枚举
    public enum TaskStatus {
        LOCKED,        // 未解锁
        UNLOCKED,      // 已解锁
        IN_PROGRESS,   // 进行中
        COMPLETED      // 已完成
    }
}
