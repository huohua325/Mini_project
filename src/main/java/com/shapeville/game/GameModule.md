# ShapeVille 游戏模块详细文档

## 目录
- [ShapeVille 游戏模块详细文档](#shapeville-游戏模块详细文档)
	- [目录](#目录)
	- [概述](#概述)
	- [模块结构](#模块结构)
	- [模块间关系](#模块间关系)
	- [详细实现](#详细实现)
		- [1. 形状识别模块 (ShapeRecognition)](#1-形状识别模块-shaperecognition)
			- [核心数据结构](#核心数据结构)
			- [关键算法与流程](#关键算法与流程)
			- [命令行交互示例](#命令行交互示例)
			- [与GUI层交互](#与gui层交互)
		- [2. 角度计算模块 (AngleCalculation)](#2-角度计算模块-anglecalculation)
			- [核心数据结构](#核心数据结构-1)
			- [关键算法与流程](#关键算法与流程-1)
			- [特殊设计考量](#特殊设计考量)
			- [命令行交互示例](#命令行交互示例-1)
		- [3. 面积计算模块 (AreaCalculation)](#3-面积计算模块-areacalculation)
			- [核心数据结构](#核心数据结构-2)
			- [关键算法与流程](#关键算法与流程-2)
			- [特殊设计考量](#特殊设计考量-1)
			- [与其他模块的区别](#与其他模块的区别)
		- [4. 圆形计算模块 (CircleCalculation)](#4-圆形计算模块-circlecalculation)
			- [核心数据结构](#核心数据结构-3)
			- [关键算法与流程](#关键算法与流程-3)
			- [练习内容多样性](#练习内容多样性)
			- [特殊设计考量](#特殊设计考量-2)
			- [与其他模块的区别](#与其他模块的区别-1)
		- [5. 复合图形计算模块 (CompoundShapeCalculation)](#5-复合图形计算模块-compoundshapecalculation)
			- [核心数据结构](#核心数据结构-4)
			- [预定义的复合形状](#预定义的复合形状)
			- [关键算法与流程](#关键算法与流程-4)
			- [特殊设计考量](#特殊设计考量-3)
			- [与其他模块的区别](#与其他模块的区别-2)
		- [6. 扇形计算模块 (SectorCalculation)](#6-扇形计算模块-sectorcalculation)
			- [核心数据结构](#核心数据结构-5)
			- [预定义的扇形](#预定义的扇形)
			- [关键算法与流程](#关键算法与流程-5)
			- [特殊设计考量](#特殊设计考量-4)
			- [与其他模块的区别](#与其他模块的区别-3)
	- [共通功能与设计模式](#共通功能与设计模式)
		- [1. 命令行交互模式](#1-命令行交互模式)
		- [2. 尝试次数管理](#2-尝试次数管理)
		- [3. 答案验证策略](#3-答案验证策略)
		- [4. 进度跟踪机制](#4-进度跟踪机制)
	- [与GUI模块交互](#与gui模块交互)
	- [测试与调试](#测试与调试)
	- [扩展指南](#扩展指南)

## 概述

`com.shapeville.game` 包是Shapeville应用的核心业务逻辑层，负责处理所有与几何计算、形状识别和数学练习相关的功能。该模块与GUI层解耦，既可以独立运行（通过命令行接口），也可以被GUI层调用，提供灵活的使用方式。

每个子模块都专注于特定的几何或数学概念，遵循相似的设计模式和交互流程，便于维护和扩展。

## 模块结构

`com.shapeville.game` 包包含以下主要类：

1. `ShapeRecognition`: 形状识别游戏
2. `AngleCalculation`: 角度计算游戏
3. `AreaCalculation`: 面积计算游戏
4. `CircleCalculation`: 圆形计算游戏
5. `CompoundShapeCalculation`: 复合图形计算游戏
6. `SectorCalculation`: 扇形计算游戏

## 模块间关系

虽然各模块在功能上相互独立，但它们共享以下设计特点：

1. **一致的接口模式**：所有模块提供类似的方法签名，如初始化、答案验证、任务完成判断等
2. **统一的状态管理**：都包含状态跟踪机制（如已完成任务的记录）
3. **独立的命令行入口**：每个模块都有一个以`start+模块名`命名的方法，用于命令行模式
4. **尝试次数记录**：所有模块都返回`List<Integer>`，记录每个任务的尝试次数，用于分数计算
5. **多轮练习支持**：支持多个问题或任务的连续练习，直到达到完成条件

## 详细实现

### 1. 形状识别模块 (ShapeRecognition)

`ShapeRecognition`类帮助用户学习2D和3D形状的英文名称，是形状识别与语言学习的结合。

#### 核心数据结构
```java
private List<Shape2D> shapes2D;  // 存储2D形状列表
private List<Shape3D> shapes3D;  // 存储3D形状列表
private int correctCount2D;      // 正确识别的2D形状数量
private int correctCount3D;      // 正确识别的3D形状数量
```

#### 关键算法与流程
1. **初始化**：
   ```java
   public void initializeShapes() {
       shapes2D = new ArrayList<>(Arrays.asList(Shape2D.values()));
       shapes3D = new ArrayList<>(Arrays.asList(Shape3D.values()));
       Collections.shuffle(shapes2D);  // 随机打乱顺序，增加练习多样性
       Collections.shuffle(shapes3D);
       correctCount2D = 0;
       correctCount3D = 0;
   }
   ```

2. **答案验证**：
   ```java
   public boolean check2DAnswer(Shape2D shape, String answer) {
       return answer.equals(shape.getEnglish());
   }
   
   public boolean check3DAnswer(Shape3D shape, String answer) {
       return answer.equals(shape.getEnglish());
   }
   ```

3. **任务完成判断**：
   ```java
   public boolean is2DComplete() {
       return correctCount2D >= 4;  // 至少正确识别4个形状
   }
   
   public boolean is3DComplete() {
       return correctCount3D >= 4;
   }
   ```

#### 命令行交互示例
```
请识别这个2D形状（英文名）: 圆形
circle
回答正确！

请识别这个2D形状（英文名）: 正方形
square
回答正确！
```

#### 与GUI层交互
- GUI层通过`getShapes2D()`和`getShapes3D()`获取形状列表
- 通过`check2DAnswer()`和`check3DAnswer()`验证用户输入
- 通过`is2DComplete()`和`is3DComplete()`判断任务是否完成

### 2. 角度计算模块 (AngleCalculation)

`AngleCalculation`类用于帮助用户理解和识别不同类型的角度，掌握锐角、直角、钝角、平角和优角的概念。

#### 核心数据结构
```java
private final List<Integer> angles;  // 存储角度值列表
private final Set<String> identifiedTypes;  // 存储已识别的角度类型
private static final String[] ANGLE_TYPES = {"acute", "right", "obtuse", "straight", "reflex"};
```

#### 关键算法与流程
1. **角度生成**：
   ```java
   public void initializeAngles() {
       angles.clear();
       // 只用10的倍数，排除0和360
       for (int i = 10; i < 360; i += 10) {
           angles.add(i);
       }
       Collections.shuffle(angles);  // 随机打乱角度顺序
   }
   ```

2. **角度类型判定**：
   ```java
   public String getAngleType(int angle) {
       if (angle == 90) return "right";
       if (angle > 0 && angle < 90) return "acute";
       if (angle > 90 && angle < 180) return "obtuse";
       if (angle == 180) return "straight";
       if (angle > 180 && angle < 360) return "reflex";
       return "unknown";
   }
   ```

3. **任务完成判断**：
   ```java
   public boolean isTaskComplete() {
       return identifiedTypes.size() >= 5 || angles.isEmpty();
   }
   ```

#### 特殊设计考量
1. 该模块特别注重**类型覆盖性**，要求用户识别5种不同的角度类型，而不仅仅是多个角度
2. 采用**集合去重**技术，确保每种角度类型只需要正确识别一次
3. 使用**轮询机制**，如果随机抽取的角度类型已被识别，则跳过继续抽取

#### 命令行交互示例
```
请输入该角度的类型（英文）： 45°
可选类型：acute, right, obtuse, straight, reflex
acute
回答正确！
```

### 3. 面积计算模块 (AreaCalculation)

`AreaCalculation`类专注于基本几何形状的面积计算，强化用户对几何公式的理解和应用。

#### 核心数据结构
```java
public enum ShapeType {
    RECTANGLE("矩形"), 
    PARALLELOGRAM("平行四边形"), 
    TRIANGLE("三角形"), 
    TRAPEZIUM("梯形");
    // ...
}

private final List<ShapeType> shapes;  // 形状类型列表
private final Map<String, Double> currentParams;  // 当前形状的参数
private double correctArea;  // 正确的面积值
```

#### 关键算法与流程
1. **参数生成**：
   ```java
   public void generateParams(ShapeType shape) {
       currentParams.clear();
       switch (shape) {
           case RECTANGLE:
               currentParams.put("长", (double)(1 + random.nextInt(20)));
               currentParams.put("宽", (double)(1 + random.nextInt(20)));
               correctArea = currentParams.get("长") * currentParams.get("宽");
               break;
           case PARALLELOGRAM:
               // ...类似的参数生成逻辑
               break;
           // ...其他形状
       }
   }
   ```

2. **公式提供**：
   ```java
   public String getFormula(ShapeType shape) {
       switch (shape) {
           case RECTANGLE: return "A = 长 × 宽";
           case PARALLELOGRAM: return "A = 底 × 高";
           case TRIANGLE: return "A = 1/2 × 底 × 高";
           case TRAPEZIUM: return "A = (上底 + 下底) × 高 ÷ 2";
           default: return "";
       }
   }
   ```

3. **答案验证**：
   ```java
   public boolean checkAnswer(double answer) {
       return Math.abs(answer - correctArea) < 0.1;  // 允许0.1的误差
   }
   ```

#### 特殊设计考量
1. **参数名称本地化**：使用中文参数名称（如"长"、"宽"），提升用户体验
2. **完成条件灵活化**：要求用户尝试所有4种形状，或允许自己选择退出
3. **误差容忍**：计算结果允许0.1的误差，考虑到浮点数计算的特性和用户输入习惯

#### 与其他模块的区别
- 此模块使用**枚举+字典**组合表示参数，而不是简单的数值列表
- 提供完整的**公式引导**，更注重教学性质

### 4. 圆形计算模块 (CircleCalculation)

`CircleCalculation`类专注于圆的周长和面积计算，是几何计算中的基础且重要部分。

#### 核心数据结构
```java
private enum PracticeType {
    RADIUS_AREA,     // 已知半径求面积
    RADIUS_CIRCUM,   // 已知半径求周长
    DIAMETER_AREA,   // 已知直径求面积
    DIAMETER_CIRCUM  // 已知直径求周长
}

private final Set<PracticeType> practiced;  // 已练习的类型
```

#### 关键算法与流程
- **圆的面积计算**：`area = Math.PI * r * r;`
- **圆的周长计算**：`circumference = 2 * Math.PI * r;`
- **直径转半径**：`r = d / 2.0;`

#### 练习内容多样性
模块提供4种不同的练习类型：
1. 已知半径求面积：`A = πr²`
2. 已知半径求周长：`C = 2πr`
3. 已知直径求面积：`A = π(d/2)²`
4. 已知直径求周长：`C = πd`

#### 特殊设计考量
1. **随机参数**：半径和直径值在1-20的范围内随机生成
2. **记录已练习类型**：确保用户尝试所有4种不同的计算类型
3. **使用Math.PI**：使用Java内置的π值，提高计算精度

#### 与其他模块的区别
- 此模块没有使用字段存储状态，而是完全依赖于方法内的局部变量
- 使用**枚举集合**（Set<PracticeType>）跟踪已完成的练习类型
- 更强调**参数变换**（半径/直径）和**计算目标变换**（面积/周长）

### 5. 复合图形计算模块 (CompoundShapeCalculation)

`CompoundShapeCalculation`类处理更复杂的几何问题，要求用户将复合图形分解并综合计算，是高级几何思维的训练。

#### 核心数据结构
```java
public static class CompoundShape {
    private final String name;
    private final String description;
    private final double correctArea;
    private final String solution;
    // ... 构造函数和getter方法
}

private final List<CompoundShape> shapes;  // 预定义的复合形状列表
private final Set<Integer> practiced;      // 已练习的形状索引
```

#### 预定义的复合形状
模块内置了4种复合形状，每种都有详细描述和解法：
1. 矩形(14x15)和三角形(底14,高5)的组合
2. 正方形(边长10)和等腰三角形(底10,高8)的组合
3. 由两个相等的矩形(8x12)组成的L形
4. 圆形(半径6)和正方形(边长12)的组合

#### 关键算法与流程
1. **形状初始化**：
   ```java
   private List<CompoundShape> initializeShapes() {
       return Arrays.asList(
           new CompoundShape(
               "复合形状1",
               "由一个矩形(14x15)和一个三角形(底14,高5)组成",
               14*15 + 0.5*14*5,  // 计算正确面积
               "矩形面积: 14x15=210, 三角形面积: 0.5x14x5=35, 总面积: 210+35=245"
           ),
           // ...其他形状
       );
   }
   ```

2. **答案验证**：
   ```java
   public boolean checkAnswer(int shapeIndex, double answer) {
       if (shapeIndex < 0 || shapeIndex >= shapes.size()) {
           return false;
       }
       return Math.abs(answer - shapes.get(shapeIndex).getCorrectArea()) < 0.1;
   }
   ```

3. **练习完成判断**：
   ```java
   public boolean isComplete() {
       return practiced.size() >= shapes.size();
   }
   ```

#### 特殊设计考量
1. **内部类封装**：使用静态内部类`CompoundShape`封装复合形状信息
2. **详细解题过程**：每个形状包含完整的解题步骤，便于学习参考
3. **用户选择机制**：允许用户选择要练习的具体形状，增加交互灵活性

#### 与其他模块的区别
- 使用**静态内部类**封装数据，而不是简单的数据结构
- 提供**分步计算过程**，更注重思维训练
- 所有数据都是**预定义**的，没有随机生成元素

### 6. 扇形计算模块 (SectorCalculation)

`SectorCalculation`类专注于扇形面积计算，是圆形计算的扩展，涉及角度转换和比例计算。

#### 核心数据结构
```java
public static class Sector {
    private final String name;
    private final double radius;
    private final double angle;
    private final double correctArea;
    private final String solution;
    // ... 构造函数和getter方法
}

private final List<Sector> sectors;  // 预定义的扇形列表
private final Set<Integer> practiced;  // 已练习的扇形索引
```

#### 预定义的扇形
模块预定义了4种不同的扇形：
1. 半径8，圆心角90°
2. 半径6，圆心角120°
3. 半径10，圆心角60°
4. 半径5，圆心角180°

#### 关键算法与流程
1. **扇形面积计算公式**：
   ```
   扇形面积 = (π × r² × x) / 360
   ```
   其中r是半径，x是圆心角（度）

2. **扇形初始化**：
   ```java
   private List<Sector> initializeSectors() {
       return Arrays.asList(
           new Sector("扇形1", 8, 90, Math.PI * 8 * 8 * 90 / 360, 
                     "A = π × r² × x/360 = 3.14×8²×90/360 = 50.24"),
           // ...其他扇形
       );
   }
   ```

3. **答案验证**：
   ```java
   public boolean checkAnswer(int sectorIndex, double answer) {
       if (sectorIndex < 0 || sectorIndex >= sectors.size()) {
           return false;
       }
       return Math.abs(answer - sectors.get(sectorIndex).getCorrectArea()) < 0.05;
   }
   ```

#### 特殊设计考量
1. **更高精度要求**：误差容忍从0.1降低到0.05，提高计算精确度要求
2. **圆心角概念引入**：加入角度概念，拓展圆形计算的思维
3. **结果保留两位小数**：强调在实际应用中的精确度

#### 与其他模块的区别
- 误差容忍度**更严格**（0.05 vs 0.1）
- 使用**圆周率近似值3.14**进行演示计算
- 结果精确到**两位小数**，而其他模块通常是一位小数

## 共通功能与设计模式

### 1. 命令行交互模式
所有模块都实现了标准化的命令行交互接口：
- 以`start+模块名`命名的方法作为入口
- 返回`List<Integer>`记录每个任务的尝试次数
- 支持多轮练习和中途退出
- 提供清晰的提示和输入指导

### 2. 尝试次数管理
每个模块都实现了尝试次数限制（通常为3次）：
```java
int attempts = 0;
boolean correct = false;
while (attempts < 3 && !correct) {
    // 获取用户输入并验证
    attempts++;
    if (checkAnswer(...)) {
        correct = true;
    }
}
```

### 3. 答案验证策略
所有数值计算都支持误差容忍：
```java
public boolean checkAnswer(double answer) {
    return Math.abs(answer - correctValue) < tolerance;
}
```

### 4. 进度跟踪机制
使用集合（Set）跟踪已完成的任务：
```java
private final Set<Integer> practiced;  // 或Set<String>等不同类型

public void addPracticed(int index) {
    practiced.add(index);
}

public boolean isComplete() {
    return practiced.size() >= requiredCount;
}
```

## 与GUI模块交互

Game模块与GUI模块的交互主要通过以下方式：

1. **初始化**：GUI创建Game对象并保持引用
2. **数据获取**：GUI调用Game的getter方法获取展示数据
3. **答案验证**：GUI将用户输入传给Game进行验证
4. **状态查询**：GUI通过Game的状态方法判断任务完成情况
5. **重置功能**：GUI可调用Game的reset方法重新开始任务

示例交互流程：
```java
// GUI代码
AngleCalculation game = new AngleCalculation();
int angle = game.getNextAngle();  // 获取角度
displayAngle(angle);  // 在界面上显示角度

// 用户输入后
String userAnswer = getUserInput();
boolean correct = game.checkAnswer(angle, userAnswer);  // 验证答案

if (correct) {
    game.addIdentifiedType(game.getAngleType(angle));  // 更新状态
}

if (game.isTaskComplete()) {
    showCompletionMessage();  // 显示完成信息
}
```

## 测试与调试

各模块内置的命令行接口不仅用于独立运行，也是测试和调试的重要工具：

- **独立测试**：每个模块可以独立运行并完整测试
- **快速验证**：无需GUI即可验证业务逻辑
- **命令行调试**：便于在无图形环境中调试问题

测试示例：
```java
// 测试AngleCalculation类
AngleCalculation angleTest = new AngleCalculation();
List<Integer> attempts = angleTest.startAngleRecognition();
System.out.println("每种类型的尝试次数: " + attempts);
```

## 扩展指南

如要添加新的游戏模块，应遵循以下步骤：

1. **创建新类**：在`com.shapeville.game`包中创建新类
2. **实现核心功能**：
   - 初始化方法
   - 数据生成逻辑
   - 答案验证方法
   - 状态管理功能
3. **添加命令行接口**：实现`start+模块名`方法
4. **保持一致性**：
   - 尝试次数限制保持一致（通常为3次）
   - 返回值类型保持一致（List<Integer>）
   - 命令行交互模式与现有模块保持一致

示例框架：
```java
public class NewCalculation {
    private final List<...> data;
    private final Set<...> completed;
    
    public NewCalculation() {
        this.data = initialize();
        this.completed = new HashSet<>();
    }
    
    // 必要的getter方法
    
    // 初始化数据
    private List<...> initialize() { ... }
    
    // 验证答案
    public boolean checkAnswer(...) { ... }
    
    // 判断任务完成
    public boolean isComplete() { ... }
    
    // 命令行入口
    public List<Integer> startNewCalculation() { ... }
} 