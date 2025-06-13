

/*
Core game loop and rendering panel for the 2D tile-based game.
Manages drawing, updates, input handling, collision enforcement,
and delegates to Player, TileMaker, and BuildHandler.
Implements Runnable for a fixed-tick game loop at around 60 FPS.
 */
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener {

  //dimensions for the window (in pixels and tiles)
  final int originalTileSize = 32; // tile
  public final int scale = 3;
  public final int TileSize = originalTileSize * scale;
  public final int maxScreenHeight = 10; // in tiles
  public final int maxScreenWidth = 10;  // in tiles
  public final int GAME_WIDTH = TileSize * maxScreenWidth;
  public final int GAME_HEIGHT = TileSize * maxScreenHeight;
  public PathFinder pathFinder = new PathFinder(this);
  public ArrayList<Wall> walls;
  public ArrayList<Floor> floors;

  public boolean waveInProgress = true;
  private long nextWaveStartTime = 0;
  private final int WAVE_DELAY_MS = 25000;   // 25 seconds delay between waves

  public boolean inventoryOpen = false;
  public String GAME_STATE="tut";
  //game states; "paused", "play", "dead", "tut" (tutorial)
  public UI ui = new UI(this);

  public final int MAP_WIDTH = 80;  // map size in tiles
  public final int MAP_HEIGHT = 80;
  public final int MAX_MAP_WIDTH = MAP_WIDTH * TileSize;   // map size in pixels
  public final int MAX_MAP_HEIGHT = MAP_HEIGHT * TileSize;

  KeyHandler keyH = new KeyHandler(this);
  public Thread gameThread;
  public Image image;
  public Graphics graphics;

  public Player player = new Player(this, keyH);
  public BuildHandler buildH = new BuildHandler(this, keyH);
  public TileMaker tileM = new TileMaker(this, keyH);
  public ArrayList<Tree> trees;
  public ArrayList<Zombie> zombies;
  public int wave=1;
  public int waveSurvived=1;
  public final int TOTAL_START_ZOMBIES=10;
  public int totalZombies=TOTAL_START_ZOMBIES;
  public int aliveZombies=totalZombies;
  public int zombiesKilledTotal=0;
  public int zombiesKilledNow=0;
  public int zombiesLeft = 0;

  BufferedImage offscreen = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
  Graphics2D gOff = offscreen.createGraphics();


  //dedicated collision checker instance :))
  public CollisionChecker collisionChecker = new CollisionChecker(this);        //CHANGE:  this added in bracket

  public GamePanel() {
    this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    this.setDoubleBuffered(true);
    this.setFocusable(true);
    this.addKeyListener(keyH);

    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        //mouse functionality
      }
    });

    // Initialize systems
    tileM = new TileMaker(this, keyH);  //tile manager
    generateTrees();           //generate trees

    zombies = new ArrayList<>();         //generate zombies
    spawnZombies(totalZombies);

    //NEW updatee: Initialize buildings list before creating player/buildHandler
    walls = new ArrayList<>();
    floors = new ArrayList<>();

    player = new Player(this, keyH);
    buildH = new BuildHandler(this, keyH);

    // Start game thread
    gameThread = new Thread(this);
    gameThread.start();
  }

  //chop tree
  private void tryChopTree() {
    Point target = buildH.getTargetTile();
    int targetWorldX = target.x * TileSize;  // Convert to world coordinates
    int targetWorldY = target.y * TileSize;

    for(int i = 0; i < trees.size(); i++) {
      Tree tree = trees.get(i);
      Rectangle treeBounds = tree.getBounds();

      // Check if player is facing the tree and within chopping range
      if(treeBounds.contains(targetWorldX, targetWorldY)) {
        int woodYield = tree.chop();
        if(woodYield > 0) {
          player.addWood(woodYield);
          trees.remove(i);
          i--; // Adjust index after removal
        }
        break;
      }
    }
    for(int i = 0; i < walls.size(); i++) {
      Wall wall = walls.get(i);
      Rectangle wallBounds = wall.getBounds();

      // Check if player is facing the tree and within chopping range
      if(wallBounds.contains(targetWorldX, targetWorldY)) {
        if(wall.chop(25)) {
          walls.remove(i);
          i--; // Adjust index after removal
        }
        break;
      }
    }
  }

  public void generateZombies(){
    zombies = new ArrayList<>();
    for(int x=0;x<totalZombies;x++){
      zombies.add(new Zombie(this));
    }
  }
  public void spawnZombies(int count) {
    if (zombies == null) zombies = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      zombies.add(new Zombie(this));
    }
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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // First, fill the panel with black to remove white bars.
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    // Create an offscreen buffer at native game resolution:
    BufferedImage offscreen = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D gOff = offscreen.createGraphics();

    // Render your game scene onto the offscreen buffer.
    draw(gOff);  // your existing draw(g2) method that draws the game.
    gOff.dispose();

    // Determine the current panel's size.
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    // Calculate a uniform scale factor preserving aspect ratio.
    float scaleFactor = Math.min(panelWidth / (float)GAME_WIDTH, panelHeight / (float)GAME_HEIGHT);
    int scaledWidth = (int)(GAME_WIDTH * scaleFactor);
    int scaledHeight = (int)(GAME_HEIGHT * scaleFactor);

    // Calculate offsets for centering the game image.
    int offsetX = (panelWidth - scaledWidth) / 2;
    int offsetY = (panelHeight - scaledHeight) / 2;

    // Draw the offscreen buffer scaled to the computed size.
    g.drawImage(offscreen, offsetX, offsetY, scaledWidth, scaledHeight, null);
  }

  //draws game assets.
  public void draw(Graphics2D g2) {
    if(GAME_STATE.equals("play")){
      tileM.draw(g2);
      // Draw buildings
      for (Wall building : walls) {
        int screenX = building.worldX - player.worldX + player.screenX;
        int screenY = building.worldY - player.worldY + player.screenY;
        g2.drawImage(building.image, screenX, screenY, building.width, building.height, null);
      }
      for (Floor building : floors) {
        int screenX = building.worldX - player.worldX + player.screenX;
        int screenY = building.worldY - player.worldY + player.screenY;
        g2.drawImage(building.image, screenX, screenY, building.width, building.height, null);
      }
      //draw trees (these are drawn before the player so that the player can appear on top if desired)    TREE UPDATE
      for (Tree tree : trees) {
        tree.draw(g2, this);
      }
      for (Zombie zombie : zombies){
        if(zombie.alive){
          zombie.draw(g2);
        }
      }
      player.draw(g2);
    }
    
    ui.draw(g2);
    g2.dispose();
  }

  //central collision checking method.
  public void checkCollision(){
    //example->> enforce map boundaries.
    if(player.worldX < GAME_WIDTH/2) {
      player.worldX = GAME_WIDTH/2;
    }
    if(player.worldY < GAME_HEIGHT/2) {
      player.worldY = GAME_HEIGHT/2;
    }
    if(player.worldX > MAX_MAP_WIDTH - GAME_WIDTH/2) {
      player.worldX = MAX_MAP_WIDTH - GAME_WIDTH/2;
    }
    if(player.worldY > MAX_MAP_HEIGHT - GAME_HEIGHT/2) {
      player.worldY = MAX_MAP_HEIGHT - GAME_HEIGHT/2;
    }

    // update player bounds after corrections.
    player.setBounds(player.worldX, player.worldY, TileSize, TileSize);


  }
  public void reset(){
    zombies.clear();
    wave=1;
    zombiesKilledTotal=0;
    totalZombies=TOTAL_START_ZOMBIES;
      aliveZombies=totalZombies;
    for(int i=0;i<walls.size();i++){
      walls.get(i).chop(999);
      walls.remove(i);
      i--;
    }
    for(int i=0;i<floors.size();i++){
      floors.get(i).chop(999);
      floors.remove(i);
      i--;
    }
    for(int i=0;i<trees.size();i++) {
          trees.remove(i);
          i--; // Adjust index after removal
    }
    player.setValues();
    zombies = new ArrayList<>();         //  generate zombies
    spawnZombies(totalZombies);
    generateTrees();
  }

  public void update(){
    if(GAME_STATE.equals("play")){
      if (waveInProgress && zombiesLeft <= 7) {
        waveInProgress = false;
        nextWaveStartTime = System.currentTimeMillis() + WAVE_DELAY_MS;
      }
      if (!waveInProgress && System.currentTimeMillis() >= nextWaveStartTime) {
        int nextWave = wave + 1; // Calculate the upcoming wave number.
        int toAdd = TOTAL_START_ZOMBIES * nextWave;
        totalZombies += toAdd;
        wave = nextWave; //Update the current wave value.
        ui.resetBar();

        spawnZombies(toAdd);
        zombiesLeft = zombies.size();
        waveInProgress = true;
      }

      // Toggle inventory
      if (keyH.inventoryPressed) {
        inventoryOpen = !inventoryOpen;
        keyH.inventoryPressed = false;
      }

      // Update player
      player.update();

      //  Update each zombie and apply damage and  knockback
      for (Zombie z : zombies) {
        z.update();
        if (collisionChecker.checkCollision(z, player) && z.alive && z.canDamagePlayer()) {
          player.health--;
          ui.shrinkBar();
          z.recordDamageTime();
          player.applyKnockback(25, z.direction);
        }
      }

      // Remove dead zombies with an Iterator and award score
      Iterator<Zombie> it = zombies.iterator();
      while (it.hasNext()) {
        Zombie z = it.next();
        if (!z.alive) {
          it.remove();
          zombiesKilledTotal += 2;   // +2 per kill
        }
      }

      //  Refresh zombiesLeft from the definitive source
      zombiesLeft = zombies.size();

      // Update any UI state, other systems, and collision
      waveSurvived     = wave;
      zombiesKilledNow = zombiesKilledTotal;
      tileM.update();
      // build/chop logic...
      if (player.health <= 0) {
        GAME_STATE = "dead";
        reset();
      }

      //build update --> building with F and R key
      if(keyH.buildFloor || keyH.buildWall) {
        buildH.tryBuild();
        //reset flags after handling
        keyH.buildFloor = false;
        keyH.buildWall = false;
      }

      // Tree chopping
      if(keyH.chopTree) {
        tryChopTree();
        keyH.chopTree = false;
      }
      if(player.health<=0){
        GAME_STATE="dead";
        reset();
      }
      // after removing dead zombies and spawning waves:
      zombiesLeft = zombies.size();

      //perform collision checks after moving objects.
      checkCollision();
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
