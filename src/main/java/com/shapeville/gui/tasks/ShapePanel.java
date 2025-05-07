package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;
import com.shapeville.game.ShapeRecognition;

public class ShapePanel extends BaseTaskPanel implements TaskPanelInterface {
    private final ShapeRecognition shapeRecognition;
    private int currentShapeIndex = 0;
    private boolean is2DMode = true;
    private JLabel shapeLabel;
    private JTextField answerField;
    private JButton submitButton;
    
    public ShapePanel() {
        super("形状识别");
        this.shapeRecognition = new ShapeRecognition();
    }
    
    @Override
    public void initializeUI() {
        // 创建模式选择按钮
        JPanel modePanel = new JPanel();
        JButton mode2DButton = new JButton("2D形状");
        JButton mode3DButton = new JButton("3D形状");
        
        mode2DButton.addActionListener(e -> {
            is2DMode = true;
            currentShapeIndex = 0;
            resetAttempts();
            shapeRecognition.reset();
            showCurrentShape();
        });
        
        mode3DButton.addActionListener(e -> {
            is2DMode = false;
            currentShapeIndex = 0;
            resetAttempts();
            shapeRecognition.reset();
            showCurrentShape();
        });
        
        modePanel.add(mode2DButton);
        modePanel.add(mode3DButton);
        add(modePanel, BorderLayout.NORTH);
        
        // 创建中央面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // 创建形状显示标签
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);
        
        // 创建答案输入区域
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(20);
        submitButton = new JButton("提交答案");
        
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("请输入形状的英文名称："));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        centerPanel.add(answerPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // 显示第一个形状
        showCurrentShape();
    }
    
    private void showCurrentShape() {
        if (is2DMode && currentShapeIndex < shapeRecognition.getShapes2D().size()) {
            Shape2D shape = shapeRecognition.getShapes2D().get(currentShapeIndex);
            shapeLabel.setText("请识别这个2D形状：" + shape.getChinese());
        } else if (!is2DMode && currentShapeIndex < shapeRecognition.getShapes3D().size()) {
            Shape3D shape = shapeRecognition.getShapes3D().get(currentShapeIndex);
            shapeLabel.setText("请识别这个3D形状：" + shape.getChinese());
        }
        answerField.setText("");
        answerField.requestFocus();
    }
    
    @Override
    public void handleSubmit() {
        String answer = answerField.getText().trim().toLowerCase();
        boolean correct = false;
        String correctAnswer = "";
        
        if (is2DMode && currentShapeIndex < shapeRecognition.getShapes2D().size()) {
            Shape2D shape = shapeRecognition.getShapes2D().get(currentShapeIndex);
            correct = shapeRecognition.check2DAnswer(shape, answer);
            correctAnswer = shape.getEnglish();
            if (correct) {
                shapeRecognition.incrementCorrectCount2D();
            }
        } else if (!is2DMode && currentShapeIndex < shapeRecognition.getShapes3D().size()) {
            Shape3D shape = shapeRecognition.getShapes3D().get(currentShapeIndex);
            correct = shapeRecognition.check3DAnswer(shape, answer);
            correctAnswer = shape.getEnglish();
            if (correct) {
                shapeRecognition.incrementCorrectCount3D();
            }
        }
        
        incrementAttempts();
        
        if (correct) {
            setFeedback("回答正确！");
            addAttemptToList();
            currentShapeIndex++;
            resetAttempts();
            
            if ((is2DMode && shapeRecognition.is2DComplete()) || 
                (!is2DMode && shapeRecognition.is3DComplete())) {
                endTask();
                return;
            }
            showCurrentShape();
        } else if (!hasRemainingAttempts()) {
            setFeedback("已达到最大尝试次数。正确答案是：" + correctAnswer);
            addAttemptToList();
            currentShapeIndex++;
            resetAttempts();
            
            if ((is2DMode && shapeRecognition.is2DComplete()) || 
                (!is2DMode && shapeRecognition.is3DComplete())) {
                endTask();
                return;
            }
            showCurrentShape();
        } else {
            setFeedback("回答错误，请再试一次。还剩" + getRemainingAttempts() + "次机会。");
        }
    }
    
    @Override
    public void reset() {
        currentShapeIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        is2DMode = true;
        shapeRecognition.reset();
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