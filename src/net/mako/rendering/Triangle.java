package net.mako.rendering;

import java.awt.*;

public class Triangle {

    public Vertex v1;
    public Vertex v2;
    public Vertex v3;
    public Color color;
    public boolean filled;

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = Color.WHITE;
        this.filled = filled;
    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled,Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
        this.filled = filled;
    }

    public Mesh toMesh() {
        return new Mesh(
                java.util.List.of(
                        this
                )
        );
    }

}
