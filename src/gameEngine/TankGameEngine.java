package gameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import gameHelper.*;
import gameObject.*;
import gameServer.GameServer;

/**
 * Klasa Silnik gry
 *
 * @author Marek Gajdamowicz
 */
public class TankGameEngine extends Canvas implements Runnable {

    ArrayList<Shields> shieldsList = new ArrayList<Shields>();
    Sound sound = new Sound();
    private boolean running = false;
    private static int LIFE = 10;
    public static int LIFELEFT;
    private Thread thread;
    private static int mX;
    private static int mY;
    private int counter;
    public static int MOUSEXFROMSERVER;
    public static int MOUSEYFROMSERVER;
    private boolean mclick;
    private boolean gameover;
    private boolean secondClientPlayerGetHit = false;
    private boolean count = true;
    private boolean secondPlayerLeftGame = false;
    private PlayerTank thisClientPlayer;
    private EnemyPlayerTank secondClientPlayer;
    private Shields shieldsTankH,shieldsTankH1 ;
    private Shields shieldsTankV,shieldsTankV1;
    private Shields shieldTruckH,shieldTruckH1;
    private Shields shieldTruckV,shieldTruckV1;
    private Shields shieldsTurretH,shieldsTurretH1;
    private Shields shieldsTurretV;
    private Shields sandbagV,sandbagV1,sandbagV2,sandbagV3,sandbagV4,sandbagV5;
    private BulletHandler bulletHandler;
    private EnemyBulletHandler enemyBulletHandler;
    private ExplosionsAnimationHandler explosionsAnimationHandler;
    ExplosionsAnimation tempExplosion;
    public static double newX;
    public static double newY;
    public static int MX;
    public static int MY;
    int FPS = 60;

    //----------------------------------------------- do połączenia z serwerem ----------------------------------------------------
    private Socket socket;
    private String host;
    private int playerID;
    private ReadFromServer readFromServerRunnable;
    private WriteToServer writeToServerRunnable;
    //------------------------------------------------------------------------------------------------------------------

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    BufferedImageLoader loader = new BufferedImageLoader();
    private BufferedImage battlegrid, cursor;

