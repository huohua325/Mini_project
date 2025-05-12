package com.shapeville.game;

import java.util.*;

public class AngleCalculation {
    private final Set<String> identifiedTypes;
    private static final String[] ANGLE_TYPES = {"acute", "right", "obtuse", "straight", "reflex"};
    private static final int REQUIRED_TYPES = 4; // 需要识别的角度类型数量
    private static final int POINTS_PER_QUESTION = 3; // 每道题的基础分值
    private int totalQuestions = 0; // 记录总共尝试的题目数

    public AngleCalculation() {
        this.identifiedTypes = new HashSet<>();
    }

    public String[] getAngleTypes() {
        return ANGLE_TYPES;
    }

    public Set<String> getIdentifiedTypes() {
        return identifiedTypes;
    }

    public String getAngleType(int angle) {
        if (angle == 90) return "right";
        if (angle > 0 && angle < 90) return "acute";
        if (angle > 90 && angle < 180) return "obtuse";
        if (angle == 180) return "straight";
        if (angle > 180 && angle < 360) return "reflex";
        return "unknown";
    }

    public boolean checkAnswer(int angle, String answer) {
        String correctType = getAngleType(angle);
        return correctType.equals(answer);
    }

    public boolean isTypeIdentified(String type) {
        return identifiedTypes.contains(type);
    }

    public void addIdentifiedType(String type) {
        identifiedTypes.add(type);
    }

    public boolean isTaskComplete() {
        return identifiedTypes.size() >= REQUIRED_TYPES;
    }

    public void incrementTotalQuestions() {
        totalQuestions++;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int calculateScore(List<Integer> attempts) {
        int totalScore = 0;
        
        // 计算每道题的得分
        for (int attempt : attempts) {
            if (attempt == 1) totalScore += 3;      // 第1次答对得3分
            else if (attempt == 2) totalScore += 2;  // 第2次答对得2分
            else if (attempt == 3) totalScore += 1;  // 第3次答对得1分
        }
        
        // 返回实际得分，总分为题目数量 * 每题分值
        return totalScore;
    }

    public int getMaxPossibleScore() {
        return totalQuestions * POINTS_PER_QUESTION;
    }

    public String getRemainingTypesMessage() {
        int remaining = REQUIRED_TYPES - identifiedTypes.size();
        if (remaining > 0) {
            return String.format("还需要识别 %d 种不同的角度类型", remaining);
        }
        return "已完成所有角度类型的识别！";
    }
}