package com.example.taskdo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class PrefConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file),Context.MODE_PRIVATE);
    }
//login
    public void writeLoginStatus(boolean status){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status),status);
        editor.commit();
    }

    public boolean readLoginStatus(){

        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status),false);

    }
//username
    public void writeName(String name){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name),name);
        editor.commit();

    }


    public String readName(){
        return sharedPreferences.getString(context.getString(R.string.pref_user_name),"User");
    }

//project name
    public void writeProjectName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("com.abc.preference_project_name",name);
        editor.commit();
    }

    public String readProjectName(){
        return sharedPreferences.getString("com.abc.preference_project_name",null);
    }

//manager
    public void writeProjectManager(String name){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_project_manager),name);
        editor.commit();

    }

    public String readProjectManager(){
        return sharedPreferences.getString(context.getString(R.string.pref_project_manager),null);
    }

//task name
    public String readTaskName(){
        return sharedPreferences.getString("com.abc.preference_task_name",null);
    }

    public void writeTaskName(String name){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_Task_name),name);
        editor.commit();

    }


// toast display
    public void displayToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

}
