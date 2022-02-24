package com.example.taskdo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements OnLoginFormActivityListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefConfig = new PrefConfig(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        if (findViewById(R.id.fragment_container)!=null){
            if (savedInstanceState !=null){
                return;
            }
            if (prefConfig.readLoginStatus()){
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new Project()).commit();
                //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new test()).commit();
            }
            else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new Login()).commit();
            }
        }
    }

    @Override
    public void performRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Register()).addToBackStack(null).commit();

    }

    @Override
    public void performLogin(String name) {
        prefConfig.writeName(name);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Project()).commit();
    }

    @Override
    public void performLogOut() {
        prefConfig.writeName(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Login()).commit();
    }

    @Override
    public void performAddProject() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddProject()).commit();
    }

    @Override
    public void performProject() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Project()).commit();
    }

    @Override
    public void performAddUser() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddUser()).commit();
    }

    @Override
    public void performTask() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Task()).commit();
    }

    @Override
    public void performAddTask() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new AddTask()).commit();
    }

    @Override
    public void performTaskInfo() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TaskInfo()).commit();
    }

    @Override
    public void performProjectManage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Project_Manage()).commit();
    }

    @Override
    public void performAssign() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AssignPerson()).commit();
    }

}