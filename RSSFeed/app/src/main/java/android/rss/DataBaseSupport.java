package android.rss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DataBaseSupport extends SQLiteOpenHelper {

    public DataBaseSupport(Context context) {
        super(context, "rssDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table rss11 (" +
                "id integer primary key autoincrement, " +
                "title text, " +
                "description text, " +
                "date text, " +
                "link text);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}