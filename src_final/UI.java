import java.awt.FontMetrics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class UI {
    GamePanel gp;
    public Image heartIMG,deadHeartIMG;
    Font arial_40;
    int barWidth;
    int barHeight;
    int zombiesLeft;
    Rectangle zombiesLeftBar;
    Color zombieBarColor = new Color(0, 247, 82);

    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        getUiImages();

        
        barWidth=gp.GAME_WIDTH/2;
        barHeight=gp.GAME_HEIGHT/12;

        zombiesLeftBar = new Rectangle((gp.GAME_WIDTH/2)-(barWidth/2), (gp.GAME_HEIGHT/5)*4, barWidth, barHeight);
    }
    public void getUiImages(){
        try {
            heartIMG = ImageIO.read(getClass().getResourceAsStream("/resources/UI/Heart.png"));
            deadHeartIMG = ImageIO.read(getClass().getResourceAsStream("/resources/UI/Heart_Dead.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void shrinkBar(){
        //add shrink bar
        zombiesLeftBar.grow(-(barWidth/gp.totalZombies)/2, 0);
        zombiesLeftBar.translate(-(barWidth/gp.totalZombies)/2, 0);
    }
    public void resetBar(){
        zombiesLeftBar = new Rectangle((gp.GAME_WIDTH/2)-(barWidth/2), (gp.GAME_HEIGHT/5)*4, barWidth, barHeight);
    }
    private void drawCenteredString(Graphics2D g, String text, int x, int y, int w, int h) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth  = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        // calculate top-left corner for the text
        int tx = x + (w - textWidth) / 2;
        int ty = y + (h - textHeight) / 2 + fm.getAscent();
        g.drawString(text, tx, ty);
    }
    public void draw(Graphics2D g){
        if(gp.GAME_STATE.equals("play")){
            BufferedImage heart1 = (BufferedImage)heartIMG;
            BufferedImage heart2 = (BufferedImage)heartIMG;
            BufferedImage heart3 = (BufferedImage)heartIMG;
            BufferedImage heart4 = (BufferedImage)heartIMG;
            BufferedImage heart5 = (BufferedImage)heartIMG;
            if(gp.player.health<=0){
                heart1 = (BufferedImage)deadHeartIMG;
                heart2 = (BufferedImage)deadHeartIMG;
                heart3 = (BufferedImage)deadHeartIMG;
                heart4 = (BufferedImage)deadHeartIMG;
                heart5 = (BufferedImage)deadHeartIMG;
            }else if(gp.player.health<=1){
                heart2 = (BufferedImage)deadHeartIMG;
                heart3 = (BufferedImage)deadHeartIMG;
                heart4 = (BufferedImage)deadHeartIMG;
                heart5 = (BufferedImage)deadHeartIMG;
            }else if(gp.player.health<=2){
                heart3 = (BufferedImage)deadHeartIMG;
                heart4 = (BufferedImage)deadHeartIMG;
                heart5 = (BufferedImage)deadHeartIMG;
            }else if(gp.player.health<=3){
                heart4 = (BufferedImage)deadHeartIMG;
                heart5 = (BufferedImage)deadHeartIMG;
            }else if(gp.player.health<=4){
                heart5 = (BufferedImage)deadHeartIMG;
            }
            g.drawImage(heart5, (gp.GAME_WIDTH/4)*3+gp.TileSize, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);
            g.drawImage(heart4, (gp.GAME_WIDTH/4)*3, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);
            g.drawImage(heart2, (gp.GAME_WIDTH/4)*3-gp.TileSize, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);
            g.drawImage(heart3, (gp.GAME_WIDTH/4)*3-gp.TileSize*2, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);
            g.drawImage(heart1, (gp.GAME_WIDTH/4)*3-gp.TileSize*3, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);

            
            g.setColor(Color.WHITE);
            g.setFont(arial_40);
            g.drawString("Press B for stats", 50, 50);

            g.setColor(Color.black);
            g.fillRect((gp.GAME_WIDTH/2)-(barWidth/2), (gp.GAME_HEIGHT/5)*4, barWidth, barHeight);
            
            g.setColor(zombieBarColor);
            
            g.fill(zombiesLeftBar);
            if (gp.inventoryOpen) {
                int x = gp.GAME_WIDTH/2-450/2, y = gp.GAME_HEIGHT/2-250/2, w = 450, h = 250;
                g.setColor(new Color(0,0,0,200));
                g.fillRect(x,y,w,h);
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2));
                g.drawRect(x,y,w,h);
    
                g.setFont(new Font("Arial",Font.BOLD,24));
                drawCenteredString(g,"STATS", x,y,w,40);
    
                g.setFont(new Font("Arial",Font.PLAIN,18));
                int line = y+60, lh = 25;
                // Left column
                g.drawString("Resources:",       x+20, line);       line+=lh;
                g.drawString("  Wood = " + gp.player.wood, x+40, line); line+=lh;
                // Right column
                int rx = x + w/2 +10, ry = y+60;
                g.drawString("  Health: " + gp.player.health, rx, ry); ry+=lh;
                ry+=lh/2;
                g.drawString("Abilities:",       rx, ry);          ry+=lh;
                g.drawString("Kamehameha (DMG: 5)", rx, ry);     ry+=lh;
                ry+=lh/2;
                g.drawString("Wave: "+gp.wave,         rx, ry);          ry+=lh;
                g.drawString("Zombies Left: "+gp.aliveZombies,           rx, ry);          ry+=lh;
                g.drawString("Total Zombies Killed: "+gp.zombiesKilledTotal,       rx, ry);
            }
        }else if(gp.GAME_STATE.equals("paused")){
            g.setColor(Color.WHITE);
            g.setFont(arial_40);
            g.drawString("Game Paused", 100,(100));
            g.drawString("Press P to unpuase", 100,100+gp.TileSize);
        }else if(gp.GAME_STATE.equals("dead")){
            g.setColor(Color.WHITE);
            g.setFont(arial_40);
            g.drawString("You Died", 100,100);
            g.drawString("You Survived "+gp.waveSurvived+" wave(s)", 100,100+gp.TileSize);
            g.drawString("You killed "+gp.zombiesKilledNow+" zombie(s)", 100,(100)+gp.TileSize*2);
            g.drawString("To play the again press P", 100,(100)+gp.TileSize*3);

        }else if(gp.GAME_STATE.equals("wave")){
            g.setColor(Color.WHITE);
            g.setFont(arial_40);
            g.drawString("Wave: "+gp.wave, 100,(100));
            g.drawString("Press P to play next wave", 100,100+gp.TileSize);
        }else if(gp.GAME_STATE.equals("tut")){
            g.setColor(Color.WHITE);
            g.setFont(arial_40);
            g.drawString("Press P to play/pause", 100,(100));
            g.drawString("WASD to walk", 100,100+gp.TileSize);
            g.drawString("Hold Space to chop trees (for wood)", 100,(100)+gp.TileSize*2);
            g.drawString("R to place wall (on grass, with 1 wood)", 100,(100)+gp.TileSize*3);
            g.drawString("F to place wall (on water, with 2 woods)", 100,(100)+gp.TileSize*4);
            g.drawString("J to attack (kamehameha)", 100,(100)+gp.TileSize*5);
            g.drawString("B to open statistics", 100,(100)+gp.TileSize*6);
        }
    }
}

