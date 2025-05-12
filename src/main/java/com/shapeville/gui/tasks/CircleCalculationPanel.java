package com.shapeville.gui.tasks;

import com.shapeville.gui.shapes.CircleDrawer;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.border.EmptyBorder;
import java.util.HashSet;
import java.util.Set;

public class CircleCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private JComboBox<String> calculationType;
    private JLabel valueLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel timerLabel;
    private JLabel formulaLabel;
    private JPanel drawingPanel;
    private CircleDrawer circleDrawer;
    
    private Timer timer;
    private int timeLeft = 180; // 3分钟
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
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        initializeUI();
    }
    
    @Override
    public void initializeUI() {
        // 初始化组件
        initializeComponents();
        
        // 设置布局
        setupLayout();
        
        // 开始新的计算任务
        startNewCalculation();
        
        // 启动计时器
        startTimer();
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
    
    @Override
    public void handleSubmit() {
        try {
            double userAnswer = Double.parseDouble(answerField.getText());
            attempts++;
            
            if (Math.abs(userAnswer - correctAnswer) < 0.1) {
                String currentType = getCalculationType();
                completedTypes.add(currentType);
                score += (4 - attempts); // 根据尝试次数给分
                
                if (completedTypes.size() >= TOTAL_TYPES) {
                    setFeedback("恭喜！你已完成所有计算类型。最终得分：" + score + "分（满分12分）");
                    endTask();
                } else {
                    setFeedback("回答正确！得分：" + (4 - attempts) + "分。\n" +
                               "已完成 " + completedTypes.size() + "/" + TOTAL_TYPES + " 种计算类型。");
                    startNewCalculation();
                }
            } else {
                if (attempts >= 3) {
                    String formattedAnswer = df.format(correctAnswer);
                    String currentType = getCalculationType();
                    completedTypes.add(currentType); // 即使答错也标记为完成
                    
                    if (completedTypes.size() >= TOTAL_TYPES) {
                        setFeedback("本题答错次数过多。正确答案是: " + formattedAnswer + "\n" +
                                  "所有计算类型已完成。最终得分：" + score + "分（满分12分）");
                        endTask();
                    } else {
                        setFeedback("本题答错次数过多。正确答案是: " + formattedAnswer + "\n" +
                                  "已完成 " + completedTypes.size() + "/" + TOTAL_TYPES + " 种计算类型。");
                        startNewCalculation();
                    }
                } else {
                    setFeedback("回答错误，还有" + (3 - attempts) + "次机会。");
                }
            }
        } catch (NumberFormatException e) {
            setFeedback("请输入有效的数字！");
        }
    }
    
    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            timerLabel.setText(String.format("剩余时间: %d:%02d", minutes, seconds));
            
            if (timeLeft <= 0) {
                ((Timer)e.getSource()).stop();
                answerField.setEnabled(false);
                submitButton.setEnabled(false);
                setFeedback("时间到！本次练习结束。最终得分：" + score + "分");
            }
        });
        timer.start();
    }
    
    @Override
    public void cleanup() {
        if (timer != null) {
            timer.stop();
        }
    }
    
    @Override
    public void reset() {
        timeLeft = 180;
        score = 0;
        completedCalculations = 0;
        completedTypes.clear();
        if (timer != null) {
            timer.stop();
        }
        startNewCalculation();
        startTimer();
    }
    
    @Override
    public int calculateScore() {
        return score;
    }

    @Override
    public void startTask() {
        reset();
        startTimer();
    }

    @Override
    public void pauseTask() {
        if (timer != null) {
            timer.stop();
        }
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
    }

    @Override
    public void resumeTask() {
        if (timer != null && timeLeft > 0) {
            timer.start();
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