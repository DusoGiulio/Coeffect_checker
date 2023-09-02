package Exceptioin;

/**
 * Eccezione personalizzata per gli errori sintattici.
 */
public class SintatticException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crea una nuova eccezione sintattica con un messaggio personalizzato che indica la riga dell'errore.
     * 
     * @param line Il numero di riga in cui si è verificato l'errore sintattico.
     */
    public SintatticException(int line) {
        super("Errore sintattico alla riga -> " + line); // Imposta il messaggio di errore personalizzato
    }
}