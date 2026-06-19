package net.mako.rendering;

import javax.swing.*;
import java.awt.*;

public class Utils {
    
    public static JFrame getBasicJFrame(String title) {
        // WINDOW
        Dimension screenSize = new Dimension(800,600);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(screenSize);
        frame.setSize(screenSize);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        
        return frame;
    }
    
}
