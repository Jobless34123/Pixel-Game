
// description: This is a java class for the player.

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Player extends Entity {
    private Weapon sword;             // ← hold your sword
    private int attackCooldown = 0;   // ← cooldown timer

    GamePanel gp;
    KeyHandler keyH;

    // where the player is drawn
    public final int screenX;
    public final int screenY;

    public int playerX;
    public int playerY;

    //WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD WOOD
    public int wood = 0;

    Rectangle atk;
    Rectangle atk2;
    Color blastColor;

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
        super();
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
        sword = new Weapon("Basic Sword", 5, gp.TileSize*2, 24);

        atk = new Rectangle(
        worldX + hitBox.x,
        worldY + hitBox.y,
        hitBox.width,
        hitBox.height
        );
        
        atk2 = new Rectangle(
        worldX + hitBox.x,
        worldY + hitBox.y,
        hitBox.width,
        hitBox.height
        );
        atk2.setLocation(gp.GAME_WIDTH/2,gp.GAME_HEIGHT/2);
        blastColor = new Color(255,255,255,0);

    }

    public void setValues(){
        //starting position on the map (in pixels)
        worldX = gp.TileSize * 30;
        worldY = gp.TileSize * 41;
        speed = 10;
        health=5;
        maxHealth=5;
    }

    public void update(){

        //movement
        playerX=worldX/gp.TileSize;
        playerY=worldY/gp.TileSize;
        if(keyH.upPressed||keyH.downPressed||keyH.leftPressed||keyH.rightPressed){
            spriteCounter++;

            //check tile collision
            collisionOn=false;
            gp.collisionChecker.checkTile(this);

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
        // Handle attack
        if (attackCooldown > 0) attackCooldown--;
        if (attackCooldown == 5) {
            blastColor=new Color(255,255,255,0);
            atk2.setBounds(screenX + hitBox.x,screenY + hitBox.y,hitBox.width,hitBox.height);
        }
        if (keyH.attackPressed && attackCooldown == 0) {
            attackCooldown = 20;
            doAttack();
            applyVelocity();
        }
        
        //sprite (animation) update
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            spriteCounter++;
            if(spriteCounter > 10){
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }
        }
        // Apply any knockback
        applyVelocity();
        //update the inherited Rectangle bounds after moving.
        setBounds(worldX, worldY, gp.TileSize, gp.TileSize);
    }

    private void doAttack() {
        // 1) Build the attack box *in world coordinates*:
        atk.setBounds(
                worldX + hitBox.x,
                worldY + hitBox.y,
                hitBox.width,
                hitBox.height
        );
        atk2.setSize(hitBox.width,hitBox.height);
        
        

        // 2) Shift it one sword‐length in the facing direction:
        switch (direction) {
            case "up":      atk.height += sword.range;atk.y-=sword.range;
                            atk2.height+=sword.range;atk2.y-=(sword.range+hitBox.height);
                            blastColor=new Color(255,255,255,255);    break;
            case "down":    atk.height += sword.range;
                            atk2.height+=sword.range;atk2.y+=hitBox.height;
                            blastColor=new Color(255,255,255,255);    break;
            case "left":    atk.width += sword.range;atk.x-=sword.range; 
                            atk2.width+=sword.range;atk2.x-=sword.range+hitBox.width;
                            blastColor=new Color(255,255,255,255);    break;
            case "right":   atk.width += sword.range;
                            atk2.width+=sword.range;atk2.x+=hitBox.width;
                            blastColor=new Color(255,255,255,255);     break;
        }

        // 3) Damage and knock back any zombies it overlaps:
        for (Zombie z : gp.zombies) {
            if (z.alive && atk.intersects(
                    new Rectangle(z.worldX+z.hitBox.x,
                            z.worldY+z.hitBox.y,
                            z.hitBox.width,
                            z.hitBox.height
                    )
            )) {
                if(!z.takeDamage(sword.damage)){
                    gp.ui.shrinkBar();
                    gp.zombiesKilledTotal++;
                }
                z.applyKnockback(sword.knockback, direction);
            }
        }
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


    public void draw(Graphics2D g2) {
        BufferedImage toDraw = null;
        g2.setColor(blastColor);
        g2.fill(atk2);   
        // 1) Decide which sprite to draw:
        if (attackCooldown > 0) {

             
        } 
            // Normal player sprite
            switch (direction) {
                case "up":
                    if (spriteNum == 1) toDraw = (BufferedImage) backS;
                    if (spriteNum == 2) toDraw = (BufferedImage) back1;
                    if (spriteNum == 3) toDraw = (BufferedImage) backS;
                    if (spriteNum == 4) toDraw = (BufferedImage) back2;
                    break;
                case "down":
                    if (spriteNum == 1) toDraw = (BufferedImage) frontS;
                    if (spriteNum == 2) toDraw = (BufferedImage) front1;
                    if (spriteNum == 3) toDraw = (BufferedImage) frontS;
                    if (spriteNum == 4) toDraw = (BufferedImage) front2;
                    break;
                case "left":
                    if (spriteNum == 1) toDraw = (BufferedImage) leftS;
                    if (spriteNum == 2) toDraw = (BufferedImage) left1;
                    if (spriteNum == 3) toDraw = (BufferedImage) leftS;
                    if (spriteNum == 4) toDraw = (BufferedImage) left2;
                    break;
                case "right":
                    if (spriteNum == 1) toDraw = (BufferedImage) rightS;
                    if (spriteNum == 2) toDraw = (BufferedImage) right1;
                    if (spriteNum == 3) toDraw = (BufferedImage) rightS;
                    if (spriteNum == 4) toDraw = (BufferedImage) right2;
                    break;
            
        }

        // 2) Draw the selected sprite at the player’s screen position:
        if (toDraw != null) {
            g2.drawImage(toDraw, screenX, screenY, gp.TileSize, gp.TileSize, null);
        } else {
            // fallback: draw a placeholder rectangle if something went wrong
            g2.setColor(Color.MAGENTA);
            g2.fillRect(screenX, screenY, gp.TileSize, gp.TileSize);
        }
        /*
        //for visual debugging this draws the collision boundary (a semi-transparent red rectangle). jk its aimbot
        g2.setColor(new Color(255, 0, 0, 100));
        g2.drawRect(screenX, screenY, gp.TileSize, gp.TileSize);
        // 3) Debug hitbox (optional):
        g2.setColor(new Color(0, 115, 255, 100));
        g2.fillRect(screenX + hitBox.x, screenY + hitBox.y, hitBox.width, hitBox.height);
        */
        // 4) Draw UI elements (health bar, wood count, etc.) after this
    }
}
