package geekbrains.java2.chatapp.server;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.dto.Message;
import geekbrains.java2.chatapp.server.dao.AuthenticationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChatAppServer {
    private final AuthenticationService authService;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private static final int PORT = 6009;
    public static void main(String[] args) {
        new ChatAppServer();
    }
    public ChatAppServer (){
        authService = new AuthenticationService();
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
                        clientHandler.getUsername().isPresent() &&
                                clientHandler.getUsername().get().equals(message.getTarget()))
                .forEach(clientHandler -> clientHandler.sendMessage(message));
    }

    protected synchronized void removeClientHandler(ClientHandler clientHandler){
        clientHandlers.remove(clientHandler);
    }
}
