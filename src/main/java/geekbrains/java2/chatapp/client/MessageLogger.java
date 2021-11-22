package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.dto.Message;

import java.io.*;
import java.util.ArrayList;

public class MessageLogger {
    private final File logFile;
    public MessageLogger(){
        logFile = new File("/Users/mihailnikitin/Desktop/GeekBrains/Java2/ChatApp/src/main/java/geekbrains/java2/chatapp/client/msg.log");
    }
    public void writeMessage(Message message){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(logFile,true)))
        {
            oos.writeObject(message);
        }
        catch (IOException exception)
        {
            throw  new RuntimeException(exception);
        }
    }
    public ArrayList<Message> readMessages(int limit){
        ArrayList<Message> messages =  new ArrayList<>();
        if(limit == -1)
            limit = Integer.MAX_VALUE;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(logFile))) {
            int numberOfMessages = 0;
            while (numberOfMessages++ < limit)
                messages.add((Message) ois.readObject());
        }
        catch (EOFException ignored){ }
        catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
        return messages;
    }

}
