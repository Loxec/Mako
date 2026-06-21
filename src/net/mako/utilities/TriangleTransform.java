package net.mako.utilities;

import net.mako.rendering.Triangle;
import net.mako.rendering.Vertex;

import java.awt.*;
import java.util.Vector;

public class TriangleTransform {
    public static void translateX(int amount, Triangle triangle) {
        triangle.v1.x += amount;
        triangle.v2.x += amount;
        triangle.v3.x += amount;
    }

    public static void translateY(int amount, Triangle triangle) {
        triangle.v1.y += amount;
        triangle.v2.y += amount;
        triangle.v3.y += amount;
    }

    public static void translate(int amount_x,int amount_y, Triangle triangle) {
        triangle.v1.x += amount_x;
        triangle.v2.x += amount_x;
        triangle.v3.x += amount_x;

        triangle.v1.y += amount_y;
        triangle.v2.y += amount_y;
        triangle.v3.y += amount_y;
    }

    public static void rotate(double angleDegrees, Triangle t) {

        double angle = Math.toRadians(angleDegrees);

        double centerX = (t.v1.x + t.v2.x + t.v3.x) / 3.0;
        double centerY = (t.v1.y + t.v2.y + t.v3.y) / 3.0;

        for (Vertex v : t.vertexes) {

            double dx = v.x - centerX;
            double dy = v.y - centerY;

            double newX = dx * Math.cos(angle) - dy * Math.sin(angle);
            double newY = dx * Math.sin(angle) + dy * Math.cos(angle);

            v.x = newX + centerX;
            v.y = newY + centerY;
        }
    }

    public static void scaleUp(double amount, Triangle t) {

        double centerX = (t.v1.x + t.v2.x + t.v3.x) / 3.0;
        double centerY = (t.v1.y + t.v2.y + t.v3.y) / 3.0;

        for (Vertex v : t.vertexes) {

            double dx = v.x - centerX;
            double dy = v.y - centerY;

            v.x = centerX + dx * amount;
            v.y = centerY + dy * amount;
        }
    }

    public static void scaleDown(double amount, Triangle t) {

        double centerX = (t.v1.x + t.v2.x + t.v3.x) / 3.0;
        double centerY = (t.v1.y + t.v2.y + t.v3.y) / 3.0;

        for (Vertex v : t.vertexes) {

            double dx = v.x - centerX;
            double dy = v.y - centerY;

            v.x = centerX + dx / amount;
            v.y = centerY + dy / amount;
        }
    }


}
