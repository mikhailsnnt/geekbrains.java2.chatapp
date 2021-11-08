package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.client.gui.AuthFrame;
import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.dto.AuthenticationResult;
import geekbrains.java2.chatapp.dto.Message;
import geekbrains.java2.chatapp.client.gui.ChatFrame;

import javax.swing.*;
import java.util.Date;

public class ChatGuiClient {
    private final AuthFrame authFrame;
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
        authFrame = new AuthFrame(this::performAuth);
    }
    private void performAuth(AuthCredentials authCredentials){
        connector.sendObject(authCredentials);
        AuthenticationResult result = (AuthenticationResult) connector.readObject();
        if (result == AuthenticationResult.SUCCESSFULLY){
            username = connector.readUTF();
            initiateChat();
            authFrame.closeView();
        }
        else if (result == AuthenticationResult.USER_IS_LOGGED) {
            authFrame.showAlert("User is already logged in");
        }
        else if(result == AuthenticationResult.BAD_CREDENTIALS){
            authFrame.showAlert("Bad credentials");
        }
        else if(result == AuthenticationResult.TIMEOUT) {

        }
    }
    public void sendMessage(String text) {
        // Sending message asd
    }
    private void initiateChat(){
        chatFrame = new ChatFrame(this::sendMessage);
        DefaultListModel<Message> messageModel = chatFrame.getMessageModel();
        new Thread(()->{
        while(true){
            messageModel.addElement((Message) connector.readObject());
        }}).start();
    }


}
