package android.rss;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class DataBaseManager {

    private DataBaseSupport helper;

    DataBaseManager(DataBaseSupport dbh){
        helper = dbh;
    }

    public ArrayList<RSSItem> GetNewsArrayFromDataBase(){
        ArrayList<RSSItem> news = new ArrayList<>();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("rss11", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                RSSItem element = new RSSItem(title, description, date, link);
                news.add(element);
            } while (cursor.moveToNext());
        }
        return news;
    }

    public void AddItemToDataBase(RSSItem item) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("title", item.getTitle());
        value.put("description", item.getDescription());
        value.put("link", item.getLink());
        value.put("date", item.getPubDate());
        db.insert("rss11", null, value);
        db.close();
    }
}
