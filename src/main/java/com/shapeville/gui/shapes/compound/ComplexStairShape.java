package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 复杂阶梯（形状4）的实现
 * 由四个矩形组成：
 * - 底部矩形：60m × 36m
 * - 中间矩形：5m × 36m
 * - 左侧矩形：36m × 36m
 * - 左上小矩形：19m × 36m
 */
public class ComplexStairShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public ComplexStairShape() {
        dimensions = new HashMap<>();
        dimensions.put("bottomWidth", 60.0);  // 底部矩形宽度
        dimensions.put("bottomHeight", 36.0);  // 底部矩形高度
        dimensions.put("midWidth", 5.0);     // 中间矩形宽度
        dimensions.put("midHeight", 36.0);    // 中间矩形高度
        dimensions.put("leftWidth", 36.0);    // 左侧矩形宽度
        dimensions.put("leftHeight", 36.0);   // 左侧矩形高度
        dimensions.put("topWidth", 19.0);      // 左上小矩形宽度
        dimensions.put("topHeight", 36.0);    // 左上小矩形高度
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("ComplexStairShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("bottomWidth"),
            dimensions.get("leftHeight") + dimensions.get("bottomHeight"));
        
        // 计算实际尺寸
        int bottomWidth = (int)(dimensions.get("bottomWidth") * scale);  // 60m
        int bottomHeight = (int)(dimensions.get("bottomHeight") * scale); // 36m
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);      // 36m
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);    // 36m
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        
        // 计算T形的起始位置
        // bottomWidth = 60m, leftWidth = 36m
        // 主矩形(36m)从左数第4个位置开始(19m + 36m + 5m = 60m)
        int startX = centerX - bottomWidth / 2;  // T形横条的左边界
        int startY = centerY - (leftHeight + bottomHeight) / 2;  // 整体的上边界
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 1. 绘制T形的"腿"部分 (36×36的主矩形,位于左侧)
            int legX = startX + 19;  // 从左边界开始,偏移19m
            path.moveTo(legX, startY);
            path.lineTo(legX + leftWidth, startY);  // 向右36m
            path.lineTo(legX + leftWidth, startY + leftHeight);  // 向下36m
            path.lineTo(legX, startY + leftHeight);  // 向左36m
            path.closePath();
            
            // 2. 绘制T形的"横"部分 (60×36的横条)
            Path2D.Double crossPath = new Path2D.Double();
            crossPath.moveTo(startX, startY + leftHeight);  // 从左边界开始
            crossPath.lineTo(startX + bottomWidth, startY + leftHeight);  // 向右60m
            crossPath.lineTo(startX + bottomWidth, startY + leftHeight + bottomHeight);  // 向下36m
            crossPath.lineTo(startX, startY + leftHeight + bottomHeight);  // 向左60m
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
        int legX = startX + 19;
        
        // 绘制主矩形(T形腿)尺寸
        // 宽度标注移到上方40个单位
        drawDimensionLine(g, legX, startY - 40,
                         legX + leftWidth, startY - 40,
                         "36 m");
        // 高度标注移到左侧40个单位
        drawDimensionLine(g, legX - 40, startY,
                         legX - 40, startY + leftHeight,
                         "36 m");
        
        // 绘制底部横条(T形横)尺寸
        // 宽度标注移到下方40个单位
        drawDimensionLine(g, startX, startY + leftHeight + bottomHeight + 40,
                         startX + bottomWidth, startY + leftHeight + bottomHeight + 40,
                         "60 m");
        // 高度标注移到左侧60个单位(避开主矩形的标注)
        drawDimensionLine(g, startX - 60, startY + leftHeight,
                         startX - 60, startY + leftHeight + bottomHeight,
                         "36 m");
        
        // 绘制左侧延伸部分尺寸
        // 移到左上角,避开其他标注
        drawDimensionLine(g, startX, startY - 20,
                         startX + 19, startY - 20,
                         "19 m");
        
        // 绘制右侧延伸部分尺寸
        // 移到右上角,避开其他标注
        drawDimensionLine(g, legX + leftWidth, startY - 20,
                         startX + bottomWidth, startY - 20,
                         "5 m");
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
            "   60 × 36 = 2160 m²\n\n" +
            "2. 计算中间矩形面积：\n" +
            "   5 × 36 = 180 m²\n\n" +
            "3. 计算左侧矩形面积：\n" +
            "   36 × 36 = 1296 m²\n\n" +
            "4. 计算左上小矩形面积：\n" +
            "   19 × 36 = 684 m²\n\n" +
            "5. 计算总面积：\n" +
            "   底部矩形 + 中间矩形 + 左侧矩形 + 左上小矩形\n" +
            "   = 2160 + 180 + 1296 + 684\n" +
            "   = 4320 m²"
        );
    }
} 