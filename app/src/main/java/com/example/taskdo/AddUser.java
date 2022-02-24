package com.example.taskdo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser extends Fragment {

    TextView username;
    Button add;
    OnLoginFormActivityListener loginFormActivityListener;

    public AddUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =  inflater.inflate(R.layout.fragment_add_project, container, false);
       username = view.findViewById(R.id.add_name);
       add = view.findViewById(R.id.ok);

       add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               performAdd();
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

    private void performAdd(){
        String manager = MainActivity.prefConfig.readName();
        String Username = username.getText().toString();
        String name = MainActivity.prefConfig.readProjectName();

        Call<User> call = MainActivity.apiInterface.performAddProject(name,manager,Username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    loginFormActivityListener.performProject();
                }else if (response.body().getResponse().equals("exist")){
                    MainActivity.prefConfig.displayToast("Project has exist now");
                }else if (response.body().getResponse().equals("error")){
                    MainActivity.prefConfig.displayToast("try again");
                }else if (response.body().getResponse().equals("user_not_found")){
                    MainActivity.prefConfig.displayToast("user_not_found");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    // back
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {

        }
    };
}