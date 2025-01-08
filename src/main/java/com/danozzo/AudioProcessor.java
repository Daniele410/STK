package com.danozzo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

import java.io.File;
import java.util.Arrays;

public class AudioProcessor {
    private AudioContext ac;
    private MediaPlayer mediaPlayer;
    private Gain gain;
    private AudioAnalyzer analyzer;

    public AudioProcessor(String filePath) {
        Media media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // Creiamo un AudioContext per l'analisi audio
        ac = new AudioContext();
        this.analyzer = new AudioAnalyzer(ac);
        analyzer.startAnalysis();

        // Colleghiamo l'audio stream all'analizzatore
        gain = new Gain(ac, 1, 0.5f);
        gain.addInput(ac.getAudioInput());
        analyzer.connectAudioStream(gain);
    }

    public float[] getFrequencies() {
        float[] frequencies = analyzer.getFrequencies();
        // Aggiungi un log per verificare che i dati cambino durante la riproduzione
        System.out.println("Frequenze: " + Arrays.toString(frequencies));
        return frequencies;
    }

    public void startAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pauseAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resumeAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public AudioAnalyzer getAnalyzer() {
        return analyzer;
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public Duration getTotalDuration() {
        return mediaPlayer.getTotalDuration();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
