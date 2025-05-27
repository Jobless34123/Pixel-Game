package entity;

import java.awt.*;

public class Entity extends Rectangle {
  public int worldX;
  public int worldY;
  public int speed;

  //  animation
  public Image frontS, front1, front2, backS, back1, back2, rightS, right1, right2, leftS, left1, left2;
  public String direction;
  public int spriteCounter = 0;
  public int spriteNum = 1;
  public Rectangle hitBox = new Rectangle();
  public boolean collisionOn=false;

  public Entity() { }

  // This method returns the collision bounds.
  // Also chgange and mess with it for hit box
  public Rectangle getCollisionBounds() {
    return new Rectangle(worldX, worldY, width, height);
  }
}
