


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
        int playerX = (player.worldX + gp.TileSize / 2) / gp.TileSize;
        int playerY = (player.worldY + gp.TileSize - gp.TileSize / 5) / gp.TileSize;

        int dx = 0, dy = 0;
        switch (player.direction) {
            case "up": dy = -1; break;
            case "down": dy = 1; break;
            case "left": dx = -1; break;
            case "right": dx = 1; break;
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
        for(Entity entity : gp.buildings) {
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
                gp.buildings.add(new Floor(gp, targetX * gp.TileSize, targetY * gp.TileSize));
                gp.tileM.mapTileNum[targetY][targetX]=1;
            }
        }
        else if(keyH.buildWall) {
            // Can build walls on land or existing floors
            if((gp.tileM.mapTileNum[targetY][targetX] == 1 || hasFloorAt(targetX, targetY)) &&
                    player.spendWood(2)) {
                gp.buildings.add(new Wall(gp, targetX * gp.TileSize, targetY * gp.TileSize));
                gp.tileM.mapTileNum[targetY][targetX]=0;
            }
        }
    }

    private boolean hasFloorAt(int x, int y) {
        for(Entity entity : gp.buildings) {
            int entityX = entity.worldX / gp.TileSize;
            int entityY = entity.worldY / gp.TileSize;
            if(entityX == x && entityY == y && entity.name.equals("floor")) {
                return true;
            }
        }
        return false;
    }
}