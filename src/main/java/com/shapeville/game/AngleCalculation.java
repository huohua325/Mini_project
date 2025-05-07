package com.shapeville.game;

import java.util.*;

public class AngleCalculation {
    private final List<Integer> angles;
    private final Set<String> identifiedTypes;
    private static final String[] ANGLE_TYPES = {"acute", "right", "obtuse", "straight", "reflex"};

    public AngleCalculation() {
        this.angles = new ArrayList<>();
        this.identifiedTypes = new HashSet<>();
        initializeAngles();
    }

    public void initializeAngles() {
        angles.clear();
        // 只用10的倍数，排除0和360
        for (int i = 10; i < 360; i += 10) {
            angles.add(i);
        }
        Collections.shuffle(angles);
    }

    public String[] getAngleTypes() {
        return ANGLE_TYPES;
    }

    public List<Integer> getAngles() {
        return angles;
    }

    public Set<String> getIdentifiedTypes() {
        return identifiedTypes;
    }

    public boolean hasNextAngle() {
        return !angles.isEmpty();
    }

    public int getNextAngle() {
        if (!angles.isEmpty()) {
            return angles.remove(0);
        }
        return -1;
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

    public void addIdentifiedType(String type) {
        identifiedTypes.add(type);
    }

    public boolean isTaskComplete() {
        return identifiedTypes.size() >= 5 || angles.isEmpty();
    }

    // 命令行模式的入口方法
    public List<Integer> startAngleRecognition() {
        List<Integer> attemptsPerType = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (!isTaskComplete()) {
                int angle = getNextAngle();
                if (angle == -1) break;

                String correctType = getAngleType(angle);
                if (identifiedTypes.contains(correctType)) continue; // 已经识别过该类型

                int attempts = 0;
                boolean correct = false;
                while (attempts < 3 && !correct) {
                    System.out.println("请输入该角度的类型（英文）： " + angle + "°");
                    System.out.println("可选类型：acute, right, obtuse, straight, reflex");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    attempts++;
                    
                    if (checkAnswer(angle, answer)) {
                        System.out.println("回答正确！\n");
                        correct = true;
                        addIdentifiedType(correctType);
                    } else {
                        System.out.println("回答错误，请再试一次。");
                    }
                }
                if (!correct) {
                    System.out.println("正确答案是: " + correctType + "\n");
                    addIdentifiedType(correctType);
                }
                attemptsPerType.add(attempts);
            }
        }
        System.out.println("角度类型识别任务结束，返回主菜单。\n");
        return attemptsPerType;
    }
}