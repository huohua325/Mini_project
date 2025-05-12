package com.shapeville.gui.tasks;

import com.shapeville.gui.TaskWindow;
import javax.swing.*;
import java.awt.*;

public abstract class BaseTaskPanel extends JPanel {
    protected Timer timer;
    protected int attempts;
    protected String taskName;
    protected java.util.List<Integer> attemptsPerTask;
    protected TaskWindow parentWindow;
    
    public BaseTaskPanel(String taskName) {
        this.taskName = taskName;
        this.attempts = 0;
        this.attemptsPerTask = new java.util.ArrayList<>();
        setLayout(new BorderLayout());
        initializeUI();
    }
    
    public void setParentWindow(TaskWindow window) {
        this.parentWindow = window;
    }
    
    // 子类必须实现的方法
    public abstract void initializeUI();
    public abstract void handleSubmit();
    public abstract void reset();
    protected abstract int calculateScore();
    
    // 通用方法 - 将反馈转发到TaskWindow
    protected void setFeedback(String message) {
        if (parentWindow != null) {
            parentWindow.setFeedback(message);
        } else {
            System.out.println("警告: 无法设置反馈，parentWindow为null");
        }
    }
    
    protected void appendFeedback(String message) {
        if (parentWindow != null) {
            parentWindow.appendFeedback(message);
        } else {
            System.out.println("警告: 无法添加反馈，parentWindow为null");
        }
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