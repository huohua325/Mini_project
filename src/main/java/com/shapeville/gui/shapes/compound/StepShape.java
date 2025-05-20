package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * Implementation of the Step shape.
 * Overall structure:
 * - Left side height: 11 cm (total height)
 * - Top horizontal edge in two segments:
 *   * Left segment: 10 cm (higher)
 *   * Right segment: 8 cm (lower, 3cm lower than the left segment)
 * - Right vertical edge: 8 cm
 * - Total bottom edge length: 18 cm (10 + 8)
 * Can be viewed as a large rectangle (18x11) minus a small rectangle in the top-right corner (8x3).
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class StepShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    /**
     * Constructs a new StepShape.
     * Initializes the dimensions of the shape.
     */
    public StepShape() {
        dimensions = new HashMap<>();
        dimensions.put("totalHeight", 11.0);     // Total height
        dimensions.put("leftWidth", 10.0);       // Left segment width
        dimensions.put("rightWidth", 8.0);       // Right segment width
        dimensions.put("rightSideHeight", 8.0);  // Right side height
        dimensions.put("stepHeight", 3.0);       // Height difference of the step
    }
    
    /**
     * Draws the Step shape.
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
        double scale = FIXED_SIZE / Math.max(dimensions.get("leftWidth") + dimensions.get("rightWidth"), 
            dimensions.get("totalHeight"));
        
        // Calculate actual dimensions
        int totalHeight = (int)(dimensions.get("totalHeight") * scale);
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightSideHeight = (int)(dimensions.get("rightSideHeight") * scale);
        int stepHeight = (int)(dimensions.get("stepHeight") * scale);
        int totalWidth = leftWidth + rightWidth;
        
        // Calculate drawing position (centered)
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - totalHeight / 2;
        
        try {
            // Create shape path
            Path2D.Double path = new Path2D.Double();
            
            // Draw counter-clockwise from the top-left corner
            path.moveTo(startX, startY);  // Top-left corner
            path.lineTo(startX + leftWidth, startY);  // Top edge left segment (10cm)
            path.lineTo(startX + leftWidth, startY + stepHeight);  // Small vertical line down (3cm)
            path.lineTo(startX + totalWidth, startY + stepHeight);  // Top edge right segment (8cm)
            path.lineTo(startX + totalWidth, startY + totalHeight);  // Right edge (11cm)
            path.lineTo(startX, startY + totalHeight);  // Bottom edge (18cm)
            path.closePath();  // Return to the top-left corner
            
            // Fill shape
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // Draw outline
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("Error drawing Step shape: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Draws the dimensions for the Step shape.
     *
     * @param g The graphics context to draw on.
     * @param width The available width for drawing.
     * @param height The available height for drawing.
     */
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // Use the same calculations as in the draw method
        double scale = FIXED_SIZE / Math.max(dimensions.get("leftWidth") + dimensions.get("rightWidth"), 
            dimensions.get("totalHeight"));
        
        // Calculate actual dimensions
        int totalHeight = (int)(dimensions.get("totalHeight") * scale);
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightSideHeight = (int)(dimensions.get("rightSideHeight") * scale);
        int stepHeight = (int)(dimensions.get("stepHeight") * scale);
        int totalWidth = leftWidth + rightWidth;
        
        // Calculate drawing position
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - totalHeight / 2;
        
        // Draw total height on the left side (11cm)
        drawDimensionLine(g, startX - 40, startY,
                         startX - 40, startY + totalHeight,
                         String.format("%.0f cm", dimensions.get("totalHeight"))); // 11 cm
        
        // Draw left segment of the top edge (10cm)
        drawDimensionLine(g, startX, startY - 20,
                         startX + leftWidth, startY - 20,
                         String.format("%.0f cm", dimensions.get("leftWidth"))); // 10 cm
        
        // Draw right segment of the top edge (8cm)
        drawDimensionLine(g, startX + leftWidth, startY + stepHeight - 20,
                         startX + totalWidth, startY + stepHeight - 20,
                         String.format("%.0f cm", dimensions.get("rightWidth"))); // 8 cm
        
        // Draw right side height (8cm)
        drawDimensionLine(g, startX + totalWidth + 20, startY + stepHeight,
                         startX + totalWidth + 20, startY + totalHeight,
                         String.format("%.0f cm", dimensions.get("rightSideHeight"))); // 8 cm
        
        // Draw total length of the bottom edge (18cm)
        drawDimensionLine(g, startX, startY + totalHeight + 20,
                         startX + totalWidth, startY + totalHeight + 20,
                         String.format("%.0f cm", dimensions.get("leftWidth") + dimensions.get("rightWidth"))); // 18 cm
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
     * Calculates the area of the Step shape.
     *
     * @return The calculated area.
     */
    @Override
    public double calculateArea() {
        // Total area = Area of large rectangle - Area of small rectangle in top-right corner
        double totalArea = (dimensions.get("leftWidth") + dimensions.get("rightWidth")) * 
                          dimensions.get("totalHeight");
        double cutoutArea = dimensions.get("rightWidth") * dimensions.get("stepHeight");
        
        return totalArea - cutoutArea;
    }
    
    /**
     * Gets the solution steps for calculating the area of the Step shape.
     *
     * @return A text description of the solution steps.
     */
    @Override
    public String getSolutionSteps() {
        double totalWidth = dimensions.get("leftWidth") + dimensions.get("rightWidth");
        double totalHeight = dimensions.get("totalHeight");
        double cutoutWidth = dimensions.get("rightWidth");
        double cutoutHeight = dimensions.get("stepHeight");
        
        double largeRectArea = totalWidth * totalHeight;
        double cutoutArea = cutoutWidth * cutoutHeight;
        double totalShapeArea = largeRectArea - cutoutArea;
        
        return String.format(
            "1. Calculate the area of the large rectangle:\n" +
            "   Base \u00d7 Height = (%.0f + %.0f) \u00d7 %.0f = %.0f \u00d7 %.0f = %.0f cm\u00b2\n\n" + // (10+8) x 11 = 18 x 11 = 198
            "2. Calculate the area of the top-right cutout:\n" +
            "   Width \u00d7 Height = %.0f \u00d7 %.0f = %.0f cm\u00b2\n\n" + // 8 x 3 = 24
            "3. Calculate the total area:\n" +
            "   Large rectangle area - Cutout area = %.0f - %.0f = %.0f cm\u00b2", // 198 - 24 = 174
            dimensions.get("leftWidth"), dimensions.get("rightWidth"), dimensions.get("totalHeight"), totalWidth, totalHeight, largeRectArea,
            cutoutWidth, cutoutHeight, cutoutArea,
            largeRectArea, cutoutArea, totalShapeArea
        );
    }
} 