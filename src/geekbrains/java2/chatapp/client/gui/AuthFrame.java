package geekbrains.java2.chatapp.client.gui;
import geekbrains.java2.chatapp.client.AuthenticationPerformer;
import geekbrains.java2.chatapp.dto.AuthCredentials;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class AuthFrame {
    private JFrame frame;
    public AuthFrame(AuthenticationPerformer performAuthentication){
        initView(performAuthentication);
    }
    private void initView(AuthenticationPerformer performAuthentication){
        frame = new JFrame();
        frame.setTitle("Chat-app authentication");
        frame.setBounds(300,300,500,450);
        JTextField loginTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(2,1));
        loginTextField.setColumns(20);
        passwordField.setColumns(20);
        panel.add(loginTextField);
        panel.add(passwordField);
        frame.add(panel);
        JButton authButton = new JButton("Log in");
        authButton.addActionListener(e -> {
            if(!loginTextField.getText().isEmpty() && passwordField.getPassword().length != 0)
                performAuthentication.authenticate(
                        new AuthCredentials(loginTextField.getText(),String.valueOf(passwordField.getPassword())));
        });
        frame.add(authButton, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public void dispose(){
        frame.setVisible(false);
        frame.dispose();
    }

}
