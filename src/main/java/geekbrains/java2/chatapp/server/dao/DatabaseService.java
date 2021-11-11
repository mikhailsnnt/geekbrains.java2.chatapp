package geekbrains.java2.chatapp.server.dao;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.server.User;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public class DatabaseService {
    private static final HashSet<User> userSet = new HashSet();
    private final String connectionURL;
    public DatabaseService(String connectionURL) throws DAOException{
//        userSet.add(new User("l1","p1","Vasya"));
//        userSet.add(new User("l2","p2","Petya"));
//        userSet.add(new User("l3","p3","u3"));
//        userSet.add(new User("l4","p4","u4"));
//        userSet.add(new User("l5","p5","u5"));
//        userSet.add(new User("l6","p6","u6"));
        this.connectionURL = connectionURL;
        try(Connection connection = DriverManager.getConnection(connectionURL)) {
        }
        catch (SQLException exception){
            throw new DAOException("Initialising DAO exception", exception );
        }
    }

    public synchronized Optional<User> findUser(AuthCredentials credentials) throws DAOException{
        try (Connection connection = DriverManager.getConnection(connectionURL)){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users WHERE login = ? AND password = ?")) {
                statement.setString(1,credentials.getLogin());
                statement.setString(2,credentials.getPassword());
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next())
                    return Optional.empty();
                return Optional.of(
                        new User(resultSet.getString("login"), resultSet.getString("password"),resultSet.getString("username"))
                );
            }
        }catch (SQLException exception){
            throw new DAOException("User searching exception",exception);
        }
    }
}
