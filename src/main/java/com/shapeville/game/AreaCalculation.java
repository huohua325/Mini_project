package com.shapeville.game;

import java.util.*;

/**
 * The AreaCalculation class handles the area calculation game logic.
 * It manages shape types, parameter generation, answer validation,
 * and provides both GUI and command-line interfaces for the game.
 *
 * Features:
 * - Random parameter generation for different shapes
 * - Formula management for area calculations
 * - Answer validation with tolerance
 * - Support for multiple shape types
 * - Both GUI and CLI interfaces
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class AreaCalculation {
    /**
     * Enumeration of supported shape types.
     * Each shape has Chinese, English display names and image resource names.
     */
    public enum ShapeType {
        RECTANGLE("矩形", "Rectangle", "rectangle"), 
        PARALLELOGRAM("平行四边形", "Parallelogram", "parallelogram"), 
        TRIANGLE("三角形", "Triangle", "triangle"), 
        TRAPEZIUM("梯形", "Trapezium", "trapezium");

        private final String chinese;
        private final String english;
        private final String imageName;
        
        /**
         * Constructs a ShapeType with Chinese and English names.
         * @param chinese The Chinese name of the shape
         * @param english The English display name of the shape
         * @param imageName The English name used for image resources
         */
        ShapeType(String chinese, String english, String imageName) {
            this.chinese = chinese;
            this.english = english;
            this.imageName = imageName;
        }
        
        /**
         * Gets the Chinese name of the shape.
         * @return The Chinese name
         */
        public String getChinese() {
            return chinese;
        }

        /**
         * Gets the English display name of the shape.
         * @return The English display name
         */
        public String getEnglish() {
            return english;
        }
        
        /**
         * Gets the image name (English) of the shape.
         * @return The image resource name
         */
        public String getImageName() {
            return imageName;
        }
    }

    private final Random random = new Random();
    private final List<ShapeType> shapes;
    private final Map<String, Double> currentParams;
    private double correctArea;

    /**
     * Constructs a new AreaCalculation instance.
     * Initializes the shape list and parameters map.
     */
    public AreaCalculation() {
        this.shapes = new ArrayList<>(Arrays.asList(ShapeType.values()));
        this.currentParams = new HashMap<>();
        shuffleShapes();
    }

    /**
     * Gets the list of available shapes.
     * @return List of ShapeType objects
     */
    public List<ShapeType> getShapes() {
        return shapes;
    }

    /**
     * Randomly shuffles the order of shapes.
     */
    public void shuffleShapes() {
        Collections.shuffle(shapes);
    }

    /**
     * Gets the current parameters for shape calculation.
     * @return Map of parameter names and values
     */
    public Map<String, Double> getCurrentParams() {
        return currentParams;
    }

    /**
     * Gets the correct area for the current shape.
     * @return The calculated correct area
     */
    public double getCorrectArea() {
        return correctArea;
    }

    /**
     * Generates random parameters for a given shape type.
     * @param shape The shape type to generate parameters for
     */
    public void generateParams(ShapeType shape) {
        currentParams.clear();
        switch (shape) {
            case RECTANGLE:
                currentParams.put("Length", (double)(1 + random.nextInt(20)));
                currentParams.put("Width", (double)(1 + random.nextInt(20)));
                correctArea = currentParams.get("Length") * currentParams.get("Width");
                break;
            case PARALLELOGRAM:
                currentParams.put("Base", (double)(1 + random.nextInt(20)));
                currentParams.put("Height", (double)(1 + random.nextInt(20)));
                correctArea = currentParams.get("Base") * currentParams.get("Height");
                break;
            case TRIANGLE:
                currentParams.put("Base", (double)(1 + random.nextInt(20)));
                currentParams.put("Height", (double)(1 + random.nextInt(20)));
                correctArea = 0.5 * currentParams.get("Base") * currentParams.get("Height");
                break;
            case TRAPEZIUM:
                currentParams.put("Top", (double)(1 + random.nextInt(20)));
                currentParams.put("Bottom", (double)(1 + random.nextInt(20)));
                currentParams.put("Height", (double)(1 + random.nextInt(20)));
                correctArea = 0.5 * (currentParams.get("Top") + currentParams.get("Bottom")) * currentParams.get("Height");
                break;
        }
    }

    /**
     * Gets the area formula for a given shape type.
     * @param shape The shape type to get the formula for
     * @return The formula string
     */
    public String getFormula(ShapeType shape) {
        switch (shape) {
            case RECTANGLE: return "A = Length × Width";
            case PARALLELOGRAM: return "A = Base × Height";
            case TRIANGLE: return "A = 1/2 × Base × Height";
            case TRAPEZIUM: return "A = (Top + Bottom) × Height ÷ 2";
            default: return "";
        }
    }

    /**
     * Gets a formatted string of current parameters.
     * @return Formatted parameter string
     */
    public String getParamsString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : currentParams.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(" = ").append(String.format("%.1f", entry.getValue()));
        }
        return sb.toString();
    }

    /**
     * Checks if the provided answer is correct within tolerance.
     * @param answer The user's answer to check
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(double answer) {
        return Math.abs(answer - correctArea) < 0.1;
    }

    /**
     * Gets the substitution string showing the calculation process.
     * @param shape The shape type for the calculation
     * @return Formatted calculation process string
     */
    public String getSubstitutionString(ShapeType shape) {
        StringBuilder sb = new StringBuilder();
        switch (shape) {
            case RECTANGLE:
                sb.append(String.format("Area = Length × Width = %.1f × %.1f = %.1f", 
                    currentParams.get("Length"), currentParams.get("Width"), correctArea));
                break;
            case PARALLELOGRAM:
                sb.append(String.format("Area = Base × Height = %.1f × %.1f = %.1f", 
                    currentParams.get("Base"), currentParams.get("Height"), correctArea));
                break;
            case TRIANGLE:
                sb.append(String.format("Area = (Base × Height) ÷ 2 = (%.1f × %.1f) ÷ 2 = %.1f", 
                    currentParams.get("Base"), currentParams.get("Height"), correctArea));
                break;
            case TRAPEZIUM:
                sb.append(String.format("Area = (Top + Bottom) × Height ÷ 2 = (%.1f + %.1f) × %.1f ÷ 2 = %.1f", 
                    currentParams.get("Top"), currentParams.get("Bottom"), 
                    currentParams.get("Height"), correctArea));
                break;
        }
        return sb.toString();
    }

    /**
     * Starts the command-line version of the area calculation game.
     * @return List of attempts taken for each shape
     */
    public List<Integer> startAreaCalculation() {
        Set<ShapeType> practiced = new HashSet<>();
        List<Integer> attemptsPerShape = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (practiced.size() < 4) {
                System.out.println("Select a shape to practice:");
                for (int i = 0; i < shapes.size(); i++) {
                    if (!practiced.contains(shapes.get(i))) {
                        System.out.println((i + 1) + ". " + shapes.get(i).getChinese());
                    }
                }
                System.out.println("5. Return to main menu");
                System.out.print("Enter your choice (1-5): ");
                String choice = scanner.nextLine();
                if ("5".equals(choice)) break;

                int idx;
                try {
                    idx = Integer.parseInt(choice) - 1;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again.\n");
                    continue;
                }
                if (idx < 0 || idx >= shapes.size() || practiced.contains(shapes.get(idx))) {
                    System.out.println("Invalid input. Please try again.\n");
                    continue;
                }

                ShapeType shape = shapes.get(idx);
                practiced.add(shape);
                generateParams(shape);

                int attempts = 0;
                boolean correct = false;
                while (attempts < 3 && !correct) {
                    System.out.println("Calculate the area of " + shape.getChinese());
                    System.out.println("Parameters: " + getParamsString());
                    System.out.print("Enter your answer (1 decimal place): ");
                    String answerStr = scanner.nextLine();
                    attempts++;
                    
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (checkAnswer(answer)) {
                            System.out.println("Correct!\n");
                            correct = true;
                        } else {
                            System.out.println("Incorrect. Try again.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
                if (!correct) {
                    System.out.println("The correct answer is: " + String.format("%.1f", correctArea));
                }
                System.out.println("Formula: " + getFormula(shape));
                System.out.println("Substitution: " + getParamsString());
                System.out.println();
                
                attemptsPerShape.add(attempts);
            }
        }
        
        System.out.println("Area calculation task completed. Returning to main menu.\n");
        return attemptsPerShape;
    }
}