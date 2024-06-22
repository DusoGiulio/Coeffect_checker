package coeffectChecking.ResultGenerator;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/**
 * Classe per la compilazione ed esecuzione del codice sorgente MiniJava risultante.
 */
public class Compile_Execute {

    /**
     * Costruttore della classe.
     *
     * @param codiceSorgente   Il codice sorgente MiniJava da scrivere nel file.
     * @param percorsoCompleto Il percorso completo del file in cui scrivere il codice sorgente.
     * @param sep              Il separatore di sistema.
     */
    public Compile_Execute(String codiceSorgente, String percorsoCompleto, String sep) {
        // Creazione del file e scrittura del codice
        try (PrintWriter writer = new PrintWriter(new FileWriter(percorsoCompleto))) {
            writer.println(codiceSorgente);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Compilazione del file creato
        try {
            Process processoCompilazione = Runtime.getRuntime().exec("javac " + percorsoCompleto);
            int exitCode = processoCompilazione.waitFor();
            if (exitCode != 0) {
                System.out.println("Compilazione fallita.");
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(processoCompilazione.getErrorStream()));
                String str;
                while ((str = errorReader.readLine()) != null) {
                    System.err.println(str);
                }
                return;
            }

            // Estrarre il nome della classe e il package dal percorso del file
            String classPath = percorsoCompleto.substring(0, percorsoCompleto.lastIndexOf(sep));
            String classNameWithPackage = percorsoCompleto.substring(percorsoCompleto.lastIndexOf(sep) + 1, percorsoCompleto.indexOf(".java"));

            // Esecuzione del file compilato
            ProcessBuilder processoEsecuzioneBuilder = new ProcessBuilder("java", "-cp", classPath, classNameWithPackage);
            processoEsecuzioneBuilder.redirectErrorStream(true);
            Process processoEsecuzione = processoEsecuzioneBuilder.start();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(processoEsecuzione.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            exitCode = processoEsecuzione.waitFor();
            if (exitCode != 0) {
                System.out.println("Esecuzione fallita.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
