package ajou.ac.kr.teaming.activity;

import android.util.Log;


public class LogManager
{
    public static final boolean _DEBUG = true;
    private static final String _TAG = "DOG_WALKER";

    public static void printLog(String text) {
        if ( _DEBUG ) {
            Log.d(_TAG, text);
        }
    }

    public static void printError(String text) {
        if(_DEBUG)
            Log.e(_TAG, "**ERROR** : " + text);
    }

}
