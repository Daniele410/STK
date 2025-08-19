
# STK: Simple Toolkit

## Description

**STK** is an application designed to visualize music frequencies with corresponding visual effects. It analyzes audio
files and generates dynamic, frequency-based visualizations. The primary goal of the project is to provide a tool that
can visually represent audio frequency data through interactive and customizable effects.

## Main Features

- **Music Visualization**: Real-time display of audio frequencies through various visual effects.
- **Microphone Input**: Live audio analysis from microphone input for real-time visualization.
- **Audio Analysis**: Analyzes the frequency spectrum of input music or microphone audio.
- **Customizable Effects**: Extendable visual effects that react to different audio frequencies.
- **Interactive User Interface**: Simple and user-friendly interface for displaying the audio visualization.
- **Toggle Between Sources**: Switch between file playback and microphone input with a single button.

## Technologies Used

- **Language**: Java
- **Framework**: JavaFX for the user interface
- **Audio Processing**: Java Sound API for microphone input and frequency analysis
- **Build Tools**: Maven

## Project Structure

The project structure is organized as follows:

```
STK/
├── src/
│   ├── main/
│   │   ├── java/com/danozzo/
│   │   │   ├── Main.java
│   │   │   ├── AudioVisualizer.java
│   │   │   ├── AudioProcessor.java
│   │   │   └── FrequencyAnalyzer.java
│   │   ├── resources/
│   │       └── sample_audio.mp3
│   └── test/
├── pom.xml
└── README.md
```

## Prerequisites

To run the project, you need to have the following installed:

- Java 11 or higher
- Maven 3.6+

## Installation Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/Daniele410/STK.git
   ```
2. Navigate to the project directory:
   ```bash
   cd STK
   ```
3. Build and run the project:
   ```bash
   mvn clean install
   mvn javafx:run
   ```

## How to Use

1. Launch the application using Maven or an IDE such as IntelliJ IDEA.
2. **File Mode**: Upload an audio file (e.g., MP3) from the user interface and use the playback controls.
3. **Microphone Mode**: Click the "Toggle Microphone" button to switch to live microphone input.
4. Watch the visual effects change dynamically based on the frequency analysis of the music or microphone input.
5. Use the "Change Effect" button to cycle through different visualization modes.
6. Control volume and playback using the provided sliders and buttons.

## Contributions

Contributions, issues, and feature requests are welcome! To contribute:

1. Fork the repository.
2. Create a branch for your changes:
   ```bash
   git checkout -b branch-name
   ```
3. Make changes and commit:
   ```bash
   git commit -m "Description of changes"
   ```
4. Submit a pull request to the main branch.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Author

Danozzo

- [GitHub](https://github.com/Daniele410)
- [LinkedIn](https://www.linkedin.com/in/daniele-miraglia)

---
Thanks for choosing STK! If you have any questions or need support, feel free to contact me.
