package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.SectorCalculation;
import com.shapeville.game.SectorCalculation.Sector;
import com.shapeville.gui.TaskWindow;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SectorCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private static final int MAX_ATTEMPTS = 3;  // 每题最多3次尝试机会
    private static final int TIME_PER_QUESTION = 5 * 60; // 每道题5分钟时间限制（秒）
    private final SectorCalculation sectorCalculation;
    private int currentSectorIndex = 0;
    private JComboBox<String> sectorSelector;
    private JPanel sectorDisplayPanel;
    private JLabel sectorLabel;
    private JTextArea descriptionArea;
    private JTextField areaField;
    private JButton submitButton;
    private JTextArea areaSolutionArea;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private int attempts = 0;
    private boolean areaCorrect = false;
    private boolean arcLengthCorrect = false;
    private List<Boolean> correctAnswers;
    private int score = 0;
    private JButton nextButton;
    private Timer questionTimer; // 每道题的计时器
    private int remainingTime;   // 当前题目的剩余时间
    private JLabel timerLabel;   // 计时器显示标签
    
    public SectorCalculationPanel() {
        super("扇形面积计算");
        this.sectorCalculation = new SectorCalculation();
        this.correctAnswers = new ArrayList<>();
        initializeUI();
    }
    
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 添加计时器标签
        timerLabel = new JLabel("剩余时间: 5:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLUE);
        topPanel.add(timerLabel, BorderLayout.EAST);
        
        // 创建扇形选择器
        List<Sector> sectors = sectorCalculation.getSectors();
        String[] sectorNames = new String[sectors.size()];
        for (int i = 0; i < sectors.size(); i++) {
            sectorNames[i] = "扇形 " + (i + 1);
        }
        sectorSelector = new JComboBox<>(sectorNames);
        sectorSelector.addActionListener(e -> {
            currentSectorIndex = sectorSelector.getSelectedIndex();
            showCurrentSector();
        });
        topPanel.add(sectorSelector, BorderLayout.NORTH);
        
        // 设置扇形标签
        sectorLabel = new JLabel("", SwingConstants.CENTER);
        sectorLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(sectorLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        // 创建中央面板
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建扇形显示面板
        sectorDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentSectorIndex >= 0 && currentSectorIndex < sectorCalculation.getSectors().size()) {
                    Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
                    sector.draw((Graphics2D) g, getWidth(), getHeight());
                }
            }
        };
        sectorDisplayPanel.setPreferredSize(new Dimension(400, 400));
        sectorDisplayPanel.setBackground(Color.WHITE);
        
        // 创建右侧输入面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // 添加面积输入区域
        JPanel areaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        areaPanel.add(new JLabel("面积："));
        areaField = new JTextField(10);
        areaPanel.add(areaField);
        inputPanel.add(areaPanel);
        
        // 添加提交按钮
        submitButton = new JButton("提交答案");
        submitButton.addActionListener(e -> handleSubmit());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(submitButton);
        
        // 添加下一题按钮
        nextButton = new JButton("下一题");
        nextButton.addActionListener(e -> goToNextQuestion());
        nextButton.setVisible(false);
        buttonPanel.add(nextButton);
        inputPanel.add(buttonPanel);
        
        // 添加反馈标签
        feedbackLabel = new JLabel("");
        inputPanel.add(feedbackLabel);
        
        // 添加解答区域
        areaSolutionArea = new JTextArea(4, 30);
        areaSolutionArea.setEditable(false);
        areaSolutionArea.setLineWrap(true);
        areaSolutionArea.setWrapStyleWord(true);
        JScrollPane areaSolutionScroll = new JScrollPane(areaSolutionArea);
        areaSolutionScroll.setBorder(BorderFactory.createTitledBorder("解题步骤"));
        inputPanel.add(areaSolutionScroll);
        
        // 将面板添加到主面板
        centerPanel.add(sectorDisplayPanel, BorderLayout.CENTER);
        centerPanel.add(inputPanel, BorderLayout.EAST);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // 显示第一个扇形
        showCurrentSector();
    }
    
    private void drawSector(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = sectorDisplayPanel.getWidth() / 2;
        int centerY = sectorDisplayPanel.getHeight() / 2;
        
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        int pixelRadius = Math.min(centerX, centerY) - 50;
        
        // 绘制扇形
        g2d.setColor(new Color(200, 200, 255));
        g2d.fillArc(centerX - pixelRadius, centerY - pixelRadius, 
                    pixelRadius * 2, pixelRadius * 2, 
                    0, -(int)sector.getAngle());
        
        // 绘制扇形边界
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(centerX - pixelRadius, centerY - pixelRadius, 
                    pixelRadius * 2, pixelRadius * 2, 
                    0, -(int)sector.getAngle());
        g2d.drawLine(centerX, centerY, 
                    centerX + pixelRadius, centerY);
        g2d.drawLine(centerX, centerY, 
                    (int)(centerX + pixelRadius * Math.cos(Math.toRadians(-sector.getAngle()))),
                    (int)(centerY + pixelRadius * Math.sin(Math.toRadians(-sector.getAngle()))));
        
        // 标注半径
        g2d.setColor(Color.BLACK);
        g2d.drawString("r = " + sector.getRadius(), centerX + pixelRadius/2, centerY - 10);
        
        // 标注角度
        int angleX = centerX + pixelRadius/3;
        int angleY = centerY - pixelRadius/3;
        g2d.drawString(sector.getAngle() + "°", angleX, angleY);
    }
    
    private void showCurrentSector() {
        List<Sector> sectors = sectorCalculation.getSectors();
        if (currentSectorIndex < sectors.size()) {
            Sector sector = sectors.get(currentSectorIndex);
            sectorLabel.setText("扇形 " + (currentSectorIndex + 1));
            areaField.setText("");
            areaField.setEnabled(true);
            submitButton.setEnabled(true);
            nextButton.setVisible(false);
            sectorSelector.setSelectedIndex(currentSectorIndex);
            areaSolutionArea.setText("请计算扇形的面积");
            
            // 重绘扇形
            sectorDisplayPanel.repaint();
            
            // 开始计时
            startQuestionTimer();
        } else {
            endTask();
        }
    }
    
    private void completedCurrentQuestion(boolean correct) {
        // 停止计时器
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        // 记录答题结果
        correctAnswers.add(correct);
        addAttemptToList();
        
        // 标记当前扇形已练习
        sectorCalculation.addPracticed(currentSectorIndex);
        
        // 检查是否完成所有扇形
        if (sectorCalculation.isComplete()) {
            endTask();
        } else {
            // 显示下一题按钮
            nextButton.setVisible(true);
        }
    }
    
    @Override
    public void handleSubmit() {
        String areaStr = areaField.getText().trim();
        
        try {
            double answer = Double.parseDouble(areaStr);
            incrementAttempts();
            
            Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
            double correctArea = sector.getCorrectArea();
            
            // 检查答案是否正确（允许0.1的误差）
            if (Math.abs(answer - correctArea) <= 0.1) {
                int currentAttempts = getAttempts();
                int points = currentAttempts == 1 ? 6 : 
                            currentAttempts == 2 ? 4 : 
                            currentAttempts == 3 ? 2 : 0;
                
                String feedback = String.format("太棒了！答案正确！\n本题得分：%d分", points);
                setFeedback(feedback);
                
                // 显示解题步骤
                areaSolutionArea.setText(sector.getSolution());
                
                // 禁用输入和提交
                areaField.setEnabled(false);
                submitButton.setEnabled(false);
                
                // 显示下一题按钮
                nextButton.setVisible(true);
                
                // 标记当前扇形已完成
                sectorCalculation.addPracticed(currentSectorIndex);
                
            } else if (!hasRemainingAttempts()) {
                String feedback = String.format("很遗憾，三次机会已用完。\n正确答案是：%.1f %s²\n本题得分：0分",
                    correctArea, sector.getUnit());
                setFeedback(feedback);
                
                // 显示解题步骤
                areaSolutionArea.setText(sector.getSolution());
                
                // 禁用输入和提交
                areaField.setEnabled(false);
                submitButton.setEnabled(false);
                
                // 显示下一题按钮
                nextButton.setVisible(true);
                
                // 标记当前扇形已完成
                sectorCalculation.addPracticed(currentSectorIndex);
                
            } else {
                int remainingAttempts = getRemainingAttempts();
                int nextPoints = remainingAttempts == 2 ? 4 : 2;
                String feedback = String.format("答案不正确，请再试一次。\n还剩%d次机会，答对可得%d分。", 
                                             remainingAttempts, nextPoints);
                setFeedback(feedback);
            }
        } catch (NumberFormatException e) {
            setFeedback("请输入有效的数字！");
        }
    }
    
    @Override
    public void reset() {
        currentSectorIndex = 0;
        resetTask();
        attemptsPerTask.clear();
        correctAnswers.clear();
        sectorCalculation.reset();
        score = 0;
        updateScoreLabel();
        showCurrentSector();
    }
    
    private void resetTask() {
        resetAttempts();
        areaCorrect = false;
        arcLengthCorrect = false;
        nextButton.setVisible(false);
    }
    
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        for (int i = 0; i < attemptsPerTask.size(); i++) {
            if (i < correctAnswers.size() && correctAnswers.get(i)) {
                int attempts = attemptsPerTask.get(i);
                if (attempts == 1) totalScore += 6;
                else if (attempts == 2) totalScore += 4;
                else if (attempts == 3) totalScore += 2;
            }
        }
        return totalScore;
    }
    
    @Override
    public void startTask() {
        currentSectorIndex = 0;
        showCurrentSector();
    }
    
    @Override
    public void pauseTask() {
        if (submitButton != null) {
            submitButton.setEnabled(false);
        }
        if (areaField != null) {
            areaField.setEnabled(false);
        }
        if (sectorSelector != null) {
            sectorSelector.setEnabled(false);
        }
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
    }
    
    @Override
    public void resumeTask() {
        if (submitButton != null) {
            submitButton.setEnabled(true);
        }
        if (areaField != null) {
            areaField.setEnabled(true);
        }
        if (sectorSelector != null) {
            sectorSelector.setEnabled(true);
        }
        if (questionTimer != null && !questionTimer.isRunning() && remainingTime > 0) {
            questionTimer.start();
        }
    }
    
    @Override
    public void endTask() {
        // 停止计时器
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        // 计算总分
        int totalScore = calculateScore();
        int maxScore = sectorCalculation.getSectors().size() * 6; // 每题满分6分
        
        // 显示最终得分
        String message = String.format("练习完成！\n总得分：%d/%d", totalScore, maxScore);
        JOptionPane.showMessageDialog(this, message, "练习结束", JOptionPane.INFORMATION_MESSAGE);
        
        // 通知父窗口任务结束
        if (parentWindow != null) {
            parentWindow.showResult(totalScore, maxScore);
        }
    }
    
    private String generateFeedback(int score, int maxScore) {
        double percentage = (double) score / maxScore * 100;
        StringBuilder feedback = new StringBuilder();
        
        feedback.append(String.format("本次练习总得分：%d/%d\n\n", score, maxScore));
        feedback.append("详细评价：\n");
        
        if (percentage >= 90) {
            feedback.append("优秀！你已经完全掌握了扇形的面积和弧长计算。");
        } else if (percentage >= 80) {
            feedback.append("很好！你对扇形的计算有很好的理解。");
        } else if (percentage >= 70) {
            feedback.append("不错！继续练习可以做得更好。");
        } else if (percentage >= 60) {
            feedback.append("及格！但还需要多加练习。");
        } else {
            feedback.append("需要继续努力！建议复习扇形的计算公式：\n");
            feedback.append("面积公式：A = (θ/360°) × πr²\n");
            feedback.append("弧长公式：L = (θ/360°) × 2πr");
        }
        
        return feedback.toString();
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        return "扇形面积计算 - 当前得分：" + calculateScore();
    }

    /**
     * 获取当前题目的尝试次数
     * @return 当前尝试次数
     */
    private int getAttempts() {
        return MAX_ATTEMPTS - getRemainingAttempts();
    }

    // 添加一个debug方法来打印计算步骤区域的状态
    private void printSolutionAreaStatus(String label) {
        System.out.println("==== " + label + " ====");
        System.out.println("areaSolutionArea visibility: " + areaSolutionArea.isVisible());
        System.out.println("areaSolutionArea text: [" + areaSolutionArea.getText() + "]");
        System.out.println("arcLengthSolutionArea visibility: " + arcLengthSolutionArea.isVisible());
        System.out.println("arcLengthSolutionArea text: [" + arcLengthSolutionArea.getText() + "]");
        System.out.println("=====================");
    }

    private void updateFeedbackAndScore(boolean isCorrect, boolean isAreaCalculation) {
        String calculationType = isAreaCalculation ? "面积" : "弧长";
        
        if (isCorrect) {
            setFeedback(calculationType + "计算正确！");
            if (attempts == 1) {
                score += isAreaCalculation ? 6 : 4;
            } else if (attempts == 2) {
                score += isAreaCalculation ? 4 : 2;
            } else if (attempts == 3) {
                score += isAreaCalculation ? 2 : 1;
            }
        } else {
            attempts++;
            setFeedback(calculationType + "计算错误，请继续尝试。（还剩" + (MAX_ATTEMPTS - attempts) + "次机会）");
        }
        
        updateScoreLabel();
    }

    private void initializeScoreComponents() {
        scoreLabel = new JLabel("得分：0");
        scoreLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        add(scoreLabel);
    }
    
    private void updateScoreLabel() {
        if (scoreLabel != null) {
            scoreLabel.setText("得分：" + score);
        }
    }
    
    private void resetScore() {
        score = 0;
        updateScoreLabel();
    }

    private void initializeComponents() {
        // 初始化标签
        sectorLabel = new JLabel("", SwingConstants.CENTER);
        sectorLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        
        // 初始化文本区域
        areaSolutionArea = new JTextArea();
        areaSolutionArea.setEditable(false);
        areaSolutionArea.setLineWrap(true);
        areaSolutionArea.setWrapStyleWord(true);
        
        // 初始化输入字段
        areaField = new JTextField(10);
        
        // 初始化按钮
        submitButton = new JButton("提交答案");
        submitButton.addActionListener(e -> handleSubmit());
        
        nextButton = new JButton("下一题");
        nextButton.addActionListener(e -> goToNextQuestion());
        nextButton.setVisible(false);  // 初始时不可见
        
        // 初始化反馈标签
        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 初始化分数标签
        scoreLabel = new JLabel("得分：0");
        scoreLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 初始化描述区域
        descriptionArea = new JTextArea(2, 40);
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        descriptionArea.setBackground(new Color(240, 240, 240));
    }
    
    private void setupLayout() {
        // 创建主面板，使用BorderLayout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建内容主面板
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        
        // 创建扇形选择器面板
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        List<Sector> sectors = sectorCalculation.getSectors();
        String[] sectorNames = new String[sectors.size()];
        for (int i = 0; i < sectors.size(); i++) {
            sectorNames[i] = "扇形 " + (i + 1);
        }
        sectorSelector = new JComboBox<>(sectorNames);
        sectorSelector.addActionListener(e -> {
            if (!sectorCalculation.getPracticed().contains(sectorSelector.getSelectedIndex())) {
                currentSectorIndex = sectorSelector.getSelectedIndex();
                resetTask();
                showCurrentSector();
            }
        });
        selectorPanel.add(sectorSelector);
        selectorPanel.add(sectorLabel);
        selectorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(selectorPanel);
        
        // 创建扇形显示面板
        sectorDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSector(g);
            }
        };
        sectorDisplayPanel.setPreferredSize(new Dimension(300, 300));
        sectorDisplayPanel.setBackground(Color.WHITE);
        sectorDisplayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(sectorDisplayPanel);
        
        // 添加公式提示
        JLabel formulaLabel = new JLabel("记住：扇形面积 = (πr²×角度)/360°，弧长 = (2πr×角度)/360°");
        formulaLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formulaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalStrut(20));
        mainContentPanel.add(formulaLabel);
        
        // 添加参数信息区域
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("参数信息"));
        descriptionScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalStrut(20));
        mainContentPanel.add(descriptionScroll);
        
        // 添加输入区域
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        inputPanel.add(new JLabel("面积："));
        inputPanel.add(areaField);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalStrut(20));
        mainContentPanel.add(inputPanel);
        
        // 添加按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(buttonPanel);
        
        // 添加解答区域
        mainContentPanel.add(Box.createVerticalStrut(20));
        JLabel areaSolutionLabel = new JLabel("面积计算步骤：");
        areaSolutionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(areaSolutionLabel);
        JScrollPane areaSolutionScroll = new JScrollPane(areaSolutionArea);
        areaSolutionScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(areaSolutionScroll);
        
        // 添加反馈和分数
        mainContentPanel.add(Box.createVerticalStrut(20));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(feedbackLabel);
        mainContentPanel.add(Box.createVerticalStrut(5));
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContentPanel.add(scoreLabel);
        
        // 将整个内容面板添加到滚动面板
        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // 添加到主面板
        add(mainScrollPane, BorderLayout.CENTER);
    }
    
    private void goToNextQuestion() {
        // 找到下一个未完成的扇形
        do {
            currentSectorIndex++;
            if (currentSectorIndex >= sectorCalculation.getSectors().size()) {
                endTask();
                return;
            }
        } while (sectorCalculation.getPracticed().contains(currentSectorIndex));
        
        // 重置状态
        resetAttempts();
        showCurrentSector();
    }
    
    private void showSolutions() {
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        
        // 显示正确答案和解题步骤
        areaSolutionArea.setText(sector.getSolution());
        
        // 显示反馈信息
        String feedback = String.format("已达到最大尝试次数。正确答案：\n面积：%.2f %s²",
                                   sector.getCorrectArea(), sector.getUnit());
        setFeedback(feedback);
        
        // 禁用输入和提交
        areaField.setEnabled(false);
        submitButton.setEnabled(false);
        
        // 显示下一题按钮
        nextButton.setVisible(true);
        
        // 标记当前扇形已完成
        sectorCalculation.addPracticed(currentSectorIndex);
    }

    // 启动每道题的计时器
    private void startQuestionTimer() {
        // 停止正在运行的计时器（如果有）
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        // 重置剩余时间
        remainingTime = TIME_PER_QUESTION;
        updateTimerLabel();
        
        // 创建并启动新的计时器
        questionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                updateTimerLabel();
                
                if (remainingTime <= 0) {
                    questionTimer.stop();
                    handleTimeUp();
                }
            }
        });
        questionTimer.start();
    }
    
    // 更新计时器标签
    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        
        // 当剩余时间少于1分钟时文字变红
        if (remainingTime < 60) {
            timerLabel.setForeground(Color.RED);
        } else {
            timerLabel.setForeground(Color.BLUE);
        }
        
        timerLabel.setText(String.format("剩余时间: %d:%02d", minutes, seconds));
    }
    
    // 处理时间用完的情况
    private void handleTimeUp() {
        JOptionPane.showMessageDialog(this,
            "此题时间已到。请查看解题步骤，然后点击下一题继续。",
            "时间提醒",
            JOptionPane.WARNING_MESSAGE);
        
        // 显示正确答案和解题步骤
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        String solution = sector.getSolution();
        if (solution == null || solution.isEmpty()) {
            solution = "此题暂无详细解题步骤";
        }
        areaSolutionArea.setText(solution);
        
        // 禁用输入和提交
        areaField.setEnabled(false);
        submitButton.setEnabled(false);
        
        // 显示下一题按钮
        nextButton.setVisible(true);
        
        // 更新反馈
        setFeedback("时间到！正确答案：面积 = " + String.format("%.1f", sector.getCorrectArea()));
    }
} 