package com.example.android.bargainmate.Utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created in com.example.android.bargainmate.Utils by PETERR on 11/27/2018.
 */

public class BargainSyncIntentService extends IntentService {
    public BargainSyncIntentService() {
        super("BargainSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String que=intent.getStringExtra("query");
        if (que != null){
       SearchUtils.searchItem(this, que);}
    }
}
