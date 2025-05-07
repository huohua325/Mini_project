package com.shapeville.game;

import java.util.*;

public class CircleCalculation {

    private static final Scanner scanner = new Scanner(System.in);

    // 练习类型
    private enum PracticeType {
        RADIUS_AREA, RADIUS_CIRCUM, DIAMETER_AREA, DIAMETER_CIRCUM
    }

    public List<Integer> startCircleCalculation() {
        Set<PracticeType> practiced = new HashSet<>();
        List<Integer> attemptsPerPractice = new ArrayList<>();
        Random rand = new Random();

        while (practiced.size() < 4) {
            System.out.println("请选择要练习的内容：");
            if (!practiced.contains(PracticeType.RADIUS_AREA)) System.out.println("1. 已知半径求面积");
            if (!practiced.contains(PracticeType.RADIUS_CIRCUM)) System.out.println("2. 已知半径求周长");
            if (!practiced.contains(PracticeType.DIAMETER_AREA)) System.out.println("3. 已知直径求面积");
            if (!practiced.contains(PracticeType.DIAMETER_CIRCUM)) System.out.println("4. 已知直径求周长");
            System.out.println("5. 返回主菜单");
            System.out.print("请输入选项（1-5）：");
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
                    if (practiced.contains(PracticeType.RADIUS_AREA)) { System.out.println("已完成该练习。\n"); continue; }
                    type = PracticeType.RADIUS_AREA;
                    correct = Math.PI * r * r;
                    formula = "A = π × r²";
                    params = "r = " + r;
                    System.out.println("已知半径 r = " + r + "，请计算圆的面积。");
                    break;
                case "2":
                    if (practiced.contains(PracticeType.RADIUS_CIRCUM)) { System.out.println("已完成该练习。\n"); continue; }
                    type = PracticeType.RADIUS_CIRCUM;
                    correct = 2 * Math.PI * r;
                    formula = "C = 2 × π × r";
                    params = "r = " + r;
                    System.out.println("已知半径 r = " + r + "，请计算圆的周长。");
                    break;
                case "3":
                    if (practiced.contains(PracticeType.DIAMETER_AREA)) { System.out.println("已完成该练习。\n"); continue; }
                    type = PracticeType.DIAMETER_AREA;
                    correct = Math.PI * (d / 2.0) * (d / 2.0);
                    formula = "A = π × (d/2)²";
                    params = "d = " + d;
                    System.out.println("已知直径 d = " + d + "，请计算圆的面积。");
                    break;
                case "4":
                    if (practiced.contains(PracticeType.DIAMETER_CIRCUM)) { System.out.println("已完成该练习。\n"); continue; }
                    type = PracticeType.DIAMETER_CIRCUM;
                    correct = Math.PI * d;
                    formula = "C = π × d";
                    params = "d = " + d;
                    System.out.println("已知直径 d = " + d + "，请计算圆的周长。");
                    break;
                default:
                    System.out.println("无效输入，请重新选择。\n");
                    continue;
            }

            practiced.add(type);

            int attempts = 0;
            boolean isCorrect = false;
            while (attempts < 3 && !isCorrect) {
                System.out.print("请输入你的答案（保留1位小数）：");
                String answerStr = scanner.nextLine();
                attempts++;
                
                try {
                    double answer = Double.parseDouble(answerStr);
                    if (Math.abs(answer - correct) < 0.1) {
                        System.out.println("回答正确！\n");
                        isCorrect = true;
                    } else {
                        System.out.println("回答错误，请再试一次。");
                    }
                } catch (Exception e) {
                    System.out.println("输入无效，请输入数字。");
                }
            }
            if (!isCorrect) {
                System.out.println("正确答案是: " + String.format("%.1f", correct));
            }
            System.out.println("公式：" + formula);
            System.out.println("代入值：" + params);
            System.out.println();
            
            attemptsPerPractice.add(attempts);
        }
        System.out.println("圆的面积和周长计算任务结束，返回主菜单。\n");
        return attemptsPerPractice;
    }
}