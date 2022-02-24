package com.example.taskdo;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class test extends Fragment implements DatePickerDialog.OnDateSetListener {

    TextView deadline;
    public test() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.task_info, container, false);
        deadline = view.findViewById(R.id.info_time);
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });

        return view;
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
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR,year);
//        calendar.set(Calendar.MONTH,month);
//        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

    }

    private void showDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}


