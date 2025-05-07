package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TaskWindow extends JFrame {
    private String taskName;
    private JTextArea taskDescription;
    private JPanel inputPanel;
    private JLabel timerLabel;
    private JTextArea feedbackArea;
    private javax.swing.Timer timer;
    private int seconds = 0;
    
    // 形状识别相关变量
    private ArrayList<Shape2D> shapes2D;
    private ArrayList<Shape3D> shapes3D;
    private int currentShapeIndex = 0;
    private int attempts = 0;
    private boolean is2DMode = true;
    private JLabel shapeLabel;
    private JTextField answerField;
    private JButton submitButton;
    private ArrayList<Integer> attemptsPerShape;
    
    // 每轮测试的形状数量
    private static final int SHAPES_PER_TEST_2D = 5;  // 每次测试5个2D形状
    private static final int SHAPES_PER_TEST_3D = 4;  // 每次测试4个3D形状
    
    public TaskWindow(String taskName) {
        this.taskName = taskName;
        initializeUI();
        if (taskName.equals("形状识别")) {
            setupShapeRecognition();
        }
        startTimer();
    }
    
    private void initializeUI() {
        setTitle("Shapeville - " + taskName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建任务说明区域
        taskDescription = new JTextArea();
        taskDescription.setEditable(false);
        taskDescription.setWrapStyleWord(true);
        taskDescription.setLineWrap(true);
        taskDescription.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(taskDescription);
        scrollPane.setPreferredSize(new Dimension(750, 100));
        mainPanel.add(scrollPane, BorderLayout.NORTH);
        
        // 创建输入面板
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // 创建计时器标签
        timerLabel = new JLabel("用时: 0:00");
        timerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        // 创建反馈区域
        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setLineWrap(true);
        feedbackArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(750, 100));
        
        // 创建底部控制面板
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(timerLabel, BorderLayout.WEST);
        controlPanel.add(feedbackScroll, BorderLayout.CENTER);
        
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupShapeRecognition() {
        // 初始化形状列表
        shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
        shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
        attemptsPerShape = new ArrayList<>();
        
        // 创建模式选择按钮
        JPanel modePanel = new JPanel();
        JButton mode2DButton = new JButton("2D形状");
        JButton mode3DButton = new JButton("3D形状");
        
        mode2DButton.addActionListener(e -> {
            is2DMode = true;
            currentShapeIndex = 0;
            attempts = 0;
            attemptsPerShape.clear();
            showCurrentShape();
        });
        
        mode3DButton.addActionListener(e -> {
            is2DMode = false;
            currentShapeIndex = 0;
            attempts = 0;
            attemptsPerShape.clear();
            showCurrentShape();
        });
        
        modePanel.add(mode2DButton);
        modePanel.add(mode3DButton);
        inputPanel.add(modePanel);
        
        // 创建形状显示标签
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        shapeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.add(shapeLabel);
        
        // 创建答案输入区域
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(20);
        submitButton = new JButton("提交答案");
        
        submitButton.addActionListener(e -> handleShapeAnswer());
        
        answerPanel.add(new JLabel("请输入形状的英文名称："));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        inputPanel.add(answerPanel);
        
        // 显示第一个形状
        showCurrentShape();
    }
    
    private void showCurrentShape() {
        if (is2DMode && currentShapeIndex < SHAPES_PER_TEST_2D) {
            Shape2D shape = shapes2D.get(currentShapeIndex);
            shapeLabel.setText("请识别这个2D形状：" + shape.getChinese());
        } else if (!is2DMode && currentShapeIndex < SHAPES_PER_TEST_3D) {
            Shape3D shape = shapes3D.get(currentShapeIndex);
            shapeLabel.setText("请识别这个3D形状：" + shape.getChinese());
        }
        answerField.setText("");
        answerField.requestFocus();
    }
    
    private void handleShapeAnswer() {
        String answer = answerField.getText().trim().toLowerCase();
        boolean correct = false;
        String correctAnswer = "";
        
        if (is2DMode && currentShapeIndex < SHAPES_PER_TEST_2D) {
            Shape2D shape = shapes2D.get(currentShapeIndex);
            correct = answer.equals(shape.getEnglish().toLowerCase());
            correctAnswer = shape.getEnglish();
        } else if (!is2DMode && currentShapeIndex < SHAPES_PER_TEST_3D) {
            Shape3D shape = shapes3D.get(currentShapeIndex);
            correct = answer.equals(shape.getEnglish().toLowerCase());
            correctAnswer = shape.getEnglish();
        }
        
        attempts++;
        
        if (correct) {
            feedbackArea.setText("回答正确！");
            attemptsPerShape.add(attempts);
            currentShapeIndex++;
            attempts = 0;
            
            if ((is2DMode && currentShapeIndex >= SHAPES_PER_TEST_2D) || 
                (!is2DMode && currentShapeIndex >= SHAPES_PER_TEST_3D)) {
                timer.stop();
                showResult();
                return;
            }
            showCurrentShape();
        } else if (attempts >= 3) {
            feedbackArea.setText("已达到最大尝试次数。正确答案是：" + correctAnswer);
            attemptsPerShape.add(attempts);
            currentShapeIndex++;
            attempts = 0;
            
            if ((is2DMode && currentShapeIndex >= SHAPES_PER_TEST_2D) || 
                (!is2DMode && currentShapeIndex >= SHAPES_PER_TEST_3D)) {
                timer.stop();
                showResult();
                return;
            }
            showCurrentShape();
        } else {
            feedbackArea.setText("回答错误，请再试一次。还剩" + (3 - attempts) + "次机会。");
        }
    }
    
    private void showResult() {
        int score = calculateScore();
        String feedback = generateFeedback(score);
        UIManager.getInstance().showResult(taskName, score, feedback);
        dispose();
    }
    
    private String generateFeedback(int score) {
        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("本次测试完成！\n总分：%d\n\n", score));
        
        if (is2DMode) {
            feedback.append("2D形状识别测试统计：\n");
        } else {
            feedback.append("3D形状识别测试统计：\n");
        }
        
        feedback.append(String.format("测试形状数量：%d\n", attemptsPerShape.size()));
        
        int perfectCount = 0;
        for (int attempts : attemptsPerShape) {
            if (attempts == 1) perfectCount++;
        }
        
        feedback.append(String.format("一次答对数量：%d\n", perfectCount));
        
        if (score >= 90) {
            feedback.append("\n太棒了！你对这些形状已经非常熟悉了！");
        } else if (score >= 70) {
            feedback.append("\n不错的表现！继续练习可以做得更好！");
        } else {
            feedback.append("\n继续加油！多加练习一定能提高！");
        }
        
        return feedback.toString();
    }
    
    private int calculateScore() {
        int totalScore = 0;
        for (int attempts : attemptsPerShape) {
            if (attempts == 1) totalScore += 3;
            else if (attempts == 2) totalScore += 2;
            else if (attempts == 3) totalScore += 1;
        }
        int maxPossibleScore = attemptsPerShape.size() * 3;
        return (int)((double)totalScore / maxPossibleScore * 100);
    }
    
    private void startTimer() {
        timer = new javax.swing.Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                updateTimerLabel();
            }
        });
        timer.start();
    }
    
    private void updateTimerLabel() {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        timerLabel.setText(String.format("用时: %d:%02d", minutes, secs));
    }
    
    public void setTaskDescription(String description) {
        taskDescription.setText(description);
    }
    
    public void setFeedback(String feedback) {
        feedbackArea.setText(feedback);
    }
    
    public void cleanup() {
        if (timer != null) {
            timer.stop();
        }
    }
    
    @Override
    public void dispose() {
        cleanup();
        super.dispose();
    }
}
