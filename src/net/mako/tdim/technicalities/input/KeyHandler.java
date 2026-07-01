package net.mako.tdim.technicalities.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class KeyHandler implements KeyListener {

    public Consumer onKeyPressed;
    public Consumer onKeyReleased;
    public Consumer onKeyTyped;

    public Consumer onForwardPressed;
    public Consumer onBackwardPressed;
    public Consumer onLeftPressed;
    public Consumer onRightPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        if(onKeyTyped != null) {
            onKeyTyped.accept(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(onKeyPressed != null) {
            onKeyPressed.accept(e);
        }

        int code = e.getKeyCode();

        // ARROWS
        if(code == KeyEvent.VK_LEFT && onLeftPressed != null) {
            onLeftPressed.accept(e);
        }else if(code == KeyEvent.VK_RIGHT && onRightPressed != null) {
            onRightPressed.accept(e);
        }else if(code == KeyEvent.VK_UP && onForwardPressed != null) {
            onForwardPressed.accept(e);
        }else if(code == KeyEvent.VK_DOWN && onBackwardPressed != null) {
            onBackwardPressed.accept(e);
        }

        // QWERTY
        if(code == KeyEvent.VK_A && onLeftPressed != null) {
            onLeftPressed.accept(e);
        }else if(code == KeyEvent.VK_D && onRightPressed != null) {
            onRightPressed.accept(e);
        }else if(code == KeyEvent.VK_W && onForwardPressed != null) {
            onForwardPressed.accept(e);
        }else if(code == KeyEvent.VK_S && onBackwardPressed != null) {
            onBackwardPressed.accept(e);
        }

        // AZERTY

        if(code == KeyEvent.VK_Q && onLeftPressed != null) {
            onLeftPressed.accept(e);
        }else if(code == KeyEvent.VK_D && onRightPressed != null) {
            onRightPressed.accept(e);
        }else if(code == KeyEvent.VK_Z && onForwardPressed != null) {
            onForwardPressed.accept(e);
        }else if(code == KeyEvent.VK_S && onBackwardPressed != null) {
            onBackwardPressed.accept(e);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(onKeyReleased != null) {
            onKeyReleased.accept(e);
        }
    }
}
