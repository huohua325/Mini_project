package com.shapeville.gui.shapes;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * Interface for compound shape rendering.
 * Defines the required methods that all compound shapes must implement.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public interface ShapeRenderer {
    /**
     * Draws the shape in the specified graphics context.
     *
     * @param g The graphics context to draw on
     * @param width The available width for drawing
     * @param height The available height for drawing
     */
    void draw(Graphics2D g, int width, int height);
    
    /**
     * Draws the dimension annotations for the shape.
     *
     * @param g The graphics context to draw on
     * @param width The available width for drawing
     * @param height The available height for drawing
     */
    void drawDimensions(Graphics2D g, int width, int height);
    
    /**
     * Gets all dimensions of the shape.
     *
     * @return A map containing dimension names and their values
     */
    Map<String, Double> getDimensions();
    
    /**
     * Calculates the area of the shape.
     *
     * @return The calculated area
     */
    double calculateArea();
    
    /**
     * Gets the solution steps for solving the shape's area.
     *
     * @return A text description of the solution steps
     */
    String getSolutionSteps();
} 