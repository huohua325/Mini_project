package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 不规则四边形（形状5）的实现
 * 由一个矩形和一个直角三角形组成：
 * - 底部矩形：4m × 2m
 * - 上方直角三角形：
 *   * 左边（与矩形共边）：4m
 *   * 右边：2m
 *   * 斜边：16m
 */
public class IrregularShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public IrregularShape() {
        dimensions = new HashMap<>();
        dimensions.put("baseWidth", 4.0);    // 底部矩形宽度
        dimensions.put("baseHeight", 2.0);   // 底部矩形高度
        dimensions.put("leftSide", 4.0);     // 三角形左边（与矩形共边）
        dimensions.put("rightSide", 2.0);    // 三角形右边
        dimensions.put("hypotenuse", 16.0);  // 三角形斜边
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("IrregularShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("baseWidth"),
            dimensions.get("baseHeight") + dimensions.get("leftSide"));
        
        // 计算实际尺寸
        int baseWidth = (int)(dimensions.get("baseWidth") * scale);
        int baseHeight = (int)(dimensions.get("baseHeight") * scale);
        int leftSide = (int)(dimensions.get("leftSide") * scale);
        int rightSide = (int)(dimensions.get("rightSide") * scale);
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - baseWidth / 2;
        int startY = centerY - (baseHeight + leftSide) / 2;
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 从左下角开始逆时针绘制
            path.moveTo(startX, startY + leftSide + baseHeight);              // 左下角
            path.lineTo(startX + baseWidth, startY + leftSide + baseHeight);  // 底边
            path.lineTo(startX + baseWidth, startY + leftSide);              // 右边竖线
            path.lineTo(startX + baseWidth, startY + rightSide);             // 右上边
            path.lineTo(startX, startY);                                     // 斜边到左上角
            path.closePath();
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("绘制不规则四边形时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // 使用与draw方法相同的计算
        double scale = FIXED_SIZE / Math.max(dimensions.get("baseWidth"),
            dimensions.get("baseHeight") + dimensions.get("leftSide"));
        
        // 计算实际尺寸
        int baseWidth = (int)(dimensions.get("baseWidth") * scale);
        int baseHeight = (int)(dimensions.get("baseHeight") * scale);
        int leftSide = (int)(dimensions.get("leftSide") * scale);
        int rightSide = (int)(dimensions.get("rightSide") * scale);
        
        // 计算绘制位置
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - baseWidth / 2;
        int startY = centerY - (baseHeight + leftSide) / 2;
        
        // 绘制底部矩形尺寸
        drawDimensionLine(g, startX, startY + leftSide + baseHeight + 20,
                         startX + baseWidth, startY + leftSide + baseHeight + 20,
                         "4 m");
        
        // 绘制左侧矩形高度（向左移动40像素）
        drawDimensionLine(g, startX - 40, startY + leftSide,
                         startX - 40, startY + leftSide + baseHeight,
                         "2 m");
        
        // 绘制左侧三角形高度（向左移动40像素）
        drawDimensionLine(g, startX - 40, startY,
                         startX - 40, startY + leftSide,
                         "4 m");
        
        // 绘制右侧高度
        drawDimensionLine(g, startX + baseWidth + 20, startY + rightSide,
                         startX + baseWidth + 20, startY + leftSide,
                         "2 m");
        
        // 绘制斜边长度（用虚线表示）
        g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                                  10.0f, new float[]{5.0f}, 0.0f));
        drawDimensionLine(g, startX + 10, startY - 10,
                         startX + baseWidth + 10, startY + rightSide - 10,
                         "16 m");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        // 矩形面积
        double rectangleArea = dimensions.get("baseWidth") * dimensions.get("baseHeight");
        
        // 三角形面积
        double triangleArea = dimensions.get("baseWidth") * 
                            (dimensions.get("leftSide") - dimensions.get("rightSide")) / 2;
        
        return rectangleArea + triangleArea;
    }
    
    @Override
    public String getSolutionSteps() {
        double rectangleArea = dimensions.get("baseWidth") * dimensions.get("baseHeight");
        double triangleHeight = dimensions.get("leftSide") - dimensions.get("rightSide");
        double triangleArea = dimensions.get("baseWidth") * triangleHeight / 2;
        
        return String.format(
            "1. 计算底部矩形面积：\n" +
            "   底 × 高 = 4 × 2 = 8 m²\n\n" +
            "2. 计算上方直角三角形面积：\n" +
            "   底 × 高 ÷ 2\n" +
            "   底边 = 4 m\n" +
            "   高 = 左边 - 右边 = 4 - 2 = 2 m\n" +
            "   面积 = 4 × 2 ÷ 2 = 4 m²\n\n" +
            "3. 计算总面积：\n" +
            "   矩形面积 + 三角形面积\n" +
            "   = 8 + 4\n" +
            "   = 12 m²"
        );
    }
} 