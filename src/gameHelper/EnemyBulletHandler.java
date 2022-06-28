package gameHelper;

import gameObject.EnemyBullet;
import gameObject.PlayerTank;


import java.awt.*;
import java.util.LinkedList;

/**
 * Klasa pomocnicza, kontener na pociski przeciwnika.
 * @author Marek Gadamowicz
 */
public class EnemyBulletHandler {

    public static boolean ENEMY_TANK_GETHIT = false;
    public static boolean IF_ENEMY_TANK_HIT = false;
    public static boolean MCLICKENEMYTURRETANIMATION = false;
    public static boolean EXPLOSIONANIMATION = false;
    public static double SEND_X_EXPLOSION;
    public static double SEND_Y_EXPLOSION;

    LinkedList<EnemyBullet> enemyBullets = new LinkedList<EnemyBullet>();
    Sound sound = new Sound();

    public void update(){
        for (int i = 0; i <enemyBullets.size(); i ++){
            EnemyBullet tempBullet = enemyBullets.get(i);
            tempBullet.update();

            if(tempBullet.getY() < 0 || tempBullet.getY() > 600 || tempBullet.getX() < 0 || tempBullet.getX() > 900)
                removeBullet(tempBullet);
        }
    }

    public void render(Graphics g){
        for (int i = 0; i <enemyBullets.size(); i ++){
            EnemyBullet tempBullet = enemyBullets.get(i);
            tempBullet.render(g);
        }
    }

    public EnemyBullet addEnemyBullet(EnemyBullet tempBullet){
        if(enemyBullets.size() == 0) {
            sound.playSE(1);
            MCLICKENEMYTURRETANIMATION = true;
            enemyBullets.add(tempBullet);
        }
        return tempBullet;
    }

    public void removeBullet(EnemyBullet tempBullet){
        MCLICKENEMYTURRETANIMATION = false;
        enemyBullets.remove(tempBullet);
    }

    public void checkEnemyBulletCollisionPlayer(){
        for (int i = 0; i <enemyBullets.size(); i ++){
            EnemyBullet tempBullet = enemyBullets.get(i);
            if(tempBullet.initHitboxEnemyBullet().intersects(PlayerTank.hitbox1)) {
                sound.playSE(2);
                SEND_X_EXPLOSION = tempBullet.getX();
                SEND_Y_EXPLOSION = tempBullet.getY();
                EXPLOSIONANIMATION = true;
                removeBullet(tempBullet);
                IF_ENEMY_TANK_HIT = true;
            }
        }
    }

    public void checkBulletCollisionShields(Rectangle r){
        for (int i = 0; i <enemyBullets.size(); i ++){
            EnemyBullet tempBullet = enemyBullets.get(i);
            if(tempBullet.initHitboxEnemyBullet().intersects(r)) {
                sound.playSE(2);
                SEND_X_EXPLOSION = tempBullet.getX();
                SEND_Y_EXPLOSION = tempBullet.getY();
                EXPLOSIONANIMATION = true;
                removeBullet(tempBullet);
            }
        }
    }
}

