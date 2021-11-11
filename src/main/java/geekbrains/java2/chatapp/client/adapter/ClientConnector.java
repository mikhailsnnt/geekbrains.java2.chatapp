package geekbrains.java2.chatapp.client.adapter;

import java.io.*;
import java.net.Socket;

public class ClientConnector {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    public ClientConnector(String host, int port){
        try {
            socket = new Socket(host, port);
            in = new ObjectInputStream( new BufferedInputStream( socket.getInputStream()));
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.flush();
        }
        catch (IOException ioException){
            throw  new ClientNetworkingException("Connection establishing exception", ioException);
        }
    }
    public void sendObject(Object object){
        try{
            out.writeObject(object);
            out.flush();
        }catch (IOException ioException){
            throw new ClientNetworkingException("Sending object exception", ioException);
        }
    }
    public Object readObject(){
        try {
            return in.readObject();
        }
        catch (IOException ioException){
            throw new ClientNetworkingException("Reading object exception", ioException);
        }
        catch (ClassNotFoundException classNotFoundException){
            throw new RuntimeException(classNotFoundException);
        }
    }
    public String readUTF(){
        try{
            return in.readUTF();
        }
        catch (IOException ioException){
            throw new ClientNetworkingException("Reading UTF exception", ioException);
        }
    }
}
