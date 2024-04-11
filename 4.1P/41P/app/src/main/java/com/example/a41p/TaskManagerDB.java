package com.example.a41p;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerDB extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "tasks";
    private static final String DB_NAME = "taskManagerDatabase.db";
    public TaskManagerDB(Context context) {super(context, DB_NAME, null, 1);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlDB = "CREATE TABLE tasks (id TEXT PRIMARY KEY, title TEXT, description TEXT, duedate TEXT)";
        db.execSQL(sqlDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean addTask(Task task) {
        SQLiteDatabase sql_DB = getWritableDatabase();
        ContentValues cal = new ContentValues();
        cal.put("id", task.getId());
        cal.put("title", task.getTitle());
        cal.put("description", task.getDescription());
        cal.put("dueDate", task.getDueDate());

        long rowId = sql_DB.insert(TABLE_NAME, null, cal);
        sql_DB.close();
        if (rowId > -1) {
            System.out.println("Task added" + rowId);
            return true;
        } else {
            System.out.println("Insert failed | ERROR");
            return false;
        }
    }

    public Boolean updateTask(Task task) {
        SQLiteDatabase sql_DB = getWritableDatabase();
        ContentValues cal = new ContentValues();
        cal.put("id", task.getId());
        cal.put("title", task.getTitle());
        cal.put("description", task.getDescription());
        cal.put("dueDate", task.getDueDate()); // Make sure this column name matches your schema as well

        // Corrected column name from "_id" to "id"
        int numOfRowsAffected = sql_DB.update(TABLE_NAME, cal, "id = ?", new String[]{task.getId()});
        sql_DB.close();

        // Return true if at least one row was updated.
        return numOfRowsAffected > 0;
    }

    public Task getTask(String id) {

        // SELECT id, title, description, dueDate FROM TABLE_NAME WHERE id = 'specific_id';
        SQLiteDatabase sql_DB = this.getReadableDatabase();
        Cursor query = sql_DB.query(TABLE_NAME, new String[] {"id", "title", "description", "dueDate"},
                                    "id=?", new String[] { id }, null, null,null,null);
        if (query == null) return null;
        query.moveToFirst();
        Task task = new Task(query.getString(0), query.getString(1), query.getString(2), query.getString(3));
        query.close();
        sql_DB.close();
        return task;
    }

    public List<Task> getAllTask() {

        // SELECT * FROM TABLE_NAME;
        SQLiteDatabase sql_DB = getReadableDatabase();
        Cursor query = sql_DB.query(TABLE_NAME, null, null, null, null, null, null);
        List<Task> result = new ArrayList<>();

        while (query.moveToNext()) {
            result.add(new Task(query.getString(0), query.getString(1), query.getString(2), query.getString(3)));
        }
        query.close();
        sql_DB.close();
        return result;
    }

    public Boolean deleteTask(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {id};
        int numRowsDeleted = db.delete(TABLE_NAME, "id" + "= ?", whereArgs);
        db.close();
        if (numRowsDeleted > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String generateUniqueId(TaskManagerDB taskManagerDB) {
        List<Task> taskList = taskManagerDB.getAllTask();
        int newId = 1;
        boolean isUnique;
        do {
            isUnique = true;
            String newIdStr = String.format("%05d", newId);
            for (Task task : taskList) {
                if (task.getId().equals(newIdStr)) {
                    isUnique = false;
                    break;
                }
            }
            if (!isUnique) {
                newId++;
            }
        } while (!isUnique);
        return String.format("%05d", newId);
    }

    public boolean deleteAllTask() {
        SQLiteDatabase sql_DB = getReadableDatabase();
        try {
            sql_DB.delete(TABLE_NAME, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
