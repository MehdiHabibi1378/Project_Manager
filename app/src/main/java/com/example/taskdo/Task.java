package com.example.taskdo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Task extends Fragment {


    ArrayList<Task_list> listOfTask = new ArrayList<>();
    OnLoginFormActivityListener loginFormActivityListener;
    DrawerLayout drawerTask;
    Button add_task;
    ImageView task_setting;
    GridView task_gride;

    public Task() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task, container, false);
        add_task = view.findViewById(R.id.add_task);
        task_setting = view.findViewById(R.id.setting_task);
        task_gride = view.findViewById(R.id.gride_task);
        drawerTask = view.findViewById(R.id.draTask);
        if (MainActivity.prefConfig.readName().equals(MainActivity.prefConfig.readProjectManager())){
            task_setting.setVisibility(View.VISIBLE);
        }
//get info
        String manager = MainActivity.prefConfig.readProjectManager();
        String project_name = MainActivity.prefConfig.readProjectName();
        String url = "http://192.168.1.6//testing/task_list.php?project_name="+project_name
                +"&project_manager="+manager;
        getData_Task getdata = new getData_Task(url);
        getdata.execute();

        try{

            String res = (String) getdata.get();
            JSONObject responsJson = new JSONObject(res);
            String raw = responsJson.getString("response");
            System.out.println(raw);
            if (responsJson.getString("status").equals("ok")) {

                JSONArray jsonArray = new JSONArray(raw);
                //responsJson = new JSONObject((Map) jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    raw = jsonArray.getString(i);
                    responsJson = new JSONObject(raw);
                    String name,do_by,create_date,time,status;
                    name = responsJson.getString("name");
                    do_by = responsJson.getString("do_by");
                    create_date = responsJson.getString("create_time");
                    time = responsJson.getString("time");
                    status = responsJson.getString("status");
                    int s= Integer.parseInt(status);
                    listOfTask.add(new Task_list(name,project_name,manager,do_by,create_date,time,s));
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (listOfTask.size()>0){
            TaskAdapter taskAdapter = new TaskAdapter(listOfTask);
            task_gride.setAdapter(taskAdapter);
        }


//setting
        task_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerTask.openDrawer(GravityCompat.END);
                //showMenu(v);
            }
        });
//add task
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.prefConfig.writeProjectName(project_name);
                loginFormActivityListener.performAddTask();
            }
        });
// back
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
              MainActivity.prefConfig.writeProjectName(null);
              MainActivity.prefConfig.writeProjectManager(null);
              loginFormActivityListener.performProject();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
//grid on Item
        task_gride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.prefConfig.writeTaskName(listOfTask.get(position).getName());
                loginFormActivityListener.performTaskInfo();
            }
        });


        return view;
    }

//menu fun
    private void showMenu(View view){
        PopupMenu popupMenu = new PopupMenu(this.getContext(),view);
        popupMenu.getMenuInflater().inflate(R.menu.project_list_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()== R.id.delete_project){
                    if (MainActivity.prefConfig.readName().equals(MainActivity.prefConfig.readProjectManager())) {
                        loginFormActivityListener.performProjectManage();
                    }else {
                        MainActivity.prefConfig.displayToast("just manager can Enter");
                    }
                }else if (item.getItemId() == R.id.add_new_user){
                    loginFormActivityListener.performAddUser();
                }

                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }
}


class getData_Task extends AsyncTask {
    String url ;
    public getData_Task(String url){
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String res="none";
        try {
            URL u = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line ;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            res = sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

class TaskAdapter extends BaseAdapter {


    ArrayList<Task_list> task_lists ;
    TextView text,assign,time;

    public TaskAdapter(ArrayList<Task_list> list){
        this.task_lists = list;

    }

    @Override
    public int getCount() {
        return task_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return task_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_gride,parent,false);


            text = view.findViewById(R.id.task_text);
            text.setText(task_lists.get(position).getName());
            assign = view.findViewById(R.id.task_doby);
            time = view.findViewById(R.id.task_timeLeft);

            if (task_lists.get(position).getDo_by().equals("null") || task_lists.get(position).getDo_by()==null ){
                assign.setText("no one");
            }else{
                assign.setText("Assign to:"+task_lists.get(position).getDo_by());
            }
            Task_list task = task_lists.get(position);

            if (task.getFinish_date().equals("0000-00-00") || task.getFinish_date().equals("null") ){
                time.setText("time is not set");
            }else{
                int t =timeLeft(task);
                time.setText("Time left="+String.valueOf(t));
            }

        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int timeLeft(Task_list task){
        String date = task.getCreate_date();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(date,dtf);
        date = task.getFinish_date();
        LocalDate date2 = LocalDate.parse(date,dtf);
        long daysBetween = ChronoUnit.DAYS.between(date1,date2);

        int time_left = (int) (daysBetween);
        return time_left;

    }


}