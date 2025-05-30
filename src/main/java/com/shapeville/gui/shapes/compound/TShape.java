package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * T形状（形状8）的实现
 * 由两个矩形组成：
 * - 顶部矩形：36m × 36m，偏右但不完全对齐
 * - 底部矩形：60m × 36m
 */
public class TShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    private final double TOP_RECT_OFFSET = 5.0; // 上部矩形向左偏移的距离（米）
    
    public TShape() {
        dimensions = new HashMap<>();
        dimensions.put("topWidth", 36.0);   // 顶部矩形宽度
        dimensions.put("topHeight", 36.0);  // 顶部矩形高度
        dimensions.put("bottomWidth", 60.0); // 底部矩形宽度
        dimensions.put("bottomHeight", 36.0); // 底部矩形高度
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("TShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"), 
            dimensions.get("topHeight") + dimensions.get("bottomHeight"));
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        
        // 计算实际尺寸
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        int topHeight = (int)(dimensions.get("topHeight") * scale);
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale);
        int topOffset = (int)(TOP_RECT_OFFSET * scale); // 上部矩形的偏移量
        
        // 计算起始位置 - 修正为倒T形状，上部矩形偏右但不完全对齐
        int bottomX = centerX - bottomWidth / 2;
        int bottomY = centerY - bottomHeight / 2;
        int topX = bottomX + bottomWidth - topWidth - topOffset; // 上部矩形右侧有一定偏移
        int topY = bottomY - topHeight; // 上部矩形在下部矩形上方
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 绘制形状
            path.moveTo(bottomX, bottomY);
            path.lineTo(bottomX + bottomWidth, bottomY);
            path.lineTo(bottomX + bottomWidth, bottomY + bottomHeight);
            path.lineTo(bottomX, bottomY + bottomHeight);
            path.lineTo(bottomX, bottomY);
            
            // 添加上部矩形
            path.moveTo(topX, topY);
            path.lineTo(topX + topWidth, topY);
            path.lineTo(topX + topWidth, topY + topHeight);
            path.lineTo(topX, topY + topHeight);
            path.closePath();
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("绘制T形状时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // 使用与draw方法相同的计算
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"), 
            dimensions.get("topHeight") + dimensions.get("bottomHeight"));
        
        int centerX = width / 2;
        int centerY = height / 2;
        
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        int topHeight = (int)(dimensions.get("topHeight") * scale);
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale);
        int topOffset = (int)(TOP_RECT_OFFSET * scale); // 上部矩形的偏移量
        
        int bottomX = centerX - bottomWidth / 2;
        int bottomY = centerY - bottomHeight / 2;
        int topX = bottomX + bottomWidth - topWidth - topOffset; // 上部矩形右侧有一定偏移
        int topY = bottomY - topHeight; // 上部矩形在下部矩形上方
        
        // 绘制顶部矩形尺寸
        drawDimensionLine(g, topX, topY - 20,
                         topX + topWidth, topY - 20,
                         "36 m");
        drawDimensionLine(g, topX - 20, topY,
                         topX - 20, topY + topHeight,
                         "36 m");
        
        // 绘制底部矩形尺寸
        drawDimensionLine(g, bottomX, bottomY + bottomHeight + 20,
                         bottomX + bottomWidth, bottomY + bottomHeight + 20,
                         "60 m");
        drawDimensionLine(g, bottomX - 20, bottomY,
                         bottomX - 20, bottomY + bottomHeight,
                         "36 m");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        double topArea = dimensions.get("topWidth") * dimensions.get("topHeight");
        double bottomArea = dimensions.get("bottomWidth") * dimensions.get("bottomHeight");
        return topArea + bottomArea;
    }
    
    @Override
    public String getSolutionSteps() {
        return String.format(
            "1. 计算顶部矩形面积：\n" +
            "   宽 × 高 = 36 × 36 = 1296 m²\n\n" +
            "2. 计算底部矩形面积：\n" +
            "   宽 × 高 = 60 × 36 = 2160 m²\n\n" +
            "3. 计算总面积：\n" +
            "   顶部面积 + 底部面积\n" +
            "   = 1296 + 2160\n" +
            "   = 3456 m²"
        );
    }
} 