package geekbrains.java2.chatapp.dto;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private final String text;
    private final int fromUser;
    private final Date sendTime;
    private final int target;

    public Message(String text, int fromUser) {
        this(text,fromUser,new Date(),0);
    }

    public Message(String text, int fromUser, int target) {
        this(text,fromUser,new Date(),target);
    }

    public Message(String text , int fromUser, Date sendTime){
        this(text,fromUser,sendTime,0);
    }
    public Message(String text , int fromUser, Date sendTime, int target){
        this.text = text;
        this.fromUser = fromUser;
        this.sendTime = sendTime;
        this.target = target;
    }

    public int getFromUser() {
        return fromUser;
    }

    public String getText() {
        return text;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public boolean isPrivate (){
        return target != 0;
    }

    public Integer getTarget(){
        return target;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", fromUser='" + fromUser + '\'' +
                '}';
    }
}
