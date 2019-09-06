package sample.sqlciphermigrationsample;

import android.app.Application;

import net.sqlcipher.database.SQLiteDatabase;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeSQLCipher();
    }

    private void initializeSQLCipher() {
        SQLiteDatabase.loadLibs(this);
    }
}