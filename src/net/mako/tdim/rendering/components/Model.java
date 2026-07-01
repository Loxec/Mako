package net.mako.tdim.rendering.components;

import java.util.List;

public class Model {

    public List<Mesh> meshes;

    public Model(List<Mesh> mesh) {
        this.meshes = mesh;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }

}
