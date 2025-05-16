package com.shapeville.game;

import java.util.*;
import com.shapeville.gui.shapes.ShapeRenderer;
import com.shapeville.gui.shapes.compound.*;

/**
 * Manages the compound shape calculation exercises in the Shapeville application.
 * This class handles the initialization, management, and evaluation of compound shape
 * exercises, including both GUI and command-line interfaces.
 *
 * The class provides functionality for:
 * - Initializing various compound shapes with their properties
 * - Managing user progress through exercises
 * - Evaluating user answers
 * - Tracking practice attempts
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class CompoundShapeCalculation {
    /**
     * Inner class representing a compound shape with its properties and rendering information.
     * Each compound shape contains a name, description, correct area value, solution steps,
     * and a renderer for visual representation.
     */
    public static class CompoundShape {
        private final String name;
        private final String description;
        private final double correctArea;
        private final String solution;
        private final ShapeRenderer renderer;

        /**
         * Constructs a new CompoundShape with the specified properties.
         *
         * @param name The name of the compound shape
         * @param description The description of the shape's components and measurements
         * @param correctArea The correct area value for the shape
         * @param solution The detailed solution steps for calculating the area
         * @param renderer The renderer for visual representation of the shape
         */
        public CompoundShape(String name, String description, double correctArea, 
                           String solution, ShapeRenderer renderer) {
            this.name = name;
            this.description = description;
            this.correctArea = correctArea;
            this.solution = solution;
            this.renderer = renderer;
        }

        /**
         * Gets the name of the compound shape.
         * @return The shape's name
         */
        public String getName() { return name; }

        /**
         * Gets the description of the compound shape.
         * @return The shape's description
         */
        public String getDescription() { return description; }

        /**
         * Gets the correct area value for the compound shape.
         * @return The correct area value
         */
        public double getCorrectArea() { return correctArea; }

        /**
         * Gets the solution steps for calculating the area.
         * @return The detailed solution steps
         */
        public String getSolution() { return solution; }

        /**
         * Gets the shape renderer for visual representation.
         * @return The shape renderer
         */
        public ShapeRenderer getRenderer() { return renderer; }
    }

    private final List<CompoundShape> shapes;
    private final Set<Integer> practiced;

    /**
     * Constructs a new CompoundShapeCalculation instance.
     * Initializes the shapes list and practiced set.
     * @throws IllegalStateException if no shapes are initialized
     */
    public CompoundShapeCalculation() {
        System.out.println("Initializing CompoundShapeCalculation...");
        this.shapes = initializeShapes();
        this.practiced = new HashSet<>();
        
        if (shapes.isEmpty()) {
            System.err.println("Error: Shape list is empty after initialization");
            throw new IllegalStateException("At least one compound shape is required");
        }
        System.out.println("CompoundShapeCalculation initialized with " + shapes.size() + " shapes");
    }

    /**
     * Initializes the list of compound shapes with their properties.
     * @return A list of initialized compound shapes
     */
    private List<CompoundShape> initializeShapes() {
        List<CompoundShape> shapeList = new ArrayList<>();
        
        try {
            System.out.println("Initializing shapes...");
            
            // 1. Arrow Shape
            ArrowShape arrowShape = new ArrowShape();
            shapeList.add(new CompoundShape(
                "Arrow Shape",
                "Composed of a rectangle (14×14) and a trapezoid (base 14, top 5, height 5).\nCalculate the total area.",
                arrowShape.calculateArea(),
                arrowShape.getSolutionSteps(),
                arrowShape
            ));
            
            // 2. T Shape
            TShape tShape = new TShape();
            shapeList.add(new CompoundShape(
                "T Shape",
                "Composed of a top rectangle (36×36) and a bottom rectangle (60×36).\nCalculate the total area.",
                tShape.calculateArea(),
                tShape.getSolutionSteps(),
                tShape
            ));
            
            // 3. Trapezoid
            TrapezoidShape trapezoidShape = new TrapezoidShape();
            shapeList.add(new CompoundShape(
                "Trapezoid",
                "Base 20m, top 9m, height 11m, right side 14m.\nCalculate the total area.",
                trapezoidShape.calculateArea(),
                trapezoidShape.getSolutionSteps(),
                trapezoidShape
            ));
            
            // 4. Stair Shape
            StairShape stairShape = new StairShape();
            shapeList.add(new CompoundShape(
                "Stair Shape",
                "Main rectangle (20×21) minus two cutouts (11×11 and 10×10).\nCalculate the total area.",
                stairShape.calculateArea(),
                stairShape.getSolutionSteps(),
                stairShape
            ));
            
            // 5. Step Rectangle
            StepShape stepShape = new StepShape();
            shapeList.add(new CompoundShape(
                "Step Rectangle",
                "Combination of three rectangles: 11×10, 8×8, 8×8.\nCalculate the total area.",
                stepShape.calculateArea(),
                stepShape.getSolutionSteps(),
                stepShape
            ));
            
            // 6. Double Stair Shape
            DoubleStairShape doubleStairShape = new DoubleStairShape();
            shapeList.add(new CompoundShape(
                "Double Stair Shape",
                "Main rectangle (19×18) and top-right rectangle (16×16) minus cutout (16×16).\nCalculate the total area.",
                doubleStairShape.calculateArea(),
                doubleStairShape.getSolutionSteps(),
                doubleStairShape
            ));
            
            // 7. House Shape
            HouseShape houseShape = new HouseShape();
            shapeList.add(new CompoundShape(
                "House Shape",
                "Bottom rectangle (14×5) and two triangles (base 14 height 12 and base 16 height 13).\nCalculate the total area.",
                houseShape.calculateArea(),
                houseShape.getSolutionSteps(),
                houseShape
            ));
            
            // 8. Complex Stair
            ComplexStairShape complexStairShape = new ComplexStairShape();
            shapeList.add(new CompoundShape(
                "Complex Stair",
                "Four rectangles: 24×6, 10×12, 12×12, 2×12.\nCalculate the total area.",
                complexStairShape.calculateArea(),
                complexStairShape.getSolutionSteps(),
                complexStairShape
            ));
            
            // 9. Irregular Quadrilateral
            IrregularShape irregularShape = new IrregularShape();
            shapeList.add(new CompoundShape(
                "Irregular Quadrilateral",
                "Base 4m, left side 4m, top 16m, right diagonal approx. 17m.\nCalculate the total area.",
                irregularShape.calculateArea(),
                irregularShape.getSolutionSteps(),
                irregularShape
            ));
            
            System.out.println("All shapes initialized, total count: " + shapeList.size());
            
        } catch (Exception e) {
            System.err.println("Error initializing shapes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return shapeList;
    }

    /**
     * Gets the list of all compound shapes.
     * @return The list of compound shapes
     */
    public List<CompoundShape> getShapes() {
        return shapes;
    }

    /**
     * Gets the set of indices of practiced shapes.
     * @return The set of practiced shape indices
     */
    public Set<Integer> getPracticed() {
        return practiced;
    }

    /**
     * Adds a shape index to the set of practiced shapes.
     * @param index The index of the practiced shape
     */
    public void addPracticed(int index) {
        practiced.add(index);
    }

    /**
     * Checks if all shapes have been practiced.
     * @return true if all shapes have been practiced, false otherwise
     */
    public boolean isComplete() {
        return practiced.size() >= shapes.size();
    }

    /**
     * Checks if the provided answer is correct for the given shape.
     * @param shapeIndex The index of the shape to check
     * @param answer The user's answer
     * @return true if the answer is within acceptable margin of error, false otherwise
     */
    public boolean checkAnswer(int shapeIndex, double answer) {
        if (shapeIndex < 0 || shapeIndex >= shapes.size()) {
            return false;
        }
        return Math.abs(answer - shapes.get(shapeIndex).getCorrectArea()) < 0.1;
    }

    /**
     * Resets the practice progress, clearing all practiced shapes.
     */
    public void reset() {
        practiced.clear();
    }

    /**
     * Starts the command-line interface for compound shape calculation practice.
     * @return A list of attempts taken for each completed shape
     */
    public List<Integer> startCompoundShapeCalculation() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (!isComplete()) {
                System.out.println("Choose a compound shape to practice (1-" + shapes.size() + "), or enter 0 to return to main menu:");
                for (int i = 0; i < shapes.size(); i++) {
                    if (!practiced.contains(i)) {
                        System.out.println((i+1) + ". " + shapes.get(i).getName());
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
                if (idx < 0 || idx >= shapes.size() || practiced.contains(idx)) {
                    System.out.println("Invalid input, please try again.\n");
                    continue;
                }
                
                CompoundShape shape = shapes.get(idx);
                int attempts = 0;
                boolean correct = false;
                
                while (attempts < 3) {
                    System.out.println("Description: " + shape.getDescription());
                    System.out.print("Enter the area of the compound shape (1 decimal place): ");
                    String answerStr = scanner.nextLine();
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (checkAnswer(idx, answer)) {
                            System.out.println("Correct answer!\n");
                            correct = true;
                            break;
                        } else {
                            System.out.println("Incorrect answer, please try again.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input, please enter a number.");
                    }
                    attempts++;
                }
                
                if (!correct) {
                    System.out.println("The correct answer is: " + String.format("%.1f", shape.getCorrectArea()));
                    System.out.println("Detailed solution: " + shape.getSolution());
                }
                System.out.println();
                
                addPracticed(idx);
                attemptsPerShape.add(attempts + 1);
            }
        }
        
        System.out.println("Compound shape calculation practice completed, returning to main menu.\n");
        return attemptsPerShape;
    }
}