package gameHelper;

import gameEngine.TankGameEngine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Klasa pomocnicza, rozszezajaca klase KeyAdapter z metodami keyPressed, keyReleased.
 * @author Marek Gajdamowicz
 */
public class KeyInput extends KeyAdapter {

    TankGameEngine tankGame;

    /**
     * Konstruktor klasy pomocniczej.
     * @param tankGame
     */
    public KeyInput(TankGameEngine tankGame){
        this.tankGame = tankGame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        tankGame.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        tankGame.keyReleased(e);
    }
}
