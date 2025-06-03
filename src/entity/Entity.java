package entity;

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

  // NEWWWWWW Add these fields for buildings
  public String name;
  public boolean collision;

  //animation
  public Image frontS, front1, front2, backS, back1, back2, rightS, right1, right2, leftS, left1, left2;
  public String direction;
  public int spriteCounter = 0;
  public int spriteNum = 1;
  public Rectangle hitBox = new Rectangle();
  public boolean collisionOn=false;

  public Entity() { }

  //this method returns the collision bounds.
  // also change and mess with it for hit box
  public Rectangle getCollisionBounds() {
    return new Rectangle(worldX, worldY, width, height);
  }
}