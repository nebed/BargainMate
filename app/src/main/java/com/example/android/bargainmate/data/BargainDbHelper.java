package com.example.android.bargainmate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bargainmate.data.BargainContract.BargainEntry;

/**
 * Created in com.example.android.bargainmate.data by PETERR on 11/27/2018.
 */

public class BargainDbHelper extends SQLiteOpenHelper {
    public  final static  String DATABASE_NAME="bargains.db";
    public final static int DATABASE_VERSION=1;

    final String CREATE_BARGAIN_TABLE="CREATE TABLE " + BargainEntry.BARGAIN_TABLE_NAME + " (" +

           BargainEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            BargainEntry.COLUMN_TITLE       + " TEXT, "                 +

           BargainEntry.COLUMN_VENDOR + " TEXT, "                  +

           BargainEntry.COLUMN_IMAGE_URL   + " TEXT, "                    +
            BargainEntry.COLUMN_PRICE   + " TEXT, "                    +

            BargainEntry.COLUMN_URL   + " TEXT"                    +
            " );";

    public BargainDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BARGAIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BargainEntry.BARGAIN_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
