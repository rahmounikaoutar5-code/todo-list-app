package com.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.todolist.models.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table Tasks
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_CREATED_AT = "created_at";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT NOT NULL,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DUE_DATE + " INTEGER,"
                + COLUMN_PRIORITY + " INTEGER DEFAULT 1,"
                + COLUMN_COMPLETED + " INTEGER DEFAULT 0,"
                + COLUMN_CREATED_AT + " INTEGER"
                + ")";
        db.execSQL(CREATE_TASKS_TABLE);
        
        // Insérer des tâches d'exemple
        insertSampleTasks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
    
    // CRUD Operations
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        
        if (task.getDueDate() != null) {
            values.put(COLUMN_DUE_DATE, task.getDueDate().getTime());
        }
        
        values.put(COLUMN_PRIORITY, task.getPriority().ordinal());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_CREATED_AT, System.currentTimeMillis());
        
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }
    
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, 
                           COLUMN_DUE_DATE, COLUMN_PRIORITY, COLUMN_COMPLETED,
                           COLUMN_CREATED_AT},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        Task task = cursorToTask(cursor);
        cursor.close();
        return task;
    }
    
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + 
                           COLUMN_COMPLETED + ", " + COLUMN_PRIORITY + " DESC, " + 
                           COLUMN_DUE_DATE + " ASC";
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Task task = cursorToTask(cursor);
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return taskList;
    }
    
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        
        if (task.getDueDate() != null) {
            values.put(COLUMN_DUE_DATE, task.getDueDate().getTime());
        }
        
        values.put(COLUMN_PRIORITY, task.getPriority().ordinal());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        
        return db.update(TABLE_TASKS, values, 
                COLUMN_ID + " = ?", 
                new String[]{String.valueOf(task.getId())});
    }
    
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?", 
                new String[]{String.valueOf(id)});
        db.close();
    }
    
    // Helper Methods
    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        
        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
        task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        
        long dueDate = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE));
        if (dueDate > 0) {
            task.setDueDate(new Date(dueDate));
        }
        
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY));
        task.setPriority(Task.Priority.values()[priority]);
        
        task.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1);
        
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));
        task.setCreatedAt(new Date(createdAt));
        
        return task;
    }
    
    private void insertSampleTasks(SQLiteDatabase db) {
        // Tâche 1
        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_TITLE, "Bienvenue dans l'app !");
        values1.put(COLUMN_DESCRIPTION, "Ajoutez votre première tâche en cliquant sur le bouton +");
        values1.put(COLUMN_PRIORITY, Task.Priority.MEDIUM.ordinal());
        values1.put(COLUMN_COMPLETED, 0);
        values1.put(COLUMN_CREATED_AT, System.currentTimeMillis());
        db.insert(TABLE_TASKS, null, values1);
        
        // Tâche 2
        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_TITLE, "Café du matin ☕");
        values2.put(COLUMN_DESCRIPTION, "Prendre un bon café pour bien démarrer la journée");
        values2.put(COLUMN_PRIORITY, Task.Priority.LOW.ordinal());
        values2.put(COLUMN_COMPLETED, 1);
        values2.put(COLUMN_CREATED_AT, System.currentTimeMillis() - 86400000);
        db.insert(TABLE_TASKS, null, values2);
    }
}
