package com.example.geometry.utils;

import com.example.geometry.library.Shape;

public class GeometryUtils {
    public static int compareAreas(Shape s1, Shape s2) {
        return Double.compare(s1.getArea(), s2.getArea());
    }
}
