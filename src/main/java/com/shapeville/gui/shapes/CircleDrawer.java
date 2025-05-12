package com.shapeville.gui.shapes;

import java.awt.*;
import java.text.DecimalFormat;

public class CircleDrawer {
    private double value;
    private boolean isRadius;
    private final Color CIRCLE_COLOR = new Color(135, 206, 235);
    private final Color LINE_COLOR = new Color(70, 130, 180);
    private final Color TEXT_COLOR = new Color(25, 25, 112);
    private final BasicStroke DASHED_STROKE = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f,
            new float[]{5.0f},
            0.0f);
    private final DecimalFormat df = new DecimalFormat("0.0");
    private static final int FIXED_DIAMETER = 200; // 固定圆形直径为200像素

    public CircleDrawer() {
        this.value = 0;
        this.isRadius = true;
    }

    public void setValues(double value, boolean isRadius) {
        this.value = value;
        this.isRadius = isRadius;
    }

    public void draw(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 计算圆的位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        int x = centerX - FIXED_DIAMETER / 2;
        int y = centerY - FIXED_DIAMETER / 2;

        // 绘制圆形
        g2d.setColor(CIRCLE_COLOR);
        g2d.fillOval(x, y, FIXED_DIAMETER, FIXED_DIAMETER);
        g2d.setColor(LINE_COLOR);
        g2d.drawOval(x, y, FIXED_DIAMETER, FIXED_DIAMETER);

        // 绘制半径或直径线
        g2d.setStroke(DASHED_STROKE);
        if (isRadius) {
            // 绘制半径
            g2d.drawLine(centerX, centerY, centerX + FIXED_DIAMETER / 2, centerY);
            // 绘制半径标注
            drawText(g2d, "r = " + df.format(value), 
                    centerX + FIXED_DIAMETER / 4, 
                    centerY - 15);
        } else {
            // 绘制直径
            g2d.drawLine(centerX - FIXED_DIAMETER / 2, centerY, 
                        centerX + FIXED_DIAMETER / 2, centerY);
            // 绘制直径标注
            drawText(g2d, "d = " + df.format(value), 
                    centerX, 
                    centerY - 15);
        }

        // 恢复默认笔画
        g2d.setStroke(new BasicStroke(1.0f));
    }

    private void drawText(Graphics2D g2d, String text, int x, int y) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, x - textWidth / 2, y);
    }
} 