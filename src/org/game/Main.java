package org.game;
 
import javax.swing.JFrame;
 
public class Main {
    
    public static void main(String[] args) { 
        
        Game g = new Game(); 
        
        JFrame f = new JFrame();
        f.getContentPane().add(g.getCanvas()); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1024, 768);
        //f.setResizable(false);
        f.setVisible(true); 
        
        g.start();
    }

}
