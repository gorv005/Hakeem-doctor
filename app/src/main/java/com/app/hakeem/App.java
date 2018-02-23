package com.app.hakeem;

import android.app.Application;
import android.content.Context;

import com.app.hakeem.db.DBHelper;
import com.app.hakeem.db.DatabaseManager;
import com.app.hakeem.util.C;
import com.app.hakeem.util.FontsOverride;

/**
 * Created by Ady on 9/2/2017.
 */

public class App extends Application {

    private static Context context;
    private DBHelper dbHelper;

    @Override
    public void onCreate() {
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);
        FontsOverride.setDefaultFont(this, "DEFAULT", C.FONT);
        FontsOverride.setDefaultFont(this, "MONOSPACE", C.FONT);
        FontsOverride.setDefaultFont(this, "SERIF", C.FONT);
        FontsOverride.setDefaultFont(this, "SANS_SERIF", C.FONT);
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
