package main;

import entity.*;
import tile.TileMaker;
import building.BuildHandler;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

  //dimensions for the window (in pixels and tiles)
  final int originalTileSize = 32; // tile
  public final int scale = 3;                                                  // changed to public
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
  public ArrayList<Tree> trees;

  //dedicated collision checker instance :))
  public CollisionChecker collisionChecker = new CollisionChecker(this);        //CHANGE:  this added in bracket

  public GamePanel(){
    this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    this.setDoubleBuffered(true);
    this.setFocusable(true);
    this.addKeyListener(keyH);


    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        // additional mouse functionality can be added here.
      }
    });

    //initialize your tile maker (which loads the map from a text file);   TREE UPDATE
    tileM = new TileMaker(this, keyH);

    //generate trees based on the tile map (only on land tiles: tile number 1).    TREE UPDATE
    generateTrees();

    //Continue initializing other objects      TREE UPDATE
    player = new Player(this, keyH);
    buildH = new BuildHandler(this, keyH);

    //start the game thread.
    gameThread = new Thread(this);
    gameThread.start();
  }


  public void generateTrees() {        // TREE UPDATE
    trees = new ArrayList<>();
    Random rand = new Random();

    //loop through every tile in the map.
    for (int row = 0; row < MAP_HEIGHT; row++) {
      for (int col = 0; col < MAP_WIDTH; col++) {
        //  check if the tile is "land" (here assumed to be tile number 1).
        if (tileM.mapTileNum[row][col] == 1) {
          // adjust the probability as needed (try 10% chance is 0.1 (initially)).
          if (rand.nextDouble() < 0.07) {
            int tileWorldX = col * TileSize;
            int tileWorldY = row * TileSize;

            //calculate random offset inside the tile ensuring the tree stays fully within.
            int maxOffsetX = TileSize - TileSize;  //if you want tree to fill the tile, offset can be zero. i think the tress become too small for offset.
            int maxOffsetY = TileSize - TileSize;
            int offsetX = 0;  //when using the full tile, you might not want an offset
            int offsetY = 0;

            int worldX = tileWorldX + offsetX;
            int worldY = tileWorldY + offsetY;

            //create a rectangle for candidate tree placement, pretty much checks if there is tree here already so trees dont over lap
            Rectangle candidate = new Rectangle(worldX, worldY, TileSize, TileSize);
            boolean overlaps = false;
            for (Tree t : trees) {
              if (t.getBounds().intersects(candidate)) {
                overlaps = true;
                break;
              }
            }
            if (!overlaps) {
              trees.add(new Tree(worldX, worldY, TileSize));
            }
          }
        }
      }
    }
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

    //draw trees (these are drawn before the player so that the player can appear on top if desired)    TREE UPDATE
    for (Tree tree : trees) {
      tree.draw(g2, this);
    }

    player.draw(g2);
  }

  //central collision checking method.
  public void checkCollision(){
    //example->> enforce map boundaries.
    if(player.worldX < GAME_WIDTH) {
      player.worldX = GAME_WIDTH;
    }
    if(player.worldY < GAME_HEIGHT) {
      player.worldY = GAME_HEIGHT;
    }
    if(player.worldX > MAX_MAP_WIDTH - GAME_WIDTH) {
      player.worldX = MAX_MAP_WIDTH - GAME_WIDTH;
    }
    if(player.worldY > MAX_MAP_HEIGHT - GAME_HEIGHT) {
      player.worldY = MAX_MAP_HEIGHT - GAME_HEIGHT;
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
