/**
 * Main - Dean Wilcox 4 December 2023 
 */

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PuzzleGame extends JFrame{

    private final int WIDTH = 500;
    private int HEIGHT = 500; //going to change later
    JPanel panel;
    private final int COLUMNS = 3, ROWS = 4;
    private ArrayList<FancyButton> buttonList;
    private ArrayList<FancyButton> buttonSolution = new ArrayList<FancyButton>();
    private BufferedImage imageSource;
    private BufferedImage imageResized;

    public PuzzleGame() {
        super("Puzzle Game");

        panel = new JPanel(); //sets a new instance of the JPanel
        panel.setLayout(new GridLayout(ROWS, COLUMNS));
        add(panel);

        try {
            imageSource = loadImage();
            int sourceWidth = imageSource.getWidth();
            int sourceHeight = imageSource.getHeight();
            //gets the aspect ratio of the source image
            double aspectRatio = (double)sourceHeight / sourceWidth;
            HEIGHT = (int)(aspectRatio * WIDTH);

            imageResized = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = imageResized.createGraphics();
            graphics.drawImage(imageSource, 0, 0, WIDTH, HEIGHT, null);
            graphics.dispose();

        }catch(IOException e){
            JOptionPane.showMessageDialog(rootPane, "Error loading the image");
        }
    
        buttonList = new ArrayList<FancyButton>();

        for (int i = 0; i < COLUMNS * ROWS; i++){

            int row = i / COLUMNS;
            int column = i % COLUMNS;

            Image imageSlice = createImage(new FilteredImageSource(imageResized.getSource(), 
            new CropImageFilter(column* WIDTH/COLUMNS, 
            row *HEIGHT/ROWS, WIDTH/COLUMNS, HEIGHT/ROWS)));

            FancyButton btn = new FancyButton();
            btn.addActionListener(e -> MyClickEventHandler(e));

            if(i == COLUMNS * ROWS -1) //the last button
            {
                btn.setBorderPainted(false); //set no border
                btn.setContentAreaFilled(false); //set no fill
            } else {
                btn.setIcon(new ImageIcon(imageSlice));
            }

            buttonSolution.add(btn);
            buttonList.add(btn);
    }

        Collections.shuffle(buttonList);
        for(var button : buttonList){
            panel.add(button);
        }

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocation(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void MyClickEventHandler(ActionEvent e) {
        FancyButton btnClicked = (FancyButton)e.getSource();

        int i = buttonList.indexOf(btnClicked);
        int column = i % COLUMNS;
        int row = i /COLUMNS;

        int emptyIndex = -1;
        // this is going to find our empty button
        for(int j = 0; j < buttonList.size(); j++){
            if(buttonList.get(j).getIcon() == null){
                emptyIndex = j;
            }
        }

        int emptyColumn = emptyIndex % COLUMNS;
        int emptyRow = emptyIndex / COLUMNS;

      
        if((emptyRow == row && Math.abs(column - emptyColumn) == 1 ||
        emptyColumn == column && Math.abs(row - emptyRow) == 1)){
            Collections.swap(buttonList, i, emptyIndex);
            updateButtons();
        }

        if(buttonSolution.equals(buttonList)){
            JOptionPane.showMessageDialog(btnClicked, "Well done, you won!");
        }

    }

    public void updateButtons() {
        panel.removeAll();
        for(var btn: buttonList){
            panel.add(btn);
        }
        panel.validate();
    }


    private BufferedImage loadImage() throws IOException {
        return ImageIO.read(new File("donkey.jpg"));
    }
}