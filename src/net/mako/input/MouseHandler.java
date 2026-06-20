package net.mako.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {

    public Runnable onMouseClicked;
    public Runnable onMousePressed;
    public Runnable onMouseReleased;
    public Runnable onMouseEntered;
    public Runnable onMouseExited;

    public Runnable onMouseDragged;
    public Runnable onMouseMoved;

    @Override
    public void mouseClicked(MouseEvent e) {
        if(onMouseClicked != null) {
            onMouseClicked.run();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(onMousePressed != null) {
            onMousePressed.run();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(onMouseReleased != null) {
            onMouseReleased.run();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(onMouseEntered != null) {
            onMouseEntered.run();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(onMouseExited != null) {
            onMouseExited.run();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(onMouseDragged != null) {
            onMouseDragged.run();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(onMouseMoved != null) {
            onMouseMoved.run();
        }
    }
}
