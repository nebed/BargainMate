package com.example.android.bargainmate.Utils;

import android.content.ContentValues;
import android.util.Log;

import com.example.android.bargainmate.data.BargainContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created in com.example.android.bargainmate.Utils by PETERR on 11/27/2018.
 */

public final class Jsonutils {

    public static ContentValues[] getBargainFromJson(String JsonResult) throws JSONException {

        JSONArray jsonArray =new JSONArray(JsonResult);

            /* Is there an error? */
            if (jsonArray.length() <= 0){
                Log.e("BAD JSON", "BAD JSON");
                return null;
            }
            Log.e("GGOOD JSON", "GOOD JSON IN JSON UTILS");


        ContentValues[] BargainContentValues = new ContentValues[jsonArray.length()];


        for (int i=0; i<jsonArray.length(); i++){

            String title;
            String vendor;
            String url;
            String price;
            String imageUrl;


            JSONObject article=jsonArray.getJSONObject(i);

            title=article.getString("title");
            vendor=article.getString("source");
            url=article.getString("url");
            imageUrl=article.getString("image");
            price=article.getString("price");


            ContentValues bargainValues = new ContentValues();

            bargainValues.put(BargainContract.BargainEntry.COLUMN_TITLE, title);
            bargainValues.put(BargainContract.BargainEntry.COLUMN_VENDOR, vendor);
            bargainValues.put(BargainContract.BargainEntry.COLUMN_URL, url);
            bargainValues.put(BargainContract.BargainEntry.COLUMN_IMAGE_URL, imageUrl);
            bargainValues.put(BargainContract.BargainEntry.COLUMN_PRICE, price);

            BargainContentValues[i] = bargainValues;

        }
        Log.v("BARGAIN", "JSON UTILS ENDED");
        return BargainContentValues;


    }
}
