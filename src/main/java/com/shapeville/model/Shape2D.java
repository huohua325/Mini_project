package com.shapeville.model;

public enum Shape2D {
    CIRCLE("circle", "圆形"),
    RECTANGLE("rectangle", "矩形"),
    TRIANGLE("triangle", "三角形"),
    OVAL("oval", "椭圆"),
    OCTAGON("octagon", "八边形"),
    SQUARE("square", "正方形"),
    HEPTAGON("heptagon", "七边形"),
    RHOMBUS("rhombus", "菱形"),
    PENTAGON("pentagon", "五边形"),
    HEXAGON("hexagon", "六边形"),
    KITE("kite", "风筝形");

    private final String english;
    private final String chinese;

    Shape2D(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public String getEnglish() { return english; }
    public String getChinese() { return chinese; }
}
