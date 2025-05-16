# Shapeville - Geometry Learning Playground

A Java Swing-based geometric calculation and recognition application designed to help users learn geometry through interactive teaching.

## Table of Contents
- [Shapeville - Geometry Learning Playground](#shapeville---geometry-learning-playground)
  - [Table of Contents](#table-of-contents)
  - [Project Description](#project-description)
  - [Features](#features)
  - [Technology Stack](#technology-stack)
  - [System Requirements](#system-requirements)
  - [Installation and Running](#installation-and-running)
    - [Method 1: Using Maven](#method-1-using-maven)
    - [Method 2: Direct Run](#method-2-direct-run)
    - [Method 3: Using IDE](#method-3-using-ide)
  - [Project Structure](#project-structure)
  - [Module Details](#module-details)
    - [Main Module](#main-module)
    - [Model Module](#model-module)
    - [Game Module](#game-module)
    - [GUI Module](#gui-module)
      - [Core Window Classes](#core-window-classes)
      - [Task Panel Module (tasks)](#task-panel-module-tasks)
      - [Shape Rendering Module (shapes)](#shape-rendering-module-shapes)
  - [Workflow and Data Flow](#workflow-and-data-flow)
    - [1. Vertical Data Flow](#1-vertical-data-flow)
    - [2. Horizontal Data Flow](#2-horizontal-data-flow)
    - [3. Key Data Interaction Points](#3-key-data-interaction-points)
    - [4. Window and Task Panel Collaboration](#4-window-and-task-panel-collaboration)
  - [Learning Plan and Usage](#learning-plan-and-usage)
    - [Learning Suggestions](#learning-suggestions)
    - [Usage Instructions](#usage-instructions)
  - [Developer Documentation](#developer-documentation)
    - [Development Environment Setup](#development-environment-setup)
    - [Module Development Guidelines](#module-development-guidelines)
      - [Model Module Development](#model-module-development)
      - [Game Module Development](#game-module-development)
      - [GUI Module Development](#gui-module-development)
    - [Adding New Features](#adding-new-features)
    - [Testing Guidelines](#testing-guidelines)
    - [Best Practices](#best-practices)
  - [License](#license)
  - [Contact](#contact)

## Project Description

Shapeville is an interactive geometry learning application built with Java Swing. The application combines gaming elements with educational content, helping users master various geometric concepts and calculation methods through a series of progressively challenging tasks.

From basic angle recognition and shape identification to complex area calculations and sector computations, Shapeville provides a comprehensive and systematic geometry learning experience. The application tracks user progress, unlocks new content based on completion status, and provides immediate feedback and scoring to help users understand their mastery level.

Shapeville is not just a learning tool but also a platform for self-challenge, motivating users to continuously improve their geometric knowledge and calculation abilities through star ratings and level progression.

## Features

The project offers diverse geometry learning tasks, including:

- **Shape Recognition**: Identify and distinguish various 2D and 3D geometric shapes
- **Angle Calculation**: Learn to identify and measure different types of angles (acute, right, obtuse, straight, reflex)
- **Area Calculation**: Master methods for calculating areas of basic geometric shapes
- **Circle Calculation**: Learn circle circumference and area calculations
- **Compound Shape Calculation**: Calculate areas of complex figures composed of multiple basic shapes
- **Sector Calculation**: Learn and apply sector area and arc length calculation formulas

System features:

- **Progressive Learning System**: Tasks are graded by difficulty, from basic to advanced
- **Immediate Feedback**: Each question provides instant evaluation and correct answers
- **Progress Tracking**: Records user learning progress and scores
- **Level System**: Evaluates user levels based on task completion and scores
- **Unlock Mechanism**: Advanced tasks unlock after completing basic tasks
- **Performance Rating**: Evaluates user performance through star ratings and percentages
- **Data Persistence**: Saves user progress and scores
- **Intuitive GUI**: Modern UI design with simple and intuitive operations

## Technology Stack

- **Java 17**：Core programming language
- **Swing**：GUI framework, used to build user interface
- **Java2D**：Used to draw various geometric shapes
- **Maven**：Project management and build tool
- **JUnit 5**：Unit test framework
- **Gson**：JSON data processing library, used for data persistence

## System Requirements

- **JDK 17** or higher
- **Maven 3.6.x** or higher
- **Memory**：At least 512MB available RAM
- **Storage**：At least 100MB available disk space
- **Display**：Resolution not less than 1024x768

## Installation and Running

### Method 1: Using Maven

1. Clone project to local:
   ```bash
   git clone https://github.com/yourusername/shapeville.git
   cd shapeville
   ```

2. Use Maven to compile project:
```bash
mvn clean package
```

3. Run compiled JAR file:
   ```bash
   java -jar target/shapeville-1.0-SNAPSHOT.jar
   ```

### Method 2: Direct Run

1. Ensure Java 17 or higher is installed:
   ```bash
   java -version
   ```

2. Use Maven to execute:
```bash
mvn exec:java -Dexec.mainClass="com.shapeville.Main"
```

### Method 3: Using IDE

1. Import project in IDE (recommended IntelliJ IDEA or Eclipse)
2. Ensure JDK 17 is configured
3. Run `com.shapeville.Main` class's `main` method

## Project Structure

```
shapeville/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── shapeville/
│       │           ├── Main.java                        # Program entry point
│       │           ├── game/                            # Game logic module
│       │           │   ├── AngleCalculation.java        # Angle calculation logic
│       │           │   ├── AreaCalculation.java         # Area calculation logic
│       │           │   ├── CircleCalculation.java       # Circle calculation logic
│       │           │   ├── CompoundShapeCalculation.java # Compound shape calculation
│       │           │   ├── SectorCalculation.java       # Sector calculation logic
│       │           │   └── ShapeRecognition.java        # Shape recognition logic
│       │           ├── gui/                             # User interface module
│       │           │   ├── MainWindow.java              # Main window
│       │           │   ├── TaskWindow.java              # Task window
│       │           │   ├── ResultWindow.java            # Result window
│       │           │   ├── UIManager.java               # UI manager
│       │           │   ├── tasks/                       # Task panel
│       │           │   │   ├── TaskPanelInterface.java  # Task panel interface
│       │           │   │   ├── BaseTaskPanel.java       # Task panel base class
│       │           │   │   ├── AngleCalculationPanel.java # Angle calculation panel
│       │           │   │   ├── AreaCalculationPanel.java # Area calculation panel
│       │           │   │   ├── CircleCalculationPanel.java # Circle calculation panel
│       │           │   │   ├── CompoundShapeCalculationPanel.java # Compound shape panel
│       │           │   │   ├── SectorCalculationPanel.java # Sector calculation panel
│       │           │   │   └── ShapePanel.java          # Shape recognition panel
│       │           │   └── shapes/                      # Shape rendering
│       │           │       ├── ShapeRenderer.java       # Shape rendering interface
│       │           │       ├── CompoundShapeDrawer.java # Compound shape drawing base class
│       │           │       ├── ShapeDrawer.java         # Basic shape drawing tool class
│       │           │       ├── CircleDrawer.java        # Circle drawing dedicated class
│       │           │       └── compound/                # Compound shape implementation
│       │           │           ├── ArrowShape.java      # Arrow shape
│       │           │           ├── ComplexStairShape.java # Complex stair shape
│       │           │           ├── DoubleStairShape.java # Double stair shape
│       │           │           ├── HouseShape.java      # House shape
│       │           │           ├── IrregularShape.java  # Irregular shape
│       │           │           ├── StairShape.java      # Stair shape
│       │           │           ├── StepShape.java       # Step shape
│       │           │           ├── TrapezoidShape.java  # Trapezoid shape
│       │           │           └── TShape.java          # T shape
│       │           ├── model/                           # Data model
│       │           │   ├── Shape2D.java                 # 2D shape enumeration
│       │           │   └── Shape3D.java                 # 3D shape enumeration
│       │           └── utils/                           # Tool class
│       │               ├── DataManager.java             # Data management tool
│       │               ├── ShapeDrawer.java             # Shape drawing tool (duplicated with gui/shapes's same name file)
│       │               └── Utils.java                   # General tool method
│       └── resources/                                   # Resource file
│           ├── data/                                    # Data file
│           └── images/                                  # Image resource
│               ├── 2d/                                  # 2D shape image
│               └── 3d/                                  # 3D shape image
├── pom.xml                                              # Maven project configuration
└── README.md                                            # Project description document
```

## Module Details

### Main Module

`Main.java` is the program's entry point, responsible for initializing the graphical interface and starting the program. Main functions:

- Set system appearance to local system appearance
- Initialize and display GUI through `UIManager`
- Run UI code in EDT thread to ensure UI thread safety

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            // Set system appearance
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize and display GUI
        UIManager.getInstance().initialize();
    });
}
```

### Model Module

model module defines the shape enumeration types used in the system, which is the data model layer of the system.

- **Shape2D.java**
  - Defines the enumeration of all 2D geometric shapes supported by the system
  - Each shape contains English and Chinese names
  - Provides a method to get the shape name
  - Includes 11 shapes such as circle, rectangle, triangle, ellipse, octagon, etc.

- **Shape3D.java**
  - Defines the enumeration of all 3D geometric shapes supported by the system
  - Each shape contains English and Chinese names
  - Provides a method to get the shape name
  - Includes 8 shapes such as cube, rectangular prism, cylinder, sphere, etc.

### Game Module

game module is the core business logic layer of Shapeville, responsible for calculating, verifying, and scoring various geometric tasks. This module is separated from the UI layer and focuses on data processing and calculation logic.

- **AngleCalculation.java**
  - Core logic for angle recognition and calculation
  - Generates random angles and determines their type (acute, right, obtuse, etc.)
  - Verifies user answers and records attempt times
  - Tracks recognized angle types and completion progress

- **AreaCalculation.java**
  - Core logic for calculating areas of basic geometric shapes
  - Supports shapes such as rectangle, triangle, parallelogram, trapezoid, etc.
  - Generates random parameters and provides area calculation formulas
  - Verifies the correctness of user calculation results

- **CircleCalculation.java**
  - Dedicated module for calculating related to circles
  - Supports calculations between radius/diameter and circumference/area
  - Generates random parameters and provides formulas
  - Verifies user answers and scores

- **CompoundShapeCalculation.java**
  - Core logic for calculating compound shapes
  - Manages 9 predefined compound shapes (arrow, T-shape, trapezoid, etc.)
  - Provides detailed descriptions and calculation steps
  - Verifies user area calculation results

- **SectorCalculation.java**
  - Dedicated module for calculating sectors
  - Provides sector area and arc length calculation
  - Manages predefined sector collection
  - Verifies user calculation results and provides formulas

- **ShapeRecognition.java**
  - Core logic for shape recognition
  - Manages 2D and 3D shape library
  - Randomly selects shapes for recognition test
  - Verifies user recognition results

### GUI Module

GUI module is the user interface layer of Shapeville, responsible for all visual presentation and user interaction functions. It uses the MVC architecture and implements a modernized user interface with Java Swing.

#### Core Window Classes

- **UIManager.java**
  - GUI module's core controller (singleton mode)
  - Manages creation, switching, and interaction of all windows
  - Tracks task status (locked, unlocked, in progress, completed)
  - Handles task unlocking conditions and user progress calculation

- **MainWindow.java**
  - Main entry window of the application
  - Displays all available learning tasks and user progress
  - Manages task button status and level display
  - Provides overall learning progress visualization

- **TaskWindow.java**
  - Container window for task execution
  - Loads and displays the interface of specific tasks
  - Handles task completion events and feedback display
  - Communicates with UIManager to report task results

- **ResultWindow.java**
  - Task result display window
  - Animated display of score and star rating
  - Displays detailed feedback and score analysis
  - Provides subsequent operation selection

#### Task Panel Module (tasks)

Task panel module is the core interactive layer of GUI, responsible for presenting various geometric learning tasks to users, processing user input, and providing feedback.

- **TaskPanelInterface.java**
  - Defines basic interface methods for task panels
  - Includes start, pause, resume, end task lifecycle methods
  - Defines methods to get score and feedback

- **BaseTaskPanel.java**
  - Abstract base class for all task panels
  - Implements common functions such as attempt times management and feedback display
  - Defines abstract methods that must be implemented by subclasses

- **Task Dedicated Panels**
  - **AngleCalculationPanel.java**: Angle recognition and calculation task panel
  - **AreaCalculationPanel.java**: Basic shape area calculation task panel
  - **CircleCalculationPanel.java**: Circle calculation task panel
  - **CompoundShapeCalculationPanel.java**: Compound shape calculation task panel
  - **SectorCalculationPanel.java**: Sector calculation task panel
  - **ShapePanel.java**: Shape recognition task panel

#### Shape Rendering Module (shapes)

Shape rendering module is responsible for visualizing various geometric shapes, ensuring that users can understand geometric concepts intuitively.

- **ShapeRenderer.java**
  - Core rendering interface, defining methods that all shape renderers must implement
  - Provides methods for drawing shapes, displaying dimensions, and calculating areas

- **CompoundShapeDrawer.java**
  - Base class for drawing compound shapes
  - Provides initialization of drawing context, scaling calculation, and dimension annotation
  - Defines common color and style constants

- **ShapeDrawer.java** and **CircleDrawer.java**
  - Tool class for drawing basic geometric shapes
  - Provides drawing methods for shapes such as rectangle, triangle, parallelogram, etc.
  - CircleDrawer is specifically used for drawing circles

- **compound/ directory**
  - Contains specific implementations of 9 compound shapes
  - Each shape inherits from CompoundShapeDrawer and implements ShapeRenderer interface
  - Implements automatic scaling and detailed dimension annotation
  - Contains accurate area calculation and solution steps

## Workflow and Data Flow

The project uses layered architecture, with clear data flow:

### 1. Vertical Data Flow

- **Top-down**：
  ```
  Main → UIManager → Specific window → Business logic → Data model
  ```
  - User interaction starts from UI layer
  - Through UIManager for unified management
  - Passes to specific business logic layer
  - Finally affects data model

- **Bottom-up**：
  ```
  Data model → Business logic → Specific window → UIManager → Main
  ```
  - Updates data model
  - Triggers business logic processing
  - Through UI layer to display results
  - Finally feedback to user

### 2. Horizontal Data Flow

- **Core calculation process**：
  ```
  User input → Data verification → Calculation processing → Result display
  ```
  - User inputs data through GUI
  - System performs data verification
  - Calls game module for calculation
  - Displays result in ResultWindow

- **Progress tracking process**：
  ```
  User operation → Progress update → UI update
  ```
  - Records user operations
  - Updates progress data
  - Updates UI display

### 3. Key Data Interaction Points

- **UI and business logic interaction**：
  - Interaction between TaskWindow and specific calculation module
  - Interaction between ResultWindow and data model
  - Interaction between MainWindow and UIManager

### 4. Window and Task Panel Collaboration

- `MainWindow` is the user's main entry point, responsible for task selection and progress display. When a user clicks a task button, `UIManager` switches to the corresponding `TaskWindow` based on task unlocking status.
- `TaskWindow` is responsible for executing specific tasks, with actual interaction and judging logic implemented by panels in `tasks` folder. After completing a task, `TaskWindow` calls `UIManager.showResult` to pop out `ResultWindow` to display score and feedback.
- `ResultWindow` displays task results and allows users to return to main interface or re-challenge tasks.
- `UIManager` acts as a global scheduler, maintaining all task status, score, unlocking logic, and level evaluation, responsible for window creation, switching, and destruction.

## Learning Plan and Usage

### Learning Suggestions

1. **Progressive Learning**：Complete all basic tasks first, then challenge advanced tasks
2. **Repeat Practice**：For difficult tasks, you can try multiple times to get familiar with concepts and calculation methods
3. **Focus on Feedback**：After completing a task, carefully check feedback and score analysis to find out shortcomings
4. **Comprehensive Learning**：Try all types of tasks to improve geometric abilities

### Usage Instructions

1. **Start Application**：Run the program, the main window will display all available tasks
2. **Select Task**：Click the task button to start a specific task
   - Basic tasks are unlocked by default
   - Advanced tasks require completion of all basic tasks and at least 70 points to unlock
3. **Execute Task**：
   - Read task description
   - Complete task requirements based on prompts
   - Submit answers to get feedback
   - Check result analysis after task completion
4. **Check Results**：After task completion, the following will be displayed:
   - Score and star rating
   - Detailed task feedback
   - Score analysis and performance rating
5. **Continue Learning**：Based on results, choose:
   - Return to main interface to select new tasks
   - Re-attempt current task
   - End learning session

## Developer Documentation

### Development Environment Setup

1. Ensure JDK 17 and Maven are installed
2. Clone repository to local
3. Import project in IDE (recommended IntelliJ IDEA or Eclipse)
4. Ensure Maven correctly loads all dependencies

### Module Development Guidelines

#### Model Module Development

1. **Add New 2D Shape**
   - Add new shape enumeration value in `Shape2D.java`
   - Provide English and Chinese names as parameters
   - Ensure support is added in corresponding drawing module

2. **Add New 3D Shape**
   - Add new shape enumeration value in `Shape3D.java`
   - Provide English and Chinese names as parameters
   - Ensure support is added in corresponding drawing module

3. **Best Practices**
   - Use enumeration type instead of string to represent shape, improving type safety
   - Use `getChinese()` and `getEnglish()` methods when multi-language support is needed
   - Ensure new shape is synchronized in all related modules when adding new shape

#### Game Module Development

1. **General Design Pattern**
   - Task progress tracking: Each module implements tracking user progress mechanism
   - Attempt times management: Limits user attempt times
   - Score system: Calculates score based on attempt times
   - Command line interaction: Provides independent command line interaction mode (convenient for testing)

2. **Add New Shape**
   - Add basic shape: Update `AreaCalculation.ShapeType` and related calculation methods
   - Add compound shape: Create new shape class and register in `CompoundShapeCalculation`

3. **Extend Calculation Function**
   - Add new methods to existing classes
   - Create new calculation task class (naming format: `[Calculation Type]Calculation.java`)

4. **Improve Score System**
   - Add time factor
   - Implement difficulty coefficient
   - Increase continuous correct reward

#### GUI Module Development

1. **Add New Window**
   - Create new class inheriting from JFrame
   - Add new window management method in UIManager

2. **Add New Task Type**
   - Create new task panel class (inheriting BaseTaskPanel and implementing TaskPanelInterface)
   - Register new task in UIManager
   - Update TaskWindow.setupTask() method
   - Add new task button in MainWindow

3. **Create New Basic Shape**
   - Add new static method in ShapeDrawer class
   - Implement filling, border, and dimension annotation

4. **Create New Compound Shape**
   - Create new class in compound package (inheriting CompoundShapeDrawer)
   - Implement necessary interface methods (drawing, calculating area, etc.)
   - Register new shape in CompoundShapeCalculation

5. **Rendering Hint**
   - Use Java2D anti-aliasing feature to improve drawing quality
   - Ensure shape is appropriately scaled to fit different panel sizes
   - Use consistent color scheme and line style
   - Consider using double buffering technology to avoid flickering

### Adding New Features

If you need to add new task types, you need to complete the following steps:

1. **Plan Function**
   - Clearly define task goals and user interaction process
   - Design data model and calculation logic

2. **Implement Business Logic**
   - Create new business logic class in `game` package
   - Implement necessary calculation and verification methods
   - Add progress tracking and scoring functionality

3. **Create User Interface**
   - Create corresponding task panel class in `gui.tasks` package
   - Design reasonable UI layout and interaction elements
   - Implement interaction with business logic layer

4. **Integrate into System**
   - Register new task in `UIManager`
   - Update `TaskWindow.setupTask()` method
   - Add new task button in `MainWindow`
   - Update task unlocking logic

5. **Test and Optimize**
   - Perform unit tests and integration tests
   - Collect user feedback and optimize experience

### Testing Guidelines

1. **Unit Test**
   - Use JUnit 5 to create test class
   - Test area calculation formula
   - Test angle type determination logic
   - Test answer verification and scoring mechanism

2. **Integration Test**
   - Test interaction between windows
   - Test task completion process
   - Test data persistence functionality

3. **UI Test**
   - Test display effect under various screen resolutions
   - Test user interface responsiveness
   - Verify exception handling and error prompt

### Best Practices

1. **Code Organization**
   - Maintain clear separation between layers
   - Follow single responsibility principle
   - Use consistent naming conventions

2. **UI Development**
   - Use thread-safe way to update UI (SwingUtilities.invokeLater)
   - Use layout manager reasonably
   - Implement correct resource release

3. **Performance Optimization**
   - Use delayed initialization strategy
   - Cache frequently used resources
   - Avoid unnecessary object creation
   - Use double buffering technology to optimize drawing performance

4. **Extensible Design**
   - Use interfaces and abstract classes to improve flexibility
   - Follow design patterns (singleton, template method, strategy, etc.)
   - Reserve future feature expansion points

## License

[MIT License](LICENSE)

## Contact

Project maintainer: [HuoHua325]  
Email: []  
GitHub: []
