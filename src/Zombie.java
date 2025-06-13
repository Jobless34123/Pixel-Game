
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;


public class Zombie extends Entity {

    GamePanel gp;
    public int screenX;
    public int screenY;
    public int movementX;
    public int movementY;
    public int actionCounter=0;
    public boolean onPath;
    int spawnX=1;
    int spawnY=1;
    int spawnArea;
    private long lastDamageTime = 0;

    public Zombie(GamePanel gp){
        super();
        this.gp=gp;
        
        screenX = (gp.GAME_WIDTH / 2) - (gp.TileSize / 2);
        screenY = (gp.GAME_HEIGHT / 2) - (gp.TileSize / 2);

        name="zombie";
        speed=3;
        maxHealth = 10;
        healthS = maxHealth;
        onPath=true;
        alive=true;
        //Hit box change for collision
        hitBox = new Rectangle();     
        hitBox.x=10*gp.scale;
        hitBox.y=15*gp.scale;
        hitBox.width=12*gp.scale;
        hitBox.height=14*gp.scale;
        spawnArea=(int)(Math.random()*7);
        System.out.println(spawnArea);
        
        if(spawnArea<=0){
            spawnX=(int)(Math.random()*20)+16;
            spawnY=(int)(Math.random()*11)+3;
        }else if(spawnArea<=1){
            spawnX=(int)(Math.random()*13)+66;
            spawnY=(int)(Math.random()*13)+3;
        }else if(spawnArea<=2){
            spawnX=(int)(Math.random()*20)+7;
            spawnY=(int)(Math.random()*19)+47;
        }else if(spawnArea<=3){
            spawnX=(int)(Math.random()*23)+44;
            spawnY=(int)(Math.random()*18)+34;
        }else if(spawnArea<=5){
            spawnX=(int)(Math.random()*47)+3;
            spawnY=(int)(Math.random()*8)+71;
        }else if(spawnArea<=6){
            spawnX=(int)(Math.random()*28)+50;
            spawnY=(int)(Math.random()*8)+71;
        }
            
        worldX=spawnX*gp.TileSize;
        worldY=spawnY*gp.TileSize;
        //setValues();
        getZombieImage();
        direction = "down";
        spriteCounter = 0;
        spriteNum = 1;
    }

    public void loseHealth(int dmg) {
        this.healthS -= dmg;
        if (this.healthS <= 0 && alive) {
            onDeath();
        }
    }

    public boolean canDamagePlayer() {
        return System.currentTimeMillis() - lastDamageTime >= 800;
    }

    public void recordDamageTime() {
        lastDamageTime = System.currentTimeMillis();
    }

    public void getZombieImage(){
        //add images later
        try {
            
            frontS = ImageIO.read(getClass().getResourceAsStream("/resources/zombies/Zombie_Front_Standing.png"));
        } catch (Exception e) {
            // TODO: handle exception
        }
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
            int goalCol=(gp.player.worldX+gp.player.hitBox.x)/gp.TileSize;
            int goalRow=(gp.player.worldY +gp.player.hitBox.y)/gp.TileSize;
            searchPath(goalCol, goalRow);
        }
    }
    public void update(){
        setAction();
            for(int i=0;i<gp.walls.size();i++){
                if(gp.collisionChecker.checkCollision(this, gp.walls.get(i))){
                    if(gp.walls.get(i).chop(1)){
                        gp.walls.remove(gp.walls.get(i));
                    }
                }
                
            }
        collisionOn=false;
        gp.collisionChecker.checkTile(this);
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
        System.out.println("x: "+worldX/gp.TileSize+" y: "+worldY/gp.TileSize);

        spriteCounter++;
        applyVelocity();
        collisionCheck();   
    }
    
    public void collisionCheck(){
        if(worldX<=0){
            worldX=3;
        }
        if(worldX>gp.MAX_MAP_WIDTH){
            worldX=gp.MAX_MAP_WIDTH-2;
        }
        if(worldY<=0){
            worldY=3;
        }
        if(worldY>gp.MAX_MAP_HEIGHT){
            worldY=gp.MAX_MAP_HEIGHT-2;
        }
    }

    @Override
    protected void onDeath() {
        alive = false;
    }

    public void draw(Graphics g2){
        BufferedImage image = (BufferedImage) frontS;

        int screenX = worldX - gp.player.worldX + gp.player.screenX + movementX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY + movementY;

        g2.drawImage(image, screenX, screenY, gp.TileSize, gp.TileSize, null);

        //for visual debugging this draws the collision boundary (a semi-transparent red rectangle). jk its aimbot
        g2.setColor(new Color(255, 0, 0, 100));
        g2.drawRect(screenX, screenY, gp.TileSize, gp.TileSize);
        //hitbox
        g2.setColor(new Color(0, 115, 255, 100));
        g2.fillRect(screenX+hitBox.x, screenY+hitBox.y, hitBox.width, hitBox.height);
    }   
    public void searchPath(int goalWorldX, int goalWorldY){
        /*
        int startX = (worldX)/gp.TileSize; 
        int startY = (worldY)/gp.TileSize;
        if(worldX<=0){
            worldX=3;
        }
        if(worldX>gp.MAX_MAP_WIDTH){
            worldX=gp.MAX_MAP_WIDTH-2;
        }
        if(worldY<=0){
            worldY=3;
        }
        if(worldY>gp.MAX_MAP_HEIGHT){
            worldY=gp.MAX_MAP_HEIGHT-2;
        }
        */
        int startX = (worldX+hitBox.x)/gp.TileSize; 
        int startY = (worldY+hitBox.y)/gp.TileSize;
        if(startX>=gp.MAP_WIDTH){
            startX--;
        }
        if(startX<=0){
            startX++;
        }
        if(startY>=gp.MAP_HEIGHT){
            startY--;
        }
        if(startY<=0){
            startY++;
        }

        gp.pathFinder.setNodes(startX, startY, goalWorldX, goalWorldY);
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