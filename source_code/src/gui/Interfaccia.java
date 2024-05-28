package gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Interfaccia extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<File> fileList;
    private JTextArea outputTextArea;
    protected File percorsoCartellaOutput;
    protected File percorsoCartellaPredefCoeff;
    protected File percorsoCartellaExp;
    String sep= FileSystems.getDefault().getSeparator();
	String file="CoeffettiBase.txt";
	String indirizzoCompleto="src"+sep+"TestText"+sep+"Predefiniti"+sep+file;

    public Interfaccia() {
    	
    	this.percorsoCartellaOutput= this.createDir("Output");
    	this.percorsoCartellaPredefCoeff=this.createDir("PredeCoeff");
    	this.percorsoCartellaExp= this.createDir("examples");
    	
    	Interfaccia.copyFile(indirizzoCompleto,this.percorsoCartellaPredefCoeff.toString(),  file);
    	Interfaccia.copyFile("src"+sep+"TestText"+sep+"CoefInference1.txt", this.percorsoCartellaExp.toString(), "CoefInference1.txt");
    	Interfaccia.copyFile("src"+sep+"TestText"+sep+"CoefInference2.txt", this.percorsoCartellaExp.toString(), "CoefInference2.txt");
    	
        fileList = new ArrayList<>();

        // Impostazioni del frame
        setTitle("Analisi coeffetti");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Pannello principale
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Lista dei file
        DefaultListModel<File> listModel = new DefaultListModel<>();
        JList<File> fileListUI = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(fileListUI);
        
        // Pulsante per aggiungere file
        JButton addButton = new JButton("Aggiungi File");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);

                // Aggiungi un filtro per permettere solo file con estensione .txt o .java
                FileNameExtensionFilter filter = new FileNameExtensionFilter("File di testo e file Java", "txt", "java");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(Interfaccia.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    for (File file : selectedFiles) {
                        listModel.addElement(file);
                        fileList.add(file);
                    }
                   
                }
                fileList.add(new File(percorsoCartellaPredefCoeff.toString()+sep+file));
            }
            
        });
        
        // Pulsante per salvare la lista di file
        JButton saveButton = new JButton("Avvia Analisi");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
					new CopyFile(fileList, percorsoCartellaOutput);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });

        // Area di testo per visualizzare gli output
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        // Aggiungi componenti al pannello principale
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(addButton, BorderLayout.NORTH);
        mainPanel.add(saveButton, BorderLayout.SOUTH);

        // Aggiungi il pannello principale al frame
        add(mainPanel, BorderLayout.NORTH);
        add(outputScrollPane, BorderLayout.CENTER);

        // Rendi il frame visibile
        setVisible(true);

        // Reindirizza System.out.println all'area di testo
        PrintStream printStream = new PrintStream(new CustomOutputStream(outputTextArea));
        System.setOut(printStream);
    }
    
    private File createDir(String name) {
    	File outputDir=null;
       	//Creazione cartella di Output in cui sccriverò i file di output e cartella di Input in cui inserire il file
   	 try {
            // Ottieni il percorso dell'eseguibile (il file .jar)
            File jarFile = new File(CreateDirectory.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            
            // Ottieni la directory in cui si trova il file .jar
            File jarDir = jarFile.getParentFile();
            
            // Costruisci il percorso per la cartella Output
            outputDir = new File(jarDir, name);

            // Controlla se la cartella esiste, se no la crea
            if (!outputDir.exists()) {
                boolean created = outputDir.mkdirs();
                if (created) {
                    System.out.println("Cartella "+name+" creata con successo.");
                } else {
                    System.out.println("Errore nella creazione della cartella "+name+".");
                }
            } else {
                System.out.println("La cartella "+name+" esiste già.");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Errore nell'ottenere il percorso del file .jar.");
        }
     return outputDir;
    	
    }
    public static void copyFile(String sourceFilePath, String destinationDirPath, String fileName) {
        // Crea oggetti Path per il file di origine e la cartella di destinazione
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationDirPath, fileName);

        try {
            // Copia il file dalla sorgente alla destinazione
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copiato con successo.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nella copia del file.");
        }
    }

}

class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}



