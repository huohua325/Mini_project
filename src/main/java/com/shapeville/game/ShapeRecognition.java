package com.shapeville.game;

import com.shapeville.model.Shape2D;
import com.shapeville.model.Shape3D;

import java.util.*;

public class ShapeRecognition {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Shape2D> shapes2D;
    private List<Shape3D> shapes3D;
    private int correctCount2D;
    private int correctCount3D;

    public ShapeRecognition() {
        initializeShapes();
    }

    public void initializeShapes() {
        shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
        shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
        Collections.shuffle(shapes2D);
        Collections.shuffle(shapes3D);
        correctCount2D = 0;
        correctCount3D = 0;
    }

    public List<Shape2D> getShapes2D() {
        return shapes2D;
    }

    public List<Shape3D> getShapes3D() {
        return shapes3D;
    }

    public boolean check2DAnswer(Shape2D shape, String answer) {
        return answer.equals(shape.getEnglish());
    }

    public boolean check3DAnswer(Shape3D shape, String answer) {
        return answer.equals(shape.getEnglish());
    }

    public void incrementCorrectCount2D() {
        correctCount2D++;
    }

    public void incrementCorrectCount3D() {
        correctCount3D++;
    }

    public boolean is2DComplete() {
        return correctCount2D >= 4;
    }

    public boolean is3DComplete() {
        return correctCount3D >= 4;
    }

    public int getCorrectCount2D() {
        return correctCount2D;
    }

    public int getCorrectCount3D() {
        return correctCount3D;
    }

    public void reset() {
        initializeShapes();
    }

    // 2D形状识别
    public List<Integer> start2DRecognition() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        correctCount2D = 0;

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
                    incrementCorrectCount2D();
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
        correctCount3D = 0;

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
                    incrementCorrectCount3D();
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
