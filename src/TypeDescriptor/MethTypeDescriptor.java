package TypeDescriptor;

/**
 * Questa classe rappresenta un descrittore di tipo per il tipo di un metodo, che pu� essere statico o non statico.
 */
public class MethTypeDescriptor extends TypeDescriptor {
    private boolean isStatic;

    /**
     * Costruisce un nuovo descrittore di tipo per il tipo di un metodo, specificando se il metodo � statico o non statico.
     *
     * @param isStatic True se il metodo � statico, false altrimenti.
     */
    public MethTypeDescriptor(boolean isStatic) {
        super();
        this.isStatic = isStatic;
    }

    /**
     * Verifica se il metodo � statico.
     *
     * @return True se il metodo � statico, false altrimenti.
     */
    public boolean isStatic() {
        return this.isStatic;
    }

    /**
     * Restituisce una rappresentazione testuale del descrittore di tipo del metodo.
     *
     * @return La stringa "static" se il metodo � statico, altrimenti una stringa vuota.
     */
    @Override
    public String toString() {
        if (this.isStatic) {
            return "static";
        } else {
            return "";
        }
    }
}
