package net.mako.tdim.rendering.components;

import java.util.ArrayList;
import java.util.List;

public class Mesh {

    public List<Triangle> triangles;

    public Mesh(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public Model getModel() {
        List<Mesh> meshes = new ArrayList<>();
        meshes.add(this);
        return new Model(meshes);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }

}
