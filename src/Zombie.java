import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Zombie extends Entity {

    
    public int screenX;
    public int screenY;
    public int movementX;
    public int movementY;
    public boolean onPath;

    public Zombie(GamePanel gp){
        super(gp);

        
        screenX = (gp.GAME_WIDTH / 2) - (gp.TileSize / 2);
        screenY = (gp.GAME_HEIGHT / 2) - (gp.TileSize / 2);

        name="zombie";
        speed=3;
        maxHealth=10;
        health=maxHealth;
        onPath=true;
        //Hit box change for collision
        hitBox = new Rectangle();     
        hitBox.x=10*gp.scale;
        hitBox.y=15*gp.scale;
        hitBox.width=12*gp.scale;
        hitBox.height=14*gp.scale;

        worldX=(int)(Math.random()*50)*gp.TileSize;
        worldY=(int)(Math.random()*50)*gp.TileSize;

        //setValues();
        getZombieImage();
        direction = "down";
    }
    public void getZombieImage(){
        //add images later
    }
    public void setAction(){
        if(!onPath){
            actionCounter++;
            if(actionCounter==120){
                Random random = new Random();
                int i=random.nextInt(100)+1;
                if(i<=25){
                    direction="up";
                }
                if(i>25&&i<=50){
                    direction="down";
                }
                if(i>50&&i<=75){
                    direction="left";
                }
                if(i>75&&i<=100){
                    direction="right";
                }
                actionCounter=0;
            }
        }else{            
            int goalCol=(gp.player.worldX+gp.player.hitBox.x)/gp.TileSize  ;
            int goalRow=(gp.player.worldY +gp.player.hitBox.y)/gp.TileSize;
            searchPath(goalCol, goalRow);
        }
    }
    public void update(){
        setAction();
        gp.collisionChecker.checkTile(this);
        collisionOn=false;
        if(!collisionOn){
        switch (direction) {
            case "up":
                worldY-=speed;
                break;
            case "down":
                worldY+=speed;
                break;
            case "left":
                worldX-=speed;
                break;
            case "right":
                worldX+=speed;
                break;
        
            default:
                break;
        }
        }
            //check tile collision
    }
    public void draw(Graphics g2){
        g2.setColor(new Color(255, 0, 0, 255));
        int screenX = worldX - gp.player.worldX + gp.player.screenX + movementX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY + movementY;
        g2.fillRect(screenX, screenY, hitBox.width, hitBox.height);
    }   
    public void searchPath(int goalWorldX, int goalWorldY){

        int startX = (worldX+hitBox.x)/gp.TileSize; 
        int startY = (worldY+hitBox.y)/gp.TileSize;

        gp.pathFinder.setNodes(startX, startY, goalWorldX, goalWorldY);

        if(gp.pathFinder.search()){

            //next world X and Y
            int nextX = gp.pathFinder.pathList.get(0).col * gp.TileSize;
            int nextY = gp.pathFinder.pathList.get(0).row * gp.TileSize;

            int leftX = worldX+hitBox.x;
            int rightX = worldX+hitBox.x+hitBox.width;
            int topY = worldY+hitBox.y;
            int bottomY = worldY+hitBox.y+hitBox.height;

            if(topY>nextY && leftX>=nextX && rightX < nextX+gp.TileSize){
                direction="up";
            }
            else if(topY<nextY && leftX>=nextX && rightX < nextX+gp.TileSize){
                direction="down";
            }
            else if(topY>=nextY && bottomY<nextY +gp.TileSize){
                //left or right
                if(leftX>nextX){
                    direction="left";
                }
                if(leftX<nextX){
                    direction="right";
                }
            }
            else if (topY>nextY && leftX>nextX){
                //up or left
                direction="up";
                collisionOn=false;
                gp.collisionChecker.checkTile(this);
                if(collisionOn){
                    direction="left";
                }
            }
            else if (topY>nextY && leftX<nextX){
                //up or right
                direction="up";
                collisionOn=false;
                gp.collisionChecker.checkTile(this);
                if(collisionOn){
                    direction="right";
                }
            }
            else if (topY<nextY && leftX>nextX){
                //down or left
                direction="down";
                collisionOn=false;
                gp.collisionChecker.checkTile(this);
                if(collisionOn){
                    direction="left";
                }
            }
            else if (topY<nextY && leftX<nextX){
                //down or right
                direction="down";
                collisionOn=false;
                gp.collisionChecker.checkTile(this);
                if(collisionOn){
                    direction="right";
                }
            }
            int nextWorldX = gp.pathFinder.pathList.get(0).col;
            int nextWorldY = gp.pathFinder.pathList.get(0).row;
            if(nextWorldX==goalWorldX && nextWorldY ==goalWorldY ){
                //onPath=false;
            }
        }
    }
    
}
