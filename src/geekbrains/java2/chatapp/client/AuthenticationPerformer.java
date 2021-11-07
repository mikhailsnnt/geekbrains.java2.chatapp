package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.dto.AuthCredentials;

public interface AuthenticationPerformer {
    boolean authenticate(AuthCredentials credentials);
}
