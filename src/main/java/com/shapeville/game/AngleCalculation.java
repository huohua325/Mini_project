package com.shapeville.game;

import java.util.*;

/**
 * A class for angle calculations and type identification in the game.
 * This class handles angle type recognition, scoring, and progress tracking.
 * 
 * @author Shapeville
 * @version 1.0
 */
public class AngleCalculation {
    /** Set of angle types that have been correctly identified by the player */
    private final Set<String> identifiedTypes;
    
    /** Array of all possible angle types in the game */
    private static final String[] ANGLE_TYPES = {"acute", "right", "obtuse", "straight", "reflex"};
    
    /** Number of different angle types required to complete the task */
    private static final int REQUIRED_TYPES = 4;
    
    /** Base points awarded for each question */
    private static final int POINTS_PER_QUESTION = 3;
    
    /** Total number of questions attempted */
    private int totalQuestions = 0;

    /**
     * Constructs a new AngleCalculation instance.
     * Initializes the set of identified angle types.
     */
    public AngleCalculation() {
        this.identifiedTypes = new HashSet<>();
    }

    /**
     * Gets all possible angle types in the game.
     *
     * @return Array of angle type strings
     */
    public String[] getAngleTypes() {
        return ANGLE_TYPES;
    }

    /**
     * Gets the set of angle types that have been correctly identified.
     *
     * @return Set of identified angle types
     */
    public Set<String> getIdentifiedTypes() {
        return identifiedTypes;
    }

    /**
     * Determines the type of angle based on its degree measure.
     *
     * @param angle The angle measure in degrees
     * @return The type of angle ("right", "acute", "obtuse", "straight", "reflex", or "unknown")
     */
    public String getAngleType(int angle) {
        if (angle == 90) return "right";
        if (angle > 0 && angle < 90) return "acute";
        if (angle > 90 && angle < 180) return "obtuse";
        if (angle == 180) return "straight";
        if (angle > 180 && angle < 360) return "reflex";
        return "unknown";
    }

    /**
     * Checks if the provided answer matches the correct angle type.
     *
     * @param angle The angle measure in degrees
     * @param answer The player's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(int angle, String answer) {
        String correctType = getAngleType(angle);
        return correctType.equals(answer);
    }

    /**
     * Checks if a specific angle type has been identified.
     *
     * @param type The angle type to check
     * @return true if the type has been identified, false otherwise
     */
    public boolean isTypeIdentified(String type) {
        return identifiedTypes.contains(type);
    }

    /**
     * Adds a new angle type to the set of identified types.
     *
     * @param type The angle type to add
     */
    public void addIdentifiedType(String type) {
        identifiedTypes.add(type);
    }

    /**
     * Checks if enough angle types have been identified to complete the task.
     *
     * @return true if the required number of types have been identified, false otherwise
     */
    public boolean isTaskComplete() {
        return identifiedTypes.size() >= REQUIRED_TYPES;
    }

    /**
     * Increments the total number of questions attempted.
     */
    public void incrementTotalQuestions() {
        totalQuestions++;
    }

    /**
     * Gets the total number of questions attempted.
     *
     * @return The total number of questions
     */
    public int getTotalQuestions() {
        return totalQuestions;
    }

    /**
     * Calculates the total score based on the number of attempts for each question.
     * Scoring system:
     * - 3 points for correct answer on first attempt
     * - 2 points for correct answer on second attempt
     * - 1 point for correct answer on third attempt
     *
     * @param attempts List of attempt counts for each question
     * @return The total score
     */
    public int calculateScore(List<Integer> attempts) {
        int totalScore = 0;
        
        // Calculate points for each question based on attempts
        for (int attempt : attempts) {
            if (attempt == 1) totalScore += 3;      // 3 points for first attempt
            else if (attempt == 2) totalScore += 2;  // 2 points for second attempt
            else if (attempt == 3) totalScore += 1;  // 1 point for third attempt
        }
        
        return totalScore;
    }

    /**
     * Calculates the maximum possible score based on the total number of questions.
     *
     * @return The maximum possible score
     */
    public int getMaxPossibleScore() {
        return totalQuestions * POINTS_PER_QUESTION;
    }

    /**
     * Gets a message indicating the remaining number of angle types to be identified.
     *
     * @return A status message about remaining angle types to identify
     */
    public String getRemainingTypesMessage() {
        int remaining = REQUIRED_TYPES - identifiedTypes.size();
        if (remaining > 0) {
            return String.format("You need to identify %d more angle types", remaining);
        }
        return "You have completed all angle type identifications!";
    }
}