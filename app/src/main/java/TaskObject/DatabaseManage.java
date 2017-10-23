package TaskObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by anhnh on 09/26/2017.
 */

public class DatabaseManage extends SQLiteOpenHelper {

    /** All Static variables */
    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "TEST_DB_8";

    /**
     * To do list table name
     */
    private static final String TABLE_NAME = "tasks_tbl";

    /**
     * To do list Table Columns names
     */
    private static final String TASK_ID = "ID";
    private static final String TASK_NAME = "NAME";
    private static final String TASK_FROM_DATE = "FROM_DATE";
    private static final String TASK_TO_DATE = "TO_DATE";
    private static final String TASK_CONTENT = "CONTENT";
    private static final String TASK_PER_COM = "PER_COMP";

    /**
     * Constructor
     */
    public DatabaseManage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TABLE_NAME +
                "(" + TASK_ID + " INTEGER PRIMARY KEY autoincrement," +
                TASK_NAME + " TEXT," +
                TASK_FROM_DATE + " INTEGER," +
                TASK_TO_DATE + " INTEGER," +
                TASK_CONTENT + " TEXT," +
                TASK_PER_COM + " INTEGER)";
        db.execSQL(createTable);
    }

    private SQLiteDatabase db;

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** Drop old table if existed */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        /** Create table again */
        onCreate(db);
    }

    public long addTask(Task task) {
        db = this.getReadableDatabase();
        ContentValues value = new ContentValues();
//        value.put(TASK_ID, task.getId());
        value.put(TASK_NAME, task.getHeader());
        value.put(TASK_FROM_DATE, task.getFromDate());
        value.put(TASK_TO_DATE, task.getToDate());
        value.put(TASK_CONTENT, task.getContent());
        value.put(TASK_PER_COM, task.getPercentComplete());

        /** Insert row */
        long result = db.insert(TABLE_NAME, null, value);
        db.close();
        return result;
    }

    public void updateTask(Task task) {
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
//        values.put(TASK_ID, task.getId());
        values.put(TASK_NAME, task.getHeader());
        values.put(TASK_FROM_DATE, task.getFromDate());
        values.put(TASK_TO_DATE, task.getToDate());
        values.put(TASK_CONTENT, task.getContent());
        values.put(TASK_PER_COM, task.getPercentComplete());
        /** public int update (String table, ContentValues values, String whereClause, String[] whereArgs) */
        try {
            db.update(TABLE_NAME, values, TASK_ID + "=?", new String[]{String.valueOf(task.getId())});
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        db.close();
    }

    public void deleteTask(int ID) {
        db = this.getReadableDatabase();
        /** public int delete (String table, String whereClause, String[] whereArgs) */
        db.delete(TABLE_NAME, TASK_ID + "=?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public ArrayList<Task> getAllTask() {
        db = this.getReadableDatabase();
        /**
         * public Cursor query (
         *      String table, String[] columns, String selection, String[] selectionArgs,
         *      String groupBy, String having, String orderBy)
         * */
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, TASK_ID+" ASC ");
        ArrayList<Task> allTasks = new ArrayList<>();
        Task task;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                Long fromDate = cursor.getLong(2);
                Long toDate = cursor.getLong(3);
                String content = cursor.getString(4);
                int percent_complete = cursor.getInt(5);
                task = new Task(id, name, fromDate, toDate, content, percent_complete);
                allTasks.add(task);
            }
        } else {
            return null;
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public Task getTask(int ID) {
        db = this.getReadableDatabase();
        /**
         * public Cursor query (
         *      String table, String[] columns, String selection, String[] selectionArgs,
         *      String groupBy, String having, String orderBy)
         * */
        Cursor cursor = db.query(TABLE_NAME, null, TASK_ID + "=?", new String[]{String.valueOf(ID)}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Long fromDate = cursor.getLong(2);
            Long toDate = cursor.getLong(3);
            String content = cursor.getString(4);
            int percent_complete = cursor.getInt(5);
            Task task = new Task(id, name, fromDate, toDate, content, percent_complete);
            return task;
        } else {
            return null;
        }
    }
}
