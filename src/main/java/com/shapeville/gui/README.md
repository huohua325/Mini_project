# Shapeville GUI 模块开发指南

## 概述

GUI 模块是 Shapeville 几何学习乐园的核心界面层，负责提供用户界面并协调各个功能模块之间的交互。该模块采用了 MVC (模型-视图-控制器) 架构设计，使用 Java Swing 实现了现代化的用户界面，并通过 UIManager 实现了统一的界面管理。

## 模块结构

```
gui/
├── UIManager.java                       # UI 管理器（单例模式）
├── MainWindow.java                      # 主窗口
├── TaskWindow.java                      # 任务窗口
├── ResultWindow.java                    # 结果窗口
├── tasks/                               # 任务面板
│   ├── TaskPanelInterface.java          # 任务面板接口
│   ├── BaseTaskPanel.java               # 任务面板基类
│   ├── AngleCalculationPanel.java       # 角度计算任务面板
│   ├── AreaCalculationPanel.java        # 面积计算任务面板
│   ├── CircleCalculationPanel.java      # 圆形计算任务面板
│   ├── CompoundShapeCalculationPanel.java  # 复合形状计算任务面板
│   ├── SectorCalculationPanel.java      # 扇形计算任务面板
│   └── ShapePanel.java                  # 形状识别任务面板
└── shapes/                              # 形状绘制
    ├── ShapeRenderer.java               # 形状渲染接口
    ├── CompoundShapeDrawer.java         # 复合形状绘制基类
    ├── ShapeDrawer.java                 # 基本形状绘制工具类
    ├── CircleDrawer.java                # 圆形绘制专用类
    └── compound/                        # 复合形状实现目录
        ├── ArrowShape.java              # 箭头形状
        ├── ComplexStairShape.java       # 复杂阶梯形状
        ├── DoubleStairShape.java        # 双阶梯形状
        ├── HouseShape.java              # 房屋形状
        ├── IrregularShape.java          # 不规则形状
        ├── StairShape.java              # 阶梯形状
        ├── StepShape.java               # 台阶形状
        ├── TrapezoidShape.java          # 梯形
        └── TShape.java                  # T形状
```

# 第一部分：核心窗口与管理

## UIManager.java

UIManager 是 GUI 模块的核心控制器，采用单例模式设计，负责管理所有窗口的创建、切换和交互。

### 主要职责
- 提供窗口间的导航（主窗口、任务窗口、结果窗口）
- 管理任务状态（锁定、解锁、进行中、完成）
- 跟踪用户进度和分数
- 处理任务解锁条件（基础任务完成后解锁高级任务）
- 计算用户等级和整体进度

### 核心功能
- **初始化**：`initialize()` 创建主窗口并启动应用
- **窗口切换**：`showMainWindow()`, `switchToTask()`, `showResult()`
- **任务管理**：`checkAndUnlockTasks()`, `isTaskUnlocked()`
- **进度计算**：`calculateOverallProgress()`, `calculateTotalScore()`

### 任务状态枚举
```java
public enum TaskStatus {
    LOCKED,        // 未解锁
    UNLOCKED,      // 已解锁
    IN_PROGRESS,   // 进行中
    COMPLETED      // 已完成
}
```

## MainWindow.java

MainWindow 是应用程序的主入口窗口，展示了所有可用的学习任务和用户进度。

### 主要职责
- 显示任务选择界面（基础和高级任务）
- 展示用户等级和学习进度
- 管理任务按钮的状态（锁定、解锁、进行中、完成）
- 提供模式切换功能（完整功能/正常模式）

### 核心组件
- **任务按钮**：展示各个学习任务的按钮
- **进度条**：显示整体学习进度
- **等级标签**：显示用户当前等级
- **模式切换按钮**：切换完整功能/正常模式

### 核心方法
- `updateTaskStatus()`：根据任务状态更新按钮外观
- `updateProgress()`：更新进度条
- `updateUserLevel()`：更新用户等级显示

## TaskWindow.java

TaskWindow 负责加载和显示具体任务的界面，作为各个任务面板的容器。

### 主要职责
- 根据任务类型加载相应的任务面板
- 显示任务说明和反馈
- 与 UIManager 通信，报告任务结果

### 核心组件
- **任务描述区**：显示任务说明
- **输入面板**：容纳具体任务面板
- **反馈区**：显示即时反馈

### 核心方法
- `setupTask()`：根据任务名称初始化相应的任务面板
- `setFeedback()`/`appendFeedback()`：设置/追加反馈信息
- `showResult()`：显示任务结果并通知 UIManager

## ResultWindow.java

ResultWindow 负责展示任务完成后的结果和反馈，提供动画效果和详细分析。

### 主要职责
- 动画显示任务得分
- 提供星级评价（1-5星）
- 展示详细反馈和成绩分析
- 提供后续操作选项（继续学习、重新尝试、结束学习）

