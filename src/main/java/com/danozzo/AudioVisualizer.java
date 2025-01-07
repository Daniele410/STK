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
    private AudioAnalyzer audioAnalyzer;

    @Override
    public void start(Stage stage) {
        audioProcessor = new AudioProcessor("src/main/resources/audio.mp3");
        audioProcessor.startAudio();  // Avvia la riproduzione audio

        audioAnalyzer = new AudioAnalyzer("src/main/resources/audio.mp3");  // Avvia analisi audio

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

                double volume = audioProcessor.getVolumeLevel();
                gc.setFill(Color.hsb(volume * 360, 1, 1));
                gc.fillOval(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
