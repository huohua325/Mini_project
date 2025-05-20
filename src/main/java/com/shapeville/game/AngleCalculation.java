package com.shapeville.game;

import java.util.*;

/**
 * A class that handles angle type calculations and scoring in the ShapeVille game.
 * This class manages the identification of different angle types and tracks the player's progress.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class AngleCalculation {
    private final Set<String> identifiedTypes;
    private static final String[] ANGLE_TYPES = {"acute", "right", "obtuse", "straight", "reflex"};
    private static final int REQUIRED_TYPES = 4; // Number of angle types required to identify
    private static final int POINTS_PER_QUESTION = 3; // Base points per question
    private int totalQuestions = 0; // Total number of attempted questions

    /**
     * Constructs a new AngleCalculation instance.
     * Initializes the set of identified angle types.
     */
    public AngleCalculation() {
        this.identifiedTypes = new HashSet<>();
    }

    /**
     * Gets the array of possible angle types.
     * @return Array of angle type strings
     */
    public String[] getAngleTypes() {
        return ANGLE_TYPES;
    }

    /**
     * Gets the set of angle types that have been correctly identified.
     * @return Set of identified angle types
     */
    public Set<String> getIdentifiedTypes() {
        return identifiedTypes;
    }

    /**
     * Determines the type of angle based on its measure in degrees.
     * @param angle The angle measure in degrees
     * @return The type of angle as a string ("acute", "right", "obtuse", "straight", "reflex", or "unknown")
     */
    public String getAngleType(int angle) {
        if (angle <= 0 || angle >= 360) return "unknown";
        if (angle == 90) return "right";
        if (angle > 0 && angle < 90) return "acute";
        if (angle > 90 && angle < 180) return "obtuse";
        if (angle == 180) return "straight";
        if (angle > 180 && angle < 360) return "reflex";
        return "unknown";
    }

    /**
     * Checks if the provided answer matches the correct angle type.
     * @param angle The angle measure to check
     * @param answer The user's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(int angle, String answer) {
        String correctType = getAngleType(angle);
        return correctType.equals(answer);
    }

    /**
     * Checks if a specific angle type has been identified.
     * @param type The angle type to check
     * @return true if the type has been identified, false otherwise
     */
    public boolean isTypeIdentified(String type) {
        return identifiedTypes.contains(type);
    }

    /**
     * Adds a newly identified angle type to the set.
     * @param type The angle type to add
     */
    public void addIdentifiedType(String type) {
        identifiedTypes.add(type);
    }

    /**
     * Checks if the required number of angle types have been identified.
     * @return true if the task is complete, false otherwise
     */
    public boolean isTaskComplete() {
        return identifiedTypes.size() >= REQUIRED_TYPES;
    }

    /**
     * Increments the total number of attempted questions.
     */
    public void incrementTotalQuestions() {
        totalQuestions++;
    }

    /**
     * Gets the total number of attempted questions.
     * @return The total number of questions attempted
     */
    public int getTotalQuestions() {
        return totalQuestions;
    }

    /**
     * Calculates the total score based on the number of attempts for each question.
     * 3 points for correct answer on first attempt
     * 2 points for correct answer on second attempt
     * 1 point for correct answer on third attempt
     *
     * @param attempts List of attempt counts for each question
     * @return The total score
     */
    public int calculateScore(List<Integer> attempts) {
        int totalScore = 0;
        
        for (int attempt : attempts) {
            if (attempt == 1) totalScore += 3;      // 3 points for first attempt
            else if (attempt == 2) totalScore += 2;  // 2 points for second attempt
            else if (attempt == 3) totalScore += 1;  // 1 point for third attempt
        }
        
        return totalScore;
    }

    /**
     * Calculates the maximum possible score based on total questions.
     * @return The maximum possible score
     */
    public int getMaxPossibleScore() {
        return totalQuestions * POINTS_PER_QUESTION;
    }

    /**
     * Gets a message indicating the remaining number of angle types to identify.
     * @return A status message about remaining angle types
     */
    public String getRemainingTypesMessage() {
        int remaining = REQUIRED_TYPES - identifiedTypes.size();
        if (remaining > 0) {
            return String.format("You need to identify %d more angle types", remaining);
        }
        return "All angle types have been successfully identified!";
    }
}