package geekbrains.java2.chatapp.client.adapter;
@FunctionalInterface
public interface SendPerformer {
    void send(String message, String target);
}
