# Shapeville GUI Shapes 模块开发指南

## 概述

Shapes 模块是 Shapeville 几何学习乐园的图形渲染层，负责各种几何图形的可视化绘制。该模块提供了从基本形状到复杂复合形状的各种绘制工具，确保用户能够直观地理解几何概念。

## 模块结构

```
shapes/
├── ShapeRenderer.java             # 形状渲染接口
├── CompoundShapeDrawer.java       # 复合形状绘制基类
├── ShapeDrawer.java               # 基本形状绘制工具类
├── CircleDrawer.java              # 圆形绘制专用类
└── compound/                      # 复合形状实现目录
    ├── ArrowShape.java            # 箭头形状
    ├── ComplexStairShape.java     # 复杂阶梯形状
    ├── DoubleStairShape.java      # 双阶梯形状
    ├── HouseShape.java            # 房屋形状
    ├── IrregularShape.java        # 不规则形状
    ├── StairShape.java            # 阶梯形状
    ├── StepShape.java             # 台阶形状
    ├── TrapezoidShape.java        # 梯形
    └── TShape.java                # T形状
```

## 接口与基类

### ShapeRenderer.java

核心接口，定义了所有形状渲染器必须实现的方法。

#### 主要方法
- `void draw(Graphics2D g, int width, int height)`: 绘制形状
- `void drawDimensions(Graphics2D g, int width, int height)`: 绘制尺寸标注
- `Map<String, Double> getDimensions()`: 获取形状尺寸数据
- `double calculateArea()`: 计算形状面积
- `String getSolutionSteps()`: 获取解题步骤说明

### CompoundShapeDrawer.java

复合形状的绘制基类，实现了 `ShapeRenderer` 接口并提供了通用的绘图工具方法。

#### 主要功能

- 提供绘图上下文初始化方法
- 实现缩放比例计算，使形状适应绘图区域
- 提供尺寸线和箭头绘制功能
- 定义了通用的颜色和样式常量

#### 核心方法
- `prepareGraphics(Graphics2D g2d)`: 设置抗锯齿等渲染选项
- `calculateScale(double realWidth, double realHeight, int availableWidth, int availableHeight)`: 计算适当的缩放比例
- `drawDimensionLine(Graphics2D g2d, int x1, int y1, int x2, int y2, String text)`: 绘制带箭头的尺寸标注线

## 工具类

### ShapeDrawer.java

提供静态方法绘制各种基本几何形状。

#### 支持的形状
- 矩形 (drawRectangle)
- 平行四边形 (drawParallelogram)
- 三角形 (drawTriangle)
- 梯形 (drawTrapezium)

#### 特点
- 每个方法都支持填充、边框和尺寸标注
- 使用统一的样式和颜色方案
- 提供细致的尺寸标注功能

### CircleDrawer.java

专门用于圆形绘制的工具类。

#### 主要功能
- 绘制圆形及其半径/直径
- 显示半径或直径的数值标注
- 使用虚线显示半径或直径线

## 复合形状实现 (compound 目录)

compound 目录包含了九种复合形状的具体实现，每种形状都继承自 `CompoundShapeDrawer` 类并实现了 `ShapeRenderer` 接口。

### 通用特点

所有复合形状实现都：
- 使用 Java2D API 进行绘制
- 实现自动缩放以适应不同尺寸的面板
- 提供详细的尺寸标注
- 包含精确的面积计算和解题步骤说明
- 使用固定的基准尺寸进行绘制

### 可用形状一览

1. **ArrowShape**: 由一个矩形和一个梯形组合成的箭头形状
2. **TShape**: T形结构，由垂直放置的两个矩形组成
3. **TrapezoidShape**: 带有详细标注的梯形
4. **StairShape**: 从矩形中挖去两个矩形形成的阶梯形状
5. **StepShape**: 由三个矩形组合的台阶状形状
6. **DoubleStairShape**: 复杂的双阶梯结构
7. **HouseShape**: 由矩形底部和三角形屋顶组成的房屋形状
8. **ComplexStairShape**: 由四个矩形组成的复杂阶梯
9. **IrregularShape**: 不规则四边形

## 工作流程

