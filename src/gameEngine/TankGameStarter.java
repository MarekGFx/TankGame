package gameEngine;

import gameHelper.BufferedImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Klasa Tytulowego ekranu gry
 *
 * @author Marek Gajdamowicz
 */
public class TankGameStarter implements ActionListener{

    JFrame frame = new JFrame();
    JLabel label = new JLabel();
    JButton button = new JButton();
    TankGameEngine tankGame;
    BufferedImageLoader loader = new BufferedImageLoader();
    private BufferedImage title;

    {
        try {
            title = loader.loadImage("/titleimage/titleImage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Kunstruktor okna tytulowego gry zawierajacu etykiete i przycisk startu gry
     */
    public TankGameStarter(){

        button.setBounds(350,320,150,50);
        button.setText("Rozpocznij Bitwę");
        button.setFocusable(false);
        button.addActionListener(this);
        button.setFont(new Font("MV",Font.CENTER_BASELINE,13));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createEtchedBorder());

        ImageIcon titleImage = new ImageIcon(title);
        label.setText("Bitwa Czołgów");
        label.setIcon(titleImage);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setFont(new Font("MV",Font.CENTER_BASELINE,60));
        label.setForeground(Color.green);
        label.setIconTextGap(-100);
        label.setOpaque(true);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.add(button);

        frame.setTitle("Czołgi");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setResizable(false);
        frame.add(label);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Metoda implementujaca interfejs ActionListener nasluchujaca nacisniecie przycisku.
     * Akcja wywoluje obiekt TankGameEngine, laczy z serwerem, tworzy nowe okno gry i startuje glowny watek gry.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==button){
            label.setText("oczekiwanie na drugiego gracza");
            label.setFont(new Font("MV",Font.CENTER_BASELINE,40));
            JOptionPane.showMessageDialog(null,
                    "Oczekiwanie na drugiego gracza,\n"+
                    "sterujesz czołgiem koloru zielonego,\n"+
                    "pojazd przeciwnika jest koloru czerwonego",
                    "oczekiwanie na połączenie z przeciwnikiem",
                    JOptionPane.INFORMATION_MESSAGE);
            tankGame = new TankGameEngine();
            tankGame.setPreferredSize(new Dimension(900,600));
            tankGame.connectToServer();
            frame.dispose();
            tankGame.setUpGUI();
            tankGame.start();
        }
    }
}
