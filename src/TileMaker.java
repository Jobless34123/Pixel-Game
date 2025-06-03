

/*
NOTE: This will not make the final cut as it replaces the tile to place a new tile. Rather than this.
      We will make a building object class in package entity for more control and collision detection.

      This was just for testing incase we need it for smth completely unrated to building

Responsible for handling the player's block-building actions in the game.
Determines the target tile based on player position and direction,
checks bounds, and toggles tile states (build/destroy).

Dependencies:
 - GamePanel: provides game state and map dimensions.
 - KeyHandler: tracks player input (e.g., build key pressed).
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class TileMaker {
    GamePanel gp;
    KeyHandler keyH;
    public Tile[] tile;
    public int [][] mapTileNum;
    public TileMaker(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        tile = new Tile[20];
        mapTileNum = new int[gp.MAP_HEIGHT][gp.MAP_WIDTH];
        getTileImage();
        map();
    }

    //loads tile images into the tile array and sets collision flags.
    public void getTileImage(){
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Water.png"));//0
            tile[0].collision=true;
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Grass.png"));//1
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Top.png"));//2
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Bottom.png"));//3
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Left.png"));//4
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Right.png"));//5
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Top_Left.png"));//6
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Top_Right.png"));//7
            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Bottom_Left.png"));//8
            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Tile_Sand_Bottom_Right.png"));//9
            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/Wall_Middle.png"));//10
        } catch (Exception e) {
        }
    }
    public void update(){
    }
    //reads the map layout from a text file
    //each line in the file corresponds to a row of tile indices
    public void map(){
        try {
            InputStream is = getClass().getResourceAsStream("/maps/TestMap.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int width=0;
            int height=0;

            while(height<gp.MAP_HEIGHT&&width<gp.MAP_WIDTH){
                String line = br.readLine();
                while(width<gp.MAP_WIDTH){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[width]);
                    mapTileNum[height][width] = num;
                    width++;
                }
                if(width == gp.MAP_WIDTH){
                    width=0;
                    height++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //draws all tiles within the map, offset by the player's position.
    //calculates screen coordinates for each tile to render only visible area.
    // graphics2D g2 context used for rendering
    public void draw(Graphics2D g2){
        int height=0;
        int width=0;
        while (height<gp.MAP_HEIGHT&& width<gp.MAP_WIDTH) {
            int tileNum = mapTileNum[height][width];

            int worldX = width*gp.TileSize;
            int worldY = height*gp.TileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.TileSize, gp.TileSize, null);
            width++;
            if(width>=gp.MAP_WIDTH){
                width=0;
                height++;

            }

        }
        if(gp.zombies.onPath){
            g2.setColor(new Color(255,0,0,70));
            for(int i=0;i<gp.pathFinder.pathList.size();i++){
                int worldX = gp.pathFinder.pathList.get(i).col*gp.TileSize;
                int worldY = gp.pathFinder.pathList.get(i).row*gp.TileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;
                g2.fillRect(screenX, screenY, gp.TileSize, gp.TileSize);
            }
        }
    }
}