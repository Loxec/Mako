# Mako 2D Game Engine (Java)

A lightweight 2D game engine built from scratch in Java.  
This project is the foundation of a custom game, created as a learning base for future game development
---

## Current Features

## RENDERING

#### Geometry System
- Vertex (2D point structure)
- Triangle primitive
- Rectangle (composed of two triangles)
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
- Consumers 
- Custom consumers like backward forward etc

## UTILITIES

#### Triangle Transform
- you can rotate your triangle
- you can translate your triangle
- you can scaleUp/Down your triangle

## MSH
- a new file format to create easy model in 2d
- .msh loader
- .msh converter into mesh

---

## Project Structure

```

net.mako.tdim.rendering
├── RenderPanel   → Main rendering surface
├── Mesh          → Collection of triangles
├── Triangle      → Primitive shape
├── Vertex        → 2D coordinate
├── MRectangle    → Helper shape (2 triangles) 
├── Utils         → Provide usefull function and variables
├── Sprite        → You can now load images

````

```

net.mako.tdim.rendering.msh
├── FormatType     → Used to sort the files and their formating
├── ModelParser    → What help you at loading your model
├── ModelConverter → More for internal purpose

````

```

net.mako.tdim.technicalities.input
├── KeyHandler    → The keyboard listener
├── MouseHandler  → The mouse listener

```

```

net.mako.tdim.rendering.utilities

├── TriangleTransform → the method that make your life simpler

```

---

## Example usage

```java
import net.mako.tdim.rendering.components.Mesh;
import net.mako.tdim.rendering.components.Sprite;
import net.mako.tdim.rendering.components.Triangle;
import net.mako.tdim.rendering.components.Vertex;
import net.mako.tdim.technicalities.input.KeyHandler;
import net.mako.tdim.rendering.msh.ModelParser;
import net.mako.tdim.rendering.*;
import net.mako.tdim.rendering.utilities.TriangleTransform;

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
        Triangle t1 = new Triangle(
                new Vertex(100, 100),
                new Vertex(150, 200),
                new Vertex(200, 100),
                true,
                "move"
        );

        Triangle t2 = new Triangle(
                new Vertex(100, 100),
                new Vertex(150, 200),
                new Vertex(200, 100),
                true,
                "rotate",
                new Color(255, 0, 0)
        );
        Triangle t3 = new Triangle(
                new Vertex(100, 100),
                new Vertex(150, 200),
                new Vertex(200, 100),
                true,
                "scale"
        );

        //loading a model for each different format
        ModelParser basic_t_model = new ModelParser("/assets/exemple_bt.msh");
        ModelParser indexed_t_model = new ModelParser("/assets/exemple_it.msh");
        ModelParser multimesh_model = new ModelParser("/assets/exemple_mt.msh");
        ModelParser indexed_multimesh_model = new ModelParser("/assets/exemple_mit.msh");

        //renderPanel only accept mesh which is a list of triangle , so we can directly do Triangle.toMesh() to save space
        panel.addMeshToPanel(t1.toMesh());
        panel.addMeshToPanel(t3.toMesh());
        panel.addMeshToPanel(t2.toMesh());

        // adding the models and not the meshes                                 we need to place our model somewhere
        panel.addModelToPanel(basic_t_model.getModel(basic_t_model.getFormatType()),0,0);
        panel.addModelToPanel(indexed_t_model.getModel(indexed_t_model.getFormatType()),0,50);
        panel.addModelToPanel(multimesh_model.getModel(multimesh_model.getFormatType()),300,0);
        panel.addModelToPanel(indexed_multimesh_model.getModel(indexed_multimesh_model.getFormatType()),0,0);

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


        // assing the consumers before the loop ;)
        int speed = 5;

        kh.onForwardPressed = (e -> {
            sprite.setPosition(sprite.getX(), sprite.getY() - speed);
        });
        kh.onBackwardPressed = (e -> {
            sprite.setPosition(sprite.getX(), sprite.getY() + speed);
        });
        kh.onRightPressed = (e -> {
            sprite.setPosition(sprite.getX() + speed, sprite.getY());
        });
        kh.onLeftPressed = (e -> {
            sprite.setPosition(sprite.getX() - speed, sprite.getY());
        });

        //it's important to refresh the panel
        new Thread(() -> {
            while (true) {

                sprite.setRotation(sprite.getRotation() + 1.0);

                //we get all the meshes
                for (Mesh mesh : panel.getMeshes()) {
                    //then the triangles
                    for (Triangle tt : mesh.triangles) {
                        // and then we search our wanted triangle
                        if (tt.getTag().equals("move")) {
                            TriangleTransform.translate(1, 0, tt);
                        } else if (tt.getTag().equals("rotate")) {
                            TriangleTransform.rotate(1, tt);
                        } else if (tt.getTag().equals("scale")) {
                            TriangleTransform.scaleUp(1.0025, tt);
                        }
                    }
                }

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
