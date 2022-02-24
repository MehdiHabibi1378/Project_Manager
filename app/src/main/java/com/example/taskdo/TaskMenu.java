package com.example.taskdo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TaskMenu extends Fragment {


    OnLoginFormActivityListener loginFormActivityListener;
    TextView name;
    Button manage_pro,add_user;
    public TaskMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_task_menu, container, false);
       name = view.findViewById(R.id.pro_info);
       manage_pro = view.findViewById(R.id.project_manage);
       add_user = view.findViewById(R.id.add_new_user);
       name.setText(MainActivity.prefConfig.readTaskName());

       add_user.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loginFormActivityListener.performAddUser();
           }
       });

       manage_pro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loginFormActivityListener.performProjectManage();
           }
       });



       return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }
}