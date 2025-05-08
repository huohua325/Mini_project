# Shapeville 任务面板（Tasks）模块详解

## 目录
- [Shapeville 任务面板（Tasks）模块详解](#shapeville-任务面板tasks模块详解)
	- [目录](#目录)
	- [概述](#概述)
	- [整体架构](#整体架构)
	- [核心组件](#核心组件)
		- [TaskPanelInterface 接口](#taskpanelinterface-接口)
		- [BaseTaskPanel 基类](#basetaskpanel-基类)
	- [具体任务面板](#具体任务面板)
		- [AngleCalculationPanel 角度计算面板](#anglecalculationpanel-角度计算面板)
		- [AreaCalculationPanel 面积计算面板](#areacalculationpanel-面积计算面板)
		- [ShapePanel 形状识别面板](#shapepanel-形状识别面板)
		- [CompoundShapeCalculationPanel 复合形状计算面板](#compoundshapecalculationpanel-复合形状计算面板)
		- [SectorCalculationPanel 扇形计算面板](#sectorcalculationpanel-扇形计算面板)
	- [数据流向](#数据流向)
	- [UI交互流程](#ui交互流程)
	- [与Game模块的交互](#与game模块的交互)
	- [扩展指南](#扩展指南)

## 概述

`com.shapeville.gui.tasks` 包是Shapeville应用的GUI层中负责具体任务交互的部分。每个任务面板都是一个独立的Swing组件，负责特定几何概念的用户交互、数据验证和结果展示。它们遵循相同的接口规范，通过继承共享基本功能，同时通过调用`game`包中的对应类实现业务逻辑。

这些任务面板被`TaskWindow`类动态加载并显示，是用户与应用交互的主要界面。

## 整体架构

任务面板模块采用了典型的层次结构：

1. **顶层接口**：`TaskPanelInterface` 定义了所有任务面板必须实现的方法
2. **抽象基类**：`BaseTaskPanel` 提供了共同的功能实现和状态管理
3. **具体实现类**：如`AngleCalculationPanel`、`ShapePanel`等，实现特定任务的具体交互逻辑

```
TaskPanelInterface (接口)
      ↑
BaseTaskPanel (抽象类)
      ↑
┌─────┼─────┬─────┬─────┐
│     │     │     │     │
Angle Area Shape Sector Compound
Panel Panel Panel Panel Panel
```

## 核心组件

### TaskPanelInterface 接口

`TaskPanelInterface`定义了任务面板的生命周期管理和状态查询方法：

```java
public interface TaskPanelInterface {
    void startTask();   // 开始任务
    void pauseTask();   // 暂停任务
    void resumeTask();  // 恢复任务
    void endTask();     // 结束任务
    int getScore();     // 获取分数
    String getFeedback(); // 获取反馈信息
}
```

这些方法使`TaskWindow`能够统一管理不同任务的启动、暂停、恢复和结束，以及获取任务的分数和反馈信息。

### BaseTaskPanel 基类

`BaseTaskPanel`是所有任务面板的基类，继承自`JPanel`并提供了以下共同功能：

1. **UI初始化**：预先设置布局和反馈区域
2. **尝试次数管理**：跟踪用户每个任务的尝试次数（最多3次）
3. **反馈展示**：集中管理反馈信息的显示
4. **分数计算**：根据尝试次数计算得分（1次尝试=3分，2次=2分，3次=1分）

```java
protected int calculateScore() {
    int totalScore = 0;
    for (int attempts : attemptsPerTask) {
        if (attempts == 1) totalScore += 3;
        else if (attempts == 2) totalScore += 2;
        else if (attempts == 3) totalScore += 1;
    }
    return (int)((double)totalScore / (attemptsPerTask.size() * 3) * 100);
}
```

`BaseTaskPanel`定义了子类必须实现的抽象方法：
- `initializeUI()`: 初始化具体任务的UI组件
- `handleSubmit()`: 处理用户提交的答案
- `reset()`: 重置任务状态
- `calculateScore()`: 计算任务分数

## 具体任务面板

### AngleCalculationPanel 角度计算面板

**功能**：帮助用户识别不同类型的角度（锐角、直角、钝角、平角、优角）。

**核心数据结构**：
```java
private final List<Integer> angles;  // 存储角度值
private final Set<String> identifiedTypes;  // 已识别的角度类型
private int currentAngle;  // 当前显示的角度
```

**主要交互流程**：
1. 初始化时生成10°~350°范围内的随机角度
2. 通过绘图在`angleDisplayPanel`上显示当前角度
3. 用户从下拉框中选择角度类型并提交
4. 系统验证答案，记录尝试次数，并给出反馈
5. 用户需要成功识别5种不同类型的角度或直到角度列表为空

**特点**：
- 独立实现角度判定逻辑，没有依赖game包中的对应类
- 使用自定义绘制方法展示角度
- 使用集合跟踪已识别的角度类型，确保每种类型只需识别一次

### AreaCalculationPanel 面积计算面板

**功能**：帮助用户计算基本几何形状的面积。

**核心数据结构**：
```java
private AreaCalculation areaCalculation;  // 引用game包中的面积计算类
private int currentShapeIndex = 0;  // 当前形状索引
```

**主要交互流程**：
1. 系统展示当前形状、计算公式和参数
2. 用户输入计算结果并提交
3. 系统验证答案，记录尝试次数，并给出反馈
4. 完成所有4种形状的计算

**特点**：
- 直接调用`game`包中的`AreaCalculation`类完成核心业务逻辑
- 清晰展示每种形状的计算公式和参数
- 使用索引跟踪当前处理的形状

### ShapePanel 形状识别面板

**功能**：帮助用户学习2D和3D形状的英文名称。

**核心数据结构**：
```java
private final ShapeRecognition shapeRecognition;  // 引用game包中的形状识别类
private int currentShapeIndex = 0;  // 当前形状索引
private boolean is2DMode = true;  // 当前模式(2D或3D)
```

**主要交互流程**：
1. 用户选择2D或3D模式
2. 系统显示形状的中文名称
3. 用户输入对应的英文名称并提交
4. 系统验证答案，记录尝试次数，并给出反馈
5. 需要正确识别4个形状才能完成任务

**特点**：
- 支持2D和3D两种模式切换
- 直接调用`game`包中的`ShapeRecognition`类完成答案验证
- 使用索引跟踪当前处理的形状

### CompoundShapeCalculationPanel 复合形状计算面板

**功能**：训练用户计算由多个基本形状组成的复杂图形面积。

**核心数据结构**：
```java
private final CompoundShapeCalculation compoundCalculation;  // 引用game包中的复合形状计算类
private int currentShapeIndex = 0;  // 当前形状索引
```

**主要交互流程**：
1. 用户从下拉框中选择要计算的复合形状
2. 系统显示形状描述
3. 用户计算并输入面积
4. 系统验证答案，并在正确或用尽尝试次数后显示详细解题步骤
5. 完成所有预定义的复合形状计算

**特点**：
- 提供下拉框让用户选择要练习的形状
- 展示详细的形状描述和解题步骤
- 使用`Set`跟踪已完成的形状，允许非线性练习顺序

### SectorCalculationPanel 扇形计算面板

**功能**：训练用户计算扇形的面积。

**核心数据结构**：
```java
private final SectorCalculation sectorCalculation;  // 引用game包中的扇形计算类
private int currentSectorIndex = 0;  // 当前扇形索引
private JPanel sectorDisplayPanel;  // 扇形显示面板
```

**主要交互流程**：
1. 用户从下拉框中选择要计算的扇形
2. 系统在`sectorDisplayPanel`上图形化显示扇形，同时显示半径和角度参数
3. 用户计算并输入扇形面积
4. 系统验证答案，并在正确或用尽尝试次数后显示详细解题步骤
5. 完成所有预定义的扇形计算

**特点**：
- 使用自定义绘制方法直观展示扇形
- 结合图形展示和参数展示，帮助理解
- 提供详细的解题步骤
- 使用`Set`跟踪已完成的扇形，允许非线性练习顺序

## 数据流向

Task面板的数据流主要遵循以下模式：

1. **初始化流程**：
   ```
   TaskWindow创建 → 特定任务面板实例化 → 加载Game模块对应类 → 初始化UI
   ```

2. **提交答案流程**：
   ```
   用户输入 → handleSubmit() → (可能)调用Game模块验证 → 更新尝试次数 → 反馈显示 → (可能)进入下一题
   ```

3. **任务完成流程**：
   ```
   任务完成条件满足 → endTask() → 计算分数 → TaskWindow获取结果 → ResultWindow显示
   ```

4. **状态管理流程**：
   ```
   用户操作 → Task面板状态更新 → 反馈UI更新 → (可能)通知TaskWindow
   ```

## UI交互流程

所有任务面板都遵循类似的UI交互流程：

1. **显示当前任务**：展示当前需要解决的问题（角度、形状、几何图形等）
2. **收集用户输入**：通过文本框、下拉框等收集用户答案
3. **验证答案**：检查用户答案是否正确
4. **提供反馈**：在反馈区域显示结果（正确/错误）
5. **管理尝试次数**：每个问题最多3次尝试机会
6. **进入下一题或结束**：根据任务完成条件决定下一步

## 与Game模块的交互

除了`AngleCalculationPanel`，大多数任务面板都通过以下方式与`game`包中的对应类交互：

1. **引用持有**：面板类持有对应game类的实例
   ```java
   private final ShapeRecognition shapeRecognition;
   ```

2. **初始化委托**：面板初始化时调用game类的初始化方法
   ```java
   shapeRecognition.reset();
   ```

3. **业务逻辑委托**：关键业务逻辑委托给game类处理
   ```java
   boolean correct = shapeRecognition.check2DAnswer(shape, answer);
   ```

4. **状态同步**：保持面板UI状态与game类的数据状态同步
   ```java
   if (compoundCalculation.isComplete()) {
       endTask();
   }
   ```

这种设计实现了UI层和业务逻辑层的分离，提高了代码的可维护性和可测试性。

## 扩展指南

如果需要添加新的任务面板，应遵循以下步骤：

1. **创建新类**：在`com.shapeville.gui.tasks`包中创建新类，继承`BaseTaskPanel`并实现`TaskPanelInterface`

   ```java
   public class NewTaskPanel extends BaseTaskPanel implements TaskPanelInterface {
       public NewTaskPanel() {
           super("新任务名称");
       }
       
       // 实现必要的方法...
   }
   ```

2. **实现抽象方法**：
   - `initializeUI()`: 初始化特定任务的UI组件
   - `handleSubmit()`: 处理用户提交的答案
   - `reset()`: 重置任务状态
   - `calculateScore()`: 计算分数（通常可以直接复用基类的实现）

3. **实现接口方法**：
   - `startTask()`: 通常调用`reset()`
   - `pauseTask()`: 禁用输入组件
   - `resumeTask()`: 启用输入组件
   - `endTask()`: 清理资源、计算最终分数
   - `getScore()`: 返回计算的分数
   - `getFeedback()`: 返回反馈信息

4. **考虑与Game模块的交互**：创建对应的game类或直接在面板中实现业务逻辑

5. **在TaskWindow中注册**：确保TaskWindow能够加载和显示新的任务面板

遵循这些步骤和已有面板的模式，可以方便地扩展系统支持新的几何概念和学习任务。 