package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.AngleCalculation;
import javax.swing.SpinnerNumberModel;

public class AngleCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private final AngleCalculation angleCalculation;
    private int currentAngle;
    private JLabel angleLabel;
    private JPanel angleDisplayPanel;
    private JComboBox<String> angleTypeComboBox;
    private JButton submitButton;
    private JButton homeButton;
    private JSpinner angleSpinner;
    private boolean isEnding = false;
    
    public AngleCalculationPanel() {
        super("角度识别");
        this.angleCalculation = new AngleCalculation();
        initializeUI();
        setupAngleUI();
    }
    
    @Override
    public void initializeUI() {
        // 只设置基本布局，不进行复杂初始化
        setLayout(new BorderLayout(10, 10));
    }
    
    private void setupAngleUI() {
        setLayout(new BorderLayout(10, 10));
        
        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Home按钮
        homeButton = new JButton("返回主页");
        homeButton.addActionListener(e -> endTask());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // 创建角度显示面板
        angleDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawAngle(g);
            }
        };
        angleDisplayPanel.setPreferredSize(new Dimension(600, 400));
        angleDisplayPanel.setBackground(Color.WHITE);
        
        // 创建底部控制面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // 创建Spinner用于角度输入
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(10, 0, 360, 10);
        angleSpinner = new JSpinner(spinnerModel);
        JLabel spinnerLabel = new JLabel("请输入角度(0-360度)：");
        angleSpinner.setPreferredSize(new Dimension(80, 25));
        
        // 创建角度类型选择下拉框
        angleTypeComboBox = new JComboBox<>(angleCalculation.getAngleTypes());
        angleTypeComboBox.setPreferredSize(new Dimension(120, 25));
        
        // 创建提交按钮
        submitButton = new JButton("选定角度");
        submitButton.addActionListener(e -> handleSubmit());
        
        // 添加Spinner的值变化监听器
        angleSpinner.addChangeListener(e -> {
            currentAngle = (Integer) angleSpinner.getValue();
            angleDisplayPanel.repaint();
        });
        
        // 将组件添加到底部面板
        bottomPanel.add(spinnerLabel);
        bottomPanel.add(angleSpinner);
        bottomPanel.add(angleTypeComboBox);
        bottomPanel.add(submitButton);
        
        // 将面板添加到主面板
        add(topPanel, BorderLayout.NORTH);
        add(angleDisplayPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // 初始化显示
        updateAngleDisplay();
    }
    
    private void drawAngle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = angleDisplayPanel.getWidth() / 2;
        int centerY = angleDisplayPanel.getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 50;
        
        // 设置线条样式
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // 绘制水平参考线（0度）
        g2d.drawLine(centerX - radius, centerY, centerX + radius, centerY);
        
        // 绘制当前角度线
        double angleRad = Math.toRadians(-currentAngle);
        int endX = centerX + (int)(radius * Math.cos(angleRad));
        int endY = centerY + (int)(radius * Math.sin(angleRad));
        g2d.drawLine(centerX, centerY, endX, endY);
        
        // 绘制角度弧线
        int arcRadius = radius / 3; // 弧线半径设置为主线条的1/3
        g2d.setStroke(new BasicStroke(1)); // 弧线使用较细的线条
        g2d.drawArc(centerX - arcRadius, centerY - arcRadius, 
                    arcRadius * 2, arcRadius * 2, 
                    0, currentAngle);
    }
    
    private void showInputControls(boolean show) {
        angleSpinner.setEnabled(show);
        angleTypeComboBox.setEnabled(!show);
        submitButton.setText(show ? "选定角度" : "提交答案");
        if (show) {
            setFeedback(angleCalculation.getRemainingTypesMessage());
        }
    }
    
    private void updateAngleDisplay() {
        currentAngle = (Integer) angleSpinner.getValue();
        angleDisplayPanel.repaint();
        resetAttempts();
        showInputControls(true);
    }
    
    @Override
    public void handleSubmit() {
        if (angleSpinner.isEnabled()) {
            // 用户正在输入角度
            currentAngle = (Integer) angleSpinner.getValue();
            String correctType = angleCalculation.getAngleType(currentAngle);
            
            // 检查是否已经识别过这种类型
            if (angleCalculation.isTypeIdentified(correctType)) {
                setFeedback("这种角度类型已经被正确识别过了，请尝试其他类型的角度！\n" + 
                           angleCalculation.getRemainingTypesMessage());
                return;
            }
            
            // 锁定角度输入，允许选择类型
            showInputControls(false);
            setFeedback("请选择这个角度的类型");
            
        } else {
            // 用户正在回答角度类型
            String selectedType = (String) angleTypeComboBox.getSelectedItem();
            String correctType = angleCalculation.getAngleType(currentAngle);
            
            incrementAttempts();
            angleCalculation.incrementTotalQuestions(); // 增加总题目数
            
            if (angleCalculation.checkAnswer(currentAngle, selectedType)) {
                // 答对了
                angleCalculation.addIdentifiedType(correctType);
                addAttemptToList();
                setFeedback("回答正确！\n" + angleCalculation.getRemainingTypesMessage());
                
                if (angleCalculation.isTaskComplete()) {
                    endTask();
                    return;
                }
                // 重置为输入新角度状态
                updateAngleDisplay();
                
            } else if (!hasRemainingAttempts()) {
                // 用完三次机会
                addAttemptToList();
                setFeedback("已达到最大尝试次数。正确答案是：" + correctType + "\n请尝试识别新的角度类型\n" + 
                           angleCalculation.getRemainingTypesMessage());
                
                // 重置为输入新角度状态
                updateAngleDisplay();
                
            } else {
                setFeedback("回答错误，请再试一次。还剩" + getRemainingAttempts() + "次机会。");
            }
        }
    }
    
    @Override
    public void reset() {
        resetAttempts();
        attemptsPerTask.clear();
        angleSpinner.setValue(10);
        updateAngleDisplay();
    }
    
    @Override
    public void startTask() {
        reset();
    }
    
    @Override
    public void pauseTask() {
        if (submitButton != null) submitButton.setEnabled(false);
        if (angleTypeComboBox != null) angleTypeComboBox.setEnabled(false);
        if (homeButton != null) homeButton.setEnabled(false);
    }
    
    @Override
    public void resumeTask() {
        if (submitButton != null) submitButton.setEnabled(true);
        if (angleTypeComboBox != null) angleTypeComboBox.setEnabled(true);
        if (homeButton != null) homeButton.setEnabled(true);
    }
    
    @Override
    public void endTask() {
        if (!isEnding) {
            isEnding = true;
            cleanup();
            if (parentWindow != null) {
                parentWindow.showResult(calculateScore(), angleCalculation.getMaxPossibleScore());
            }
        }
    }
    
    @Override
    protected int calculateScore() {
        return angleCalculation.calculateScore(attemptsPerTask);
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        int score = calculateScore();
        int maxScore = angleCalculation.getMaxPossibleScore();
        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("本次测试完成！\n得分：%d（总分%d分）\n\n", score, maxScore));
        feedback.append("角度识别测试统计：\n");
        
        int totalAttempted = attemptsPerTask.size();
        int correctWithinThree = 0;
        int perfectCount = 0;
        
        for (int attempts : attemptsPerTask) {
            if (attempts <= 3) {
                correctWithinThree++;
                if (attempts == 1) perfectCount++;
            }
        }
        
        feedback.append(String.format("已尝试角度数量：%d\n", totalAttempted));
        feedback.append(String.format("3次内正确数量：%d\n", correctWithinThree));
        feedback.append(String.format("一次答对数量：%d\n", perfectCount));
        
        double scorePercentage = (double) score / maxScore * 100;
        feedback.append("\n").append(scorePercentage >= 90 ? "太棒了！你对角度类型的判断已经非常熟练了！" :
                                   scorePercentage >= 80 ? "不错的表现！继续练习可以做得更好！" :
                                   scorePercentage >= 70 ? "继续加油！多加练习一定能提高！" :
                                                         "需要更多练习来提高判断能力！");
        
        return feedback.toString();
    }
} 