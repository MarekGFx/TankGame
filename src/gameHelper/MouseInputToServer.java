package gameHelper;

import gameEngine.TankGameEngine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Klasa pomocnicza rozszezajaca interfejs MouseAdapter
 * @author Marek Gajdamowicz
 */
public class MouseInputToServer extends MouseAdapter {

    TankGameEngine tankGame;

    public MouseInputToServer(TankGameEngine tankGame) {
        this.tankGame = tankGame;
    }

    public void mouseClicked(MouseEvent e) {
        try {
            tankGame.mouseClicked(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}