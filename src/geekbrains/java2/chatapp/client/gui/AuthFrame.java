package geekbrains.java2.chatapp.client.gui;
import geekbrains.java2.chatapp.client.adapter.AuthenticationPerformer;
import geekbrains.java2.chatapp.dto.AuthCredentials;
import javax.swing.*;
import java.awt.*;

public class AuthFrame extends JFrame {
    public AuthFrame(AuthenticationPerformer performAuthentication){
        initView(performAuthentication);
    }
    private void initView(AuthenticationPerformer performAuthentication){

        setTitle("Chat-app authentication");
        setBounds(300,300,500,450);
        JTextField loginTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPanel panel = new JPanel(new GridLayout(2,1));
        loginTextField.setColumns(20);
        passwordField.setColumns(20);
        panel.add(loginTextField);
        panel.add(passwordField);
        add(panel);
        JButton authButton = new JButton("Log in");
        authButton.addActionListener(e -> {
            if(!loginTextField.getText().isEmpty() && passwordField.getPassword().length != 0)
                performAuthentication.authenticate(
                        new AuthCredentials(loginTextField.getText(),String.valueOf(passwordField.getPassword())));
        });
        add(authButton, BorderLayout.SOUTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginTextField.addActionListener(e->passwordField.requestFocus());
        passwordField.addActionListener(e->authButton.doClick());

        pack();
        setVisible(true);
    }
    public void closeView(){
        setVisible(false);
        dispose();
    }
    public void showAlert(String text){
        JOptionPane.showMessageDialog(this,text);
    }

}
