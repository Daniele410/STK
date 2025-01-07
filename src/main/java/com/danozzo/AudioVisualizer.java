package com.danozzo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AudioVisualizer extends Application {
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
        Canvas canvas = new Canvas(800, 600);  // Usa dimensioni iniziali
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Bottone per cambiare l'effetto
        Button changeEffectButton = new Button("Change Effect");
        changeEffectButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        changeEffectButton.setOnAction(e -> {
            // Cambia l'effetto
            effectMode = (effectMode + 1) % 3;  // Passa tra 3 modalità di visualizzazione
        });

        // Layout per posizionare il bottone in alto a destra
        HBox topLayout = new HBox();
        topLayout.getChildren().add(changeEffectButton);
        topLayout.setStyle("-fx-alignment: top-right; -fx-padding: 10px;");

        // Layout principale con il canvas
        StackPane root = new StackPane();
        root.getChildren().addAll(canvas, topLayout);

        // Scene responsive
        Scene scene = new Scene(root);
        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            canvas.setWidth(newWidth.doubleValue());
        });
        scene.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            canvas.setHeight(newHeight.doubleValue());
        });

        stage.setTitle("Audio Visualizer");
        stage.setScene(scene);
        stage.show();

        // Timer di animazione per aggiornare la visualizzazione
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.BLACK);  // Imposta il colore di sfondo
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());  // Pulisce lo schermo

                // Ottieni le frequenze dall'analizzatore
                float[] frequencies = analyzer.getFrequencies();

                if (frequencies != null && frequencies.length > 0) {
                    // Troviamo il valore massimo tra le frequenze per normalizzare
                    float maxFrequency = 0;
                    for (int i = 0; i < frequencies.length; i++) {
                        maxFrequency = Math.max(maxFrequency, frequencies[i]);
                    }

                    // Calcoliamo la larghezza di ciascuna barra in base alla larghezza del canvas
                    double barWidth = canvas.getWidth() / frequencies.length;

                    // A seconda dell'effetto selezionato, visualizza le frequenze
                    switch (effectMode) {
                        case 0: // Effetto barre
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza (valore tra 0 e 1)
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double barHeight = normalizedFreq * canvas.getHeight();
                                gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));  // Colore progressivo
                                gc.fillRect(i * barWidth, canvas.getHeight() - barHeight, barWidth, barHeight);
                            }
                            break;
                        case 1: // Effetto spirale
                            double angleIncrement = 5; // Incremento dell'angolo per la spirale
                            for (int i = 0; i < frequencies.length; i++) {
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double radius = normalizedFreq * (canvas.getWidth() / 2);
                                double angle = i * angleIncrement;
                                double x = canvas.getWidth() / 2 + radius * Math.cos(Math.toRadians(angle));
                                double y = canvas.getHeight() / 2 + radius * Math.sin(Math.toRadians(angle));
                                gc.setFill(Color.hsb(angle, 1, 1));
                                gc.fillOval(x - 5, y - 5, 10, 10); // Disegna piccoli punti lungo la spirale
                            }
                            break;
                        case 2: // Effetto linee migliorato
                            double centerX = canvas.getWidth() / 2;
                            double centerY = canvas.getHeight() / 2;
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double angle = i * (360.0 / frequencies.length);
                                double lineLength = normalizedFreq * (canvas.getWidth() / 2);
                                double endX = centerX + lineLength * Math.cos(Math.toRadians(angle));
                                double endY = centerY + lineLength * Math.sin(Math.toRadians(angle));
                                gc.setStroke(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                                gc.setLineWidth(2);
                                gc.strokeLine(centerX, centerY, endX, endY); // Disegna linee dinamiche
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