### 核心组件
- **分数标签**：显示得分（带动画效果）
- **星级面板**：显示星级评价
- **反馈面板**：显示详细反馈
- **分析面板**：显示完成度、正确率和表现评级
- **操作按钮**：提供后续操作选项

### 动画效果
- 使用 `Timer` 实现分数递增动画
- 根据得分动态更新星级
- 使用颜色渐变显示评级

## 窗口交互流程

### 1. 启动流程
1. 应用程序启动 -> `UIManager.initialize()`
2. 创建并显示 MainWindow
3. 用户查看可用任务

### 2. 任务执行流程
1. 用户在 MainWindow 选择任务 -> `UIManager.switchToTask()`
2. 创建 TaskWindow 并加载相应任务面板
3. 用户执行任务，获取反馈
4. 任务完成 -> `TaskWindow.showResult()` -> `UIManager.showResult()`
5. 创建并显示 ResultWindow

### 3. 结果处理流程
1. ResultWindow 显示得分和反馈
2. 用户选择后续操作：
   - 继续学习 -> 返回 MainWindow
   - 重新尝试 -> 重新创建 TaskWindow
   - 结束学习 -> 退出应用程序

# 第二部分：任务面板模块 (tasks)

## 概述

任务面板模块是 Shapeville 几何学习乐园的核心交互层，负责向用户呈现各种几何学习任务的界面，处理用户输入，并提供即时反馈。该模块通过一套统一的接口和基类设计，提供了可扩展的任务框架，使开发者能够轻松地添加新的任务类型。

## 接口与基类

### TaskPanelInterface.java

所有任务面板必须实现的接口，定义了任务生命周期的基本方法。

#### 主要方法
- `void startTask()` - 开始任务
- `void pauseTask()` - 暂停任务
- `void resumeTask()` - 恢复任务
- `void endTask()` - 结束任务
- `int getScore()` - 获取任务得分
- `String getFeedback()` - 获取任务反馈信息

### BaseTaskPanel.java

所有任务面板的抽象基类，实现了通用功能并定义了子类必须实现的抽象方法。

#### 主要属性
- `Timer timer` - 用于计时的定时器
- `int attempts` - 当前题目的尝试次数
- `String taskName` - 任务名称
- `List<Integer> attemptsPerTask` - 每个子任务的尝试次数列表
- `TaskWindow parentWindow` - 父窗口引用

#### 主要方法
- 抽象方法：
  - `void initializeUI()` - 初始化用户界面
  - `void handleSubmit()` - 处理提交事件
  - `void reset()` - 重置任务状态
  - `int calculateScore()` - 计算任务得分

- 实用方法：
  - `void setFeedback(String message)` - 设置反馈信息
  - `void appendFeedback(String message)` - 追加反馈信息
  - `void incrementAttempts()` - 增加尝试次数
  - `void resetAttempts()` - 重置尝试次数
  - `boolean hasRemainingAttempts()` - 检查是否还有剩余尝试次数
  - `void cleanup()` - 清理资源

## 具体任务面板实现

所有任务面板都继承自 `BaseTaskPanel` 并实现了 `TaskPanelInterface` 接口。

### 1. AngleCalculationPanel.java

角度识别与计算任务面板，用于训练用户识别不同类型的角度。

#### 主要功能
- 生成可视化的角度显示
- 允许用户通过微调器输入角度值
- 提供角度类型选择（锐角、直角、钝角等）
- 提供即时反馈和正确答案
- 追踪已识别的角度类型
- 完成任务的进度监控

#### 核心组件
- 角度可视化绘制区
- 角度输入微调器
- 角度类型选择下拉框
- 提交按钮
- 返回主页按钮

### 2. AreaCalculationPanel.java

面积计算任务面板，用于训练用户计算各种基本几何形状的面积。

#### 主要功能
- 显示各种几何形状（矩形、三角形、平行四边形、梯形）
- 提供形状参数和计算公式
- 验证用户计算结果
- 显示正确答案和计算步骤
- 显示进度和得分

### 3. CircleCalculationPanel.java

圆形计算任务面板，专注于圆的周长和面积计算训练。

#### 主要功能
- 提供圆形可视化显示
- 支持半径或直径的计算任务
- 提供公式和计算步骤
- 验证用户计算结果
- 显示进度和得分

### 4. CompoundShapeCalculationPanel.java

复合形状计算任务面板，用于训练用户计算复杂几何形状的面积。

#### 主要功能
- 显示由基本形状组合而成的复杂图形
- 提供形状的详细描述
- 验证用户的面积计算结果
- 显示详细的计算步骤和解题思路
- 支持形状选择和切换
- 提供计时功能

