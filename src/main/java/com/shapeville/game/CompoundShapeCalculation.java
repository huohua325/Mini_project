package com.shapeville.game;

import java.util.*;
import com.shapeville.gui.shapes.ShapeRenderer;
import com.shapeville.gui.shapes.compound.*;

public class CompoundShapeCalculation {
    // 复合形状参数类
    public static class CompoundShape {
        private final String name;
        private final String description;
        private final double correctArea;
        private final String solution;
        private final ShapeRenderer renderer;

        public CompoundShape(String name, String description, double correctArea, 
                           String solution, ShapeRenderer renderer) {
            this.name = name;
            this.description = description;
            this.correctArea = correctArea;
            this.solution = solution;
            this.renderer = renderer;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getCorrectArea() { return correctArea; }
        public String getSolution() { return solution; }
        public ShapeRenderer getRenderer() { return renderer; }
    }

    private final List<CompoundShape> shapes;
    private final Set<Integer> practiced;

    public CompoundShapeCalculation() {
        System.out.println("开始创建CompoundShapeCalculation...");
        this.shapes = initializeShapes();
        this.practiced = new HashSet<>();
        
        if (shapes.isEmpty()) {
            System.err.println("错误：初始化后形状列表为空");
            throw new IllegalStateException("至少需要一个复合形状");
        }
        System.out.println("CompoundShapeCalculation创建完成，形状数量：" + shapes.size());
    }

    private List<CompoundShape> initializeShapes() {
        List<CompoundShape> shapeList = new ArrayList<>();
        
        try {
            System.out.println("开始初始化形状...");
            
            // 1. 添加箭头形状
            ArrowShape arrowShape = new ArrowShape();
            shapeList.add(new CompoundShape(
                "箭头形状",
                "由一个矩形(14×14)和一个梯形(底14，顶5，高5)组成。\n计算总面积。",
                arrowShape.calculateArea(),
                arrowShape.getSolutionSteps(),
                arrowShape
            ));
            
            // 2. 添加T形状
            TShape tShape = new TShape();
            shapeList.add(new CompoundShape(
                "T形状",
                "由顶部矩形(36×36)和底部矩形(60×36)组成。\n计算总面积。",
                tShape.calculateArea(),
                tShape.getSolutionSteps(),
                tShape
            ));
            
            // 3. 添加梯形
            TrapezoidShape trapezoidShape = new TrapezoidShape();
            shapeList.add(new CompoundShape(
                "梯形",
                "底边20m，顶边9m，高11m，右斜边14m。\n计算总面积。",
                trapezoidShape.calculateArea(),
                trapezoidShape.getSolutionSteps(),
                trapezoidShape
            ));
            
            // 4. 添加阶梯形状
            StairShape stairShape = new StairShape();
            shapeList.add(new CompoundShape(
                "阶梯形状",
                "主矩形(20×21)减去两个缺口(11×11和10×10)。\n计算总面积。",
                stairShape.calculateArea(),
                stairShape.getSolutionSteps(),
                stairShape
            ));
            
            // 5. 添加阶梯状矩形
            StepShape stepShape = new StepShape();
            shapeList.add(new CompoundShape(
                "阶梯状矩形",
                "三个矩形组合：11×10，8×8，8×8。\n计算总面积。",
                stepShape.calculateArea(),
                stepShape.getSolutionSteps(),
                stepShape
            ));
            
            // 6. 添加双阶梯形状
            DoubleStairShape doubleStairShape = new DoubleStairShape();
            shapeList.add(new CompoundShape(
                "双阶梯形状",
                "主矩形(19×18)和右上矩形(16×16)减去缺口(16×16)。\n计算总面积。",
                doubleStairShape.calculateArea(),
                doubleStairShape.getSolutionSteps(),
                doubleStairShape
            ));
            
            // 7. 添加房屋形状
            HouseShape houseShape = new HouseShape();
            shapeList.add(new CompoundShape(
                "房屋形状",
                "底部矩形(14×5)和两个三角形(底14高12和底16高13)。\n计算总面积。",
                houseShape.calculateArea(),
                houseShape.getSolutionSteps(),
                houseShape
            ));
            
            // 8. 添加复杂阶梯
            ComplexStairShape complexStairShape = new ComplexStairShape();
            shapeList.add(new CompoundShape(
                "复杂阶梯",
                "四个矩形：24×6，10×12，12×12，2×12。\n计算总面积。",
                complexStairShape.calculateArea(),
                complexStairShape.getSolutionSteps(),
                complexStairShape
            ));
            
            // 9. 添加不规则四边形
            IrregularShape irregularShape = new IrregularShape();
            shapeList.add(new CompoundShape(
                "不规则四边形",
                "底边4m，左边4m，顶边16m，右斜边约17m。\n计算总面积。",
                irregularShape.calculateArea(),
                irregularShape.getSolutionSteps(),
                irregularShape
            ));
            
            System.out.println("所有形状初始化完成，共" + shapeList.size() + "个形状");
            
        } catch (Exception e) {
            System.err.println("初始化形状时出错：" + e.getMessage());
            e.printStackTrace();
        }
        
        return shapeList;
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