package gameServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa Serwera
 * @author Marek Gajdamowicz
 */
public class GameServer {

    public static Boolean SECONDPLAYERLEFTGAME = false;

    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    private Socket p1Socket;
    private Socket p2Socket;

    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;

    private double p1x, p1y, p2x, p2y;
    private int m1x, m1y, m2x, m2y, energy1, energy2;

    private boolean mclick1, mclick2, gameover1, gameover2, leftGame1, leftGame2;

    /**
     * Konstruktor serwera, tworzy nowy socked.
     */
    public GameServer() {
        System.out.println("==== GAME SERVER ====");
        numPlayers = 0;
        maxPlayers = 2;

        p1x = 30;
        p1y = 250;
        p2x = 830;
        p2y = 250;


        try {
            ss = new ServerSocket(45371);
        } catch (IOException ex) {
            System.out.println("IOExeption from GameServer constructor");
        }
    }

    /**
     * Metoda oczekujaca na polaczenie dwoch klientow, po polaczeniu dwoch graczy tworzy obiekty odczytujace
     * dane od klienta i wysylajace do kienta dane, w dwoch wtkach dla kazdego podlczonego gracza.
     */
    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");

            while (numPlayers < maxPlayers) {
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has connected.");

                ReadFromClient readFromClient = new ReadFromClient(numPlayers, in);
                WriteToClient writeToClient = new WriteToClient(numPlayers, out);

                if (numPlayers == 1) {
                    p1Socket = s;
                    p1ReadRunnable = readFromClient;
                    p1WriteRunnable = writeToClient;
                } else {
                    p2Socket = s;
                    p2ReadRunnable = readFromClient;
                    p2WriteRunnable = writeToClient;
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();
                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);
                    readThread1.start();
                    readThread2.start();
                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);
                    writeThread1.start();
                    writeThread2.start();
                }
            }

            System.out.println("No longer accepting connections");

        } catch (IOException ex) {
            System.out.println("IOExeption from acceptConnections()");
        } catch (NullPointerException ex){
            System.out.println("Wykryto już działający server oczekujący na połączenie");
            return;
        }
    }

    /**
     * Klasa prywatna odczytujaca dane od klienta implementujaca interfejs Runnaable.
     */
    private class ReadFromClient implements Runnable {

        private int playerID;
        private DataInputStream dataIn;

        /**
         * Konstruktor klasy prywatnej
         * @param playerID identyfikator gracza,
         * @param dataIn strumien danych wysylanych przez kienta na serwer
         */
        public ReadFromClient(int playerID, DataInputStream dataIn) {
            this.playerID = playerID;
            this.dataIn = dataIn;
            System.out.println("RFC" + playerID + " Runnable created");
        }

        /**
         * Metod run() wywolywanaa przez start watku, po akceptacji polaczenia.
         */
        @Override
        public void run() {
            try {

                while (true) {
                    if (playerID == 1) {
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();
                        m1x = dataIn.readInt()+11;
                        m1y = dataIn.readInt()+11;
                        energy1 = dataIn.readInt();
                        gameover1 = dataIn.readBoolean();
                        leftGame1 =dataIn.readBoolean();
                        mclick1 = dataIn.readBoolean();

                    } else {
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();
                        m2x = dataIn.readInt()+11;
                        m2y = dataIn.readInt()+11;
                        energy2 = dataIn.readInt();
                        gameover2 = dataIn.readBoolean();
                        leftGame2 = dataIn.readBoolean();
                        mclick2 = dataIn.readBoolean();
                    }
                }

            } catch (IOException ex) {
                SECONDPLAYERLEFTGAME = true;
                System.out.println("IOException from ReadFromClient run()");
            }
        }
    }

    /**
     * Klasa prywatna wysylajaca dane do klienta implementujaca interfejs Runnaable.
     */
    private class WriteToClient implements Runnable {

        private int playerID;
        private DataOutputStream dataOut;

        /**
         * Konstruktor klasy prywatnej
         * @param playerID identyfikator gracza
         * @param dataOut strumien danych wysylanych z serwera do klienta
         */
        public WriteToClient(int playerID, DataOutputStream dataOut) {
            this.playerID = playerID;
            this.dataOut = dataOut;
            System.out.println("WTC" + playerID + " Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (playerID == 1) {
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);
                        dataOut.writeInt(m2x);
                        dataOut.writeInt(m2y);
                        dataOut.writeInt(energy2);
                        dataOut.writeBoolean(gameover2);
                        dataOut.writeBoolean(SECONDPLAYERLEFTGAME);
                        dataOut.writeBoolean(mclick2);
                        dataOut.flush();
                    } else {
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);
                        dataOut.writeInt(m1x);
                        dataOut.writeInt(m1y);
                        dataOut.writeInt(energy1);
                        dataOut.writeBoolean(gameover1);
                        dataOut.writeBoolean(SECONDPLAYERLEFTGAME);
                        dataOut.writeBoolean(mclick1);
                        dataOut.flush();
                    }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException from WriteToClient run()");
                    }
                }
            } catch (IOException ex) {
                SECONDPLAYERLEFTGAME = true;
                System.out.println("IOException from WriteToClient run()");
            }
        }

        /**
         * Metoda wysylajaca komunikat do kientow o podlaczeniu dwoch graczy
         */
        public void sendStartMsg(){
            try {
                dataOut.writeUTF("We now have 2 player. Go!");
            } catch (IOException ex){
                System.out.println("IOException from sendStartMsg()");
            }
        }
    }
}