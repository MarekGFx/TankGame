package gameHelper;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Klasa pomocnicza przechowujaca efekty dzwiekowe, z metodami do odtwarzania dzwiekow.
 * @author Marek Gajdamowicz
 */
public class Sound {

    Clip clip;
    URL[] soundURL = new URL[10];

    public Sound() {

        soundURL[0] = getClass().getResource("/sound/Gameplay.wav");
        soundURL[1] = getClass().getResource("/sound/shoot.wav");
        soundURL[2] = getClass().getResource("/sound/explosion.wav");
    }

    public void setFile(int i) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }


    public void playMusic(int i) {
        setFile(i);
        play();
        loop();
    }

    public void stopMusic(){
        stop();
    }

    public void playSE(int i){
        setFile(i);
        play();
    }
}
