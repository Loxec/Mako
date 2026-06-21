package net.mako.rendering;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class RenderPanel extends JPanel {

    private static List<Mesh> meshes = new ArrayList<>();
    private static List<Sprite> sprites = new ArrayList<>();

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

    private static void drawMeshes(Graphics2D g2d) {
        for(Mesh mesh : meshes) {
            for (Triangle t : mesh.triangles) {
                Path2D path = new Path2D.Double();
                path.moveTo(t.v1.x, t.v1.y);
                path.lineTo(t.v2.x, t.v2.y);
                path.lineTo(t.v3.x, t.v3.y);
                path.closePath();
                g2d.draw(path);

                if (t.filled) {
                    g2d.setColor(t.color);
                    g2d.fill(path);
                }
            }
        }
    }

    private static void drawSprites(Graphics2D g2d) {
        for(Sprite sprite : sprites) {
            if(sprite.getImage() != null) {
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
