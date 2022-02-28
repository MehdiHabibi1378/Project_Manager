package com.example.taskdo.Listener;

import com.example.taskdo.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("register.php")
    Call<User> preformRegistration(@Query("name") String Name, @Query("email") String Email, @Query("user_name") String UserName, @Query("user_password") String UserPassword);

    @GET("login.php")
    Call<User> performUserLogin(@Query("user_name") String UserName,@Query("user_password") String UserPassword);

    @GET("make_project.php")
    Call<User> performAddProject(@Query("name") String name,@Query("manager") String manager,@Query("user_name") String username);

    @GET("project_list.php")
    Call<User> project_list(@Query("user_name") String username);

    @GET("task.php")
    Call<User> performAddTask(@Query("name") String name,@Query("project_name")String project_name,
                              @Query("project_manager") String manager,@Query("create_date")String date);
    @GET("update_status.php")
    Call<User> performUpdateStatus(@Query("name") String name,@Query("project_name")String project_name,
    @Query("project_manager") String manager,@Query("status") String status);

    @GET("update_doby.php")
    Call<User> performUpdateDoby(@Query("name") String name,@Query("project_name")String project_name,
                                   @Query("project_manager") String manager,@Query("do_by") String doby);

    @GET("list_of_users.php")
    Call<User> performMembers(@Query("name")String name , @Query("manager") String manager);
    @GET("remove_user.php")
    Call<User> performRemoveUser(@Query("name")String name , @Query("manager") String manager,
                                 @Query("user_name")String user);

    @GET("setTime.php")
    Call<User> performUpdateTime(@Query("name") String name,@Query("project_name")String project_name,
                                   @Query("project_manager") String manager,@Query("time") String time);

    @GET("delete_task.php")
    Call<User> performDeleteTask(@Query("name") String name,@Query("project_name")String project_name,
                                 @Query("project_manager") String manager);

    @GET("delete_project.php")
    Call<User> performDeleteProject(@Query("name") String name, @Query("manager") String manager);

    @GET("request.php")
    Call<User> performRequest(@Query("name") String name,@Query("manager") String manager,@Query("user_name") String username);

    @GET("delete_request.php")
    Call<User> performDeleteRequest(@Query("name") String name,@Query("manager") String manager,@Query("user_name") String username);
}
