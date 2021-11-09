package geekbrains.java2.chatapp.client.adapter;

import geekbrains.java2.chatapp.dto.AuthCredentials;
@FunctionalInterface
public interface AuthenticationPerformer {
    void authenticate(AuthCredentials credentials);
}
