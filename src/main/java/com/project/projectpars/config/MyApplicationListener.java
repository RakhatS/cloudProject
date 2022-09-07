package com.project.projectpars.config;

import com.project.projectpars.models.AuthorizationLogs;
import com.project.projectpars.models.User;
import com.project.projectpars.services.AuthoLogsService;
import com.project.projectpars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyApplicationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private AuthoLogsService authoLogsService;
    @Autowired
    UserService userService;
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Object user = event.getAuthentication().getPrincipal();
        Long id_user = userService.getUserByEmail((String) user).getId();
        authoLogsService.save(new AuthorizationLogs(id_user,new Date(),"Nur-Sultan","unsuccess"));
    }

}