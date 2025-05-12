package com.shapeville.gui.shapes;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ShapeDrawer {
    private static final Color SHAPE_FILL_COLOR = new Color(230, 240, 255);
    private static final Color SHAPE_BORDER_COLOR = new Color(70, 130, 180);
    private static final Color DIMENSION_LINE_COLOR = new Color(200, 0, 0);
    private static final Color DIMENSION_TEXT_COLOR = new Color(0, 0, 150);
    private static final float MAIN_STROKE_WIDTH = 2.0f;
    private static final float DIMENSION_STROKE_WIDTH = 1.0f;
    private static final int DIMENSION_LINE_OFFSET = 20;
    private static final Font DIMENSION_FONT = new Font("微软雅黑", Font.PLAIN, 14);

    public static void drawRectangle(Graphics2D g2d, int x, int y, int width, int height, Map<String, Double> params, boolean showDimensions) {
        // 设置渲染提示
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制填充
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillRect(x, y, width, height);
        
        // 绘制边框
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawRect(x, y, width, height);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // 标注长度
            String lengthText = String.format("长 = %.1f", params.get("长"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(lengthText);
            g2d.drawString(lengthText, x + (width - textWidth) / 2, y + height + DIMENSION_LINE_OFFSET);
            
            // 标注宽度
            String widthText = String.format("宽 = %.1f", params.get("宽"));
            g2d.drawString(widthText, x - DIMENSION_LINE_OFFSET - fm.stringWidth(widthText), y + height / 2);
            
            // 绘制标注辅助线
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(x, y + height + 5, x + width, y + height + 5); // 长度辅助线
            g2d.drawLine(x - 5, y, x - 5, y + height); // 宽度辅助线
        }
    }

    public static void drawParallelogram(Graphics2D g2d, int x, int y, int width, int height, int offset, Map<String, Double> params, boolean showDimensions) {
        int[] xPoints = {x + offset, x + offset + width, x + width, x};
        int[] yPoints = {y + height, y + height, y, y};
        
        // 绘制填充
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // 绘制边框
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // 标注底边
            String baseText = String.format("底 = %.1f", params.get("底"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(baseText);
            g2d.drawString(baseText, x + offset + (width - textWidth) / 2, y + height + DIMENSION_LINE_OFFSET);
            
            // 标注高
            String heightText = String.format("高 = %.1f", params.get("高"));
            g2d.drawString(heightText, x - DIMENSION_LINE_OFFSET - fm.stringWidth(heightText), y + height / 2);
            
            // 绘制高度辅助线
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(x, y, x, y + height);
        }
    }

    public static void drawTriangle(Graphics2D g2d, int centerX, int centerY, int base, int height, Map<String, Double> params, boolean showDimensions) {
        int[] xPoints = {centerX - base/2, centerX + base/2, centerX};
        int[] yPoints = {centerY + height/2, centerY + height/2, centerY - height/2};
        
        // 绘制填充
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // 绘制边框
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawPolygon(xPoints, yPoints, 3);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // 标注底边
            String baseText = String.format("底 = %.1f", params.get("底"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(baseText);
            g2d.drawString(baseText, centerX - textWidth / 2, centerY + height/2 + DIMENSION_LINE_OFFSET);
            
            // 标注高
            String heightText = String.format("高 = %.1f", params.get("高"));
            g2d.drawString(heightText, centerX + 10, centerY);
            
            // 绘制高度辅助线
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(centerX, centerY - height/2, centerX, centerY + height/2);
        }
    }

    public static void drawTrapezium(Graphics2D g2d, int centerX, int centerY, int topWidth, int bottomWidth, int height, Map<String, Double> params, boolean showDimensions) {
        int[] xPoints = {centerX - topWidth/2, centerX + topWidth/2, 
                        centerX + bottomWidth/2, centerX - bottomWidth/2};
        int[] yPoints = {centerY - height/2, centerY - height/2,
                        centerY + height/2, centerY + height/2};
        
        // 绘制填充
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // 绘制边框
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // 标注上底
            String topText = String.format("上底 = %.1f", params.get("上底"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(topText);
            g2d.drawString(topText, centerX - textWidth / 2, centerY - height/2 - 5);
            
            // 标注下底
            String bottomText = String.format("下底 = %.1f", params.get("下底"));
            textWidth = fm.stringWidth(bottomText);
            g2d.drawString(bottomText, centerX - textWidth / 2, centerY + height/2 + DIMENSION_LINE_OFFSET);
            
            // 标注高
            String heightText = String.format("高 = %.1f", params.get("高"));
            g2d.drawString(heightText, centerX + bottomWidth/2 + DIMENSION_LINE_OFFSET, centerY);
            
            // 绘制高度辅助线
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(centerX + bottomWidth/2 + 5, centerY - height/2,
                       centerX + bottomWidth/2 + 5, centerY + height/2);
        }
    }
} 