package com.example.taskdo;

public class Task_list {
    private String name,project_name,project_manager,do_by,create_date,finish_date;
    private int status;

    public Task_list(String name, String project_name, String project_manager,
                     String do_by, String create_date, String deadline, int status) {
        this.name = name;
        this.project_name = project_name;
        this.project_manager = project_manager;
        this.do_by = do_by;
        this.create_date = create_date;
        this.finish_date = deadline;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getProject_manager() {
        return project_manager;
    }

    public String getDo_by() {
        return do_by;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDo_by(String do_by) {
        this.do_by = do_by;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }
}
