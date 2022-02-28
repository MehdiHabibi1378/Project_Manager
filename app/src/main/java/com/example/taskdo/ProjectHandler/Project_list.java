package com.example.taskdo.ProjectHandler;

public class Project_list {

    private String name;
    private String manager;

    public Project_list(String name, String manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }
}
