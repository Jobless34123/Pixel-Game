package building;

import entity.Entity;
import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Floor extends Entity {
    public String name;
    public boolean collision;

    public Floor(GamePanel gp, int worldX, int worldY) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = gp.TileSize;
        this.height = gp.TileSize;
        this.name = "floor";
        this.collision = false; //players can walk on floors

        //set collision bounds
        setBounds(worldX, worldY, width, height);

        //create a simple colored rectangle cuz no image yet and this can later be coded to show breaking floor slowly animation
        createFloorImage(gp.TileSize);
    }

    private void createFloorImage(int tileSize) {
        //a simple brown colored floor
        BufferedImage floorImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = floorImage.createGraphics();

        //fill with brown color for wooden floor
        g2.setColor(new Color(139, 115, 85)); // Saddle brown
        g2.fillRect(0, 0, tileSize, tileSize);

        //add some wood grain lines for texture  (this si nto
        g2.setColor(new Color(101, 67, 33)); // Darker brown
        g2.setStroke(new BasicStroke(2));
        for(int i = 0; i < tileSize; i += 8) {
            g2.drawLine(0, i, tileSize, i);
        }

        //border
        g2.setColor(new Color(101, 67, 33));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(1, 1, tileSize-2, tileSize-2);

        g2.dispose();

        //bufferedImage to the Image field
        this.image = floorImage;
    }
}