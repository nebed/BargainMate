package com.example.android.bargainmate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import com.example.android.bargainmate.data.BargainContract;

/**
 * Created in com.example.android.bargainmate by PETERR on 12/4/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_bargain);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Activity activity = getActivity();

        activity.getContentResolver().notifyChange(BargainContract.BargainEntry.BARGAIN_CONTENT_URI, null);

    }
}
