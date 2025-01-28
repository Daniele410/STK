
# STK: Simple Toolkit

## Description

**STK** is an application designed to visualize music frequencies with corresponding visual effects. It analyzes audio
files and generates dynamic, frequency-based visualizations. The primary goal of the project is to provide a tool that
can visually represent audio frequency data through interactive and customizable effects.

## Main Features

- **Music Visualization**: Real-time display of audio frequencies through various visual effects.
- **Audio Analysis**: Analyzes the frequency spectrum of the input music.
- **Customizable Effects**: Extendable visual effects that react to different audio frequencies.
- **Interactive User Interface**: Simple and user-friendly interface for displaying the audio visualization.

## Technologies Used

- **Language**: Java
- **Framework**: JavaFX for the user interface
- **Audio Processing**: TarsosDSP for audio analysis
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
2. Upload an audio file (e.g., MP3) from the user interface.
3. Watch the visual effects change dynamically based on the frequency analysis of the music.

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
