package com.danozzo;

import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

import java.io.File;

public class AudioProcessor {
    private AudioContext ac;
    private SamplePlayer player;
    private Gain gain;
    private FFT fft;
    private PowerSpectrum powerSpectrum;

    public AudioProcessor(String filePath) {
        ac = new AudioContext();
        try {
            // Carica il file audio
            player = new SamplePlayer(ac, SampleManager.sample(new File(filePath).getAbsolutePath()));
            gain = new Gain(ac, 1, 0.5f);
            gain.addInput(player);
            ac.out.addInput(gain);

            ShortFrameSegmenter segmenter = new ShortFrameSegmenter(ac);
            segmenter.addInput(gain);
            ac.out.addDependent(segmenter);

            fft = new FFT();
            powerSpectrum = new PowerSpectrum();
            segmenter.addListener(fft);
            fft.addListener(powerSpectrum);

            player.start();
            ac.start();  // Assicura che il motore audio parta

            System.out.println("FFT e PowerSpectrum inizializzati.");
        } catch (Exception e) {
            System.out.println("Errore nel caricamento del file audio: " + e.getMessage());
        }
    }

    public void startAudio() {
        if (player != null) {
            ac.start();
            player.start();  // Inizia la riproduzione audio
        }
    }

    public void pauseAudio() {
        if (player != null) {
            player.pause(true);  // Metti in pausa la riproduzione
        }
    }

    public void resumeAudio() {
        if (player != null) {
            player.start();  // Continua la riproduzione
        }
    }

    public float getVolumeLevel() {
        return gain.getGain();
    }

    public float[] getFrequencies() {
        float[] features = powerSpectrum.getFeatures();
        if (features == null) {
            System.out.println("Errore: powerSpectrum.getFeatures() è null");
            return new float[0];  // Restituisce un array vuoto
        }
        return features;  // Restituisce i dati delle frequenze
    }
}
