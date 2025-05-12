package com.shapeville.gui.shapes;

import java.awt.Graphics2D;
import java.util.Map;

/**
 * 复合形状渲染接口
 * 定义了所有复合形状必须实现的方法
 */
public interface ShapeRenderer {
    /**
     * 绘制形状
     * @param g 图形上下文
     * @param width 可用宽度
     * @param height 可用高度
     */
    void draw(Graphics2D g, int width, int height);
    
    /**
     * 绘制尺寸标注
     * @param g 图形上下文
     * @param width 可用宽度
     * @param height 可用高度
     */
    void drawDimensions(Graphics2D g, int width, int height);
    
    /**
     * 获取形状的所有尺寸
     * @return 尺寸映射表
     */
    Map<String, Double> getDimensions();
    
    /**
     * 计算形状面积
     * @return 面积
     */
    double calculateArea();
    
    /**
     * 获取解题步骤说明
     * @return 解题步骤文本
     */
    String getSolutionSteps();
} 