package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;

public abstract class BaseTaskPanel extends JPanel {
    protected Timer timer;
    protected int attempts;
    protected JTextArea feedbackArea;
    protected String taskName;
    protected java.util.List<Integer> attemptsPerTask;
    
    public BaseTaskPanel(String taskName) {
        this.taskName = taskName;
        this.attempts = 0;
        this.attemptsPerTask = new java.util.ArrayList<>();
        setLayout(new BorderLayout());
        initializeCommonComponents();
        initializeUI();
    }
    
    private void initializeCommonComponents() {
        // 创建反馈区域
        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setLineWrap(true);
        feedbackArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(750, 100));
        add(feedbackScroll, BorderLayout.SOUTH);
    }
    
    // 子类必须实现的方法
    public abstract void initializeUI();
    public abstract void handleSubmit();
    public abstract void reset();
    protected abstract int calculateScore();
    
    // 通用方法
    protected void setFeedback(String message) {
        feedbackArea.setText(message);
    }
    
    protected void appendFeedback(String message) {
        feedbackArea.append(message + "\n");
    }
    
    protected void incrementAttempts() {
        attempts++;
    }
    
    protected void resetAttempts() {
        attempts = 0;
    }
    
    protected void addAttemptToList() {
        attemptsPerTask.add(attempts);
    }
    
    protected int getRemainingAttempts() {
        return 3 - attempts;
    }
    
    protected boolean hasRemainingAttempts() {
        return attempts < 3;
    }
    
    public java.util.List<Integer> getAttemptsPerTask() {
        return attemptsPerTask;
    }
    
    public void cleanup() {
        if (timer != null) {
            timer.stop();
        }
    }
} 