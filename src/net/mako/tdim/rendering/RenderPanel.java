package net.mako.tdim.rendering;

import net.mako.tdim.rendering.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderPanel extends JPanel {

    private static final List<Mesh> meshes = Collections.synchronizedList(new ArrayList<>());
    private static final List<Sprite> sprites = Collections.synchronizedList(new ArrayList<>());

    public RenderPanel() {

        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setVisible(true);

    }

    public void addMeshToPanel(Mesh mesh) {
        meshes.add(mesh);
    }
    public void addSpriteToPanel(Sprite sprite) {
        sprites.add(sprite);
    }
    public void addModelToPanel(Model model,double cx,double cy) {
        for(Mesh mesh : model.meshes) {
            for(Triangle t : mesh.triangles){
                for(Vertex v : t.vertexes){
                    Vertex newV = new Vertex(cx+v.getX(),cy+v.getY());
                    t.setNextVertex(newV);
                }
            }

            meshes.add(mesh);
        }
    }

    private static void drawMeshes(Graphics2D g2d) {
        synchronized (meshes) {
            for (Mesh mesh : meshes) {
                for (Triangle t : mesh.triangles) {
                    g2d.setColor(t.color);
                    Path2D path = new Path2D.Double();
                    path.moveTo(t.v1.getX(), t.v1.getY());
                    path.lineTo(t.v2.getX(), t.v2.getY());
                    path.lineTo(t.v3.getX(), t.v3.getY());
                    path.closePath();
                    g2d.draw(path);

                    if (t.filled) {
                        g2d.fill(path);
                    }
                }
            }
        }
    }

    private static void drawSprites(Graphics2D g2d) {
        synchronized (sprites) {
            for (Sprite sprite : sprites) {
                if (sprite.getImage() != null) {
                    AffineTransform at = new AffineTransform();

                    at.translate(sprite.getX(), sprite.getY());

                    at.rotate(
                            Math.toRadians(sprite.getRotation()),
                            sprite.getScaleX() / 2,
                            sprite.getScaleY() / 2
                    );

                    at.scale(
                            sprite.getScaleX() / sprite.getImage().getWidth(),
                            sprite.getScaleY() / sprite.getImage().getHeight()
                    );

                    g2d.drawImage(sprite.getImage(), at, null);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawMeshes(g2d);
        drawSprites(g2d);
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }
    public List<Sprite> getSprites() {
        return sprites;
    }

}
