package com.example.android.bargainmate.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.bargainmate.data.BargainContract;

import java.net.URL;

/**
 * Created in com.example.android.bargainmate.Utils by PETERR on 11/27/2018.
 */

public class SearchUtils {

    synchronized public static void searchItem(Context context, String query) {


        try{
        URL BargainRequestUrl = NetworkUtils.makeUrl(query);

        String bargainResponse = NetworkUtils.getResponseFromHttpUrl(BargainRequestUrl);

        ContentValues[] bargainValues = Jsonutils.getBargainFromJson(bargainResponse);
         if (bargainValues !=null ){
             ContentResolver bargainContentResolver = context.getContentResolver();

             bargainContentResolver.delete(
                     BargainContract.BargainEntry.BARGAIN_CONTENT_URI,
                     null,
                     null);

             bargainContentResolver.bulkInsert(
                     BargainContract.BargainEntry.BARGAIN_CONTENT_URI, bargainValues);


         }
        }

        catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }


    }

}
