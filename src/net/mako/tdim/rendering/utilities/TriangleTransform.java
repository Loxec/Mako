package net.mako.tdim.rendering.utilities;

import net.mako.tdim.rendering.components.Triangle;
import net.mako.tdim.rendering.components.Vertex;

public class TriangleTransform {
    public static void translateX(double amount, Triangle triangle) {
        triangle.v1.setX(triangle.v1.getX() + amount);
        triangle.v2.setX(triangle.v2.getX() + amount);
        triangle.v3.setX(triangle.v3.getX() + amount);
    }

    public static void translateY(double amount, Triangle triangle) {
        triangle.v1.setY(triangle.v1.getY() + amount);
        triangle.v2.setY(triangle.v2.getY() + amount);
        triangle.v3.setY(triangle.v3.getY() + amount);
    }

    public static void translate(double amount_x,double amount_y, Triangle triangle) {
        triangle.v1.setX(triangle.v1.getX() + amount_x);
        triangle.v2.setX(triangle.v2.getX() + amount_x);
        triangle.v3.setX(triangle.v3.getX() + amount_x);

        triangle.v1.setY(triangle.v1.getY() + amount_y);
        triangle.v2.setY(triangle.v2.getY() + amount_y);
        triangle.v3.setY(triangle.v3.getY() + amount_y);
    }

    public static void rotate(double angleDegrees, Triangle t) {

        double angle = Math.toRadians(angleDegrees);

        double centerX = (t.v1.getX() + t.v2.getY() + t.v3.getY()) / 3.0;
        double centerY = (t.v1.getY() + t.v2.getY() + t.v3.getY()) / 3.0;

        for (Vertex v : t.vertexes) {

            double dx = v.getX() - centerX;
            double dy = v.getY() - centerY;

            double newX = dx * Math.cos(angle) - dy * Math.sin(angle);
            double newY = dx * Math.sin(angle) + dy * Math.cos(angle);

            v.setX(newX + centerX);
            v.setY(newY + centerY);
        }
    }

    public static void scaleUp(double amount, Triangle t) {

        double centerX = (t.v1.getX() + t.v2.getX() + t.v3.getX()) / 3.0;
        double centerY = (t.v1.getY() + t.v2.getY() + t.v3.getY()) / 3.0;

        for (Vertex v : t.vertexes) {

            double dx = v.x - centerX;
            double dy = v.y - centerY;

            v.x = centerX + dx * amount;
            v.y = centerY + dy * amount;
        }
    }

    public static void scaleDown(double amount, Triangle t) {

        double centerX = (t.v1.getX() + t.v2.getX() + t.v3.getX()) / 3.0;
        double centerY = (t.v1.getY() + t.v2.getY() + t.v3.getY()) / 3.0;

        for (Vertex v : t.vertexes) {

            double dx = v.x - centerX;
            double dy = v.y - centerY;

            v.x = centerX + dx / amount;
            v.y = centerY + dy / amount;
        }
    }


}
