# Shapeville

一个基于Java Swing的图形界面应用程序。

## 项目描述
Shapeville是一个Java桌面应用程序，使用Swing框架构建用户界面，提供现代化的系统外观。

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
│       └── java/
│           └── com/
│               └── shapeville/
│                   ├── Main.java
│                   └── gui/
│                       └── UIManager.java
├── pom.xml
└── README.md
```

## 开发
本项目使用Maven进行依赖管理，主要依赖包括：
- JUnit Jupiter 5.8.2 (测试框架)
- Gson 2.10.1 (JSON处理)

## 许可证
[待添加]

## 贡献
欢迎提交Issue和Pull Request。
