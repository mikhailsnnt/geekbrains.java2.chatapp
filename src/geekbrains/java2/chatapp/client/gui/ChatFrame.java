package geekbrains.java2.chatapp.client.gui;

import geekbrains.java2.chatapp.client.adapter.SendPerformer;
import geekbrains.java2.chatapp.dto.Message;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ChatFrame extends JFrame {
    MessageHolderPanel messages;
    public ChatFrame(SendPerformer messageSender){
        setBounds(100,100,600,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeViews(messageSender);
        setVisible(true);
    }
    private void initializeViews(SendPerformer messageSender){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        messages = new MessageHolderPanel();
        messages.setBorder(BorderFactory.createLineBorder(Color.lightGray,3));
        panel.add(messages, BorderLayout.CENTER);
        setTitle("Chat frame");
        panel.add(new MessageSendPanel(messageSender),BorderLayout.SOUTH);
        panel.setBackground(Color.black);
        setContentPane(panel);


    }

    public DefaultListModel<Message> getMessageModel(){
        return messages.getMessageModel();
    }
}
