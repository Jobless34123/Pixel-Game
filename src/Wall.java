
import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends Entity {
    public int hp;
    GamePanel gp;
    public Wall(GamePanel gp, int worldX, int worldY) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = gp.TileSize;
        this.height = gp.TileSize;
        name = "wall";
        collision = true; //walls should block movement
        hp=100;

        //set collision bounds
        setBounds(worldX, worldY, width, height);

        //create a simple colored rectangle as placeholder image
        createWallImage(gp.TileSize);
        this.hitBox.width=gp.TileSize+4;
        this.hitBox.height=gp.TileSize+4;
        this.hitBox.translate(-2, -2);
    }

    private void createWallImage(int tileSize) {
        //create a simple stone/gray colored wall
        BufferedImage wallImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = wallImage.createGraphics();

        //fill with gray color for stone wall
        g2.setColor(new Color(64, 101, 16)); // Gray
        g2.fillRect(0, 0, tileSize, tileSize);

        //some stone texture with darker rectangles
        g2.setColor(new Color(207, 133, 71));
        //should create brick pattern
        int brickHeight = tileSize / 4;
        int brickWidth = tileSize / 3;

        for(int row = 0; row < 4; row++) {
            for(int col = 0; col < 3; col++) {
                int x = col * brickWidth;
                int y = row * brickHeight;

                //offset every other row for brick pattern
                if(row % 2 == 1) {
                    x += brickWidth / 2;
                    if(x + brickWidth > tileSize) continue;
                }

                g2.drawRect(x, y, brickWidth, brickHeight);
            }
        }

        //darker border for nice textutre
        g2.setColor(new Color(138, 69, 39));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(1, 1, tileSize-2, tileSize-2);

        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        
        g2.setColor(new Color(15, 17, 26, 255));
        g2.fillRect(screenX+hitBox.x, screenY+hitBox.y, hitBox.width, hitBox.height);

        g2.dispose();

        //bufferedImage to the Image field
        this.image = wallImage;
    }
    public boolean chop(int dmg) {
        hp -= dmg; // Damage per chop
        if(hp <= 0) {
            gp.tileM.mapTileNum[worldY/gp.TileSize][worldX/gp.TileSize]=gp.tileM.mapTileNumOriginal[worldY/gp.TileSize][worldX/gp.TileSize];
            return true;
        }
        return false;
    }
}