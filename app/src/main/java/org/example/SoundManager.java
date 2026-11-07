package org.example;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {
    private Clip musicClip;
    private String currentMusicTrack = "";

    public void playMusic(String musicFileName) {
        // Se la traccia è già in riproduzione, non riavviarla
        if (currentMusicTrack.equals(musicFileName) && musicClip != null && musicClip.isRunning()) {
            return;
        }

        stopMusic(); // Ferma la musica precedente

        try {
            InputStream audioSrc = getClass().getResourceAsStream("/" + musicFileName);
            if (audioSrc == null) {
                System.out.println("File musicale non trovato: " + musicFileName);
                return;
            }

            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Riproduzione in loop continuo
            musicClip.start();

            currentMusicTrack = musicFileName;
        } catch (Exception e) {
            System.out.println("Errore caricamento musica (" + musicFileName + "): " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
            currentMusicTrack = "";
        }
    }

    public void playEffect(String soundFileName) {
        new Thread(() -> {
            try {
                InputStream audioSrc = getClass().getResourceAsStream("/" + soundFileName);
                if (audioSrc == null) return;

                InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                Clip sfxClip = AudioSystem.getClip();
                sfxClip.open(audioStream);
                sfxClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) sfxClip.close();
                });
                sfxClip.start();
            } catch (Exception e) {
                System.out.println("Errore effetto sonoro: " + e.getMessage());
            }
        }).start();
    }
}