#### 核心组件
- 形状下拉选择器
- 形状显示面板（具有绘制功能）
- 形状描述区域
- 答案输入字段
- 计时器显示
- 解题步骤显示区域
- 下一题按钮

### 5. SectorCalculationPanel.java

扇形计算任务面板，专注于扇形面积和弧长计算训练。

#### 主要功能
- 显示具有不同角度和半径的扇形
- 提供扇形参数和计算公式
- 验证用户的面积和弧长计算结果
- 显示详细的计算步骤
- 支持多个扇形练习任务

### 6. ShapePanel.java

形状识别任务面板，用于训练用户识别2D和3D几何形状。

#### 主要功能
- 显示各种2D和3D几何形状
- 要求用户输入形状的英文名称
- 提供识别提示和反馈
- 支持2D和3D模式切换
- 跟踪已识别的形状类型

## 任务面板生命周期

每个任务面板都遵循以下生命周期：

1. **初始化** - 构造函数创建业务逻辑对象，初始化UI
2. **开始任务** - `startTask()` 方法启动计时器，设置初始状态
3. **交互阶段** - 用户与界面交互，提交答案，获取反馈
4. **暂停/恢复** - 可选阶段，允许暂停和恢复任务
5. **结束任务** - `endTask()` 方法停止计时器，计算最终得分，通知父窗口任务完成
6. **清理资源** - `cleanup()` 方法释放资源

## 开发新任务面板的步骤

要开发新的任务面板，请按照以下步骤操作：

1. 创建新的类，继承 `BaseTaskPanel` 并实现 `TaskPanelInterface`：

```java
public class NewTaskPanel extends BaseTaskPanel implements TaskPanelInterface {
    
    public NewTaskPanel() {
        super("新任务名称");
        // 初始化业务逻辑对象
        initializeUI();
    }
    
    @Override
    public void initializeUI() {
        // 初始化UI组件
    }
    
    @Override
    public void handleSubmit() {
        // 处理用户提交的答案
    }
    
    @Override
    public void reset() {
        // 重置任务状态
    }
    
    @Override
    protected int calculateScore() {
        // 计算任务得分
        return score;
    }
    
    @Override
    public void startTask() {
        // 开始任务
    }
    
    @Override
    public void pauseTask() {
        // 暂停任务
    }
    
    @Override
    public void resumeTask() {
        // 恢复任务
    }
    
    @Override
    public void endTask() {
        // 结束任务并通知父窗口
        if (parentWindow != null) {
            parentWindow.taskCompleted(getScore(), getFeedback());
        }
    }
    
    @Override
    public int getScore() {
        return calculateScore();
    }
    
    @Override
    public String getFeedback() {
        // 返回任务反馈信息
        return feedback;
    }
}
```

2. 在 `UIManager` 中注册新的任务面板

3. 在 `TaskWindow` 中添加对新任务面板的支持

## 设计模式与最佳实践

### 1. 组件设计模式

- **模板方法模式** - `BaseTaskPanel` 定义了任务面板的骨架，子类实现具体行为
- **策略模式** - 各种计算逻辑可以被视为不同的策略
- **观察者模式** - 通过 `parentWindow` 对象，任务面板可以通知父窗口任务状态变化

### 2. UI 组件复用

- 使用 `JPanel`、`JButton`、`JTextField` 等标准 Swing 组件
- 合理组织组件层次，使用布局管理器（如 `BorderLayout`、`FlowLayout`）
- 对于特殊的显示需求，创建自定义绘制面板

### 3. 交互设计

- 提供清晰的用户指导和反馈
- 实现合理的错误处理和输入验证
- 使用一致的交互模式（如提交按钮、反馈区域）

# 第三部分：形状渲染模块 (shapes)

## 概述

Shapes 模块是 Shapeville 几何学习乐园的图形渲染层，负责各种几何图形的可视化绘制。该模块提供了从基本形状到复杂复合形状的各种绘制工具，确保用户能够直观地理解几何概念。

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

# 第四部分：通用开发指南

## 添加新窗口

要添加新的窗口，请按照以下步骤操作：

1. 创建一个继承自 `JFrame` 的新类：
```java
public class NewWindow extends JFrame {
    public NewWindow() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Shapeville - 新窗口");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // 添加组件...
    }
}
```

2. 在 `UIManager` 中添加管理新窗口的方法：
```java
public void showNewWindow() {
    if (currentWindow != null) {
        currentWindow.dispose();
    }
    NewWindow newWindow = new NewWindow();
    newWindow.setVisible(true);
    currentWindow = newWindow;
}
```

## 添加新任务类型

要添加新的任务类型，请按照以下步骤操作：

1. 在 `tasks` 目录中创建新的任务面板类（详见上文任务面板开发步骤）

