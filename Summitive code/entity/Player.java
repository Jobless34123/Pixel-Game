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

     public final int screenX;
     public final int screenY;
     public int playerX;
     public int playerY;
     public Player(GamePanel gp, KeyHandler keyH){
         this.gp = gp;
         this.keyH = keyH;
 
        screenX = (gp.GAME_WIDTH/2)-(gp.TileSize/2);
        screenY = (gp.GAME_HEIGHT/2)-(gp.TileSize/2);

         setValues();
         getPlayerImage();
         direction = "left";
     }
     public void setValues(){

        worldY = gp.TileSize * 24;
        worldX = gp.TileSize * 24;
         speed=4;
     }
     public void update(){
        playerX=worldX/gp.TileSize;
        playerY=worldY/gp.TileSize;
         if(keyH.upPressed||keyH.downPressed||keyH.leftPressed||keyH.rightPressed){
         spriteCounter++;
         }
         
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
         g2.drawImage(image, screenX, screenY, gp.TileSize, gp.TileSize, null);
     }
 }