package gameObject;

import gameHelper.BufferedImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Klasa Shields dziedziczaca po klasie  GameObject.
 * @author Marek Gajdamowicz
 */
public class Shields extends GameObject{

    public static Rectangle hitboxS;
    public BufferedImage destroyedTankH, destroyedTankV,destroyedTurretH,
                        destroyedTurretV,truckH, truckV,sandbagH,sandbagV;
    public BufferedImage shield;
    private String shieldType;  //    destroyedTankH, destroyedTankV,destroyedTurretH,
                                //    destroyedTurretV,truckH, truckV,sandbagH,sandbagV;

    /**
     * Konstruktor klasy Shields
     * @param x polozenie obiektu wzgledem osi x
     * @param y polozenie obiektu wzgledem osi y
     * @param shieldType okresla typ oslony, musi zawierac okreslona nazwe: destroyedTankH, destroyedTankV,
     *                                                                      destroyedTurretH, destroyedTurretV,  truckH,
     *                                                                      truckV, sandbagH, sandbagV.
     * @throws IOException
     */
    public Shields(double x, double y, String shieldType) throws IOException {
        super((int)x, (int)y);
        this.shieldType = shieldType; // destroyedTankH, destroyedTankV,  destroyedTurretH, destroyedTurretV,  truckH,
                                      // truckV, sandbagH, sandbagV

        BufferedImageLoader loader = new BufferedImageLoader();
        shield = loader.loadImage("/shield/sandbagH.png");
        destroyedTankH = loader.loadImage("/shield/destroyedTankH.png");
        destroyedTankV = loader.loadImage("/shield/destroyedTankV.png");
        destroyedTurretH = loader.loadImage("/shield/destroyedTurretH.png");
        destroyedTurretV = loader.loadImage("/shield/destroyedTurretV.png");
        truckH = loader.loadImage("/shield/truckH.png");
        truckV = loader.loadImage("/shield/truckV.png");
        sandbagH = loader.loadImage("/shield/sandbagH.png");
        sandbagV = loader.loadImage("/shield/sandbagV.png");
    }

    /**
     * Rysule grafike obiektu Shields w zaleznosci od nazwy parametru shieldType.
     * @param g
     */
    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        if (Objects.equals(getShieldType(), "destroyedTankH")) {
            shield = destroyedTankH;
        } else if (Objects.equals(getShieldType(), "destroyedTankV")) {
            shield = destroyedTankV;
        } else if (Objects.equals(getShieldType(), "destroyedTurretH")) {
            shield = destroyedTurretH;
        } else if (Objects.equals(getShieldType(), "destroyedTurretV")) {
            shield = destroyedTurretV;
        } else if (Objects.equals(getShieldType(), "truckH")) {
            shield = truckH;
        } else if (Objects.equals(getShieldType(), "truckV")) {
            shield = truckV;
        } else if (Objects.equals(getShieldType(), "sandBagH")){
            shield = sandbagH;
        }else if (Objects.equals(getShieldType(), "sandBagV")){
            shield = sandbagV;
        }
        g.drawImage(shield, (int) getX(), (int) getY(), null);
        g2d.dispose();
    }

    /**
     * Metoda zwraca obiekt typu Ractangle, sluzy do obslugi kolizji z ninnymi obiektami gry.
     * @return Ractangle
     */
    public Rectangle  initHitboxShield(){
        int x = (int) this.getX();
        int y = (int) this.getY();
        int width = this.shield.getWidth();
        int height = this.shield.getHeight();
        return hitboxS = new Rectangle(x, y, width, height);
    }

    public void drawHitboxShields(Graphics g) {
        g.setColor(Color.pink);
        g.drawRect(hitboxS.x,hitboxS.y,hitboxS.width,hitboxS.height);
    }

    public String getShieldType () {
            return shieldType;
    }

    public void setShieldType (String shieldType){
            this.shieldType = shieldType;
    }
}
