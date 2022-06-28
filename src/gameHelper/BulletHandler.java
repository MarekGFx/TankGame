package gameHelper;

import gameObject.*;

import java.awt.*;
import java.util.LinkedList;

/**
 * Klasa pomocnicza, kontener na pociski gracza.
 * @author Marek Gadamowicz
 */
public class BulletHandler {

    public static boolean MCLICKTURRETANIMATION = false;
    public static boolean EXPLOSIONANIMATION = false;
    public static boolean ENEMYPLAYERGETHIT = false;
    public static double SEND_X_EXPLOSION;
    public static double SEND_Y_EXPLOSION;

    private ExplosionsAnimationHandler explosionsAnimationHandler;
    Sound sound = new Sound();

    LinkedList<Bullet> bullets = new LinkedList<Bullet>();

    public void update(){
        for (int i = 0; i <bullets.size(); i ++){
            Bullet tempBullet = bullets.get(i);
            tempBullet.update();
            if(tempBullet.getY() < 0 || tempBullet.getY() > 600 || tempBullet.getX() < 0 || tempBullet.getX() > 900)
                removeBullet(tempBullet);
        }
    }

    public void render(Graphics g){
        for (int i = 0; i <bullets.size(); i ++){
            Bullet tempBullet = bullets.get(i);
            tempBullet.render(g);
        }
    }

    public Bullet addBullet(Bullet tempBullet){
        if(bullets.size() == 0) {
            sound.playSE(1);
            MCLICKTURRETANIMATION = true;
            bullets.add(tempBullet);
        }
        return tempBullet;
    }

    public void removeBullet(Bullet tempBullet){
        MCLICKTURRETANIMATION = false;
        bullets.remove(tempBullet);
    }

    public void checkBulletCollisionShields(Rectangle r){
        for (int i = 0; i <bullets.size(); i ++){
            Bullet tempBullet = bullets.get(i);
            if(tempBullet.initHitboxBullet().intersects(r)) {
                sound.playSE(2);
                SEND_X_EXPLOSION = tempBullet.getX();
                SEND_Y_EXPLOSION = tempBullet.getY();
                EXPLOSIONANIMATION = true;
                removeBullet(tempBullet);
            }
        }
    }

    public void checkBulletCollisionEnemyPlayer(){
        for (int i = 0; i <bullets.size(); i ++){
            Bullet tempBullet = bullets.get(i);
            if(tempBullet.initHitboxBullet().intersects(EnemyPlayerTank.hitbox2)) {
                sound.playSE(2);
                SEND_X_EXPLOSION = tempBullet.getX();
                SEND_Y_EXPLOSION = tempBullet.getY();
                EXPLOSIONANIMATION = true;
                ENEMYPLAYERGETHIT = true;
                removeBullet(tempBullet);
            }
        }
    }

    public boolean isEmpty(){
        return bullets.size() == 0;
    }

    public int size(){
        return bullets.size();
    }
}

