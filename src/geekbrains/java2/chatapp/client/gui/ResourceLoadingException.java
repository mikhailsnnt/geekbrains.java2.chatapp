package geekbrains.java2.chatapp.client.gui;

public class ResourceLoadingException extends RuntimeException{
    public ResourceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceLoadingException(String message) {
        super(message);
    }
}
