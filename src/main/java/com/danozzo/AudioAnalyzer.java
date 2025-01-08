package com.danozzo;

import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.ugens.Gain;

public class AudioAnalyzer {
    private AudioContext ac;
    private ShortFrameSegmenter segmenter;
    private FFT fft;
    private PowerSpectrum powerSpectrum;

    public AudioAnalyzer(AudioContext audioContext) {
        this.ac = audioContext;
        segmenter = new ShortFrameSegmenter(ac);
        fft = new FFT();
        powerSpectrum = new PowerSpectrum();

        segmenter.addListener(fft);
        fft.addListener(powerSpectrum);
    }

    public void connectAudioStream(Gain gain) {
        segmenter.addInput(gain);
        ac.out.addDependent(segmenter);
    }

    public void startAnalysis() {
        ac.start(); // Start audio processing
        System.out.println("AudioContext started.");
    }

    public float[] getFrequencies() {
        float[] features = powerSpectrum.getFeatures();
        if (features == null || features.length == 0) {
            System.out.println("Errore: powerSpectrum.getFeatures() è null");
            return new float[0];
        }
        return features;
    }
}
