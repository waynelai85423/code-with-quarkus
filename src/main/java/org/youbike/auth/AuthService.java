package org.youbike.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.youbike.model.entity.User;
import org.youbike.resource.UserResource.LoginRequest;
import org.youbike.service.UserService;

@ApplicationScoped
public class AuthService {

    @Inject
    UserService userService;

    public String passwordEncrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User checkValidUser(LoginRequest loginRequest) {
        User user = userService.findByUserName(loginRequest.userName()).orElseThrow(
                () -> new NotFoundException("User not found: " + loginRequest.userName()));
        if (!BCrypt.checkpw(loginRequest.password(), user.getPassword())) {
            throw new NotFoundException("Invalid password for user: " + loginRequest.password());
        }
        return user;
    }
}
