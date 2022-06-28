package gameObject;

import gameHelper.BufferedImageLoader;
import gameHelper.ExplosionsAnimationHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasa ExplosionsAnimation dziedziczaca po klasie GameObject, twozy wyswietla grafike eksplozji.
 */
public class ExplosionsAnimation extends GameObject{

    private ExplosionsAnimationHandler explosionsAnimationHandler;
    private BufferedImage exp1;

    public ExplosionsAnimation(double x, double y, ExplosionsAnimationHandler explosionsAnimationHandler) throws IOException {
        super(x, y);
        this.explosionsAnimationHandler = explosionsAnimationHandler;

        BufferedImageLoader loader = new BufferedImageLoader();
        exp1 = loader.loadImage("/explosion/exp1.png");
    }

    public void update(){
        setX(getX());
        setY(getY());
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(exp1, (int) getX(), (int) getY(), null);
        g2d.dispose();
    }
}
