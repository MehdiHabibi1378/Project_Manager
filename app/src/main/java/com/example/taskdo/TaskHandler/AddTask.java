package com.example.taskdo.TaskHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.taskdo.Listener.OnLoginFormActivityListener;
import com.example.taskdo.MainActivity;
import com.example.taskdo.R;
import com.example.taskdo.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTask extends Fragment {

    OnLoginFormActivityListener loginFormActivityListener;
    EditText name;
    ImageView addTask;

    public AddTask() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_project, container, false);
        name = view.findViewById(R.id.add_name);
        addTask = view.findViewById(R.id.ok);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                loginFormActivityListener.performTask();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

    private void addNewTask (){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String Name = name.getText().toString();
        String project_name = MainActivity.prefConfig.readProjectName();
        String project_manager = MainActivity.prefConfig.readProjectManager();
        if (Name!=null) {
            Call<User> call = MainActivity.apiInterface.performAddTask(Name, project_name, project_manager, date);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body().getResponse().equals("ok")) {
                        loginFormActivityListener.performTask();
                        MainActivity.prefConfig.displayToast("Task Add Sec...");
                    } else if (response.body().getResponse().equals("exist")) {
                        MainActivity.prefConfig.displayToast("has now exist");
                    } else if (response.body().getResponse().equals("error")) {
                        MainActivity.prefConfig.displayToast("try again");
                    } else {
                        MainActivity.prefConfig.displayToast("something went wrung");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }else
            MainActivity.prefConfig.displayToast("please chose a name");

    }

    // back
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            MainActivity.prefConfig.writeProjectName(null);
            loginFormActivityListener.performTask();
        }
    };
}