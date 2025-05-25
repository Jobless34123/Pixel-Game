package building;

import main.KeyHandler;

import main.GamePanel;

public class BuildHandler {
    GamePanel gp;
    KeyHandler keyH;
    public BuildHandler(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
    }

    public void build() {
        int playerX = (gp.player.worldX + gp.TileSize / 2) / gp.TileSize;// sets player X coordinante in tiles 
        int playerY = (gp.player.worldY + gp.TileSize - gp.TileSize / 5) / gp.TileSize;// sets player Y coordinante in tiles (Extra stuff for better hitbox)

        int dx = 0;//where to place relative to the player
        int dy = 0;

        switch (gp.player.direction) {//enables target positions to be accurate to the player direction
            case "up": dy = -1; break;
            case "down": dy = 1; break;
            case "left": dx = -1; break;
            case "right": dx = 1; break;
        }

        int targetX = playerX + dx;//sets target values
        int targetY = playerY + dy;

        //checks if tager values are within bounds
        if (targetX >= 0 && targetX < gp.MAP_WIDTH &&
            targetY >= 0 && targetY < gp.MAP_HEIGHT) {
            //checks if player can build there
            if (gp.tileM.mapTileNum[targetY][targetX] == 1) {
                    gp.tileM.mapTileNum[targetY][targetX] = 0; // builds
            }
        }
    }
}