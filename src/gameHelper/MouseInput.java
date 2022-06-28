package gameHelper;

import gameObject.Bullet;
import gameObject.PlayerTank;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Klasa pomocnicza rozszezajaca MouseAdapter, sluzy do twozenia obiektu Bullet i dodania do kontenera BulletHandler.
 * @author Marek Gajdamowicz
 */
public class MouseInput extends MouseAdapter{

    private BulletHandler bulletHandler;

    /**
     * Konstruktor klasy MouseInput, zawierajacy obiekt innej pomocniczej klasy BulletHandler.
     * @param bulletHandler
     */
    public MouseInput(BulletHandler bulletHandler) {
        this.bulletHandler = bulletHandler;
    }

    /**
     * Metoda nasluchujaca nacisniecie przycisku, myszy, pobiera koordynaty x i y i tworzy obiekt
     * Bullet i dodaje go do kontenera BulletHandler.
     * @param e
     */
    public void mouseClicked(MouseEvent e) {

        int mX = e.getX() + 11;
        int mY = e.getY() + 11;

        try {
            Bullet tempBullet = bulletHandler.addBullet(new Bullet(PlayerTank.hitbox1.getCenterX(), PlayerTank.hitbox1.getCenterY(), bulletHandler));
            float angle = (float) Math.atan2(mY - PlayerTank.hitbox1.getCenterY(), mX - PlayerTank.hitbox1.getCenterX());
            int bulletVel = 9;
            tempBullet.velX = (bulletVel) * Math.cos(angle);
            tempBullet.velY = (bulletVel) * Math.sin(angle);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex){
            System.out.println("NullPointerExeption from MouseInput");
        }
    }
}
