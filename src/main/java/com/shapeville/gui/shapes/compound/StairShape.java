package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 阶梯形状（形状2）的实现
 * 由两个矩形组合而成：
 * - 左侧大矩形：11cm × 21cm
 * - 右侧小矩形：9cm × 10cm（底部对齐）
 * 特点：
 * - 总底边长：20cm（11 + 9）
 * - 高度差：11cm（21 - 10）
 */
public class StairShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200;
    
    public StairShape() {
        dimensions = new HashMap<>();
        dimensions.put("leftWidth", 11.0);   // 左侧矩形宽度
        dimensions.put("leftHeight", 21.0);  // 左侧矩形高度
        dimensions.put("rightWidth", 9.0);   // 右侧矩形宽度
        dimensions.put("rightHeight", 10.0); // 右侧矩形高度
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("StairShape.draw() - 开始绘制");
        
        prepareGraphics(g);
        
        // 计算缩放比例
        double scale = FIXED_SIZE / Math.max(
            dimensions.get("leftWidth") + dimensions.get("rightWidth"),
            dimensions.get("leftHeight")
        );
        
        // 计算实际尺寸
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightHeight = (int)(dimensions.get("rightHeight") * scale);
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        int totalWidth = leftWidth + rightWidth;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - leftHeight / 2;
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 从左上角开始逆时针绘制
            path.moveTo(startX, startY); // 左上角
            path.lineTo(startX + leftWidth, startY); // 左矩形上边
            path.lineTo(startX + leftWidth, startY + leftHeight - rightHeight); // 右矩形上边的左端点
            path.lineTo(startX + totalWidth, startY + leftHeight - rightHeight); // 右矩形上边
            path.lineTo(startX + totalWidth, startY + leftHeight); // 右矩形右边
            path.lineTo(startX, startY + leftHeight); // 底边
            path.closePath();
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            
        } catch (Exception e) {
            System.err.println("绘制阶梯形状时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // 使用与draw方法相同的计算
        double scale = FIXED_SIZE / Math.max(
            dimensions.get("leftWidth") + dimensions.get("rightWidth"),
            dimensions.get("leftHeight")
        );
        
        int leftWidth = (int)(dimensions.get("leftWidth") * scale);
        int leftHeight = (int)(dimensions.get("leftHeight") * scale);
        int rightWidth = (int)(dimensions.get("rightWidth") * scale);
        int rightHeight = (int)(dimensions.get("rightHeight") * scale);
        
        int centerX = width / 2;
        int centerY = height / 2;
        int totalWidth = leftWidth + rightWidth;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - leftHeight / 2;
        
        // 绘制左侧矩形尺寸
        drawDimensionLine(g, startX - 40, startY,
                         startX - 40, startY + leftHeight,
                         "21 cm");
        drawDimensionLine(g, startX, startY - 20,
                         startX + leftWidth, startY - 20,
                         "11 cm");
        
        // 绘制右侧矩形尺寸
        drawDimensionLine(g, startX + totalWidth + 20, startY + leftHeight - rightHeight,
                         startX + totalWidth + 20, startY + leftHeight,
                         "10 cm");
        drawDimensionLine(g, startX + leftWidth, startY + leftHeight - rightHeight - 20,
                         startX + totalWidth, startY + leftHeight - rightHeight - 20,
                         "9 cm");
        
        // 绘制总底边长度
        drawDimensionLine(g, startX, startY + leftHeight + 20,
                         startX + totalWidth, startY + leftHeight + 20,
                         "20 cm");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        // 左侧矩形面积
        double leftArea = dimensions.get("leftWidth") * dimensions.get("leftHeight");
        
        // 右侧矩形面积
        double rightArea = dimensions.get("rightWidth") * dimensions.get("rightHeight");
        
        return leftArea + rightArea;
    }
    
    @Override
    public String getSolutionSteps() {
        double leftArea = dimensions.get("leftWidth") * dimensions.get("leftHeight");
        double rightArea = dimensions.get("rightWidth") * dimensions.get("rightHeight");
        
        return String.format(
            "1. 计算左侧矩形面积：\n" +
            "   宽 × 高 = 11 × 21 = 231 cm²\n\n" +
            "2. 计算右侧矩形面积：\n" +
            "   宽 × 高 = 9 × 10 = 90 cm²\n\n" +
            "3. 计算总面积：\n" +
            "   左侧矩形 + 右侧矩形\n" +
            "   = 231 + 90\n" +
            "   = 321 cm²"
        );
    }
} 