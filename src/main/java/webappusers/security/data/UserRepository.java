package webappusers.security.data;

import org.springframework.data.repository.CrudRepository;
import webappusers.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
