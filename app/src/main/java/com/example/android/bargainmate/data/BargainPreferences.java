package com.example.android.bargainmate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;


import com.example.android.bargainmate.R;

/**
 * Created in com.example.android.bargainmate.data by PETERR on 11/27/2018.
 */

public class BargainPreferences {


    public static boolean searchOn(Context context, String store){

        String key;
        switch (store){
            case "ju":
                key=context.getResources().getString(R.string.pref_jumia);
                break;
            case "ko":
                key=context.getResources().getString(R.string.pref_konga);
                break;
            case "sl":
                key=context.getResources().getString(R.string.pref_slot);
                break;
            case "ji":
                key=context.getResources().getString(R.string.pref_jiji);
                break;
            case "ka":
                key=context.getResources().getString(R.string.pref_kara);
                break;
            default:
                throw new IllegalArgumentException();
        }
                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
                return sharedPreferences.getBoolean(key, true);

    }

}
