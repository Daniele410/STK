package com.danozzo;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

public class AudioAnalyzer {
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 1024;
    
    private TargetDataLine microphone;
    private AudioFormat format;
    private boolean isRecording = false;
    private float[] currentFrequencies;
    private Thread recordingThread;
    
    public AudioAnalyzer() {
        format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        currentFrequencies = new float[BUFFER_SIZE / 2];
        setupMicrophone();
    }
    
    private void setupMicrophone() {
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Microphone not supported with this format");
                return;
            }
            microphone = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            System.err.println("Error setting up microphone: " + e.getMessage());
        }
    }
    
    public void startMicrophoneAnalysis() {
        if (microphone == null) return;
        
        try {
            microphone.open(format);
            microphone.start();
            isRecording = true;
            
            recordingThread = new Thread(() -> {
                byte[] buffer = new byte[BUFFER_SIZE * 2]; // 16-bit samples
                while (isRecording) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        float[] samples = convertBytesToFloats(buffer, bytesRead);
                        currentFrequencies = performFFT(samples);
                    }
                }
            });
            recordingThread.start();
            System.out.println("Microphone analysis started.");
        } catch (LineUnavailableException e) {
            System.err.println("Error starting microphone: " + e.getMessage());
        }
    }
    
    public void stopMicrophoneAnalysis() {
        isRecording = false;
        if (microphone != null && microphone.isOpen()) {
            microphone.stop();
            microphone.close();
        }
        if (recordingThread != null) {
            try {
                recordingThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Microphone analysis stopped.");
    }
    
    private float[] convertBytesToFloats(byte[] audioBytes, int bytesRead) {
        float[] samples = new float[bytesRead / 2]; // 16-bit = 2 bytes per sample
        for (int i = 0, j = 0; i < bytesRead - 1; i += 2, j++) {
            int sample = (audioBytes[i + 1] << 8) | (audioBytes[i] & 0xFF);
            samples[j] = sample / 32768.0f; // Normalize to -1.0 to 1.0
        }
        return samples;
    }
    
    // Simple FFT implementation for frequency analysis
    private float[] performFFT(float[] samples) {
        int n = samples.length;
        // Ensure power of 2 for simple FFT
        if ((n & (n - 1)) != 0) {
            n = Integer.highestOneBit(n);
        }
        
        // Simple magnitude calculation for demonstration
        // In a real implementation, you'd use a proper FFT algorithm
        float[] magnitudes = new float[Math.min(n / 2, currentFrequencies.length)];
        
        for (int i = 0; i < magnitudes.length; i++) {
            if (i < samples.length) {
                // Simple frequency band energy calculation
                float sum = 0;
                int bandStart = i * samples.length / magnitudes.length;
                int bandEnd = Math.min((i + 1) * samples.length / magnitudes.length, samples.length);
                
                for (int j = bandStart; j < bandEnd; j++) {
                    sum += Math.abs(samples[j]);
                }
                magnitudes[i] = sum / (bandEnd - bandStart) * 1000; // Scale for visualization
            }
        }
        
        return magnitudes;
    }
    
    public float[] getFrequencies() {
        return currentFrequencies != null ? currentFrequencies.clone() : new float[0];
    }
    
    public boolean isAnalyzing() {
        return isRecording;
    }
}
