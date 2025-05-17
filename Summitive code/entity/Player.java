/*
 * 
 */

package entity;
import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        setValues();
        getPlayerImage();
        direction = "left";
    }
    public void setValues(){
        x=100;
        y=100;
        speed=2;
    }
    public void update(){
        if(keyH.upPressed==true||keyH.downPressed==true||keyH.leftPressed==true||keyH.rightPressed==true){
        spriteCounter++;
        }
        
        if(keyH.upPressed==true){
            direction = "up";
                y-=speed;
          }
        if(keyH.downPressed==true){
            direction = "down";
                y+=speed;
        }
        if(keyH.leftPressed==true){
            direction = "left";
                x-=speed;
        }
        if(keyH.rightPressed==true){
            direction = "right";
                x+=speed;
        }

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
   public void getPlayerImage(){
    try{
        frontS = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Front_Standing.png"));
        front1 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Front_1.png"));
        front2 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Front_2.png"));
        backS = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Back_Standing.png"));
        back1 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Back_1.png"));
        back2 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Back_2.png"));
        rightS = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Right_Standing.png"));
        right1 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Right_1.png"));
        right2 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Right_2.png"));
        leftS = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Left_Standing.png"));
        left1 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Left_1.png"));
        left2 = ImageIO.read(getClass().getResourceAsStream("../resources/playerIMG/Player_Left_2.png"));
    } catch(Exception e){
        e.printStackTrace();
    }
}

    public void draw(Graphics g2){
//        g2.setColor(Color.WHITE);
//        g2.fillRect(x, y, gp.TileSize, gp.TileSize);
        BufferedImage image = null;
        if(direction.equals("up")){
            if(spriteNum==1){
                image = backS;
            }
            if(spriteNum==2){
                image = back1;
            }
            if(spriteNum==3){
                image = backS;
            }
            if(spriteNum==4){
                image = back2;
            }
        }
        if(direction.equals("down")){
            if(spriteNum==1){
                image = frontS;
            }
            if(spriteNum==2){
                image = front1;
            }
            if(spriteNum==3){
                image = frontS;
            }
            if(spriteNum==4){
                image = front2;
            }
        }
        if(direction.equals("left")){
            if(spriteNum==1){
                image = leftS;
            }
            if(spriteNum==2){
                image = left1;
            }
            if(spriteNum==3){
                image = leftS;
            }
            if(spriteNum==4){
                image = left2;
            }
        }
        if(direction.equals("right")){
            if(spriteNum==1){
                image = rightS;
            }
            if(spriteNum==2){
                image = right1;
            }
            if(spriteNum==3){
                image = rightS;
            }
            if(spriteNum==4){
                image = right2;
            }
        }
        g2.drawImage(image, x, y, gp.TileSize, gp.TileSize, null);
    }
}
