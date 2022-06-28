package gameHelper;

import gameObject.ExplosionsAnimation;

import java.awt.*;
import java.util.LinkedList;

/**
 * Metoda pomocnicza, kontener na efekty wybuchow.
 * @author Marek Gajdamowicz
 */
public class ExplosionsAnimationHandler {

    LinkedList<ExplosionsAnimation> explosionsAnimations = new LinkedList<ExplosionsAnimation>();

    public void update(){
        for (int i = 0; i < explosionsAnimations.size(); i++) {
            ExplosionsAnimation tempExplosion = explosionsAnimations.get(i);
            tempExplosion.update();
            if(!BulletHandler.EXPLOSIONANIMATION) {
                removeExplosion(tempExplosion);
            }
        }
    }

    public void render(Graphics g){
        for (int i = 0; i<explosionsAnimations.size(); i++){
            ExplosionsAnimation tempExplosion = explosionsAnimations.get(i);
            tempExplosion.render(g);
        }
    }

    public ExplosionsAnimation addExplosion(ExplosionsAnimation tempExplosion){
        for (int i = 0; i < 20; i++) {
            explosionsAnimations.add(tempExplosion);
        }
        return tempExplosion;
    }

    public void removeExplosion(ExplosionsAnimation tempExplosion){
        explosionsAnimations.remove(tempExplosion);
    }
}

