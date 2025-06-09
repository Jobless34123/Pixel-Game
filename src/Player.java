

// description: This is a java class for the player.

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    // where the player is drawn
    public final int screenX;
    public final int screenY;

    public int playerX;
    public int playerY;

    //WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD
    public int wood = 0;

    public void addWood(int amount) {
        wood += amount;
    }

    public boolean spendWood(int amount) {
        if(wood >= amount) {
            wood -= amount;
            return true;
        }
        return false;
    }



    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        //calculate screenX/screenY so player appears centered
        screenX = (gp.GAME_WIDTH / 2) - (gp.TileSize / 2);
        screenY = (gp.GAME_HEIGHT / 2) - (gp.TileSize / 2);

        hitBox = new Rectangle();     //Hit box change for collision
        hitBox.x=10*gp.scale;
        hitBox.y=15*gp.scale;
        hitBox.width=12*gp.scale;
        hitBox.height=14*gp.scale;

        setValues();
        getPlayerImage();
        direction = "left";

        //initialize collision bounds (based on starting position and size). might need reworking
        setBounds(worldX, worldY, gp.TileSize, gp.TileSize);
    }

    public void setValues(){
        //starting position on the map (in pixels)
        worldX = gp.TileSize * 24;
        worldY = gp.TileSize * 24;
        speed = 4;
    }

    public void update(){

        //movement
        playerX=worldX/gp.TileSize;
        playerY=worldY/gp.TileSize;
        if(keyH.upPressed||keyH.downPressed||keyH.leftPressed||keyH.rightPressed){
            spriteCounter++;

            //check tile collision
            collisionOn=false;
            gp.collisionChecker.checkTile(this);;

            if(keyH.upPressed){
                direction = "up";
            }
            if(keyH.downPressed){
                direction = "down";
            }
            if(keyH.leftPressed){
                direction = "left";
            }
            if(keyH.rightPressed){
                direction = "right";
            }

            // if collision false, player moves

            if(!collisionOn){
                if(keyH.upPressed){
                    direction = "up";
                    worldY-=speed;
                }
                if(keyH.downPressed){
                    direction = "down";
                    worldY+=speed;
                }
                if(keyH.leftPressed){
                    direction = "left";
                    worldX-=speed;
                }
                if(keyH.rightPressed){
                    direction = "right";
                    worldX+=speed;
                }
            }
            //to cycle between different sprites
            if(spriteCounter>10){
                if(spriteNum==1){
                    spriteNum++;
                }else if(spriteNum==2){
                    spriteNum++;
                }else if(spriteNum==3){
                    spriteNum++;
                }else if(spriteNum==4){
                    spriteNum=1;
                }
                spriteCounter=0;
            }
        }



        //sprite (animation) update
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            spriteCounter++;
            if(spriteCounter > 10){
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }
        }
        //update the inherited Rectangle bounds after moving.
        setBounds(worldX, worldY, gp.TileSize, gp.TileSize);
    }

    public void getPlayerImage(){
        try{
            frontS = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Front_Standing.png"));
            front1 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Front_1.png"));
            front2 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Front_2.png"));
            backS = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Back_Standing.png"));
            back1 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Back_1.png"));
            back2 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Back_2.png"));
            rightS = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Right_Standing.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Right_2.png"));
            leftS = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Left_Standing.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/playerIMG/Player_Left_2.png"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics g2){
        BufferedImage image = null;
        if(direction.equals("up")){
            if(spriteNum==1){
                image = (BufferedImage) backS;
            }
            if(spriteNum==2){
                image = (BufferedImage) back1;
            }
            if(spriteNum==3){
                image = (BufferedImage) backS;
            }
            if(spriteNum==4){
                image = (BufferedImage) back2;
            }
        }
        if(direction.equals("down")){
            if(spriteNum==1){
                image = (BufferedImage) frontS;
            }
            if(spriteNum==2){
                image = (BufferedImage) front1;
            }
            if(spriteNum==3){
                image = (BufferedImage) frontS;
            }
            if(spriteNum==4){
                image = (BufferedImage) front2;
            }
        }
        if(direction.equals("left")){
            if(spriteNum==1){
                image = (BufferedImage) leftS;
            }
            if(spriteNum==2){
                image = (BufferedImage) left1;
            }
            if(spriteNum==3){
                image = (BufferedImage) leftS;
            }
            if(spriteNum==4){
                image = (BufferedImage) left2;
            }
        }
        if(direction.equals("right")){
            if(spriteNum==1){
                image = (BufferedImage) rightS;
            }
            if(spriteNum==2){
                image = (BufferedImage) right1;
            }
            if(spriteNum==3){
                image = (BufferedImage) rightS;
            }
            if(spriteNum==4){
                image = (BufferedImage) right2;
            }
        }
        g2.drawImage(image, screenX, screenY, gp.TileSize, gp.TileSize, null);

        //for visual debugging this draws the collision boundary (a semi-transparent red rectangle). jk its aimbot
        g2.setColor(new Color(255, 0, 0, 100));
        g2.drawRect(screenX, screenY, gp.TileSize, gp.TileSize);
        //hitbox
        g2.setColor(new Color(0, 115, 255, 100));
        g2.fillRect(screenX+hitBox.x, screenY+hitBox.y, hitBox.width, hitBox.height);
    }
}
