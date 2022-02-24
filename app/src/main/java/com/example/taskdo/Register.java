package com.example.taskdo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends Fragment {

    private EditText Name,Username,UserPassword,Email;
    private Button BnRegister;

    public Register() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_register, container, false);
        Name = view.findViewById(R.id.fullName);
        Email = view.findViewById(R.id.email);
        Username = view.findViewById(R.id.username);
        UserPassword = view.findViewById(R.id.password);
        BnRegister = view.findViewById(R.id.singUp);

        BnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });
        return view;
    }

    public void performRegistration(){
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String username = Username.getText().toString();
        String password = UserPassword.getText().toString();

        Call<User> call = MainActivity.apiInterface.preformRegistration(name,email,username,password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    MainActivity.prefConfig.displayToast("Registration seccess....");

                }else if (response.body().getResponse().equals("exist")){
                    MainActivity.prefConfig.displayToast("User already exist...");
                }else if (response.body().getResponse().equals("error")){
                    MainActivity.prefConfig.displayToast("Somting went wrong");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        Name.setText("");
        Email.setText("");
        Username.setText("");
        UserPassword.setText("");
    }


}