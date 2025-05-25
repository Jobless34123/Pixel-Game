package main;

import entity.*;
import tile.TileMaker;
import building.BuildHandler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

  //dimensions for the window (in pixels and tiles)
  final int originalTileSize = 32; // tile
  final int scale = 3;
  public final int TileSize = originalTileSize * scale;
  public final int maxScreenHeight = 10; // in tiles
  public final int maxScreenWidth = 10;  // in tiles
  public final int GAME_WIDTH = TileSize * maxScreenWidth;
  public final int GAME_HEIGHT = TileSize * maxScreenHeight;

  public final int MAP_WIDTH = 50;  // map size in tiles
  public final int MAP_HEIGHT = 50;
  public final int MAX_MAP_WIDTH = MAP_WIDTH * TileSize;   // map size in pixels
  public final int MAX_MAP_HEIGHT = MAP_HEIGHT * TileSize;

  KeyHandler keyH = new KeyHandler();
  public Thread gameThread;
  public Image image;
  public Graphics graphics;

  public Player player = new Player(this, keyH);
  public BuildHandler buildH = new BuildHandler(this, keyH);
  public TileMaker tileM = new TileMaker(this, keyH);

  //dedicated collision checker instance :))
  CollisionChecker collisionChecker = new CollisionChecker();

  public GamePanel(){
    this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    this.setDoubleBuffered(true);
    this.setFocusable(true);
    this.addKeyListener(keyH);


    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        // Additional mouse functionality can be added here.
      }
    });

    //start the game thread.
    gameThread = new Thread(this);
    gameThread.start();
  }

  //overriding the paint method to use double buffering.
  public void paint(Graphics g){
    image = createImage(GAME_WIDTH, GAME_HEIGHT);
    graphics = image.getGraphics();
    g.drawImage(image, 0, 0, this);
    Graphics2D g2 = (Graphics2D) g;
    draw(g2);
    g2.dispose();
  }

  //draws game assets.
  public void draw(Graphics2D g2){
    tileM.draw(g2);
    player.draw(g2);
  }

  //central collision checking method.
  public void checkCollision(){
    //example->> enforce map boundaries.
    if(player.worldX < 0) {
      player.worldX = 0;
    }
    if(player.worldY < 0) {
      player.worldY = 0;
    }
    if(player.worldX > MAX_MAP_WIDTH - TileSize) {
      player.worldX = MAX_MAP_WIDTH - TileSize;
    }
    if(player.worldY > MAX_MAP_HEIGHT - TileSize) {
      player.worldY = MAX_MAP_HEIGHT - TileSize;
    }

    // update player bounds after corrections.
    player.setBounds(player.worldX, player.worldY, TileSize, TileSize);

    //additional static or dynamic obstacles could be added into an obstacles list.
    //t hen loop through:
        /*
        for (Entity obstacle : obstacles) {
            if (collisionChecker.checkCollision(player, obstacle)) {
                // Collision response (e.g., revert player.position)
            }
        }
        this should work..
        */
  }

  public void update(){
    player.update();
    tileM.update();

    //perform collision checks after moving objects.
    checkCollision();

    //trigger building functionality if the key was pressed.
    if (keyH.enterPressed) {
      buildH.build();
      keyH.enterPressed = false;
    }
  }

  public void run(){
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    long now;

    while (true) {
      now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;

      //update and repaint only after the required time has passed.
      if (delta >= 1) {
        update();
        repaint();
        delta--;
      }
    }
  }

  //KeyListener methods
  public void keyPressed(KeyEvent e){

  }

  public void keyReleased(KeyEvent e){

  }

  public void keyTyped(KeyEvent e){
    //unused. maybe will bve used later. for some prompt maybe idk
  }
}
