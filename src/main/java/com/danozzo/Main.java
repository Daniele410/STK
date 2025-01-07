package com.danozzo;

public class Main {
    public static void main(String[] args) {
        AudioProcessor audioProcessor = new AudioProcessor("src/main/resources/audio.mp3");
        audioProcessor.startAudio();  // Avvia la riproduzione audio

        AudioVisualizer.launch(AudioVisualizer.class, args);  // Avvia la visualizzazione audio
    }
}
