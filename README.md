# Mako 2D Game Engine (Java)

A lightweight 2D game engine built from scratch in Java.  
This project is the foundation of a custom game, created as a learning base for future game development
---

## Current Features

## RENDERING

#### Geometry System
- Vertex (2D point structure)
- Triangle primitive
- Square (composed of two triangles)
- Mesh grouping system

#### Rendering
- Custom RenderPanel based on JPanel
- Triangle rendering using Path2D
- Filled polygon support
- Color support per triangle
- Continuous render loop (basic game loop)

#### Engine Structure
- Simple mesh-based scene system
- Manual repaint loop
- Swing-based window system

## INPUT

#### Keyboard
- Simple keyListener
- Runnables 
- Custom renables like backward forward etc

---

## Project Structure

```

net.mako.rendering
├── RenderPanel   → Main rendering surface
├── Mesh          → Collection of triangles
├── Triangle      → Primitive shape
├── Vertex        → 2D coordinate
├── MRectangle    → Helper shape (2 triangles) 
├── Utils         → Provide usefull function and variables
├── Sprite        → You can now load images

````

```

net.mako.input
├── KeyHandler    → The keyboard listener
├── MouseHandler  → The mouse listener

```

---

## Example usage

```java
import net.mako.input.KeyHandler;
import net.mako.rendering.*;

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
        Sprite sprite = new Sprite("/assets/skybox_pearl.png", 300, 300);

        //just do whatever you want with the scale and position
        int scale_multiplier = 1;
        sprite.setScaleX(sprite.getScaleX() * scale_multiplier);
        sprite.setScaleY(sprite.getScaleY() * scale_multiplier);

        sprite.setRotation(45.0);

        sprite.setPosition(350, 350);

        //and then add the sprite ready
        panel.addSpriteToPanel(sprite);

        // used to control the sprite
        KeyHandler kh = new KeyHandler();

        // add this to the panel
        window.addKeyListener(kh);


        // assing the runnables before the loop ;)
        int speed = 5;

        kh.onForwardPressed = (() -> {
            sprite.setPosition(sprite.getX(),sprite.getY()-speed);
        });
        kh.onBackwardPressed = (() -> {
            sprite.setPosition(sprite.getX(),sprite.getY()+speed);
        });
        kh.onRightPressed = (() -> {
            sprite.setPosition(sprite.getX()+speed,sprite.getY());
        });
        kh.onLeftPressed = (() -> {
            sprite.setPosition(sprite.getX()-speed,sprite.getY());
        });

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

## Status

Early prototype — not optimized.
Made for learning and experimentation.

---

## Author

Made with Java and curiosity.
