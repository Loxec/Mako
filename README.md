# Mako 2D Render Engine (Java)

A lightweight 2D rendering engine built from scratch in Java using Swing.  
This project is the foundation of a custom game/graphics engine, created as a learning base for future game development 
---

## Overview

Mako 2D Render Engine is a minimal rendering system that handles:

- Vertex-based geometry
- Triangle rasterization
- Mesh composition
- Basic scene rendering inside a Swing panel
- Sprites

The goal is to understand how rendering pipelines work at a low level without relying on external game engines.

---

## Current Features

### Geometry System
- Vertex (2D point structure)
- Triangle primitive
- Square (composed of two triangles)
- Mesh grouping system

### Rendering
- Custom RenderPanel based on JPanel
- Triangle rendering using Path2D
- Filled polygon support
- Color support per triangle
- Continuous render loop (basic game loop)

### Engine Structure
- Simple mesh-based scene system
- Manual repaint loop
- Swing-based window system

---

## Project Structure

```

net.mako
├── RenderPanel   → Main rendering surface
├── Mesh          → Collection of triangles
├── Triangle      → Primitive shape
├── Vertex        → 2D coordinate
├── MRectangle    → Helper shape (2 triangles) 
├── Utils         → Provide usefull function and variables
├── Sprite        → You can now load images

````

---

## Example Usage

```java
import net.mako.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        //gives a JFrame with some information
        JFrame window = Utils.getBasicJFrame("Mako renderer");
        window.setLayout(new BorderLayout());

        //add the renderer panel which will contain every meshes
        RenderPanel panel = new RenderPanel();
        window.add(panel, BorderLayout.CENTER);

        window.setVisible(true);

        // add a new triangle made of 3 Vertex and a boolean that represent if we need to fill it or no (we can also specify color)
        Triangle t = new Triangle(
                new Vertex(100, 100),
                new Vertex(150, 200),
                new Vertex(200, 100),
                true
        );

        //renderPanel only accept mesh which is a list of triangle , so we can directly do Triangle.toMesh() to save space
        panel.addMeshToPanel(t.toMesh());

        //load a new sprite
        Sprite sprite = new Sprite("/assets/skybox_pearl.png",300,300);

        //just do whatever you want with the scale and position
        int scale_multiplier = 1;
        sprite.setScaleX(sprite.getScaleX() * scale_multiplier);
        sprite.setScaleY(sprite.getScaleY() * scale_multiplier);

        sprite.setRotation(45.0);

        sprite.setPosition(350,350);

        //and then add the sprite ready
        panel.addSpriteToPanel(sprite);


        //it's important to refresh the panel
        new Thread(() -> {
            while (true) {

                sprite.setRotation(sprite.getRotation() + 1.0);

                panel.repaint();
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
````

---

## Why this project?

* Learn how rendering pipelines work
* Understand geometry and rasterization
* Build a foundation for a custom game engine
* Prepare future transition toward 3D engine development

---

## Status

Early prototype — not optimized.
Made for learning and experimentation.

---

## Author

Made with Java and curiosity.
