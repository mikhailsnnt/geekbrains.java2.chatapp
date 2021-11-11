package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.client.adapter.ClientConnector;
import geekbrains.java2.chatapp.client.gui.AuthFrame;
import geekbrains.java2.chatapp.dto.*;
import geekbrains.java2.chatapp.client.gui.ChatFrame;

import javax.swing.*;

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
            String[] connectedUsers = (String[])connector.readObject();
            initiateChat(connectedUsers);
            authFrame.closeView();
        }
        else if (result == AuthenticationResult.USER_IS_LOGGED) {
            authFrame.showAlert("User is already logged in");
        }
        else if(result == AuthenticationResult.BAD_CREDENTIALS){
            authFrame.showAlert("Bad credentials");
        }
        else if(result == AuthenticationResult.TIMEOUT) {
            authFrame.showAlert("Authentication timeout");

        }
    }
    public synchronized void sendMessage(String text, String target) {
        connector.sendObject(ClientCommand.message);
        connector.sendObject(new Message(text,username,target));
    }
    private void initiateChat(String[] connectedUsers){
        chatFrame = new ChatFrame(this::sendMessage,username);
        for (String username :
                connectedUsers) {
            chatFrame.addUser(username);
        }

        DefaultListModel<Message> messageModel = chatFrame.getMessageModel();
        new Thread(()->{
        while(true){
            ServerCommand command = (ServerCommand) connector.readObject();
            if(command == ServerCommand.MESSAGE)
                messageModel.addElement((Message) connector.readObject());
            else if (command == ServerCommand.NEW_USER)
                chatFrame.addUser(connector.readUTF());
            else if (command == ServerCommand.REMOVE_USER)
                chatFrame.removeUser(connector.readUTF());
        }}).start();
    }


}
