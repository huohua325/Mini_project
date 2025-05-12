package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;
import com.shapeville.game.ShapeRecognition;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ShapePanel extends BaseTaskPanel implements TaskPanelInterface {
    private final ShapeRecognition shapeRecognition;
    private int currentShapeIndex = 0;
    private boolean is2DMode = true;
    private JLabel shapeLabel;
    private JLabel shapeImageLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton homeButton;
    private ArrayList<Shape2D> shapes2D;
    private ArrayList<Shape3D> shapes3D;
    private ArrayList<Integer> attemptsPerShape;
    private ArrayList<Boolean> correctAnswers;
    
    private static final int SHAPES_PER_TEST = 4;  // 统一2D和3D的测试数量
    private boolean isEnding = false;
    
    public ShapePanel() {
        super("形状识别");
        this.shapeRecognition = new ShapeRecognition();
        initializeShapes();
        initializeUI();
    }
    
    private void initializeShapes() {
        shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
        shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
        attemptsPerShape = new ArrayList<>();
        correctAnswers = new ArrayList<>();
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
    }
    
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        add(createTopPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        showCurrentShape();
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        
        // Home按钮
        homeButton = new JButton("返回主页");
        homeButton.addActionListener(e -> endTask());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.add(homeButton);
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // 模式选择按钮
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addModeButton(modePanel, "2D形状", true);
        addModeButton(modePanel, "3D形状", false);
        topPanel.add(modePanel, BorderLayout.CENTER);
        
        // 形状标签
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        shapeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(shapeLabel, BorderLayout.SOUTH);
        
        return topPanel;
    }
    
    private void addModeButton(JPanel panel, String text, boolean is2D) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            is2DMode = is2D;
            currentShapeIndex = 0;
            resetAttempts();
            initializeShapes();
            showCurrentShape();
        });
        panel.add(button);
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // 图片面板
        shapeImageLabel = new JLabel();
        shapeImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        imagePanel.add(shapeImageLabel, BorderLayout.CENTER);
        
        // 答案输入面板
        JPanel answerPanel = createAnswerPanel();
        
        // 添加到内容面板
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        
        gbc.gridx = 0;
        gbc.weightx = 0.6;
        contentPanel.add(imagePanel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        contentPanel.add(answerPanel, gbc);
        
        return contentPanel;
    }
    
    private JPanel createAnswerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel inputLabel = new JLabel("请输入形状的英文名称：");
        inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        answerField = new JTextField(20);
        answerField.setMaximumSize(new Dimension(200, 30));
        answerField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        submitButton = new JButton("提交答案");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> handleSubmit());
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(inputLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(answerField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(submitButton);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }
    
    private void showCurrentShape() {
        if (currentShapeIndex < shapes2D.size() && is2DMode || 
            currentShapeIndex < shapes3D.size() && !is2DMode) {
            Object shape = is2DMode ? shapes2D.get(currentShapeIndex) : shapes3D.get(currentShapeIndex);
            
            // 检查是否已经识别过这种类型
            boolean isIdentified = is2DMode ? 
                shapeRecognition.isTypeIdentified2D((Shape2D)shape) :
                shapeRecognition.isTypeIdentified3D((Shape3D)shape);
            
            if (isIdentified) {
                // 跳过已识别的形状
                currentShapeIndex++;
                showCurrentShape();
                return;
            }
            
            String chinese = is2DMode ? ((Shape2D)shape).getChinese() : ((Shape3D)shape).getChinese();
            String english = is2DMode ? ((Shape2D)shape).getEnglish() : ((Shape3D)shape).getEnglish();
            
            shapeLabel.setText("请识别这个" + (is2DMode ? "2D" : "3D") + "形状：" + chinese);
            displayShapeImage(english.toLowerCase() + ".png", is2DMode);
            answerField.setText("");
            answerField.requestFocus();
            
            // 显示剩余需要识别的类型数量
            setFeedback(shapeRecognition.getRemainingTypesMessage(is2DMode));
        }
    }
    
    private void displayShapeImage(String imageName, boolean is2D) {
        String basePath = System.getProperty("user.dir") + "/shapeville/src/main/resources/images/" + 
                         (is2D ? "2d/" : "3d/");
        try {
            File imageFile = new File(basePath + imageName);
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
                int maxSize = 160;
                
                // 计算缩放比例和新尺寸
                double scale = Math.min((double)maxSize / img.getWidth(), (double)maxSize / img.getHeight());
                int newWidth = (int)(img.getWidth() * scale);
                int newHeight = (int)(img.getHeight() * scale);
                
                Image scaledImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                shapeImageLabel.setIcon(new ImageIcon(scaledImg));
                shapeImageLabel.setPreferredSize(new Dimension(maxSize + 20, maxSize + 20));
            } else {
                shapeImageLabel.setIcon(null);
                shapeImageLabel.setText("图片未找到");
            }
        } catch (IOException e) {
            shapeImageLabel.setIcon(null);
            shapeImageLabel.setText("加载图片失败");
        }
    }
    
    @Override
    public void handleSubmit() {
        String answer = answerField.getText().trim().toLowerCase();
        Object shape = is2DMode ? shapes2D.get(currentShapeIndex) : shapes3D.get(currentShapeIndex);
        String correctAnswer = is2DMode ? ((Shape2D)shape).getEnglish().toLowerCase() : 
                                        ((Shape3D)shape).getEnglish().toLowerCase();
        boolean correct = is2DMode ? shapeRecognition.check2DAnswer((Shape2D)shape, answer) :
                                   shapeRecognition.check3DAnswer((Shape3D)shape, answer);
        
        incrementAttempts();
        
        if (correct) {
            setFeedback("回答正确！\n" + shapeRecognition.getRemainingTypesMessage(is2DMode));
            correctAnswers.add(true);
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
            setFeedback("已达到最大尝试次数。正确答案是：" + correctAnswer + "\n" + 
                       shapeRecognition.getRemainingTypesMessage(is2DMode));
            correctAnswers.add(false);
            addAttemptToList();
            currentShapeIndex++;
            resetAttempts();
            showCurrentShape();
        } else {
            setFeedback("回答错误，请再试一次。还剩" + getRemainingAttempts() + "次机会。");
        }
    }
    
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        // 只计算答对的题目的分数
        for (int i = 0; i < attemptsPerTask.size(); i++) {
            if (correctAnswers.get(i)) {  // 只有答对的题目才计分
                int attempts = attemptsPerTask.get(i);
                if (attempts == 1) totalScore += 3;      // 第1次答对得3分
                else if (attempts == 2) totalScore += 2;  // 第2次答对得2分
                else if (attempts == 3) totalScore += 1;  // 第3次答对得1分
            }
        }
        return totalScore;
    }
    
    @Override
    public String getFeedback() {
        int score = calculateScore();
        // 总分是所有尝试过的题目数量乘以3分
        int maxScore = attemptsPerTask.size() * 3;  // 每题满分3分

        StringBuilder feedback = new StringBuilder();
        feedback.append(String.format("本次测试完成！\n得分：%d（总分%d分）\n\n", score, maxScore));
        feedback.append("形状识别测试统计：\n");
        
        int totalAttempted = attemptsPerTask.size();
        int correctWithinThree = 0;
        
        for (int i = 0; i < attemptsPerTask.size(); i++) {
            if (correctAnswers.get(i)) {  // 只统计答对的题目
                correctWithinThree++;
            }
        }
        
        feedback.append(String.format("已尝试形状数量：%d\n", totalAttempted));
        feedback.append(String.format("答对数量：%d\n", correctWithinThree));
        
        double scorePercentage = maxScore > 0 ? (double)score / maxScore * 100 : 0;
        feedback.append("\n").append(scorePercentage >= 90 ? "太棒了！你对这些形状已经非常熟悉了！" :
                                   scorePercentage >= 80 ? "不错的表现！继续练习可以做得更好！" :
                                   scorePercentage >= 70 ? "继续加油！多加练习一定能提高！" :
                                                         "需要更多练习来提高判断能力！");
        
        return feedback.toString();
    }
    
    @Override
    public void endTask() {
        if (!isEnding) {
            isEnding = true;
                cleanup();
                if (parentWindow != null) {
                int score = calculateScore();
                int maxScore = attemptsPerTask.size() * 3;  // 每题满分3分
                parentWindow.showResult(score, maxScore);
                }
        }
    }
    
    @Override
    public void reset() {
        currentShapeIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        correctAnswers.clear();
        is2DMode = true;
        shapeRecognition.reset();
        initializeShapes();
        showCurrentShape();
    }
    
    @Override
    public void startTask() {
        reset();
    }
    
    @Override
    public void pauseTask() {
        submitButton.setEnabled(false);
        answerField.setEnabled(false);
        homeButton.setEnabled(false);
    }
    
    @Override
    public void resumeTask() {
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
        homeButton.setEnabled(true);
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
} 