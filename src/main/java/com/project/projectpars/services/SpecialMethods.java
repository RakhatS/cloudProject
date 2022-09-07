package com.project.projectpars.services;




import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;


public class SpecialMethods {
    public static File multipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static String getSizeMB(long size) {
        String s = "";
        double kb = size / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;
        double tb = gb / 1024;
        if(size < 1024L) {
            s = size + " Bytes";
        } else if(size >= 1024 && size < (1024L * 1024)) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= (1024L * 1024) && size < (1024L * 1024 * 1024)) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= (1024L * 1024 * 1024) && size < (1024L * 1024 * 1024 * 1024)) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= (1024L * 1024 * 1024 * 1024)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }
    public static double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
    public static long GigatoByte(long giga){return (long) (giga*Math.pow(1024,3));}

    public static long getTotalUsedMemory(String path){
        long sum = 0;
        File dir = new File(path);
        for ( File file :dir.listFiles()){
            if ( file.isFile() ){
                sum+= file.length();
            }
        }
        return sum;
    }


    public static long daysBetweenTwoDates(Date dateFrom, Date dateTo) {
        return DAYS.between(Instant.ofEpochMilli(dateFrom.getTime()), Instant.ofEpochMilli(dateTo.getTime()));
    }
}
