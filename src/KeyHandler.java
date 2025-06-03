

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Handles what pressed keys does
public class KeyHandler implements KeyListener{

    public boolean upPressed, downPressed, rightPressed, leftPressed, enterPressed;
    public String direction;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

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
        if (code == KeyEvent.VK_ENTER){
            enterPressed=true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

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
    }
    
}
