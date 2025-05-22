package tile;
import java.awt.Graphics;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileMaker {
    GamePanel gp;
    Tile[] tile;
    public TileMaker(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10];
        getTileImage();
    }

    public void getTileImage(){
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("../resources/tiles/Tile_Water.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("../resources/tiles/Tile_Grass.png"));
        } catch (Exception e) {
        }
    }
    public void draw(Graphics g2){

        for(int x=0;x<8;x++){
                g2.drawImage(tile[0].image, gp.TileSize*x, 0, gp.TileSize, gp.TileSize, null);
        }
        for(int y=0;y<8;y++){
            g2.drawImage(tile[1].image, gp.TileSize*y, gp.TileSize, gp.TileSize, gp.TileSize, null);
        }
    }
}
