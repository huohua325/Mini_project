package com.shapeville.gui.tasks;

import javax.swing.*;
import java.awt.*;
import com.shapeville.game.SectorCalculation;
import com.shapeville.game.SectorCalculation.Sector;
import java.util.List;

public class SectorCalculationPanel extends BaseTaskPanel implements TaskPanelInterface {
    private final SectorCalculation sectorCalculation;
    private int currentSectorIndex = 0;
    private JLabel sectorLabel;
    private JTextArea descriptionArea;
    private JTextField answerField;
    private JButton submitButton;
    private JTextArea solutionArea;
    private JComboBox<String> sectorSelector;
    private JPanel sectorDisplayPanel;
    
    public SectorCalculationPanel() {
        super("扇形计算");
        this.sectorCalculation = new SectorCalculation();
    }
    
    @Override
    public void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // 创建顶部面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建扇形选择器
        List<Sector> sectors = sectorCalculation.getSectors();
        String[] sectorNames = new String[sectors.size()];
        for (int i = 0; i < sectors.size(); i++) {
            sectorNames[i] = "扇形 " + (i + 1);
        }
        sectorSelector = new JComboBox<>(sectorNames);
        sectorSelector.addActionListener(e -> {
            if (!sectorCalculation.getPracticed().contains(sectorSelector.getSelectedIndex())) {
                currentSectorIndex = sectorSelector.getSelectedIndex();
                resetAttempts();
                showCurrentSector();
            }
        });
        topPanel.add(sectorSelector, BorderLayout.NORTH);
        
        // 创建扇形标签
        sectorLabel = new JLabel("", SwingConstants.CENTER);
        sectorLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        topPanel.add(sectorLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 创建中央面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // 创建扇形显示面板
        sectorDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSector(g);
            }
        };
        sectorDisplayPanel.setPreferredSize(new Dimension(300, 300));
        sectorDisplayPanel.setBackground(Color.WHITE);
        centerPanel.add(sectorDisplayPanel);
        
        // 创建描述区域
        descriptionArea = new JTextArea(2, 40);
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        descriptionArea.setBackground(new Color(240, 240, 240));
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("参数信息"));
        centerPanel.add(descriptionScroll);
        
        // 创建答案输入区域
        JPanel answerPanel = new JPanel();
        answerField = new JTextField(15);
        submitButton = new JButton("提交答案");
        submitButton.addActionListener(e -> handleSubmit());
        
        answerPanel.add(new JLabel("请输入扇形面积（π取3.14，保留2位小数）："));
        answerPanel.add(answerField);
        answerPanel.add(submitButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(answerPanel);
        
        // 创建解答区域
        solutionArea = new JTextArea(3, 40);
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
        
        // 显示第一个扇形
        showCurrentSector();
    }
    
    private void drawSector(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = sectorDisplayPanel.getWidth() / 2;
        int centerY = sectorDisplayPanel.getHeight() / 2;
        
        Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
        int pixelRadius = Math.min(centerX, centerY) - 50;
        
        // 绘制扇形
        g2d.setColor(new Color(200, 200, 255));
        g2d.fillArc(centerX - pixelRadius, centerY - pixelRadius, 
                    pixelRadius * 2, pixelRadius * 2, 
                    0, -(int)sector.getAngle());
        
        // 绘制扇形边界
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawArc(centerX - pixelRadius, centerY - pixelRadius, 
                    pixelRadius * 2, pixelRadius * 2, 
                    0, -(int)sector.getAngle());
        g2d.drawLine(centerX, centerY, 
                    centerX + pixelRadius, centerY);
        g2d.drawLine(centerX, centerY, 
                    (int)(centerX + pixelRadius * Math.cos(Math.toRadians(-sector.getAngle()))),
                    (int)(centerY + pixelRadius * Math.sin(Math.toRadians(-sector.getAngle()))));
        
        // 标注半径
        g2d.setColor(Color.BLACK);
        g2d.drawString("r = " + sector.getRadius(), centerX + pixelRadius/2, centerY - 10);
        
        // 标注角度
        int angleX = centerX + pixelRadius/3;
        int angleY = centerY - pixelRadius/3;
        g2d.drawString(sector.getAngle() + "°", angleX, angleY);
    }
    
    private void showCurrentSector() {
        List<Sector> sectors = sectorCalculation.getSectors();
        if (currentSectorIndex < sectors.size()) {
            Sector sector = sectors.get(currentSectorIndex);
            sectorLabel.setText("扇形 " + (currentSectorIndex + 1));
            descriptionArea.setText(String.format("半径(r) = %.1f, 圆心角 = %.1f°", 
                                                sector.getRadius(), sector.getAngle()));
            solutionArea.setText("");
            answerField.setText("");
            answerField.requestFocus();
            sectorSelector.setSelectedIndex(currentSectorIndex);
            sectorDisplayPanel.repaint();
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
            
            if (sectorCalculation.checkAnswer(currentSectorIndex, answer)) {
                setFeedback("回答正确！");
                solutionArea.setText(sectorCalculation.getSectors().get(currentSectorIndex).getSolution());
                sectorCalculation.addPracticed(currentSectorIndex);
                addAttemptToList();
                
                if (sectorCalculation.isComplete()) {
                    endTask();
                    return;
                }
                
                // 找到下一个未完成的扇形
                do {
                    currentSectorIndex++;
                    if (currentSectorIndex >= sectorCalculation.getSectors().size()) {
                        endTask();
                        return;
                    }
                } while (sectorCalculation.getPracticed().contains(currentSectorIndex));
                
                resetAttempts();
                showCurrentSector();
            } else if (!hasRemainingAttempts()) {
                Sector sector = sectorCalculation.getSectors().get(currentSectorIndex);
                setFeedback("已达到最大尝试次数。正确答案是：" + String.format("%.2f", sector.getCorrectArea()));
                solutionArea.setText(sector.getSolution());
                sectorCalculation.addPracticed(currentSectorIndex);
                addAttemptToList();
                
                if (sectorCalculation.isComplete()) {
                    endTask();
                    return;
                }
                
                // 找到下一个未完成的扇形
                do {
                    currentSectorIndex++;
                    if (currentSectorIndex >= sectorCalculation.getSectors().size()) {
                        endTask();
                        return;
                    }
                } while (sectorCalculation.getPracticed().contains(currentSectorIndex));
                
                resetAttempts();
                showCurrentSector();
            } else {
                setFeedback("回答错误，请再试一次。还剩" + getRemainingAttempts() + "次机会。");
            }
        } catch (NumberFormatException e) {
            setFeedback("请输入有效的数字！");
        }
    }
    
    @Override
    public void reset() {
        currentSectorIndex = 0;
        resetAttempts();
        attemptsPerTask.clear();
        sectorCalculation.reset();
        showCurrentSector();
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
        sectorSelector.setEnabled(false);
    }
    
    @Override
    public void resumeTask() {
        submitButton.setEnabled(true);
        answerField.setEnabled(true);
        sectorSelector.setEnabled(true);
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