# Shapeville

一个基于Java Swing的几何图形计算与识别应用程序。

## 项目描述
Shapeville是一个Java桌面应用程序，使用Swing框架构建用户界面。该应用程序提供了多种几何图形的计算功能，包括角度计算、面积计算、圆形计算、复合图形计算等，并具有现代化的系统外观。

## 功能特点
- 角度计算（Angle Calculation）
- 面积计算（Area Calculation）
- 圆形计算（Circle Calculation）
- 复合图形计算（Compound Shape Calculation）
- 扇形计算（Sector Calculation）
- 图形识别（Shape Recognition）
- 支持2D和3D图形
- 用户进度跟踪
- 直观的图形用户界面

## 技术栈
- Java 17
- Swing (GUI框架)
- Maven (项目管理)
- JUnit 5 (单元测试)
- Gson (JSON处理)

## 系统要求
- JDK 17 或更高版本
- Maven 3.6.x 或更高版本

## 构建和运行

### 构建项目
```bash
mvn clean package
```

### 运行项目
```bash
mvn exec:java -Dexec.mainClass="com.shapeville.Main"
```

或者直接运行生成的JAR文件：
```bash
java -jar target/shapeville-1.0-SNAPSHOT.jar
```

## 项目结构
```
shapeville/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── shapeville/
│       │           ├── core/
│       │           │   ├── AngleCalculation.java
│       │           │   ├── AreaCalculation.java
│       │           │   ├── CircleCalculation.java
│       │           │   ├── CompoundShapeCalculation.java
│       │           │   ├── SectorCalculation.java
│       │           │   └── ShapeRecognition.java
│       │           ├── gui/
│       │           │   ├── tasks/
│       │           │   │   ├── AngleCalculationPanel.java
│       │           │   │   ├── AreaCalculationPanel.java
│       │           │   │   ├── BaseTaskPanel.java
│       │           │   │   ├── CompoundShapeCalculationPanel.java
│       │           │   │   ├── SectorCalculationPanel.java
│       │           │   │   ├── ShapePanel.java
│       │           │   │   └── TaskPanelInterface.java
│       │           │   ├── MainWindow.java
│       │           │   ├── ResultWindow.java
│       │           │   ├── TaskWindow.java
│       │           │   └── UIManager.java
│       │           ├── model/
│       │           │   ├── Shape2D.java
│       │           │   ├── Shape3D.java
│       │           │   └── UserProgress.java
│       │           ├── utils/
│       │           │   ├── DataManager.java
│       │           │   └── Utils.java
│       │           └── Main.java
│       └── resources/
│           └── data/
├── pom.xml
└── README.md
```

## 模块说明

### core 模块
包含所有核心计算逻辑，负责处理各种几何图形的计算。

### gui 模块
- `tasks`: 包含所有具体任务的面板实现
- `MainWindow.java`: 主程序窗口
- `ResultWindow.java`: 结果显示窗口
- `TaskWindow.java`: 任务窗口
- `UIManager.java`: UI管理器

### model 模块
包含数据模型定义，如2D图形、3D图形和用户进度跟踪。

### utils 模块
提供数据管理和通用工具类。

## 开发
本项目使用Maven进行依赖管理，主要依赖包括：
- JUnit Jupiter 5.8.2 (测试框架)
- Gson 2.10.1 (JSON处理)

## 如何贡献
1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 许可证
[MIT License](LICENSE)

## 作者
[您的名字]

## 致谢
- 感谢所有贡献者的付出
- 感谢Java Swing框架提供的GUI支持
