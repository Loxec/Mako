package net.mako.rendering;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Triangle {

    public Vertex v1;
    public Vertex v2;
    public Vertex v3;
    public List<Vertex> vertexes;
    public Color color;
    public boolean filled;
    private String tag;
    public Runnable onTagChanged;

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = Color.WHITE;
        this.filled = filled;
        this.tag = "Triangle";
        this.vertexes = new ArrayList<Vertex>();
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled,Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
        this.filled = filled;
        this.tag = "Triangle";
        this.vertexes = new ArrayList<Vertex>();
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled,String tag) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = Color.WHITE;
        this.filled = filled;
        this.tag = tag;
        this.vertexes = new ArrayList<Vertex>();
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled,Color color,String tag) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
        this.filled = filled;
        this.tag = tag;
        this.vertexes = new ArrayList<Vertex>();
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3,boolean filled,String tag,Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
        this.filled = filled;
        this.tag = tag;
        this.vertexes = new ArrayList<Vertex>();
        vertexes.add(v1);
        vertexes.add(v2);
        vertexes.add(v3);
    }

    public Mesh toMesh() {
        return new Mesh(
                java.util.List.of(
                        this
                )
        );
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
        if(onTagChanged != null) {
            onTagChanged.run();
        }
    }
}
