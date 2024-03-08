package gui;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;

import Main.AvviaAnalisi;

public class CopyFile {

	String sep= FileSystems.getDefault().getSeparator();
	String file="Target.txt";
	String indirizzoCompleto="src"+sep+"gui"+sep+file;

	public CopyFile(List<File> fileList) throws IOException 
	{
		try (FileWriter fileWriter = new FileWriter(indirizzoCompleto)) {
            for (File file : fileList) {
                // LEgo da ogni file
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
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

        new AvviaAnalisi();
	}
        
}
