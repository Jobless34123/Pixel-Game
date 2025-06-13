import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame{

  GamePanel panel;

  public GameFrame(){
    panel = new GamePanel(); //run GamePanel constructor
    this.add(panel);
    this.setTitle("HORDE FROM HELL"); //set title for frame
    this.setBackground(Color.BLACK);
    this.setUndecorated(true);              // Remove window borders and title bar
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximize to full screen
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X button will stop program execution
    this.setVisible(true); //makes window visible to user
  }
  
}