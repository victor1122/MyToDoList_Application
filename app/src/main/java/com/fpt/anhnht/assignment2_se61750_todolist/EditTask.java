package com.fpt.anhnht.assignment2_se61750_todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Locale;

import TaskObject.DatabaseManage;
import TaskObject.Task;

public class EditTask extends AppCompatActivity {

    EditText fromDate, toDate, header, content, percent, fromTime, toTime;
    Calendar myCalendar, fromCal, toCal;
    DatePickerDialog fromDatePicker, toDatePicker;
    TimePickerDialog fromTimePicker, toTimePicker;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    SeekBar seek;
    Button btn;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_edit_task);
        initData();
        setDateView();
        setAction();

    }


    /**
     * Set data, reference to all widget
     */
    public void initData() {
        //Parameters
        String stringTask = getIntent().getExtras().getString("TaskObject");
        task = new Gson().fromJson(stringTask, Task.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        //Set calendar
        myCalendar = Calendar.getInstance();
        fromCal = Calendar.getInstance();
        toCal = Calendar.getInstance();
        //Set from date
        fromDate = (EditText) findViewById(R.id.editFromDate);
        fromCal.setTimeInMillis(task.getFromDate());
        fromDate.setText(sdf.format(fromCal.getTime()));
        //Set to date
        toDate = (EditText) findViewById(R.id.editToDate);
        toCal.setTimeInMillis(task.getToDate());
        toDate.setText(sdf.format(toCal.getTime()));
        //set header
        header = (EditText) findViewById(R.id.editHeader);
        header.setText(task.getHeader());
        //Set content
        content = (EditText) findViewById(R.id.editContent);
        content.setText(task.getContent());
        //Set percent complete
        percent = (EditText) findViewById(R.id.editPercentCom);
        percent.setText(String.valueOf(task.getPercentComplete()));
        //Set seek bar
        seek = (SeekBar) findViewById(R.id.editSeek);
        seek.setProgress(Integer.parseInt(percent.getText().toString()), true);
        btn = (Button) findViewById(R.id.editButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        };
        findViewById(R.id.editTask).setOnClickListener(listener);
        fromTime = (EditText) findViewById(R.id.editfromTime);
        fromTime.setText(fromCal.get(Calendar.HOUR_OF_DAY) + "h:" + fromCal.get(Calendar.MINUTE));
        toTime = (EditText) findViewById(R.id.editToTime);
        toTime.setText(toCal.get(Calendar.HOUR_OF_DAY) + "h:" + toCal.get(Calendar.MINUTE));
    }

    /**
     * Set view for date picker
     */
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
                fromTime.setText(hourOfDay + "h:" + minute + "m");
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
        //boolean is 24h format or not

        toTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toCal.set(Calendar.HOUR, hourOfDay);
                toCal.set(Calendar.MINUTE, minute);
                toTime.setText(hourOfDay + "h:" + minute + "m");
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
    }

    /**
     * Set on click for update button, date picker
     */
    public void onClick(View view) {
        if (view == fromDate) {
            fromDatePicker.show();
        } else if (view == toDate) {
            toDatePicker.show();
        } else if (view == toTime) {
            toTimePicker.show();
        } else if (view == fromTime) {
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
            } else if (fromCal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                fromDate.setError("");
                fromDate.setText("");
                Toast.makeText(this, "From date must after current date " + sdf.format(Calendar.getInstance().getTime()), Toast.LENGTH_LONG).show();
                return;
            } else if (toCal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                toDate.setError("");
                toDate.setText("");
                Toast.makeText(this, "To date must after current date " + sdf.format(Calendar.getInstance().getTime()), Toast.LENGTH_LONG).show();
                return;
            }

            Bundle bundle = new Bundle();
            Task newTask = new Task(task.getId(),
                    header.getText().toString(),
                    fromCal.getTimeInMillis(),
                    toCal.getTimeInMillis(),
                    content.getText().toString(),
                    Integer.parseInt(percent.getText().toString()));
            DatabaseManage db = new DatabaseManage(this);
            db.updateTask(newTask);

            db.close();
            bundle.putString("nameActivity", "Edit Task");

            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Set action for widget
     */
    private void setAction() {
        //Text change when seek bar change
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int percentage, boolean b) {
                percent.setText(String.valueOf(percentage));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Seek bar change when text change
        percent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                percent.setSelection(percent.getText().length());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int num;
                try {
                    num = Integer.parseInt(charSequence.toString());
                } catch (NumberFormatException e) {
                    return;
                }
                if (num > 0 || num <= 100) {
                    seek.setProgress(num, true);
                }
                if (num < 0 || num > 100) {
                    percent.setError("Please input number from 0 to 100 only");
                    percent.setText("0");
                    return;
                }
                percent.setSelection(percent.getText().length());
            }

            @Override
            public void afterTextChanged(Editable e) {
                percent.setSelection(percent.getText().length());
            }
        });
    }


}

