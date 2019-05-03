package nl.ou.applabdemo.util;

import android.app.Application;
import android.content.Context;

/**
 * Geeft de context terug
 */
public class AppContext extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
