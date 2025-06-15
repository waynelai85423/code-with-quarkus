package org.youbike.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import org.youbike.model.entity.User;

@ApplicationScoped
public class UserRepo implements PanacheRepositoryBase<User, String> {

    public Optional<User> findByUserName(String userName) {
        return find("userName", userName).firstResultOptional();
    }

}
