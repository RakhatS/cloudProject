package com.project.projectpars.controllers;


import com.project.projectpars.models.AuthorizationLogs;
import com.project.projectpars.models.FileActiveLogs;
import com.project.projectpars.models.User;
import com.project.projectpars.services.AuthoLogsService;
import com.project.projectpars.services.FileActiveService;
import com.project.projectpars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.project.projectpars.services.SpecialMethods.*;

@Controller
public class ReportController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileActiveService fileActiveService;
    @Autowired
    private AuthoLogsService authoLogsService;

    @GetMapping("/report")
    public String getReport(Model model){
        if (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles().toString().equals("[ROLE_ADMIN]"))
            return "redirect:/report-admin";
        User user = userService.getUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        Long id = user.getId();
        List<FileActiveLogs> fileActiveLogsList = fileActiveService.getAllByUserId(id);
        List<AuthorizationLogs> authorizationLogsList = authoLogsService.getAllByUserId(id);
        model.addAttribute("files",fileActiveLogsList);
        model.addAttribute("actives",authorizationLogsList);
        long memory = GigatoByte(user.getMemory());
        long usedMemory = getTotalUsedMemory(new File("").getAbsolutePath()+"/clouds/"+user.getEmail());
        model.addAttribute("usedMemory", calculatePercentage(usedMemory,memory));
        model.addAttribute("freeMemory",calculatePercentage(memory-usedMemory,memory));
        model.addAttribute("usedMemoryTxt", getSizeMB(usedMemory));
        model.addAttribute("freeMemoryTxt",getSizeMB(memory-usedMemory));


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        List<String> days = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (LocalDate d = now.minusDays(14); !d.isAfter(now); d = d.plusDays(1)) {
            days.add(d.toString());
        }
        int count = 1;
        for (String x : days) {
            model.addAttribute("day"+count,x);
            count++;
        }
        count= 1;
        for (String x : days) {
            int added = 0, removed = 0,restored = 0;
            for (FileActiveLogs fileActiveLogs: fileActiveLogsList) {
                if (x.equals(sdf.format(fileActiveLogs.getDate()))){
                    if(fileActiveLogs.getAction().equals("Added"))
                        added++;
                    if(fileActiveLogs.getAction().equals("Restored"))
                        restored++;
                    if(fileActiveLogs.getAction().equals("Removed"))
                        removed++;
                }
            }
            model.addAttribute("add"+count,added);
            model.addAttribute("rem"+count,removed);
            model.addAttribute("res"+count,restored);
            removed = 0;
            restored = 0;
            added = 0;
            count++;
        }
        count = 1;
        for (String x : days) {
            int success = 0, unsuccess = 0;
            for (AuthorizationLogs authorizationLogs: authorizationLogsList) {
                if (x.equals(sdf.format(authorizationLogs.getDate()))){
                    if(authorizationLogs.getAction().equals("success"))
                        success++;
                    else
                        unsuccess++;
                }
            }
            model.addAttribute("succ"+count,success);
            model.addAttribute("unsucc"+count,unsuccess);
            success = 0;
            unsuccess = 0;
            count++;
        }



        return "report";
    }












    @GetMapping("/report-admin")
    public String getReportAdmin(Model model) {
        if (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles().toString().equals("[ROLE_USER]"))
            return "redirect:/report";
        model.addAttribute("user",new User());
        User user = userService.getUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        Long id = user.getId();
        List<FileActiveLogs> fileActiveLogsList = fileActiveService.getAllByUserId(id);
        List<AuthorizationLogs> authorizationLogsList = authoLogsService.getAllByUserId(id);
        model.addAttribute("files",fileActiveLogsList);
        model.addAttribute("actives",authorizationLogsList);
        long memory = GigatoByte(user.getMemory());
        long usedMemory = getTotalUsedMemory(new File("").getAbsolutePath()+"/clouds/"+user.getEmail());
        model.addAttribute("usedMemory", calculatePercentage(usedMemory,memory));
        model.addAttribute("freeMemory",calculatePercentage(memory-usedMemory,memory));
        model.addAttribute("usedMemoryTxt", getSizeMB(usedMemory));
        model.addAttribute("freeMemoryTxt",getSizeMB(memory-usedMemory));


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        List<String> days = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (LocalDate d = now.minusDays(14); !d.isAfter(now); d = d.plusDays(1)) {
            days.add(d.toString());
        }
        int count = 1;
        for (String x : days) {
            model.addAttribute("day"+count,x);
            count++;
        }
        count= 1;
        for (String x : days) {
            int added = 0, removed = 0,restored = 0;
            for (FileActiveLogs fileActiveLogs: fileActiveLogsList) {
                if (x.equals(sdf.format(fileActiveLogs.getDate()))){
                    if(fileActiveLogs.getAction().equals("Added"))
                        added++;
                    if(fileActiveLogs.getAction().equals("Restored"))
                        restored++;
                    if(fileActiveLogs.getAction().equals("Removed"))
                        removed++;
                }
            }
            model.addAttribute("add"+count,added);
            model.addAttribute("rem"+count,removed);
            model.addAttribute("res"+count,restored);
            removed = 0;
            restored = 0;
            added = 0;
            count++;
        }
        count = 1;
        for (String x : days) {
            int success = 0, unsuccess = 0;
            for (AuthorizationLogs authorizationLogs: authorizationLogsList) {
                if (x.equals(sdf.format(authorizationLogs.getDate()))){
                    if(authorizationLogs.getAction().equals("success"))
                        success++;
                    else
                        unsuccess++;
                }
            }
            model.addAttribute("succ"+count,success);
            model.addAttribute("unsucc"+count,unsuccess);
            success = 0;
            unsuccess = 0;
            count++;
        }
        return "report-admin";
    }

    @PostMapping("/report-admin")
    public String postReportAdmin(Model model, @ModelAttribute("user")User user) {
        if (((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles().toString().equals("[ROLE_USER]"))
            return "redirect:/report";
        Long id = userService.getUserByEmail(user.getEmail()).getId();
        List<FileActiveLogs> fileActiveLogsList = fileActiveService.getAllByUserId(id);
        List<AuthorizationLogs> authorizationLogsList = authoLogsService.getAllByUserId(id);
        model.addAttribute("files",fileActiveLogsList);
        model.addAttribute("actives",authorizationLogsList);
        long memory = GigatoByte(user.getMemory());
        long usedMemory = getTotalUsedMemory(new File("").getAbsolutePath()+"/clouds/"+user.getEmail());
        model.addAttribute("usedMemory", calculatePercentage(usedMemory,memory));
        model.addAttribute("freeMemory",calculatePercentage(memory-usedMemory,memory));
        model.addAttribute("usedMemoryTxt", getSizeMB(usedMemory));
        model.addAttribute("freeMemoryTxt",getSizeMB(memory-usedMemory));


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        List<String> days = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (LocalDate d = now.minusDays(14); !d.isAfter(now); d = d.plusDays(1)) {
            days.add(d.toString());
        }
        int count = 1;
        for (String x : days) {
            model.addAttribute("day"+count,x);
            count++;
        }
        count= 1;
        for (String x : days) {
            int added = 0, removed = 0,restored = 0;
            for (FileActiveLogs fileActiveLogs: fileActiveLogsList) {
                if (x.equals(sdf.format(fileActiveLogs.getDate()))){
                    if(fileActiveLogs.getAction().equals("Added"))
                        added++;
                    if(fileActiveLogs.getAction().equals("Restored"))
                        restored++;
                    if(fileActiveLogs.getAction().equals("Removed"))
                        removed++;
                }
            }
            model.addAttribute("add"+count,added);
            model.addAttribute("rem"+count,removed);
            model.addAttribute("res"+count,restored);
            removed = 0;
            restored = 0;
            added = 0;
            count++;
        }
        count = 1;
        for (String x : days) {
            int success = 0, unsuccess = 0;
            for (AuthorizationLogs authorizationLogs: authorizationLogsList) {
                if (x.equals(sdf.format(authorizationLogs.getDate()))){
                    if(authorizationLogs.getAction().equals("success"))
                        success++;
                    else
                        unsuccess++;
                }
            }
            model.addAttribute("succ"+count,success);
            model.addAttribute("unsucc"+count,unsuccess);
            success = 0;
            unsuccess = 0;
            count++;
        }


        return "report-admin";
    }
}
