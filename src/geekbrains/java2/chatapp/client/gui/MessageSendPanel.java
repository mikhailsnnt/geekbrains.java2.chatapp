package geekbrains.java2.chatapp.client.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class MessageSendPanel extends JPanel {
    public MessageSendPanel(Consumer<String> messageSender){
        setLayout(new BorderLayout());
        JButton sendButton = new JButton();
        JTextField sendMessageField = new JTextField() ;
        sendMessageField.addActionListener(event-> sendButton.doClick());
        sendButton.addActionListener(event->{
            if(!sendMessageField.getText().isEmpty())
            {
                messageSender.accept(sendMessageField.getText());
            }
        });
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
}
