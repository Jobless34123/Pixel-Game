

/*
 Base class for objects in the game world (Building, MObs , NPCs, etc.).
 Should manages position, speed, directional state, animations, and collision bounds.
 Extends Rectangle to reuse basic geometric operations.
 */

import java.awt.*;

//initialization
public class Entity extends Rectangle {
  public int worldX;
  public int worldY;
  public int speed;
  public Image image;

  //fields for buildings
  public String name;
  public boolean collision;
  public int health;
  public int maxHealth;
  public boolean alive;

  // fields for combat
  public int healthS;
  public int dx = 0, dy = 0;      // knockback velocity

  //animation
  public Image frontS, front1, front2, backS, back1, back2, rightS, right1, right2, leftS, left1, left2;
  public String direction;
  public int spriteCounter = 0;
  public int spriteNum = 1;
  public Rectangle hitBox = new Rectangle();
  public boolean collisionOn=false;

  public Entity() { }


  public void takeDamage(int dmg) {
    healthS -= dmg;
    if (healthS <= 0) {
      onDeath();
    }
  }

  public void applyKnockback(int kb, String attackDir) {
    // remove onPath / knockbackTimer unless you need them
    switch (attackDir) {
      case "up":    dy -= kb; break;
      case "down":  dy += kb; break;
      case "left":  dx -= kb; break;
      case "right": dx += kb; break;
    }
  }
  
  protected void onDeath() {
        alive = false;
  }
  public void applyVelocity() {
    if (dx != 0 || dy != 0) {
      worldX += dx;
      worldY += dy;
      // dampen velocity
      dx = (int)(dx * 0.95);
      dy = (int)(dy * 0.95);

      x = worldX;
      y = worldY;
    }
  }

  //this method returns the collision bounds.
  // also change and mess with it for hit box
  public Rectangle getCollisionBounds() {
    return new Rectangle(worldX, worldY, width, height);
  }
}