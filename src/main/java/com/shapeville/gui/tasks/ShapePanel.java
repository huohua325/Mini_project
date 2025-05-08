package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;
import com.shapeville.game.ShapeRecognition;
import com.shapeville.utils.ShapeDrawer;

public class ShapePanel extends BaseTaskPanel implements TaskPanelInterface {
    private final ShapeRecognition shapeRecognition;
    private int currentShapeIndex = 0;
    private boolean is2DMode = true;
    private JLabel shapeLabel;
    private ShapeDrawer.ShapePanel shapeDrawingPanel;
    private JTextField answerField;
    private JButton submitButton;
    
    public ShapePanel() {
        super("Shape Recognition");
        this.shapeRecognition = new ShapeRecognition();
    }
    
    @Override
    public void initializeUI() {
        // Create mode selection buttons
        JPanel modePanel = new JPanel();
        JButton mode2DButton = new JButton("2D Shapes (Basic)");
        JButton mode3DButton = new JButton("3D Shapes (Advanced)");
        
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
        
        // Create central panel with fixed size
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setPreferredSize(new Dimension(400, 500));
        
        // Create shape display label
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(shapeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        
        // Create shape drawing panel with fixed size and border
        shapeDrawingPanel = new ShapeDrawer.ShapePanel(300, 300);
        shapeDrawingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        shapeDrawingPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        shapeDrawingPanel.setOpaque(true); // 确保面板是不透明的
        
        // Create a wrapper panel to center the shape drawing panel
        JPanel shapeWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        shapeWrapper.setOpaque(true); // 确保包装面板是不透明的
        shapeWrapper.add(shapeDrawingPanel);
        centerPanel.add(shapeWrapper);
        
        centerPanel.add(Box.createVerticalStrut(20));
        
        // Create answer input area
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(20);
        submitButton = new JButton("Submit Answer");
        
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("Enter the shape name:"));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        centerPanel.add(answerPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Show first shape immediately
        SwingUtilities.invokeLater(this::showCurrentShape);
    }
    
    private void showCurrentShape() {
        if (is2DMode && currentShapeIndex < shapeRecognition.getShapes2D().size()) {
            Shape2D shape = shapeRecognition.getShapes2D().get(currentShapeIndex);
            shapeLabel.setText("Identify this 2D shape:");
            Shape shapeObj = ShapeDrawer.create2DShape(shape.name(), 300, 300);
            shapeDrawingPanel.setShape(shapeObj);
            System.out.println("Drawing 2D shape: " + shape.name()); // 调试信息
        } else if (!is2DMode && currentShapeIndex < shapeRecognition.getShapes3D().size()) {
            Shape3D shape = shapeRecognition.getShapes3D().get(currentShapeIndex);
            shapeLabel.setText("Identify this 3D shape:");
            Shape shapeObj = ShapeDrawer.create3DShape(shape.name(), 300, 300);
            shapeDrawingPanel.setShape(shapeObj);
            System.out.println("Drawing 3D shape: " + shape.name()); // 调试信息
        }
        answerField.setText("");
        answerField.requestFocus();
        
        // Force immediate repaint
        shapeDrawingPanel.revalidate();
        shapeDrawingPanel.repaint();
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
            setFeedback("Correct!");
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
            setFeedback("Maximum attempts reached. The correct answer is: " + correctAnswer);
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
            setFeedback("Incorrect. " + getRemainingAttempts() + " attempts remaining.");
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
        if (is2DMode) {
            // Basic scoring for 2D shapes
        for (int attempts : attemptsPerTask) {
            if (attempts == 1) totalScore += 3;
            else if (attempts == 2) totalScore += 2;
            else if (attempts == 3) totalScore += 1;
        }
        return (int)((double)totalScore / (attemptsPerTask.size() * 3) * 100);
        } else {
            // Advanced scoring for 3D shapes
            for (int attempts : attemptsPerTask) {
                if (attempts == 1) totalScore += 5;
                else if (attempts == 2) totalScore += 3;
                else if (attempts == 3) totalScore += 1;
            }
            return (int)((double)totalScore / (attemptsPerTask.size() * 5) * 100);
        }
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