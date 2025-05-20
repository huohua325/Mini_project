package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * Implementation of the Stair shape (Shape 2 in the list).
 * Composed of two combined rectangles:
 * - Left large rectangle: 11cm × 21cm
 * - Right small rectangle: 9cm × 10cm (aligned at the bottom)
 * Characteristics:
 * - Total base length: 20cm (11 + 9)
 * - Height difference: 11cm (21 - 10)
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class StairShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    /**
     * Constructs a new StairShape.
     * Initializes the dimensions of the shape.
     */
    public StairShape() {
        dimensions = new HashMap<>();
        dimensions.put("leftWidth", 11.0);   // Left rectangle width
        dimensions.put("leftHeight", 21.0);  // Left rectangle height
        dimensions.put("rightWidth", 9.0);   // Right rectangle width
        dimensions.put("rightHeight", 10.0); // Right rectangle height
    }
    
    /**
     * Draws the Stair shape.
     *
     * @param g The graphics context to draw on.
     * @param width The available width for drawing.
     * @param height The available height for drawing.
     */
    @Override
    public void draw(Graphics2D g, int width, int height) {
        // Removed System.out.println for drawing start
        
        prepareGraphics(g);
        
        // Calculate scaling factor
        double scale = FIXED_SIZE / Math.max(
            dimensions.get("leftWidth") + dimensions.get("rightWidth"),
            dimensions.get("leftHeight")
        );
        
        // Calculate actual dimensions
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightHeight = (int)(dimensions.get("rightHeight") * scale);
        
        // Calculate drawing position (centered)
        int centerX = width / 2;
        int centerY = height / 2;
        int totalWidth = leftWidth + rightWidth;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - leftHeight / 2;
        
        try {
            // Create shape path
            Path2D.Double path = new Path2D.Double();
            
            // Draw counter-clockwise from the top-left corner
            path.moveTo(startX, startY); // Top-left corner
            path.lineTo(startX + leftWidth, startY); // Top edge of left rectangle
            path.lineTo(startX + leftWidth, startY + leftHeight - rightHeight); // Left endpoint of top edge of right rectangle
            path.lineTo(startX + totalWidth, startY + leftHeight - rightHeight); // Top edge of right rectangle
            path.lineTo(startX + totalWidth, startY + leftHeight); // Right edge of right rectangle
            path.lineTo(startX, startY + leftHeight); // Bottom edge
            path.closePath();
            
            // Fill shape
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // Draw outline
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("Error drawing Stair shape: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Draws the dimensions for the Stair shape.
     *
     * @param g The graphics context to draw on.
     * @param width The available width for drawing.
     * @param height The available height for drawing.
     */
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // Use the same calculations as in the draw method
        double scale = FIXED_SIZE / Math.max(
            dimensions.get("leftWidth") + dimensions.get("rightWidth"),
            dimensions.get("leftHeight")
        );
        
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightHeight = (int)(dimensions.get("rightHeight") * scale);
        
        int centerX = width / 2;
        int centerY = height / 2;
        int totalWidth = leftWidth + rightWidth;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - leftHeight / 2;
        
        // Draw dimensions for the left rectangle
        drawDimensionLine(g, startX - 40, startY,
                         startX - 40, startY + leftHeight,
                         String.format("%.0f cm", dimensions.get("leftHeight"))); // 21 cm
        drawDimensionLine(g, startX, startY - 20,
                         startX + leftWidth, startY - 20,
                         String.format("%.0f cm", dimensions.get("leftWidth"))); // 11 cm
        
        // Draw dimensions for the right rectangle
        drawDimensionLine(g, startX + totalWidth + 20, startY + leftHeight - rightHeight,
                         startX + totalWidth + 20, startY + leftHeight,
                         String.format("%.0f cm", dimensions.get("rightHeight"))); // 10 cm
        drawDimensionLine(g, startX + leftWidth, startY + leftHeight - rightHeight - 20,
                         startX + totalWidth, startY + leftHeight - rightHeight - 20,
                         String.format("%.0f cm", dimensions.get("rightWidth"))); // 9 cm
        
        // Draw total length of the bottom edge
        drawDimensionLine(g, startX, startY + leftHeight + 20,
                         startX + totalWidth, startY + leftHeight + 20,
                         String.format("%.0f cm", dimensions.get("leftWidth") + dimensions.get("rightWidth"))); // 20 cm
    }
    
    /**
     * Gets the dimensions of the shape.
     *
     * @return A map containing dimension names and their values.
     */
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    /**
     * Calculates the area of the Stair shape.
     *
     * @return The calculated area.
     */
    @Override
    public double calculateArea() {
        // Area of the left rectangle
        double leftArea = dimensions.get("leftWidth") * dimensions.get("leftHeight");
        
        // Area of the right rectangle
        double rightArea = dimensions.get("rightWidth") * dimensions.get("rightHeight");
        
        return leftArea + rightArea;
    }
    
    /**
     * Gets the solution steps for calculating the area of the Stair shape.
     *
     * @return A text description of the solution steps.
     */
    @Override
    public String getSolutionSteps() {
        double leftArea = dimensions.get("leftWidth") * dimensions.get("leftHeight");
        double rightArea = dimensions.get("rightWidth") * dimensions.get("rightHeight");
        
        return String.format(
            "1. Calculate the area of the left rectangle:\n" +
            "   Width \u00d7 Height = %.0f \u00d7 %.0f = %.0f cm\u00b2\n\n" + // 11 x 21 = 231
            "2. Calculate the area of the right rectangle:\n" +
            "   Width \u00d7 Height = %.0f \u00d7 %.0f = %.0f cm\u00b2\n\n" + // 9 x 10 = 90
            "3. Calculate the total area:\n" +
            "   Left rectangle area + Right rectangle area = %.0f + %.0f = %.0f cm\u00b2", // 231 + 90 = 321
            dimensions.get("leftWidth"), dimensions.get("leftHeight"), leftArea,
            dimensions.get("rightWidth"), dimensions.get("rightHeight"), rightArea,
            leftArea, rightArea, calculateArea()
        );
    }
} 