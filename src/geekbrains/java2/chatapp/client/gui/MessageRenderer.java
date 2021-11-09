package geekbrains.java2.chatapp.client.gui;

import geekbrains.java2.chatapp.dto.Message;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MessageRenderer  implements ListCellRenderer<Message> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm");
    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(new JLabel(value.getFromUser()),BorderLayout.CENTER);
        leftPanel.add(new JLabel(dateFormat.format(value.getSendTime())), BorderLayout.SOUTH);
        if (value.isPrivate())
            leftPanel.add(new JLabel("(private)"),BorderLayout.NORTH);
        panel.add(leftPanel,BorderLayout.WEST);
        JLabel messageTextLabel = new JLabel(value.getText());
        messageTextLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        panel.add(messageTextLabel, BorderLayout.CENTER);
        return panel;
    }
}
