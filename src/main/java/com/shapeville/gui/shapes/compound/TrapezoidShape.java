package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * T形状（形状6）的实现
 * 尺寸：
 * - 底边：20m
 * - 顶边：9m
 * - 高：11m
 * - 左侧为直角
 */
public class TrapezoidShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public TrapezoidShape() {
        dimensions = new HashMap<>();
        dimensions.put("bottomWidth", 20.0); // 底边长度
        dimensions.put("topWidth", 9.0);     // 顶边长度
        dimensions.put("height", 11.0);      // 高度
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("TrapezoidShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"), dimensions.get("height"));
        
        // 计算实际尺寸
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        int shapeHeight = (int)(dimensions.get("height") * scale);
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        int x = centerX - bottomWidth / 2;
        int y = centerY - shapeHeight / 2;
        
        try {
            // 创建T形路径
            Path2D.Double path = new Path2D.Double();
            
            // 从左下角开始逆时针绘制
            path.moveTo(x, y + shapeHeight); // 左下角
            path.lineTo(x + bottomWidth, y + shapeHeight); // 底边
            path.lineTo(x + bottomWidth, y); // 右边
            path.lineTo(x, y); // 顶边
            path.closePath(); // 回到左下角
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("绘制T形时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"), dimensions.get("height"));
        
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        int shapeHeight = (int)(dimensions.get("height") * scale);
        
        int centerX = width / 2;
        int centerY = height / 2;
        int x = centerX - bottomWidth / 2;
        int y = centerY - shapeHeight / 2;
        
        // 绘制底边长度
        drawDimensionLine(g, x, y + shapeHeight + 20,
                         x + bottomWidth, y + shapeHeight + 20,
                         "20 m");
        
        // 绘制顶边长度
        drawDimensionLine(g, x, y - 20,
                         x + topWidth, y - 20,
                         "9 m");
        
        // 绘制高度
        drawDimensionLine(g, x - 40, y,
                         x - 40, y + shapeHeight,
                         "11 m");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        double bottomWidth = dimensions.get("bottomWidth");
        double topWidth = dimensions.get("topWidth");
        double height = dimensions.get("height");
        
        // T形面积 = 底边 × 高
        return bottomWidth * height;
    }
    
    @Override
    public String getSolutionSteps() {
        return String.format(
            "1. 计算T形面积：\n" +
            "   底边 × 高\n" +
            "   = 20 × 11\n" +
            "   = 220 m²"
        );
    }
} 