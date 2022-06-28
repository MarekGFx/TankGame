package gameObject;

import gameHelper.BufferedImageLoader;
import gameHelper.BulletHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasa  obiektu Bullet dziedziczaca po klasie GameObject.
 * @author Marek Gajdamowicz
 */
public class Bullet extends GameObject{

    private BulletHandler bulletHandler;
    public static Rectangle hitboxBullet;
    private BufferedImage bullet,bullet1,exp1;
    public double velX;
    public double velY;

    /**
     * Konstruktor klasy Bullet.
     * @param x
     * @param y
     * @param bulletHandler
     * @throws IOException
     */
    public Bullet(double x, double y, BulletHandler bulletHandler ) throws IOException {
        super(x, y);
        this.bulletHandler = bulletHandler;

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

    /**
     * Metoda zwracajaca obiekt Rectangle, ktory posluzy do sprawdzania kolizji z pozostalymi obiektami.
     * @return
     */
    public Rectangle initHitboxBullet(){
        int x = (int) this.getX();
        int y = (int) this.getY();
        int width = 8; //this.bullet.getWidth();
        int height = 8;// this.bullet.getHeight();
        return hitboxBullet = new Rectangle(x, y, width, height);
    }

}
