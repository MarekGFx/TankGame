package tests.java;

import gameHelper.BulletHandler;
import gameObject.Bullet;
import gameObject.EnemyPlayerTank;
import gameObject.Shields;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Klasa testujaca moodul kontenera na pociski
 * @author Marek Gajdamowicz
 */
public class BulletHandlerTest {

    /**
     * Test dodawania i usuwania pociskow z kontenera.
     * @throws IOException
     */
    @Test
    public void simpleAddBulletAndRemove() throws IOException {
        BulletHandler bulletHandler = new BulletHandler();
        Bullet bullet = new Bullet(50,100,bulletHandler);
        bulletHandler.addBullet(bullet);

        assertFalse(bulletHandler.isEmpty());
        assertEquals(1,bulletHandler.size());

        bulletHandler.removeBullet(bullet);
        assertTrue(bulletHandler.isEmpty());
        assertEquals(0,bulletHandler.size());
    }

    /**
     * Test dodawania nowego obiektu Bullet, tylko gdy kontener jest pusty.
     * @throws IOException
     */
    @Test
    public void addNewBulletOnlyIfHandlerEmpty() throws IOException {
        BulletHandler bulletHandler = new BulletHandler();
        Bullet bullet1 = new Bullet(50,100,bulletHandler);
        Bullet bullet2 = new Bullet(150,100,bulletHandler);
        Bullet bullet3 = new Bullet(167,200,bulletHandler);

        bulletHandler.addBullet(bullet1);
        bulletHandler.addBullet(bullet2);
        bulletHandler.addBullet(bullet3);

        assertEquals(1,bulletHandler.size());
        assertEquals(bullet1,bullet1);

        bulletHandler.removeBullet(bullet1);

        assertEquals(0,bulletHandler.size());

        bulletHandler.addBullet(bullet2);
        bulletHandler.addBullet(bullet3);

        assertEquals(1,bulletHandler.size());
        assertEquals(bullet2,bullet2);
    }

    /**
     * Test usuwania obiektu Bullet z kontenera gdy znajdzie poza obszarem okna gry.
     * @throws IOException
     */
    @Test
    public void removeIfBulletOutsideFrame() throws IOException {
        BulletHandler bulletHandler = new BulletHandler();
        Bullet bullet1 = new Bullet(70,100,bulletHandler);

        bulletHandler.addBullet(bullet1);
        assertEquals(1,bulletHandler.size());

        bullet1.setX(1000);
        bulletHandler.update();

        assertEquals(0,bulletHandler.size());

        Bullet bullet2 = new Bullet(700,150,bulletHandler);

        bulletHandler.addBullet(bullet2);
        assertEquals(1,bulletHandler.size());

        bullet2.setY(700);
        bulletHandler.update();

        assertEquals(0,bulletHandler.size());
    }

    /**
     * Test usuwania obiektu Bullet z kontenera gdy nastapi kolizja z obiektem klasy EnemyPlayerTank.
     * @throws IOException
     */
    @Test
    public void removeIfBulletCollisionEnemyPlayer() throws IOException {
        BulletHandler bulletHandler = new BulletHandler();
        Bullet bullet1 = new Bullet(70,100,bulletHandler);

        bulletHandler.addBullet(bullet1);
        assertEquals(1,bulletHandler.size());

        EnemyPlayerTank enemyPlayerTank = new EnemyPlayerTank(70,100,10);
        enemyPlayerTank.initHitboxPlayer2();

        bulletHandler.checkBulletCollisionEnemyPlayer();

        assertEquals(0,bulletHandler.size());
    }
    /**
     * Test usuwania obiektu Bullet z kontenera gdy nastapi kolizja z obiektem klasy Shields.
     * @throws IOException
     */
    @Test
    public void removeIfBulletCollisionShield() throws IOException {
        BulletHandler bulletHandler = new BulletHandler();
        Bullet bullet1 = new Bullet(70,100,bulletHandler);

        bulletHandler.addBullet(bullet1);
        assertEquals(1,bulletHandler.size());

        Shields shield = new Shields(70,100,"sandbagH");

        bulletHandler.checkBulletCollisionShields(shield.initHitboxShield());

        assertEquals(0,bulletHandler.size());
    }


}
