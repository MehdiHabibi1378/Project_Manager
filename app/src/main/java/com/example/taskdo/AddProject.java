package com.example.taskdo;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProject extends Fragment {


    OnLoginFormActivityListener loginFormActivityListener;
    EditText Name;
    ImageView add;
    public AddProject() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_project, container, false);
        Name = view.findViewById(R.id.add_name);
        add = view.findViewById(R.id.ok);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAdd();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                loginFormActivityListener.performProject();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK ){
//                    loginFormActivityListener.performProject();
//                    return true;
//                }
//                return false;
//            }
//        });


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
        String name = Name.getText().toString();

        Call<User> call = MainActivity.apiInterface.performAddProject(name,manager,manager);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    loginFormActivityListener.performProject();
                }else if (response.body().getResponse().equals("exist")){
                    MainActivity.prefConfig.displayToast("Project has exist now");
                }else if (response.body().getResponse().equals("error")){
                    MainActivity.prefConfig.displayToast("try again");
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
            loginFormActivityListener.performProject();
        }
    };
}