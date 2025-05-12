package com.shapeville.game;

import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;

import java.util.*;

public class ShapeRecognition {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Shape2D> shapes2D;
    private List<Shape3D> shapes3D;
    private Set<Shape2D> identified2DShapes;
    private Set<Shape3D> identified3DShapes;
    private static final int REQUIRED_TYPES = 4; // 需要识别的不同形状数量

    public ShapeRecognition() {
        initializeShapes();
    }

    public void initializeShapes() {
        shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
        shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
        identified2DShapes = new HashSet<>();
        identified3DShapes = new HashSet<>();
    }

    public List<Shape2D> getShapes2D() {
        return shapes2D;
    }

    public List<Shape3D> getShapes3D() {
        return shapes3D;
    }

    public boolean check2DAnswer(Shape2D shape, String answer) {
        boolean isCorrect = answer.toLowerCase().equals(shape.getEnglish().toLowerCase());
        if (isCorrect) {
            identified2DShapes.add(shape);
        }
        return isCorrect;
    }

    public boolean check3DAnswer(Shape3D shape, String answer) {
        boolean isCorrect = answer.toLowerCase().equals(shape.getEnglish().toLowerCase());
        if (isCorrect) {
            identified3DShapes.add(shape);
        }
        return isCorrect;
    }

    public boolean is2DComplete() {
        return identified2DShapes.size() >= REQUIRED_TYPES;
    }

    public boolean is3DComplete() {
        return identified3DShapes.size() >= REQUIRED_TYPES;
    }

    public int getIdentified2DCount() {
        return identified2DShapes.size();
    }

    public int getIdentified3DCount() {
        return identified3DShapes.size();
    }

    public boolean isTypeIdentified2D(Shape2D shape) {
        return identified2DShapes.contains(shape);
    }

    public boolean isTypeIdentified3D(Shape3D shape) {
        return identified3DShapes.contains(shape);
    }

    public void reset() {
        initializeShapes();
    }

    public String getRemainingTypesMessage(boolean is2DMode) {
        int remaining = REQUIRED_TYPES - (is2DMode ? identified2DShapes.size() : identified3DShapes.size());
        if (remaining > 0) {
            return String.format("还需要识别 %d 种不同的%s形状", remaining, is2DMode ? "2D" : "3D");
        }
        return "已完成所有形状类型的识别！";
    }

    // 2D形状识别
    public List<Integer> start2DRecognition() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        identified2DShapes.clear();

        for (Shape2D shape : shapes2D) {
            int attempts = 0;
            boolean correct = false;
            while (attempts < 3 && !correct) {
                System.out.println("请识别这个2D形状:");
                String answer = scanner.nextLine().trim().toLowerCase();
                attempts++;
                
                if (check2DAnswer(shape, answer)) {
                    System.out.println("回答正确！\n");
                    correct = true;
                } else {
                    System.out.println("回答错误，请再试一次。");
                }
            }
            if (!correct) {
                System.out.println("正确答案是: " + shape.getEnglish() + "\n");
            }
            attemptsPerShape.add(attempts);
            if (is2DComplete()) break;
        }
        System.out.println("2D形状识别任务结束，返回主菜单。\n");
        return attemptsPerShape;
    }

    // 3D形状识别
    public List<Integer> start3DRecognition() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        identified3DShapes.clear();

        for (Shape3D shape : shapes3D) {
            int attempts = 0;
            boolean correct = false;
            while (attempts < 3 && !correct) {
                System.out.println("请识别这个3D形状: ");
                String answer = scanner.nextLine().trim().toLowerCase();
                attempts++;
                
                if (check3DAnswer(shape, answer)) {
                    System.out.println("回答正确！\n");
                    correct = true;
                } else {
                    System.out.println("回答错误，请再试一次。");
                }
            }
            if (!correct) {
                System.out.println("正确答案是: " + shape.getEnglish() + "\n");
            }
            attemptsPerShape.add(attempts);
            if (is3DComplete()) break;
        }
        System.out.println("3D形状识别任务结束，返回主菜单。\n");
        return attemptsPerShape;
    }
}
