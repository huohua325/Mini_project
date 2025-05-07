package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.AreaCalculation;
import com.shapeville.game.AreaCalculation.ShapeType;

public class AreaCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private AreaCalculation areaCalculation;
    private int currentShapeIndex = 0;
    private JLabel shapeLabel;
    private JLabel formulaLabel;
    private JLabel paramsLabel;
    private JTextField answerField;
    private JButton submitButton;

    public AreaCalculationPanel() {
        super("面积计算");
        this.areaCalculation = new AreaCalculation();
    }

    @Override
    public void initializeUI() {
        // 创建中央面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

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

        add(centerPanel, BorderLayout.CENTER);

        // 显示第一个形状
        showNextShape();
    }

    private void showNextShape() {
        if (currentShapeIndex >= 4) {
            endTask();
            return;
        }

        ShapeType shape = areaCalculation.getShapes().get(currentShapeIndex);
        shapeLabel.setText("请计算" + shape.getChinese() + "的面积");

        // 生成随机参数
        areaCalculation.generateParams(shape);

        // 显示公式和参数
        formulaLabel.setText("公式：" + areaCalculation.getFormula(shape));
        paramsLabel.setText("参数：" + areaCalculation.getParamsString());

        // 清空答案输入框
        answerField.setText("");
        answerField.requestFocus();
    }

    @Override
    public void handleSubmit() {
        String answerStr = answerField.getText().trim();
        
        try {
            double answer = Double.parseDouble(answerStr);
            incrementAttempts();

            if (areaCalculation.checkAnswer(answer)) {
                setFeedback("回答正确！");
                addAttemptToList();
                currentShapeIndex++;
                resetAttempts();
                showNextShape();
            } else if (!hasRemainingAttempts()) {
                setFeedback("已达到最大尝试次数。正确答案是：" + String.format("%.1f", areaCalculation.getCorrectArea()));
                addAttemptToList();
                currentShapeIndex++;
                resetAttempts();
                showNextShape();
            } else {
                setFeedback("回答错误，请再试一次。还剩" + getRemainingAttempts() + "次机会。");
            }
        } catch (NumberFormatException e) {
            setFeedback("请输入有效的数字！");
        }
    }

    @Override
    public void reset() {
        currentShapeIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        areaCalculation.shuffleShapes();
        showNextShape();
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
        answerField.setEnabled(false);
    }

    @Override
    public void resumeTask() {
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
    }

    @Override
    public void endTask() {
        cleanup();
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