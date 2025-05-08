# Shapeville GUI模块详解

## 目录
- [Shapeville GUI模块详解](#shapeville-gui模块详解)
	- [目录](#目录)
	- [概述](#概述)
	- [核心组件](#核心组件)
		- [MainWindow 主窗口](#mainwindow-主窗口)
		- [ResultWindow 结果窗口](#resultwindow-结果窗口)
		- [TaskWindow 任务窗口](#taskwindow-任务窗口)
		- [UIManager UI管理器](#uimanager-ui管理器)
	- [数据流向](#数据流向)
	- [状态管理](#状态管理)
	- [界面导航](#界面导航)
	- [与其他模块的交互](#与其他模块的交互)
	- [扩展指南](#扩展指南)

## 概述

Shapeville的GUI模块是应用程序的表现层，负责所有用户界面的呈现和交互逻辑。该模块采用了Model-View-Controller架构的变体，通过UIManager统一管理各个窗口之间的切换和数据传递。

GUI模块主要包含四个核心类：
- `MainWindow`：应用程序的主界面，展示所有可用任务
- `TaskWindow`：特定任务的执行窗口，加载并显示任务面板
- `ResultWindow`：任务完成后的结果展示窗口
- `UIManager`：统一管理UI状态、窗口切换和任务数据的单例类

整个GUI模块基于Java Swing构建，遵循组件化设计原则，每个窗口和面板都是独立的组件，可以单独测试和维护。

## 核心组件

### MainWindow 主窗口

MainWindow是应用程序的入口界面，实现以下核心功能：

1. **任务展示与选择**：以网格形式展示所有可用的学习任务
2. **任务状态显示**：通过颜色区分任务的解锁、进行中和完成状态
3. **进度跟踪**：显示用户的总体学习进度和当前等级
4. **导航控制**：提供主页和退出按钮

关键属性：
```java
private JProgressBar progressBar;  // 进度条
private Map<String, JButton> taskButtons;  // 任务按钮映射
private Map<String, Boolean> taskCompletionStatus;  // 任务完成状态
```

关键方法：
```java
private void addTaskButton(String text, String tooltip, String difficulty)  // 添加任务按钮
public void setTaskCompleted(String taskName)  // 设置任务为已完成
public void updateProgress()  // 更新总体进度
public void updateTaskStatus(Map<String, TaskStatus> taskStatusMap)  // 更新任务状态
```

MainWindow的视觉设计注重用户友好性，采用色彩编码区分任务难度和状态，并使用对比鲜明的按钮样式提高可识别性。

### ResultWindow 结果窗口

ResultWindow负责在任务完成后展示用户的表现结果，包括：

1. **分数展示**：通过动画效果展示得分
2. **星级评价**：基于得分显示1-5颗星
3. **详细反馈**：显示任务的具体反馈信息
4. **成绩分析**：包括完成度、正确率和表现评级
5. **后续操作按钮**：继续学习、重新尝试或结束学习

关键属性：
```java
private final int score;  // 得分
private final String feedback;  // 反馈信息
private final String taskName;  // 任务名称
private Timer animationTimer;  // 分数动画计时器
```

关键方法：
```java
private void startScoreAnimation()  // 启动分数动画
private void updateStars(int starCount)  // 更新星级评价
private String getPerformanceLevel()  // 获取表现等级
```

ResultWindow采用了动画效果增强用户体验，分数逐渐增加的动画和星级变化提供了直观的成就感反馈。

### TaskWindow 任务窗口

TaskWindow是执行特定学习任务的容器窗口，主要职责包括：

1. **加载任务面板**：根据任务名称动态加载对应的任务面板
2. **展示任务说明**：显示当前任务的详细说明
3. **提供控制按钮**：提供开始、暂停、继续和结束按钮
4. **计时功能**：跟踪任务完成时间
5. **任务状态监控**：监控任务的进度和完成情况

TaskWindow通过动态加载来自`tasks`包的任务面板类，实现了系统对不同几何任务的可扩展性。

### UIManager UI管理器

UIManager是整个GUI模块的核心控制器，负责：

1. **窗口管理**：控制不同窗口之间的切换
2. **任务状态管理**：跟踪每个任务的状态（锁定、解锁、进行中、完成）
3. **进度与评分系统**：计算总体进度和更新用户等级
4. **任务解锁逻辑**：基于完成情况解锁高级任务

关键属性：
```java
private static UIManager instance;  // 单例实例
private Map<String, TaskStatus> taskStatusMap;  // 任务状态映射
private Map<String, Integer> taskScores;  // 任务分数映射
private Set<String> unlockedTasks;  // 已解锁任务集合
```

关键方法：
```java
public void switchToTask(String taskName)  // 切换到指定任务
public void showResult(String taskName, int score, String feedback)  // 显示任务结果
private void checkAndUnlockTasks()  // 检查并解锁任务
private void updateUserLevel()  // 更新用户等级
```

UIManager采用单例模式确保全局只有一个UI状态管理器，所有窗口通过它通信，避免状态不一致问题。

## 数据流向

GUI模块中的数据流主要遵循以下路径：

1. **用户选择任务**：
   ```
   MainWindow → UIManager.switchToTask() → TaskWindow创建 → 特定任务面板加载
   ```

2. **任务执行流程**：
   ```
   TaskWindow → TaskPanel接口 → 用户输入 → 提交验证 → 结果反馈
   ```

3. **任务完成流程**：
   ```
   TaskPanel.endTask() → TaskWindow.finish() → UIManager.showResult() → ResultWindow创建 → 更新MainWindow状态
   ```

4. **返回主界面**：
   ```
   ResultWindow → UIManager.showMainWindow() → MainWindow更新 → 可能解锁新任务
   ```

这种流程设计确保了数据和状态的一致性，通过UIManager作为中介，不同窗口之间不直接交互，降低了组件间的耦合度。

## 状态管理

GUI模块采用集中式状态管理，所有状态都由UIManager维护：

1. **任务状态**：通过`TaskStatus`枚举定义四种状态（锁定、解锁、进行中、完成）
2. **用户等级**：基于完成任务数和总分计算，分为四个等级（初学者、中级学习者、高级学习者、专家）
3. **进度计算**：综合考虑完成任务数和总分，计算总体进度百分比
4. **任务解锁条件**：基础任务完成且得分达到70分以上才能解锁高级任务

```java
private void updateUserLevel() {
    int totalScore = calculateTotalScore();
    int completedTasks = countCompletedTasks();
    
    if (totalScore >= 540 && completedTasks >= 6) { // 90分 * 6个任务
        userLevel = 4; // 专家
    } else if (totalScore >= 420 && completedTasks >= 5) { // 70分 * 6个任务
        userLevel = 3; // 高级
    } else if (totalScore >= 300 && completedTasks >= 4) { // 60分 * 5个任务
        userLevel = 2; // 中级
    } else {
        userLevel = 1; // 初学者
    }
}
```

## 界面导航

应用程序的界面导航流程如下：

1. **启动**：程序启动显示MainWindow
2. **任务选择**：用户从MainWindow选择任务，进入TaskWindow
3. **任务执行**：在TaskWindow中完成任务
4. **结果展示**：任务完成后显示ResultWindow
5. **后续选择**：
   - 从ResultWindow继续学习回到MainWindow
   - 从ResultWindow重新尝试回到TaskWindow
   - 从ResultWindow或MainWindow退出程序

这种导航结构形成了一个完整的学习循环，便于用户连续练习不同的几何概念。

## 与其他模块的交互

GUI模块主要与以下其他模块交互：

1. **Tasks模块**：TaskWindow通过`TaskPanelInterface`加载和管理不同的任务面板
   ```java
   // TaskWindow中加载任务面板的代码片段
   private void loadTaskPanel(String taskName) {
       // 根据任务名称动态创建对应的任务面板
   }
   ```

2. **Game模块**：任务面板通过调用game包中的相应类实现业务逻辑
   ```java
   // 任务面板中调用Game模块的代码示例
   private final AreaCalculation areaCalculation = new AreaCalculation();
   // 在处理提交时验证答案
   boolean correct = areaCalculation.checkAnswer(input);
   ```

这种设计实现了UI层和业务逻辑层的分离，提高了代码的可维护性和可测试性。

## 扩展指南

如果需要扩展GUI模块，可以遵循以下步骤：

1. **添加新任务**：
   - 在UIManager中注册新任务名称和描述
   - 在Tasks包中创建对应的任务面板类
   - 更新TaskWindow中的loadTaskPanel方法以支持新任务

2. **修改UI样式**：
   - 所有窗口都使用标准的Swing组件，可以通过修改样式相关代码自定义外观
   - 主要样式定义集中在各个窗口类的createStyledButton等方法中

3. **添加新窗口**：
   - 创建新的窗口类，继承JFrame
   - 在UIManager中添加对应的显示和管理方法
   - 更新导航逻辑以支持新窗口

遵循现有的设计模式和命名约定，可以确保新功能与现有系统无缝集成。 