2. 更新 `UIManager` 中的任务数组：
```java
private static final String[] BASIC_TASKS = {
    "形状识别", "角度识别", "面积计算", "圆形计算", "新任务名称"
};
```

3. 在 `TaskWindow.setupTask()` 方法中添加新任务的处理：
```java
else if (taskName.equals("新任务名称")) {
    currentTask = new NewTaskPanel();
    inputPanel.add((JPanel)currentTask, BorderLayout.CENTER);
    if (currentTask instanceof BaseTaskPanel) {
        ((BaseTaskPanel) currentTask).setParentWindow(this);
    }
}
```

4. 在 `MainWindow` 中添加新任务按钮：
```java
addTaskButton("新任务名称", "新任务描述（基础难度）", "basic");
```

## 添加新用户界面组件

1. 创建具有一致外观的按钮：
```java
private JButton createStyledButton(String text, Color color) {
    JButton button = new JButton(text);
    button.setFont(new Font("微软雅黑", Font.BOLD, 14));
    button.setForeground(Color.WHITE);
    button.setBackground(color);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setOpaque(true);
    return button;
}
```

2. 创建与应用风格匹配的面板：
```java
JPanel panel = new JPanel(new BorderLayout(10, 10));
panel.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
    BorderFactory.createEmptyBorder(15, 15, 15, 15)
));
```

## 修改任务解锁条件

要修改任务解锁条件，请编辑 `UIManager.checkAndUnlockTasks()` 方法：

```java
private void checkAndUnlockTasks() {
    // 检查基础任务的完成情况和总分
    boolean canUnlockAdvanced = true;
    for (String task : BASIC_TASKS) {
        int score = taskScores.getOrDefault(task, 0);
        if (score < 60) {  // 修改最低分数要求
            canUnlockAdvanced = false;
            break;
        }
    }
    
    // 其余逻辑保持不变...
}
```

## 更改外观和风格

1. 修改全局颜色方案：编辑各窗口中的颜色常量
2. 更改字体：修改各组件的字体设置
3. 调整布局：修改各窗口的布局管理器和组件大小

## 与其他模块集成

GUI 模块与其他模块的集成主要通过以下方式实现：

1. **与 Game 模块集成**：
   - 任务面板通过实例化相应的 Game 类进行集成
   - 例如，AngleCalculationPanel 使用 AngleCalculation 对象

2. **与 Model 模块集成**：
   - 主要通过 UIManager 跟踪用户进度和分数
   - 使用 Model 中定义的枚举类型，如 Shape2D 和 Shape3D

3. **与 Utils 模块集成**：
   - 使用工具函数进行数据处理和验证
   - 通过 DataManager 持久化用户进度

## 设计模式应用

GUI 模块运用了多种设计模式：

1. **单例模式**：UIManager 采用单例模式确保全局唯一的界面管理
2. **模板方法模式**：BaseTaskPanel 定义了任务面板的基本结构
3. **策略模式**：不同任务面板实现了不同的交互策略
4. **观察者模式**：窗口间的通信采用了类似观察者的模式
5. **工厂方法**：TaskWindow 中的 setupTask() 方法类似于简单工厂

## 注意事项

1. **线程安全**：UI 更新应在 EDT (Event Dispatch Thread) 中进行
    ```java
    SwingUtilities.invokeLater(() -> {
        // UI 更新代码
    });
    ```

2. **资源释放**：窗口关闭时应释放资源
    ```java
    @Override
    public void dispose() {
        cleanup();  // 释放资源
        super.dispose();
    }
    ```

3. **错误处理**：合理处理异常并向用户显示友好的错误消息
    ```java
    try {
        // 操作代码
    } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "发生错误：" + e.getMessage(),
            "错误",
            JOptionPane.ERROR_MESSAGE
        );
    }
    ```

4. **布局管理**：使用适当的布局管理器确保界面在不同屏幕分辨率下正常显示

5. **可访问性**：考虑键盘导航和屏幕阅读器等辅助功能

## 性能优化

1. **延迟初始化**：采用延迟初始化策略减少启动时间
2. **图像缓存**：对于频繁使用的图像进行缓存
3. **窗口重用**：在适当情况下重用窗口而非重新创建
4. **异步加载**：将耗时操作放在后台线程中执行

## 未来扩展方向

1. **本地化支持**：添加多语言支持
2. **主题切换**：实现明暗主题切换
3. **用户账户系统**：实现多用户支持
4. **云同步**：添加云端数据同步功能
5. **增强反馈**：添加更详细的学习分析和建议
6. **可定制界面**：允许用户自定义界面布局和颜色
7. **3D形状渲染**: 添加3D几何形状的绘制和交互功能
8. **动态形状生成**: 根据参数动态生成更多样化的形状
9. **形状动画**: 实现形状变换和组合的动画效果
10. **移动适配**: 优化触摸屏设备上的显示效果 