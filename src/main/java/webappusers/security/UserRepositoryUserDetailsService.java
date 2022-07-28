package webappusers.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import webappusers.User;
import webappusers.security.data.UserRepository;

import java.util.Date;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private UserRepository userRepo;

    public UserRepositoryUserDetailsService(UserRepository userRepo) {

        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if(user != null && user.isEnabled()){
        user.setLastLogin(new Date());

        userRepo.save(user);

         return  user;}

        throw new UsernameNotFoundException("User: " + username + " not found");
    }
}
