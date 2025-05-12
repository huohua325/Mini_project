# Shapeville 模型层 (Model)

本目录包含 Shapeville 几何学习乐园项目的模型层组件，主要定义了应用程序中使用的形状枚举类型。

## 文件结构

- `Shape2D.java`: 二维形状枚举类
- `Shape3D.java`: 三维形状枚举类

## Shape2D.java

`Shape2D` 枚举定义了系统支持的所有二维几何形状。每个形状都包含英文和中文名称。

```java
public enum Shape2D {
    CIRCLE("circle", "圆形"),
    RECTANGLE("rectangle", "矩形"),
    TRIANGLE("triangle", "三角形"),
    // ...其他形状
}
```

### 已支持的二维形状

| 枚举值 | 英文名称 | 中文名称 |
|--------|---------|---------|
| CIRCLE | circle | 圆形 |
| RECTANGLE | rectangle | 矩形 |
| TRIANGLE | triangle | 三角形 |
| OVAL | oval | 椭圆 |
| OCTAGON | octagon | 八边形 |
| SQUARE | square | 正方形 |
| HEPTAGON | heptagon | 七边形 |
| RHOMBUS | rhombus | 菱形 |
| PENTAGON | pentagon | 五边形 |
| HEXAGON | hexagon | 六边形 |
| KITE | kite | 风筝形 |

### 方法

- `getEnglish()`: 获取形状的英文名称
- `getChinese()`: 获取形状的中文名称

## Shape3D.java

`Shape3D` 枚举定义了系统支持的所有三维几何形状。每个形状都包含英文和中文名称。

```java
public enum Shape3D {
    CUBE("cube", "立方体"),
    CUBOID("cuboid", "长方体"),
    // ...其他形状
}
```

### 已支持的三维形状

| 枚举值 | 英文名称 | 中文名称 |
|--------|---------|---------|
| CUBE | cube | 立方体 |
| CUBOID | cuboid | 长方体 |
| CYLINDER | cylinder | 圆柱体 |
| SPHERE | sphere | 球体 |
| TRIANGULAR_PRISM | triangular prism | 三棱柱 |
| SQUARE_BASED_PYRAMID | square-based pyramid | 方锥体 |
| CONE | cone | 圆锥体 |
| TETRAHEDRON | tetrahedron | 四面体 |

### 方法

- `getEnglish()`: 获取形状的英文名称
- `getChinese()`: 获取形状的中文名称

## 开发指南

### 添加新的二维形状

如需添加新的二维形状，请按照以下步骤操作：

1. 在 `Shape2D.java` 中的枚举列表中添加新的形状枚举值
2. 提供英文名称和中文名称作为参数
3. 确保在相应的图形渲染模块中也添加对应的绘制支持

例如：
```java
DECAGON("decagon", "十边形"),
```

### 添加新的三维形状

如需添加新的三维形状，请按照以下步骤操作：

1. 在 `Shape3D.java` 中的枚举列表中添加新的形状枚举值
2. 提供英文名称和中文名称作为参数
3. 确保在相应的图形渲染模块中也添加对应的绘制支持

例如：
```java
DODECAHEDRON("dodecahedron", "十二面体"),
```

### 与其他模块的集成

Shape2D 和 Shape3D 枚举通常与以下模块集成：

- GUI 形状绘制模块：使用枚举来决定绘制何种形状
- 任务面板：在任务中引用特定形状
- 形状计算模块：根据形状类型执行不同的计算逻辑

### 最佳实践

- 使用枚举类型而非字符串来表示形状，这有助于类型安全和代码可读性
- 在需要多语言支持的地方使用 `getChinese()` 和 `getEnglish()` 方法
- 在添加新的形状时，确保在所有相关模块中同步更新相应的支持代码 