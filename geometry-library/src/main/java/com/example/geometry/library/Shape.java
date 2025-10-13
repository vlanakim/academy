package com.example.geometry.library;

public interface Shape {
    double getArea();
    double getPerimeter();
    default String getDescription() {
        return "This is a 2D shape.";
    }
}
