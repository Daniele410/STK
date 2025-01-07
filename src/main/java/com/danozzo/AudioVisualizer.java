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
            effectMode = (effectMode + 1) % 4;  // Passa tra 4 modalità di visualizzazione
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
                        case 0: // Effetto barre con profondità Z
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza (valore tra 0 e 1)
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double barHeight = normalizedFreq * canvas.getHeight();
                                double depth = Math.sin(i * Math.PI / frequencies.length) * 100;  // Aggiungi profondità
                                gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));  // Colore progressivo
                                gc.fillRect(i * barWidth + depth, canvas.getHeight() - barHeight, barWidth, barHeight);
                            }
                            break;

                        case 1: // Effetto rotazione 3D
                            double centerX = canvas.getWidth() / 2;
                            double centerY = canvas.getHeight() / 2;
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza (valore tra 0 e 1)
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double angle = (i * 360.0 / frequencies.length) + (now / 100000000.0 * 360); // Ruotare lentamente
                                double radius = normalizedFreq * (canvas.getWidth() / 4);
                                double x = centerX + radius * Math.cos(Math.toRadians(angle));
                                double y = centerY + radius * Math.sin(Math.toRadians(angle));
                                gc.setFill(Color.hsb(angle, 1, 1));
                                gc.fillRect(x - 2, y - 2, 4, 4); // Disegna piccoli quadrati ruotanti
                            }
                            break;

                        case 2: // Effetto barre con prospettiva 3D
                            for (int i = 0; i < frequencies.length; i++) {
                                // Normalizza la frequenza
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double barHeight = normalizedFreq * canvas.getHeight();
                                double distance = Math.cos(i * Math.PI / frequencies.length) * 200;  // Aggiungi distorsione prospettica
                                gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));  // Colore progressivo
                                gc.fillRect(i * barWidth + distance, canvas.getHeight() - barHeight, barWidth, barHeight);
                            }
                            break;

                        case 3: // Effetto linee 3D rotanti
                            double rotationAngle = now / 1000000000.0 * 360;  // Angolo di rotazione continuo
                            for (int i = 0; i < frequencies.length; i++) {
                                float normalizedFreq = frequencies[i] / maxFrequency;
                                double angle = i * (360.0 / frequencies.length) + rotationAngle;
                                double radius = normalizedFreq * (canvas.getWidth() / 3);
                                double x = canvas.getWidth() / 2 + radius * Math.cos(Math.toRadians(angle));
                                double y = canvas.getHeight() / 2 + radius * Math.sin(Math.toRadians(angle));
                                double lineLength = normalizedFreq * (canvas.getWidth() / 2);
                                double endX = canvas.getWidth() / 2 + lineLength * Math.cos(Math.toRadians(angle));
                                double endY = canvas.getHeight() / 2 + lineLength * Math.sin(Math.toRadians(angle));
                                gc.setStroke(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                                gc.setLineWidth(2);
                                gc.strokeLine(x, y, endX, endY); // Linea rotante 3D
                            }
                            break;
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
