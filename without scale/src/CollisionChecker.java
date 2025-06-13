

/*
Utility class to detect collisions between entities and map tiles.
Provides methods to check entity-to-entity bounding-box collisions
and to determine if movement into an adjacent tile is blocked.
 */

import java.awt.Point;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    //checks collision between two entities using their current bounds
    public boolean checkCollision(Entity a, Entity b) {
        
        int aLeftWorldX = a.worldX + a.hitBox.x;
        int aRightWorldX = a.worldX + a.hitBox.x + a.hitBox.width;
        int aTopWorldY = a.worldY + a.hitBox.y;
        int aBottomWorldY = a.worldY + a.hitBox.y + a.hitBox.height;

        Point topLeft = new Point(aLeftWorldX, aTopWorldY);
        Point topRight = new Point(aRightWorldX, aTopWorldY);
        Point bottomLeft = new Point(aLeftWorldX, aBottomWorldY);
        Point bottomRight = new Point(aRightWorldX, aBottomWorldY);
        if(b.contains(topLeft)||b.contains(topRight)||b.contains(bottomLeft)||b.contains(bottomRight)){
            return true;
        }
        

        
        return false;
    }

    //checks if the tile entity is moving to has collision or not
    public void checkTile(Entity entity){
        //get the four coords of the corners of the hitbox
        int entityLeftWorldX = entity.worldX + entity.hitBox.x;
        int entityRightWorldX = entity.worldX + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.worldY + entity.hitBox.y;
        int entityBottomWorldY = entity.worldY + entity.hitBox.y + entity.hitBox.height;
        

        //get the map coords of the corners of the hitbox
        int entityLeftMapX = entityLeftWorldX/gp.TileSize;
        int entityRightMapX = entityRightWorldX/gp.TileSize;
        int entityTopMapY = entityTopWorldY/gp.TileSize;
        int entityBottomMapY = entityBottomWorldY/gp.TileSize;

        //the two tiles entity is walking into
        int tileNum1, tileNum2;

        //check if the tiles have collision, if they do set collisionOn to be true so player stops
        if(entity.direction.equals("up")){
            entityTopMapY = (entityTopWorldY - entity.speed)/gp.TileSize;
            if(entityTopMapY>0){
                tileNum1 = gp.tileM.mapTileNum[entityTopMapY][entityLeftMapX];
                tileNum2 = gp.tileM.mapTileNum[entityTopMapY][entityRightMapX];
                if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                    entity.collisionOn=true;
                }
            }
        }
        if(entity.direction.equals("down")){
            entityBottomMapY = (entityBottomWorldY + entity.speed)/gp.TileSize;
            if(entityBottomMapY<gp.MAP_HEIGHT){
                tileNum1 = gp.tileM.mapTileNum[entityBottomMapY][entityLeftMapX];
                tileNum2 = gp.tileM.mapTileNum[entityBottomMapY][entityRightMapX];
                if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                    entity.collisionOn=true;
                }
            }
        }
        if(entity.direction.equals("left")){
            entityLeftMapX = (entityLeftWorldX - entity.speed)/gp.TileSize;
            if(entityLeftMapX>0){
                tileNum1 = gp.tileM.mapTileNum[entityTopMapY][entityLeftMapX];
                tileNum2 = gp.tileM.mapTileNum[entityBottomMapY][entityLeftMapX];
                if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                    entity.collisionOn=true;
                }
            }
        }
        if(entity.direction.equals("right")){
            entityRightMapX = (entityRightWorldX + entity.speed)/gp.TileSize;
            if(entityRightMapX<gp.MAP_HEIGHT){
                tileNum1 = gp.tileM.mapTileNum[entityTopMapY][entityRightMapX];
                tileNum2 = gp.tileM.mapTileNum[entityBottomMapY][entityRightMapX];
                if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                    entity.collisionOn=true;
                }
            }
        }


        //NEWWWWW: Check building collisions (walls)
        checkBuildingCollision(entity);
    }

    //NEW METHOD Check collision with buildings (especially walls)
    public void checkBuildingCollision(Entity entity) {
        //calculate where the entity will be after moving
        int futureX = entity.worldX;
        int futureY = entity.worldY;

        switch(entity.direction) {
            case "up": futureY -= entity.speed; break;
            case "down": futureY += entity.speed; break;
            case "left": futureX -= entity.speed; break;
            case "right": futureX += entity.speed; break;
        }

        //create a rectangle representing the entity future position
        java.awt.Rectangle futureHitBox = new java.awt.Rectangle(
                futureX + entity.hitBox.x,
                futureY + entity.hitBox.y,
                entity.hitBox.width,
                entity.hitBox.height
        );

        //check collision with all buildings
        for(Floor building : gp.floors) {
            if(building.name.equals("wall")) { //only walls block movement
                java.awt.Rectangle buildingBounds = new java.awt.Rectangle(
                        building.worldX,
                        building.worldY,
                        gp.TileSize,
                        gp.TileSize
                );

                if(futureHitBox.intersects(buildingBounds)) {
                    entity.collisionOn = true;
                    break;
                }
            }
        }
        for(Wall building : gp.walls) {
            if(building.name.equals("wall")) { //only walls block movement
                java.awt.Rectangle buildingBounds = new java.awt.Rectangle(
                        building.worldX,
                        building.worldY,
                        gp.TileSize,
                        gp.TileSize
                );

                if(futureHitBox.intersects(buildingBounds)) {
                    entity.collisionOn = true;
                    break;
                }
            }
        }
    }
}