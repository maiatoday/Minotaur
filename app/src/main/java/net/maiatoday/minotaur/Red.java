package net.maiatoday.minotaur;

import android.util.Log;

/**
 * Created by maia on 2014/11/18.
 */
public class Red {
    public static void showTrace(String message) {
        Log.d("RED", message, new Exception("RED THREAD " + message));
    }
}
