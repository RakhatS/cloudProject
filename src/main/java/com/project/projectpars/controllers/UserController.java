package com.project.projectpars.controllers;

import com.project.projectpars.models.AuthorizationLogs;
import com.project.projectpars.models.User;
import com.project.projectpars.services.AuthoLogsService;
import com.project.projectpars.services.UserService;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.io.File;




@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user",new User());
        return "registration";
    }
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user")User user) {
        if(userService.getUserByEmail(user.getEmail()) != null){
            return "redirect:/registration";
        }
        userService.createUser(user);
        String path = "clouds/" +user.getEmail();
        File dir=new File(path);
        if(dir.exists()){
            System.out.println("error");
        }else{
            dir.mkdir();
        }
        String path2 = path +="/recyclebin/";
        dir=new File(path2);
        if(dir.exists()){
            System.out.println("error");
        }else{
            dir.mkdir();
        }

        //recyclebin
        return "redirect:/login";
    }

}