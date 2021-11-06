package geekbrains.java2.chatapp.client;

public class ResourceLoadingException extends RuntimeException{
    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceLoadingException(String message) {
        super(message);
    }
}
