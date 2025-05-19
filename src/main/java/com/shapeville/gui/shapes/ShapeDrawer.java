package com.shapeville.gui.shapes;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * The ShapeDrawer class provides static methods for drawing various geometric shapes
 * with dimensions and measurements. It supports drawing rectangles, parallelograms,
 * triangles, and trapeziums with customizable colors and dimension labels.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class ShapeDrawer {
    /** Color used for shape fill */
    private static final Color SHAPE_FILL_COLOR = new Color(230, 240, 255);
    /** Color used for shape borders */
    private static final Color SHAPE_BORDER_COLOR = new Color(70, 130, 180);
    /** Color used for dimension lines */
    private static final Color DIMENSION_LINE_COLOR = new Color(200, 0, 0);
    /** Color used for dimension text */
    private static final Color DIMENSION_TEXT_COLOR = new Color(0, 0, 150);
    /** Width of the main shape stroke */
    private static final float MAIN_STROKE_WIDTH = 2.0f;
    /** Width of the dimension lines */
    private static final float DIMENSION_STROKE_WIDTH = 1.0f;
    /** Offset for dimension lines from shape */
    private static final int DIMENSION_LINE_OFFSET = 20;
    /** Font used for dimension text */
    private static final Font DIMENSION_FONT = new Font("Arial", Font.PLAIN, 14);

    /**
     * Draws a rectangle with optional dimension labels.
     *
     * @param g2d Graphics2D context to draw on
     * @param x X-coordinate of the top-left corner
     * @param y Y-coordinate of the top-left corner
     * @param width Width of the rectangle
     * @param height Height of the rectangle
     * @param params Map containing dimension parameters ("长" for length, "宽" for width)
     * @param showDimensions Whether to show dimension labels
     */
    public static void drawRectangle(Graphics2D g2d, int x, int y, int width, int height, Map<String, Double> params, boolean showDimensions) {
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw fill
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillRect(x, y, width, height);
        
        // Draw border
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawRect(x, y, width, height);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // Draw length dimension
            String lengthText = String.format("Length = %.1f", params.get("长"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(lengthText);
            g2d.drawString(lengthText, x + (width - textWidth) / 2, y + height + DIMENSION_LINE_OFFSET);
            
            // Draw width dimension
            String widthText = String.format("Width = %.1f", params.get("宽"));
            g2d.drawString(widthText, x - DIMENSION_LINE_OFFSET - fm.stringWidth(widthText), y + height / 2);
            
            // Draw dimension lines
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(x, y + height + 5, x + width, y + height + 5); // Length line
            g2d.drawLine(x - 5, y, x - 5, y + height); // Width line
        }
    }

    /**
     * Draws a parallelogram with optional dimension labels.
     *
     * @param g2d Graphics2D context to draw on
     * @param x X-coordinate of the bottom-left corner
     * @param y Y-coordinate of the top point
     * @param width Width of the parallelogram
     * @param height Height of the parallelogram
     * @param offset Horizontal offset for the top edge
     * @param params Map containing dimension parameters ("底" for base, "高" for height)
     * @param showDimensions Whether to show dimension labels
     */
    public static void drawParallelogram(Graphics2D g2d, int x, int y, int width, int height, int offset, Map<String, Double> params, boolean showDimensions) {
        int[] xPoints = {x + offset, x + offset + width, x + width, x};
        int[] yPoints = {y + height, y + height, y, y};
        
        // Draw fill
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // Draw border
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // Draw base dimension
            String baseText = String.format("Base = %.1f", params.get("底"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(baseText);
            g2d.drawString(baseText, x + offset + (width - textWidth) / 2, y + height + DIMENSION_LINE_OFFSET);
            
            // Draw height dimension
            String heightText = String.format("Height = %.1f", params.get("高"));
            g2d.drawString(heightText, x - DIMENSION_LINE_OFFSET - fm.stringWidth(heightText), y + height / 2);
            
            // Draw height line
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(x, y, x, y + height);
        }
    }

    /**
     * Draws a triangle with optional dimension labels.
     *
     * @param g2d Graphics2D context to draw on
     * @param centerX X-coordinate of the triangle's center
     * @param centerY Y-coordinate of the triangle's center
     * @param base Length of the triangle's base
     * @param height Height of the triangle
     * @param params Map containing dimension parameters ("底" for base, "高" for height)
     * @param showDimensions Whether to show dimension labels
     */
    public static void drawTriangle(Graphics2D g2d, int centerX, int centerY, int base, int height, Map<String, Double> params, boolean showDimensions) {
        int[] xPoints = {centerX - base/2, centerX + base/2, centerX};
        int[] yPoints = {centerY + height/2, centerY + height/2, centerY - height/2};
        
        // Draw fill
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Draw border
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawPolygon(xPoints, yPoints, 3);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // Draw base dimension
            String baseText = String.format("Base = %.1f", params.get("底"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(baseText);
            g2d.drawString(baseText, centerX - textWidth / 2, centerY + height/2 + DIMENSION_LINE_OFFSET);
            
            // Draw height dimension
            String heightText = String.format("Height = %.1f", params.get("高"));
            g2d.drawString(heightText, centerX + 10, centerY);
            
            // Draw height line
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(centerX, centerY - height/2, centerX, centerY + height/2);
        }
    }

    /**
     * Draws a trapezium (trapezoid) with optional dimension labels.
     *
     * @param g2d Graphics2D context to draw on
     * @param centerX X-coordinate of the trapezium's center
     * @param centerY Y-coordinate of the trapezium's center
     * @param topWidth Width of the top parallel side
     * @param bottomWidth Width of the bottom parallel side
     * @param height Height of the trapezium
     * @param params Map containing dimension parameters ("上底" for top base, "下底" for bottom base, "高" for height)
     * @param showDimensions Whether to show dimension labels
     */
    public static void drawTrapezium(Graphics2D g2d, int centerX, int centerY, int topWidth, int bottomWidth, int height, Map<String, Double> params, boolean showDimensions) {
        int[] xPoints = {centerX - topWidth/2, centerX + topWidth/2, 
                        centerX + bottomWidth/2, centerX - bottomWidth/2};
        int[] yPoints = {centerY - height/2, centerY - height/2,
                        centerY + height/2, centerY + height/2};
        
        // Draw fill
        g2d.setColor(SHAPE_FILL_COLOR);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        // Draw border
        g2d.setColor(SHAPE_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
        g2d.drawPolygon(xPoints, yPoints, 4);
        
        if (showDimensions && params != null) {
            g2d.setFont(DIMENSION_FONT);
            g2d.setColor(DIMENSION_TEXT_COLOR);
            
            // Draw top base dimension
            String topText = String.format("Top = %.1f", params.get("上底"));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(topText);
            g2d.drawString(topText, centerX - textWidth / 2, centerY - height/2 - 5);
            
            // Draw bottom base dimension
            String bottomText = String.format("Bottom = %.1f", params.get("下底"));
            textWidth = fm.stringWidth(bottomText);
            g2d.drawString(bottomText, centerX - textWidth / 2, centerY + height/2 + DIMENSION_LINE_OFFSET);
            
            // Draw height dimension
            String heightText = String.format("Height = %.1f", params.get("高"));
            g2d.drawString(heightText, centerX + bottomWidth/2 + DIMENSION_LINE_OFFSET, centerY);
            
            // Draw height line
            g2d.setColor(DIMENSION_LINE_COLOR);
            g2d.setStroke(new BasicStroke(DIMENSION_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5}, 0.0f));
            g2d.drawLine(centerX + bottomWidth/2 + 5, centerY - height/2,
                       centerX + bottomWidth/2 + 5, centerY + height/2);
        }
    }
} 