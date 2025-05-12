package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 双阶梯形状（形状3）的实现
 * 整体结构：
 * - 左侧高度：19 cm（总高度）
 * - 顶部水平边分两段：
 *   * 左段：18 cm（较高）
 *   * 右段：16 cm（较低，比左段低3cm）
 * - 右侧垂直边：16 cm
 * - 底边总长：34 cm（18 + 16）
 * 可以看作是一个大矩形(34×19)减去右上角的小矩形(16×3)
 */
public class DoubleStairShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public DoubleStairShape() {
        dimensions = new HashMap<>();
        dimensions.put("totalHeight", 19.0);     // 总高度
        dimensions.put("leftWidth", 18.0);       // 左段宽度
        dimensions.put("rightWidth", 16.0);      // 右段宽度
        dimensions.put("rightSideHeight", 16.0); // 右侧高度
        dimensions.put("stepHeight", 3.0);       // 台阶高度差
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("DoubleStairShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(dimensions.get("leftWidth") + dimensions.get("rightWidth"), 
            dimensions.get("totalHeight"));
        
        // 计算实际尺寸
        int totalHeight = (int)(dimensions.get("totalHeight") * scale);    // 19cm
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);        // 18cm
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);      // 16cm
        int rightSideHeight = (int)(dimensions.get("rightSideHeight") * scale); // 16cm
        int stepHeight = (int)(dimensions.get("stepHeight") * scale);      // 3cm
        int totalWidth = leftWidth + rightWidth;  // 34cm
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - totalHeight / 2;
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 从左上角开始逆时针绘制
            path.moveTo(startX, startY);  // 左上角
            path.lineTo(startX + leftWidth, startY);  // 上边左段(18cm)
            path.lineTo(startX + leftWidth, startY + stepHeight);  // 向下的小竖线(3cm)
            path.lineTo(startX + totalWidth, startY + stepHeight);  // 上边右段(16cm)
            path.lineTo(startX + totalWidth, startY + totalHeight);  // 右边(19cm)
            path.lineTo(startX, startY + totalHeight);  // 底边(34cm)
            path.closePath();  // 回到左上角
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("绘制双阶梯形状时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        double scale = FIXED_SIZE / Math.max(dimensions.get("leftWidth") + dimensions.get("rightWidth"), 
            dimensions.get("totalHeight"));
        
        int totalHeight = (int)(dimensions.get("totalHeight") * scale);
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightSideHeight = (int)(dimensions.get("rightSideHeight") * scale);
        int stepHeight = (int)(dimensions.get("stepHeight") * scale);
        int totalWidth = leftWidth + rightWidth;
        
        int centerX = width / 2;
        int centerY = height / 2;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - totalHeight / 2;
        
        // 绘制左侧总高度(19cm)
        drawDimensionLine(g, startX - 20, startY,
                         startX - 20, startY + totalHeight,
                         "19 cm");
        
        // 绘制上边左段(18cm)
        drawDimensionLine(g, startX, startY - 20,
                         startX + leftWidth, startY - 20,
                         "18 cm");
        
        // 绘制上边右段(16cm)
        drawDimensionLine(g, startX + leftWidth, startY + stepHeight - 20,
                         startX + totalWidth, startY + stepHeight - 20,
                         "16 cm");
        
        // 绘制右侧高度(16cm)
        drawDimensionLine(g, startX + totalWidth + 20, startY + stepHeight,
                         startX + totalWidth + 20, startY + totalHeight,
                         "16 cm");
        
        // 绘制底边总长(34cm)
        drawDimensionLine(g, startX, startY + totalHeight + 20,
                         startX + totalWidth, startY + totalHeight + 20,
                         "34 cm");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        // 总面积 = 大矩形面积 - 右上角小矩形面积
        double totalArea = (dimensions.get("leftWidth") + dimensions.get("rightWidth")) * 
                          dimensions.get("totalHeight");
        double cutoutArea = dimensions.get("rightWidth") * dimensions.get("stepHeight");
        
        return totalArea - cutoutArea;
    }
    
    @Override
    public String getSolutionSteps() {
        return String.format(
            "1. 计算大矩形面积：\n" +
            "   底边 × 高 = (18 + 16) × 19\n" +
            "   = 34 × 19 = 646 cm²\n\n" +
            "2. 计算右上角缺口面积：\n" +
            "   宽 × 高 = 16 × 3\n" +
            "   = 48 cm²\n\n" +
            "3. 计算总面积：\n" +
            "   大矩形面积 - 缺口面积\n" +
            "   = 646 - 48\n" +
            "   = 598 cm²"
        );
    }
} 