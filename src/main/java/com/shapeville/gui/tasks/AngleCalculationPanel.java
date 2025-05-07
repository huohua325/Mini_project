package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class AngleCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private final List<Integer> angles;
    private final Set<String> identifiedTypes;
    private int currentAngle;
    private JLabel angleLabel;
    private JPanel angleDisplayPanel;
    private JComboBox<String> angleTypeComboBox;
    private JButton submitButton;
    private final String[] ANGLE_TYPES = {"acute", "right", "obtuse", "straight", "reflex"};
    
    public AngleCalculationPanel() {
        super("角度识别");
        this.angles = new ArrayList<>();
        this.identifiedTypes = new HashSet<>();
        initializeAngles();
    }
    
    private void initializeAngles() {
        // 只用10的倍数，排除0和360
        for (int i = 10; i < 360; i += 10) {
            angles.add(i);
        }
        Collections.shuffle(angles);
    }
    
    @Override
    public void initializeUI() {
        // 创建中央面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // 创建角度显示面板
        angleDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAngle(g);
            }
        };
        angleDisplayPanel.setPreferredSize(new Dimension(300, 300));
        angleDisplayPanel.setBackground(Color.WHITE);
        
        // 创建角度标签
        angleLabel = new JLabel("", SwingConstants.CENTER);
        angleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        angleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 创建下拉选择框
        angleTypeComboBox = new JComboBox<>(ANGLE_TYPES);
        angleTypeComboBox.setMaximumSize(new Dimension(200, 30));
        
        // 创建提交按钮
        submitButton = new JButton("提交答案");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> handleSubmit());
        
        // 添加组件到中央面板
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(angleLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(angleDisplayPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(angleTypeComboBox);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(submitButton);
        centerPanel.add(Box.createVerticalStrut(20));
        
        // 添加中央面板到主面板
        add(centerPanel, BorderLayout.CENTER);
        
        // 显示第一个角度
        showNextAngle();
    }
    
    private void drawAngle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = angleDisplayPanel.getWidth() / 2;
        int centerY = angleDisplayPanel.getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 50;
        
        // 绘制第一条线（水平线）
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(centerX - radius, centerY, centerX, centerY);
        
        // 绘制第二条线（根据角度旋转）
        double angleRad = Math.toRadians(currentAngle);
        int endX = centerX + (int)(radius * Math.cos(angleRad));
        int endY = centerY - (int)(radius * Math.sin(angleRad));
        g2d.drawLine(centerX, centerY, endX, endY);
        
        // 绘制弧线
        g2d.setColor(Color.RED);
        g2d.drawArc(centerX - 30, centerY - 30, 60, 60, 0, -currentAngle);
    }
    
    private void showNextAngle() {
        if (!angles.isEmpty()) {
            currentAngle = angles.remove(0);
            angleLabel.setText("请判断该角度的类型（" + currentAngle + "°）");
            angleDisplayPanel.repaint();
            resetAttempts();
        } else {
            endTask();
        }
    }
    
    private String getAngleType(int angle) {
        if (angle == 90) return "right";
        if (angle > 0 && angle < 90) return "acute";
        if (angle > 90 && angle < 180) return "obtuse";
        if (angle == 180) return "straight";
        if (angle > 180 && angle < 360) return "reflex";
        return "unknown";
    }
    
    @Override
    public void handleSubmit() {
        String selectedType = (String) angleTypeComboBox.getSelectedItem();
        String correctType = getAngleType(currentAngle);
        
        incrementAttempts();
        
        if (selectedType.equals(correctType)) {
            setFeedback("回答正确！");
            identifiedTypes.add(correctType);
            addAttemptToList();
            
            if (identifiedTypes.size() >= 5 || angles.isEmpty()) {
                endTask();
                return;
            }
            showNextAngle();
        } else if (!hasRemainingAttempts()) {
            setFeedback("已达到最大尝试次数。正确答案是：" + correctType);
            identifiedTypes.add(correctType);
            addAttemptToList();
            
            if (identifiedTypes.size() >= 5 || angles.isEmpty()) {
                endTask();
                return;
            }
            showNextAngle();
        } else {
            setFeedback("回答错误，请再试一次。还剩" + getRemainingAttempts() + "次机会。");
        }
    }
    
    @Override
    public void reset() {
        angles.clear();
        identifiedTypes.clear();
        initializeAngles();
        resetAttempts();
        attemptsPerTask.clear();
        showNextAngle();
    }
    
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        for (int attempts : attemptsPerTask) {
            if (attempts == 1) totalScore += 3;
            else if (attempts == 2) totalScore += 2;
            else if (attempts == 3) totalScore += 1;
        }
        return (int)((double)totalScore / (attemptsPerTask.size() * 3) * 100);
    }
    
    @Override
    public void startTask() {
        reset();
    }
    
    @Override
    public void pauseTask() {
        submitButton.setEnabled(false);
        angleTypeComboBox.setEnabled(false);
    }
    
    @Override
    public void resumeTask() {
        submitButton.setEnabled(true);
        angleTypeComboBox.setEnabled(true);
    }
    
    @Override
    public void endTask() {
        cleanup();
        setFeedback("任务完成！最终得分：" + calculateScore());
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        return feedbackArea.getText();
    }
} 