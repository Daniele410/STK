package com.danozzo;

public class Main {
    public static void main(String[] args) {
        AudioAnalyzer audioAnalyzer = new AudioAnalyzer("src/main/resources/audio.mp3");
        audioAnalyzer.startAnalysis();

        AudioVisualizer.launch(AudioVisualizer.class, args);
    }
}
