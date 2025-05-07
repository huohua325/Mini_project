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
    
    public MainWindow() {
        taskButtons = new HashMap<>();
        taskCompletionStatus = new HashMap<>();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Shapeville - 几何学习乐园");
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
        progressPanel.setBorder(BorderFactory.createTitledBorder("总体进度"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(0, 25));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // 创建控制按钮面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        homeButton = createStyledButton("主页", new Color(51, 153, 255));
        endButton = createStyledButton("结束会话", new Color(255, 51, 51));
        
        homeButton.addActionListener(e -> com.shapeville.gui.UIManager.getInstance().showMainWindow());
        endButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "确定要结束学习会话吗？",
                "确认",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        controlPanel.add(homeButton);
        controlPanel.add(endButton);
        
        bottomPanel.add(progressPanel, BorderLayout.CENTER);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // 初始化任务完成状态
        initializeTaskStatus();
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
        button.setToolTipText(tooltip);
        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        
        // 设置按钮样式
        if ("advanced".equals(difficulty)) {
            button.setBackground(new Color(255, 204, 153));
            button.setForeground(new Color(153, 51, 0));
        } else {
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
        int completedTasks = 0;
        for (Boolean completed : taskCompletionStatus.values()) {
            if (completed) completedTasks++;
        }
        int progress = (int)((double)completedTasks / taskCompletionStatus.size() * 100);
        progressBar.setValue(progress);
        progressBar.setString(progress + "% (" + completedTasks + "/" + taskCompletionStatus.size() + ")");
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
        // 更新每个任务按钮的状态
        taskStatusMap.forEach((taskName, status) -> {
            JButton button = taskButtons.get(taskName);
            if (button != null) {
                updateButtonStatus(button, status);
            }
        });
    }
    
    private void updateButtonStatus(JButton button, TaskStatus status) {
        switch (status) {
            case LOCKED:
                button.setEnabled(false);
                button.setBackground(Color.GRAY);
                break;
            case UNLOCKED:
                button.setEnabled(true);
                button.setBackground(new Color(51, 153, 255));
                break;
            case IN_PROGRESS:
                button.setEnabled(true);
                button.setBackground(new Color(255, 153, 51));
                break;
            case COMPLETED:
                button.setEnabled(true);
                button.setBackground(new Color(51, 153, 51));
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.shapeville.gui.UIManager.getInstance().initialize();
        });
    }
}
