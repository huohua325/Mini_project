# Shapeville 任务面板模块开发指南

## 概述

任务面板模块是 Shapeville 几何学习乐园的核心交互层，负责向用户呈现各种几何学习任务的界面，处理用户输入，并提供即时反馈。该模块通过一套统一的接口和基类设计，提供了可扩展的任务框架，使开发者能够轻松地添加新的任务类型。

## 模块结构

```
tasks/
├── TaskPanelInterface.java              # 任务面板接口
├── BaseTaskPanel.java                   # 任务面板基类
├── AngleCalculationPanel.java           # 角度计算任务面板
├── AreaCalculationPanel.java            # 面积计算任务面板
├── CircleCalculationPanel.java          # 圆形计算任务面板
├── CompoundShapeCalculationPanel.java   # 复合形状计算任务面板
├── SectorCalculationPanel.java          # 扇形计算任务面板
└── ShapePanel.java                      # 形状识别任务面板
```

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

## 增强现有任务面板

要增强现有任务面板，可以考虑以下方面：

1. **视觉增强**
   - 添加动画效果
   - 改进颜色和样式
   - 增加更多的视觉提示

2. **功能增强**
   - 添加难度级别选择
   - 实现更详细的反馈系统
   - 添加额外的学习资源和提示

3. **交互增强**
   - 添加键盘快捷键
   - 支持触摸操作
   - 添加拖放功能

## 与其他模块的集成

任务面板模块与以下模块紧密集成：

1. **TaskWindow** - 任务面板的容器，负责显示任务面板并处理任务完成事件
2. **Game** - 提供任务的业务逻辑，任务面板通过它验证答案和计算得分
3. **Shapes** - 提供形状的绘制功能，特别是在复合形状任务中

## 注意事项

1. 确保 UI 代码与业务逻辑分离，避免在任务面板中包含复杂的计算逻辑
2. 保持用户界面的一致性，新的任务面板应该与现有面板风格一致
3. 仔细处理错误情况和边界条件，提供用户友好的错误消息
4. 避免硬编码的字符串，考虑将文本放入资源文件以支持多语言
5. 使用 `cleanup()` 方法确保正确释放资源，特别是计时器和事件监听器

## 单元测试建议

为任务面板开发单元测试时，应该测试以下方面：

1. UI 组件的正确初始化
2. 任务流程的完整性（开始、交互、结束）
3. 用户输入的验证
4. 得分计算的准确性
5. 与父窗口的交互

## 未来扩展方向

1. **移动适配** - 优化任务面板以适应移动设备的触摸交互
2. **多媒体增强** - 添加声音效果和视频教程
3. **实时协作** - 支持多用户同时参与任务
4. **学习分析** - 收集更详细的用户行为数据，提供个性化的学习建议
5. **虚拟现实集成** - 将任务面板扩展到VR环境中，提供沉浸式几何学习体验 