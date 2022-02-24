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
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {

    private TextView RegText;
    private EditText UserName,UserPassword;
    private Button LoginBn;
    OnLoginFormActivityListener loginFormActivityListener;
    Connection connection ;

//    public interface OnLoginFormActivityListener{
//
//        public void performRegister();
//        public void performLogin(String name);
//
//    }

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        RegText = view.findViewById(R.id.register);
        UserName = view.findViewById(R.id.assign_to);
        UserPassword = view.findViewById(R.id.pass);
        LoginBn = view.findViewById(R.id.add_task);
        LoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogin();
            }
        });

        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginFormActivityListener.performRegister();
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

    private void performLogin (){
        String username = UserName.getText().toString();
        String password = UserPassword.getText().toString();

        Call<User> call = MainActivity.apiInterface.performUserLogin(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println(response.body().getName());
                if (response.body().getResponse().equals("ok")) {
                    MainActivity.prefConfig.writeLoginStatus(true);
                    loginFormActivityListener.performLogin(username);
                } else if (response.body().getResponse().equals("failed")){
                    MainActivity.prefConfig.displayToast("Login Faild... palease try again");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        UserName.setText("");
        UserPassword.setText("");
    }


}