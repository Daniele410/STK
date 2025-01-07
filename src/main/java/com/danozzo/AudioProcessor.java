package com.danozzo;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

import java.io.File;

public class AudioProcessor {
    private AudioContext ac;
    private SamplePlayer player;
    private Gain gain;

    public AudioProcessor(String filePath) {
        ac = new AudioContext();
        try {
            player = new SamplePlayer(ac, SampleManager.sample(new File(filePath).getAbsolutePath()));
            gain = new Gain(ac, 1, 0.5f);
            gain.addInput(player);
            ac.out.addInput(gain);
        } catch (Exception e) {
            System.out.println("Errore nel caricamento del file audio: " + e.getMessage());
        }
    }

    public void startAudio() {
        if (player != null) {
            ac.start();
            player.start();  // Inizia la riproduzione dell'audio
        }
    }

    public void pauseAudio() {
        if (player != null) {
            player.pause(true);  // Metti in pausa la riproduzione
        }
    }

    public void resumeAudio() {
        if (player != null) {
            player.start();  // Continua la riproduzione dall'ultimo punto
        }
    }

    public float getVolumeLevel() {
        return gain.getGain();
    }

}
