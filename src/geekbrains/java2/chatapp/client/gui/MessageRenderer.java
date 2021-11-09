package geekbrains.java2.chatapp.client.gui;

import geekbrains.java2.chatapp.dto.Message;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MessageRenderer  implements ListCellRenderer<Message> {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm");
    private String myUsername;
    private static final Color myMessagesColor = new Color(173,216,230);
    private static final Color privateMessagesColor = new Color(255,255,237);

    public MessageRenderer(String myUsername) {
        this.myUsername = myUsername;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        String fromUser= value.getFromUser();
        if(fromUser.equals(myUsername))
            panel.setBackground(myMessagesColor);
        else
            leftPanel.add(new JLabel(fromUser),BorderLayout.CENTER);
        if (value.isPrivate())
            leftPanel.setBackground(privateMessagesColor);
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
