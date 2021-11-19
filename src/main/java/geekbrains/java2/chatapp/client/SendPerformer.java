package geekbrains.java2.chatapp.client;
@FunctionalInterface
public interface SendPerformer {
    void send(String message, String target);
}
