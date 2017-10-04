package com.appizona.openvision.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appizona.openvision.model.Todo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yehia on 15/09/17.
 */

public class TodoDatabase extends SQLiteOpenHelper {

    private static final String NAME = "todo.db";
    private static final int VERSION = 2;

    private static TodoDatabase database;


    private TodoDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static TodoDatabase getInstance(Context context) {
        if (database == null)
            database = new TodoDatabase(context);

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS task (title TEXT,due_date TEXT,time_stamp TEXT primary key,description TEXT,is_done TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if EXISTS task");
        onCreate(db);
    }


    public void addTodo(String title, String dueDate, String timeStamp, String description, String isDone) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("due_date", dueDate);
        values.put("time_stamp", timeStamp);
        values.put("description", description);
        values.put("is_done", isDone);

        database.insert("task", null, values);

    }


    public List<Todo> getAllTodos() {
        int i = 0;
        String title, description, dueDate, timeStamp, isDone;
        Todo todos[] = new Todo[getTodoCont()];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from task", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            title = res.getString(res.getColumnIndex("title"));
            description = res.getString(res.getColumnIndex("description"));
            dueDate = res.getString(res.getColumnIndex("due_date"));
            timeStamp = res.getString(res.getColumnIndex("time_stamp"));
            isDone = res.getString(res.getColumnIndex("is_done"));
            if (i == todos.length)
                break;

            todos[i] = new Todo()
                    .setTitle(title)
                    .setDescription(description)
                    .setDueDate(dueDate)
                    .setTimeStamp(timeStamp)
                    .setDone(isDone);
            res.moveToNext();
            i++;
        }

        return new ArrayList<>(Arrays.asList(todos));
    }


    public List<Todo> getAllTodosByDate(String dDate) {
        int i = 0;
        String title, description, dueDate, timeStamp, isDone;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM task WHERE TRIM(due_date) = '" + dDate.trim() + "'", null);
        Todo todos[] = new Todo[res.getCount()];
        res.moveToFirst();

        while (!res.isAfterLast()) {
            title = res.getString(res.getColumnIndex("title"));
            description = res.getString(res.getColumnIndex("description"));
            dueDate = res.getString(res.getColumnIndex("due_date"));
            timeStamp = res.getString(res.getColumnIndex("time_stamp"));
            isDone = res.getString(res.getColumnIndex("is_done"));
            if (i == todos.length)
                break;

            todos[i] = new Todo()
                    .setTitle(title)
                    .setDescription(description)
                    .setDueDate(dueDate)
                    .setTimeStamp(timeStamp)
                    .setDone(isDone);
            res.moveToNext();
            i++;
        }

        return new ArrayList<>(Arrays.asList(todos));
    }

    public boolean deleteTodo(String timeStamp) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete("task", "time_stamp=?", new String[]{timeStamp}) > 0;

    }

    private int getTodoCont() {
        String countQuery = "SELECT * FROM task";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void updateTodo(String title, String dueDate, String timeStamp, String description, String isDone) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("due_date", dueDate);
        values.put("description", description);
        values.put("is_done", isDone);

        database.update("task", values, "time_stamp=" + timeStamp, null);
    }
}
