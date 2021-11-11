package geekbrains.java2.chatapp.dto;

import java.io.Serializable;

public class AuthCredentials implements Serializable {
    private final String login;
    private String password;

    public AuthCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

