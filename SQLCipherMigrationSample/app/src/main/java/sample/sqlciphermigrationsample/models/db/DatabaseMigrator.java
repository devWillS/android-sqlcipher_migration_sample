package sample.sqlciphermigrationsample.models.db;

import android.content.Context;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

/*
https://discuss.zetetic.net/t/migrating-from-sqlcipher-3-5-1-to-4-1-3-in-android/3652
*/

public class DatabaseMigrator {

    public static void checkAndMigrateDatabase(Context context, String databaseName, String password) {

        String path = context.getDatabasePath(databaseName).getPath();

        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            @Override
            public void postKey(SQLiteDatabase database) {

                Cursor cursor = database.rawQuery("PRAGMA cipher_migrate", null);

                Boolean migrationOccurred = false;

                if (cursor.getCount() == 1) {
                    cursor.moveToFirst();
                    String selection = cursor.getString(0);

                    migrationOccurred = selection.equals("0");

                    Log.d("selection", selection);
                }

                cursor.close();

                Log.d("migrationOccurred:", String.valueOf(migrationOccurred));
            }

            @Override
            public void preKey(SQLiteDatabase database) { }
        };

        SQLiteDatabase database = null;

        try {
//            database = SQLiteDatabase.openDatabase(path, password, null, 0, hook);
            database = SQLiteDatabase.openOrCreateDatabase(path, password, null, hook);
        }
        catch (Exception e) {
            Log.e("trying to open db", e.getLocalizedMessage());
        }

        if (database != null) {

            database.close();
        }
    }
}