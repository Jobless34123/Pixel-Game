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
    Font pixelFont;
    int barWidth;
    int barHeight;
    int zombiesLeft;
    Rectangle zombiesLeftBar;

    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 20);

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
        zombiesLeftBar.grow(-(barWidth/gp.totalZombies)/4, 0);
        zombiesLeftBar.translate(-(barWidth/gp.totalZombies)/4, 0);
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

    public void resetBar(){
        zombiesLeftBar = new Rectangle(
                (gp.GAME_WIDTH/2) - (barWidth/2),
                (gp.GAME_HEIGHT/5) * 4,
                barWidth,
                barHeight
        );
    }

    public void draw(Graphics2D g){

        // Set up rendering hints for pixel text
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                java.awt.RenderingHints.VALUE_RENDER_SPEED);

        if (gp.GAME_STATE.equals("play")) {

            BufferedImage heart1 = (BufferedImage) heartIMG;
            BufferedImage heart2 = (BufferedImage) heartIMG;
            BufferedImage heart3 = (BufferedImage) heartIMG;
            BufferedImage heart4 = (BufferedImage) heartIMG;
            BufferedImage heart5 = (BufferedImage) heartIMG;
            if (gp.player.health <= 0) {
                heart1 = (BufferedImage) deadHeartIMG;
                heart2 = (BufferedImage) deadHeartIMG;
                heart3 = (BufferedImage) deadHeartIMG;
                heart4 = (BufferedImage) deadHeartIMG;
                heart5 = (BufferedImage) deadHeartIMG;
            } else if (gp.player.health <= 1) {
                heart2 = (BufferedImage) deadHeartIMG;
                heart3 = (BufferedImage) deadHeartIMG;
                heart4 = (BufferedImage) deadHeartIMG;
                heart5 = (BufferedImage) deadHeartIMG;
            } else if (gp.player.health <= 2) {
                heart3 = (BufferedImage) deadHeartIMG;
                heart4 = (BufferedImage) deadHeartIMG;
                heart5 = (BufferedImage) deadHeartIMG;
            } else if (gp.player.health <= 3) {
                heart4 = (BufferedImage) deadHeartIMG;
                heart5 = (BufferedImage) deadHeartIMG;
            } else if (gp.player.health <= 4) {
                heart5 = (BufferedImage) deadHeartIMG;
            }
            g.drawImage(heart5, (gp.GAME_WIDTH / 6) * 3 + gp.TileSize, (gp.GAME_HEIGHT / 10), 50, 40, null);
            g.drawImage(heart4, (gp.GAME_WIDTH / 6) * 3 + gp.TileSize * 3 / 2, (gp.GAME_HEIGHT / 10), 50, 40, null);
            g.drawImage(heart3, (gp.GAME_WIDTH / 6) * 3 + gp.TileSize * 2, (gp.GAME_HEIGHT / 10), 50, 40, null);
            g.drawImage(heart2, (gp.GAME_WIDTH / 6) * 3 + gp.TileSize * 5 / 2, (gp.GAME_HEIGHT / 10), 50, 40, null);
            g.drawImage(heart1, (gp.GAME_WIDTH / 6) * 3 + gp.TileSize * 3, (gp.GAME_HEIGHT / 10), 50, 40, null);

            // Draw scores and zombie count using the pixel font (pixel font not working remove in final version)
            if (pixelFont != null) {
                g.setFont(pixelFont.deriveFont(20f));
            } else {
                g.setFont(arial_40);
            }
            g.setColor(Color.WHITE);
            g.drawString("Alive Zombies: " + gp.zombiesLeft, 50, 110);
            g.drawString("Score: " + gp.zombiesKilledNow, 40, 140);

            // Draw the zombie progress bar
            g.setColor(Color.BLACK);
            g.fillRect((gp.GAME_WIDTH / 2) - (barWidth / 2), (gp.GAME_HEIGHT / 5) * 4, barWidth, barHeight);
            g.setColor(new Color(0, 247, 82));
            g.fill(zombiesLeftBar);

            // Draw Inventory if open
            if (gp.inventoryOpen) {
                int x = 500, y = 200, w = 425, h = 350;
                g.setColor(new Color(0, 0, 0, 200));
                g.fillRect(x, y, w, h);
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2));
                g.drawRect(x, y, w, h);

                // Draw inventory title using pixel font if loaded
                if (pixelFont != null) {
                    g.setFont(pixelFont.deriveFont(Font.BOLD, 24f));
                } else {
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                }
                drawCenteredString(g, "INVENTORY", x, y, w, 40);

                // Draw inventory content
                if (pixelFont != null) {
                    g.setFont(pixelFont.deriveFont(18f));
                } else {
                    g.setFont(new Font("Arial", Font.PLAIN, 18));
                }
                int line = y + 60, lh = 25;
                // Left column
                g.drawString("CLASS: WIZARD", x + 20, line);
                line += lh;
                g.drawString("Resources:", x + 20, line);
                line += lh;
                g.drawString("  Wood = " + gp.player.wood, x + 40, line);
                line += lh;
                // Right column
                int rx = x + w / 2 + 10, ry = y + 60;
                g.drawString("Stats:", rx, ry);
                ry += lh;
                g.drawString("  Health: " + gp.player.health, rx, ry);
                ry += lh;
            }
        }
        else if (gp.GAME_STATE.equals("tut")) {
            // Draw  semi-transparent dark background
            g.setColor(new Color(0, 0, 0, 175));
            g.fillRect(0, 0, gp.GAME_WIDTH, gp.GAME_HEIGHT);

            // Draw the title
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.YELLOW);
            String title = "Zombie Survival";
            drawCenteredString(g, title, 0, gp.GAME_HEIGHT / 6, gp.GAME_WIDTH, 60);

            // Draw instructions
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.setColor(Color.WHITE);
            String[] instructions = {
                    "Press P to Play",
                    "WASD to Move",
                    "Space to Chop Trees",
                    "R/F to Build Defenses",
                    "J to Attack",
                    "Press Q to Quit",
            };
            int startY = gp.GAME_HEIGHT / 3;
            int lineHeight = 35;
            for (String line : instructions) {
                drawCenteredString(g, line, 0, startY, gp.GAME_WIDTH, 30);
                startY += lineHeight;
            }

            //footer
            g.setFont(new Font("Arial", Font.ITALIC, 20));
            g.setColor(new Color(200, 200, 200));
            drawCenteredString(g, "Good luck, survivor!", 0, gp.GAME_HEIGHT - 70, gp.GAME_WIDTH, 30);
        }
        else if (gp.GAME_STATE.equals("dead")) {
            // Draw the dark overlay
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, gp.GAME_WIDTH, gp.GAME_HEIGHT);

            // Draw a bold Game Over title
            g.setFont(new Font("Arial", Font.BOLD, 64));
            g.setColor(Color.RED);
            String gameOverText = "Game Over";
            drawCenteredString(g, gameOverText, 0, gp.GAME_HEIGHT / 4, gp.GAME_WIDTH, 80);

            // statistics
            g.setFont(new Font("Arial", Font.PLAIN, 28));
            g.setColor(Color.WHITE);
            int midY = gp.GAME_HEIGHT / 2;
            String waveText = "You survived " + gp.waveSurvived + " wave(s)";
            String killedText = "Your Score is " + gp.zombiesKilledNow;
            String restartMsg = "Press P to Restart or Q to Quit";
            drawCenteredString(g, waveText, 0, midY, gp.GAME_WIDTH, 30);
            drawCenteredString(g, killedText, 0, midY + 40, gp.GAME_WIDTH, 30);
            drawCenteredString(g, restartMsg, 0, midY + 80, gp.GAME_WIDTH, 30);
        }
        else if (gp.GAME_STATE.equals("paused")) {
            //  cool pause
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, gp.GAME_WIDTH, gp.GAME_HEIGHT);

            // Use the pixel font if available
            Font titleFont = (pixelFont != null) ? pixelFont.deriveFont(Font.BOLD, 48f) : new Font("Arial", Font.BOLD, 48);
            g.setFont(titleFont);
            g.setColor(Color.WHITE);
            drawCenteredString(g, "Paused", 0, gp.GAME_HEIGHT / 4, gp.GAME_WIDTH, 60);

            // Draw instructions below
            Font instrFont = (pixelFont != null) ? pixelFont.deriveFont(24f) : new Font("Arial", Font.PLAIN, 24);
            g.setFont(instrFont);
            String[] instructions = {
                    "Press P to unpause",
                    "Press Q to Quit"
            };
            int startY = gp.GAME_HEIGHT / 2;
            int lineHeight = 35;
            for (String line : instructions) {
                drawCenteredString(g, line, 0, startY, gp.GAME_WIDTH, 30);
                startY += lineHeight;
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(arial_40);

        // Set rendering hints for pixel text BEFORE setting the font
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                java.awt.RenderingHints.VALUE_RENDER_SPEED); // Often good for pixel art too

        g.setColor(Color.WHITE);

        if (pixelFont != null) {
            g.setFont(pixelFont.deriveFont(20f)); // You can derive a new size here if needed
        } else {
            g.setFont(arial_40); // Fallback if pixel font failed to load
        }


        g.drawString("Alive Zombies: "+gp.zombiesLeft, 50, 110);
        g.drawString("Score: " + gp.zombiesKilledNow, 40, 140);

        // Inventory
        if (gp.inventoryOpen) {
            int x = 500, y = 200, w = 425, h = 350;
            g.setColor(new Color(0,0,0,200));
            g.fillRect(x,y,w,h);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x,y,w,h);

            //decide if  inventory title to be pixel art or Arial
            if (pixelFont != null) {
                g.setFont(pixelFont.deriveFont(Font.BOLD, 24f)); // Use pixel font for title
            } else {
                g.setFont(new Font("Arial",Font.BOLD,24));
            }
            drawCenteredString(g,"INVENTORY", x,y,w,40);

            // And the inventory content
            if (pixelFont != null) {
                g.setFont(pixelFont.deriveFont(18f)); // Use pixel font for content
            } else {
                g.setFont(new Font("Arial",Font.PLAIN,18));
            }
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

