package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.shapeville.game.AreaCalculation;
import com.shapeville.game.AreaCalculation.ShapeType;
import com.shapeville.gui.shapes.ShapeDrawer;

public class AreaCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private AreaCalculation areaCalculation;
    private int currentShapeIndex = 0;
    private JLabel shapeLabel;
    private JLabel formulaLabel;
    private JLabel paramsLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private javax.swing.Timer timer;
    private JLabel timerLabel;
    private int remainingSeconds = 180; // 3分钟 = 180秒
    private boolean isEnding = false;
    private JPanel shapeDisplayPanel;
    private JLabel substitutionLabel;
    private JComboBox<String> shapeSelector;
    private Set<ShapeType> completedShapes;
    private Map<String, Double> currentParams;
    private int attemptCount = 0;
    private static final int MAX_ATTEMPTS = 3;

    public AreaCalculationPanel() {
        super("面积计算");
        this.areaCalculation = new AreaCalculation();
        this.completedShapes = new HashSet<>();
        this.currentParams = new HashMap<>();
        initializeTimer();
        setupAreaUI();
    }

    private void initializeTimer() {
        timerLabel = new JLabel("剩余时间: 3:00");
        timerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        timer = new javax.swing.Timer(1000, e -> updateTimer());
    }

    private void updateTimer() {
        remainingSeconds--;
        if (remainingSeconds >= 0) {
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            timerLabel.setText(String.format("剩余时间: %d:%02d", minutes, seconds));
        } else {
            timer.stop();
            endTask();
        }
    }

    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
    }
    
    private void setupAreaUI() {
        setLayout(new BorderLayout(10, 10));

        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Home按钮
        homeButton = new JButton("返回主页");
        homeButton.addActionListener(e -> endTask());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // 计时器
        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerPanel.add(timerLabel);
        topPanel.add(timerPanel, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);

        // 创建中央面板（使用JScrollPane包装）
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // 创建形状选择下拉框
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        shapeSelector = new JComboBox<>(areaCalculation.getShapes().stream()
                .map(ShapeType::getChinese)
                .toArray(String[]::new));
        shapeSelector.addActionListener(e -> {
            if (shapeSelector.getSelectedIndex() != -1) {
                currentShapeIndex = shapeSelector.getSelectedIndex();
                showSelectedShape();
            }
        });
        selectorPanel.add(new JLabel("请选择要计算的形状："));
        selectorPanel.add(shapeSelector);
        centerPanel.add(selectorPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 创建形状显示标签
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 创建公式显示标签
        formulaLabel = new JLabel("", SwingConstants.CENTER);
        formulaLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        formulaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(formulaLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        // 创建参数显示标签
        paramsLabel = new JLabel("", SwingConstants.CENTER);
        paramsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        paramsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(paramsLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 创建代入计算过程标签
        substitutionLabel = new JLabel("", SwingConstants.CENTER);
        substitutionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        substitutionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        substitutionLabel.setVisible(false);
        centerPanel.add(substitutionLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 创建答案输入区域
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(10);
        submitButton = new JButton("提交答案");
        
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("请输入面积（保留1位小数）："));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        answerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(answerPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // 创建形状显示面板
        shapeDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCurrentShape((Graphics2D) g);
            }
        };
        shapeDisplayPanel.setPreferredSize(new Dimension(400, 300));
        shapeDisplayPanel.setBackground(Color.WHITE);
        shapeDisplayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeDisplayPanel);

        // 将中央面板放入滚动面板
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // 显示第一个形状
        showSelectedShape();
        
        // 启动计时器
        timer.start();
    }

    private void lockCurrentShape() {
        answerField.setEnabled(false);
        submitButton.setEnabled(false);
        setFeedback("请选择其他形状继续练习");
    }

    private void showSelectedShape() {
        ShapeType shape = areaCalculation.getShapes().get(currentShapeIndex);
        
        // 如果这个形状已经完成，提示用户选择其他形状
        if (completedShapes.contains(shape)) {
            setFeedback("这个形状已经完成，请选择其他形状。");
            lockCurrentShape();
            return;
        }

        shapeLabel.setText("请计算" + shape.getChinese() + "的面积");

        // 生成随机参数
        areaCalculation.generateParams(shape);

        // 更新当前参数
        currentParams.clear();
        currentParams.putAll(areaCalculation.getCurrentParams());

        // 显示公式和参数
        formulaLabel.setText("公式：" + areaCalculation.getFormula(shape));
        paramsLabel.setText("参数：" + areaCalculation.getParamsString());

        // 隐藏代入计算过程
        substitutionLabel.setVisible(false);

        // 重置尝试次数
        attemptCount = 0;

        // 清空答案输入框并启用
        answerField.setText("");
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
        answerField.requestFocus();

        // 重绘形状
        shapeDisplayPanel.repaint();
    }

    private void drawCurrentShape(Graphics2D g2d) {
        ShapeType shape = areaCalculation.getShapes().get(currentShapeIndex);
        int width = shapeDisplayPanel.getWidth();
        int height = shapeDisplayPanel.getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        
        boolean showDimensions = completedShapes.contains(shape) || attemptCount >= MAX_ATTEMPTS;
        
        switch (shape) {
            case RECTANGLE:
                ShapeDrawer.drawRectangle(g2d, centerX - 100, centerY - 75, 200, 150, currentParams, showDimensions);
                break;
            case PARALLELOGRAM:
                ShapeDrawer.drawParallelogram(g2d, centerX - 100, centerY - 75, 200, 150, 50, currentParams, showDimensions);
                break;
            case TRIANGLE:
                ShapeDrawer.drawTriangle(g2d, centerX, centerY, 200, 150, currentParams, showDimensions);
                break;
            case TRAPEZIUM:
                ShapeDrawer.drawTrapezium(g2d, centerX, centerY, 150, 200, 150, currentParams, showDimensions);
                break;
        }
    }

    @Override
    public void handleSubmit() {
        try {
            double answer = Double.parseDouble(answerField.getText());
            attemptCount++;
            
            if (areaCalculation.checkAnswer(answer)) {
                // 答案正确
                completedShapes.add(areaCalculation.getShapes().get(currentShapeIndex));
                substitutionLabel.setText(areaCalculation.getSubstitutionString(areaCalculation.getShapes().get(currentShapeIndex)));
                substitutionLabel.setVisible(true);
                setFeedback("回答正确！请选择其他形状继续练习，需要完成所有四个形状的练习。");
                shapeDisplayPanel.repaint();
                lockCurrentShape();
                
                // 检查是否完成所有形状
                if (completedShapes.size() >= areaCalculation.getShapes().size()) {
                    timer.stop();
                    endTask();
                }
            } else {
                // 答案错误
                if (attemptCount >= MAX_ATTEMPTS) {
                    // 用完所有尝试机会
                    substitutionLabel.setText(areaCalculation.getSubstitutionString(areaCalculation.getShapes().get(currentShapeIndex)));
                    substitutionLabel.setVisible(true);
                    setFeedback("已用完3次机会。正确答案是：" + String.format("%.1f", areaCalculation.getCorrectArea()) + "\n请选择其他形状继续练习。");
                    shapeDisplayPanel.repaint();
                    completedShapes.add(areaCalculation.getShapes().get(currentShapeIndex));
                    lockCurrentShape();
                    
                    // 检查是否完成所有形状
                    if (completedShapes.size() >= areaCalculation.getShapes().size()) {
                        timer.stop();
                        endTask();
                    }
                } else {
                    setFeedback("答案错误，还有" + (MAX_ATTEMPTS - attemptCount) + "次机会");
                }
            }
        } catch (NumberFormatException e) {
            setFeedback("请输入有效的数字（保留1位小数）");
        }
    }

    @Override
    protected void setFeedback(String message) {
        if (parentWindow != null) {
            parentWindow.setFeedback(message);
        }
    }

    @Override
    public void endTask() {
        if (!isEnding) {
            isEnding = true;
            timer.stop();
            int score = calculateScore();
            String feedback = getFeedback();
            if (parentWindow != null) {
                // 在显示结果窗口之前更新反馈区
                setFeedback(feedback);
                parentWindow.showResult(score, areaCalculation.getShapes().size() * 3);
            }
        }
    }

    @Override
    protected int calculateScore() {
        return completedShapes.size() * 3;  // 每个正确的形状得3分
    }

    @Override
    public int getScore() {
        return calculateScore();
    }

    @Override
    public String getFeedback() {
        int score = calculateScore();
        int maxScore = areaCalculation.getShapes().size() * 3;  // 每个形状满分3分

        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("本次测试完成！\n得分：%d（总分%d分）\n\n", score, maxScore));
        feedback.append("面积计算测试统计：\n");
        
        feedback.append(String.format("已完成形状数量：%d\n", completedShapes.size()));
        feedback.append(String.format("总形状数量：%d\n", areaCalculation.getShapes().size()));
        
        double scorePercentage = maxScore > 0 ? (double)score / maxScore * 100 : 0;
        feedback.append("\n").append(scorePercentage >= 90 ? "太棒了！你对这些形状的面积计算已经非常熟练了！" :
                                   scorePercentage >= 80 ? "不错的表现！继续练习可以做得更好！" :
                                   scorePercentage >= 70 ? "继续加油！多加练习一定能提高！" :
                                                         "需要更多练习来提高计算能力！");
        
        return feedback.toString();
    }

    @Override
    public void startTask() {
        reset();
        timer.start();
    }

    @Override
    public void pauseTask() {
        if (timer != null) {
            timer.stop();
        }
        if (submitButton != null) {
            submitButton.setEnabled(false);
        }
        if (answerField != null) {
            answerField.setEnabled(false);
        }
        if (homeButton != null) {
            homeButton.setEnabled(false);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(false);
        }
    }

    @Override
    public void resumeTask() {
        if (timer != null) {
            timer.start();
        }
        if (submitButton != null) {
            submitButton.setEnabled(true);
        }
        if (answerField != null) {
            answerField.setEnabled(true);
        }
        if (homeButton != null) {
            homeButton.setEnabled(true);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(true);
        }
    }

    @Override
    public void reset() {
        currentShapeIndex = 0;
        attemptCount = 0;
        remainingSeconds = 180;
        isEnding = false;
        completedShapes.clear();
        currentParams.clear();
        showSelectedShape();
        timer.restart();
    }
} 