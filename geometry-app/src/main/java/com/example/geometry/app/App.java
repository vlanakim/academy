package com.example.geometry.app;

import com.example.geometry.library.*;
import com.example.geometry.utils.GeometryUtils;
import com.example.geometry.threeD.*;

public class App {
    public static void main(String[] args) {
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);
        Shape triangle = new Triangle(3, 4, 5);

        System.out.println("Circle area: " + circle.getArea());
        System.out.println("Rectangle area: " + rectangle.getArea());
        System.out.println("Triangle area: " + triangle.getArea());

        System.out.println("Comparing circle and rectangle: " +
                GeometryUtils.compareAreas(circle, rectangle));
        System.out.println("Comparing perimeters: " +
                GeometryUtils.comparePerimeters(circle, rectangle));

        Cube cube = new Cube(3);
        Sphere sphere = new Sphere(4);

        System.out.println("Cube volume: " + cube.getVolume());
        System.out.println("Sphere volume: " + sphere.getVolume());
    }
}