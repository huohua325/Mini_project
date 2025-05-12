package com.shapeville.gui.shapes;

import java.awt.*;

/**
 * 复合形状绘制基类
 * 提供通用的绘图工具方法
 */
public abstract class CompoundShapeDrawer implements ShapeRenderer {
    protected static final int PADDING = 40;
    protected static final int ARROW_SIZE = 5;
    protected static final float DASH_LENGTH = 5.0f;
    protected static final Color SHAPE_COLOR = new Color(200, 220, 240);
    protected static final Color LINE_COLOR = Color.BLACK;
    protected static final Color TEXT_COLOR = new Color(0, 51, 153);
    
    /**
     * 准备绘图上下文
     * @param g2d 图形上下文
     */
    protected void prepareGraphics(Graphics2D g2d) {
        System.out.println("准备绘图上下文");
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        // 设置基本绘图属性
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setBackground(Color.WHITE);
        
        System.out.println("绘图上下文准备完成");
    }
    
    /**
     * 计算缩放比例，使形状适应绘图区域
     * @param realWidth 实际宽度
     * @param realHeight 实际高度
     * @param availableWidth 可用宽度
     * @param availableHeight 可用高度
     * @return 缩放比例
     */
    protected double calculateScale(double realWidth, double realHeight, 
                                  int availableWidth, int availableHeight) {
        double scaleX = (availableWidth - 2 * PADDING) / realWidth;
        double scaleY = (availableHeight - 2 * PADDING) / realHeight;
        return Math.min(scaleX, scaleY);
    }
    
    /**
     * 绘制带箭头的尺寸线
     * @param g2d 图形上下文
     * @param x1 起点x坐标
     * @param y1 起点y坐标
     * @param x2 终点x坐标
     * @param y2 终点y坐标
     * @param text 尺寸文本
     */
    protected void drawDimensionLine(Graphics2D g2d, int x1, int y1, int x2, int y2, String text) {
        // 保存原始设置
        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        try {
            // 设置虚线样式
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                        10.0f, new float[]{DASH_LENGTH}, 0.0f));
            g2d.setColor(LINE_COLOR);
            
            // 绘制主线
            g2d.drawLine(x1, y1, x2, y2);
            
            // 计算箭头方向
            double angle = Math.atan2(y2 - y1, x2 - x1);
            
            // 绘制起点箭头
            drawArrow(g2d, x1, y1, angle + Math.PI);
            
            // 绘制终点箭头
            drawArrow(g2d, x2, y2, angle);
            
            // 绘制尺寸文本
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            g2d.setColor(TEXT_COLOR);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            
            // 计算文本位置
            int textX, textY;
            if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
                // 水平线
                textX = (x1 + x2 - textWidth) / 2;
                textY = y1 - 5;
            } else {
                // 垂直线
                textX = x1 + 5;
                textY = (y1 + y2 + textHeight) / 2;
            }
            
            g2d.drawString(text, textX, textY);
            
        } finally {
            // 恢复原始设置
            g2d.setStroke(originalStroke);
            g2d.setColor(originalColor);
            g2d.setFont(originalFont);
        }
    }
    
    /**
     * 绘制箭头
     */
    private void drawArrow(Graphics2D g2d, int x, int y, double angle) {
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        
        xPoints[0] = x;
        yPoints[0] = y;
        
        xPoints[1] = x - (int)(ARROW_SIZE * Math.cos(angle - Math.PI/6));
        yPoints[1] = y - (int)(ARROW_SIZE * Math.sin(angle - Math.PI/6));
        
        xPoints[2] = x - (int)(ARROW_SIZE * Math.cos(angle + Math.PI/6));
        yPoints[2] = y - (int)(ARROW_SIZE * Math.sin(angle + Math.PI/6));
        
        g2d.setColor(LINE_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
} 