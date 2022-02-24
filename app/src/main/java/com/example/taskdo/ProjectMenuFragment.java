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


public class ProjectMenuFragment extends Fragment {

    Button loguot;
    TextView user;
    OnLoginFormActivityListener loginFormActivityListener;
    public ProjectMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project_menu, container, false);

        user = view.findViewById(R.id.user_info);
        user.setText(MainActivity.prefConfig.readName());
        loguot = view.findViewById(R.id.loguot);
        loguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.prefConfig.writeLoginStatus(false);
                loginFormActivityListener.performLogOut();
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