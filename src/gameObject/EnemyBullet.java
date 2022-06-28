package gameObject;

import gameHelper.BufferedImageLoader;
import gameHelper.EnemyBulletHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasa obiektu EnemyBullet dziedziczaca po klasie GameObject.
 * @author Marek Gajdamowicz
 */
public class EnemyBullet extends GameObject{

    private EnemyBulletHandler enemyBulletHandler;
    private BufferedImage bullet;
    public double velX;
    public double velY;

    public EnemyBullet(double x, double y, EnemyBulletHandler enemyBulletHandler ) throws IOException {
        super(x, y);
        this.enemyBulletHandler = enemyBulletHandler;

        BufferedImageLoader loader = new BufferedImageLoader();
        bullet = loader.loadImage("/bullet/bullet.png");
    }

    public void update(){
        setX(getX()+velX);
        setY(getY()+velY);
    }

    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(bullet, (int)getX(), (int)getY(), null);
        g2d.dispose();
    }

    public Rectangle initHitboxEnemyBullet(){
        int x = (int) this.getX();
        int y = (int) this.getY();
        int width = this.bullet.getWidth();
        int height = this.bullet.getHeight();
        return new Rectangle(x, y, width, height);
    }

}

