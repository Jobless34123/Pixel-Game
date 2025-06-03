package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

/**
 * handles loading and rendering of tile-based maps
 * manages tile textures and collision data
 */
public class TileMaker {
    GamePanel gp;
    KeyHandler keyH;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileMaker(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        tile = new Tile[20];
        mapTileNum = new int[gp.MAP_HEIGHT][gp.MAP_WIDTH];
        getTileImage();
        map();
    }

    //loads tile images into the tile array and sets collision flags
    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("../resources/tiles/Tile_Water.png")); // 0
            tile[0].collision = true;

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("../resources/tiles/Tile_Grass.png")); // 1

            // ... rest of your tile loading code ...
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("../resources/tiles/Tile_Sand_Top.png"));
            // ... continue with all your existing tile definitions ...

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        //empty for now - could be used for animated tiles later
    }

    //reads the map layout from a text file
    public void map() {
        try {
            InputStream is = getClass().getResourceAsStream("../maps/TestMap.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int width = 0;
            int height = 0;

            while (height < gp.MAP_HEIGHT && width < gp.MAP_WIDTH) {
                String line = br.readLine();
                while (width < gp.MAP_WIDTH) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[width]);
                    mapTileNum[height][width] = num;
                    width++;
                }
                if (width == gp.MAP_WIDTH) {
                    width = 0;
                    height++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //renders all visible tiles
    public void draw(Graphics2D g2) {
        for (int height = 0; height < gp.MAP_HEIGHT; height++) {
            for (int width = 0; width < gp.MAP_WIDTH; width++) {
                int tileNum = mapTileNum[height][width];
                int worldX = width * gp.TileSize;
                int worldY = height * gp.TileSize;

                //convert world coordinates to screen coordinates
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                //only draw tiles that are potentially visible
                if (worldX + gp.TileSize > gp.player.worldX - gp.player.screenX &&
                        worldX - gp.TileSize < gp.player.worldX + gp.player.screenX &&
                        worldY + gp.TileSize > gp.player.worldY - gp.player.screenY &&
                        worldY - gp.TileSize < gp.player.worldY + gp.player.screenY) {

                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.TileSize, gp.TileSize, null);
                }
            }
        }
    }
}