1. 用户界面通过 `ShapeRenderer` 接口调用适当的绘制实现
2. 绘制器计算缩放比例，确保形状适应绘图区域
3. 绘制形状的填充、边框和尺寸标注
4. 当需要计算面积时，调用 `calculateArea()` 方法
5. 当需要解题步骤时，调用 `getSolutionSteps()` 方法

## 开发指南

### 创建新的基本形状

要添加新的基本形状绘制功能，请在 `ShapeDrawer` 类中添加新的静态方法：

```java
public static void drawNewShape(Graphics2D g2d, int x, int y, /*其他参数*/ Map<String, Double> params, boolean showDimensions) {
    // 1. 设置抗锯齿等渲染属性
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    // 2. 绘制填充
    g2d.setColor(SHAPE_FILL_COLOR);
    // 填充逻辑...
    
    // 3. 绘制边框
    g2d.setColor(SHAPE_BORDER_COLOR);
    g2d.setStroke(new BasicStroke(MAIN_STROKE_WIDTH));
    // 边框绘制逻辑...
    
    // 4. 如果需要显示尺寸，绘制尺寸标注
    if (showDimensions && params != null) {
        // 尺寸标注绘制逻辑...
    }
}
```

### 创建新的复合形状

要创建新的复合形状，请遵循以下步骤：

1. 在 `compound` 包中创建新的类，继承 `CompoundShapeDrawer`：

```java
package com.shapeville.gui.shapes.compound;

import java.awt.*;
import java.util.*;
import com.shapeville.gui.shapes.CompoundShapeDrawer;

public class NewCompoundShape extends CompoundShapeDrawer {
    private final Map<String, Double> dimensions;
    
    public NewCompoundShape() {
        dimensions = new HashMap<>();
        // 初始化尺寸参数...
    }
    
    @Override
    public void draw(Graphics2D g, int width, int height) {
        prepareGraphics(g);
        // 绘制形状逻辑...
    }
    
    @Override
    public void drawDimensions(Graphics2D g, int width, int height) {
        // 绘制尺寸标注逻辑...
    }
    
    @Override
    public Map<String, Double> getDimensions() {
        return new HashMap<>(dimensions);
    }
    
    @Override
    public double calculateArea() {
        // 计算面积逻辑...
        return totalArea;
    }
    
    @Override
    public String getSolutionSteps() {
        // 返回详细的解题步骤...
        return steps;
    }
}
```

2. 在 `CompoundShapeCalculation` 类中注册新形状

### 渲染提示

- 使用 Java2D 的抗锯齿功能提高绘图质量
- 确保形状适当缩放以适应不同大小的面板
- 使用一致的颜色方案和线条样式
- 提供清晰的尺寸标注和标签
- 考虑使用双缓冲技术避免闪烁

### 增强现有形状

1. 添加交互功能（如高亮选中区域）
2. 提供动画效果（如形状组装过程）
3. 增加不同的尺寸单位支持
4. 实现可编辑的尺寸参数

## 与其他模块的集成

Shapes 模块与以下模块有紧密的集成关系：

1. **GUI Tasks**: 任务面板使用 Shapes 模块绘制题目中的几何图形
2. **Game**: 游戏逻辑层通过 Shapes 模块获取形状面积等计算结果
3. **Utils**: 使用工具函数进行坐标转换和数学计算

## 注意事项

1. 保持绘图代码的高效性，避免不必要的重复计算
2. 确保正确释放图形资源，特别是在使用自定义字体或图像时
3. 使用相对坐标和缩放比例，确保在不同分辨率下显示正常
4. 注意边界情况和舍入误差，特别是在计算面积时
5. 遵循已有形状的命名和实现约定，保持代码一致性

## 未来扩展方向

1. **3D形状渲染**: 添加3D几何形状的绘制和交互功能
2. **动态形状生成**: 根据参数动态生成更多样化的形状
3. **形状动画**: 实现形状变换和组合的动画效果
4. **导出功能**: 允许将绘制的形状导出为图像或SVG
5. **交互式标注**: 允许用户添加和编辑尺寸标注
6. **移动适配**: 优化触摸屏设备上的显示效果 