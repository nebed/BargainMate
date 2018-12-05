package com.example.android.bargainmate.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.bargainmate.data.BargainContract.BargainEntry;


/**
 * Created in com.example.android.bargainmate.data by PETERR on 11/27/2018.
 */

public class BargainProvider extends ContentProvider {
    public static final int CODE_BARGAIN = 100;
    public static final int CODE_BARGAIN_WITH_ID = 101;

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BargainEntry.CONTENT_AUTHORITY;

         /* This URI is content://com.example.android.bargainmate/bargains/ */
        matcher.addURI(authority, BargainEntry.PATH_BARGAIN, CODE_BARGAIN);
        matcher.addURI(authority, BargainEntry.PATH_BARGAIN+ "/#", CODE_BARGAIN_WITH_ID);

        return matcher;
    }

    private BargainDbHelper BargaindbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        BargaindbHelper = new BargainDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_BARGAIN: {
                cursor = BargaindbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        BargainEntry.BARGAIN_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_BARGAIN_WITH_ID: {
                selection = BargainEntry._ID+"=?";
                selectionArgs=new String[]{ String.valueOf(ContentUris.parseId(uri))};
                cursor = BargaindbHelper.getReadableDatabase().query(
                        BargainEntry.BARGAIN_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = BargaindbHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {
            case CODE_BARGAIN:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BargainEntry.BARGAIN_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
        }

                return rowsInserted;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Bargain");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        throw new RuntimeException("We are not implementing insert in Bargain");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted =0;
        SQLiteDatabase Ddb=BargaindbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_BARGAIN:

                numRowsDeleted = Ddb.delete(
                        BargainEntry.BARGAIN_TABLE_NAME,
                        null,
                        null);
                break;
            case CODE_BARGAIN_WITH_ID:
                selection = BargainEntry._ID+"=?";
                selectionArgs =new String[] {String.valueOf(ContentUris.parseId(uri))};
                numRowsDeleted =Ddb.delete(BargainEntry.BARGAIN_TABLE_NAME, selection, selectionArgs);
                break;
        }
 /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

            return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("We are not implementing update in Bargain");
    }
}
