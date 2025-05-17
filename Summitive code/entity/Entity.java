package entity;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity extends Rectangle{
  int x;
  int y;
  int speed;
  public BufferedImage frontS, front1, front2, backS, back1, back2, rightS, right1, right2, leftS, left1, left2;
  public String direction;
  public int spriteCounter = 0;
  public int spriteNum=1;
  public Entity(){
  }
  
}
