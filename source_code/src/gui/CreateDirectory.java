package gui;

import java.io.File;

/**
 * La classe CreateDirectory è responsabile della creazione di una directory con il nome specificato.
 * La directory viene creata al di fuori del working directory dell'eseguibile.
 */
public class CreateDirectory {

    /**
     * Costruttore che crea una directory con il nome specificato.
     *
     * @param name Il nome della directory da creare.
     */
    public CreateDirectory(String name) {
        // Ottieni il percorso del working directory dell'eseguibile
        String executablePath = System.getProperty("user.dir");

        // Costruisci il percorso per la cartella Output al di fuori dell'eseguibile
        // In questo esempio, si assume che la cartella "Output" sia allo stesso livello del working directory
        File outputDir = new File(executablePath + File.separator + ".." + File.separator + name);

        // Controlla se la cartella esiste, se no la crea
        if (!outputDir.exists()) {
            boolean created = outputDir.mkdirs();
            if (created) {
                System.out.println("Cartella creata con successo.");
            } else {
                System.out.println("Errore nella creazione della cartella.");
            }
        } else {
            System.out.println("La cartella esiste già.");
        }
    }
}
