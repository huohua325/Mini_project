package com.shapeville.gui.shapes;

import java.awt.*;

/**
 * Abstract base class for compound shape drawing.
 * Provides common utility methods for shape rendering.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public abstract class CompoundShapeDrawer implements ShapeRenderer {
    /** Padding space around shapes */
    protected static final int PADDING = 40;
    /** Size of dimension arrows */
    protected static final int ARROW_SIZE = 5;
    /** Length of dash pattern */
    protected static final float DASH_LENGTH = 5.0f;
    /** Color used for shape fill */
    protected static final Color SHAPE_COLOR = new Color(200, 220, 240);
    /** Color used for lines */
    protected static final Color LINE_COLOR = Color.BLACK;
    /** Color used for text */
    protected static final Color TEXT_COLOR = new Color(0, 51, 153);
    
    /**
     * Prepares the graphics context for drawing.
     * Sets up anti-aliasing, rendering hints, and basic drawing properties.
     *
     * @param g2d The graphics context to prepare
     */
    protected void prepareGraphics(Graphics2D g2d) {
        System.out.println("Preparing graphics context");
        
        // Set anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        // Set basic drawing properties
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setBackground(Color.WHITE);
        
        System.out.println("Graphics context preparation completed");
    }
    
    /**
     * Calculates the scale factor to fit the shape within the available drawing area.
     *
     * @param realWidth The actual width of the shape
     * @param realHeight The actual height of the shape
     * @param availableWidth The available width in the drawing area
     * @param availableHeight The available height in the drawing area
     * @return The scale factor to apply
     */
    protected double calculateScale(double realWidth, double realHeight, 
                                  int availableWidth, int availableHeight) {
        double scaleX = (availableWidth - 2 * PADDING) / realWidth;
        double scaleY = (availableHeight - 2 * PADDING) / realHeight;
        return Math.min(scaleX, scaleY);
    }
    
    /**
     * Draws a dimension line with arrows and measurement text.
     *
     * @param g2d The graphics context to draw on
     * @param x1 The x-coordinate of the start point
     * @param y1 The y-coordinate of the start point
     * @param x2 The x-coordinate of the end point
     * @param y2 The y-coordinate of the end point
     * @param text The measurement text to display
     */
    protected void drawDimensionLine(Graphics2D g2d, int x1, int y1, int x2, int y2, String text) {
        // Save original settings
        Stroke originalStroke = g2d.getStroke();
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        try {
            // Set dashed line style
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                        10.0f, new float[]{DASH_LENGTH}, 0.0f));
            g2d.setColor(LINE_COLOR);
            
            // Draw main line
            g2d.drawLine(x1, y1, x2, y2);
            
            // Calculate arrow direction
            double angle = Math.atan2(y2 - y1, x2 - x1);
            
            // Draw start arrow
            drawArrow(g2d, x1, y1, angle + Math.PI);
            
            // Draw end arrow
            drawArrow(g2d, x2, y2, angle);
            
            // Draw dimension text
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(TEXT_COLOR);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            
            // Calculate text position
            int textX, textY;
            if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
                // Horizontal line
                textX = (x1 + x2 - textWidth) / 2;
                textY = y1 - 5;
            } else {
                // Vertical line
                textX = x1 + 5;
                textY = (y1 + y2 + textHeight) / 2;
            }
            
            g2d.drawString(text, textX, textY);
            
        } finally {
            // Restore original settings
            g2d.setStroke(originalStroke);
            g2d.setColor(originalColor);
            g2d.setFont(originalFont);
        }
    }
    
    /**
     * Draws an arrow head at the specified position and angle.
     *
     * @param g2d The graphics context to draw on
     * @param x The x-coordinate of the arrow tip
     * @param y The y-coordinate of the arrow tip
     * @param angle The angle of the arrow in radians
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