    {
        try {
            cursor = loader.loadImage("/cursor/cursor.png");
            battlegrid = loader.loadImage("/battlegrid/battlegrid.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda tworzaca obiekty czolgow w zaleznosci od pierwszenstwa polaczenia z serwerem.
     * @throws IOException
     */
    private void createPlayers() throws IOException {
        if(playerID==1){
            thisClientPlayer = new PlayerTank(30, 250, LIFE);
            secondClientPlayer = new EnemyPlayerTank(830, 250,LIFE);
        }
        else {
            secondClientPlayer = new EnemyPlayerTank(30, 250,LIFE);
            thisClientPlayer= new PlayerTank(830, 250, LIFE);
        }
    }

    /**
     * Metoda inicjujaca wszystkie obiekty w grze i klasy wspomagajace.
     * @throws IOException
     * @throws InterruptedException
     */
    public void initialize() throws IOException, InterruptedException {
        sound.playMusic(0);
        explosionsAnimationHandler = new ExplosionsAnimationHandler();
        bulletHandler = new BulletHandler();
        enemyBulletHandler = new EnemyBulletHandler();
        this.addMouseListener(new MouseInput(bulletHandler));
        addKeyListener(new KeyInput(this));
        addMouseListener(new MouseInputToServer(this));
        createPlayers();
        shieldsList.add(shieldsTankH = new Shields(500, 400, "destroyedTankH"));
        shieldsList.add(shieldsTankH1 = new Shields(300, 80, "destroyedTankH"));
        shieldsList.add(shieldsTankV = new Shields(350, 300, "destroyedTankV"));
        shieldsList.add(shieldsTankV1 = new Shields(680, 440, "destroyedTankV"));
        shieldsList.add(shieldTruckH = new Shields(600, 250, "truckH"));
        shieldsList.add(shieldTruckH1 = new Shields(150, 450, "truckH"));
        shieldsList.add(shieldTruckV = new Shields(200, 200, "truckV"));
        shieldsList.add(shieldTruckV1 = new Shields(650, 110, "truckV"));
        shieldsList.add(shieldsTurretH = new Shields(520, 445, "destroyedTurretH"));
        shieldsList.add(shieldsTurretH1 = new Shields(690, 445, "destroyedTurretH"));
        shieldsList.add(shieldsTurretV = new Shields(385, 350, "destroyedTurretV"));
        shieldsList.add(sandbagV = new Shields(90, 200, "sandBagV"));
        shieldsList.add(sandbagV1 = new Shields(90, 252, "sandBagV"));
        shieldsList.add(sandbagV2 = new Shields(90, 304, "sandBagV"));
        shieldsList.add(sandbagV3 = new Shields(780, 200, "sandBagV"));
        shieldsList.add(sandbagV4 = new Shields(780, 252, "sandBagV"));
        shieldsList.add(sandbagV5 = new Shields(780, 304, "sandBagV"));
    }

    /**
     * Metota startujaca glowny watek gry.
     */
    synchronized void start(){
        if(running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Metoda zatrzymujaca glowny watek gry i zamykajaca program.
     */
    private synchronized void stop() {
        if (!running)
             return;
             running = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(1);
    }

    /**
     * Metoda run() glownego watku gry, w ktorej uruchamiana jest metoda initialize(), nastepnie w petli dopoki
     * nie zostanie przerwana przez skonczenie albo wyjscie z gry, uruchamiane są metody update() i render().
     * petla zawiera algorytm ograniczajacy rysowanie liczby ramek animacji na sekunde do 60.
     */
    public void run(){
        try {
            initialize();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (running){
            try {
                currentTime = System.nanoTime();
                delta +=(currentTime - lastTime)/drawInterval;
                lastTime = currentTime;

                if(delta >= 1) {
                    update();
                    render();
                    delta--;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    /**
     * Metoda aktualizujaca polozenie obiektow, metod klas pomocniczych (w tym kolizji obiektow), warunkow zwyciestwa,
     * przegranej i naglego opuszczenia gry przez przeciwnika.
     */
    private void update() {// throws IOException, InterruptedException, NullPointerException {
        try {
            if (!count) {

                explosionsAnimationHandler.update();
                if (BulletHandler.EXPLOSIONANIMATION) {
                    tempExplosion = explosionsAnimationHandler.addExplosion(new ExplosionsAnimation(
                            BulletHandler.SEND_X_EXPLOSION - 10,
                            BulletHandler.SEND_Y_EXPLOSION - 10,
                            explosionsAnimationHandler));
                    BulletHandler.EXPLOSIONANIMATION = false;
                }

                if (EnemyBulletHandler.EXPLOSIONANIMATION) {
                    tempExplosion = explosionsAnimationHandler.addExplosion(new ExplosionsAnimation(
                            EnemyBulletHandler.SEND_X_EXPLOSION - 10,
                            EnemyBulletHandler.SEND_Y_EXPLOSION - 10,
                            explosionsAnimationHandler));
                    EnemyBulletHandler.EXPLOSIONANIMATION = false;
                }

                thisClientPlayer.update();
                secondClientPlayer.update();
                thisClientPlayer.initHitboxPlayer1();
                secondClientPlayer.initHitboxPlayer2();
                thisClientPlayer.checkTankColision();
                bulletHandler.update();
                enemyBulletHandler.update();
                bulletHandler.checkBulletCollisionEnemyPlayer();
                enemyBulletHandler.checkEnemyBulletCollisionPlayer();

                for (Shields shields : shieldsList) {
                    thisClientPlayer.checkShieldColisionPlayer1(shields.initHitboxShield());
                    bulletHandler.checkBulletCollisionShields(shields.initHitboxShield());
                    enemyBulletHandler.checkBulletCollisionShields(shields.initHitboxShield());
                }

                if (secondPlayerLeftGame || GameServer.SECONDPLAYERLEFTGAME) {
                    JOptionPane.showMessageDialog(null, "Przeciwnik opuścił pole bitwy,\n " +
                                    "KONIEC GRY", null,
                                    JOptionPane.INFORMATION_MESSAGE);
                    running = false;
                }

                if (PlayerTank.GAMEOVER && gameover){
                    JOptionPane.showMessageDialog(null, "Remis,\n KONIEC GRY", null,
                            JOptionPane.INFORMATION_MESSAGE);
                    running = false;
                }
                else if (PlayerTank.GAMEOVER) {
                    JOptionPane.showMessageDialog(null, "Wróg wygrywa,\n KONIEC GRY", null,
                            JOptionPane.INFORMATION_MESSAGE);
                    running = false;
                }
                else if (gameover) {
                    JOptionPane.showMessageDialog(null, "Wygrywasz,\n KONIEC GRY", null,
                            JOptionPane.INFORMATION_MESSAGE);
                    running = false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("NullPionterException from update");
        }
    }

    /**
     * Metoda umieszczona w petli metody run() rysujaca scene gry na podstawie aktualnych danych z metody update().
     * @throws InterruptedException
     */
    private void render() throws InterruptedException {

        if(count) {
            for (counter = 5; counter >= 0; counter--) {
                BufferStrategy bs = this.getBufferStrategy();
                if (bs == null) {
                    createBufferStrategy(3);
                    return;
                }
                Graphics g = bs.getDrawGraphics();
                g.drawImage(battlegrid, 0, 0, getWidth(), getHeight(), this);
                g.setFont(new Font("serif", Font.BOLD, 60));
                g.setColor(Color.BLACK);
                g.drawString("START ZA  " + counter, 300, 250);
                g.dispose();
                bs.show();
                Thread.sleep(1000);
                if (counter == 0)
                    count = false;
            }
        }

        BufferStrategy bs =this.getBufferStrategy();
        if (bs==null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        //////////////////////////////////////pomiędzy rysowanie sceny gry
        g.drawImage(battlegrid, 0,0,getWidth(),getHeight(),this);

        g.setFont(new Font("serif",Font.BOLD, 15));
        if(playerID == 1) {
            g.setColor(Color.GREEN);
            g.drawString("GREEN TANK HP : " + thisClientPlayer.getEnergy(), 7, 15);
            g.setColor(Color.RED);
            g.drawString("RED TANK HP: " + secondClientPlayer.getEnergy(), 740, 15);
        }else {
            g.setColor(Color.GREEN);
            g.drawString("GREEN TANK HP : " + thisClientPlayer.getEnergy(), 740, 15);
            g.setColor(Color.RED);
            g.drawString("RED TANK HP: " + secondClientPlayer.getEnergy(), 7, 15);
        }

        for (Shields shields : shieldsList) {
            shields.render(g);
        }

        thisClientPlayer.render(g);
        secondClientPlayer.render(g);
        bulletHandler.render(g);
        enemyBulletHandler.render(g);
        explosionsAnimationHandler.render(g);
        //////////////////////////////////////////////////////////////////
        g.dispose();
        bs.show();
    }

    /**
     * Metoda sluzy do polaczenia z serwerem, w niej tworzone sa obiekty prywatnych klas, odczytujaca i zapisujaca dane
     * na serwer.
     */
    public void connectToServer(){
        if(GameLauncher.ARG != null) {
            host = GameLauncher.ARG;
        } else {
            host = "localhost";
        }
        try{
            socket = new Socket(host,45371);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are player#" + playerID);
            if (playerID == 1){
                System.out.println("Waiting for Player #2 to connect");
            }
            readFromServerRunnable = new ReadFromServer(in);
            writeToServerRunnable = new WriteToServer(out);
            readFromServerRunnable.waitForStartMsg();
        } catch (IOException ex){
            System.out.println("IOException from connectToServer()");
        }
    }

    /**
     * Prywatna klasa odczytujaca dane z serwera, implemetujaca interfejs Runnable
     */
    private class ReadFromServer implements Runnable{ //---------wątek wczytujący z serwera
        private  DataInputStream dataIn;

        /**
         * Konstruktor klasy prywatnej ReadFromServer
         * @param dataIn dane odczytane z serwera
         */
        public ReadFromServer(DataInputStream dataIn){
            this.dataIn = dataIn;
            System.out.println("RFS Runnable created");
        }

        /**
         * Metoda run() interfejsu Runnable, ktora odczytuje w petli dane wychodzace z serwera.
         */
        @Override
        public void run() {
            try {
                while (true){
                    if (secondClientPlayer != null) {
                        secondClientPlayer.setX(dataIn.readDouble());
                        secondClientPlayer.setY(dataIn.readDouble());
                        newX = dataIn.readDouble();
                        newY = dataIn.readDouble();
                        mX = dataIn.readInt();
                        mY = dataIn.readInt();
                        LIFELEFT = dataIn.readInt();
                        gameover = dataIn.readBoolean();
                        secondPlayerLeftGame = dataIn.readBoolean();
                        mclick = dataIn.readBoolean();
                        while (mclick) {
                            EnemyBullet tempBullet1 = enemyBulletHandler.addEnemyBullet(new EnemyBullet(
                                    secondClientPlayer.initHitboxPlayer2().getCenterX(),
                                    secondClientPlayer.initHitboxPlayer2().getCenterY(),
                                    enemyBulletHandler));
                            float angle = (float) Math.atan2(mY - secondClientPlayer.initHitboxPlayer2().getCenterY(),
                                    mX - secondClientPlayer.initHitboxPlayer2().getCenterX());
                            int bulletVel = 9;
                            tempBullet1.velX = (bulletVel) * Math.cos(angle);
                            tempBullet1.velY = (bulletVel) * Math.sin(angle);

                            MOUSEXFROMSERVER = mX;
                            MOUSEYFROMSERVER = mY;
                            mclick = false;
                        }
                    }
                    dataIn.readDouble();
                    dataIn.readDouble();
                    newX = dataIn.readDouble();
                    newY = dataIn.readDouble();
                    mX = dataIn.readInt();
                    mY = dataIn.readInt();
                    LIFELEFT = dataIn.readInt();
                    gameover = dataIn.readBoolean();
                    secondPlayerLeftGame = dataIn.readBoolean();
                    mclick = dataIn.readBoolean();

                }
            } catch (IOException ex) {
                secondPlayerLeftGame = true;
                System.out.println("IOException from ReadFromServer run()");
            }
        }

        /**
         * Metoda oczekujaca na wiadomosc z serwera o polaczeniu dwoch graczy, wtedy zostaja uruchomione watki
         * zapisujacy i odczytujacy na serwer.
         */
        public void waitForStartMsg(){
            try{
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                Thread readThread = new Thread(readFromServerRunnable);
                Thread writeThread = new Thread(writeToServerRunnable);
                readThread.start();
                writeThread.start();
            } catch (IOException ex){
                System.out.println("IOException from ReadFromServer waitForStartMsg()");
            }
        }
    }

    /**
     * Metoda prywatna wysylajaca dane klienta na serwer, implementujaca interfejs Runnable.
     */
    private class WriteToServer implements Runnable{  //---------wątek zapisujący na serwer

        private DataOutputStream dataOut;

        /**
         * Koonstruktor klasy prywatnej
         * @param dataOut dane obiktu DataOutputStream do wyslania na serwer
         */
        public WriteToServer(DataOutputStream dataOut){
            this.dataOut = dataOut;
            System.out.println("WTS Runnable created");
        }

        /**
         * Metoda run() interfejsu Runnable, wysyla w petli dane na serwer.
         */
        @Override
        public void run() {
            try {    //-------- przesyłanie położenia tego klienta na serwer

                while (true){
                    if(thisClientPlayer != null) {
                        dataOut.writeDouble(thisClientPlayer.getX());
                        dataOut.writeDouble(thisClientPlayer.getY());
                        dataOut.writeDouble(thisClientPlayer.getX());
                        dataOut.writeDouble(thisClientPlayer.getY());
                        dataOut.writeInt(MX);
                        dataOut.writeInt(MY);
                        dataOut.writeInt(thisClientPlayer.getEnergy());
                        dataOut.writeBoolean(PlayerTank.GAMEOVER);
                        dataOut.writeBoolean(secondPlayerLeftGame);
                        dataOut.writeBoolean(mouseClicked);
                        mouseClicked = false;
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(35);
                    } catch (InterruptedException ex){
                        System.out.println("InterruptedException from WriteToServer run()");
                    }
                }
            }catch (IOException ex){
                secondPlayerLeftGame = true;
                System.out.println("IOException from WriteToServer run()");
            }
        }
    }

    /**
     * Metoda tworzaca okno gry
     */
    public void setUpGUI(){
        Point hotspot = new Point(0,0);
        JFrame frame = new JFrame();
        frame.setTitle("Player#" + playerID);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setCursor(toolkit.createCustomCursor(cursor,hotspot,"cursor"));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Metoda interfejsu KeyListener, nasluchujaca nacisniecie klawisza klawiatury.
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        thisClientPlayer.keyPressed(e);
    }

    /**
     * Metoda interfejsu KeyListener, nasluchujaca puszczenie klawisza klawiatury.
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        thisClientPlayer.keyReleased(e);
    }

    public static boolean mouseClicked = false;

    /**
     * Metoda interfejsu MouseListener, nasluchujaca nacisniecie przycisku myszy.
     *
     * @param e
     * @throws IOException
     */
    public void mouseClicked(MouseEvent e) throws IOException {
        thisClientPlayer.mouseClicked(e);

        int mx = e.getX();
        int my = e.getY();
        MX = mx;
        MY = my;
        mouseClicked = true;
    }
}

