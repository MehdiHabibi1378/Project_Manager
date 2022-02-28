package com.example.taskdo.ProjectHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskdo.GetData;
import com.example.taskdo.Listener.OnItemSelectedListener;
import com.example.taskdo.Listener.OnLoginFormActivityListener;
import com.example.taskdo.MainActivity;
import com.example.taskdo.R;
import com.example.taskdo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Project_Manage extends Fragment implements OnItemSelectedListener {


    TextView name,manager;
    ProjectManageAdapter adapter;
    GridView user_grid;
    ImageView deleteProject;
    ArrayList<String> users = new ArrayList<>();
    public OnLoginFormActivityListener loginFormActivityListener;
    public Project_Manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_project__manage, container, false);
        name = view.findViewById(R.id.PM_name);
        deleteProject = view.findViewById(R.id.delete_project);
        manager = view.findViewById(R.id.PM_manager);
        user_grid = view.findViewById(R.id.PM_user_grid);
        name.setText("Name :"+ MainActivity.prefConfig.readProjectName());
        manager.setText("Manager :"+MainActivity.prefConfig.readProjectManager());
        updateMembers();
        adapter = new ProjectManageAdapter(users,this);
        user_grid.setAdapter(adapter);
        user_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.prefConfig.displayToast("test");


            }
        });

        deleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure to delete "+MainActivity.prefConfig.readProjectName());
                builder.setCancelable(true);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProject();
                    }
                });

                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        user_grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure to Delete");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user_name = users.get(position);
                        removeUser(user_name);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        // back
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                loginFormActivityListener.performTask();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

        return view;
    }


    private void updateMembers(){

        String url = "list_of_users.php?name="+MainActivity.prefConfig.readProjectName()
                +"&manager="+MainActivity.prefConfig.readProjectManager();
        GetData getdata = new GetData(url);
        getdata.execute();

        try{

            String res = (String) getdata.get();
            JSONObject responsJson = new JSONObject(res);
            String raw = responsJson.getString("response");
            if (responsJson.getString("status").equals("ok")) {
                JSONArray jsonArray = new JSONArray(raw);
                for (int i = 0; i < jsonArray.length(); i++) {
                    raw = jsonArray.getString(i);
                    responsJson = new JSONObject(raw);
                    String username;
                    username = responsJson.getString("username");
                    if (!username.equals(MainActivity.prefConfig.readProjectManager())){
                        users.add(username);
                    }

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Call<User> call = MainActivity.apiInterface.performMembers(project_name,manager);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.body().getStatus().equals("ok")){
//                    String raw = response.body().getResponse();
//                    JSONArray jsonArray = null;
//                    try {
//                        jsonArray = new JSONArray(raw);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            raw = jsonArray.getString(i);
//                            JSONObject responsJson = new JSONObject(raw);
//                            String username;
//                            username = responsJson.getString("username");
//                            project_members.add(username);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //responsJson = new JSONObject((Map) jsonArray);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

    private void deleteProject(){
        String name = MainActivity.prefConfig.readProjectName();
        String manager = MainActivity.prefConfig.readProjectManager();
        Call<User> call = MainActivity.apiInterface.performDeleteProject(name,manager);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getStatus().equals("ok")){
                    loginFormActivityListener.performProject();
                }else {
                    MainActivity.prefConfig.displayToast("delete failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @Override
    public void onItemSelected(String user_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to Delete");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeUser(user_name);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void removeUser(String user){
        String project_name=MainActivity.prefConfig.readProjectName();
        String project_manager = MainActivity.prefConfig.readProjectManager();
        if (!project_manager.equals(user)){
            Call<User> call = MainActivity.apiInterface.performRemoveUser(project_name,project_manager,user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body().getResponse().equals("ok")){
                        users.remove(user);
                        user_grid.setAdapter(adapter);
                    }else {
                        MainActivity.prefConfig.displayToast("failed");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }else
            MainActivity.prefConfig.displayToast("You are manager");

    }


}

class ProjectManageAdapter extends BaseAdapter {


    OnItemSelectedListener listener;
    ArrayList<String> user;
    TextView userText,removeUser;

    public ProjectManageAdapter(ArrayList<String> task_lists,OnItemSelectedListener listener) {
        this.listener = listener;
        this.user = task_lists;
    }


    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_manage_style,parent,false);
            userText = view.findViewById(R.id.userText);
            removeUser = view.findViewById(R.id.remove_user);
            userText.setText(user.get(position));
            removeUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemSelected(user.get(position));
                }
            });
        }
        return view;
    }



}