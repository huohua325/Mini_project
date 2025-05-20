package com.shapeville.game;

import java.util.*;

/**
 * A class that handles circle-related calculations in the Shapeville application.
 * This class provides functionality for practicing various circle calculations
 * including area and circumference calculations using both radius and diameter.
 *
 * Features:
 * - Four different types of circle calculations
 * - Multiple attempts for each calculation
 * - Interactive command-line interface
 * - Progress tracking
 * - Formula display and validation
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public class CircleCalculation {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Enumeration of different practice types for circle calculations.
     * Each type represents a specific calculation scenario.
     */
    private enum PracticeType {
        RADIUS_AREA, RADIUS_CIRCUM, DIAMETER_AREA, DIAMETER_CIRCUM
    }

    /**
     * Starts the circle calculation practice session.
     * Allows users to practice different types of circle calculations
     * with multiple attempts for each calculation.
     *
     * @return List of integers representing the number of attempts for each completed practice
     */
    public List<Integer> startCircleCalculation() {
        Set<PracticeType> practiced = new HashSet<>();
        List<Integer> attemptsPerPractice = new ArrayList<>();
        Random rand = new Random();

        while (practiced.size() < 4) {
            System.out.println("Choose a practice type:");
            if (!practiced.contains(PracticeType.RADIUS_AREA)) System.out.println("1. Calculate area from radius");
            if (!practiced.contains(PracticeType.RADIUS_CIRCUM)) System.out.println("2. Calculate circumference from radius");
            if (!practiced.contains(PracticeType.DIAMETER_AREA)) System.out.println("3. Calculate area from diameter");
            if (!practiced.contains(PracticeType.DIAMETER_CIRCUM)) System.out.println("4. Calculate circumference from diameter");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice (1-5): ");
            String choice = scanner.nextLine();
            if ("5".equals(choice)) break;

            int r = 1 + rand.nextInt(20);
            int d = 1 + rand.nextInt(20);
            double correct = 0;
            String formula = "";
            String params = "";
            PracticeType type = null;

            switch (choice) {
                case "1":
                    if (practiced.contains(PracticeType.RADIUS_AREA)) { System.out.println("Practice already completed.\n"); continue; }
                    type = PracticeType.RADIUS_AREA;
                    correct = Math.PI * r * r;
                    formula = "A = π × r²";
                    params = "r = " + r;
                    System.out.println("Given radius r = " + r + ", calculate the circle's area.");
                    break;
                case "2":
                    if (practiced.contains(PracticeType.RADIUS_CIRCUM)) { System.out.println("Practice already completed.\n"); continue; }
                    type = PracticeType.RADIUS_CIRCUM;
                    correct = 2 * Math.PI * r;
                    formula = "C = 2 × π × r";
                    params = "r = " + r;
                    System.out.println("Given radius r = " + r + ", calculate the circle's circumference.");
                    break;
                case "3":
                    if (practiced.contains(PracticeType.DIAMETER_AREA)) { System.out.println("Practice already completed.\n"); continue; }
                    type = PracticeType.DIAMETER_AREA;
                    correct = Math.PI * (d / 2.0) * (d / 2.0);
                    formula = "A = π × (d/2)²";
                    params = "d = " + d;
                    System.out.println("Given diameter d = " + d + ", calculate the circle's area.");
                    break;
                case "4":
                    if (practiced.contains(PracticeType.DIAMETER_CIRCUM)) { System.out.println("Practice already completed.\n"); continue; }
                    type = PracticeType.DIAMETER_CIRCUM;
                    correct = Math.PI * d;
                    formula = "C = π × d";
                    params = "d = " + d;
                    System.out.println("Given diameter d = " + d + ", calculate the circle's circumference.");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.\n");
                    continue;
            }

            practiced.add(type);

            int attempts = 0;
            boolean isCorrect = false;
            while (attempts < 3 && !isCorrect) {
                System.out.print("Enter your answer (1 decimal place): ");
                String answerStr = scanner.nextLine();
                attempts++;
                
                try {
                    double answer = Double.parseDouble(answerStr);
                    if (Math.abs(answer - correct) < 0.1) {
                        System.out.println("Correct!\n");
                        isCorrect = true;
                    } else {
                        System.out.println("Incorrect. Try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
            if (!isCorrect) {
                System.out.println("The correct answer is: " + String.format("%.1f", correct));
            }
            System.out.println("Formula: " + formula);
            System.out.println("Values: " + params);
            System.out.println();
            
            attemptsPerPractice.add(attempts);
        }
        System.out.println("Circle calculation practice completed. Returning to main menu.\n");
        return attemptsPerPractice;
    }
}