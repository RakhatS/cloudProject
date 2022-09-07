package com.project.projectpars.services;


import com.project.projectpars.models.FIles;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.*;

import static com.project.projectpars.services.SpecialMethods.daysBetweenTwoDates;
import static com.project.projectpars.services.SpecialMethods.getSizeMB;


@Service
public class FileService {
    private long usedMemory = 0;


    public List<FIles> listFiles(List<FIles> fIlesList,String path) throws IOException {
        this.usedMemory = 0;
        int COUNT = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        Date date;
        File dir = new File(path);
        for ( File file :dir.listFiles()){
            if ( file.isFile() ){
                date=new Date(file.lastModified());
                fIlesList.add(new FIles(COUNT,file.getName(),file.getPath(),getSizeMB(file.length()), sdf.format(date),""));
                this.usedMemory += file.length();
                COUNT++;
            }
        }
        return fIlesList;
    }


    public List<FIles> listFilesBin(List<FIles> fIlesList,String path) throws IOException {
        int COUNT = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date;
        File dir = new File(path);
        for ( File file :dir.listFiles()){
            if ( file.isFile() ){
                Calendar cal = new GregorianCalendar();
                cal.setTime(new Date(file.lastModified()));
                cal.add(Calendar.DAY_OF_MONTH,31);
                long left = daysBetweenTwoDates(new Date(),cal.getTime());
                date=new Date(file.lastModified());
                fIlesList.add(new FIles(COUNT,file.getName(),file.getPath(),getSizeMB(file.length()), sdf.format(date),String.valueOf(left)+"days"));
                COUNT++;
            }
        }
        return fIlesList;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(long usedMemory) {
        this.usedMemory = usedMemory;
    }
}