package com.example.notes;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

class DataBaseManager extends SQLiteOpenHelper {

    public DataBaseManager(Context context) {
        super(context, "ToDoDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ToDoList (" +
                "id integer primary key autoincrement," +
                "title text, description text, " +
                "priority text, " +
                "date text, " +
                "isDone integer);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}