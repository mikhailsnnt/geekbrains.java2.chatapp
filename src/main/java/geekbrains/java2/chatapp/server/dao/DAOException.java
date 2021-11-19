package geekbrains.java2.chatapp.server.dao;

public class DAOException extends Exception{
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
