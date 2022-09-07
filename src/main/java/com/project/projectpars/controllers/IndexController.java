package com.project.projectpars.controllers;


import com.project.projectpars.models.FIles;
import com.project.projectpars.models.FileActiveLogs;
import com.project.projectpars.models.User;
import com.project.projectpars.repo.UserRepository;
import com.project.projectpars.services.FileActiveService;
import com.project.projectpars.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import static com.project.projectpars.services.SpecialMethods.*;


@Controller
@RequestMapping("/index")
public class IndexController {
    private String pathUser;

    @Autowired
    FileService fileService;
    @Autowired
    FileActiveService fileActiveService;
    private boolean isWarning = false;


    @GetMapping
    public String index_home(Model model) {
        pathUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        List<FIles> fIlesList = new ArrayList<>();
        try {
            fileService.listFiles(fIlesList,(new File("").getAbsolutePath() + "/clouds/"+pathUser));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isWarning){
            model.addAttribute("warningText","Not enough memory");
            isWarning = false;
        }
        if(!fIlesList.isEmpty())
            model.addAttribute("files",fIlesList);
        long used_memory = fileService.getUsedMemory();
        long memory = GigatoByte(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemory());
        model.addAttribute("percent",calculatePercentage(used_memory,memory) + "%");
        model.addAttribute("totalmemory",getSizeMB(memory));
        model.addAttribute("usedmemory",getSizeMB(used_memory));
        model.addAttribute("percenttext",String.format("%.2f",calculatePercentage(used_memory,memory)) + "%");
        return "index";
    }

    @PostMapping("/add")
    public String uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        long givedMemory = GigatoByte(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMemory());
            List<FIles> fIlesList = new ArrayList<>();
            try {
                fileService.listFiles(fIlesList,(new File("").getAbsolutePath() + "/clouds/"+pathUser));
            } catch (IOException e) {
                e.printStackTrace();
            }
        long memory = givedMemory - fileService.getUsedMemory();
        long totalMemory = 0;
        for(MultipartFile file:files)totalMemory+=file.getSize();
        if((memory - totalMemory) < 0) {
            isWarning = true;
            return "redirect:/index";
        }
        String cop = "";
        int count = 1;
        for(MultipartFile file:files){
            while(findFile(file.getOriginalFilename() + cop,new File("clouds/"+pathUser))){
                cop = "("+count+")";
                count++;
            }
            multipartToFile(file).renameTo(new File("clouds/" + pathUser + "/" + file.getOriginalFilename() + cop));
            fileActiveService.save(new FileActiveLogs(userId,file.getOriginalFilename() + cop,"Added",new Date()));

        }
        return "redirect:/index";
    }



    @GetMapping("/recycle-bin/{id}/delete")
    public String deleteFile(@PathVariable("id") int id){
        List<FIles> fIlesList = new ArrayList<>();
        try {
            fileService.listFiles(fIlesList,(new File("").getAbsolutePath() + "/clouds/"+pathUser+"/recyclebin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!fIlesList.isEmpty() && fIlesList.get(id) != null){
            String fileName = fIlesList.get(id).getFile_path();
            try {
                Files.delete(Paths.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/index/recycle-bin";
    }

    @GetMapping("/{id}/remove")
    public String addToCartFile(@PathVariable("id") int id){
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<FIles> fIlesList = new ArrayList<>();
        try {
            fileService.listFiles(fIlesList,(new File("").getAbsolutePath() + "/clouds/"+pathUser));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!fIlesList.isEmpty() && fIlesList.get(id) != null){
            String fileName = fIlesList.get(id).getFile_path();
            File file = new File(fileName);
            String cop = "";
            int count = 1;
            while(findFile(file.getName() + cop,new File("clouds/"+pathUser+"/recyclebin"))){
                cop = "("+count+")";
                count++;
            }
            try {
                Files.move(Path.of(file.getAbsolutePath()), Path.of(new File("").getAbsolutePath()+"/clouds/"+pathUser +"/recyclebin/"+file.getName()+ cop));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileActiveService.save(new FileActiveLogs(userId,fIlesList.get(id).getName()+ cop,"Removed",new Date()));

        }
        return "redirect:/index";
    }

    @GetMapping("/recycle-bin")
    public String getKorzina(Model model){
        pathUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();

        List<FIles> fIlesList = new ArrayList<>();
        try {
            fileService.listFilesBin(fIlesList,(new File("").getAbsolutePath() + "/clouds/"+pathUser + "/recyclebin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (FIles fIles:fIlesList){
            if(fIles.getLeft().equals("0days")){
                try {
                    Files.delete(Paths.get(fIles.getFile_path()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!fIlesList.isEmpty())
            model.addAttribute("files",fIlesList);
        return "recyclebin";
    }

    @GetMapping("/recycle-bin/{id}/reestablish")
    public String reestablishFile(@PathVariable("id") int id){
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<FIles> fIlesList = new ArrayList<>();
        try {
            fileService.listFiles(fIlesList,(new File("").getAbsolutePath() + "/clouds/"+pathUser+ "/recyclebin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!fIlesList.isEmpty() && fIlesList.get(id) != null){
            String fileName = fIlesList.get(id).getFile_path();
            File file = new File(fileName);

            String cop = "";
            int count = 1;
                while(findFile(file.getName() + cop,new File("clouds/"+pathUser))){
                    cop = "("+count+")";
                    count++;
                }
            try {
                Files.move(Path.of(file.getAbsolutePath()), Path.of(new File("").getAbsolutePath()+"/clouds/"+pathUser+"/"+file.getName()+ cop));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileActiveService.save(new FileActiveLogs(userId,fIlesList.get(id).getName()+ cop,"Restored",new Date()));


        }
        return "redirect:/index/recycle-bin";
    }




    public boolean findFile(String name,File file) {
        File[] list = file.listFiles();
        if (list != null)
            for (File fil : list) {
                if (fil.isDirectory()) {
                    findFile(name, fil);
                } else if (name.equalsIgnoreCase(fil.getName())) {
                    return true;
                }
            }
        return false;
    }

}
