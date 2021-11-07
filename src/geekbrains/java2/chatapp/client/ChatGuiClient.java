package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.client.gui.AuthFrame;
import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.dto.AuthenticationResult;
import geekbrains.java2.chatapp.dto.Message;
import geekbrains.java2.chatapp.client.gui.ChatFrame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class ChatGuiClient {
    private ChatFrame chatFrame;
    private String username;
    private final ClientConnector connector;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6009;
    public static void main(String[] args) {
        new ChatGuiClient();
    }
    public  ChatGuiClient() {
        connector = new ClientConnector(HOST,PORT);
        System.out.println("Connected");
        new AuthFrame(this::performAuth);
    }
    private boolean performAuth(AuthCredentials authCredentials){
        connector.sendObject(authCredentials);
        AuthenticationResult result = (AuthenticationResult) connector.readObject();
        if (result == AuthenticationResult.SUCCESSFULLY){
            username = connector.readUTF();
            initiateChatFrame();
            return true;
        }
        else if (result == AuthenticationResult.USER_IS_LOGGED) {
            System.out.println(result);
        }
        else if(result == AuthenticationResult.BAD_CREDENTIALS){
            System.out.println(result);
        }
        return false;
    }
    public void sendMessage(String text) {
        // Sending message asd
        chatFrame.addOutComingMessage(new Message(text,username,new Date()));
    }
    private void  initiateChatFrame(){
        chatFrame = new ChatFrame(this);

    }

}
