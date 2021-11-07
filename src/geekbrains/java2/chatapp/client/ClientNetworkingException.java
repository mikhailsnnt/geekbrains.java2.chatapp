package geekbrains.java2.chatapp.client;

public class ClientNetworkingException extends RuntimeException{
    public ClientNetworkingException(String message) {
        super(message);
    }

    public ClientNetworkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
