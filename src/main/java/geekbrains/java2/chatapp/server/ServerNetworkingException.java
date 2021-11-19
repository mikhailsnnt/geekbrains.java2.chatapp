package geekbrains.java2.chatapp.server;

public class ServerNetworkingException extends RuntimeException{
    public ServerNetworkingException(String message) {
        super(message);
    }

    public ServerNetworkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
