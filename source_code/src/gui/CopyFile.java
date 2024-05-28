package gui;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;

import Main.AvviaAnalisi;

public class CopyFile {

	String sep= FileSystems.getDefault().getSeparator();
	String file="analizedFile.txt";

	public CopyFile(List<File> fileList, File indirizzo) throws IOException 
	{
		File file = new File(indirizzo.toString()+sep+this.file);

        try {
            // Crea il nuovo file
            boolean created = file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(file)) {
                for (File file1 : fileList) {
                    // Legggo da ogni file
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file1))) {
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            // Scrivo ogni line sul file Target.txt
                            fileWriter.write(line);
                            fileWriter.write(System.lineSeparator()); //\n
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            new AvviaAnalisi(indirizzo);
            if (created) {
                System.out.println("Il file è stato creato con successo.");
            } else {
                System.out.println("Il file esiste già.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Si è verificato un errore durante la creazione del file.");
        }
	}
              
}
