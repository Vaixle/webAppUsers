package webappusers.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import webappusers.User;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String fullName;
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {

        return new User(username, passwordEncoder.encode(password), fullName, email);
    }

}
