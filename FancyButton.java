/**
 * Main - Dean Wilcox 4 December 2023 
 */

import java.awt.Color;
import  java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class FancyButton extends JButton {
    
  
    public FancyButton() {
        super();
    
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
            }
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
    };
    addMouseListener(mouseAdapter);
    
    }

}