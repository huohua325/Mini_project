package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.CompoundShapeCalculation;
import com.shapeville.game.CompoundShapeCalculation.CompoundShape;
import com.shapeville.gui.shapes.ShapeRenderer;
import com.shapeville.gui.UIManager;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompoundShapeCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private static final int MAX_ATTEMPTS = 3;  // 每题最多3次尝试机会
    private static final int TIME_PER_QUESTION = 5 * 60; // 每道题5分钟时间限制（秒）
    private final CompoundShapeCalculation compoundCalculation;
    private int currentShapeIndex = 0;
    private List<Boolean> correctAnswers;  // 记录每题是否答对
    private JLabel shapeLabel;
    private JTextArea descriptionArea;
    private JTextField answerField;
    private JButton submitButton;
    private JTextArea solutionArea;
    private JComboBox<String> shapeSelector;
    private ShapeDisplayPanel shapeDisplayPanel;
    private Timer questionTimer; // 每道题的计时器
    private int remainingTime;   // 当前题目的剩余时间
    private JLabel timerLabel;   // 计时器显示标签
    private JButton nextButton; // 添加"下一题"按钮
    
    public CompoundShapeCalculationPanel() {
        super("复合形状计算");
        try {
            this.compoundCalculation = new CompoundShapeCalculation();
            this.correctAnswers = new ArrayList<>();
            
            // 确保形状列表已经初始化
            if (this.compoundCalculation.getShapes().isEmpty()) {
                throw new IllegalStateException("形状列表为空");
            }
            
            // 在确认有形状后再初始化UI
            initializeUI();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                "初始化复合形状失败：" + e.getMessage(),
                "错误",
                JOptionPane.ERROR_MESSAGE);
            throw e;  // 重新抛出异常，让上层处理
        }
    }
    
    @Override
    public void initializeUI() {
        if (compoundCalculation == null || compoundCalculation.getShapes().isEmpty()) {
            setLayout(new BorderLayout());
            JLabel errorLabel = new JLabel("暂无可用的复合形状", SwingConstants.CENTER);
            errorLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            add(errorLabel, BorderLayout.CENTER);
            return;
        }
        
        // 首先初始化基本组件
        submitButton = new JButton("提交答案");
        answerField = new JTextField(15);
        shapeLabel = new JLabel("", SwingConstants.CENTER);
        descriptionArea = new JTextArea(3, 30);
        solutionArea = new JTextArea(4, 30);
        nextButton = new JButton("下一题"); 
        nextButton.setVisible(false); // 初始时不可见
        nextButton.addActionListener(e -> goToNextQuestion()); // 添加下一题的动作
        
        // 设置布局
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 添加计时器标签
        timerLabel = new JLabel("剩余时间: 5:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        timerLabel.setForeground(Color.BLUE);
        topPanel.add(timerLabel, BorderLayout.EAST);
        
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
        
        // 设置形状标签
        shapeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(shapeLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 创建中央面板
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建形状显示面板
        shapeDisplayPanel = new ShapeDisplayPanel();
        
        // 创建显示容器
        JPanel displayContainer = new JPanel(new BorderLayout());
        displayContainer.setBorder(BorderFactory.createTitledBorder("形状显示"));
        displayContainer.setBackground(Color.WHITE);
        
        // 创建滚动面板并添加形状显示面板
        JScrollPane scrollPane = new JScrollPane(shapeDisplayPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // 设置首选大小
        Dimension displaySize = new Dimension(500, 400);
        shapeDisplayPanel.setPreferredSize(displaySize);
        displayContainer.setPreferredSize(displaySize);
        
        // 添加滚动面板到显示容器
        displayContainer.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(displayContainer, BorderLayout.CENTER);
        
        // 创建右侧信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        // 设置描述区域
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        descriptionArea.setBackground(new Color(240, 240, 240));
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("形状描述"));
        infoPanel.add(descriptionScroll);
        
        // 创建答案输入区域
        JPanel answerPanel = new JPanel();
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("请输入面积（保留1位小数）："));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        answerPanel.add(nextButton); // 添加下一题按钮到面板
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(answerPanel);
        
        // 设置解答区域
        solutionArea.setEditable(false);
        solutionArea.setWrapStyleWord(true);
        solutionArea.setLineWrap(true);
        solutionArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        solutionArea.setBackground(new Color(240, 240, 240));
        JScrollPane solutionScroll = new JScrollPane(solutionArea);
        solutionScroll.setBorder(BorderFactory.createTitledBorder("解题步骤"));
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(solutionScroll);
        
        centerPanel.add(infoPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
        
        // 显示第一个形状
        showCurrentShape();
        
        // 设置初始反馈信息
        setFeedback("请输入答案并点击提交按钮。");
        
        // 确保所有组件都可见
        setVisible(true);
        revalidate();
        repaint();
    }
    
    private void showCurrentShape() {
        List<CompoundShape> shapes = compoundCalculation.getShapes();
        
        if (currentShapeIndex < shapes.size()) {
            CompoundShape shape = shapes.get(currentShapeIndex);
            
            shapeLabel.setText(shape.getName());
            descriptionArea.setText(shape.getDescription());
            solutionArea.setText("");  // 清空解答
            solutionArea.setVisible(true); // 确保解答区域可见
            answerField.setText("");
            answerField.setEnabled(true); // 确保输入框可用
            answerField.requestFocus();
            submitButton.setEnabled(true); // 确保提交按钮可用
            nextButton.setVisible(false); // 隐藏下一题按钮
            shapeSelector.setSelectedIndex(currentShapeIndex);
            
            // 设置形状并强制重绘
            if (shapeDisplayPanel != null) {
                shapeDisplayPanel.setCurrentShape(shape.getRenderer());
                shapeDisplayPanel.setVisible(true);
                shapeDisplayPanel.revalidate();
                shapeDisplayPanel.repaint();
                
                // 确保父容器也被重绘
                Container parent = shapeDisplayPanel.getParent();
                if (parent != null) {
                    parent.revalidate();
                    parent.repaint();
                }
            }
            
            // 开始当前题目的计时
            startQuestionTimer();
        } else {
            endTask();
        }
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
        
        // 直接显示解题步骤
        CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
        String solution = shape.getSolution();
        if (solution == null || solution.isEmpty()) {
            solution = "此题暂无详细解题步骤";
        }
        solutionArea.setText(solution);
        solutionArea.revalidate();
        solutionArea.repaint();
        
        // 当前题目视为3次回答错误
        completedCurrentQuestion(false);
    }
    
    // 标记当前题目完成并进入下一题
    private void completedCurrentQuestion(boolean correct) {
        // 停止当前题目计时器
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
        
        // 记录答题结果
        correctAnswers.add(correct);
        
        // 显示解题步骤
        CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
        String feedback = "";
        if (!correct) {
            feedback = "时间到或三次回答错误。正确答案是：" + String.format("%.1f", shape.getCorrectArea()) + 
                       "\n本题得分：0分\n让我们看看详细的解题步骤：";
        } else {
            int currentAttempts = getAttempts();
            int points = currentAttempts == 1 ? 6 : 
                        currentAttempts == 2 ? 4 : 
                        currentAttempts == 3 ? 2 : 0;
            feedback = String.format("答案正确！\n本题得分：%d分\n让我们看看详细的解题步骤：", points);
        }
        setFeedback(feedback);
        
        // 确保解题步骤不为空
        String solution = shape.getSolution();
        if (solution == null || solution.isEmpty()) {
            solution = "此题暂无详细解题步骤";
        }
        solutionArea.setText(solution);
        
        // 强制更新UI以确保解题步骤显示
        solutionArea.revalidate();
        solutionArea.repaint();
        
        // 标记当前形状已练习
        compoundCalculation.addPracticed(currentShapeIndex);
        addAttemptToList();
        
        // 检查是否完成所有形状
        if (compoundCalculation.isComplete()) {
            JOptionPane.showMessageDialog(this, 
                "您已完成所有复合形状的练习。", 
                "任务完成", 
                JOptionPane.INFORMATION_MESSAGE);
            endTask();
            return;
        }
        
        // 禁用提交按钮和输入框，显示下一题按钮
        submitButton.setEnabled(false);
        answerField.setEnabled(false);
        nextButton.setVisible(true);
    }
    
    // 添加进入下一题的方法
    private void goToNextQuestion() {
        // 找到下一个未完成的形状
        do {
            currentShapeIndex++;
            if (currentShapeIndex >= compoundCalculation.getShapes().size()) {
                endTask();
                return;
            }
        } while (compoundCalculation.getPracticed().contains(currentShapeIndex));
        
        // 重置按钮状态和尝试次数
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
        nextButton.setVisible(false);
        resetAttempts();
        
        // 显示新题目
        showCurrentShape();
    }
    
    // 形状显示面板内部类
    private static class ShapeDisplayPanel extends JPanel {
        private ShapeRenderer currentShape;
        private static final int MARGIN = 40;
        
        public ShapeDisplayPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(500, 400));
            setMinimumSize(new Dimension(500, 400));
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setOpaque(true);
        }
        
        public void setCurrentShape(ShapeRenderer shape) {
            this.currentShape = shape;
            
            // 如果形状改变，可能需要调整面板大小
            if (shape != null) {
                // 为形状预留足够的空间
                int width = Math.max(500, getPreferredSize().width);
                int height = Math.max(400, getPreferredSize().height);
                setPreferredSize(new Dimension(width, height));
            }
            
            revalidate();
            repaint();
        }
        
        @Override
        public Dimension getPreferredSize() {
            if (currentShape != null) {
                // 根据形状的实际大小调整面板大小
                return new Dimension(500 + 2 * MARGIN, 400 + 2 * MARGIN);
            }
            return new Dimension(500, 400);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // 清除背景
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            
            if (currentShape != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                try {
                    // 设置绘图质量
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // 计算绘制区域
                    int drawingWidth = getWidth() - 2 * MARGIN;
                    int drawingHeight = getHeight() - 2 * MARGIN;
                    
                    // 创建绘制区域
                    g2d.translate(MARGIN, MARGIN);
                    currentShape.draw(g2d, drawingWidth, drawingHeight);
                    currentShape.drawDimensions(g2d, drawingWidth, drawingHeight);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    g2d.dispose();
                }
            } else {
                g.setColor(Color.GRAY);
                g.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                String message = "暂无形状显示";
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(message)) / 2;
                int y = (getHeight() + fm.getHeight()) / 2;
                g.drawString(message, x, y);
            }
        }
    }
    
    @Override
    public void handleSubmit() {
        String answerStr = answerField.getText().trim();
        
        try {
            double answer = Double.parseDouble(answerStr);
            incrementAttempts();
            
            if (compoundCalculation.checkAnswer(currentShapeIndex, answer)) {
                // 答对了，记录正确答案
                // 根据尝试次数显示不同的得分反馈
                int currentAttempts = getAttempts();
                int points = currentAttempts == 1 ? 6 : 
                            currentAttempts == 2 ? 4 : 
                            currentAttempts == 3 ? 2 : 0;
                String feedback = String.format("太棒了！答案正确！\n本题得分：%d分", points);
                setFeedback(feedback);
                
                // 显示解题步骤
                CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
                String solution = shape.getSolution();
                if (solution == null || solution.isEmpty()) {
                    solution = "此题暂无详细解题步骤";
                }
                solutionArea.setText(solution);
                solutionArea.revalidate();
                solutionArea.repaint();
                
                // 禁用提交按钮和输入框
                submitButton.setEnabled(false);
                answerField.setEnabled(false);
                
                // 完成当前题目，标记为正确
                completedCurrentQuestion(true);
                
            } else if (!hasRemainingAttempts()) {
                // 三次都答错了，记录错误答案
                CompoundShape shape = compoundCalculation.getShapes().get(currentShapeIndex);
                String feedback = "很遗憾，三次机会已用完。正确答案是：" + String.format("%.1f", shape.getCorrectArea()) + 
                           "\n本题得分：0分\n让我们看看详细的解题步骤：";
                setFeedback(feedback);
                
                // 提前显示解题步骤，确保即使completedCurrentQuestion有问题也能显示
                String solution = shape.getSolution();
                if (solution == null || solution.isEmpty()) {
                    solution = "此题暂无详细解题步骤";
                }
                solutionArea.setText(solution);
                solutionArea.revalidate();
                solutionArea.repaint();
                
                // 禁用提交按钮和输入框
                submitButton.setEnabled(false);
                answerField.setEnabled(false);
                
                // 完成当前题目，标记为错误
                completedCurrentQuestion(false);
                
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
        currentShapeIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        correctAnswers.clear();  // 清空答题记录
        compoundCalculation.reset();
        showCurrentShape();
    }
    
    /**
     * 获取当前题目的尝试次数
     * @return 当前尝试次数
     */
    private int getAttempts() {
        return MAX_ATTEMPTS - getRemainingAttempts();
    }
    
    @Override
    protected int calculateScore() {
        int totalScore = 0;
        // 只计算答对的题目的分数
        for (int i = 0; i < attemptsPerTask.size(); i++) {
            if (i < correctAnswers.size() && correctAnswers.get(i)) {  // 只有答对的题目才计分
                int attempts = attemptsPerTask.get(i);
                if (attempts == 1) totalScore += 6;      // 第一次就答对得6分
                else if (attempts == 2) totalScore += 4; // 第二次答对得4分
                else if (attempts == 3) totalScore += 2; // 第三次答对得2分
            }
        }
        return totalScore;  // 直接返回实际得分，不转换为百分比
    }
    
    @Override
    public void startTask() {
        // 重置状态
        currentShapeIndex = 0;
        showCurrentShape();
    }
    
    @Override
    public void pauseTask() {
        if (submitButton != null) {
            submitButton.setEnabled(false);
        }
        if (answerField != null) {
            answerField.setEnabled(false);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(false);
        }
        
        // 暂停计时器
        if (questionTimer != null && questionTimer.isRunning()) {
            questionTimer.stop();
        }
    }
    
    @Override
    public void resumeTask() {
        if (submitButton != null) {
            submitButton.setEnabled(true);
        }
        if (answerField != null) {
            answerField.setEnabled(true);
        }
        if (shapeSelector != null) {
            shapeSelector.setEnabled(true);
        }
        
        // 恢复计时器
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
        
        if (parentWindow != null) {
            int score = calculateScore();
            int maxScore = compoundCalculation.getShapes().size() * 6;  // 每题满分6分
            
            // 显示结果
            parentWindow.showResult(score, maxScore);
        }
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        // 返回包含总分信息的字符串
        return "复合形状计算 - 当前得分：" + calculateScore();
    }
}
