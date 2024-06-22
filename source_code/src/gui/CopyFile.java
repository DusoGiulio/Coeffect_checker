package gui;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;
import Main.AvviaAnalisi;

/**
 * La classe CopyFile gestisce la copia del contenuto di una lista di file in un unico file di destinazione.
 * Dopo la copia, avvia un'analisi tramite la classe AvviaAnalisi.
 */
public class CopyFile {

    private String sep = FileSystems.getDefault().getSeparator();
    private String fileName = "analizedFile.java";

    /**
     * Costruttore della classe CopyFile.
     * Copia il contenuto di una lista di file in un unico file di destinazione e avvia l'analisi.
     *
     * @param fileList La lista dei file da copiare.
     * @param indirizzo La directory di destinazione dove creare il file di output.
     * @throws IOException Se si verifica un errore di I/O durante la creazione o scrittura del file.
     */
    public CopyFile(List<File> fileList, File indirizzo) throws IOException {
        // Crea il file di destinazione combinando l'indirizzo e il nome del file
        File outputFile = new File(indirizzo.toString() + sep + this.fileName);
        if (outputFile.exists()) {
            if (!outputFile.delete()) {
                throw new IOException("Unable to delete existing file: " + outputFile.getPath());
            }
        }

        try {
            // Crea il nuovo file
            boolean created = outputFile.createNewFile();
            try (FileWriter fileWriter = new FileWriter(outputFile)) {
                for (File inputFile : fileList) {
                    // Legge da ogni file della lista
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
                        bufferedReader.transferTo(fileWriter);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e; // Rilancia l'eccezione per gestirla a un livello superiore se necessario
            }

            // Avvia l'analisi sul file creato
            new AvviaAnalisi(indirizzo);
            if (created) {
               // System.out.println("Il file è stato creato con successo.");
            } else {
                //System.out.println("Il file esiste già.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Si è verificato un errore durante la creazione del file."+outputFile);
            throw e; // Rilancia l'eccezione per segnalare il fallimento
        }
    }
}
