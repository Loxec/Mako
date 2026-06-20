package net.mako.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public Runnable onKeyPressed;
    public Runnable onKeyReleased;
    public Runnable onKeyTyped;

    public Runnable onForwardPressed;
    public Runnable onBackwardPressed;
    public Runnable onLeftPressed;
    public Runnable onRightPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        if(onKeyTyped != null) {
            onKeyTyped.run();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(onKeyPressed != null) {
            onKeyPressed.run();
        }

        int code = e.getKeyCode();

        // ARROWS
        if(code == KeyEvent.VK_LEFT && onLeftPressed != null) {
            onLeftPressed.run();
        }else if(code == KeyEvent.VK_RIGHT && onRightPressed != null) {
            onRightPressed.run();
        }else if(code == KeyEvent.VK_UP && onForwardPressed != null) {
            onForwardPressed.run();
        }else if(code == KeyEvent.VK_DOWN && onBackwardPressed != null) {
            onBackwardPressed.run();
        }

        // QWERTY
        if(code == KeyEvent.VK_A && onLeftPressed != null) {
            onLeftPressed.run();
        }else if(code == KeyEvent.VK_D && onRightPressed != null) {
            onRightPressed.run();
        }else if(code == KeyEvent.VK_W && onForwardPressed != null) {
            onForwardPressed.run();
        }else if(code == KeyEvent.VK_S && onBackwardPressed != null) {
            onBackwardPressed.run();
        }

        // AZERTY

        if(code == KeyEvent.VK_Q && onLeftPressed != null) {
            onLeftPressed.run();
        }else if(code == KeyEvent.VK_D && onRightPressed != null) {
            onRightPressed.run();
        }else if(code == KeyEvent.VK_Z && onForwardPressed != null) {
            onForwardPressed.run();
        }else if(code == KeyEvent.VK_S && onBackwardPressed != null) {
            onBackwardPressed.run();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(onKeyReleased != null) {
            onKeyReleased.run();
        }
    }
}
