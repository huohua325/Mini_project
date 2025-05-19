package com.shapeville.game;

import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;

import java.util.*;

/**
 * The ShapeRecognition class manages the shape recognition game logic.
 * It handles both 2D and 3D shape recognition tasks, tracks progress,
 * and validates user answers.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public class ShapeRecognition {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Shape2D> shapes2D;
    private List<Shape3D> shapes3D;
    private Set<Shape2D> identified2DShapes;
    private Set<Shape3D> identified3DShapes;
    /** Number of different shape types required to complete the task */
    private static final int REQUIRED_TYPES = 4;

    /**
     * Constructs a new ShapeRecognition instance and initializes the shapes.
     */
    public ShapeRecognition() {
        initializeShapes();
    }

    /**
     * Initializes the shape collections and shuffles them.
     */
    public void initializeShapes() {
        shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
        shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
        identified2DShapes = new HashSet<>();
        identified3DShapes = new HashSet<>();
    }

    /**
     * Gets the list of 2D shapes.
     * @return List of 2D shapes
     */
    public List<Shape2D> getShapes2D() {
        return shapes2D;
    }

    /**
     * Gets the list of 3D shapes.
     * @return List of 3D shapes
     */
    public List<Shape3D> getShapes3D() {
        return shapes3D;
    }

    /**
     * Checks if the answer for a 2D shape is correct.
     * @param shape The 2D shape to check
     * @param answer The user's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean check2DAnswer(Shape2D shape, String answer) {
        boolean isCorrect = answer.toLowerCase().equals(shape.getEnglish().toLowerCase());
        if (isCorrect) {
            identified2DShapes.add(shape);
        }
        return isCorrect;
    }

    /**
     * Checks if the answer for a 3D shape is correct.
     * @param shape The 3D shape to check
     * @param answer The user's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean check3DAnswer(Shape3D shape, String answer) {
        boolean isCorrect = answer.toLowerCase().equals(shape.getEnglish().toLowerCase());
        if (isCorrect) {
            identified3DShapes.add(shape);
        }
        return isCorrect;
    }

    /**
     * Checks if the required number of 2D shapes have been identified.
     * @return true if enough 2D shapes have been identified, false otherwise
     */
    public boolean is2DComplete() {
        return identified2DShapes.size() >= REQUIRED_TYPES;
    }

    /**
     * Checks if the required number of 3D shapes have been identified.
     * @return true if enough 3D shapes have been identified, false otherwise
     */
    public boolean is3DComplete() {
        return identified3DShapes.size() >= REQUIRED_TYPES;
    }

    /**
     * Gets the number of correctly identified 2D shapes.
     * @return Number of identified 2D shapes
     */
    public int getIdentified2DCount() {
        return identified2DShapes.size();
    }

    /**
     * Gets the number of correctly identified 3D shapes.
     * @return Number of identified 3D shapes
     */
    public int getIdentified3DCount() {
        return identified3DShapes.size();
    }

    /**
     * Checks if a specific 2D shape has been correctly identified.
     * @param shape The 2D shape to check
     * @return true if the shape has been identified, false otherwise
     */
    public boolean isTypeIdentified2D(Shape2D shape) {
        return identified2DShapes.contains(shape);
    }

    /**
     * Checks if a specific 3D shape has been correctly identified.
     * @param shape The 3D shape to check
     * @return true if the shape has been identified, false otherwise
     */
    public boolean isTypeIdentified3D(Shape3D shape) {
        return identified3DShapes.contains(shape);
    }

    /**
     * Resets the game state by reinitializing all shapes.
     */
    public void reset() {
        initializeShapes();
    }

    /**
     * Gets a message indicating the remaining shapes to be identified.
     * @param is2DMode Whether the current mode is 2D or 3D
     * @return A message describing the remaining task
     */
    public String getRemainingTypesMessage(boolean is2DMode) {
        int remaining = REQUIRED_TYPES - (is2DMode ? identified2DShapes.size() : identified3DShapes.size());
        if (remaining > 0) {
            return String.format("Need to identify %d more different %s shapes", 
                               remaining, is2DMode ? "2D" : "3D");
        }
        return "All shape types have been identified!";
    }

    /**
     * Starts the 2D shape recognition task.
     * @return List of attempts taken for each shape
     */
    public List<Integer> start2DRecognition() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        identified2DShapes.clear();

        for (Shape2D shape : shapes2D) {
            int attempts = 0;
            boolean correct = false;
            while (attempts < 3 && !correct) {
                System.out.println("Please identify this 2D shape:");
                String answer = scanner.nextLine().trim().toLowerCase();
                attempts++;
                
                if (check2DAnswer(shape, answer)) {
                    System.out.println("Correct answer!\n");
                    correct = true;
                } else {
                    System.out.println("Incorrect. Please try again.");
                }
            }
            if (!correct) {
                System.out.println("The correct answer is: " + shape.getEnglish() + "\n");
            }
            attemptsPerShape.add(attempts);
            if (is2DComplete()) break;
        }
        System.out.println("2D shape recognition task completed. Returning to main menu.\n");
        return attemptsPerShape;
    }

    /**
     * Starts the 3D shape recognition task.
     * @return List of attempts taken for each shape
     */
    public List<Integer> start3DRecognition() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        identified3DShapes.clear();

        for (Shape3D shape : shapes3D) {
            int attempts = 0;
            boolean correct = false;
            while (attempts < 3 && !correct) {
                System.out.println("Please identify this 3D shape:");
                String answer = scanner.nextLine().trim().toLowerCase();
                attempts++;
                
                if (check3DAnswer(shape, answer)) {
                    System.out.println("Correct answer!\n");
                    correct = true;
                } else {
                    System.out.println("Incorrect. Please try again.");
                }
            }
            if (!correct) {
                System.out.println("The correct answer is: " + shape.getEnglish() + "\n");
            }
            attemptsPerShape.add(attempts);
            if (is3DComplete()) break;
        }
        System.out.println("3D shape recognition task completed. Returning to main menu.\n");
        return attemptsPerShape;
    }
}
