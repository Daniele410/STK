package com.danozzo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AudioVisualizer extends Application {
    private static final int WIDTH = 800;  // Larghezza della finestra
    private static final int HEIGHT = 600; // Altezza della finestra
    private AudioProcessor audioProcessor;

    @Override
    public void start(Stage stage) {
        // Crea il processore audio
        audioProcessor = new AudioProcessor("src/main/resources/audio.mp3");
        audioProcessor.startAudio();

        // Ottieni l'analizzatore audio dal processore
        AudioAnalyzer analyzer = audioProcessor.getAnalyzer();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas), WIDTH, HEIGHT);

        stage.setTitle("Audio Visualizer");
        stage.setScene(scene);
        stage.show();

        // Timer di animazione per aggiornare la visualizzazione
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.BLACK);  // Imposta il colore di sfondo
                gc.fillRect(0, 0, WIDTH, HEIGHT);  // Pulisce lo schermo

                // Ottieni le frequenze dall'analizzatore
                float[] frequencies = analyzer.getFrequencies();

                if (frequencies != null && frequencies.length > 0) {
                    // Troviamo il valore massimo tra le frequenze per normalizzare
                    float maxFrequency = 0;
                    for (int i = 0; i < frequencies.length; i++) {
                        maxFrequency = Math.max(maxFrequency, frequencies[i]);
                    }

                    // Calcoliamo la larghezza di ciascuna barra in base alla larghezza del canvas
                    double barWidth = (double) WIDTH / frequencies.length;

                    // Visualizza le frequenze come barre
                    for (int i = 0; i < frequencies.length; i++) {
                        // Normalizza la frequenza (valore tra 0 e 1)
                        float normalizedFreq = frequencies[i] / maxFrequency;

                        // Imposta la dimensione della barra in base alla frequenza normalizzata
                        double barHeight = normalizedFreq * HEIGHT;

                        // Imposta il colore in base alla frequenza
                        gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));  // Colore progressivo

                        // Visualizza la barra in modo che si estenda su tutta la finestra
                        gc.fillRect(i * barWidth, HEIGHT - barHeight, barWidth, barHeight);
                    }
                }
            }
        }.start();

        // Avvia la riproduzione audio (opzionale)
        audioProcessor.startAudio();
    }

    public static void main(String[] args) {
        launch();
    }
}