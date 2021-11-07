package geekbrains.java2.chatapp.server;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.dto.AuthenticationResult;
import geekbrains.java2.chatapp.dto.ClientCommand;
import geekbrains.java2.chatapp.dto.Message;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler {
    private static final int AUTHENTICATE_TIMEOUT = 120; //Seconds
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String username;

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    private final ChatAppServer server;
    private boolean isAuthenticated;
    public ClientHandler(Socket socket , ChatAppServer server) {
        this.socket = socket;
        this.server = server;
        isAuthenticated = false;
        try {
            this.out = new ObjectOutputStream(new BufferedOutputStream( socket.getOutputStream() ) );
            out.flush();
            this.in = new ObjectInputStream( new BufferedInputStream( socket.getInputStream()));
        }
        catch (IOException ioException){
            throw  new ServerNetworkingException("Initialising clienthandler exception", ioException);
        }
        authenticate();
    }
    private void mainListenLoop(){
        while(true){
            ClientCommand commands  = (ClientCommand) readObject();
            if(commands == ClientCommand.message){
                Message message = (Message) readObject();
                if (message.isPrivate())
                    server.sendPrivateMessage(message);
                else
                    server.broadcastMessage(message);
            }
            else if (commands == ClientCommand.quit){
                server.removeClientHandler(this);
                break;
            }
        }
    }
    private void authenticate(){
        long authenticationStarted = System.currentTimeMillis();
        while(System.currentTimeMillis() - authenticationStarted < (long)1000*AUTHENTICATE_TIMEOUT){
             AuthCredentials credentials = (AuthCredentials) readObject();
            Optional<User> user = server.findUser(credentials);
            if(!user.isPresent())
            {
                sendObject(AuthenticationResult.BAD_CREDENTIALS);
                continue;
            }
            if(server.userIsLoggedIn(user.get()))
            {
                sendObject(AuthenticationResult.USER_IS_LOGGED);
                continue;
            }
            sendObject(AuthenticationResult.SUCCESSFULLY);
            sendUTF(user.get().getUsername());
            isAuthenticated = true;
            mainListenLoop();

        }
    }
    private Object readObject(){
        try{
            return in.readObject();
        }
        catch (IOException ioException){
            throw  new ServerNetworkingException("Client reading exception", ioException);
        }
        catch (ClassNotFoundException classNotFoundException){
            throw  new RuntimeException(classNotFoundException);
        }
    }
    private synchronized void sendObject(Object obj){
        try {
            out.writeObject(obj);
            out.flush();
        }
        catch (IOException ioException){
            throw new ServerNetworkingException("Client sending exception" , ioException);
        }
    }
    private synchronized void sendUTF(String str){
        try{
            out.writeUTF(str);
            out.flush();
        }
        catch (IOException ioException){
            throw new ServerNetworkingException("Client sending UTF exception",ioException);
        }
    }
    public Optional<String> getUsername(){
        return Optional.ofNullable(username);
    }
    public void sendMessage(Message message){
        sendObject(message);
    }

}
