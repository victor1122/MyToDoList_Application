package com.fpt.anhnht.assignment2_se61750_todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;

import TaskObject.DatabaseManage;
import TaskObject.Task;

public class AddTask extends AppCompatActivity {

    private EditText fromDate, toDate, header, content, toTime, fromTime;
    private Calendar myCalendar, fromCal, toCal;
    private DatePickerDialog fromDatePicker, toDatePicker;
    private TimePickerDialog fromTimePicker, toTimePicker;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_add_alarm);
        initData();
        setDateView();
        header.requestFocus();
    }

    public void initData() {
        myCalendar = Calendar.getInstance();
        fromCal = Calendar.getInstance();
        toCal = Calendar.getInstance();
        fromDate = (EditText) findViewById(R.id.fromDate);
        fromDate.setText(sdf.format(fromCal.getTime()));
        toDate = (EditText) findViewById(R.id.toDate);
        toDate.setText(sdf.format(toCal.getTime()));
        header = (EditText) findViewById(R.id.headerRemind);
        content = (EditText) findViewById(R.id.contentRemind);
        btn = (Button) findViewById(R.id.addRemind);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };
        findViewById(R.id.addTaskView).setOnClickListener(listener);
        fromTime = (EditText)findViewById(R.id.fromTime);
        fromTime.setText(fromCal.get(Calendar.HOUR_OF_DAY)+"h:"+fromCal.get(Calendar.MINUTE));
        toTime = (EditText)findViewById(R.id.toTime);
        toTime.setText(toCal.get(Calendar.HOUR_OF_DAY)+"h:"+toCal.get(Calendar.MINUTE));
    }

    public void setDateView() {

        fromDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                fromCal.set(year, month, day);
                fromDate.setText(sdf.format(fromCal.getTime()));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                toCal.set(year, month, day);
                toDate.setText(sdf.format(toCal.getTime()));
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        fromTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fromCal.set(Calendar.HOUR, hourOfDay);
                fromCal.set(Calendar.MINUTE, minute);
                fromTime.setText(hourOfDay+"h:"+minute+"m");
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true);
        //boolean is 24h format or not

        toTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toCal.set(Calendar.HOUR, hourOfDay);
                toCal.set(Calendar.MINUTE, minute);
                toTime.setText(hourOfDay+"h:"+minute+"m");
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true);
    }

    public void onClick(View view) {
        if (view == fromDate) {
            fromDatePicker.show();
        } else if (view == toDate) {
            toDatePicker.show();
        }else if (view == toTime) {
            toTimePicker.show();
        }else if (view == fromTime) {
            fromTimePicker.show();
        } else if (view == btn) {
            if (header.getText().toString().isEmpty()) {
                header.setError("This field can not be empty");
                header.requestFocus();
                return;
            } else if (fromCal.getTimeInMillis() > toCal.getTimeInMillis()) {
                fromDate.setError("");
                toDate.setError("");
                fromDate.setText("");
                toDate.setText("");
                Toast.makeText(this, "From date must before to date", Toast.LENGTH_LONG).show();
                return;
            } else if(fromCal.getTimeInMillis()<Calendar.getInstance().getTimeInMillis()){
                fromDate.setError("");
                fromDate.setText("");
                Toast.makeText(this, "From date must after current date "+fromCal.getTimeInMillis(), Toast.LENGTH_LONG).show();
                return;
            } else if(toCal.getTimeInMillis()<Calendar.getInstance().getTimeInMillis()){
                toDate.setError("");
                toDate.setText("");
                Toast.makeText(this, "To date must after current date "+sdf.format(Calendar.getInstance().getTime()), Toast.LENGTH_LONG).show();
                return;
            }

            Bundle bundle = new Bundle();
            Task task = new Task(header.getText().toString(),
                    fromCal.getTimeInMillis(),
                    toCal.getTimeInMillis(),
                    content.getText().toString(), 0);
            DatabaseManage db = new DatabaseManage(this);
            db.addTask(task);
            bundle.putString("nameActivity", "Add Task");

            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }


}
