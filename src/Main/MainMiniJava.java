package Main;

import java.io.IOException;
import javax.swing.SwingUtilities;
import gui.Interfaccia;
/**
 * Classe principale per l'esecuzione del compilatore MiniJava.
 */
public class MainMiniJava {
    
    /**
     * Metodo principale per l'esecuzione del compilatore MiniJava.
     * 
     * @param args Gli argomenti della riga di comando (non utilizzati).
     * @throws IOException In caso di errori di input/output.
     */
	public static void main(String[] args) throws IOException{
        
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interfaccia();
            }
        });
		
	}		
}

