package geekbrains.java2.chatapp.server;

import geekbrains.java2.chatapp.dto.*;

import java.io.*;
import java.net.Socket;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Optional;

public class ClientHandler {
    private static final int AUTHENTICATE_TIMEOUT = 120; //Seconds
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String username;

    public boolean isAuthenticated() {
        return username != null;
    }

    private final ChatAppServer server;

    public ClientHandler(Socket socket , ChatAppServer server) {
        this.socket = socket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(new BufferedOutputStream( socket.getOutputStream() ) );
            out.flush();
            this.in = new ObjectInputStream( new BufferedInputStream( socket.getInputStream()));
        }
        catch (IOException ioException){
            throw  new ServerNetworkingException("Initialising clienthandler exception", ioException);
        }
    }
    private void mainListenLoop(){
        while(true){
            ClientCommand commands  = (ClientCommand) readObject();
            if(commands == ClientCommand.message){
                Message message = (Message) readObject();
                if(message == null)
                    return;

                if (message.isPrivate())
                    server.sendPrivateMessage(message);
                else
                    server.broadcastMessage(message);
            }
            else if (commands == ClientCommand.quit || commands == null){
                closeSession();
                break;
            }
        }
    }
    public void authenticate(){
        setSocketTimeout(AUTHENTICATE_TIMEOUT*1000);
        while(true){
            AuthCredentials credentials;
             try{
                 credentials = (AuthCredentials) in.readObject();
             }
             catch (SocketTimeoutException socketTimeoutException){
                 sendObject(AuthenticationResult.TIMEOUT);
                 closeSession();
                 return;
             }
             catch (ClassCastException | ClassNotFoundException | IOException exception){
                 closeSession();
                 if (! (exception instanceof EOFException ))
                     exception.printStackTrace();
                 return;
             }
             if(credentials == null)
                 return;
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
            username = user.get().getUsername();
            server.notifyClientsAboutNewUser(username);
            sendObject(server.getUserListForNewUser(username));
            setSocketTimeout(0);
            mainListenLoop();
            return;
        }

    }
    private Object readObject(){
        try{
            return in.readObject();
        }
        catch (EOFException e){
            closeSession();
            return null;
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
        catch (EOFException exception){
            closeSession();
        }
        catch (IOException ioException){
            throw new ServerNetworkingException("Client sending UTF exception",ioException);
        }
    }
    public Optional<String> getUsername(){
        return Optional.ofNullable(username);
    }
    public void sendMessage(Message message){
        sendObject(ServerCommand.MESSAGE);
        sendObject(message);
    }
    public synchronized void closeSession(){
        try{
            if(!socket.isClosed())
                socket.close();
            in.close();
            out.close();
            server.removeClientHandler(this);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
    public synchronized void sendNewUserInfo(String login){
        sendObject(ServerCommand.NEW_USER);
        sendUTF(login);
    }
    public synchronized void sendDisconnectedUserInfo(String login){
        sendObject(ServerCommand.REMOVE_USER);
        sendUTF(login);
    }
    private void setSocketTimeout(int timeout){
        try {
            socket.setSoTimeout(timeout);
        }
        catch (SocketException exception){
            closeSession();
            throw new ServerNetworkingException("Exception setting soTimeout",exception);
        }
    }


}
