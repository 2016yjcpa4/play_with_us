package org.game;
 
import javax.swing.JFrame;
 
public class Main {
    
    public static void main(String[] args) { 
        
        JFrame f = new JFrame();
        f.getContentPane().add(new Game().getCanvas()); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1024, 768);
        //f.setResizable(false);
        f.setVisible(true); 
    }

}
