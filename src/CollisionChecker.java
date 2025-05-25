package main;

import entity.Entity;

public class CollisionChecker {

    //xhecks collision between two entities using their current bounds
    public boolean checkCollision(Entity a, Entity b) {
        return a.getBounds().intersects(b.getBounds());
    }
}
