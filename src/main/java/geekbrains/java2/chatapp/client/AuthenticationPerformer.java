package geekbrains.java2.chatapp.client;

import geekbrains.java2.chatapp.dto.AuthCredentials;
@FunctionalInterface
public interface AuthenticationPerformer {
    void authenticate(AuthCredentials credentials);
}
