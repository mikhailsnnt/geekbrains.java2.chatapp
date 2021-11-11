package geekbrains.java2.chatapp.server.dao;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.dto.Message;
import geekbrains.java2.chatapp.server.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class DatabaseService {

    private final String connectionURL;
    public DatabaseService(String connectionURL) throws DAOException{
        this.connectionURL = connectionURL;
        try(Connection connection = DriverManager.getConnection(connectionURL)) {
            System.out.println("Successfully connected to database");
        }
        catch (SQLException exception){
            throw new DAOException("Initialising DAO exception", exception );
        }
    }

    public  Optional<User> findUser(AuthCredentials credentials) throws DAOException{
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

    public  void writeMessage(Message message) throws DAOException{
        try(Connection connection = DriverManager.getConnection(connectionURL)){
            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO Messages(text,fromUser,target,sendTime) VALUE (?,?,?,?)"))
            {
                statement.setString(1,message.getText());
                statement.setString(2,message.getFromUser());
                statement.setString(3,message.getTarget());
                statement.setTimestamp(4,new Timestamp(message.getSendTime().getTime()));
                statement.executeUpdate();
            }
        }
        catch (SQLException exception)
        {
            throw new DAOException("Message writing exception", exception);
        }
    }
    public  ArrayList<Message> getMessagesWithUser(String username) throws DAOException{
        try(Connection connection = DriverManager.getConnection(connectionURL)){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Messages WHERE target IS NULL OR fromUser = ? OR target = ? ORDER BY sendTime"))
            {
                statement.setString(1,username);
                statement.setString(2,username);
                ResultSet resultSet = statement.executeQuery();
                ArrayList<Message> messages = new ArrayList<>();
                while(resultSet.next()){
                    messages.add(new Message(
                            resultSet.getString("text"),
                            resultSet.getString("fromUser"),
                            new Date( resultSet.getTimestamp("sendTime").getTime()),
                            resultSet.getString("target")
                    ));
                }
                return messages;
            }
        }catch (SQLException exception){
            throw new DAOException("Messages reading exception");
        }
    }
}
