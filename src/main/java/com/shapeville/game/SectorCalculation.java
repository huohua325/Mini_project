package com.shapeville.game;

import java.util.*;

public class SectorCalculation {
    // 扇形参数类
    public static class Sector {
        private final String name;
        private final double radius;
        private final double angle;
        private final double correctArea;
        private final String solution;

        public Sector(String name, double radius, double angle, double correctArea, String solution) {
            this.name = name;
            this.radius = radius;
            this.angle = angle;
            this.correctArea = correctArea;
            this.solution = solution;
        }

        public String getName() { return name; }
        public double getRadius() { return radius; }
        public double getAngle() { return angle; }
        public double getCorrectArea() { return correctArea; }
        public String getSolution() { return solution; }
    }

    private final List<Sector> sectors;
    private final Set<Integer> practiced;

    public SectorCalculation() {
        this.sectors = initializeSectors();
        this.practiced = new HashSet<>();
    }

    private List<Sector> initializeSectors() {
        return Arrays.asList(
            new Sector("扇形1", 8, 90, Math.PI * 8 * 8 * 90 / 360, 
                      "A = π × r² × x/360 = 3.14×8²×90/360 = 50.24"),
            new Sector("扇形2", 6, 120, Math.PI * 6 * 6 * 120 / 360,
                      "A = π × r² × x/360 = 3.14×6²×120/360 = 37.68"),
            new Sector("扇形3", 10, 60, Math.PI * 10 * 10 * 60 / 360,
                      "A = π × r² × x/360 = 3.14×10²×60/360 = 52.33"),
            new Sector("扇形4", 5, 180, Math.PI * 5 * 5 * 180 / 360,
                      "A = π × r² × x/360 = 3.14×5²×180/360 = 39.25")
        );
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public Set<Integer> getPracticed() {
        return practiced;
    }

    public void addPracticed(int index) {
        practiced.add(index);
    }

    public boolean isComplete() {
        return practiced.size() >= sectors.size();
    }

    public boolean checkAnswer(int sectorIndex, double answer) {
        if (sectorIndex < 0 || sectorIndex >= sectors.size()) {
            return false;
        }
        return Math.abs(answer - sectors.get(sectorIndex).getCorrectArea()) < 0.05;
    }

    public void reset() {
        practiced.clear();
    }

    // 命令行模式的入口方法
    public List<Integer> startSectorCalculation() {
        List<Integer> attemptsPerSector = new ArrayList<>();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (!isComplete()) {
                System.out.println("请选择要练习的扇形（1-" + sectors.size() + "），或输入0返回主菜单：");
                for (int i = 0; i < sectors.size(); i++) {
                    if (!practiced.contains(i)) {
                        System.out.println((i+1) + ". " + sectors.get(i).getName() + 
                                         "（半径=" + sectors.get(i).getRadius() + 
                                         "，圆心角=" + sectors.get(i).getAngle() + "°）");
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
                
                if (idx < 0 || idx >= sectors.size() || practiced.contains(idx)) {
                    System.out.println("无效输入，请重新选择。\n");
                    continue;
                }
                
                Sector sector = sectors.get(idx);
                int attempts = 0;
                boolean correct = false;
                
                while (attempts < 3 && !correct) {
                    System.out.println("已知半径 r = " + sector.getRadius() + 
                                     "，圆心角 x = " + sector.getAngle() + 
                                     "°，请计算扇形面积（π取3.14，保留2位小数）：");
                    String answerStr = scanner.nextLine();
                    attempts++;
                    
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (checkAnswer(idx, answer)) {
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
                    System.out.println("正确答案是: " + String.format("%.2f", sector.getCorrectArea()));
                    System.out.println("详细解法：" + sector.getSolution());
                }
                System.out.println();
                
                practiced.add(idx);
                attemptsPerSector.add(attempts);
            }
            
            System.out.println("扇形面积计算任务结束，返回主菜单。\n");
            return attemptsPerSector;
        }
    }
}