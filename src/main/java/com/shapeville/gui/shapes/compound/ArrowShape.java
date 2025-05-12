package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

/**
 * 箭头形状（形状1）的实现
 * 由一个矩形(14×14)和一个梯形(底14，顶5，高5)组成
 */
public class ArrowShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    private static final int FIXED_SIZE = 200; // 固定基准尺寸
    
    public ArrowShape() {
        dimensions = new HashMap<>();
        dimensions.put("width", 14.0);  // 基本矩形宽度
        dimensions.put("height", 14.0); // 基本矩形高度
        dimensions.put("topWidth", 5.0); // 梯形顶边长度
        dimensions.put("trapezoidHeight", 5.0); // 梯形高度
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        System.out.println("ArrowShape.draw() - 开始绘制");
        System.out.println("绘制区域: " + width + "x" + height);
        
        prepareGraphics(g);
        
        // 计算缩放比例，使形状适应固定尺寸
        double scale = FIXED_SIZE / Math.max(dimensions.get("width") + dimensions.get("trapezoidHeight"), 
            dimensions.get("height"));
        
        System.out.println("缩放比例: " + scale);
        
        // 计算绘制位置（居中）
        int centerX = width / 2;
        int centerY = height / 2;
        
        // 计算实际尺寸
        int rectWidth = (int)(dimensions.get("width") * scale);
        int rectHeight = (int)(dimensions.get("height") * scale);
        int trapHeight = (int)(dimensions.get("trapezoidHeight") * scale);
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        
        System.out.println("实际尺寸 - 矩形: " + rectWidth + "x" + rectHeight + ", 梯形高: " + trapHeight + ", 顶宽: " + topWidth);
        
        // 计算起始位置（向左偏移以为梯形预留空间）
        int x = centerX - (rectWidth + trapHeight) / 2;
        int y = centerY - rectHeight / 2;
        
        System.out.println("绘制位置: (" + x + "," + y + ")");
        
        try {
            // 创建形状路径
            Path2D.Double path = new Path2D.Double();
            
            // 绘制基本矩形
            path.moveTo(x, y);
            path.lineTo(x + rectWidth, y);
            path.lineTo(x + rectWidth, y + rectHeight);
            path.lineTo(x, y + rectHeight);
            path.closePath();
            
            // 绘制右侧梯形
            Path2D.Double trapezoid = new Path2D.Double();
            trapezoid.moveTo(x + rectWidth, y);
            trapezoid.lineTo(x + rectWidth + trapHeight, y + rectHeight/2 - topWidth/2);
            trapezoid.lineTo(x + rectWidth + trapHeight, y + rectHeight/2 + topWidth/2);
            trapezoid.lineTo(x + rectWidth, y + rectHeight);
            trapezoid.closePath();
            
            // 填充形状
            g.setColor(SHAPE_COLOR);
            g.fill(path);
            g.fill(trapezoid);
            
            // 绘制轮廓
            g.setColor(LINE_COLOR);
            g.setStroke(new BasicStroke(2.0f));
            g.draw(path);
            g.draw(trapezoid);
            
            System.out.println("ArrowShape.draw() - 绘制完成");
        } catch (Exception e) {
            System.err.println("绘制箭头形状时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // 使用与draw方法相同的缩放和位置计算
        double scale = FIXED_SIZE / Math.max(dimensions.get("width") + dimensions.get("trapezoidHeight"), 
            dimensions.get("height"));
        
        int centerX = width / 2;
        int centerY = height / 2;
        
        int rectWidth = (int)(dimensions.get("width") * scale);
        int rectHeight = (int)(dimensions.get("height") * scale);
        int trapHeight = (int)(dimensions.get("trapezoidHeight") * scale);
        int topWidth = (int)(dimensions.get("topWidth") * scale);
        
        int x = centerX - (rectWidth + trapHeight) / 2;
        int y = centerY - rectHeight / 2;
        
        // 绘制矩形宽度标注
        drawDimensionLine(g, x, y + rectHeight + 20, 
                         x + rectWidth, y + rectHeight + 20, 
                         "14 cm");
        
        // 绘制矩形高度标注
        drawDimensionLine(g, x - 20, y,
                         x - 20, y + rectHeight,
                         "14 cm");
        
        // 绘制梯形高度标注
        drawDimensionLine(g, x + rectWidth + trapHeight + 10, y + rectHeight/2 - topWidth/2,
                         x + rectWidth + trapHeight + 10, y + rectHeight/2 + topWidth/2,
                         "5 cm");
        
        // 绘制梯形宽度标注
        drawDimensionLine(g, x + rectWidth, y - 20,
                         x + rectWidth + trapHeight, y - 20,
                         "5 cm");
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        double width = dimensions.get("width");
        double height = dimensions.get("height");
        double topWidth = dimensions.get("topWidth");
        double trapHeight = dimensions.get("trapezoidHeight");
        
        // 矩形面积
        double rectangleArea = width * height;
        
        // 梯形面积：(上底+下底)*高/2
        double trapezoidArea = (height + topWidth) * trapHeight / 2;
        
        return rectangleArea + trapezoidArea;
    }
    
    @Override
    public String getSolutionSteps() {
        return String.format(
            "1. 计算矩形面积：\n" +
            "   底 × 高 = 14 × 14 = 196 cm²\n\n" +
            "2. 计算梯形面积：\n" +
            "   (上底 + 下底) × 高 ÷ 2\n" +
            "   = (5 + 14) × 5 ÷ 2\n" +
            "   = 19 × 5 ÷ 2\n" +
            "   = 47.5 cm²\n\n" +
            "3. 计算总面积：\n" +
            "   矩形面积 + 梯形面积\n" +
            "   = 196 + 47.5\n" +
            "   = 243.5 cm²"
        );
    }
} 