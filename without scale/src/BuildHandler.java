
import java.awt.Point;

public class BuildHandler {
    GamePanel gp;
    KeyHandler keyH;
    Player player;

    public BuildHandler(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.player = gp.player;
    }

    public Point getTargetTile() {
        
        int playerX=24*gp.TileSize;
        int playerY=24*gp.TileSize;
        int dx = 0, dy = 0;
        switch (player.direction) {
            case "up": dy = -1; break;
            case "down": dy = 1; break;
            case "left": dx = -1; break;
            case "right": dx = 1; break;
        }
        switch (player.direction) {
            case "up": 
            playerX = (player.worldX + gp.TileSize / 2) / gp.TileSize;
            playerY = (player.worldY + (player.height-player.hitBox.height-(player.height-player.hitBox.y-player.hitBox.height)/gp.scale)) / gp.TileSize; break;
            case "down":
            playerX = (player.worldX + gp.TileSize / 2) / gp.TileSize;
            playerY = (player.worldY + (player.height-(player.hitBox.y)/gp.scale)) / gp.TileSize; break;
            case "left":
            playerX = (player.worldX + 10*gp.scale) / gp.TileSize;
            playerY = (player.worldY + gp.TileSize / 2) / gp.TileSize; break;
            case "right":
            playerX = (player.worldX + 22*gp.scale) / gp.TileSize;
            playerY = (player.worldY + gp.TileSize / 2) / gp.TileSize; break;
        }
        return new Point(playerX + dx, playerY + dy);
    }

    public void tryBuild() {
        Point target = getTargetTile();
        int targetX = target.x;
        int targetY = target.y;

        // Check bounds
        if (targetX < 0 || targetX >= gp.MAP_WIDTH ||
                targetY < 0 || targetY >= gp.MAP_HEIGHT) {
            return;
        }

        // Check if space is occupied by another building
        for(Wall entity : gp.walls) {
            int entityX = entity.worldX / gp.TileSize;
            int entityY = entity.worldY / gp.TileSize;
            if(entityX == targetX && entityY == targetY) {
                return;
            }
        }
        for(Floor entity : gp.floors) {
            int entityX = entity.worldX / gp.TileSize;
            int entityY = entity.worldY / gp.TileSize;
            if(entityX == targetX && entityY == targetY) {
                return;
            }
        }
        

        // Handle building types
        if(keyH.buildFloor) {
            // Can only build floors on water
            if(gp.tileM.mapTileNum[targetY][targetX] == 0 && player.spendWood(1)) {
                gp.floors.add(new Floor(gp, targetX * gp.TileSize, targetY * gp.TileSize));
                gp.tileM.mapTileNum[targetY][targetX]=10;
            }
        }
        else if(keyH.buildWall) {
            // Can build walls on land or existing floors
            if((!gp.tileM.tile[gp.tileM.mapTileNum[targetY][targetX]].collision || hasFloorAt(targetX, targetY)) &&
                    player.spendWood(2)) {
                gp.walls.add(new Wall(gp, targetX * gp.TileSize, targetY * gp.TileSize));
                gp.tileM.mapTileNum[targetY][targetX]=10;
            }
        }
    }

    private boolean hasFloorAt(int x, int y) {
        for(Floor entity : gp.floors) {
            int entityX = entity.worldX / gp.TileSize;
            int entityY = entity.worldY / gp.TileSize;
            if(entityX == x && entityY == y) {
                return true;
            }
        }
        return false;
    }
}