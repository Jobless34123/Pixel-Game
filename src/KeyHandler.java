
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Handles what pressed keys does
public class KeyHandler implements KeyListener{

    public boolean inventoryPressed = false;
    public boolean upPressed, downPressed, rightPressed, leftPressed, enterPressed;
    public String direction;
    public boolean buildFloor, buildWall, chopTree;
    public boolean attackPressed = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_J) {
            attackPressed = true;
        }
        if (code == KeyEvent.VK_W){
            upPressed=true;
            direction="up";
        }
        if (code == KeyEvent.VK_S){
            downPressed=true;
            direction="down";
        }
        if (code == KeyEvent.VK_A){
            leftPressed=true;
            direction="left";
        }
        if (code == KeyEvent.VK_D){
            rightPressed=true;
            direction="right";
        }
        if (code == KeyEvent.VK_F) buildFloor = true;
        if (code == KeyEvent.VK_R) buildWall = true;
        if (code == KeyEvent.VK_SPACE) chopTree = true;
        if (code == KeyEvent.VK_B)   inventoryPressed = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_J) {
            attackPressed = false;
        }
        if (code == KeyEvent.VK_W){
            upPressed=false;
        }
        if (code == KeyEvent.VK_S){
            downPressed=false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed=false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed=false;
        }
        if (code == KeyEvent.VK_ENTER){
            enterPressed=false;
        }
        if (code == KeyEvent.VK_F) buildFloor = false;
        if (code == KeyEvent.VK_R) buildWall = false;
        if (code == KeyEvent.VK_SPACE) chopTree = false;
    }

}