package com.danozzo;

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
    private AudioAnalyzer analyzer;

    public AudioProcessor(String filePath) {
        ac = new AudioContext();
        try {
            player = new SamplePlayer(ac, SampleManager.sample(new File(filePath).getAbsolutePath()));
            gain = new Gain(ac, 1, 0.5f);
            gain.addInput(player);
            ac.out.addInput(gain);

            ShortFrameSegmenter segmenter = new ShortFrameSegmenter(ac);
            segmenter.addInput(gain);
            ac.out.addDependent(segmenter);

            analyzer = new AudioAnalyzer(ac);
            analyzer.connectAudioStream(gain); // Connect after adding gain to segmenter
            analyzer.startAnalysis(); // Start audio analysis

            System.out.println("Analizzatore audio inizializzato.");
        } catch (Exception e) {
            System.out.println("Errore durante il caricamento del file audio: " + e.getMessage());
        }
    }

    public void startAudio() {
        if (player != null) {
            ac.start();
            player.start();
        }
    }

    public void pauseAudio() {
        if (player != null) {
            player.pause(true);
        }
    }

    public void resumeAudio() {
        if (player != null) {
            player.start();
        }
    }

    public float getVolumeLevel() {
        return gain.getGain();
    }

    public AudioAnalyzer getAnalyzer() {
        return analyzer;
    }
}
