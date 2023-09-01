package Exceptioin;

public class TypeCheckingException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 public TypeCheckingException(int line) {
	        super("Errore di tipo alla riga ->" + line); // Imposta il messaggio di errore personalizzato
	    }
	 public TypeCheckingException(int line, int type) {
	        super("Errore dichiarazione coeffetti alla riga ->" + line); // Imposta il messaggio di errore personalizzato
	    }

}
