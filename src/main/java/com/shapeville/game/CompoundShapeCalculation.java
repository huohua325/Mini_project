package com.shapeville.game;

import java.util.*;

public class CompoundShapeCalculation {
    // 复合形状参数类
    public static class CompoundShape {
        private final String name;
        private final String description;
        private final double correctArea;
        private final String solution;

        public CompoundShape(String name, String description, double correctArea, String solution) {
            this.name = name;
            this.description = description;
            this.correctArea = correctArea;
            this.solution = solution;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getCorrectArea() { return correctArea; }
        public String getSolution() { return solution; }
    }

    private final List<CompoundShape> shapes;
    private final Set<Integer> practiced;

    public CompoundShapeCalculation() {
        this.shapes = initializeShapes();
        this.practiced = new HashSet<>();
    }

    private List<CompoundShape> initializeShapes() {
        return Arrays.asList(
            new CompoundShape(
                "复合形状1",
                "由一个矩形(14x15)和一个三角形(底14,高5)组成",
                14*15 + 0.5*14*5,
                "矩形面积: 14x15=210, 三角形面积: 0.5x14x5=35, 总面积: 210+35=245"
            ),
            new CompoundShape(
                "复合形状2",
                "由一个正方形(边长10)和一个等腰三角形(底10,高8)组成",
                10*10 + 0.5*10*8,
                "正方形面积: 10x10=100, 三角形面积: 0.5x10x8=40, 总面积: 100+40=140"
            ),
            new CompoundShape(
                "复合形状3",
                "由两个相等的矩形(8x12)组成的L形",
                8*12 + 8*12,
                "第一个矩形: 8x12=96, 第二个矩形: 8x12=96, 总面积: 96+96=192"
            ),
            new CompoundShape(
                "复合形状4",
                "由一个圆形(半径6)和一个正方形(边长12)组成",
                Math.PI*6*6 + 12*12,
                "圆形面积: πx6²=113.1, 正方形面积: 12x12=144, 总面积: 113.1+144=257.1"
            )
        );
    }

    public List<CompoundShape> getShapes() {
        return shapes;
    }

    public Set<Integer> getPracticed() {
        return practiced;
    }

    public void addPracticed(int index) {
        practiced.add(index);
    }

    public boolean isComplete() {
        return practiced.size() >= shapes.size();
    }

    public boolean checkAnswer(int shapeIndex, double answer) {
        if (shapeIndex < 0 || shapeIndex >= shapes.size()) {
            return false;
        }
        return Math.abs(answer - shapes.get(shapeIndex).getCorrectArea()) < 0.1;
    }

    public void reset() {
        practiced.clear();
    }

    // 命令行模式的入口方法
    public List<Integer> startCompoundShapeCalculation() {
        List<Integer> attemptsPerShape = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (!isComplete()) {
                System.out.println("请选择要练习的复合形状（1-" + shapes.size() + "），或输入0返回主菜单：");
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
                    System.out.println("无效输入，请重新选择。\n");
                    continue;
                }
                if (idx < 0 || idx >= shapes.size() || practiced.contains(idx)) {
                    System.out.println("无效输入，请重新选择。\n");
                    continue;
                }
                
                CompoundShape shape = shapes.get(idx);
                int attempts = 0;
                boolean correct = false;
                
                while (attempts < 3) {
                    System.out.println("描述：" + shape.getDescription());
                    System.out.print("请输入该复合形状的面积（保留1位小数）：");
                    String answerStr = scanner.nextLine();
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (checkAnswer(idx, answer)) {
                            System.out.println("回答正确！\n");
                            correct = true;
                            break;
                        } else {
                            System.out.println("回答错误，请再试一次。");
                        }
                    } catch (Exception e) {
                        System.out.println("输入无效，请输入数字。");
                    }
                    attempts++;
                }
                
                if (!correct) {
                    System.out.println("正确答案是: " + String.format("%.1f", shape.getCorrectArea()));
                    System.out.println("详细解法：" + shape.getSolution());
                }
                System.out.println();
                
                addPracticed(idx);
                attemptsPerShape.add(attempts + 1);
            }
        }
        
        System.out.println("复合形状面积计算任务结束，返回主菜单。\n");
        return attemptsPerShape;
    }
}