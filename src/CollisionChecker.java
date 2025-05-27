package main;

import entity.Entity;

public class CollisionChecker {


    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    //xhecks collision between two entities using their current bounds
    public boolean checkCollision(Entity a, Entity b) {
        return a.getBounds().intersects(b.getBounds());
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

        //te two tiles entity is walking into
        int tileNum1, tileNum2;

        //check if the tiles have collision, if they do set collisionOn to be true so player stops
        if(entity.direction.equals("up")){
            entityTopMapY = (entityTopWorldY - entity.speed)/gp.TileSize;
            tileNum1 = gp.tileM.mapTileNum[entityTopMapY][entityLeftMapX];
            tileNum2 = gp.tileM.mapTileNum[entityTopMapY][entityRightMapX];
            if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                entity.collisionOn=true;
            }
        }
        if(entity.direction.equals("down")){
            entityBottomMapY = (entityBottomWorldY - entity.speed+5)/gp.TileSize;
            tileNum1 = gp.tileM.mapTileNum[entityBottomMapY][entityLeftMapX];
            tileNum2 = gp.tileM.mapTileNum[entityBottomMapY][entityRightMapX];
            if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                entity.collisionOn=true;
            }
        }
        if(entity.direction.equals("left")){
            entityLeftMapX = (entityLeftWorldX - entity.speed)/gp.TileSize;
            tileNum1 = gp.tileM.mapTileNum[entityTopMapY][entityLeftMapX];
            tileNum2 = gp.tileM.mapTileNum[entityBottomMapY][entityLeftMapX];
            if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                entity.collisionOn=true;
            }
        }
        if(entity.direction.equals("right")){
            entityRightMapX = (entityRightWorldX - entity.speed+6)/gp.TileSize;
            tileNum1 = gp.tileM.mapTileNum[entityTopMapY][entityRightMapX];
            tileNum2 = gp.tileM.mapTileNum[entityBottomMapY][entityRightMapX];
            if(gp.tileM.tile[tileNum1].collision||gp.tileM.tile[tileNum2].collision){
                entity.collisionOn=true;
            }
        }
    }
}