package com.shapeville.model;

/**
 * Enumeration of 3D geometric shapes supported in the Shapeville application.
 * Each shape has both English and Chinese names for internationalization support.
 * This enum is used for shape selection, display, and volume/surface area calculations.
 *
 * @author Ye Jin, Jian Wang, Zijie Long, Tianyun Zhang, Xianzhi Dong
 * @version 1.0
 * @since 2025-05-01
 */
public enum Shape3D {
    /** Cube shape with English and Chinese names */
    CUBE("cube", "立方体"),
    /** Cuboid shape with English and Chinese names */
    CUBOID("cuboid", "长方体"),
    /** Cylinder shape with English and Chinese names */
    CYLINDER("cylinder", "圆柱体"),
    /** Sphere shape with English and Chinese names */
    SPHERE("sphere", "球体"),
    /** Triangular prism shape with English and Chinese names */
    TRIANGULAR_PRISM("triangular prism", "三棱柱"),
    /** Square-based pyramid shape with English and Chinese names */
    SQUARE_BASED_PYRAMID("square-based pyramid", "方锥体"),
    /** Cone shape with English and Chinese names */
    CONE("cone", "圆锥体"),
    /** Tetrahedron shape with English and Chinese names */
    TETRAHEDRON("tetrahedron", "四面体");

    private final String english;
    private final String chinese;

    /**
     * Constructs a Shape3D enum value with English and Chinese names.
     *
     * @param english The English name of the shape
     * @param chinese The Chinese name of the shape
     */
    Shape3D(String english, String chinese) {
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