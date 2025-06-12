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

        BufferedImage heart1 = (BufferedImage)heartIMG;
        BufferedImage heart2 = (BufferedImage)heartIMG;
        BufferedImage heart3 = (BufferedImage)heartIMG;
        if(gp.player.health<=0){
            heart1 = (BufferedImage)deadHeartIMG;
            heart2 = (BufferedImage)deadHeartIMG;
            heart3 = (BufferedImage)deadHeartIMG;
        }else if(gp.player.health<=1){
            heart2 = (BufferedImage)deadHeartIMG;
            heart3 = (BufferedImage)deadHeartIMG;
        }else if(gp.player.health<=2){
            heart3 = (BufferedImage)deadHeartIMG;
        }
        g.drawImage(heart2, (gp.GAME_WIDTH/4)*3, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);
        g.drawImage(heart3, (gp.GAME_WIDTH/4)*3+gp.TileSize, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);
        g.drawImage(heart1, (gp.GAME_WIDTH/4)*3-gp.TileSize, (gp.GAME_HEIGHT/10), gp.TileSize, gp.TileSize, null);


        g.setColor(Color.WHITE);
        g.setFont(arial_40);

        g.drawString("Alive Zombies: "+gp.aliveZombies, 50, 110);
        g.drawString("Score: " + gp.score, 40, 140);

        g.setColor(Color.black);
        g.fillRect((gp.GAME_WIDTH/2)-(barWidth/2), (gp.GAME_HEIGHT/5)*4, barWidth, barHeight);
        g.setColor(Color.red);

        g.fill(zombiesLeftBar);

        //Inventory
        if (gp.inventoryOpen) {
            int x = 50, y = 50, w = 400, h = 300;
            g.setColor(new Color(0,0,0,200));
            g.fillRect(x,y,w,h);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x,y,w,h);

            g.setFont(new Font("Arial",Font.BOLD,24));
            drawCenteredString(g,"INVENTORY", x,y,w,40);

            g.setFont(new Font("Arial",Font.PLAIN,18));
            int line = y+60, lh = 25;
            // Left column
            g.drawString("CLASS: WIZARD",   x+20, line);       line+=lh;
            g.drawString("Resources:",       x+20, line);       line+=lh;
            g.drawString("  Wood = " + gp.player.wood, x+40, line); line+=lh;
            // Right column
            int rx = x + w/2 +10, ry = y+60;
            g.drawString("Stats:",           rx, ry);          ry+=lh;
            g.drawString("  Health: " + gp.player.health, rx, ry); ry+=lh;
            g.drawString("  Movement: 5",    rx, ry);          ry+=lh;
            g.drawString("  Vision: 20",     rx, ry);          ry+=lh;
            ry+=lh/2;
            g.drawString("Abilities:",       rx, ry);          ry+=lh;
            g.drawString("  Base Magic (DMG: 2)", rx, ry);     ry+=lh;
            ry+=lh/2;
            g.drawString("Weapons:",         rx, ry);          ry+=lh;
            g.drawString("Armor:",           rx, ry);          ry+=lh;
            g.drawString("Equipment:",       rx, ry);
        }
    }
}

