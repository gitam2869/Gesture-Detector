package com.example.gesturedetector;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager
{
    private static SharedPreferenceManager sharedPreferenceManager;
    private static Context context;

    private static final String SHARED_PREF_NAME = "userinfo";

    private static final String VIDEO_NO = "videono";




    private SharedPreferenceManager(Context context)
    {
        this.context = context;
    }

    public static synchronized SharedPreferenceManager getInstance(Context context)
    {
        if(sharedPreferenceManager == null)
        {
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }
        return sharedPreferenceManager;
    }


    /**
     * Store data
     */

    public boolean videoNumber(int position)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(VIDEO_NO, position);
        editor.apply();

        return true;
    }


    /**
     * Retrieve data
     */


    public int getVideoNumber()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(VIDEO_NO, -1);
    }




    /*
        Login or Logout methods
     */



}
