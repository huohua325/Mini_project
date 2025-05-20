package com.shapeville.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.*;

/**
 * Manages sector calculations in the Shapeville application.
 * This class handles the initialization, management, and evaluation of sector exercises,
 * including both graphical representation and calculation logic.
 *
 * Features:
 * - Predefined sectors with various parameters
 * - Visual rendering of sectors
 * - Area calculation and validation
 * - Solution step generation
 * - Progress tracking
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class SectorCalculation {
    /**
     * Inner class representing a sector with its properties and rendering capabilities.
     * Each sector contains radius, angle, unit of measurement, and calculation results.
     */
    public static class Sector {
        private double radius;
        private double angle;
        private String unit;
        private double correctArea;
        private String solution;

        /**
         * Constructs a new Sector with the specified parameters.
         *
         * @param radius The radius of the sector
         * @param angle The angle of the sector in degrees
         * @param unit The unit of measurement (e.g., cm, m, ft)
         */
        public Sector(double radius, double angle, String unit) {
            this.radius = radius;
            this.angle = angle;
            this.unit = unit;
            calculateCorrectArea();
        }

        /**
         * Gets the formatted name of the sector.
         * @return A string representation of the sector's parameters
         */
        public String getName() {
            return String.format("Sector (r=%.1f%s, θ=%.0f°)", radius, unit, angle);
        }

        /**
         * Draws the sector on the specified graphics context.
         *
         * @param g2d The graphics context to draw on
         * @param width The width of the drawing area
         * @param height The height of the drawing area
         */
        public void draw(Graphics2D g2d, int width, int height) {
            // Set rendering quality
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Calculate drawing parameters
            int margin = 40;
            int size = Math.min(width, height) - 2 * margin;
            int centerX = width / 2;
            int centerY = height / 2;
            
            // Draw sector fill
            g2d.setColor(new Color(255, 200, 200)); // Light pink fill
            g2d.fillArc(centerX - size/2, centerY - size/2, size, size, 0, -(int)angle);
            
            // Draw arc
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(centerX - size/2, centerY - size/2, size, size, 0, 360);
            
            // Draw sector edges
            double radians = Math.toRadians(angle);
            int endX = centerX + (int)(size/2 * Math.cos(radians));
            int endY = centerY - (int)(size/2 * Math.sin(radians));
            g2d.drawLine(centerX, centerY, centerX + size/2, centerY); // Horizontal line
            g2d.drawLine(centerX, centerY, endX, endY); // Diagonal line
            
            // Draw labels
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            // Draw radius label
            String radiusText = radius + " " + unit;
            g2d.drawString(radiusText, centerX + size/4, centerY - 5);
            
            // Draw angle label
            int arcRadius = 30;
            g2d.drawArc(centerX - arcRadius, centerY - arcRadius, 
                       2 * arcRadius, 2 * arcRadius, 0, -(int)angle);
            String angleText = String.format("%.0f°", angle);
            double labelAngle = Math.toRadians(angle/2);
            int labelX = centerX + (int)(arcRadius * 1.5 * Math.cos(labelAngle));
            int labelY = centerY - (int)(arcRadius * 1.5 * Math.sin(labelAngle));
            g2d.drawString(angleText, labelX, labelY);
        }

        /**
         * Calculates the correct area and generates solution steps.
         */
        private void calculateCorrectArea() {
            this.correctArea = Math.PI * radius * radius * angle / 360.0;
            this.solution = String.format(
                "Solution steps:\n" +
                "1. Sector area formula: A = πr²θ/360°\n" +
                "2. Substitute values: A = 3.14 × %.1f² × %.1f° ÷ 360°\n" +
                "3. Calculate result: A = %.1f %s²",
                radius, angle, correctArea, unit
            );
        }

        /**
         * Gets the radius of the sector.
         * @return The radius value
         */
        public double getRadius() { return radius; }

        /**
         * Gets the angle of the sector in degrees.
         * @return The angle value
         */
        public double getAngle() { return angle; }

        /**
         * Gets the unit of measurement.
         * @return The unit string
         */
        public String getUnit() { return unit; }

        /**
         * Gets the correct area value.
         * @return The calculated area
         */
        public double getCorrectArea() { return correctArea; }

        /**
         * Gets the solution steps.
         * @return The detailed solution explanation
         */
        public String getSolution() { return solution; }
    }

    private final List<Sector> sectors;
    private final Set<Integer> practiced;

    /**
     * Constructs a new SectorCalculation instance.
     * Initializes predefined sectors with various parameters.
     */
    public SectorCalculation() {
        sectors = new ArrayList<>();
        practiced = new HashSet<>();
        
        // Add 8 predefined sectors
        sectors.add(new Sector(8, 90, "cm"));    // Sector 1
        sectors.add(new Sector(18, 130, "ft"));  // Sector 2
        sectors.add(new Sector(19, 240, "cm"));  // Sector 3
        sectors.add(new Sector(22, 110, "ft"));  // Sector 4
        sectors.add(new Sector(3.5, 100, "m"));  // Sector 5
        sectors.add(new Sector(8, 270, "in"));   // Sector 6
        sectors.add(new Sector(12, 280, "yd"));  // Sector 7
        sectors.add(new Sector(15, 250, "mm"));  // Sector 8
    }

    /**
     * Gets the list of all sectors.
     * @return The list of sectors
     */
    public List<Sector> getSectors() {
        return sectors;
    }

    /**
     * Gets the set of practiced sector indices.
     * @return The set of practiced indices
     */
    public Set<Integer> getPracticed() {
        return practiced;
    }

    /**
     * Adds a sector index to the practiced set.
     * @param index The index of the practiced sector
     */
    public void addPracticed(int index) {
        practiced.add(index);
    }

    /**
     * Checks if all sectors have been practiced.
     * @return true if all sectors are practiced, false otherwise
     */
    public boolean isComplete() {
        return practiced.size() == sectors.size();
    }

    /**
     * Resets the practice progress.
     */
    public void reset() {
        practiced.clear();
    }

    /**
     * Starts the command-line interface for sector calculations.
     * @return A list of attempts taken for each completed sector
     */
    public List<Integer> startSectorCalculation() {
        List<Integer> attemptsPerSector = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (!isComplete()) {
                System.out.println("Choose a sector to practice (1-" + sectors.size() + "), or enter 0 to return to main menu:");
                for (int i = 0; i < sectors.size(); i++) {
                    if (!practiced.contains(i)) {
                        System.out.println((i+1) + ". " + sectors.get(i).getName());
                    }
                }
                
                String choice = scanner.nextLine();
                if ("0".equals(choice)) break;
                
                int idx;
                try {
                    idx = Integer.parseInt(choice) - 1;
                } catch (Exception e) {
                    System.out.println("Invalid input, please try again.\n");
                    continue;
                }
                
                if (idx < 0 || idx >= sectors.size() || practiced.contains(idx)) {
                    System.out.println("Invalid input, please try again.\n");
                    continue;
                }
                
                Sector sector = sectors.get(idx);
                int attempts = 0;
                boolean areaCorrect = false;
                
                while (attempts < 3 && !areaCorrect) {
                    System.out.println("Given radius r = " + sector.getRadius() + 
                                     ", central angle x = " + sector.getAngle() + "°");
                    
                    System.out.println("Calculate the sector area (use π=3.14, round to 2 decimal places):");
                    String answerStr = scanner.nextLine();
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (Math.abs(answer - sector.getCorrectArea()) < 0.05) {
                            System.out.println("Area calculation correct!");
                            areaCorrect = true;
                        } else {
                            System.out.println("Area calculation incorrect, please continue.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input, please enter a number.");
                    }
                    
                    attempts++;
                }
                
                if (!areaCorrect) {
                    System.out.println("\nCorrect answers:");
                    System.out.println("Area: " + String.format("%.2f", sector.getCorrectArea()));
                    System.out.println("\nDetailed solution:");
                    System.out.println(sector.getSolution());
                }
                System.out.println();
                
                practiced.add(idx);
                attemptsPerSector.add(attempts);
            }
            
            System.out.println("Sector calculation practice completed, returning to main menu.\n");
            return attemptsPerSector;
        }
    }
}