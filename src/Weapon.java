// src/Weapon.java

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Weapon {
    public String name;
    public int damage;
    public int range;      // pixels
    public int knockback;  // pixels
    public BufferedImage[] swingFrames;

    public Weapon(String name, int damage, int range, int knockback) {
        this.name = name;
        this.damage= damage;
        this.range = range;
        this.knockback = knockback;
        loadSprites();
    }

    private void loadSprites() {  // assumes you mirror GITHUBsrc folder structure
        try {
            swingFrames = new BufferedImage[3];
            swingFrames[0] = ImageIO.read(getClass().getResourceAsStream("/resources/weapons/sword_swing1.png"));
            swingFrames[1] = ImageIO.read(getClass().getResourceAsStream("/resources/weapons/sword_swing2.png"));
            swingFrames[2] = ImageIO.read(getClass().getResourceAsStream("/resources/weapons/sword_swing3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
