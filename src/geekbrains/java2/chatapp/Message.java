package geekbrains.java2.chatapp;

import java.util.Date;
import java.util.Optional;

public class Message {

    private final String text;
    private final String fromUser;
    private final Date sendTime;
    private final String target;
    public Message(String text , String fromUser, Date sendTime){
        this.text = text;
        this.fromUser = fromUser;
        this.sendTime = sendTime;
        this.target = null;
    }
    public Message(String text , String fromUser, Date sendTime, String target){
        this.text = text;
        this.fromUser = fromUser;
        this.sendTime = sendTime;
        this.target = target;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getText() {
        return text;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public boolean isPrivate (){
        return target == null;
    }

    public String getTarget(){
        return target;
    }
}
