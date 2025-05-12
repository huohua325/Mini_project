package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.shapeville.gui.tasks.*;
import com.shapeville.gui.tasks.TaskPanelInterface;

public class TaskWindow extends JFrame {
    private String taskName;
    private JTextArea taskDescription;
    private JPanel inputPanel;
    private JLabel timerLabel;
    private JTextArea feedbackArea;
    private javax.swing.Timer timer;
    private int seconds = 0;
    private TaskPanelInterface currentTask;
    
    public TaskWindow(String taskName) {
        this.taskName = taskName;
        initializeUI();
        setupTask();
        startTimer();
    }
    
    private void initializeUI() {
        setTitle("Shapeville - " + taskName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建任务说明区域
        taskDescription = new JTextArea();
        taskDescription.setEditable(false);
        taskDescription.setWrapStyleWord(true);
        taskDescription.setLineWrap(true);
        taskDescription.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskDescription);
        scrollPane.setPreferredSize(new Dimension(750, 100));
        mainPanel.add(scrollPane, BorderLayout.NORTH);
        
        // 创建输入面板
        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // 创建计时器标签
        timerLabel = new JLabel("用时: 0:00");
        timerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 创建反馈区域
        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setLineWrap(true);
        feedbackArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(750, 100));
        
        // 创建底部控制面板
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(timerLabel, BorderLayout.WEST);
        controlPanel.add(feedbackScroll, BorderLayout.CENTER);
        
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
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
        }
        // 这里可以添加其他任务的设置
    }
    
    private void startTimer() {
        timer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                updateTimerLabel();
            }
        });
        timer.start();
    }
    
    private void updateTimerLabel() {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timerLabel.setText(String.format("用时: %d:%02d", minutes, secs));
    }
    
    public void setTaskDescription(String description) {
        taskDescription.setText(description);
    }
    
    public void setFeedback(String feedback) {
        feedbackArea.setText(feedback);
    }
    
    public void appendFeedback(String feedback) {
        feedbackArea.append(feedback + "\n");
    }
    
    public void showResult(int score, int maxScore) {
        System.out.println("TaskWindow显示结果 - 任务: " + taskName + ", 分数: " + score + "/" + maxScore);
        String feedback = generateFeedback(score, maxScore);
        UIManager.getInstance().showResult(taskName, score, maxScore, feedback);
    }
    
    private String generateFeedback(int score, int maxScore) {
        double percentage = (double) score / maxScore * 100;
        String stars = "★".repeat((int) (percentage / 20));
        String rating;
        
        if (percentage >= 90) {
            rating = "优秀";
        } else if (percentage >= 80) {
            rating = "良好";
        } else if (percentage >= 60) {
            rating = "及格";
        } else {
            rating = "需要继续努力";
        }
        
        return String.format("得分：%d/%d\n评级：%s\n%s", score, maxScore, rating, stars);
    }
    
    public void cleanup() {
        if (timer != null) {
            timer.stop();
        }
        if (currentTask != null) {
            currentTask.pauseTask();
        }
    }
    
    @Override
    public void dispose() {
        cleanup();
        super.dispose();
    }
}
