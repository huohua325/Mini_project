package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.CompoundShapeCalculation;
import com.shapeville.game.CompoundShapeCalculation.CompoundShape;
import java.util.List;

public class CompoundShapeCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private final CompoundShapeCalculation compoundCalculation;
    private int currentShapeIndex = 0;
    private JLabel shapeLabel;
    private JTextArea descriptionArea;
    private JTextField answerField;
    private JButton submitButton;
    private JTextArea solutionArea;
    private JComboBox<String> shapeSelector;
    
    public CompoundShapeCalculationPanel() {
        super("复合形状计算");
        this.compoundCalculation = new CompoundShapeCalculation();
    }
    
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建形状选择器
        List<CompoundShape> shapes = compoundCalculation.getShapes();
        String[] shapeNames = shapes.stream()
                                  .map(CompoundShape::getName)
                                  .toArray(String[]::new);
        shapeSelector = new JComboBox<>(shapeNames);
        shapeSelector.addActionListener(e -> {
            if (!compoundCalculation.getPracticed().contains(shapeSelector.getSelectedIndex())) {
                currentShapeIndex = shapeSelector.getSelectedIndex();
                resetAttempts();
                showCurrentShape();
            }
        });
        topPanel.add(shapeSelector, BorderLayout.NORTH);
        
        // 创建形状标签
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(shapeLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 创建中央面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // 创建描述区域
        descriptionArea = new JTextArea(3, 40);
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        descriptionArea.setBackground(new Color(240, 240, 240));
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("形状描述"));
        centerPanel.add(descriptionScroll);
        
        // 创建答案输入区域
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(15);
        submitButton = new JButton("提交答案");
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("请输入面积（保留1位小数）："));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(answerPanel);
        
        // 创建解答区域
        solutionArea = new JTextArea(4, 40);
        solutionArea.setEditable(false);
        solutionArea.setWrapStyleWord(true);
        solutionArea.setLineWrap(true);
        solutionArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        solutionArea.setBackground(new Color(240, 240, 240));
        JScrollPane solutionScroll = new JScrollPane(solutionArea);
        solutionScroll.setBorder(BorderFactory.createTitledBorder("解题步骤"));
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(solutionScroll);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // 显示第一个形状
        showCurrentShape();
    }
    
    private void showCurrentShape() {
        List<CompoundShape> shapes = compoundCalculation.getShapes();
        if (currentShapeIndex < shapes.size()) {
            CompoundShape shape = shapes.get(currentShapeIndex);
            shapeLabel.setText(shape.getName());
            descriptionArea.setText(shape.getDescription());
            solutionArea.setText("");  // 清空解答
            answerField.setText("");
            answerField.requestFocus();
            shapeSelector.setSelectedIndex(currentShapeIndex);
        } else {
            endTask();
        }
    }
    
    @Override
    public void handleSubmit() {
        String answerStr = answerField.getText().trim();
        
        try {
            double answer = Double.parseDouble(answerStr);
            incrementAttempts();
            
            if (compoundCalculation.checkAnswer(currentShapeIndex, answer)) {
                setFeedback("回答正确！");
                solutionArea.setText(compoundCalculation.getShapes().get(currentShapeIndex).getSolution());
                compoundCalculation.addPracticed(currentShapeIndex);
                addAttemptToList();
                
                if (compoundCalculation.isComplete()) {
                    endTask();
                    return;
                }
                
                // 找到下一个未完成的形状
                do {
                    currentShapeIndex++;
                    if (currentShapeIndex >= compoundCalculation.getShapes().size()) {
                        endTask();
                        return;
                    }
                } while (compoundCalculation.getPracticed().contains(currentShapeIndex));
                
                resetAttempts();
                showCurrentShape();
            } else if (!hasRemainingAttempts()) {
                CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
                setFeedback("已达到最大尝试次数。正确答案是：" + String.format("%.1f", shape.getCorrectArea()));
                solutionArea.setText(shape.getSolution());
                compoundCalculation.addPracticed(currentShapeIndex);
                addAttemptToList();
                
                if (compoundCalculation.isComplete()) {
                    endTask();
                    return;
                }
                
                // 找到下一个未完成的形状
                do {
                    currentShapeIndex++;
                    if (currentShapeIndex >= compoundCalculation.getShapes().size()) {
                        endTask();
                        return;
                    }
                } while (compoundCalculation.getPracticed().contains(currentShapeIndex));
                
                resetAttempts();
                showCurrentShape();
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
        compoundCalculation.reset();
        showCurrentShape();
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
        shapeSelector.setEnabled(false);
    }
    
    @Override
    public void resumeTask() {
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
        shapeSelector.setEnabled(true);
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
