package building;

import entity.Entity;
import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends Entity {
    public String name;
    public boolean collision;

    public Wall(GamePanel gp, int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = gp.TileSize;
        this.height = gp.TileSize;
        this.name = "wall";
        this.collision = true; //walls should block movement

        //set collision bounds
        setBounds(worldX, worldY, width, height);

        //create a simple colored rectangle as placeholder image
        createWallImage(gp.TileSize);
    }

    private void createWallImage(int tileSize) {
        //create a simple stone/gray colored wall
        BufferedImage wallImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = wallImage.createGraphics();

        //fill with gray color for stone wall
        g2.setColor(new Color(128, 128, 128)); // Gray
        g2.fillRect(0, 0, tileSize, tileSize);

        //some stone texture with darker rectangles
        g2.setColor(new Color(96, 96, 96));
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
        g2.setColor(new Color(64, 64, 64));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(1, 1, tileSize-2, tileSize-2);

        g2.dispose();

        //bufferedImage to the Image field
        this.image = wallImage;
    }
}