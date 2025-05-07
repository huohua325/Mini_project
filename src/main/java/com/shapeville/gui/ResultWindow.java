package com.shapeville.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultWindow extends JFrame {
    private final int score;
    private final String feedback;
    private final String taskName;
    private Timer animationTimer;
    private int currentScore = 0;
    private JLabel scoreLabel;
    private JPanel starsPanel;
    private JProgressBar scoreProgress;
    
    public ResultWindow(String taskName, int score, String feedback) {
        this.taskName = taskName;
        this.score = score;
        this.feedback = feedback;
        initializeUI();
        startScoreAnimation();
    }
    
    private void initializeUI() {
        setTitle("Shapeville - 任务结果");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 任务名称
        JLabel taskLabel = new JLabel(taskName);
        taskLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        taskLabel.setHorizontalAlignment(SwingConstants.CENTER);
        taskLabel.setForeground(new Color(51, 51, 153));
        topPanel.add(taskLabel, BorderLayout.NORTH);
        
        // 分数显示面板
        JPanel scorePanel = new JPanel(new BorderLayout(10, 10));
        scorePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 48));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(new Color(51, 153, 255));
        
        scoreProgress = new JProgressBar(0, 100);
        scoreProgress.setValue(0);
        scoreProgress.setStringPainted(true);
        scoreProgress.setPreferredSize(new Dimension(0, 20));
        
        scorePanel.add(scoreLabel, BorderLayout.CENTER);
        scorePanel.add(scoreProgress, BorderLayout.SOUTH);
        
        topPanel.add(scorePanel, BorderLayout.CENTER);
        
        // 创建星级评价面板
        starsPanel = createStarsPanel();
        topPanel.add(starsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 创建反馈面板
        JPanel feedbackPanel = new JPanel(new BorderLayout(10, 10));
        feedbackPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "详细反馈"
        ));
        
        JTextArea feedbackArea = new JTextArea(feedback);
        feedbackArea.setEditable(false);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setLineWrap(true);
        feedbackArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        feedbackArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        feedbackPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 添加成绩分析
        JPanel analysisPanel = new JPanel();
        analysisPanel.setLayout(new BoxLayout(analysisPanel, BoxLayout.Y_AXIS));
        analysisPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        addAnalysisItem(analysisPanel, "完成度", getCompletionStatus());
        addAnalysisItem(analysisPanel, "正确率", getAccuracyStatus());
        addAnalysisItem(analysisPanel, "表现评级", getPerformanceLevel());
        
        feedbackPanel.add(analysisPanel, BorderLayout.SOUTH);
        
        mainPanel.add(feedbackPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton continueButton = createStyledButton("继续学习", new Color(51, 153, 255));
        continueButton.addActionListener(e -> {
            dispose();
            com.shapeville.gui.UIManager.getInstance().showMainWindow();
        });
        
        JButton retryButton = createStyledButton("重新尝试", new Color(255, 153, 51));
        retryButton.addActionListener(e -> {
            dispose();
            com.shapeville.gui.UIManager.getInstance().switchToTask(taskName);
        });
        
        JButton exitButton = createStyledButton("结束学习", new Color(255, 51, 51));
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "确定要结束学习吗？",
                "确认",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });
        
        buttonPanel.add(continueButton);
        buttonPanel.add(retryButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }
    
    private JPanel createStarsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setOpaque(false);
        // 先创建5个灰色星星
        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel("★");
            star.setFont(new Font("Dialog", Font.PLAIN, 32));
            star.setForeground(Color.LIGHT_GRAY);
            panel.add(star);
        }
        return panel;
    }
    
    private void updateStars(int starCount) {
        starsPanel.removeAll();
        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel("★");
            star.setFont(new Font("Dialog", Font.PLAIN, 32));
            star.setForeground(i < starCount ? Color.ORANGE : Color.LIGHT_GRAY);
            starsPanel.add(star);
        }
        starsPanel.revalidate();
        starsPanel.repaint();
    }
    
    private void startScoreAnimation() {
        animationTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentScore < score) {
                    currentScore += 1;
                    scoreLabel.setText(String.valueOf(currentScore));
                    scoreProgress.setValue(currentScore);
                    
                    // 更新星星
                    int stars = currentScore >= 90 ? 5 : 
                              currentScore >= 80 ? 4 : 
                              currentScore >= 70 ? 3 : 
                              currentScore >= 60 ? 2 : 1;
                    updateStars(stars);
                    
                    // 更新进度条颜色
                    if (currentScore >= 90) {
                        scoreProgress.setForeground(new Color(0, 153, 0));  // 深绿色
                    } else if (currentScore >= 70) {
                        scoreProgress.setForeground(new Color(0, 102, 204));  // 蓝色
                    } else if (currentScore >= 60) {
                        scoreProgress.setForeground(new Color(255, 153, 0));  // 橙色
                    } else {
                        scoreProgress.setForeground(new Color(255, 51, 51));  // 红色
                    }
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        animationTimer.start();
    }
    
    private void addAnalysisItem(JPanel panel, String label, String value) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelComponent = new JLabel(label + "：");
        labelComponent.setFont(new Font("微软雅黑", Font.BOLD, 14));
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        itemPanel.add(labelComponent);
        itemPanel.add(valueComponent);
        panel.add(itemPanel);
    }
    
    private String getCompletionStatus() {
        if (score >= 90) return "完美完成";
        if (score >= 80) return "优秀完成";
        if (score >= 70) return "良好完成";
        if (score >= 60) return "基本完成";
        return "需要改进";
    }
    
    private String getAccuracyStatus() {
        return String.format("%.1f%%", score);
    }
    
    private String getPerformanceLevel() {
        if (score >= 90) return "S级（卓越）";
        if (score >= 80) return "A级（优秀）";
        if (score >= 70) return "B级（良好）";
        if (score >= 60) return "C级（及格）";
        return "D级（不及格）";
    }
    
    @Override
    public void dispose() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        super.dispose();
    }
}
