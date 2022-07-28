package webappusers.security;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webappusers.User;
import webappusers.UsersForm;
import webappusers.security.data.UserRepository;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller()
@RequestMapping("/")
public class UsersController {



    private UserRepository userRepo;

    public UsersController(UserRepository userRepository) {

        this.userRepo = userRepository;
    }


    @ModelAttribute
    public void addUsersToModel(Model model) {

        List<User> users = new ArrayList<>();

        userRepo.findAll().forEach(i -> users.add(i));

        model.addAttribute("users", users);
    }

    @ModelAttribute(name = "usersList")
    public UsersForm usersForm() {
        return new UsersForm();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String showTableUsers() {

        return ("users");
    }

    @PostMapping(params = "block")
    public String blockUsers(@ModelAttribute("users") List<User> users, UsersForm usersList, HttpSession session, @AuthenticationPrincipal User user) {

        if(checkUserStatus(user, session)) return "redirect:/";


        List<User> usersFiltered = users.stream()
                .filter(u -> usersList
                .getUsersName()
                .contains(u.getUsername()))
                .collect(Collectors.toList());

      usersFiltered.forEach(u->u.setStatus("blocked"));

        userRepo.saveAll(usersFiltered);

        if(usersList.getUsersName().contains(user.getUsername())) session.invalidate();

        return "redirect:/";
    }

    @PostMapping(params = "unblock")
    public String unblockUsers(@ModelAttribute("users") List<User> users, UsersForm usersList, HttpSession session, @AuthenticationPrincipal User user) {

        if(checkUserStatus(user, session)) return "redirect:/";

        List<User> usersFiltered = users.stream()
                .filter(u -> usersList
                        .getUsersName()
                        .contains(u.getUsername()))
                .collect(Collectors.toList());

        usersFiltered.forEach(u->u.setStatus("active"));

        userRepo.saveAll(usersFiltered);

        return "redirect:/";
    }

    @PostMapping(params = "delete")
    public String deleteUsers(@ModelAttribute("users") List<User> users, UsersForm usersList, HttpSession session, @AuthenticationPrincipal User user) {

        if(checkUserStatus(user, session)) return "redirect:/";

        List<User> usersFiltered = users.stream()
                .filter(u -> usersList
                        .getUsersName()
                        .contains(u.getUsername()))
                .collect(Collectors.toList());

        userRepo.deleteAll(usersFiltered);

        return "redirect:/";
    }
    private boolean checkUserStatus(User user, HttpSession session) {
        User userCheck = userRepo.findByUsername(user.getUsername());
        if(userCheck == null || !userCheck.isEnabled()){
            session.invalidate();
            return true;
        }
        return false;
    }

}

