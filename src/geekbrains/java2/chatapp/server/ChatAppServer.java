package geekbrains.java2.chatapp.server;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.dto.Message;
import geekbrains.java2.chatapp.server.dao.DatabaseService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChatAppServer {
    private final DatabaseService authService;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private static final int PORT = 6009;
    public static void main(String[] args) {
        new ChatAppServer();
    }
    public ChatAppServer (){
        authService = new DatabaseService();
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

    public synchronized void notifyClientsAboutNewUser(String username){
        clientHandlers.stream()
                .filter(e->e.getUsername().isPresent() && !e.getUsername().get().equals(username)).forEach(client->client.sendNewUserInfo(username));
    }
    public synchronized void notifyClientsAboutDisconnectedUser(String username){
        clientHandlers.stream()
                .filter(e->e.getUsername().isPresent() && !e.getUsername().get().equals(username)).forEach(client->client.sendDisconnectedUserInfo(username));
    }

    public Optional<User> findUser(AuthCredentials credentials) {
        return authService.findUser(credentials);
    }

    protected synchronized void broadcastMessage(Message message) {
        clientHandlers.stream()
                .filter(ClientHandler::isAuthenticated)
                .forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    protected synchronized void  sendPrivateMessage(Message message) {
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

    public String[] getUserListForNewUser(String login) {
        return clientHandlers.stream().filter(c->
                c.getUsername().isPresent() &&
                !c.getUsername().get().equals(login)).map(clientHandler->clientHandler.getUsername().get())
                .toArray(String[]::new);
    }
}
