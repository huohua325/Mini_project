package com.shapeville.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.*;

public class SectorCalculation {
    // 扇形参数类
    public static class Sector {
        private double radius;
        private double angle;
        private String unit;
        private double correctArea;
        private String solution;

        public Sector(double radius, double angle, String unit) {
            this.radius = radius;
            this.angle = angle;
            this.unit = unit;
            calculateCorrectArea();
        }

        public String getName() {
            return String.format("扇形 (r=%.1f%s, θ=%.0f°)", radius, unit, angle);
        }

        public void draw(Graphics2D g2d, int width, int height) {
            // 设置绘图质量
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 计算绘制参数
            int margin = 40;
            int size = Math.min(width, height) - 2 * margin;
            int centerX = width / 2;
            int centerY = height / 2;
            
            // 绘制扇形
            g2d.setColor(new Color(255, 200, 200)); // 浅粉色填充
            g2d.fillArc(centerX - size/2, centerY - size/2, size, size, 0, -(int)angle);
            
            // 绘制圆弧
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(centerX - size/2, centerY - size/2, size, size, 0, 360);
            
            // 绘制扇形边
            double radians = Math.toRadians(angle);
            int endX = centerX + (int)(size/2 * Math.cos(radians));
            int endY = centerY - (int)(size/2 * Math.sin(radians));
            g2d.drawLine(centerX, centerY, centerX + size/2, centerY); // 水平线
            g2d.drawLine(centerX, centerY, endX, endY); // 斜线
            
            // 绘制标注
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            // 绘制半径标注
            String radiusText = radius + " " + unit;
            g2d.drawString(radiusText, centerX + size/4, centerY - 5);
            
            // 绘制角度标注
            int arcRadius = 30;
            g2d.drawArc(centerX - arcRadius, centerY - arcRadius, 
                       2 * arcRadius, 2 * arcRadius, 0, -(int)angle);
            String angleText = String.format("%.0f°", angle);
            double labelAngle = Math.toRadians(angle/2);
            int labelX = centerX + (int)(arcRadius * 1.5 * Math.cos(labelAngle));
            int labelY = centerY - (int)(arcRadius * 1.5 * Math.sin(labelAngle));
            g2d.drawString(angleText, labelX, labelY);
        }

        private void calculateCorrectArea() {
            this.correctArea = Math.PI * radius * radius * angle / 360.0;
            this.solution = String.format(
                "解题步骤：\n" +
                "1. 扇形面积公式：A = πr²θ/360°\n" +
                "2. 代入数值：A = 3.14 × %.1f² × %.1f° ÷ 360°\n" +
                "3. 计算结果：A = %.1f %s²",
                radius, angle, correctArea, unit
            );
        }

        // Getters
        public double getRadius() { return radius; }
        public double getAngle() { return angle; }
        public String getUnit() { return unit; }
        public double getCorrectArea() { return correctArea; }
        public String getSolution() { return solution; }
    }

    private final List<Sector> sectors;
    private final Set<Integer> practiced;

    public SectorCalculation() {
        sectors = new ArrayList<>();
        practiced = new HashSet<>();
        
        // 添加8个预定义的扇形，对应图片中的扇形
        sectors.add(new Sector(8, 90, "cm"));    // 1号扇形
        sectors.add(new Sector(18, 130, "ft"));  // 2号扇形
        sectors.add(new Sector(19, 240, "cm"));  // 3号扇形
        sectors.add(new Sector(22, 110, "ft"));  // 4号扇形
        sectors.add(new Sector(3.5, 100, "m"));  // 5号扇形
        sectors.add(new Sector(8, 270, "in"));   // 6号扇形
        sectors.add(new Sector(12, 280, "yd"));  // 7号扇形
        sectors.add(new Sector(15, 250, "mm"));  // 8号扇形
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
        return practiced.size() == sectors.size();
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
                        System.out.println((i+1) + ". " + sectors.get(i).getName());
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
                boolean areaCorrect = false;
                
                while (attempts < 3 && !areaCorrect) {
                    System.out.println("已知半径 r = " + sector.getRadius() + 
                                     "，圆心角 x = " + sector.getAngle() + "°");
                    
                    System.out.println("请计算扇形面积（π取3.14，保留2位小数）：");
                    String answerStr = scanner.nextLine();
                    try {
                        double answer = Double.parseDouble(answerStr);
                        if (Math.abs(answer - sector.getCorrectArea()) < 0.05) {
                            System.out.println("面积计算正确！");
                            areaCorrect = true;
                        } else {
                            System.out.println("面积计算错误，请继续。");
                        }
                    } catch (Exception e) {
                        System.out.println("输入无效，请输入数字。");
                    }
                    
                    attempts++;
                }
                
                if (!areaCorrect) {
                    System.out.println("\n正确答案：");
                    System.out.println("面积：" + String.format("%.2f", sector.getCorrectArea()));
                    System.out.println("\n详细解法：");
                    System.out.println(sector.getSolution());
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