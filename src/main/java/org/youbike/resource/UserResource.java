package org.youbike.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.youbike.auth.AuthService;
import org.youbike.auth.JwtUtil;
import org.youbike.model.dto.UserDto;
import org.youbike.model.entity.User;
import org.youbike.model.mapper.UserMapper;
import org.youbike.service.UserService;

@Path("/users")
@Transactional
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    JwtUtil jwtUtil;

    @Inject
    AuthService authService;

    @Inject
    UserMapper userMapper;

    @Inject
    JsonWebToken jwt;

    public record UserRequest(String userName, String password, String email) {}

    @POST
    @Path("/register")
    public Response register(UserRequest userRequest) {
        UserDto userDto = userService.register(userRequest);
        return Response.ok(userDto).status(Response.Status.CREATED).build();
    }


    public record LoginRequest(String userName, String password) {}

    @POST
    @Path("/login")
    public Response LoginRequest(LoginRequest loginRequest) {
        User user = authService.checkValidUser(loginRequest);
        String token = jwtUtil.generateToken(user);
        Map<String, Object> result = new HashMap<>();
        result.put("data", userMapper.toDto(user));
        result.put("token", token);
        return Response.ok(result).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed("user")
    public Response me() {
        String username = jwt.getName();
        User user = userService.findByUserName(username).orElse(null);
        return Response.ok(userMapper.toDto(user)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    public Response update(@PathParam("id") String id, UserRequest userRequest) {
        UserDto user = userService.update(id,userRequest);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    public Response delete(@PathParam("id") String id) {
        userService.delete(id);
        return Response.noContent().build();
    }

}
