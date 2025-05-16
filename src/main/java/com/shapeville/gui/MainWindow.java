package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import com.shapeville.gui.UIManager.TaskStatus;

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
    
    public MainWindow() {
        taskButtons = new HashMap<>();
        taskCompletionStatus = new HashMap<>();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Shapeville");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建欢迎标签
        JLabel welcomeLabel = new JLabel("欢迎来到Shapeville几何学习乐园！", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(51, 51, 153));
        
        // 创建等级标签
        levelLabel = new JLabel("当前等级：初学者", SwingConstants.RIGHT);
        levelLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        
        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(levelLabel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 创建任务按钮面板
        buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 添加任务按钮
        addTaskButton("形状识别", "识别2D和3D形状（基础难度）", "basic");
        addTaskButton("角度识别", "判断角度类型和大小（基础难度）", "basic");
        addTaskButton("面积计算", "计算基本形状的面积（基础难度）", "basic");
        addTaskButton("圆形计算", "计算圆的周长和面积（基础难度）", "basic");
        addTaskButton("复合形状", "计算复合形状的面积（高级难度）", "advanced");
        addTaskButton("扇形计算", "计算扇形的面积和弧长（高级难度）", "advanced");
        
        // 创建中央面板（包含任务按钮和说明）
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // 创建任务说明面板
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "任务说明"));
        descriptionPanel.setPreferredSize(new Dimension(0, 100));
        JLabel descriptionLabel = new JLabel("<html>完成基础任务可以解锁高级任务<br>每个任务都有相应的分数和星级评价</html>");
        descriptionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        descriptionPanel.add(descriptionLabel);
        centerPanel.add(descriptionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // 创建底部面板
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建进度条面板
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("学习进度"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(0, 25));
        progressBar.setString("总分: 0 分 (已完成: 0/6)");
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // 创建控制按钮面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setupControlPanel(controlPanel);  // 使用新的方法设置控制面板
        
        bottomPanel.add(progressPanel, BorderLayout.CENTER);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 初始化任务完成状态
        initializeTaskStatus();
    }
    
    private void setupControlPanel(JPanel controlPanel) {
        homeButton = createStyledButton("主页", new Color(51, 153, 255));
        endButton = createStyledButton("结束会话", new Color(255, 51, 51));
        
        // 创建功能切换按钮
        featureToggleButton = new JToggleButton("完整功能");
        featureToggleButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        featureToggleButton.setForeground(Color.WHITE);
        featureToggleButton.setBackground(new Color(128, 0, 128));  // 紫色
        featureToggleButton.setFocusPainted(false);
        featureToggleButton.setBorderPainted(false);
        featureToggleButton.setOpaque(true);
        
        featureToggleButton.addActionListener(e -> {
            fullFeaturesEnabled = featureToggleButton.isSelected();
            UIManager.getInstance().setFullFeaturesEnabled(fullFeaturesEnabled);
            
            if (fullFeaturesEnabled) {
                featureToggleButton.setText("正常模式");
                featureToggleButton.setBackground(new Color(0, 128, 128));  // 青色
                JOptionPane.showMessageDialog(
                    this,
                    "已切换到完整功能模式：所有任务已解锁",
                    "模式切换",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                featureToggleButton.setText("完整功能");
                featureToggleButton.setBackground(new Color(128, 0, 128));  // 紫色
                JOptionPane.showMessageDialog(
                    this,
                    "已切换到正常模式：高级任务需要达到70分才能解锁",
                    "模式切换",
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
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }
    
    private void addTaskButton(String text, String tooltip, String difficulty) {
        JButton button = new JButton(text);
        
        // 修改工具提示的显示方式
        if ("advanced".equals(difficulty)) {
            // 为高级任务添加更详细的提示信息
            String advancedTooltip = String.format("<html>%s<br><br>" +
                "<font color='red'>高级任务 - 当前未解锁</font><br>" +
                "解锁条件：完成所有基础任务并获得至少70分<br>" +
                "基础任务：形状识别、角度识别、面积计算、圆形计算</html>", 
                tooltip);
            button.setToolTipText(advancedTooltip);
            button.setBackground(new Color(255, 204, 153));
            button.setForeground(new Color(153, 51, 0));
        } else {
            // 为基础任务添加基本提示
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
                    "请先完成所有基础任务以解锁高级任务！",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            com.shapeville.gui.UIManager.getInstance().switchToTask(text);
        });
        
        buttonPanel.add(button);
        taskButtons.put(text, button);
    }
    
    private void initializeTaskStatus() {
        taskCompletionStatus.put("形状识别", false);
        taskCompletionStatus.put("角度识别", false);
        taskCompletionStatus.put("面积计算", false);
        taskCompletionStatus.put("圆形计算", false);
        taskCompletionStatus.put("复合形状", false);
        taskCompletionStatus.put("扇形计算", false);
        updateProgress();
    }
    
    private boolean canAccessAdvancedTasks() {
        if (fullFeaturesEnabled) {
            return true;  // 完整功能模式下直接返回true
        }
        // 正常模式下的检查逻辑
        return taskCompletionStatus.get("形状识别") &&
               taskCompletionStatus.get("角度识别") &&
               taskCompletionStatus.get("面积计算") &&
               taskCompletionStatus.get("圆形计算");
    }
    
    public void setTaskCompleted(String taskName) {
        taskCompletionStatus.put(taskName, true);
        JButton button = taskButtons.get(taskName);
        if (button != null) {
            button.setBackground(new Color(204, 255, 204));  // 绿色背景表示完成
        }
        updateProgress();
        updateLevel();
    }
    
    public void updateProgress() {
        // 获取所有任务的完成状态和分数
        Map<String, TaskStatus> taskStatusMap = UIManager.getInstance().getTaskStatusMap();
        Map<String, Integer> taskScores = UIManager.getInstance().getTaskScores();
        
        System.out.println("正在更新主窗口进度...");
        System.out.println("任务状态: " + taskStatusMap);
        System.out.println("任务分数: " + taskScores);
        
        // 计算总分和完成任务数
        int totalScore = 0;
        int completedTasks = 0;
        
        for (Map.Entry<String, TaskStatus> entry : taskStatusMap.entrySet()) {
            String taskName = entry.getKey();
            TaskStatus status = entry.getValue();
            
            if (status == TaskStatus.COMPLETED) {
                completedTasks++;
                totalScore += taskScores.getOrDefault(taskName, 0);
                // 更新任务完成状态
                taskCompletionStatus.put(taskName, true);
                // 更新按钮状态
                JButton button = taskButtons.get(taskName);
                if (button != null) {
                    SwingUtilities.invokeLater(() -> {
                        button.setBackground(new Color(51, 153, 51)); // 深绿色表示完成
                        button.setToolTipText(button.getToolTipText() + "<br><font color='green'>已完成！分数：" + taskScores.get(taskName) + "</font>");
                    });
                }
            }
        }
        
        // 计算总体进度
        int totalTasks = taskStatusMap.size();
        int progress = (int)((double)completedTasks / totalTasks * 100);
        
        // 更新进度条
        final int finalTotalScore = totalScore;
        final int finalCompletedTasks = completedTasks;
        
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(progress);
            progressBar.setString(String.format("总分: %d分 (已完成: %d/%d)", 
                finalTotalScore, finalCompletedTasks, totalTasks));
                
            // 根据完成度设置进度条颜色
            if (progress > 0) {
                progressBar.setForeground(new Color(255, 192, 203)); // 粉红色
            }
            
            // 更新等级
            updateLevel();
            
            System.out.println("进度更新完成 - 进度: " + progress + "%, 总分: " + finalTotalScore);
        });
    }
    
    private void updateLevel() {
        int completedTasks = 0;
        for (Boolean completed : taskCompletionStatus.values()) {
            if (completed) completedTasks++;
        }
        
        if (completedTasks >= taskCompletionStatus.size()) {
            levelLabel.setText("当前等级：专家");
        } else if (completedTasks >= 4) {
            levelLabel.setText("当前等级：进阶者");
        } else if (completedTasks >= 2) {
            levelLabel.setText("当前等级：学习者");
        } else {
            levelLabel.setText("当前等级：初学者");
        }
    }
    
    public void updateTaskStatus(Map<String, TaskStatus> taskStatusMap) {
        System.out.println("正在更新任务状态...");
        // 更新每个任务按钮的状态
        taskStatusMap.forEach((taskName, status) -> {
            JButton button = taskButtons.get(taskName);
            if (button != null) {
                SwingUtilities.invokeLater(() -> {
                    updateButtonStatus(button, status);
                    System.out.println("更新任务按钮状态: " + taskName + " -> " + status);
                });
            }
        });
        // 更新进度
        updateProgress();
    }
    
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
                if (currentTooltip != null && currentTooltip.contains("未解锁")) {
                    button.setToolTipText(currentTooltip.replace("<font color='red'>高级任务 - 当前未解锁</font><br>", ""));
                }
                break;
            
            case IN_PROGRESS:
                button.setEnabled(true);
                button.setBackground(new Color(255, 153, 51));
                if (!currentTooltip.contains("正在进行中")) {
                    button.setToolTipText(currentTooltip + "<br><font color='blue'>正在进行中...</font>");
                }
                break;
            
            case COMPLETED:
                button.setEnabled(true);
                button.setBackground(new Color(51, 153, 51));
                if (!currentTooltip.contains("已完成")) {
                    button.setToolTipText(currentTooltip + "<br><font color='green'>已完成！</font>");
                }
                break;
        }
    }
    
    public void updateUserLevel(String levelTitle) {
        if (levelLabel != null) {
            levelLabel.setText("当前等级：" + levelTitle);
        }
    }
    
    public void updateProgress(int progress) {
        if (progressBar != null) {
            progressBar.setValue(progress);
            progressBar.setString(progress + "%");
        }
    }
    
    // 将原来的endButton的ActionListener移到单独的方法中
    private void handleEndSession() {
        // 获取当前进度条上显示的总分
        String progressText = progressBar.getString();
        int score = 0;
        
        try {
            if (progressText != null && progressText.contains("总分:")) {
                String[] parts = progressText.split("分")[0].split(":");
                if (parts.length > 1) {
                    score = Integer.parseInt(parts[1].trim());
                }
            }
        } catch (Exception ex) {
            System.out.println("解析分数时出错: " + ex.getMessage());
        }
        
        String encouragement;
        if (score >= 90) {
            encouragement = "太棒了！你是几何学习的小天才！";
        } else if (score >= 70) {
            encouragement = "做得很好！继续保持这份热情！";
        } else if (score >= 50) {
            encouragement = "不错的表现！相信下次会更好！";
        } else {
            encouragement = "感谢参与！每一次练习都是进步！";
        }
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            String.format("在本次学习中，你获得了 %d 分！\n%s\n\n确定要结束学习会话吗？", score, encouragement),
            "学习总结",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
