package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.Message;

import java.util.Date;

public class ChatClient {
    private final ChatFrame chatFrame;
    private String username;
    public static void main(String[] args) {
          new ChatClient();
    }
    ChatClient(){
        username="My user";
        chatFrame = new ChatFrame(this);
    }

    public void sendMessage(String text) {
        // Sending message asd
        chatFrame.addOutComingMessage(new Message(text,username,new Date()));
    }
}
