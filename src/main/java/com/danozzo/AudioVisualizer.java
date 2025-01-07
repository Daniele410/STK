package com.danozzo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AudioVisualizer extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private AudioProcessor audioProcessor;

    @Override
    public void start(Stage stage) {
        audioProcessor = new AudioProcessor("src/main/resources/audio.mp3");

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas), WIDTH, HEIGHT);

        stage.setTitle("Audio Visualizer");
        stage.setScene(scene);
        stage.show();

        // Animazione per aggiornare la visualizzazione
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                // Ottieni le frequenze per la visualizzazione
                float[] frequencies = audioProcessor.getFrequencies();

                if (frequencies != null && frequencies.length > 0) {
                    // Visualizza le frequenze come barre
                    for (int i = 0; i < frequencies.length; i++) {
                        double barHeight = frequencies[i] * HEIGHT;
                        gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                        gc.fillRect(i * (WIDTH / frequencies.length), HEIGHT - barHeight, WIDTH / frequencies.length, barHeight);
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
