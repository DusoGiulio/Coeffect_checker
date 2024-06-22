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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe che rappresenta l'interfaccia grafica dell'applicazione per l'analisi dei coeffetti.
 * Estende JFrame e permette all'utente di aggiungere file, avviare l'analisi e visualizzare i risultati.
 */
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
    private String sep= FileSystems.getDefault().getSeparator();
	private String file="CoeffettiBase.java";
	//private String indirizzoCompleto="src"+sep+"TestText"+sep+"Predefiniti"+sep+file;
	  /**
     * Costruttore della classe Interfaccia. Inizializza le cartelle necessarie,
     * copia i file predefiniti nelle cartelle appropriate e imposta l'interfaccia grafica.
     */
    public Interfaccia() {
    	
    	
    	this.percorsoCartellaPredefCoeff=this.createDir("PredeCoeff");
    	this.percorsoCartellaExp= this.createDir("examples");
    	this.percorsoCartellaOutput= this.createDir("Output");
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
                fileChooser.setCurrentDirectory(percorsoCartellaExp);
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
                
            }
            
        });
        
        // Pulsante per salvare la lista di file
        JButton saveButton = new JButton("Avvia Analisi");
        saveButton.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					deleteDirectory(percorsoCartellaOutput);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            	percorsoCartellaOutput= createDir("Output");
            	fileList.add(new File(percorsoCartellaPredefCoeff.toString()+sep+file));
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
    /**
     * Crea una directory con il nome specificato nella stessa directory del file JAR eseguibile.
     *
     * @param name Il nome della directory da creare.
     * @return Il file rappresentante la directory creata.
     */
    public File createDir(String name) {
    	File outputDir=null;
       	//Creazione cartella di Output in cui sccriver� i file di output e cartella di Input in cui inserire il file
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
                } else {
                    System.out.println("Errore nella creazione della cartella "+name+".");
                }
            } else {
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Errore nell'ottenere il percorso del file .jar.");
        }
     return outputDir;
    	
    }
    public void deleteDirectory(File percorsoCartellaOutput) throws IOException {
    	Path directory = percorsoCartellaOutput.toPath();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    /**
     * Copia un file dalla sorgente alla destinazione specificata.
     *
     * @param sourceFilePath      Il percorso del file sorgente.
     * @param destinationDirPath  Il percorso della directory di destinazione.
     * @param fileName            Il nome del file da copiare.
     */
    public static void copyFile(String sourceFilePath, String destinationDirPath, String fileName) {
        // Crea oggetti Path per il file di origine e la cartella di destinazione
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationDirPath, fileName);

        try {
            // Copia il file dalla sorgente alla destinazione
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nella copia del file.");
        }
    }

}

/**
 * Classe CustomOutputStream per reindirizzare l'output della console a un JTextArea.
 */
class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    /**
     * Costruttore che accetta un JTextArea dove scrivere l'output.
     *
     * @param textArea Il JTextArea dove scrivere l'output.
     */
    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}




