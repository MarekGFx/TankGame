package gameEngine;

import gameServer.GameServer;

/**
 *  Klasa GameLauncher - klasa rozszerzajaca klase Thread, do uruchomienia w oddzielnych watkach servera gry i klienta.
 *
 * @author Marek Gajdamowicz
 */
public class GameLauncher extends Thread {

    public static String ARG;


    /**
     * metoda uruchamiajaca
     * @param args argumenty przekazywane w trakcie uruchomienia programu
     */
    public static void main(String[] args) {

        if(args.length > 0) {
            ARG = args[0];
        }

        Thread thread1 = new Thread(() -> {
            GameServer gs = new GameServer();
            gs.acceptConnections();
        });

        Thread thread2 = new Thread(() -> {
            TankGameStarter tankGameStarter = new TankGameStarter();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

    }
}
