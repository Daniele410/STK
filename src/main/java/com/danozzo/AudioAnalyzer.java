package com.danozzo;

import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

public class AudioAnalyzer {
    private AudioContext ac;
    private SamplePlayer player;
    private Gain gain;
    private FFT fft;
    private PowerSpectrum powerSpectrum;
    private float[] frequencies;

    public AudioAnalyzer(String filePath) {
        ac = new AudioContext();

        try {
            SamplePlayer player = new SamplePlayer(ac, SampleManager.sample(filePath));
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
            ac.start();  // 🛠️ Assicura che il motore parta

            // Stampa di debug per controllare se FFT riceve dati
            System.out.println("FFT e PowerSpectrum inizializzati.");
        } catch (Exception e) {
            System.out.println("Errore durante il caricamento dell'audio: " + e.getMessage());
        }
    }

    public void startAnalysis() {
        if (player != null) {
            ac.start();
            player.start();
        }
    }

    public float getVolumeLevel() {
        return gain.getGain();
    }

    public float[] getFrequencies() {
        float[] features = powerSpectrum.getFeatures();
        if (features == null) {
            System.out.println("Errore: powerSpectrum.getFeatures() è null");
            return new float[0]; // Restituisce un array vuoto invece di null
        }
        System.arraycopy(features, 0, frequencies, 0, features.length);
        return frequencies;
    }
}
