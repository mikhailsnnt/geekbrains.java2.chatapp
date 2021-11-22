package geekbrains.java2.chatapp.server;

public class User {
    private String login;
    private String password;
    private String username;


    private   int id;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public User(int id,String login, String password, String username) {
        this.login = login;
        this.password = password;
        this.username = username;
        this.id = id;
    }
}
