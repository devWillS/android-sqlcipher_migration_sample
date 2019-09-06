package sample.sqlciphermigrationsample.models.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHandler {


    public static long insert(SQLiteDatabase db, String title, String content) {
        String nowDate = getNowDate();
        String limited = getLimitDateFrom(nowDate);

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_TODO_TITLE, title);
        contentValues.put(FeedEntry.COLUMN_TODO_CONTENTS, content);
        contentValues.put(FeedEntry.COLUMN_CREATED, nowDate);
        contentValues.put(FeedEntry.COLUMN_MODIFIED, nowDate);
        contentValues.put(FeedEntry.COLUMN_LIMIT_DATE, limited);
        contentValues.put(FeedEntry.COLUMN_DELETE_FLG, 0);

        long ret = -1L;
        try {
            ret = db.insert(FeedEntry.TABLE_TODO, null, contentValues);
        } catch (Exception e) {
            Log.e("DatabaseError", e.getMessage());
        }

        return ret;
    }

    public static int update(SQLiteDatabase db, int todoID, String title, String content) {
        String nowDate = getNowDate();
        String limited = getLimitDateFrom(nowDate);

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_TODO_TITLE, title);
        contentValues.put(FeedEntry.COLUMN_TODO_CONTENTS, content);
        contentValues.put(FeedEntry.COLUMN_MODIFIED, nowDate);
        contentValues.put(FeedEntry.COLUMN_LIMIT_DATE, limited);


        int ret = -1;
        try {
            String whereClause = FeedEntry.COLUMN_TODO_ID + " = ?";
            String[] whereArgs = {String.valueOf(todoID)};

            ret = db.update(FeedEntry.TABLE_TODO, contentValues, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e("DatabaseError", e.getMessage());
        }

        return ret;
    }

    public static List<TodoData> select(SQLiteDatabase db) {
        List<TodoData> todoList = new ArrayList<>();

        String sql = "select * from " + FeedEntry.TABLE_TODO +
                " where " + FeedEntry.COLUMN_DELETE_FLG + " = 0" +
                " order by " + FeedEntry.COLUMN_LIMIT_DATE + " desc ";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            while (cursor.moveToNext()) {
                int todoID = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String limit = cursor.getString(5);

                TodoData todo = new TodoData(todoID, title, content, limit);
                todoList.add(todo);
            }

        } finally {
            cursor.close();
        }

        return todoList;
    }

    public static int delete(SQLiteDatabase db, int todoID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_DELETE_FLG, 1);

        int ret = -1;
        try {
            String whereClause = FeedEntry.COLUMN_TODO_ID + " = ?";
            String[] whereArgs = {String.valueOf(todoID)};
            ret = db.update(FeedEntry.TABLE_TODO, contentValues, whereClause, whereArgs);
        } catch (Exception e) {
            Log.e("DatabaseError", e.getMessage());
        }

        return ret;
    }

    private static String getNowDate() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    private static String getLimitDateFrom(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date date = new Date(dateStr);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        Date limitDate = new Date(calendar.getTimeInMillis());
        return df.format(limitDate);
    }
}
