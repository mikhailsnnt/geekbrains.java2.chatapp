package geekbrains.java2.chatapp.client.gui;

import geekbrains.java2.chatapp.dto.Message;
import geekbrains.java2.chatapp.client.ChatGuiClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ChatFrame extends JFrame {
    JTextArea messages;
    ChatGuiClient chatGuiClient;
    public ChatFrame(ChatGuiClient client){
        chatGuiClient = client;
        setBounds(100,100,600,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeViews();
        setVisible(true);
    }
    private void initializeViews(){
        messages = new JTextArea();
        messages.setEditable(false);
        messages.setBorder(BorderFactory.createLineBorder(Color.lightGray,3));
        add(messages, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        JButton sendButton = new JButton();
        JTextField sendMessageField = new JTextField() ;
        sendMessageField.addActionListener(event-> sendButton.doClick());
        sendButton.addActionListener(event->{
            if(!sendMessageField.getText().isEmpty())
            {
                chatGuiClient.sendMessage(sendMessageField.getText());
            }
        });
        bottom.add(sendMessageField,BorderLayout.CENTER);
        try{
            Image iconImage = ImageIO.read(new File("resources/send_icon.png"));
            sendButton.setIcon(new ImageIcon(iconImage));
        }
        catch (IOException exception){
            throw new ResourceLoadingException("Button image loading failed" , exception);
        }
        add(bottom,BorderLayout.SOUTH);
        bottom.add(sendButton,BorderLayout.EAST);
    }
    public void addOutComingMessage(Message message){
        messages.setText(messages.getText() + "\n" + message.getText());
    }
}
