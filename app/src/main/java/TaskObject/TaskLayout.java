package TaskObject;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fpt.anhnht.assignment2_se61750_todolist.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anhnh on 09/27/2017.
 */

public class TaskLayout extends ArrayAdapter<Task> {

    /**
     * Declare array list of tasks
     */
    private ArrayList<Task> allTask;
    private Context myContext;

    public TaskLayout(@NonNull Context context, @LayoutRes int textViewResourceId, ArrayList<Task> allTask) {
        super(context, textViewResourceId, allTask);
        this.allTask = allTask;
        this.myContext= context;
    }
    static class taskView{
        private TextView header;
        private TextView fromDate;
        private TextView toDate;
        private TextView percent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**assign the view we are converting to a local variable*/
        View v = convertView;
        taskView tasks;
        /** first check to see if the view is null. if so, we have to inflate it.
         * to inflate it basically means to render or show the view */
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(R.layout.item_list, parent, false);
            tasks = new taskView();
            tasks.header = (TextView) convertView.findViewById(R.id.txtHeader);
            tasks.fromDate =(TextView) convertView.findViewById(R.id.txtFromDate);
            tasks.toDate =(TextView) convertView.findViewById(R.id.txtToDate);
            tasks.percent =(TextView) convertView.findViewById(R.id.txtPercent);
            convertView.setTag(tasks);
        }else{
            tasks = (taskView) convertView.getTag();
        }

        Task task = allTask.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tasks.header.setText(task.getHeader());
        tasks.fromDate.setText(sdf.format(new Date(task.getFromDate())));
        tasks.toDate.setText(sdf.format(new Date(task.getToDate())));
        tasks.percent.setText(String.valueOf(task.getPercentComplete()) + "%");


        return convertView;
    }
}
