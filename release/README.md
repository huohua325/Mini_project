# Shapeville - 几何学习乐园

一个基于Java Swing的几何图形计算与识别应用程序，旨在通过互动教学帮助用户学习几何知识。

## 目录
- [Shapeville - 几何学习乐园](#shapeville---几何学习乐园)
	- [目录](#目录)
	- [项目描述](#项目描述)
	- [功能特点](#功能特点)
	- [技术栈](#技术栈)
	- [系统要求](#系统要求)
	- [安装与运行](#安装与运行)
		- [方法一：使用Maven](#方法一使用maven)
		- [方法二：直接运行](#方法二直接运行)
		- [方法三：使用IDE](#方法三使用ide)
	- [项目结构](#项目结构)
	- [详细模块说明](#详细模块说明)
		- [Main 模块](#main-模块)
		- [GUI 模块](#gui-模块)
			- [核心窗口类](#核心窗口类)
			- [任务面板类](#任务面板类)
		- [Game 模块](#game-模块)
		- [Model 模块](#model-模块)
		- [Utils 模块](#utils-模块)
	- [工作流程与数据流向](#工作流程与数据流向)
		- [1. 垂直数据流](#1-垂直数据流)
		- [2. 水平数据流](#2-水平数据流)
		- [3. 关键数据交互点](#3-关键数据交互点)
		- [4. 窗口与任务面板的协作关系](#4-窗口与任务面板的协作关系)
	- [学习计划与使用方法](#学习计划与使用方法)
		- [学习建议](#学习建议)
		- [使用方法](#使用方法)
	- [开发指南](#开发指南)
		- [开发环境设置](#开发环境设置)
		- [扩展项目](#扩展项目)
		- [如何贡献](#如何贡献)
	- [许可证](#许可证)
	- [联系方式](#联系方式)

## 项目描述

Shapeville是一个交互式的几何学习应用程序，使用Java Swing构建用户界面。该应用程序融合了游戏性和教育性，通过一系列由简到难的任务，帮助用户掌握各种几何概念和计算方法。

从基本的角度识别、形状识别，到复杂的面积计算和扇形计算，Shapeville为用户提供了全面、系统的几何学习体验。应用程序记录用户的学习进度，根据完成情况解锁新的内容，并提供即时反馈和评分，帮助用户了解自己的掌握程度。

Shapeville不仅是一个学习工具，也是一个挑战自我的平台，通过星级评价和等级提升，激励用户不断精进自己的几何知识和计算能力。

## 功能特点

项目提供了丰富多样的几何学习任务，包括：

- **形状识别**：认识和辨别各种2D和3D几何形状
- **角度计算**：学习辨别和测量不同类型的角度（锐角、直角、钝角、平角、优角）
- **面积计算**：掌握计算基本几何形状面积的方法
- **圆形计算**：学习圆的周长、面积计算
- **复合图形计算**：计算由多个基本形状组成的复杂图形的面积
- **扇形计算**：学习和应用扇形的面积、弧长计算公式

系统功能特点：

- **渐进式学习体系**：任务按难度分级，从基础到高级
- **即时反馈机制**：每道题目均提供即时评价和正确答案
- **进度追踪**：记录用户的学习进度和成绩
- **等级系统**：根据完成任务的数量和得分评定用户等级
- **解锁机制**：完成基础任务后解锁高级任务
- **成绩评定**：通过星级和百分比评价用户表现
- **数据持久化**：保存用户进度和成绩
- **直观的图形界面**：采用现代化UI设计，操作简单直观

## 技术栈

- **Java 17**：核心编程语言
- **Swing**：GUI框架，用于构建用户界面
- **Maven**：项目管理和构建工具
- **JUnit 5**：单元测试框架
- **Gson**：JSON数据处理库，用于数据持久化

## 系统要求

- **JDK 17** 或更高版本
- **Maven 3.6.x** 或更高版本
- **内存**：至少512MB可用RAM
- **存储**：至少100MB可用磁盘空间
- **显示**：分辨率不低于1024x768的显示器

## 安装与运行

### 方法一：使用Maven

1. 克隆项目到本地：
   ```bash
   git clone https://github.com/yourusername/shapeville.git
   cd shapeville
   ```

2. 使用Maven编译项目：
```bash
mvn clean package
```

3. 运行编译好的JAR文件：
   ```bash
   java -jar target/shapeville-1.0-SNAPSHOT.jar
   ```

### 方法二：直接运行

1. 确保安装了Java 17或更高版本：
   ```bash
   java -version
   ```

2. 使用Maven执行：
```bash
mvn exec:java -Dexec.mainClass="com.shapeville.Main"
```

### 方法三：使用IDE

1. 在IDE中导入项目（推荐使用IntelliJ IDEA或Eclipse）
2. 确保配置了JDK 17
3. 运行`com.shapeville.Main`类的`main`方法

## 项目结构

```
shapeville/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── shapeville/
│       │           ├── Main.java                 # 程序入口
│       │           ├── core/                     # 核心业务逻辑（待实现）
│       │           ├── game/                     # 游戏逻辑模块
│       │           │   ├── AngleCalculation.java          # 角度计算逻辑
│       │           │   ├── AreaCalculation.java           # 面积计算逻辑
│       │           │   ├── CircleCalculation.java         # 圆形计算逻辑
│       │           │   ├── CompoundShapeCalculation.java  # 复合形状计算
│       │           │   ├── SectorCalculation.java         # 扇形计算逻辑
│       │           │   └── ShapeRecognition.java          # 形状识别逻辑
│       │           ├── gui/                     # 用户界面模块
│       │           │   ├── MainWindow.java              # 主窗口
│       │           │   ├── TaskWindow.java              # 任务窗口
│       │           │   ├── ResultWindow.java            # 结果窗口
│       │           │   ├── UIManager.java               # UI管理器
│       │           │   └── tasks/                       # 任务面板
│       │           │       ├── AngleCalculationPanel.java       # 角度计算面板
│       │           │       ├── AreaCalculationPanel.java        # 面积计算面板
│       │           │       ├── BaseTaskPanel.java               # 任务面板基类
│       │           │       ├── CompoundShapeCalculationPanel.java # 复合形状面板
│       │           │       ├── SectorCalculationPanel.java      # 扇形计算面板
│       │           │       ├── ShapePanel.java                  # 形状面板
│       │           │       └── TaskPanelInterface.java          # 任务面板接口
│       │           ├── model/                   # 数据模型
│       │           │   ├── Shape2D.java                # 2D形状枚举
│       │           │   ├── Shape3D.java                # 3D形状枚举
│       │           │   └── UserProgress.java           # 用户进度模型
│       │           └── utils/                   # 工具类
│       │               ├── DataManager.java            # 数据管理工具
│       │               └── Utils.java                  # 通用工具方法
│       └── resources/                # 资源文件
│           └── data/                 # 数据文件
├── pom.xml                  # Maven项目配置
└── README.md                # 项目说明文档
```

## 详细模块说明

### Main 模块

`Main.java` 是应用程序的入口点，负责初始化图形界面并启动应用程序。主要功能：

- 设置系统外观为本地系统外观
- 通过`UIManager`初始化并显示图形界面
- 在EDT线程中运行UI代码，确保UI线程安全

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            // 设置系统外观
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 初始化并显示GUI
        UIManager.getInstance().initialize();
    });
}
```

### GUI 模块

GUI模块负责所有用户界面相关的功能，包括窗口显示、用户交互和界面管理。

#### 核心窗口类

- **MainWindow.java**
  - 应用程序的主窗口，作为用户的主入口点
  - 显示所有可用任务列表（基础和高级）
  - 显示用户当前等级和总体进度
  - 管理任务按钮的状态（锁定、解锁、进行中、已完成）
  - 跟踪任务完成状态并更新进度条
  - 提供返回主页和结束会话的功能

- **TaskWindow.java**
  - 任务执行窗口，负责加载和显示具体任务内容
  - 为每种任务（如形状识别、角度计算等）提供适当的界面和交互元素
  - 包含任务说明、计时器、反馈区域等通用组件
  - 管理任务的生命周期：开始、执行、完成
  - 计算任务得分并生成反馈
  - 任务完成后调用UIManager显示结果窗口

- **ResultWindow.java**
  - 任务结果显示窗口
  - 使用动画效果显示用户得分
  - 根据得分显示星级评价（1-5星）
  - 显示详细的任务反馈和成绩分析
  - 提供"继续学习"、"重新尝试"和"结束学习"选项
  - 根据得分显示不同颜色的进度条和适当的表现评级

- **UIManager.java**
  - 单例模式实现的UI管理器
  - 负责窗口间的切换和协调
  - 管理所有任务的状态、分数和解锁条件
  - 处理任务完成时的结果显示和记录
  - 计算用户等级和总体进度
  - 提供任务描述和状态更新
  - 实现任务解锁逻辑（基础任务完成后解锁高级任务）

#### 任务面板类

`tasks`文件夹包含所有具体任务的面板实现，每个任务面板负责特定类型的任务交互：

- **BaseTaskPanel.java**
  - 所有任务面板的基类
  - 实现通用功能：尝试次数管理、反馈显示、得分计算等
  - 提供通用UI组件和布局结构
  - 定义任务面板的生命周期方法

- **TaskPanelInterface.java**
  - 定义任务面板必须实现的接口方法
  - 包括：初始化UI、处理提交、重置、开始任务、暂停任务、恢复任务、结束任务、获取分数和反馈等

- **AngleCalculationPanel.java**
  - 实现"角度识别"任务面板
  - 随机生成10°~350°的角度（10的倍数）并图形化显示
  - 要求用户判断角度类型（锐角、直角、钝角、平角、优角）
  - 支持多轮答题，每轮有多次尝试机会
  - 计算得分并提供即时反馈
  - 实现任务重置、暂停、恢复等功能

- **AreaCalculationPanel.java**
  - 实现"面积计算"任务面板
  - 显示各种几何形状（矩形、三角形等）并提供尺寸
  - 要求用户计算形状面积并输入答案
  - 检查答案正确性并提供反馈
  - 支持公式提示和多次尝试

- **CompoundShapeCalculationPanel.java**
  - 实现"复合形状"任务面板
  - 展示由多个基本形状组成的复杂图形
  - 要求用户计算总面积
  - 提供分步计算指导和反馈
  - 包含更高难度的几何计算挑战

- **SectorCalculationPanel.java**
  - 实现"扇形计算"任务面板
  - 显示不同角度和半径的扇形
  - 要求用户计算扇形面积或弧长
  - 提供相关公式和计算步骤指导
  - 支持多轮练习和评分

- **ShapePanel.java**
  - 实现"形状识别"任务面板
  - 显示各种2D和3D几何形状
  - 要求用户识别形状并输入正确的英文名称
  - 支持2D和3D模式切换
  - 随机从形状库中选择形状进行测试

### Game 模块

game模块包含各种几何任务的核心业务逻辑，与GUI层分离，专注于数据处理和计算逻辑：

- **AngleCalculation.java**
  - 封装角度识别任务的核心逻辑
  - 生成随机角度并判定类型
  - 维护角度类型池和已识别类型集合
  - 提供答案验证和进度跟踪
  - 包含独立的命令行交互模式（用于测试）

- **AreaCalculation.java**
  - 实现各种基本几何形状的面积计算逻辑
  - 生成随机形状及其参数
  - 计算准确答案和允许误差范围
  - 验证用户答案的正确性
  - 支持多种形状：矩形、三角形、平行四边形等

- **CircleCalculation.java**
  - 专注于圆形相关计算
  - 计算圆的周长、面积
  - 生成随机半径值用于测试
  - 验证用户计算结果
  - 提供圆形计算的公式和参考

- **CompoundShapeCalculation.java**
  - 处理复合形状的计算逻辑
  - 生成由基本形状组合而成的复杂图形
  - 计算复合形状的总面积
  - 分解复杂问题为基础计算步骤
  - 验证用户的分步计算和最终结果

- **SectorCalculation.java**
  - 专注于扇形计算
  - 计算扇形面积、弧长
  - 随机生成角度和半径值
  - 验证用户答案的正确性
  - 提供扇形各部分计算的公式

- **ShapeRecognition.java**
  - 实现形状识别逻辑
  - 管理2D和3D形状库
  - 随机选择测试形状
  - 验证用户识别结果
  - 跟踪识别正确率和尝试次数

### Model 模块

model模块定义了应用程序使用的数据模型和枚举类型：

- **Shape2D.java**
  - 二维形状枚举
  - 包含英文名称和中文名称
  - 支持的形状包括：圆形、方形、矩形、三角形、平行四边形等
  - 提供形状名称的双语显示

```java
public enum Shape2D {
    CIRCLE("circle", "圆形"),
    SQUARE("square", "正方形"),
    RECTANGLE("rectangle", "矩形"),
    TRIANGLE("triangle", "三角形"),
    PARALLELOGRAM("parallelogram", "平行四边形"),
    TRAPEZOID("trapezoid", "梯形"),
    PENTAGON("pentagon", "五边形"),
    HEXAGON("hexagon", "六边形"),
    RHOMBUS("rhombus", "菱形"),
    OVAL("oval", "椭圆");
    
    private final String english;
    private final String chinese;
    
    // ...getter方法
}
```

- **Shape3D.java**
  - 三维形状枚举
  - 包含英文名称和中文名称
  - 支持的形状包括：立方体、球体、圆锥、圆柱、棱锥等
  - 提供形状名称的双语显示

- **UserProgress.java**
  - 用户进度数据模型
  - 记录用户完成的任务和得分
  - 跟踪解锁的高级任务
  - 存储用户当前等级和总体进度
  - 支持进度数据的序列化和反序列化

### Utils 模块

utils模块提供通用工具类和数据管理功能：

- **DataManager.java**
  - 负责用户数据的持久化管理
  - 使用JSON格式保存和加载用户进度
  - 提供文件读写操作
  - 处理数据保存和恢复异常
  - 确保用户进度不会丢失

- **Utils.java**
  - 提供各种通用工具方法
  - 数学计算和格式化函数
  - 字符串处理工具
  - UI辅助方法
  - 通用验证函数

## 工作流程与数据流向

项目采用分层架构，数据流向清晰：

### 1. 垂直数据流

- **自上而下**：
  ```
  Main → UIManager → 具体窗口 → 业务逻辑 → 数据模型
  ```
  - 用户交互从UI层开始
  - 通过UIManager进行统一管理
  - 传递到具体的业务逻辑层
  - 最终影响数据模型

- **自下而上**：
  ```
  数据模型 → 业务逻辑 → 具体窗口 → UIManager → Main
  ```
  - 数据模型的更新
  - 触发业务逻辑处理
  - 通过UI层展示结果
  - 最终反馈给用户

### 2. 水平数据流

- **核心计算流程**：
  ```
  用户输入 → 数据验证 → 计算处理 → 结果展示
  ```
  - 用户通过GUI输入数据
  - 系统进行数据验证
  - 调用game模块进行计算
  - 在ResultWindow展示结果

- **进度跟踪流程**：
  ```
  用户操作 → UserProgress更新 → 数据持久化 → 界面更新
  ```
  - 记录用户操作
  - 更新进度数据
  - 通过DataManager持久化
  - 更新UI显示

### 3. 关键数据交互点

- **UI与业务逻辑交互**：
  - TaskWindow与具体计算模块的交互
  - ResultWindow与数据模型的交互
  - MainWindow与UIManager的交互

- **数据持久化**：
  - UserProgress与DataManager的交互
  - 计算结果的保存与读取
  - 用户配置的存储

### 4. 窗口与任务面板的协作关系

- `MainWindow` 是用户的主入口，负责任务选择和进度展示。用户点击任务按钮后，`UIManager` 会根据任务解锁状态切换到对应的 `TaskWindow`。
- `TaskWindow` 负责具体任务的执行，实际的交互和判题逻辑由 `tasks` 文件夹下的面板类（如 `AngleCalculationPanel` 等）实现。任务完成后，`TaskWindow` 会调用 `UIManager.showResult` 弹出 `ResultWindow` 展示成绩和反馈。
- `ResultWindow` 展示任务结果，并可引导用户返回主界面或重新挑战任务。其所有操作均通过 `UIManager` 进行窗口切换和状态更新。
- `UIManager` 作为全局调度者，维护所有任务的状态、分数、解锁逻辑和等级评定，负责窗口的创建、切换和销毁。所有窗口的切换和数据流转都通过 `UIManager` 实现。
- `tasks` 文件夹下的每个任务面板类，专注于实现某一具体任务的交互和业务逻辑，所有任务面板都在 `TaskWindow` 中被调用和管理，形成"主窗口-任务窗口-结果窗口"完整的学习闭环。

## 学习计划与使用方法

### 学习建议

1. **循序渐进**：先完成所有基础任务，再挑战高级任务
2. **重复练习**：对于难度较大的任务，可多次尝试以熟悉概念和计算方法
3. **关注反馈**：任务完成后认真查看反馈和成绩分析，找出不足之处
4. **全面学习**：尝试所有类型的任务，全面提升几何能力

### 使用方法

1. **启动应用**：运行程序后，将显示主窗口，展示所有可用任务
2. **选择任务**：点击任务按钮开始特定任务
   - 基础任务默认解锁
   - 高级任务需完成所有基础任务并获得至少70分才能解锁
3. **执行任务**：
   - 阅读任务说明
   - 根据提示完成任务要求
   - 提交答案获取反馈
   - 任务完成后查看结果分析
4. **查看结果**：任务完成后会显示:
   - 得分和星级评价
   - 详细的任务反馈
   - 成绩分析和表现评级
5. **继续学习**：根据结果选择:
   - 返回主界面选择新任务
   - 重新尝试当前任务
   - 结束学习会话

## 开发指南

本项目使用Maven进行依赖管理，主要依赖包括：
- JUnit Jupiter 5.8.2 (测试框架)
- Gson 2.10.1 (JSON处理)

### 开发环境设置

1. 确保安装了JDK 17和Maven
2. 克隆仓库到本地
3. 使用IDE导入项目（推荐IntelliJ IDEA或Eclipse）
4. 确保Maven正确加载所有依赖

### 扩展项目

如果需要添加新的任务类型，需要：

1. 在`game`包中创建新的业务逻辑类
2. 在`gui.tasks`包中创建对应的任务面板类
3. 在`UIManager`中注册新任务
4. 在`MainWindow`中添加新任务按钮
5. 更新任务解锁逻辑

### 如何贡献

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 许可证

[MIT License](LICENSE)

## 联系方式

项目维护者: [您的名字]
Email: [您的邮箱]
GitHub: [您的GitHub主页]
