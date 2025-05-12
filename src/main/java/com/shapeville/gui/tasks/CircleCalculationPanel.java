package com.shapeville.gui.tasks;

import com.shapeville.gui.shapes.CircleDrawer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.border.EmptyBorder;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CircleCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private JComboBox<String> calculationType;
    private JLabel valueLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel timerLabel;
    private JLabel formulaLabel;
    private JPanel drawingPanel;
    private CircleDrawer circleDrawer;
    
    private Timer questionTimer; // 每道题的计时器
    private static final int TIME_PER_QUESTION = 3 * 60; // 每道题3分钟时间限制（秒）
    private int remainingTime; // 当前题目的剩余时间
    private int attempts = 0;
    private double currentValue;
    private boolean isRadius;
    private double correctAnswer;
    private Random random = new Random();
    private DecimalFormat df = new DecimalFormat("0.0");
    private int score = 0;
    private int completedCalculations = 0;
    private Set<String> completedTypes = new HashSet<>();
    private static final int TOTAL_TYPES = 4; // 总共四种计算类型
    private boolean isArea;
    
    public CircleCalculationPanel() {
        super("圆形计算");
        // 初始化成员变量后，调用自定义UI设置方法
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setupCircleUI();
    }
    
    @Override
    public void initializeUI() {
        // 只设置基本布局，不进行复杂初始化
        setLayout(new BorderLayout(10, 10));
    }
    
    // 将复杂UI初始化移到单独的方法中
    private void setupCircleUI() {
        // 初始化组件
        initializeComponents();
        
        // 设置布局
        setupLayout();
        
        // 开始新的计算任务
        startNewCalculation();
    }
    
    private void initializeComponents() {
        // 计算类型选择
        String[] types = {"面积计算", "周长计算"};
        calculationType = new JComboBox<>(types);
        calculationType.addActionListener(e -> startNewCalculation());
        
        // 输入区域
        valueLabel = new JLabel();
        answerField = new JTextField(10);
        submitButton = new JButton("提交答案");
        submitButton.addActionListener(e -> handleSubmit());
        
        // 计时器
        timerLabel = new JLabel("剩余时间: 3:00");
        timerLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLUE);
        
        // 公式显示
        formulaLabel = new JLabel();
        formulaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 绘图区域
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (circleDrawer != null) {
                    circleDrawer.draw(g, getWidth(), getHeight());
                }
            }
        };
        drawingPanel.setPreferredSize(new Dimension(300, 250)); // 调整为更合适的大小
        drawingPanel.setBackground(Color.WHITE);
        
        // 创建CircleDrawer实例
        circleDrawer = new CircleDrawer();
    }
    
    private void setupLayout() {
        // 顶部面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(calculationType);
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);
        
        // 中间面板（绘图区域）
        drawingPanel.setPreferredSize(new Dimension(500, 500)); // 增加绘图区域的大小
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        // 底部面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(valueLabel);
        bottomPanel.add(answerField);
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // 右侧面板（公式显示）
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(formulaLabel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void startNewCalculation() {
        attempts = 0;
        isRadius = random.nextBoolean();
        isArea = random.nextBoolean();
        currentValue = 1 + random.nextInt(20);
        
        // 如果当前类型已完成，重新选择
        String currentType = getCalculationType();
        while (completedTypes.contains(currentType)) {
            isRadius = random.nextBoolean();
            isArea = random.nextBoolean();
            currentType = getCalculationType();
        }
        
        // 更新UI显示
        calculationType.setSelectedItem(isArea ? "面积计算" : "周长计算");
        String valueType = isRadius ? "半径" : "直径";
        valueLabel.setText("已知" + valueType + ": " + currentValue + " ");
        
        // 更新公式显示
        updateFormulaDisplay();
        
        // 更新绘图
        circleDrawer.setValues(currentValue, isRadius);
        drawingPanel.repaint();
        
        // 计算正确答案
        calculateCorrectAnswer();
        
        // 重置输入框
        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        
        // 开始当前题目的计时
        startQuestionTimer();
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
            "此题时间已到，将自动进入下一题。",
            "时间提醒",
            JOptionPane.WARNING_MESSAGE);
        
        // 当前题目视为答错
        String formattedAnswer = df.format(correctAnswer);
        String currentType = getCalculationType();
        completedTypes.add(currentType); // 即使答错也标记为完成
        
        setFeedback("时间到！正确答案是: " + formattedAnswer + "\n" +
                     "已完成 " + completedTypes.size() + "/" + TOTAL_TYPES + " 种计算类型。");
        
        // 检查是否完成所有类型
        if (completedTypes.size() >= TOTAL_TYPES) {
            JOptionPane.showMessageDialog(this,
                "所有计算类型已完成。最终得分：" + score + "分（满分12分）",
                "任务完成",
                JOptionPane.INFORMATION_MESSAGE);
            endTask();
        } else {
            // 延迟几秒后进入下一题
            Timer delayTimer = new Timer(2000, e -> startNewCalculation());
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    
    private String getCalculationType() {
        return (isRadius ? "半径" : "直径") + (isArea ? "面积" : "周长");
    }
    
    private void updateFormulaDisplay() {
        boolean isArea = calculationType.getSelectedItem().toString().equals("面积计算");
        if (isArea) {
            if (isRadius) {
                formulaLabel.setText("<html>面积公式：<br>A = πr²<br>r = " + currentValue + "<br>(请使用π = 3.14)</html>");
            } else {
                formulaLabel.setText("<html>面积公式：<br>A = π(d/2)²<br>d = " + currentValue + "<br>(请使用π = 3.14)</html>");
            }
        } else {
            if (isRadius) {
                formulaLabel.setText("<html>周长公式：<br>C = 2πr<br>r = " + currentValue + "<br>(请使用π = 3.14)</html>");
            } else {
                formulaLabel.setText("<html>周长公式：<br>C = πd<br>d = " + currentValue + "<br>(请使用π = 3.14)</html>");
            }
        }
    }
    
    private void calculateCorrectAnswer() {
        boolean isArea = calculationType.getSelectedItem().toString().equals("面积计算");
        if (isArea) {
            if (isRadius) {
                correctAnswer = 3.14 * currentValue * currentValue;
            } else {
                double radius = currentValue / 2;
                correctAnswer = 3.14 * radius * radius;
            }
        } else {
            if (isRadius) {
                correctAnswer = 2 * 3.14 * currentValue;
            } else {
                correctAnswer = 3.14 * currentValue;
            }
        }
    }
    
    private void moveToNextQuestion() {
        // 检查是否完成所有类型
        if (completedTypes.size() >= TOTAL_TYPES) {
            JOptionPane.showMessageDialog(this,
                "所有计算类型已完成。最终得分：" + score + "分（满分12分）",
                "任务完成",
                JOptionPane.INFORMATION_MESSAGE);
            endTask();
        } else {
            // 延迟几秒后进入下一题
            Timer delayTimer = new Timer(2000, e -> startNewCalculation());
            delayTimer.setRepeats(false);
            delayTimer.start();
        }
    }
    
    @Override
    public void handleSubmit() {
        try {
            double userAnswer = Double.parseDouble(answerField.getText());
            attempts++;
            
            if (Math.abs(userAnswer - correctAnswer) < 0.1) {
                // 停止当前题目计时器
                if (questionTimer != null && questionTimer.isRunning()) {
                    questionTimer.stop();
                }
                
                String currentType = getCalculationType();
                completedTypes.add(currentType);
                score += (4 - attempts); // 根据尝试次数给分
                
                setFeedback("回答正确！得分：" + (4 - attempts) + "分。\n" +
                           "已完成 " + completedTypes.size() + "/" + TOTAL_TYPES + " 种计算类型。");
                
                moveToNextQuestion();
            } else {
                if (attempts >= 3) {
                    // 停止当前题目计时器
                    if (questionTimer != null && questionTimer.isRunning()) {
                        questionTimer.stop();
                    }
                    
                    String formattedAnswer = df.format(correctAnswer);
                    String currentType = getCalculationType();
                    completedTypes.add(currentType); // 即使答错也标记为完成
                    
                    setFeedback("本题答错次数过多。正确答案是: " + formattedAnswer + "\n" +
                              "已完成 " + completedTypes.size() + "/" + TOTAL_TYPES + " 种计算类型。");
                    
                    moveToNextQuestion();
                } else {
                    setFeedback("回答错误，还有" + (3 - attempts) + "次机会。");
                }
            }
        } catch (NumberFormatException e) {
            setFeedback("请输入有效的数字！");
        }
    }
    
    @Override
    public void cleanup() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
    }
    
    @Override
    public void reset() {
        score = 0;
        completedCalculations = 0;
        completedTypes.clear();
        if (questionTimer != null) {
            questionTimer.stop();
        }
        startNewCalculation();
    }
    
    @Override
    public int calculateScore() {
        return score;
    }

    @Override
    public void startTask() {
        reset();
    }

    @Override
    public void pauseTask() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
    }

    @Override
    public void resumeTask() {
        if (questionTimer != null && remainingTime > 0) {
            questionTimer.start();
        }
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
    }

    @Override
    public void endTask() {
        cleanup();
        if (parentWindow != null) {
            parentWindow.showResult(calculateScore(), 12); // 总分12分
        }
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getFeedback() {
        StringBuilder feedback = new StringBuilder();
        feedback.append("圆形计算练习结果：\n");
        feedback.append("完成计算类型数量：").append(completedTypes.size()).append("/").append(TOTAL_TYPES).append("\n");
        feedback.append("总得分：").append(score).append("/12分\n\n");
        
        if (score >= 10) {
            feedback.append("表现优秀！你已经很好地掌握了圆形的面积和周长计算。");
        } else if (score >= 6) {
            feedback.append("表现不错！继续练习可以做得更好。");
        } else {
            feedback.append("需要更多练习来提高计算能力。建议复习圆形的面积和周长公式。");
        }
        
        return feedback.toString();
    }
} 