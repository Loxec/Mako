package net.mako.tdim.rendering.components;

import java.awt.*;

public class Rectangle {

    private Vertex v1, v2, v3, v4;
    public final Triangle TRIANGLE1;
    public final Triangle TRIANGLE2;
    boolean filled;
    public Color color;

    public Rectangle(Vertex v1, Vertex v4, boolean filled){
        this.v1=v1;
        this.v2= new Vertex(v4.getX(),v1.getY());
        this.v3= new Vertex(v1.getX(),v4.getY());
        this.v4=v4;
        this.TRIANGLE1 = new Triangle(this.v1,this.v2,this.v3,filled);
        this.TRIANGLE2 = new Triangle(this.v2,this.v3,this.v4,filled);
        this.filled=filled;
        this.color=Color.WHITE;
    };

    public Rectangle(Vertex v1, Vertex v4, boolean filled, Color color){
        this.v1=v1;
        this.v2= new Vertex(v4.getX(),v1.getY());
        this.v3= new Vertex(v1.getX(),v4.getY());
        this.v4=v4;
        this.TRIANGLE1 = new Triangle(this.v1,this.v2,this.v3,filled,color);
        this.TRIANGLE2 = new Triangle(this.v2,this.v3,this.v4,filled,color);
        this.filled=filled;
        this.color=color;
    };

    public Mesh toMesh() {
        return new Mesh(
                java.util.List.of(
                        TRIANGLE1,
                        TRIANGLE2
                )
        );
    }

}
