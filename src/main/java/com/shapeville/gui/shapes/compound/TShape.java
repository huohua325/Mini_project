package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * Implementation of the T shape.
 * Composed of two rectangles:
 * - Top rectangle: 36m × 36m, slightly offset to the right but not completely aligned
 * - Bottom rectangle: 60m × 36m
 *
 * @author Your Name
 * @version 1.0
 * @since 2025-05-01
 */
public class TShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    private final double TOP_RECT_OFFSET = 5.0; // Offset distance of the top rectangle to the left (meters)
    
    /**
     * Constructs a new TShape.
     * Initializes the dimensions of the shape.
     */
    public TShape() {
        dimensions = new HashMap<>();
        dimensions.put("topWidth", 36.0);   // Top rectangle width
        dimensions.put("topHeight", 36.0);  // Top rectangle height
        dimensions.put("bottomWidth", 60.0); // Bottom rectangle width
        dimensions.put("bottomHeight", 36.0); // Bottom rectangle height
    }
    
    /**
     * Draws the T shape.
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
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"), 
            dimensions.get("topHeight") + dimensions.get("bottomHeight"));
        
        // Calculate drawing position (centered)
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Calculate actual dimensions
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        int topHeight = (int)(dimensions.get("topHeight") * scale);
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale);
        int topOffset = (int)(TOP_RECT_OFFSET * scale); // Offset of the top rectangle
        
        // Calculate start position - adjusted for inverted T shape with top rectangle slightly offset to the right
        int bottomX = centerX - bottomWidth / 2;
        int bottomY = centerY - bottomHeight / 2;
        int topX = bottomX + bottomWidth - topWidth - topOffset; // Top rectangle offset from the right
        int topY = bottomY - topHeight; // Top rectangle is above the bottom rectangle
        
        try {
            // Create shape path
            Path2D.Double path = new Path2D.Double();
            
            // Draw shape
            path.moveTo(bottomX, bottomY);
            path.lineTo(bottomX + bottomWidth, bottomY);
            path.lineTo(bottomX + bottomWidth, bottomY + bottomHeight);
            path.lineTo(bottomX, bottomY + bottomHeight);
            path.lineTo(bottomX, bottomY);
            
            // Add top rectangle
            path.moveTo(topX, topY);
            path.lineTo(topX + topWidth, topY);
            path.lineTo(topX + topWidth, topY + topHeight);
            path.lineTo(topX, topY + topHeight);
            path.closePath();
            
            // Fill shape
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // Draw outline
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("Error drawing T shape: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Draws the dimensions for the T shape.
     *
     * @param g The graphics context to draw on.
     * @param width The available width for drawing.
     * @param height The available height for drawing.
     */
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // Use the same calculations as in the draw method
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"), 
            dimensions.get("topHeight") + dimensions.get("bottomHeight"));
        
        int centerX = width / 2;
        int centerY = height / 2;
        
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        int topHeight = (int)(dimensions.get("topHeight") * scale);
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale);
        int topOffset = (int)(TOP_RECT_OFFSET * scale); // Offset of the top rectangle
        
        int bottomX = centerX - bottomWidth / 2;
        int bottomY = centerY - bottomHeight / 2;
        int topX = bottomX + bottomWidth - topWidth - topOffset; // Top rectangle offset from the right
        int topY = bottomY - topHeight; // Top rectangle is above the bottom rectangle
        
        // Draw dimensions for the top rectangle
        drawDimensionLine(g, topX, topY - 20,
                         topX + topWidth, topY - 20,
                         String.format("%.0f m", dimensions.get("topWidth"))); // 36m
        drawDimensionLine(g, topX - 20, topY,
                         topX - 20, topY + topHeight,
                         String.format("%.0f m", dimensions.get("topHeight"))); // 36m
        
        // Draw dimensions for the bottom rectangle
        drawDimensionLine(g, bottomX, bottomY + bottomHeight + 20,
                         bottomX + bottomWidth, bottomY + bottomHeight + 20,
                         String.format("%.0f m", dimensions.get("bottomWidth"))); // 60m
        drawDimensionLine(g, bottomX - 20, bottomY,
                         bottomX - 20, bottomY + bottomHeight,
                         String.format("%.0f m", dimensions.get("bottomHeight"))); // 36m
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
     * Calculates the area of the T shape.
     *
     * @return The calculated area.
     */
    @Override
    public double calculateArea() {
        double topArea = dimensions.get("topWidth") * dimensions.get("topHeight");
        double bottomArea = dimensions.get("bottomWidth") * dimensions.get("bottomHeight");
        return topArea + bottomArea;
    }
    
    /**
     * Gets the solution steps for calculating the area of the T shape.
     *
     * @return A text description of the solution steps.
     */
    @Override
    public String getSolutionSteps() {
        double topArea = dimensions.get("topWidth") * dimensions.get("topHeight");
        double bottomArea = dimensions.get("bottomWidth") * dimensions.get("bottomHeight");

        return String.format(
            "1. Calculate the area of the top rectangle:\n" +
            "   Width \u00d7 Height = %.0f \u00d7 %.0f = %.0f m\u00b2\n\n" +
            "2. Calculate the area of the bottom rectangle:\n" +
            "   Width \u00d7 Height = %.0f \u00d7 %.0f = %.0f m\u00b2\n\n" +
            "3. Calculate the total area:\n" +
            "   Top area + Bottom area = %.0f + %.0f = %.0f m\u00b2",
            dimensions.get("topWidth"), dimensions.get("topHeight"), topArea,
            dimensions.get("bottomWidth"), dimensions.get("bottomHeight"), bottomArea,
            topArea, bottomArea, calculateArea()
        );
    }
} 