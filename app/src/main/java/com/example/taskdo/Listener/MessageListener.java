package com.example.taskdo.Listener;

import com.example.taskdo.ProjectHandler.Project_list;

public interface MessageListener {

    public void accept(Project_list project);
    public void deny(Project_list project);
}
