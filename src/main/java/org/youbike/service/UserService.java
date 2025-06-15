package org.youbike.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import org.youbike.auth.AuthService;
import org.youbike.model.dto.UserDto;
import org.youbike.model.entity.User;
import org.youbike.model.mapper.UserMapper;
import org.youbike.repository.UserRepo;
import org.youbike.resource.UserResource.UserRequest;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepo userRepo;

    @Inject
    UserMapper userMapper;

    @Inject
    AuthService authService;

    public Optional<User> findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public UserDto register(UserRequest userRequest) {

        User user = userMapper.registerRequestToUser(userRequest);
        user.setPassword(authService.passwordEncrypt(userRequest.password()));
        userRepo.persist(user);
        return userMapper.toDto(user);
    }

    public UserDto update(String id ,UserRequest userRequest) {
        User beforeUser = userRepo.findById(id);
        User user = userMapper.updateRequestToUser(userRequest,beforeUser);
        if(userRequest.password() != null) {
            user.setPassword(authService.passwordEncrypt(userRequest.password()));
        }
        userRepo.persist(user);
        return userMapper.toDto(user);
    }

    public void delete(String id) {;
        User user = userRepo.findById(id);
        if (user != null) {
            userRepo.delete(user);
        }
    }
}
