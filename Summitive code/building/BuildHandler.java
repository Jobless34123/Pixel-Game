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
        int playerX = (gp.player.worldX + gp.TileSize / 2) / gp.TileSize;
        int playerY = (gp.player.worldY + gp.TileSize - gp.TileSize / 5) / gp.TileSize;

        int dx = 0;
        int dy = 0;

        switch (gp.player.direction) {
            case "up": dy = -1; break;
            case "down": dy = 1; break;
            case "left": dx = -1; break;
            case "right": dx = 1; break;
        }

        int targetX = playerX + dx;
        int targetY = playerY + dy;

        if (targetX >= 0 && targetX < gp.MAP_WIDTH &&
            targetY >= 0 && targetY < gp.MAP_HEIGHT) {

            if (gp.tileM.mapTileNum[targetY][targetX] == 1) {
                    gp.tileM.mapTileNum[targetY][targetX] = 0; // 9 = wall tile
            }
        }
    }
}