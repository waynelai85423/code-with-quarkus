package org.youbike.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.youbike.model.entity.User;

@ApplicationScoped
public class JwtUtil {

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String generateToken(User user) {
        Map<String,Object> userInfo = Map.of(
                "userName", user.getUserName(),
                "email", user.getEmail()
        );

        long expSeconds = (System.currentTimeMillis() + 3600_000) / 1000; // 1 小時後過期
        return Jwt.issuer(issuer)
                .upn(user.getUserName())
                .groups(Set.of("user"))
                .claim("userInfo", userInfo)
                .expiresAt(expSeconds)
                .sign();
    }
}
