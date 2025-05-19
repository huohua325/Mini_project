package com.shapeville.gui.shapes;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * A utility class for drawing circles with radius or diameter measurements.
 * This class provides functionality to draw circles with customizable dimensions
 * and visual properties such as colors and measurements display.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class CircleDrawer {
    /** The measurement value (radius or diameter) */
    private double value;
    /** Flag indicating if the measurement is radius (true) or diameter (false) */
    private boolean isRadius;
    /** Color used for filling the circle */
    private final Color CIRCLE_COLOR = new Color(135, 206, 235);
    /** Color used for drawing lines */
    private final Color LINE_COLOR = new Color(70, 130, 180);
    /** Color used for drawing text */
    private final Color TEXT_COLOR = new Color(25, 25, 112);
    /** Stroke style for dashed lines */
    private final BasicStroke DASHED_STROKE = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f,
            new float[]{5.0f},
            0.0f);
    /** Decimal format for displaying measurements */
    private final DecimalFormat df = new DecimalFormat("0.0");
    /** Fixed diameter size in pixels */
    private static final int FIXED_DIAMETER = 200;

    /**
     * Constructs a new CircleDrawer with default values.
     * Initializes the measurement value to 0 and sets measurement type to radius.
     */
    public CircleDrawer() {
        this.value = 0;
        this.isRadius = true;
    }

    /**
     * Sets the measurement value and type for the circle.
     *
     * @param value The measurement value to set
     * @param isRadius True if the value represents radius, false if it represents diameter
     */
    public void setValues(double value, boolean isRadius) {
        this.value = value;
        this.isRadius = isRadius;
    }

    /**
     * Draws the circle with its measurements in the specified graphics context.
     * The circle is drawn centered in the provided dimensions with either radius
     * or diameter measurement displayed.
     *
     * @param g The graphics context to draw on
     * @param width The width of the drawing area
     * @param height The height of the drawing area
     */
    public void draw(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate circle position (centered)
        int centerX = width / 2;
        int centerY = height / 2;
        int x = centerX - FIXED_DIAMETER / 2;
        int y = centerY - FIXED_DIAMETER / 2;

        // Draw the circle
        g2d.setColor(CIRCLE_COLOR);
        g2d.fillOval(x, y, FIXED_DIAMETER, FIXED_DIAMETER);
        g2d.setColor(LINE_COLOR);
        g2d.drawOval(x, y, FIXED_DIAMETER, FIXED_DIAMETER);

        // Draw radius or diameter line
        g2d.setStroke(DASHED_STROKE);
        if (isRadius) {
            // Draw radius
            g2d.drawLine(centerX, centerY, centerX + FIXED_DIAMETER / 2, centerY);
            // Draw radius label
            drawText(g2d, "r = " + df.format(value), 
                    centerX + FIXED_DIAMETER / 4, 
                    centerY - 15);
        } else {
            // Draw diameter
            g2d.drawLine(centerX - FIXED_DIAMETER / 2, centerY, 
                        centerX + FIXED_DIAMETER / 2, centerY);
            // Draw diameter label
            drawText(g2d, "d = " + df.format(value), 
                    centerX, 
                    centerY - 15);
        }

        // Restore default stroke
        g2d.setStroke(new BasicStroke(1.0f));
    }

    /**
     * Draws text centered at the specified coordinates.
     *
     * @param g2d The graphics context to draw on
     * @param text The text to draw
     * @param x The x-coordinate for text placement
     * @param y The y-coordinate for text placement
     */
    private void drawText(Graphics2D g2d, String text, int x, int y) {
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, x - textWidth / 2, y);
    }
} 