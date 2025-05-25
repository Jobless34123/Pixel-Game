package main;
/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called

Child of JPanel because JPanel contains methods for drawing to the screen

Implements KeyListener interface to listen for keyboard input

Implements Runnable interface to use "threading" - let the game do two things at once

*/


import entity.*;
import tile.TileMaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import building.BuildHandler;

public class GamePanel extends JPanel implements Runnable, KeyListener{

  //dimensions of window

  final int originalTileSize = 32;//32x32 tile
  final int scale = 3;
  public final int TileSize = originalTileSize*scale;
  public final int maxScreenHeight = 10;//in terms of tiles
  public final int maxScreenWidth = 10;//in terms of tiles
  public final int GAME_WIDTH = TileSize*maxScreenWidth;
  public final int GAME_HEIGHT = TileSize*maxScreenHeight;

  public final int MAP_WIDTH=50;//map size in tiles
  public final int MAP_HEIGHT=50;
  public final int MAX_MAP_WIDTH=MAP_WIDTH*TileSize; //map size in pixels
  public final int MAX_MAP_HEIGHT=MAP_HEIGHT*TileSize;

  KeyHandler keyH = new KeyHandler();  
  public Thread gameThread;
  public Image image;
  public Graphics graphics;
  public Player player = new Player(this, keyH);
  public BuildHandler buildH = new BuildHandler(this, keyH);
  
  public TileMaker tileM = new TileMaker(this, keyH);

  public GamePanel(){
    
    this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    this.setDoubleBuffered(true);

    this.setFocusable(true); //make everything in this class appear on the screen
    this.addKeyListener(keyH); //start listening for keyboard input


    //add the MousePressed method from the MouseAdapter - by doing this we can listen for mouse input. We do this differently from the KeyListener because MouseAdapter has SEVEN mandatory methods - we only need one of them, and we don't want to make 6 empty methods
    addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			}
		});

    //make this class run at the same time as other classes (without this each class would "pause" while another class runs). By using threading we can remove lag, and also allows us to do features like display timers in real time!
    gameThread = new Thread(this);
    gameThread.start();
  }

  //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
  public void paint(Graphics g){
    //we are using "double buffering here" - if we draw images directly onto the screen, it takes time and the human eye can actually notice flashes of lag as each pixel on the screen is drawn one at a time. Instead, we are going to draw images OFF the screen, then simply move the image on screen as needed. 
    image = createImage(GAME_WIDTH, GAME_HEIGHT); //draw off screen
    graphics = image.getGraphics();
    g.drawImage(image, 0, 0, this); //move the image on the screen
    Graphics2D g2 = (Graphics2D)g; 
    draw(g2);
    g2.dispose();

  }

  //call the draw methods in each class to update positions as things move
  public void draw(Graphics2D g2){
    tileM.draw(g2);
    player.draw(g2);
  }

  

  //handles all collision detection and responds accordingly
  public void checkCollision(){
  }

  public void update(){    
    player.update();
    tileM.update();
    if (keyH.enterPressed) {
      buildH.build();
      keyH.enterPressed = false;
    }
  }
  //run() method is what makes the game continue running without end. It calls other methods to move objects,  check for collision, and update the screen
  public void run(){
    //the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen. 
    long lastTime = System.nanoTime();
    double amountOfTicks = 60;
    double ns = 1000000000/amountOfTicks;
    double delta = 0;
    long now;

    while(true){ //this is the infinite game loop
      now = System.nanoTime();
      delta = delta + (now-lastTime)/ns;
      lastTime = now;

      //only move objects around and update screen if enough time has passed
      if(delta >= 1){
        update();
        repaint();
        delta--;
      }
    }
  }

  //if a key is pressed, we'll send it over to the PlayerBall class for processing
  public void keyPressed(KeyEvent e){
  }

  //if a key is released, we'll send it over to the PlayerBall class for processing
  public void keyReleased(KeyEvent e){
  }

  //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
  public void keyTyped(KeyEvent e){

  }
}