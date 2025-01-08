package com.danozzo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AudioVisualizer extends Application {
    private AudioProcessor audioProcessor;
    private volatile int effectMode = 0;  // Indica quale effetto visualizzare
    private boolean isAudioPaused = false;  // Variabile per controllare lo stato di pausa

    @Override
    public void start(Stage stage) {
        audioProcessor = new AudioProcessor("src/main/resources/audio.mp3");
        audioProcessor.startAudio();
        AudioAnalyzer analyzer = audioProcessor.getAnalyzer();

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Button changeEffectButton = new Button("Change Effect");
        changeEffectButton.setOnAction(e -> {
            effectMode = (effectMode + 1) % 4;
            System.out.println("Effect mode changed to: " + effectMode);
        });

        Button playButton = new Button("Play");
        playButton.setOnAction(e -> {
            if (isAudioPaused) {
                audioProcessor.resumeAudio();
                isAudioPaused = false;
            } else {
                audioProcessor.startAudio();
            }
        });

        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> {
            audioProcessor.pauseAudio();
            isAudioPaused = true;
        });

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> {
            audioProcessor.stopAudio();
            isAudioPaused = false;
        });

        Slider progressSlider = new Slider(0, 1, 0);
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> audioProcessor.setVolume(newVal.doubleValue()));

        audioProcessor.getMediaPlayer().currentTimeProperty().addListener((obs, oldVal, newVal) -> {
            progressSlider.setValue(newVal.toMillis() / audioProcessor.getTotalDuration().toMillis());
        });

        HBox controlsLayout = new HBox(10, playButton, pauseButton, stopButton, progressSlider);
        HBox effectLayout = new HBox(10, changeEffectButton);
        VBox volumeLayout = new VBox(10, controlsLayout, effectLayout, volumeSlider);
        StackPane root = new StackPane(canvas, volumeLayout);
        Scene scene = new Scene(root);

        stage.setTitle("Audio Visualizer");
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isAudioPaused) return;

                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                float[] frequencies = analyzer.getFrequencies();

                if (frequencies != null && frequencies.length > 0) {
                    float maxFrequency = 0;
                    for (float f : frequencies) maxFrequency = Math.max(maxFrequency, f);
                    double barWidth = canvas.getWidth() / frequencies.length;

                    switch (effectMode) {
                        case 0:
                            for (int i = 0; i < frequencies.length; i++) {
                                double barHeight = (frequencies[i] / maxFrequency) * canvas.getHeight();
                                gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                                gc.fillRect(i * barWidth, canvas.getHeight() - barHeight, barWidth, barHeight);
                            }
                            break;
                        case 1:
                            double centerX = canvas.getWidth() / 2;
                            double centerY = canvas.getHeight() / 2;
                            for (int i = 0; i < frequencies.length; i++) {
                                double angle = (i * 360.0 / frequencies.length) + (now / 1e8 * 360);
                                double radius = (frequencies[i] / maxFrequency) * (canvas.getWidth() / 4);
                                double x = centerX + radius * Math.cos(Math.toRadians(angle));
                                double y = centerY + radius * Math.sin(Math.toRadians(angle));
                                gc.setFill(Color.hsb(angle, 1, 1));
                                gc.fillRect(x - 2, y - 2, 4, 4);
                            }
                            break;
                        case 2:
                            for (int i = 0; i < frequencies.length; i++) {
                                double barHeight = (frequencies[i] / maxFrequency) * canvas.getHeight();
                                double distance = Math.cos(i * Math.PI / frequencies.length) * 200;
                                gc.setFill(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                                gc.fillRect(i * barWidth + distance, canvas.getHeight() - barHeight, barWidth, barHeight);
                            }
                            break;
                        case 3:
                            double rotationAngle = now / 1e9 * 360;
                            for (int i = 0; i < frequencies.length; i++) {
                                double angle = i * (360.0 / frequencies.length) + rotationAngle;
                                double radius = (frequencies[i] / maxFrequency) * (canvas.getWidth() / 3);
                                double x = canvas.getWidth() / 2 + radius * Math.cos(Math.toRadians(angle));
                                double y = canvas.getHeight() / 2 + radius * Math.sin(Math.toRadians(angle));
                                double lineLength = (frequencies[i] / maxFrequency) * (canvas.getWidth() / 2);
                                double endX = canvas.getWidth() / 2 + lineLength * Math.cos(Math.toRadians(angle));
                                double endY = canvas.getHeight() / 2 + lineLength * Math.sin(Math.toRadians(angle));
                                gc.setStroke(Color.hsb(i * 360.0 / frequencies.length, 1, 1));
                                gc.setLineWidth(2);
                                gc.strokeLine(x, y, endX, endY);
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
