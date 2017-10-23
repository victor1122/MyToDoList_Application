package com.fpt.anhnht.assignment2_se61750_todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import TaskObject.DatabaseManage;
import TaskObject.Task;
import TaskObject.TaskLayout;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private TaskLayout arrayAdap;
    private ArrayList<Task> headerArray;
    private ListView listView;
    public static DatabaseManage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.todoList);
        db = new DatabaseManage(this);
        updateView();
        listView.setAdapter(arrayAdap);
        listViewListener();
    }

    public void addAction(View v) {
        Intent intent = new Intent(this, AddTask.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Waiting for response from activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            String activity = bundle.getString("nameActivity");
            if (activity.equals("Add Task")) {
                updateView();
                arrayAdap.notifyDataSetChanged();
            }else if(activity.equals("Edit Task")){
                updateView();
                arrayAdap.notifyDataSetChanged();
            }

        }

    }

    public void listViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Task task = headerArray.get(pos);
                db.deleteTask(task.getId());
                headerArray.remove(pos);
                arrayAdap.notifyDataSetChanged();
                updateView();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                callEdit(headerArray.get(pos));
            }
        });
    }

    private void updateView() {
        headerArray = db.getAllTask();
        if (headerArray == null || headerArray.size()==0) {
            headerArray = new ArrayList<>();
        }
        arrayAdap = new TaskLayout(this, R.layout.item_list, headerArray);
        listView.setAdapter(arrayAdap);
    }

    private void callEdit(Task task){
        Intent intent = new Intent(this, EditTask.class);
        Bundle bundle = new Bundle();
        bundle.putString("TaskObject", new Gson().toJson(task));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

}
