package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * Implementation of the Irregular Quadrilateral shape (Shape 6 in the list).
 * Composed of a rectangle and a right triangle:
 * - Bottom rectangle: 4m × 2m
 * - Top right triangle:
 *   * Left side (shared with rectangle): 4m
 *   * Right side: 2m
 *   * Hypotenuse: 16m
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class IrregularShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    /**
     * Constructs a new IrregularShape.
     * Initializes the dimensions of the shape.
     */
    public IrregularShape() {
        dimensions = new HashMap<>();
        dimensions.put("baseWidth", 4.0);    // Bottom rectangle width
        dimensions.put("baseHeight", 2.0);   // Bottom rectangle height
        dimensions.put("leftSide", 4.0);     // Triangle left side (shared with rectangle)
        dimensions.put("rightSide", 2.0);    // Triangle right side
        dimensions.put("hypotenuse", 16.0);  // Triangle hypotenuse
    }
    
    /**
     * Draws the Irregular Quadrilateral shape.
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
        double scale = FIXED_SIZE / Math.max(dimensions.get("baseWidth"),
            dimensions.get("baseHeight") + dimensions.get("leftSide"));
        
        // Calculate actual dimensions
        int baseWidth = (int)(dimensions.get("baseWidth") * scale);
        int baseHeight = (int)(dimensions.get("baseHeight") * scale);
        int leftSide = (int)(dimensions.get("leftSide") * scale);
        int rightSide = (int)(dimensions.get("rightSide") * scale);
        
        // Calculate drawing position (centered)
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - baseWidth / 2;
        int startY = centerY - (baseHeight + leftSide) / 2;
        
        try {
            // Create shape path
            Path2D.Double path = new Path2D.Double();
            
            // Draw counter-clockwise from the bottom-left corner
            path.moveTo(startX, startY + leftSide + baseHeight);              // Bottom-left corner
            path.lineTo(startX + baseWidth, startY + leftSide + baseHeight);  // Bottom edge
            path.lineTo(startX + baseWidth, startY + leftSide);              // Right vertical line
            path.lineTo(startX + baseWidth, startY + rightSide);             // Top-right edge
            path.lineTo(startX, startY);                                     // Diagonal to top-left corner
            path.closePath();
            
            // Fill shape
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // Draw outline
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("Error drawing Irregular Quadrilateral shape: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Draws the dimensions for the Irregular Quadrilateral shape.
     *
     * @param g The graphics context to draw on.
     * @param width The available width for drawing.
     * @param height The available height for drawing.
     */
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // Use the same calculations as in the draw method
        double scale = FIXED_SIZE / Math.max(dimensions.get("baseWidth"),
            dimensions.get("baseHeight") + dimensions.get("leftSide"));
        
        // Calculate actual dimensions
        int baseWidth = (int)(dimensions.get("baseWidth") * scale);
        int baseHeight = (int)(dimensions.get("baseHeight") * scale);
        int leftSide = (int)(dimensions.get("leftSide") * scale);
        int rightSide = (int)(dimensions.get("rightSide") * scale);
        
        // Calculate drawing position
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - baseWidth / 2;
        int startY = centerY - (baseHeight + leftSide) / 2;
        
        // Draw bottom rectangle dimension
        drawDimensionLine(g, startX, startY + leftSide + baseHeight + 20,
                         startX + baseWidth, startY + leftSide + baseHeight + 20,
                         String.format("%.0f m", dimensions.get("baseWidth"))); // 4m
        
        // Draw left rectangle height (move 40 pixels to the left)
        drawDimensionLine(g, startX - 40, startY + leftSide,
                         startX - 40, startY + leftSide + baseHeight,
                         String.format("%.0f m", dimensions.get("baseHeight"))); // 2m
        
        // Draw left triangle height (move 40 pixels to the left)
        drawDimensionLine(g, startX - 40, startY,
                         startX - 40, startY + leftSide,
                         String.format("%.0f m", dimensions.get("leftSide"))); // 4m
        
        // Draw right side height
        drawDimensionLine(g, startX + baseWidth + 20, startY + rightSide,
                         startX + baseWidth + 20, startY + leftSide,
                         String.format("%.0f m", dimensions.get("leftSide") - dimensions.get("rightSide"))); // 4m-2m = 2m
        
        // Draw hypotenuse length (represented by a dashed line)
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                  10.0f, new float[]{5.0f}, 0.0f));
        drawDimensionLine(g, startX + 10, startY - 10,
                         startX + baseWidth + 10, startY + rightSide - 10,
                         String.format("%.0f m", dimensions.get("hypotenuse"))); // 16m
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
     * Calculates the area of the Irregular Quadrilateral shape.
     *
     * @return The calculated area.
     */
    @Override
    public double calculateArea() {
        // Area of the rectangle
        double rectangleArea = dimensions.get("baseWidth") * dimensions.get("baseHeight");
        
        // Area of the triangle
        double triangleHeight = dimensions.get("leftSide") - dimensions.get("rightSide");
        double triangleBase = dimensions.get("baseWidth");
        double triangleArea = triangleBase * triangleHeight / 2.0;
        
        return rectangleArea + triangleArea;
    }
    
    /**
     * Gets the solution steps for calculating the area of the Irregular Quadrilateral shape.
     *
     * @return A text description of the solution steps.
     */
    @Override
    public String getSolutionSteps() {
        double rectangleArea = dimensions.get("baseWidth") * dimensions.get("baseHeight");
        double triangleHeight = dimensions.get("leftSide") - dimensions.get("rightSide");
        double triangleBase = dimensions.get("baseWidth");
        double triangleArea = triangleBase * triangleHeight / 2.0;
        
        return String.format(
            "1. Divide the irregular quadrilateral into a rectangle and a right triangle.\n\n" +
            "2. Calculate the area of the rectangle:\n" +
            "   Base \u00d7 Height = %.0f \u00d7 %.0f = %.0f m\u00b2\n\n" + // 4 x 2 = 8
            "3. Calculate the area of the right triangle:\n" +
            "   Base \u00d7 Height \u00f7 2\n" +
            "   Base = %.0f m\n" + // 4
            "   Height = Left side - Right side = %.0f - %.0f = %.0f m\n" + // 4 - 2 = 2
            "   Area = %.0f \u00d7 %.0f \u00f7 2 = %.0f m\u00b2\n\n" + // 4 x 2 / 2 = 4
            "4. Calculate the total area:\n" +
            "   Rectangle area + Triangle area = %.0f + %.0f = %.0f m\u00b2", // 8 + 4 = 12
            dimensions.get("baseWidth"), dimensions.get("baseHeight"), rectangleArea,
            triangleBase, dimensions.get("leftSide"), dimensions.get("rightSide"), triangleHeight, triangleBase, triangleHeight, triangleArea,
            rectangleArea, triangleArea, calculateArea()
        );
    }
} 