package com.shapeville.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ShapeDrawer {
    public static class ShapePanel extends JPanel {
        private Shape shape;
        private final int width;
        private final int height;
        private final Color fillColor;
        private final Color strokeColor;
        
        public ShapePanel(int width, int height) {
            this.width = width;
            this.height = height;
            this.fillColor = new Color(200, 230, 255);
            this.strokeColor = new Color(70, 130, 180);
            setPreferredSize(new Dimension(width, height));
            setMinimumSize(new Dimension(width, height));
            setMaximumSize(new Dimension(width, height));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            setOpaque(true);
        }
        
        public void setShape(Shape shape) {
            this.shape = shape;
            System.out.println("Shape set: " + (shape != null)); // 调试信息
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            System.out.println("Paint component called"); // 调试信息
            
            // Clear the background
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            
            if (shape == null) {
                System.out.println("No shape to draw"); // 调试信息
                return;
            }
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fill shape
            g2d.setColor(fillColor);
            g2d.fill(shape);
            
            // Draw outline
            g2d.setColor(strokeColor);
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(shape);
            
            System.out.println("Shape drawn"); // 调试信息
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
        
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
        
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    }
    
    public static Shape create2DShape(String shapeName, int width, int height) {
        int margin = 20;
        int w = width - 2 * margin;
        int h = height - 2 * margin;
        
        return switch (shapeName.toUpperCase()) {
            case "CIRCLE" -> new Ellipse2D.Double(margin, margin, Math.min(w, h), Math.min(w, h));
            case "RECTANGLE" -> new Rectangle2D.Double(margin, margin, w, h);
            case "SQUARE" -> {
                int size = Math.min(w, h);
                yield new Rectangle2D.Double(margin, margin, size, size);
            }
            case "TRIANGLE" -> {
                Path2D.Double path = new Path2D.Double();
                path.moveTo(width/2, margin);
                path.lineTo(margin, height - margin);
                path.lineTo(width - margin, height - margin);
                path.closePath();
                yield path;
            }
            case "OVAL" -> new Ellipse2D.Double(margin, margin, w, h);
            case "PENTAGON" -> createRegularPolygon(5, width/2, height/2, Math.min(w, h)/2);
            case "HEXAGON" -> createRegularPolygon(6, width/2, height/2, Math.min(w, h)/2);
            case "HEPTAGON" -> createRegularPolygon(7, width/2, height/2, Math.min(w, h)/2);
            case "OCTAGON" -> createRegularPolygon(8, width/2, height/2, Math.min(w, h)/2);
            case "RHOMBUS" -> {
                Path2D.Double path = new Path2D.Double();
                path.moveTo(width/2, margin);
                path.lineTo(width - margin, height/2);
                path.lineTo(width/2, height - margin);
                path.lineTo(margin, height/2);
                path.closePath();
                yield path;
            }
            case "KITE" -> {
                Path2D.Double path = new Path2D.Double();
                path.moveTo(width/2, margin);
                path.lineTo(width - margin, height/2);
                path.lineTo(width/2, height - margin);
                path.lineTo(margin, height/3);
                path.closePath();
                yield path;
            }
            default -> new Rectangle2D.Double(margin, margin, w, h);
        };
    }
    
    public static Shape create3DShape(String shapeName, int width, int height) {
        // 使用基本图形来表示3D形状的正视图
        int margin = 20;
        int w = width - 2 * margin;
        int h = height - 2 * margin;
        
        return switch (shapeName.toUpperCase()) {
            case "CUBE" -> {
                Path2D.Double path = new Path2D.Double();
                // 正面
                path.moveTo(margin + w/4, margin + h/4);
                path.lineTo(margin + w*3/4, margin + h/4);
                path.lineTo(margin + w*3/4, margin + h*3/4);
                path.lineTo(margin + w/4, margin + h*3/4);
                path.closePath();
                // 顶面
                path.moveTo(margin + w/4, margin + h/4);
                path.lineTo(margin, margin);
                path.lineTo(margin + w/2, margin);
                path.lineTo(margin + w*3/4, margin + h/4);
                // 侧面
                path.moveTo(margin + w*3/4, margin + h/4);
                path.lineTo(margin + w*3/4, margin + h*3/4);
                path.lineTo(width - margin, height - margin);
                path.lineTo(width - margin, margin + h/2);
                path.closePath();
                yield path;
            }
            case "SPHERE" -> new Ellipse2D.Double(margin, margin, Math.min(w, h), Math.min(w, h));
            case "CYLINDER" -> {
                Path2D.Double path = new Path2D.Double();
                // 顶部椭圆
                path.append(new Ellipse2D.Double(margin, margin, w, h/4), false);
                // 侧面
                path.moveTo(margin, margin + h/8);
                path.lineTo(margin, margin + h*7/8);
                path.moveTo(margin + w, margin + h/8);
                path.lineTo(margin + w, margin + h*7/8);
                // 底部椭圆
                path.append(new Ellipse2D.Double(margin, margin + h*3/4, w, h/4), false);
                yield path;
            }
            case "CONE" -> {
                Path2D.Double path = new Path2D.Double();
                // 底部椭圆
                path.append(new Ellipse2D.Double(margin, margin + h*3/4, w, h/4), false);
                // 侧面
                path.moveTo(margin + w/2, margin);
                path.lineTo(margin, margin + h*7/8);
                path.lineTo(margin + w, margin + h*7/8);
                path.closePath();
                yield path;
            }
            default -> create2DShape("RECTANGLE", width, height);
        };
    }
    
    private static Shape createRegularPolygon(int sides, double centerX, double centerY, double radius) {
        Path2D.Double path = new Path2D.Double();
        double angle = 2 * Math.PI / sides;
        
        for (int i = 0; i < sides; i++) {
            double x = centerX + radius * Math.cos(angle * i - Math.PI/2);
            double y = centerY + radius * Math.sin(angle * i - Math.PI/2);
            
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();
        return path;
    }
}