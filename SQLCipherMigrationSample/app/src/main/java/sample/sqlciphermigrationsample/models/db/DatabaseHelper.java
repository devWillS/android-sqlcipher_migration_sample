package sample.sqlciphermigrationsample.models.db;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import sample.sqlciphermigrationsample.R;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "test_database.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseMigrator.checkAndMigrateDatabase(context,DATABASE_NAME,context.getString(R.string.db_pass));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TODO_TABLE = "create table " + FeedEntry.TABLE_TODO + " ( " +
                FeedEntry.COLUMN_TODO_ID + " integer primary key autoincrement, " +
                FeedEntry.COLUMN_TODO_TITLE + " text, " +
                FeedEntry.COLUMN_TODO_CONTENTS + " text, " +
                FeedEntry.COLUMN_CREATED + " text, " +
                FeedEntry.COLUMN_MODIFIED + " text, " +
                FeedEntry.COLUMN_LIMIT_DATE + " text, " +
                FeedEntry.COLUMN_DELETE_FLG + " integer" + " ) ";

        sqLiteDatabase.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
