package com.example.taskdo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignPerson extends Fragment {

    ImageView add;
    TextView assign_name;
    GridView assign_grid;
    String name,manager,project_name,assign=null;
    ArrayList<String> project_members = new ArrayList<>();
    OnLoginFormActivityListener loginFormActivityListener;
    public AssignPerson() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_person, container, false);
        name = MainActivity.prefConfig.readTaskName();
        manager = MainActivity.prefConfig.readProjectManager();
        project_name = MainActivity.prefConfig.readProjectName();

        add = view.findViewById(R.id.assign);
        assign_name = view.findViewById(R.id.assign_to);
        assign_grid = view.findViewById(R.id.assign_grid);
        updateMembers();
        MemberAdapter adapter = new MemberAdapter(project_members);
        assign_grid.setAdapter(adapter);
        assign_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                assign_name.setText(project_members.get(position));
                assign=project_members.get(position);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assign!=null){
                    updateMembers(assign);
                }
            }
        });

        return view;
    }

    //update members
    private void updateMembers(){

        String url = "list_of_users.php?name="+project_name
                +"&manager="+manager;
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
                    project_members.add(username);
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
    //update doby
    private void updateMembers(String doby){
        String do_by = doby;
        Call<User> call = MainActivity.apiInterface.performUpdateDoby(name,project_name,manager,do_by);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")) {
                    loginFormActivityListener.performTaskInfo();

                }else if (response.body().getResponse().equals("failed")){
                    MainActivity.prefConfig.displayToast("failed");
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

    // back
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            loginFormActivityListener.performTaskInfo();
        }
    };
}

class MemberAdapter extends BaseAdapter {


    ArrayList<String> member_lists ;
    TextView member;

    public MemberAdapter(ArrayList<String> list){
        this.member_lists = list;

    }

    @Override
    public int getCount() {
        return member_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return member_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_grid,parent,false);
            member = view.findViewById(R.id.memberText);
            member.setText(member_lists.get(position));
        }
        return view;
    }


}