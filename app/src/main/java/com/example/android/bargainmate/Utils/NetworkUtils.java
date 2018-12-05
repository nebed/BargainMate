package com.example.android.bargainmate.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created in com.example.android.bargainmate.Utils by PETERR on 11/27/2018.
 */

public final class NetworkUtils {

    private static final String BASE_URL_STRING="https://onlinestorecompare.herokuapp.com/search";

    public static URL makeUrl(String query){
        Uri bargainUri=Uri.parse(BASE_URL_STRING).buildUpon()
                .appendEncodedPath(query)
                .build();
        try {
            URL BargainUrl = new URL(bargainUri.toString());
            Log.v("Bargain Query Product", "URL: " + BargainUrl);
            return BargainUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    public synchronized static void startImmediateSync(@NonNull final Context context, String query) {
        Intent intentToSyncImmediately = new Intent(context, BargainSyncIntentService.class);
        intentToSyncImmediately.putExtra("query", query );
        context.startService(intentToSyncImmediately);
    }
}
