package geekbrains.java2.chatapp.server;


import geekbrains.java2.chatapp.dto.*;
import geekbrains.java2.chatapp.server.dao.DAOException;
import geekbrains.java2.chatapp.server.dao.DatabaseService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatAppServer {
    private final DatabaseService authService;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private static final int PORT = 6009;
    private final HashMap<Integer,String> connectedUsers = new HashMap<>();
    public static void main(String[] args) {
        new ChatAppServer();
    }
    private static final String dbConnectionURL = "jdbc:mariadb://192.168.1.47:3306/ChatApp?user=sainnt&password=mask";
    public ChatAppServer (){
        try {
            authService = new DatabaseService(dbConnectionURL);
        }catch (DAOException initEx){
            throw new RuntimeException(initEx);
        }

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(()-> {
                    ClientHandler client = new ClientHandler(socket,this);
                    clientHandlers.add(client);
                    client.authenticate();
                }).start();
            }
        }catch (IOException ioException){
            throw new ServerNetworkingException("Server initializing error",ioException);
        }
    }

    public boolean userIsLoggedIn(User user){
        return clientHandlers.stream().anyMatch(
                clientHandler
                        -> clientHandler.getUsername().isPresent() &&
                        clientHandler.getUsername().get().equals(user.getUsername()));
    }

    public synchronized void notifyClientsAboutNewUser(Integer userId, String username){
        clientHandlers.stream()
                .filter(e->e.getUserId().isPresent() && !e.getUserId().get().equals(userId))
                .forEach(client->client.sendNewUserInfo(userId,username));
    }
    public synchronized void notifyClientsAboutDisconnectedUser(String username){
        clientHandlers.stream()
                .filter(e->e.getUsername().isPresent() && !e.getUsername().get().equals(username)).forEach(client->client.sendDisconnectedUserInfo(username));
    }

    public Optional<User> findUser(AuthCredentials credentials) {
        try {
            return authService.findUser(credentials);
        }
        catch (DAOException exception){
            throw new RuntimeException(exception);
        }
    }

    protected synchronized void broadcastMessage(Message message) {
        try{
        authService.writeMessage(message);}
        catch (DAOException exception)
        {
            throw new RuntimeException(exception);
        }
        clientHandlers.stream()
                .filter(ClientHandler::isAuthenticated)
                .forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    protected synchronized void  sendPrivateMessage(Message message) {
        try{
            authService.writeMessage(message);}
        catch (DAOException exception)
        {
            throw new RuntimeException(exception);
        }
        clientHandlers.stream()
                .filter(clientHandler ->
                        clientHandler.getUsername().isPresent() && (
                                clientHandler.getUsername().get().equals(message.getTarget()) ||
                                clientHandler.getUsername().get().equals(message.getFromUser())))
                .forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    protected synchronized void removeClientHandler(ClientHandler clientHandler){
        if(clientHandlers.remove(clientHandler))
            if(clientHandler.getUsername().isPresent())
                notifyClientsAboutDisconnectedUser(clientHandler.getUsername().get());

    }

    public HashMap<Integer, String> getUserListForNewUser() {
        return connectedUsers;
    }

    public List<Message> getMessageHistoryForNewUser(Integer userId){
        try{
            return authService.getMessagesWithUser(userId);
        }
        catch (DAOException exception){
            throw new RuntimeException(exception);
        }
    }

    public String getUsernameById(int userId) {
        try{
            return authService.getUsernameById(userId);
        }
        catch (DAOException exception){
            throw new RuntimeException(exception);
        }
    }
}
