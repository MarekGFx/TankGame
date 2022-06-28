package gameObject;

import gameEngine.TankGameEngine;
import gameHelper.BufferedImageLoader;
import gameHelper.EnemyBulletHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *  Klasa obiektu EnemyPlayerTank dziedziczaca po klasie GameObject.
 *  Analogiczna do klasy PlayerTank.
 *  @author Marek Gajdamowicz
 */
public class EnemyPlayerTank extends GameObject{


    public static Rectangle hitbox2;
    private double actualXp2;
    private double actualYp2;
    private double nX;
    private double nY;
    private double rotation;
    String direction = "d";
    private BufferedImage tankImage2, tankDown2, tankUp2, tankLeft2, tankRight2, tankDownLeft2,
                            tankDownRight2,tankUpRight2,tankUpLeft2,turretImage2,turretImage3, turretShootImage2;

    private int energy;

    public EnemyPlayerTank(double x, double y, int energy) throws IOException {
        super(x, y);
        this.energy = energy;

        BufferedImageLoader loader = new BufferedImageLoader();
        tankImage2 = loader.loadImage("/playerTank2/tank-down.png");
        tankDown2 = loader.loadImage("/playerTank2/tank-down.png");
        tankUp2 = loader.loadImage("/playerTank2/tank-up.png");
        tankLeft2 = loader.loadImage("/playerTank2/tank-left.png");
        tankRight2 = loader.loadImage("/playerTank2/tank-right.png");
        tankDownLeft2 = loader.loadImage("/playerTank2/tank-down-left.png");
        tankDownRight2 = loader.loadImage("/playerTank2/tank-down-right.png");
        tankUpLeft2 = loader.loadImage("/playerTank2/tank-up-left.png");
        tankUpRight2 = loader.loadImage("/playerTank2/tank-up-right.png");
        turretImage2 = loader.loadImage("/playerTank2/turret1.png");
        turretShootImage2 = loader.loadImage("/playerTank2/turretS1.png");
        turretImage3 = loader.loadImage("/playerTank2/turret1.png");
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void update() {

        setX(getX());
        setY(getY());

        actualXp2 = this.getX();
        actualYp2 = this.getY();

        setEnergy(TankGameEngine.LIFELEFT);

        nX = TankGameEngine.newX;
        nY = TankGameEngine.newY;

//        System.out.println("Delta X: " + (actualXp2 - nX));
//        System.out.println("Delta Y: " + (actualYp2 - nY));

        if (actualXp2 > nX && actualYp2 < nY) {
            direction = "as";
        } else if (actualXp2 < nX && actualYp2 < nY) {
            direction = "ds";
        } else if (actualXp2 > nX && actualYp2 > nY) {
            direction = "aw";
        } else if (actualXp2 < nX && actualYp2 > nY) {
            direction = "dw";
        }else if (actualXp2 < nX) {
            direction = "d";
        } else if (actualXp2 > nX) {
            direction = "a";
        } else if (actualYp2 < nY) {
            direction = "s";
        } else if (actualYp2 > nY) {
            direction = "w";
        }

        if(this.getX()>855){
            this.setX(855);
        }
        else if(this.getX()<2){
            this.setX(2);
        }
        else if(this.getY()>555){
            this.setY(555);
        }
        else if (this.getY()<2) {
            this.setY(2);
        }
    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

            if (tankImage2 != null) {

                g2d.drawImage(tankImage2, (int) getX(), (int) getY(), null);

                switch (direction) {
                    case "w":
                        tankImage2 = tankUp2;
                        break;
                    case "s":
                        tankImage2 = tankDown2;
                        break;
                    case "a":
                        tankImage2 = tankLeft2;
                        break;
                    case "d":
                        tankImage2 = tankRight2;
                        break;
                    case "as":
                        tankImage2 = tankDownLeft2;
                        break;
                    case "ds":
                        tankImage2 = tankDownRight2;
                        break;
                    case "aw":
                        tankImage2 = tankUpLeft2;
                        break;
                    case "dw":
                        tankImage2 = tankUpRight2;
                        break;
                }

                AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
                g2d.setTransform(at);

                g2d.drawImage(tankImage2, (int) getX(), (int) getY(), null);

                if(EnemyBulletHandler.MCLICKENEMYTURRETANIMATION){
                    turretImage2 = turretShootImage2;
                }else
                    turretImage2 = turretImage3;

                if (TankGameEngine.MOUSEXFROMSERVER != 0 && TankGameEngine.MOUSEYFROMSERVER != 0) {
                    double deltaX = (getX()) - TankGameEngine.MOUSEXFROMSERVER;
                    double deltaY = (getY()) - TankGameEngine.MOUSEYFROMSERVER;

                    rotation = Math.atan2(deltaY, deltaX) + Math.toRadians(180d);
                }

                if (tankImage2 == tankDown2) {
                    at.translate(8, 10);
                    at.rotate(rotation, getX() +10, getY()+10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage2, (int) getX(), (int) getY(), null);
                } else if (tankImage2 == tankRight2) {
                    at.translate(12, 7);
                    at.rotate(rotation, getX() +10, getY()+10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage2, (int) getX(), (int) getY(), null);
                } else if (tankImage2 == tankLeft2 || tankImage2 == tankUp2 ) {
                    at.translate(8, 7);
                    at.rotate(rotation, getX() +10, getY()+10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage2, (int) getX(), (int) getY(), null);
                } else if (tankImage2 == tankDownLeft2 || tankImage2 == tankDownRight2 || tankImage2 == tankUpLeft2 ||
                        tankImage2 == tankUpRight2) {
                    at.translate(15, 16);
                    at.rotate(rotation, getX() + 10, getY() + 10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage2, (int) this.getX(), (int) this.getY(), null);
                }
            }

        g2d.dispose();
    }

    public Rectangle initHitboxPlayer2(){
        int x = (int) this.getX();
        int y = (int) this.getY();
        int width = this.tankImage2.getWidth();
        int height = this.tankImage2.getHeight();
        if(tankImage2 == tankDown2 || tankImage2 == tankLeft2 || tankImage2 == tankRight2 || tankImage2 == tankUp2 ) {
            return hitbox2 = new Rectangle(x, y, width, height);
        }else {
            return hitbox2 = new Rectangle(x + 6,y + 6, width - 15,height - 15);
        }
    }

    public void drawHitbox2(Graphics g) {
        g.setColor(Color.pink);
        g.drawRect(hitbox2.x,hitbox2.y,hitbox2.width,hitbox2.height);
    }
}

