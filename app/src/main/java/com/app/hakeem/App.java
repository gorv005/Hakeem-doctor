package com.app.hakeem;

import android.app.Application;

import com.app.hakeem.util.C;
import com.app.hakeem.util.FontsOverride;

/**
 * Created by Ady on 9/2/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        FontsOverride.setDefaultFont(this, "DEFAULT", C.FONT);
        FontsOverride.setDefaultFont(this, "MONOSPACE", C.FONT);
        FontsOverride.setDefaultFont(this, "SERIF", C.FONT);
        FontsOverride.setDefaultFont(this, "SANS_SERIF", C.FONT);
        super.onCreate();
    }
}
