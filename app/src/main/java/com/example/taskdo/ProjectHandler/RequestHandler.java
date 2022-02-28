package com.example.taskdo.ProjectHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.taskdo.GetData;
import com.example.taskdo.Listener.MessageListener;
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

public class RequestHandler extends Fragment implements MessageListener {


    OnLoginFormActivityListener loginFormActivityListener;
    ArrayList<Project_list> projects = new ArrayList<>();
    GridView grid;
    public RequestHandler() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_handler, container, false);
        grid = view.findViewById(R.id.message_grid);
        setProjects();
        MessageAdapter adapter = new MessageAdapter(projects,this);
        grid.setAdapter(adapter);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                loginFormActivityListener.performProject();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
        return view;
    }

    private void setProjects(){
        String url = "request_list.php?user_name="+ MainActivity.prefConfig.readName();
        GetData getdata = new GetData(url);
        getdata.execute();

        try{

            String res = (String) getdata.get();
            JSONObject responsJson = new JSONObject(res);
            String raw = responsJson.getString("response");
            if (responsJson.getString("status").equals("ok")) {

                JSONArray jsonArray = new JSONArray(raw);
                //responsJson = new JSONObject((Map) jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    raw = jsonArray.getString(i);
                    responsJson = new JSONObject(raw);
                    String name, manager;
                    name = responsJson.getString("name");
                    manager = responsJson.getString("manager");
                    projects.add(new Project_list(name, manager));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(Project_list project) {
        performAdd(project);
    }

    @Override
    public void deny(Project_list project) {
        deleteMessage(project);
    }

    private void performAdd(Project_list project){

        Call<User> call = MainActivity.apiInterface.performAddProject(project.getName(), project.getManager(),
                MainActivity.prefConfig.readName());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    deleteMessage(project);
                }else if (response.body().getResponse().equals("error")){
                    MainActivity.prefConfig.displayToast("error");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void deleteMessage (Project_list project) {
        Call<User> call = MainActivity.apiInterface.performDeleteRequest(project.getName(), project.getManager(),
                MainActivity.prefConfig.readName());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getStatus().equals("ok")){
                    loginFormActivityListener.performProject();
                }else if (response.body().getStatus().equals("error")){
                    MainActivity.prefConfig.displayToast("error");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }
}

class MessageAdapter extends BaseAdapter {

    MessageListener listener;
    ArrayList<Project_list> project_lists ;
    TextView text,manager,accept,deny;

    public MessageAdapter(ArrayList<Project_list> list,MessageListener listener){
        this.project_lists = list;
        this.listener = listener;

    }

    @Override
    public int getCount() {
        return project_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return project_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_style,parent,false);

            text = view.findViewById(R.id.project);
            text.setText(project_lists.get(position).getName());
            manager = view.findViewById(R.id.manager);
            manager.setText("Manager :"+project_lists.get(position).getManager());
            accept = view.findViewById(R.id.accept);
            deny = view.findViewById(R.id.deny);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.accept(project_lists.get(position));
                }
            });

            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deny(project_lists.get(position));
                }
            });

        }
        return view;
    }
}