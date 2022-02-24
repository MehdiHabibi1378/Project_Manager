package com.example.taskdo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskInfo extends Fragment implements DatePickerDialog.OnDateSetListener {


    String name,manager,project_name;
    Task_list task;
    TextView t_name,t_doby,t_time,t_status,setStatus,setDeadline,assign;
    ImageView delete;
    OnLoginFormActivityListener loginFormActivityListener;
    public TaskInfo() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.task_info, container, false);
        t_name = view.findViewById(R.id.info_name);
        t_doby = view.findViewById(R.id.info_doby_name);
        t_time = view.findViewById(R.id.info_deadline_name);
        t_status = view.findViewById(R.id.info_status_name);
        setStatus = view.findViewById(R.id.info_status);
        setDeadline = view.findViewById(R.id.info_time);
        assign = view.findViewById(R.id.info_doby);
        delete = view.findViewById(R.id.delete_task);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure to Delete");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask();
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
        });
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFormActivityListener.performAssign();
            }
        });

        setDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


//get info
        name = MainActivity.prefConfig.readTaskName();
        manager = MainActivity.prefConfig.readProjectManager();
        project_name = MainActivity.prefConfig.readProjectName();
        String url = "http://192.168.1.6//testing/task_info.php?name="+name+"&project_name="+project_name
                +"&project_manager="+manager;
        getData_task_info getdata = new getData_task_info(url);
        getdata.execute();

        try{

            String res = (String) getdata.get();
            JSONObject responsJson = new JSONObject(res);
            if (responsJson.getString("response").equals("ok")) {

                    String do_by,create_date,time,status;
                    do_by = responsJson.getString("do_by");
                    create_date = responsJson.getString("date");
                    time = responsJson.getString("time");
                    status = responsJson.getString("status");
                    int s= Integer.parseInt(status);
                    System.out.println(time);
                    task = new Task_list(name,project_name,manager,do_by,create_date,time,s);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateTextView();




//status button
        setStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

 // back
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity.prefConfig.writeTaskName(null);
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


//menu Status
    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(this.getContext(),view);
        popupMenu.getMenuInflater().inflate(R.menu.status_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()== R.id.done){
                    updateStatus("1");
                }else if (item.getItemId()==R.id.notdone){
                    updateStatus("0");
                }
                return true;
            }
        });

        popupMenu.show();
    }





//update Status
    private void updateStatus(String status){

        String st = status;
        Call<User> call = MainActivity.apiInterface.performUpdateStatus(name,project_name,manager,st);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    task.setStatus(Integer.parseInt(st));
                    updateTextView();
                }else if (response.body().getResponse().equals("failed")){
                    MainActivity.prefConfig.displayToast("failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void deleteTask(){
        Call<User> call = MainActivity.apiInterface.performDeleteTask(name,project_name,manager);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getStatus().equals("ok")){
                    MainActivity.prefConfig.writeTaskName(null);
                    loginFormActivityListener.performTask();
                }else{
                    MainActivity.prefConfig.displayToast("failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }



 //update textView
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTextView(){

        if (task.getFinish_date().equals("0000-00-00") || task.getFinish_date().equals("null") ){
            t_time.setText("not");
        }else{
            int t =timeLeft();
            t_time.setText(String.valueOf(t));
        }
        t_name.setText(task.getName());
        if (task.getStatus()==1)
            t_status.setText("done");
        else
            t_status.setText("notDone");
        if (task.getDo_by()==null)
            t_doby.setText("no body");
        else
            t_doby.setText(task.getDo_by());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int timeLeft(){
        String date = task.getCreate_date();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(date,dtf);
        date = task.getFinish_date();
        LocalDate date2 = LocalDate.parse(date,dtf);
        long daysBetween = ChronoUnit.DAYS.between(date1,date2);

        int time_left = (int) (daysBetween);
        return time_left;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        String d = String.valueOf(dayOfMonth);
        String dat;
        if (month<10&& dayOfMonth<10){
            dat = y + "-0" + m + "-0" + d;
        }else if (dayOfMonth<10 && month>=10 ){
            dat = y + "-" + m + "-0" + d;
        }else if (month<10 && dayOfMonth>=10){
            dat = y + "-0" + m + "-" + d;
        }else {
            dat = y + "-" + m + "-" + d;
        }
        updateTime(dat);
    }
    private void showDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //update time
    private void updateTime(String time){

        String st = time;
        Call<User> call = MainActivity.apiInterface.performUpdateTime(name,project_name,manager,st);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")){
                    task.setFinish_date(st);
                    updateTextView();
                }else if (response.body().getResponse().equals("failed")){
                    MainActivity.prefConfig.displayToast("failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}

class getData_task_info extends AsyncTask {
    String url ;
    public getData_task_info(String url){
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

