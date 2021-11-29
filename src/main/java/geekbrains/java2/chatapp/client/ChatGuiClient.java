package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.client.adapter.ClientConnector;
import geekbrains.java2.chatapp.client.gui.AuthFrame;
import geekbrains.java2.chatapp.dto.*;
import geekbrains.java2.chatapp.client.gui.ChatFrame;

import javax.swing.*;
import java.util.*;

public class ChatGuiClient {
    private final AuthFrame authFrame;
    private ChatFrame chatFrame;
    private String username;
    private final ClientConnector connector;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6009;
    private HashMap<Integer, String> connectedUsers;
    private int myUserId;
    MessageLogger logger = new MessageLogger();
    public static void main(String[] args) {
        new ChatGuiClient();
    }
    public  ChatGuiClient() {
        connector = new ClientConnector(HOST,PORT);
        System.out.println("Connected");
        authFrame = new AuthFrame(this::performAuth);
    }
    private void performAuth(AuthCredentials authCredentials){
        logger.writeMessage(new Message("Hello logger",1));
        connector.sendObject(authCredentials);
        AuthenticationResult result = (AuthenticationResult) connector.readObject();
        if (result == AuthenticationResult.SUCCESSFULLY){
            myUserId = (Integer)connector.readObject();
            username = connector.readUTF();
            connectedUsers = (HashMap<Integer,String>)connector.readObject();
            List<Message> messageHistory = (List<Message>) connector.readObject();
//            messageHistory = logger.readMessages(100);
            initiateChat(connectedUsers,messageHistory);
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
        int targetId = 0;
        if(target != null)
            targetId = getUserIdByUsername(target);
        connector.sendObject(ClientCommand.message);
        connector.sendObject(new Message(text,myUserId,targetId));
    }
    private synchronized void initiateChat(HashMap<Integer, String> connectedUsers, List<Message> messageHistory){
        chatFrame = new ChatFrame(this,this::sendMessage,username);
        for (String username :
                connectedUsers.values()) {
            chatFrame.addUser(username);
        }
        DefaultListModel<Message> messageModel = chatFrame.getMessageModel();
        for (Message message :
                messageHistory) {
            messageModel.addElement(message);
        }
        new Thread(()->{
        while(true){
            ServerCommand command = (ServerCommand) connector.readObject();
            if(command == ServerCommand.MESSAGE)
                messageModel.addElement((Message) connector.readObject());
            else if (command == ServerCommand.NEW_USER)
                chatFrame.addUser(readUserInfo());
            else if (command == ServerCommand.REMOVE_USER)
                chatFrame.removeUser(readUserInfo());
            else if(command == ServerCommand.USER_ID_RESPONSE){
                synchronized (connectedUsers){
                    readUserInfo();
                    connectedUsers.notify();
                }
            }
        }}).start();
    }

    private String readUserInfo(){
        int userId = (Integer)connector.readObject();
        String username = connector.readUTF();

        connectedUsers.put(userId,username);
        return username;
    }

    private Integer getUserIdByUsername(String username)
    {
        for (Map.Entry<Integer, String> entry :
                connectedUsers.entrySet()) {
            if(entry.getValue().equals(username))
                return entry.getKey();
        }
        return null;
    }
    public synchronized String getUsernameById(int id){
        synchronized (connectedUsers) {
            while (true) {
                if (connectedUsers.containsKey(id))
                    return connectedUsers.get(id);
                requestUserById(id);
                try {
                    connectedUsers.wait();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    private void requestUserById(int id){
        connector.sendObject(ClientCommand.request_user);
        connector.sendObject(id);
    }
}
