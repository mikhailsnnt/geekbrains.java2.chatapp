package geekbrains.java2.chatapp.client.gui;

import geekbrains.java2.chatapp.client.adapter.SendPerformer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class MessageSendPanel extends JPanel {
    private static final String allUsersText = "Всем";
    private final JComboBox<String> targetUser;
    public MessageSendPanel(SendPerformer messageSender){
        setLayout(new BorderLayout());
        JButton sendButton = new JButton();
        JTextField sendMessageField = new JTextField() ;
        targetUser = new JComboBox<>(new String[]{allUsersText});
        sendMessageField.addActionListener(event-> sendButton.doClick());
        sendButton.addActionListener(event->{
            if(!sendMessageField.getText().isEmpty())
            {
                String target = null;
                if(!allUsersText.equals(targetUser.getSelectedItem()))
                    target = (String)targetUser.getSelectedItem();
                messageSender.send(sendMessageField.getText(),target);
            }
        });
        add(targetUser,BorderLayout.WEST);
        add(sendMessageField,BorderLayout.CENTER);
        try{
            Image iconImage = ImageIO.read(new File("resources/send_icon.png"));
            sendButton.setIcon(new ImageIcon(iconImage));
        }
        catch (IOException exception){
            throw new ResourceLoadingException("Button image loading failed" , exception);
        }
        add(sendButton,BorderLayout.EAST);
        setVisible(true);
    }
    protected void addUser(String username){
        targetUser.addItem(username);
    }
    protected void removeUser(String username){
        targetUser.removeItem(username);
    }
}
