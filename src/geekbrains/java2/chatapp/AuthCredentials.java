package geekbrains.java2.chatapp;

public class AuthCredentials {
    private final String username;
    private String password;

    public AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

