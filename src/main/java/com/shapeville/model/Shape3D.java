package com.shapeville.model;

public enum Shape3D {
    CUBE("cube", "立方体"),
    CUBOID("cuboid", "长方体"),
    CYLINDER("cylinder", "圆柱体"),
    SPHERE("sphere", "球体"),
    TRIANGULAR_PRISM("triangular prism", "三棱柱"),
    SQUARE_BASED_PYRAMID("square-based pyramid", "方锥体"),
    CONE("cone", "圆锥体"),
    TETRAHEDRON("tetrahedron", "四面体");

    private final String english;
    private final String chinese;

    Shape3D(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public String getEnglish() { return english; }
    public String getChinese() { return chinese; }
}