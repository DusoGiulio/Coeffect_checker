package Exceptioin;

public class SintatticException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 public SintatticException(int line) {
	        super("Errore  alla riga ->" + line); // Imposta il messaggio di errore personalizzato
	    }

}
