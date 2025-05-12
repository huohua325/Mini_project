package com.shapeville.game;

import java.util.*;

public class AreaCalculation {
    // 形状类型
    public enum ShapeType {
        RECTANGLE("矩形", "rectangle"), 
        PARALLELOGRAM("平行四边形", "parallelogram"), 
        TRIANGLE("三角形", "triangle"), 
        TRAPEZIUM("梯形", "trapezium");

        private final String chinese;
        private final String imageName;
        
        ShapeType(String chinese, String imageName) {
            this.chinese = chinese;
            this.imageName = imageName;
        }
        
        public String getChinese() {
            return chinese;
        }
        
        public String getImageName() {
            return imageName;
        }
    }

    private final Random random = new Random();
    private final List<ShapeType> shapes;
    private final Map<String, Double> currentParams;
    private double correctArea;

    public AreaCalculation() {
        this.shapes = new ArrayList<>(Arrays.asList(ShapeType.values()));
        this.currentParams = new HashMap<>();
        shuffleShapes();
    }

    public List<ShapeType> getShapes() {
        return shapes;
    }

    public void shuffleShapes() {
        Collections.shuffle(shapes);
    }

    public Map<String, Double> getCurrentParams() {
        return currentParams;
    }

    public double getCorrectArea() {
        return correctArea;
    }

    public void generateParams(ShapeType shape) {
        currentParams.clear();
        switch (shape) {
            case RECTANGLE:
                currentParams.put("长", (double)(1 + random.nextInt(20)));
                currentParams.put("宽", (double)(1 + random.nextInt(20)));
                correctArea = currentParams.get("长") * currentParams.get("宽");
                break;
            case PARALLELOGRAM:
                currentParams.put("底", (double)(1 + random.nextInt(20)));
                currentParams.put("高", (double)(1 + random.nextInt(20)));
                correctArea = currentParams.get("底") * currentParams.get("高");
                break;
            case TRIANGLE:
                currentParams.put("底", (double)(1 + random.nextInt(20)));
                currentParams.put("高", (double)(1 + random.nextInt(20)));
                correctArea = 0.5 * currentParams.get("底") * currentParams.get("高");
                break;
            case TRAPEZIUM:
                currentParams.put("上底", (double)(1 + random.nextInt(20)));
                currentParams.put("下底", (double)(1 + random.nextInt(20)));
                currentParams.put("高", (double)(1 + random.nextInt(20)));
                correctArea = 0.5 * (currentParams.get("上底") + currentParams.get("下底")) * currentParams.get("高");
                break;
        }
    }

    public String getFormula(ShapeType shape) {
        switch (shape) {
            case RECTANGLE: return "A = 长 × 宽";
            case PARALLELOGRAM: return "A = 底 × 高";
            case TRIANGLE: return "A = 1/2 × 底 × 高";
            case TRAPEZIUM: return "A = (上底 + 下底) × 高 ÷ 2";
            default: return "";
        }
    }

    public String getParamsString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : currentParams.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(" = ").append(String.format("%.1f", entry.getValue()));
        }
        return sb.toString();
    }

    public boolean checkAnswer(double answer) {
        return Math.abs(answer - correctArea) < 0.1;
    }

    public String getSubstitutionString(ShapeType shape) {
        StringBuilder sb = new StringBuilder();
        switch (shape) {
            case RECTANGLE:
                sb.append(String.format("面积 = 长 × 宽 = %.1f × %.1f = %.1f", 
                    currentParams.get("长"), currentParams.get("宽"), correctArea));
                break;
            case PARALLELOGRAM:
                sb.append(String.format("面积 = 底 × 高 = %.1f × %.1f = %.1f", 
                    currentParams.get("底"), currentParams.get("高"), correctArea));
                break;
            case TRIANGLE:
                sb.append(String.format("面积 = (底 × 高) ÷ 2 = (%.1f × %.1f) ÷ 2 = %.1f", 
                    currentParams.get("底"), currentParams.get("高"), correctArea));
                break;
            case TRAPEZIUM:
                sb.append(String.format("面积 = (上底 + 下底) × 高 ÷ 2 = (%.1f + %.1f) × %.1f ÷ 2 = %.1f", 
                    currentParams.get("上底"), currentParams.get("下底"), 
                    currentParams.get("高"), correctArea));
                break;
        }
        return sb.toString();
    }

    // 命令行模式的入口方法
    public List<Integer> startAreaCalculation() {
        Set<ShapeType> practiced = new HashSet<>();
        List<Integer> attemptsPerShape = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (practiced.size() < 4) {
                System.out.println("请选择要练习的形状：");
                for (int i = 0; i < shapes.size(); i++) {
                    if (!practiced.contains(shapes.get(i))) {
                        System.out.println((i + 1) + ". " + shapes.get(i).getChinese());
                    }
                }
                System.out.println("5. 返回主菜单");
                System.out.print("请输入选项（1-5）：");
                String choice = scanner.nextLine();
                if ("5".equals(choice)) break;

                int idx;
                try {
                    idx = Integer.parseInt(choice) - 1;
                } catch (Exception e) {
                    System.out.println("无效输入，请重新选择。\n");
                    continue;
                }
                if (idx < 0 || idx >= shapes.size() || practiced.contains(shapes.get(idx))) {
                    System.out.println("无效输入，请重新选择。\n");
                    continue;
                }

                ShapeType shape = shapes.get(idx);
                practiced.add(shape);
                generateParams(shape);

                int attempts = 0;
                boolean correct = false;
                while (attempts < 3 && !correct) {
                    System.out.println("请计算" + shape.getChinese() + "的面积。");
                    System.out.println("参数：" + getParamsString());
                    System.out.print("请输入你的答案（保留1位小数）：");
                    String answerStr = scanner.nextLine();
                    attempts++;
                    
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (checkAnswer(answer)) {
                            System.out.println("回答正确！\n");
                            correct = true;
                        } else {
                            System.out.println("回答错误，请再试一次。");
                        }
                    } catch (Exception e) {
                        System.out.println("输入无效，请输入数字。");
                    }
                }
                if (!correct) {
                    System.out.println("正确答案是: " + String.format("%.1f", correctArea));
                }
                System.out.println("公式：" + getFormula(shape));
                System.out.println("代入值：" + getParamsString());
                System.out.println();
                
                attemptsPerShape.add(attempts);
            }
        }
        
        System.out.println("面积计算任务结束，返回主菜单。\n");
        return attemptsPerShape;
    }
}