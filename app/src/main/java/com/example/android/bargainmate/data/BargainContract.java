package com.example.android.bargainmate.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created in com.example.android.bargainmate.data by PETERR on 11/27/2018.
 */

public final class BargainContract {

    public static class BargainEntry implements BaseColumns{

        public final static String BARGAIN_TABLE_NAME = "bargains";

        public final static String COLUMN_TITLE="title";
        public final static String COLUMN_VENDOR="vendor";
        public final static String COLUMN_IMAGE_URL="imageurl";
        public final static String COLUMN_PRICE="price";
        public final static String COLUMN_URL="url";

        public final static String PATH_BARGAIN=BARGAIN_TABLE_NAME;

        public static final String CONTENT_AUTHORITY = "com.example.android.bargainmate";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final Uri BARGAIN_CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_BARGAIN).build();

    }

}