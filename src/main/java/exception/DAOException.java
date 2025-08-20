package exception;

/**
 * Eccezione custom per errori DAO.
 */
public class DAOException extends Exception {

    /**
     * Costruttore base con solo messaggio.
     *
     * @param message descrizione dell'errore
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Costruttore esteso con messaggio e causa originaria.
     *
     * @param message descrizione dell'errore
     * @param cause   eccezione originale che ha causato l'errore
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}