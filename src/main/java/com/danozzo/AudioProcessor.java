package com.danozzo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;

public class AudioProcessor {
    private MediaPlayer mediaPlayer;
    private AudioAnalyzer analyzer;
    private boolean useMicrophone = false;
    private String filePath;

    public AudioProcessor(String filePath) {
        this.filePath = filePath;
        
        // Try to initialize MediaPlayer, but handle gracefully if JavaFX isn't available
        try {
            Media media = new Media(new File(filePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            System.out.println("MediaPlayer not available (likely headless environment): " + e.getMessage());
            mediaPlayer = null;
        }
        
        // Create analyzer for both file and microphone input
        analyzer = new AudioAnalyzer();
    }
    
    public void toggleMicrophone() {
        useMicrophone = !useMicrophone;
        
        if (useMicrophone) {
            // Stop file playback if running
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.pause();
                } catch (Exception e) {
                    // Ignore errors in headless environment
                }
            }
            // Start microphone analysis
            analyzer.startMicrophoneAnalysis();
            System.out.println("Switched to microphone input");
        } else {
            // Stop microphone analysis
            analyzer.stopMicrophoneAnalysis();
            System.out.println("Switched to file input");
        }
    }
    
    public boolean isMicrophoneMode() {
        return useMicrophone;
    }

    public float[] getFrequencies() {
        if (useMicrophone) {
            return analyzer.getFrequencies();
        } else {
            // For file playback, generate some demo frequencies
            // In a real implementation, you'd analyze the actual audio stream
            return generateDemoFrequencies();
        }
    }
    
    private float[] generateDemoFrequencies() {
        // Generate demo frequencies that change over time for file playback
        // This is a simplified approach - real implementation would analyze the MediaPlayer audio
        float[] frequencies = new float[64];
        long time = System.currentTimeMillis() / 100;
        
        for (int i = 0; i < frequencies.length; i++) {
            // Create some animated demo data
            frequencies[i] = (float) (Math.sin(time * 0.1 + i * 0.5) * 50 + 
                                    Math.sin(time * 0.05 + i * 0.2) * 30 + 
                                    Math.random() * 10);
            frequencies[i] = Math.max(0, frequencies[i]); // Ensure positive values
        }
        
        return frequencies;
    }

    public void startAudio() {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                mediaPlayer.play();
            } catch (Exception e) {
                System.out.println("Cannot start audio playback: " + e.getMessage());
            }
        }
    }

    public void pauseAudio() {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                mediaPlayer.pause();
            } catch (Exception e) {
                System.out.println("Cannot pause audio playback: " + e.getMessage());
            }
        }
    }

    public void resumeAudio() {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                mediaPlayer.play();
            } catch (Exception e) {
                System.out.println("Cannot resume audio playback: " + e.getMessage());
            }
        }
    }

    public void stopAudio() {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                System.out.println("Cannot stop audio playback: " + e.getMessage());
            }
        }
        // Also stop microphone if active
        if (useMicrophone) {
            analyzer.stopMicrophoneAnalysis();
        }
    }

    public void setVolume(double volume) {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                mediaPlayer.setVolume(volume);
            } catch (Exception e) {
                System.out.println("Cannot set volume: " + e.getMessage());
            }
        }
    }

    public AudioAnalyzer getAnalyzer() {
        return analyzer;
    }

    public Duration getCurrentTime() {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                return mediaPlayer.getCurrentTime();
            } catch (Exception e) {
                System.out.println("Cannot get current time: " + e.getMessage());
            }
        }
        return Duration.ZERO;
    }

    public Duration getTotalDuration() {
        if (!useMicrophone && mediaPlayer != null) {
            try {
                return mediaPlayer.getTotalDuration();
            } catch (Exception e) {
                System.out.println("Cannot get total duration: " + e.getMessage());
            }
        }
        return Duration.ZERO;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
