package geekbrains.java2.chatapp.client.gui;
import geekbrains.java2.chatapp.client.ChatGuiClient;
import geekbrains.java2.chatapp.client.SendPerformer;
import geekbrains.java2.chatapp.dto.Message;
import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {
    MessageHolderPanel messages;
    private MessageSendPanel messageSendPanel;
    public ChatFrame(ChatGuiClient client,SendPerformer messageSender,String myUsername){
        setBounds(100,100,600,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeViews(client,messageSender,myUsername);
        setTitle("Chat frame: "+myUsername);
        setVisible(true);
    }

    private void initializeViews(ChatGuiClient client, SendPerformer messageSender, String myUsername){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        messages = new MessageHolderPanel(client, myUsername);
        messages.setBorder(BorderFactory.createLineBorder(Color.lightGray,3));
        panel.add(messages, BorderLayout.CENTER);
        messageSendPanel = new MessageSendPanel(messageSender);
        panel.add(messageSendPanel,BorderLayout.SOUTH);
        panel.setBackground(Color.black);
        setContentPane(panel);


    }
    public DefaultListModel<Message> getMessageModel(){
        return messages.getMessageModel();
    }
    public void addUser(String username){
        messageSendPanel.addUser(username);
    }
    public void removeUser(String username){
        messageSendPanel.removeUser(username);
    }
}
