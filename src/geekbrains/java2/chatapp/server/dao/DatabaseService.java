package geekbrains.java2.chatapp.server.dao;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.server.User;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

public class DatabaseService {
    private static final HashSet<User> userSet = new HashSet();
//    private final String connectionURL;
    public DatabaseService(){
        userSet.add(new User("l1","p1","Vasya"));
        userSet.add(new User("l2","p2","Petya"));
        userSet.add(new User("l3","p3","u3"));
        userSet.add(new User("l4","p4","u4"));
        userSet.add(new User("l5","p5","u5"));
        userSet.add(new User("l6","p6","u6"));
    }
    public Optional<User> findUser(AuthCredentials credentials){
        return userSet.stream().filter(
                u-> u.getLogin().equals( credentials.getLogin()) && u.getPassword().equals(credentials.getPassword())
        ).findFirst();
    }
//    public synchronized Optional<User> findUser(AuthCredentials credentials){
//        try (Connection connection = DriverManager.getConnection(connectionURL)){
//            try(Statement statement = connection.createStatement()) {
//                ResultSet resultSet = statement.executeQuery();
//                if (!resultSet.next())
//                    return Optional.empty();
//            }
//        }catch (SQLException exception){
//
//        }
//    }
}
