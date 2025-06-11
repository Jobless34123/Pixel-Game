
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

public class Tree extends Entity {
    public int hp;
    public int woodYield;
    public BufferedImage image;

    //chopping the wood
    public int chop() {
        hp -= 25; // Damage per chop
        if(hp <= 0) {
            return woodYield;
        }
        return 0;
    }

    //static array to hold tree variants (loaded only once)
    private static BufferedImage[] treeImages = new BufferedImage[9];

    static {
        try {
            for (int i = 0; i < 9; i++) {
                treeImages[i] = ImageIO.read(Tree.class.getResourceAsStream("/resources/trees/Tree" + (i + 1) + ".png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //constructor now accepts tileSize to scale the tree dimensions.
    //you decide to use the full tile or a fraction of it. (full works better tho. still try them out)
    public Tree(int x, int y, int tileSize) {
        this.worldX = x;
        this.worldY = y;
        //the tree exactly fill one tile.
        this.width = tileSize;
        this.height = tileSize;

        //default values that can later be varied per tree variant
        this.hp = 100;
        this.woodYield = 10;

        //randomly select one of the 9 variants
        Random rand = new Random();
        int variant = rand.nextInt(9); // 0 to 8
        this.image = treeImages[variant];

        //set the collision (bounding) area
        setBounds(worldX, worldY, width, height);
    }

    //draw the tree, positioned relative to the players current view
    public void draw(Graphics2D g2, GamePanel gp) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.drawImage(image, screenX, screenY, width, height, null);
    }
}