package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 复杂阶梯（形状4）的实现
 * 由四个矩形组成：
 * - 底部矩形：24m × 6m
 * - 中间矩形：10m × 12m
 * - 左侧矩形：12m × 12m
 * - 左上小矩形：2m × 12m
 */
public class ComplexStairShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public ComplexStairShape() {
        dimensions = new HashMap<>();
        dimensions.put("bottomWidth", 24.0);  // 底部矩形宽度
        dimensions.put("bottomHeight", 6.0);  // 底部矩形高度
        dimensions.put("midWidth", 10.0);     // 中间矩形宽度
        dimensions.put("midHeight", 12.0);    // 中间矩形高度
        dimensions.put("leftWidth", 12.0);    // 左侧矩形宽度
        dimensions.put("leftHeight", 12.0);   // 左侧矩形高度
        dimensions.put("topWidth", 2.0);      // 左上小矩形宽度
        dimensions.put("topHeight", 12.0);    // 左上小矩形高度
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("ComplexStairShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"),
            dimensions.get("leftHeight") + dimensions.get("bottomHeight"));
        
        // 计算实际尺寸
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);  // 24m
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale); // 6m
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);      // 12m
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);    // 12m
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        
        // 计算T形的起始位置
        // bottomWidth = 24m, leftWidth = 12m
        // 主矩形(12m)从左数第3个位置开始(2m + 12m + 10m = 24m)
        int startX = centerX - bottomWidth / 2;  // T形横条的左边界
        int startY = centerY - (leftHeight + bottomHeight) / 2;  // 整体的上边界
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 1. 绘制T形的"腿"部分 (12×12的主矩形,位于左侧)
            int legX = startX + 2;  // 从左边界开始,偏移2m
            path.moveTo(legX, startY);
            path.lineTo(legX + leftWidth, startY);  // 向右12m
            path.lineTo(legX + leftWidth, startY + leftHeight);  // 向下12m
            path.lineTo(legX, startY + leftHeight);  // 向左12m
            path.closePath();
            
            // 2. 绘制T形的"横"部分 (24×6的横条)
            Path2D.Double crossPath = new Path2D.Double();
            crossPath.moveTo(startX, startY + leftHeight);  // 从左边界开始
            crossPath.lineTo(startX + bottomWidth, startY + leftHeight);  // 向右24m
            crossPath.lineTo(startX + bottomWidth, startY + leftHeight + bottomHeight);  // 向下6m
            crossPath.lineTo(startX, startY + leftHeight + bottomHeight);  // 向左24m
            crossPath.closePath();
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            g.fill(crossPath);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            g.draw(crossPath);
            
        } catch (Exception e) {
            System.err.println("绘制复杂阶梯时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"),
            dimensions.get("leftHeight") + dimensions.get("bottomHeight"));
        
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale);
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);
        
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - bottomWidth / 2;
        int startY = centerY - (leftHeight + bottomHeight) / 2;
        int legX = startX + 2;
        
        // 绘制主矩形(T形腿)尺寸
        // 宽度标注移到上方40个单位
        drawDimensionLine(g, legX, startY - 40,
                         legX + leftWidth, startY - 40,
                         "12 m");
        // 高度标注移到左侧40个单位
        drawDimensionLine(g, legX - 40, startY,
                         legX - 40, startY + leftHeight,
                         "12 m");
        
        // 绘制底部横条(T形横)尺寸
        // 宽度标注移到下方40个单位
        drawDimensionLine(g, startX, startY + leftHeight + bottomHeight + 40,
                         startX + bottomWidth, startY + leftHeight + bottomHeight + 40,
                         "24 m");
        // 高度标注移到左侧60个单位(避开主矩形的标注)
        drawDimensionLine(g, startX - 60, startY + leftHeight,
                         startX - 60, startY + leftHeight + bottomHeight,
                         "6 m");
        
        // 绘制左侧延伸部分尺寸
        // 移到左上角,避开其他标注
        drawDimensionLine(g, startX, startY - 20,
                         startX + 2, startY - 20,
                         "2 m");
        
        // 绘制右侧延伸部分尺寸
        // 移到右上角,避开其他标注
        drawDimensionLine(g, legX + leftWidth, startY - 20,
                         startX + bottomWidth, startY - 20,
                         "10 m");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        double bottomArea = dimensions.get("bottomWidth") * dimensions.get("bottomHeight");
        double midArea = dimensions.get("midWidth") * dimensions.get("midHeight");
        double leftArea = dimensions.get("leftWidth") * dimensions.get("leftHeight");
        double topArea = dimensions.get("topWidth") * dimensions.get("topHeight");
        
        return bottomArea + midArea + leftArea + topArea;
    }
    
    @Override
    public String getSolutionSteps() {
        return String.format(
            "1. 计算底部矩形面积：\n" +
            "   24 × 6 = 144 m²\n\n" +
            "2. 计算中间矩形面积：\n" +
            "   10 × 12 = 120 m²\n\n" +
            "3. 计算左侧矩形面积：\n" +
            "   12 × 12 = 144 m²\n\n" +
            "4. 计算左上小矩形面积：\n" +
            "   2 × 12 = 24 m²\n\n" +
            "5. 计算总面积：\n" +
            "   底部矩形 + 中间矩形 + 左侧矩形 + 左上小矩形\n" +
            "   = 144 + 120 + 144 + 24\n" +
            "   = 432 m²"
        );
    }
} 