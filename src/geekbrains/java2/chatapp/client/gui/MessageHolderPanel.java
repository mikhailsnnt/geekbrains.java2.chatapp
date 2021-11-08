package geekbrains.java2.chatapp.client.gui;

import geekbrains.java2.chatapp.dto.Message;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MessageHolderPanel extends JScrollPane {
    private final DefaultListModel<Message> messageModel = new DefaultListModel<>();
    public MessageHolderPanel(){
        JList<Message> messageJList = new JList<>(messageModel);
        messageJList.setVisibleRowCount(3);
        setViewportView(messageJList);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        setVisible(true);
    }

    public DefaultListModel<Message> getMessageModel() {
        return messageModel;
    }
}
