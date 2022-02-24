package com.example.taskdo;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("response")
    private String Response;

    @SerializedName("name")
    private String Name;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("status")
    private String status;

    public String getStatus(){return status;}

    public String getResponse() {
        return Response;
    }

    public String getName() {
        return Name;
    }

    public String getUser_name() {
        return user_name;
    }
}
