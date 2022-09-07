package com.project.projectpars.models;

public class FIles {
    private int id;
    private String name;
    private String file_path;
    private String memory;
    private String date;
    private String left;
    public FIles() {
    }

    public FIles(int id, String name, String file_path, String memory, String date, String left) {
        this.id = id;
        this.name = name;
        this.file_path = file_path;
        this.memory = memory;
        this.date = date;
        this.left = left;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FIles{" +
                "name='" + name + '\'' +
                ", file_path='" + file_path + '\'' +
                ", memory='" + memory + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }
}