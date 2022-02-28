package com.example.taskdo.ProjectHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.taskdo.GetData;
import com.example.taskdo.Listener.OnLoginFormActivityListener;
import com.example.taskdo.MainActivity;
import com.example.taskdo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Project extends Fragment {

    ArrayList<Project_list> listOfProject = new ArrayList<>();
    OnLoginFormActivityListener loginFormActivityListener;
    //TextView UserInfo ;
    Button add;
    ImageView setting,message;
    GridView project_gride;
    DrawerLayout drawerLayout ;
    public Project() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.fragment_project, container, false);

        String url = "project_list.php?user_name="+ MainActivity.prefConfig.readName();
        GetData getdata = new GetData(url);
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
                    String name, manager;
                    name = responsJson.getString("name");
                    manager = responsJson.getString("manager");
                    listOfProject.add(new Project_list(name, manager));
                }
                for (Project_list list:listOfProject
                ) {
                    System.out.println(list.getName());
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        project_gride = view.findViewById(R.id.gride_task);
        if (listOfProject.size()>0) {
            ProjectAdapter projectAdapter = new ProjectAdapter(listOfProject);
            project_gride.setAdapter(projectAdapter);
        }




        //UserInfo = view.findViewById(R.id.userText);
        setting = view.findViewById(R.id.setting_task);
        message = view.findViewById(R.id.message);
        if (setVisibility()){
            message.setVisibility(View.VISIBLE);
        }
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFormActivityListener.performMessage();
            }
        });
        add = view.findViewById(R.id.add_task);
        //UserInfo.setText(MainActivity.prefConfig.readName());
        drawerLayout =(DrawerLayout) view.findViewById(R.id.draTest);

         setting.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 drawerLayout.openDrawer(GravityCompat.END);
                // showMenu(v);
             }
         });

         add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 loginFormActivityListener.performAddProject();
             }
         });

         project_gride.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 MainActivity.prefConfig.writeProjectName(listOfProject.get(position).getName());
                 MainActivity.prefConfig.writeProjectManager(listOfProject.get(position).getManager());
                 loginFormActivityListener.performTask();
             }
         });



         return view;
    }

    private boolean setVisibility(){
        ArrayList<Project_list> list = new ArrayList<>();
        String url = "request_list.php?user_name="+MainActivity.prefConfig.readName();
        GetData getdata = new GetData(url);
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
                    String name, manager;
                    name = responsJson.getString("name");
                    manager = responsJson.getString("manager");
                    list.add(new Project_list(name, manager));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (list.size()>0)
            return true;
        return false;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

//    private void showMenu(View view){
//        PopupMenu popupMenu = new PopupMenu(this.getContext(),view);
//        popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId()== R.id.logUot){
//                    MainActivity.prefConfig.writeLoginStatus(false);
//                    loginFormActivityListener.performLogOut();
//                }
//                return true;
//            }
//        });
//
//        popupMenu.show();
//    }


}

class ProjectAdapter extends BaseAdapter {


    ArrayList<Project_list> project_lists ;
    TextView text,manager,grow;

    public ProjectAdapter(ArrayList<Project_list> list){
        this.project_lists = list;

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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gride_style,parent,false);


            grow = view.findViewById(R.id.grows);
            float grows =grows(project_lists.get(position).getName(),project_lists.get(position).getManager());
            String gr = String.format("%.1f",grows);
            grow.setText(gr+"%");
            text = view.findViewById(R.id.userText);
            text.setText(project_lists.get(position).getName());
            manager = view.findViewById(R.id.project_manager);
            manager.setText("Manager :"+project_lists.get(position).getManager());

        }
        return view;
    }

    private float grows(String projectName,String projectManager){

        float grow = 0;
        String manager = projectManager;
        String project_name = projectName;
        String url = "task_list.php?project_name="+project_name
                +"&project_manager="+manager;
        GetData getdata = new GetData(url);
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
                    status = responsJson.getString("status");
                    int s= Integer.parseInt(status);
                    grow+=s;

                }
                grow = (grow/ jsonArray.length())*100;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return grow;
    }
    

}