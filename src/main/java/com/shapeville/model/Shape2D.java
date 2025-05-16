package com.shapeville.model;

/**
 * Enumeration of 2D geometric shapes supported in the Shapeville application.
 * Each shape has both English and Chinese names for internationalization support.
 * This enum is used for shape selection, display, and calculation purposes.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2024-05-01
 */
public enum Shape2D {
    /** Circle shape with English and Chinese names */
    CIRCLE("circle", "圆形"),
    /** Rectangle shape with English and Chinese names */
    RECTANGLE("rectangle", "矩形"),
    /** Triangle shape with English and Chinese names */
    TRIANGLE("triangle", "三角形"),
    /** Oval shape with English and Chinese names */
    OVAL("oval", "椭圆"),
    /** Octagon shape with English and Chinese names */
    OCTAGON("octagon", "八边形"),
    /** Square shape with English and Chinese names */
    SQUARE("square", "正方形"),
    /** Heptagon shape with English and Chinese names */
    HEPTAGON("heptagon", "七边形"),
    /** Rhombus shape with English and Chinese names */
    RHOMBUS("rhombus", "菱形"),
    /** Pentagon shape with English and Chinese names */
    PENTAGON("pentagon", "五边形"),
    /** Hexagon shape with English and Chinese names */
    HEXAGON("hexagon", "六边形"),
    /** Kite shape with English and Chinese names */
    KITE("kite", "风筝形");

    private final String english;
    private final String chinese;

    /**
     * Constructs a Shape2D enum value with English and Chinese names.
     *
     * @param english The English name of the shape
     * @param chinese The Chinese name of the shape
     */
    Shape2D(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    /**
     * Gets the English name of the shape.
     * @return The shape's name in English
     */
    public String getEnglish() { return english; }

    /**
     * Gets the Chinese name of the shape.
     * @return The shape's name in Chinese
     */
    public String getChinese() { return chinese; }
}
