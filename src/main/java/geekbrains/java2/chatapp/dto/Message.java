package geekbrains.java2.chatapp.dto;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private final String text;
    private final int fromUser;
    private final Date sendTime;
    private final Integer target;

    public Message(String text, int fromUser) {
        this(text,fromUser,new Date(),null);
    }

    public Message(String text, Integer fromUser, Integer target) {
        this(text,fromUser,new Date(),target);
    }

    public Message(String text , Integer fromUser, Date sendTime){
        this(text,fromUser,sendTime,null);
    }
    public Message(String text , int fromUser, Date sendTime, Integer target){
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
        return target != null;
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
