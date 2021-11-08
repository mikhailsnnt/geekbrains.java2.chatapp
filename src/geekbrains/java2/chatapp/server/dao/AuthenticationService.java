package geekbrains.java2.chatapp.server.dao;

import geekbrains.java2.chatapp.dto.AuthCredentials;
import geekbrains.java2.chatapp.server.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuthenticationService {
    private static final HashSet<User> userSet = new HashSet();
    public AuthenticationService(){
        userSet.add(new User("l1","p1","u1"));
        userSet.add(new User("l2","p2","u2"));
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
}
