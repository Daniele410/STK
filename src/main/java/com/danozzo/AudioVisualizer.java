package com.danozzo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AudioVisualizer extends Application {
    private static final int WIDTH = 800;  // Larghezza della finestra
    private static final int HEIGHT = 600; // Altezza della finestra
    private AudioProcessor audioProcessor;
    private int effectMode = 0;  // Indica quale effetto visualizzare

    @Override
    public void start(Stage stage) {
        // Crea il processore audio
        audioProcessor = new AudioProcessor("src/main/resources/audio.mp3");
        audioProcessor.startAudio();

        // Ottieni l'analizzatore audio dal processore
        AudioAnalyzer analyzer = audioProcessor.getAnalyzer();

        // Canvas per disegnare l'effetto
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Bottone per cambiare l'effetto
        Button changeEffectButton = new Button("Change Effect");
        changeEffectButton.setOnAction(e -> {
            // Cambia l'effetto
            effectMode = (effectMode + 1) % 3;  // Passa tra 3 modalità di visualizzazione
        });

        // Layout principale
        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, changeEffectButton);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
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

                    // A seconda dell'effetto selezionato, visualizza le frequenze
                    switch (effectMode) {
                        case 0: // Effetto barre
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza (valore tra 0 e 1)
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double barHeight = normalizedFreq * HEIGHT;
                                gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));  // Colore progressivo
                                gc.fillRect(i * barWidth, HEIGHT - barHeight, barWidth, barHeight);
                            }
                            break;
                        case 1: // Effetto cerchi
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double radius = normalizedFreq * (WIDTH / 2); // Usa la larghezza per il raggio
                                double angle = i * (360.0 / frequencies.length);
                                double x = WIDTH / 2 + radius * Math.cos(Math.toRadians(angle));
                                double y = HEIGHT / 2 + radius * Math.sin(Math.toRadians(angle));
                                gc.setFill(Color.hsb(angle, 1, 1));
                                gc.fillOval(x - 5, y - 5, 10, 10); // Disegna piccoli cerchi
                            }
                            break;
                        case 2: // Effetto linee
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double lineLength = normalizedFreq * WIDTH;
                                gc.setStroke(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                                gc.setLineWidth(2);
                                gc.strokeLine(i * barWidth, HEIGHT / 2, i * barWidth + lineLength, HEIGHT / 2);
                            }
                            break;
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
