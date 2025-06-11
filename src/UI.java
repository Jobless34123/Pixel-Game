
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class UI {
    GamePanel gp;
    public Image heartIMG,deadHeartIMG;
    public UI(GamePanel gp){
        this.gp = gp;
        getUiImages();
    }
    public void getUiImages(){
        try {
            heartIMG = ImageIO.read(getClass().getResourceAsStream("/resources/UI/Heart.png"));
            deadHeartIMG = ImageIO.read(getClass().getResourceAsStream("/resources/UI/Heart_Dead.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g){
        
        BufferedImage heart1 = (BufferedImage)heartIMG;
        BufferedImage heart2 = (BufferedImage)heartIMG;
        BufferedImage heart3 = (BufferedImage)heartIMG;
        g.setColor(Color.black);
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
        g.fillRect((gp.GAME_WIDTH/2)-(gp.TileSize*5/2), (gp.GAME_HEIGHT/10)*9, gp.TileSize*5, gp.TileSize/2);
    }
}
