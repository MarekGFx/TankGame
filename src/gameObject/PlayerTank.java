package gameObject;

import gameHelper.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *  Klasa obiektu PlayerTank dziedziczaca po klasie GameObject.
 *  @author Marek Gajdamowicz
 */
public class PlayerTank extends GameObject {  //extends GameObject

    public static boolean GAMEOVER = false;
    public static Rectangle hitbox1;
    int mouseX;
    int mouseY;
    private double actualXp1;
    private double actualYp1;
    private double transX = 0;
    private double transY = 0;
    private double rotation;
    private static double SPEED=0.9;

    String direction = "d";
    public BufferedImage tankImage1, tankDown1, tankUp1, tankLeft1, tankRight1,
            tankDownLeft1,tankDownRight1,tankUpLeft1, turretImage1,tankUpRight1,
            turretImageShoot1, turretImage2;
    private int energy;

    /**
     * Konstruktor Klasy PlayerTank.
     * @param x
     * @param y
     * @param energy energia (zycie gracza)
     * @throws IOException
     */
    public PlayerTank(double x, double y, int energy) throws IOException {
        super(x,y);
        this.energy = energy;

        BufferedImageLoader loader = new BufferedImageLoader();
        tankImage1 = loader.loadImage("/playerTank1/tank-down.png");
        tankDown1 = loader.loadImage("/playerTank1/tank-down.png");
        tankUp1 = loader.loadImage("/playerTank1/tank-up.png");
        tankLeft1 = loader.loadImage("/playerTank1/tank-left.png");
        tankRight1 = loader.loadImage("/playerTank1/tank-right.png");
        turretImage1 = loader.loadImage("/playerTank1/turret.png");
        turretImage2 = loader.loadImage("/playerTank1/turret.png");
        tankDownLeft1 = loader.loadImage("/playerTank1/tank-down-left.png");
        tankDownRight1 = loader.loadImage("/playerTank1/tank-down-right.png");
        tankUpLeft1 = loader.loadImage("/playerTank1/tank-up-left.png");
        tankUpRight1 = loader.loadImage("/playerTank1/tank-up-right.png");
        turretImageShoot1 = loader.loadImage("/playerTank1/turretS1.png");
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * Metoda update() aktualizuje pozycje gracza i ilosc energi jak pozostala do zakonczenia rozgrywki.
     * Dodatkowo aktualizuje parametr String direction, ktory w metodzie render() odpowiada za wyswietlanie
     * odpowiednej grafiki obiektu PlayerTank.
     */
    public void update() {

        setX(getX() + transX);
        setY(getY() + transY);

        actualXp1 = this.getX();
        actualYp1 = this.getY();

        if (actualXp1 > getX() + transX && actualYp1 < getY() + transY) {
            direction = "as";
        } else if (actualXp1 < getX() + transX && actualYp1 < getY() + transY) {
            direction = "ds";
        } else if (actualXp1 > getX() + transX && actualYp1 > getY() + transY) {
            direction = "aw";
        } else if (actualXp1 < getX() + transX && actualYp1 > getY() + transY) {
            direction = "dw";
        } else if (actualXp1 < getX() + transX) {
            direction = "d";
        } else if (actualXp1 > getX() + transX) {
            direction = "a";
        } else if (actualYp1 < getY() + transY) {
            direction = "s";
        } else if (actualYp1 > getY() + transY) {
            direction = "w";
        }

        if(this.getX()>855){
            this.setX(855);
            this.setTransX(0);
            this.setTransY(0);
        }
        else if(this.getX()<2){
            this.setX(2);
            this.setTransX(0);
            this.setTransY(0);
        }
        else if(this.getY()>555){
            this.setY(555);
            this.setTransY(0);
            this.setTransX(0);
        }
        else if (this.getY()<20) {
            this.setY(20);
            this.setTransY(0);
            this.setTransX(0);
        }

        if (EnemyBulletHandler.IF_ENEMY_TANK_HIT) {
            setEnergy(getEnergy() - 1);
            EnemyBulletHandler.IF_ENEMY_TANK_HIT = false;
            if(getEnergy() == 0){
                GAMEOVER = true;
            }
        }
    }

    /**
     * Metoda wyswietlajaca grafike obiektu PlayerTank w glownym watku gry, w zaleznoosci od
     * kierunku poruszania sie po planszy gry. Dodatkowo wrysowuje w grafike obiektu PlayerTank, grafike wiezyczki
     * ktora obraca sie w osi ustalonej na obiekcie PlayerTank.
     * @param g
     * @throws InterruptedException
     */
    public void render(Graphics g) throws InterruptedException {
        Graphics2D g2d = (Graphics2D) g.create();

            if (tankImage1 != null) {

                g2d.drawImage(tankImage1, (int) getX(), (int) getY(), null);

                switch (direction) {
                    case "w":
                        tankImage1 = tankUp1;
                        break;
                    case "s":
                        tankImage1 = tankDown1;
                        break;
                    case "a":
                        tankImage1 = tankLeft1;
                        break;
                    case "d":
                        tankImage1 = tankRight1;
                        break;
                    case "as":
                        tankImage1 = tankDownLeft1;
                        break;
                    case "ds":
                        tankImage1 = tankDownRight1;
                        break;
                    case "aw":
                        tankImage1 = tankUpLeft1;
                        break;
                    case "dw":
                        tankImage1 = tankUpRight1;
                        break;
                }

                AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
                g2d.setTransform(at);

                g2d.drawImage(tankImage1, (int)getX(), (int)getY(), null);

                if ( mouseX != 0 && mouseY != 0) {
                    double deltaX = (getX()) - mouseX;
                    double deltaY = (getY()) - mouseY;

                    rotation = Math.atan2(deltaY, deltaX) + Math.toRadians(180d);
                }

                if (BulletHandler.MCLICKTURRETANIMATION) {
                    turretImage1 = turretImageShoot1;
                }else
                    turretImage1 = turretImage2;

                if (tankImage1 == tankDown1) {
                    at.translate(8, 10);
                    at.rotate(rotation, getX()+10, getY()+10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage1, (int) this.getX(), (int) this.getY(), null);
                } else if (tankImage1 == tankRight1) {
                    at.translate(12, 7);
                    at.rotate(rotation, getX()+10, getY()+10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage1, (int) this.getX(), (int) this.getY(), null);
                } else if (tankImage1 == tankLeft1 || tankImage1 == tankUp1 ) {
                    at.translate(8, 7);
                    at.rotate(rotation, getX()+10, getY()+10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage1, (int) this.getX(), (int) this.getY(), null);
                } else if (tankImage1 == tankDownLeft1 || tankImage1 == tankDownRight1 || tankImage1 == tankUpLeft1||
                          tankImage1 == tankUpRight1) {
                    at.translate(15, 16);
                    at.rotate(rotation, getX() + 10, getY() + 10);
                    g2d.setTransform(at);
                    g2d.drawImage(turretImage1, (int) this.getX(), (int) this.getY(), null);
                }
            }

        g2d.dispose();
    }

    /**
     * Metda zwracajaca obiekt typu Rectangle, sluzacy do sprawdzania i obslugi kolizji z pozostalymi obiektami gry.
     * @return Rectangle
     */
    public Rectangle initHitboxPlayer1(){
        int x = (int) this.getX();
        int y = (int) this.getY();
        int width = this.tankImage1.getWidth();
        int height = this.tankImage1.getHeight();

        if(tankImage1 == tankDown1 || tankImage1 == tankLeft1 || tankImage1 == tankRight1 || tankImage1 == tankUp1 ) {
            return hitbox1 = new Rectangle(x, y, width, height);
        }else {
            return hitbox1 = new Rectangle(x + 6,y + 6, width - 15,height - 15);
        }

    }

    /**
     * Metoda obslugujaca kolizje miedzy obiektami PlayerTank i EnemyPlayerTank.
     */
    public void checkTankColision() {
        if (hitbox1.intersects(EnemyPlayerTank.hitbox2)){
            System.out.println("kolizja");
            this.setTransX(0);
            this.setTransY(0);

        }
    }

    /**
     * Metoda obsugujaca kolizje midzy obiektem PlayerTank a obiektami klasy Shields
     * @param r obiekt typu Ractangle, zwrocony przez metode initHitboxShield() klasy Shields.
     */
    public void checkShieldColisionPlayer1(Rectangle r){
        if (hitbox1.intersects(r)) {
            if(transX > 0 && transY >0){
                setTransX(0);
                setTransY(0);
            } else if(transX < 0 && transY < 0 ) {
                setTransX(0);
                setTransY(0);
            } else if(transX < 0 && transY >0){
                setTransX(0);
                setTransY(0);
            }else if(transX > 0 && transY < 0){
                setTransX(0);
                setTransY(0);
            } else if (transX > 0) {
                 setTransX(0);
                 setX(r.getX() - hitbox1.width);
            } else if (transX < 0) {
                 setTransX(0);
                 setX(r.getX() + r.width);
            } else if (transY > 0) {
                 setTransY(0);
                 setY(r.getY() - hitbox1.height);
            } else if (transY < 0) {
                 setTransY(0);
                 setY(r.getY() + r.height);
            }
         }

    }

    /**
     * Metoda suzaca do rysowania obiektu typu Rectangle zwrocony przez metode initHitboxPlayer1(),
     * uzywana do testow
     * @param g
     */
    public void drawHitbox1(Graphics g) {
        g.setColor(Color.pink);
        g.drawRect(hitbox1.x,hitbox1.y,hitbox1.width,hitbox1.height);
    }

    public  void setTransX(double transX) {

        this.transX = transX;
    }

    public void setTransY(double transY) {
        this.transY = transY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_D) {
            this.setTransX(SPEED);
        } else if (key == KeyEvent.VK_A) {
            this.setTransX(-SPEED);
        } else if (key == KeyEvent.VK_S) {
            this.setTransY(SPEED);
        } else if (key == KeyEvent.VK_W) {
            this.setTransY(-SPEED);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_D) {
            this.setTransX(0);
        } else if (key == KeyEvent.VK_A) {
            this.setTransX(0);
        } else if (key == KeyEvent.VK_S) {
            this.setTransY(0);
        } else if (key == KeyEvent.VK_W) {
            this.setTransY(0);
        }
    }

    public void mouseClicked(MouseEvent e){
        mouseX = e.getX();
        mouseY = e.getY();



    }
}

