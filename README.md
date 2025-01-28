# STK: Simple Toolkit

## Descrizione

**STK** è un'applicazione sviluppata per semplificare il lavoro quotidiano degli sviluppatori. Fornisce una serie di
strumenti utili per gestire file, analizzare dati e migliorare la produttività. L'obiettivo principale del progetto è
offrire un toolkit versatile, semplice da utilizzare e facilmente estendibile.

## Funzionalità principali

- **Gestione file**: Caricamento, modifica e salvataggio dei file in vari formati.
- **Analisi dati**: Strumenti per visualizzare e manipolare dataset.
- **Strumenti personalizzabili**: Estendibilità per integrare nuove funzionalità in base alle esigenze dell'utente.

## Tecnologie utilizzate

- **Linguaggio**: Java
- **Framework**: JavaFX per l'interfaccia utente
- **Strumenti di build**: Maven

## Struttura del progetto

La struttura del progetto è organizzata come segue:

```
STK/
├── src/
│   ├── main/
│   │   ├── java/com/danozzo/
│   │   │   ├── Main.java
│   │   │   ├── FileManager.java
│   │   │   ├── DataAnalyzer.java
│   │   │   └── ToolkitUtils.java
│   │   ├── resources/
│   │       └── audio.mp3
│   └── test/
├── pom.xml
└── README.md
```

## Prerequisiti

Per eseguire il progetto è necessario avere installato:

- Java 11 o superiore
- Maven 3.6+

## Istruzioni per l'installazione

1. Clona il repository:
   ```bash
   git clone https://github.com/Daniele410/STK.git
   ```
2. Accedi alla directory del progetto:
   ```bash
   cd STK
   ```
3. Compila ed esegui il progetto:
   ```bash
   mvn clean install
   mvn javafx:run
   ```

## Come utilizzare

1. Avvia l'applicazione utilizzando Maven o un IDE come IntelliJ IDEA.
2. Sfrutta le funzionalità disponibili dall'interfaccia utente per:
    - Caricare e modificare file.
    - Analizzare dataset.
    - Configurare nuovi strumenti.

## Contributi

Contributi, issue e richieste di nuove funzionalità sono benvenuti! Segui questi passaggi per contribuire:

1. Fai un fork del repository.
2. Crea un branch per le tue modifiche:
   ```bash
   git checkout -b nome-branch
   ```
3. Effettua le modifiche e fai un commit:
   ```bash
   git commit -m "Descrizione delle modifiche"
   ```
4. Manda una pull request sul branch principale.

## Licenza

Questo progetto è distribuito sotto licenza MIT. Vedi il file [LICENSE](LICENSE) per maggiori dettagli.

## Autore

Daniele Miraglia

- [GitHub](https://github.com/Daniele410)
- [LinkedIn](https://www.linkedin.com/in/daniele-miraglia)

---
Grazie per aver scelto STK! Per qualsiasi domanda o supporto, non esitare a contattarmi.
