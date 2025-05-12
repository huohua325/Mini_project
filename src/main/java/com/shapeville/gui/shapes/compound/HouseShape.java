package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 不规则五边形的实现
 * 由一个矩形和一个不规则三角形组成：
 * - 底部矩形：14cm × 5cm
 * - 上方三角形：底14cm（与矩形共边），左边12cm，右边16cm
 */
public class HouseShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public HouseShape() {
        dimensions = new HashMap<>();
        dimensions.put("baseWidth", 14.0);   // 底部矩形宽度
        dimensions.put("baseHeight", 5.0);   // 底部矩形高度
        dimensions.put("leftSide", 12.0);    // 三角形左边
        dimensions.put("rightSide", 16.0);   // 三角形右边
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("HouseShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("baseWidth"),
            dimensions.get("baseHeight") + Math.max(dimensions.get("leftSide"), dimensions.get("rightSide")));
        
        // 计算实际尺寸
        int baseWidth = (int)(dimensions.get("baseWidth") * scale);
        int baseHeight = (int)(dimensions.get("baseHeight") * scale);
        int leftSide = (int)(dimensions.get("leftSide") * scale);
        int rightSide = (int)(dimensions.get("rightSide") * scale);
        
        // 计算三角形顶点的位置（使用余弦定理）
        double a = dimensions.get("baseWidth"); // 底边
        double b = dimensions.get("leftSide");  // 左边
        double c = dimensions.get("rightSide"); // 右边
        
        // 使用余弦定理计算底边与左边之间的角度
        double cosA = (b * b + a * a - c * c) / (2 * b * a);
        double sinA = Math.sqrt(1 - cosA * cosA);
        
        // 计算三角形顶点的x和y坐标（相对于矩形左上角）
        int topX = (int)(b * cosA * scale);
        int topY = (int)(b * sinA * scale);
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - baseWidth / 2;
        int startY = centerY - (baseHeight + topY) / 2;
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 从左下角开始逆时针绘制
            path.moveTo(startX, startY + topY + baseHeight);              // 左下角
            path.lineTo(startX + baseWidth, startY + topY + baseHeight);  // 底边
            path.lineTo(startX + baseWidth, startY + topY);              // 右边竖线
            path.lineTo(startX + topX, startY);                          // 右上斜边
            path.lineTo(startX, startY + topY);                          // 左上斜边
            path.closePath();
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("绘制形状时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // 使用与draw方法相同的计算
        double scale = FIXED_SIZE / Math.max(dimensions.get("baseWidth"),
            dimensions.get("baseHeight") + Math.max(dimensions.get("leftSide"), dimensions.get("rightSide")));
        
        // 计算实际尺寸和位置
        int baseWidth = (int)(dimensions.get("baseWidth") * scale);
        int baseHeight = (int)(dimensions.get("baseHeight") * scale);
        
        // 计算三角形顶点位置
        double a = dimensions.get("baseWidth");
        double b = dimensions.get("leftSide");
        double c = dimensions.get("rightSide");
        double cosA = (b * b + a * a - c * c) / (2 * b * a);
        double sinA = Math.sqrt(1 - cosA * cosA);
        int topX = (int)(b * cosA * scale);
        int topY = (int)(b * sinA * scale);
        
        // 计算绘制位置
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - baseWidth / 2;
        int startY = centerY - (baseHeight + topY) / 2;
        
        // 绘制底部矩形尺寸
        drawDimensionLine(g, startX, startY + topY + baseHeight + 20,
                         startX + baseWidth, startY + topY + baseHeight + 20,
                         "14 cm");
        drawDimensionLine(g, startX - 20, startY + topY,
                         startX - 20, startY + topY + baseHeight,
                         "5 cm");
        
        // 绘制三角形边长
        drawDimensionLine(g, startX, startY + topY - 10,
                         startX + topX, startY - 10,
                         "12 cm");
        drawDimensionLine(g, startX + topX + 10, startY,
                         startX + baseWidth + 10, startY + topY,
                         "16 cm");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        // 矩形面积
        double rectangleArea = dimensions.get("baseWidth") * dimensions.get("baseHeight");
        
        // 三角形面积（使用海伦公式）
        double a = dimensions.get("baseWidth"); // 底边
        double b = dimensions.get("leftSide");  // 左边
        double c = dimensions.get("rightSide"); // 右边
        
        // 半周长
        double s = (a + b + c) / 2;
        
        // 三角形面积
        double triangleArea = Math.sqrt(s * (s - a) * (s - b) * (s - c));
        
        return rectangleArea + triangleArea;
    }
    
    @Override
    public String getSolutionSteps() {
        double rectangleArea = dimensions.get("baseWidth") * dimensions.get("baseHeight");
        
        double a = dimensions.get("baseWidth");
        double b = dimensions.get("leftSide");
        double c = dimensions.get("rightSide");
        double s = (a + b + c) / 2;
        double triangleArea = Math.sqrt(s * (s - a) * (s - b) * (s - c));
        
        return String.format(
            "1. 计算矩形面积：\n" +
            "   底 × 高 = 14 × 5 = 70 cm²\n\n" +
            "2. 计算三角形面积（使用海伦公式）：\n" +
            "   半周长 s = (a + b + c) / 2\n" +
            "   s = (14 + 12 + 16) / 2 = 21\n" +
            "   面积 = √(s(s-a)(s-b)(s-c))\n" +
            "   = √(21(21-14)(21-12)(21-16))\n" +
            "   ≈ %.2f cm²\n\n" +
            "3. 计算总面积：\n" +
            "   矩形面积 + 三角形面积\n" +
            "   = 70 + %.2f\n" +
            "   ≈ %.2f cm²",
            triangleArea, triangleArea, rectangleArea + triangleArea
        );
    }
} 