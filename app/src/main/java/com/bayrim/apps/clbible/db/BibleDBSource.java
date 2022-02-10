package com.bayrim.apps.clbible.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dennis on 11/11/2015.
 */
public class BibleDBSource {
    private SQLiteDatabase mDatabase;       // The actual DB!
    private BibleDBHelper mBibleDBHelper; // Helper class for creating and opening the DB
    private Context mContext;

    public BibleDBSource(Context context) {
        mContext = context;
        mBibleDBHelper = new BibleDBHelper(mContext);
    }
    /*
     * Open the db. Will create if it doesn't exist
     */
    public void open() throws SQLException {
        mDatabase = mBibleDBHelper.getWritableDatabase();
    }

    /*
     * We always need to close our db connections
     */
    public void close() {
        mDatabase.close();
    }

    public Cursor selectAllBibleBooks() {
        Cursor cursor = mDatabase.query(
                "BibleBook", // table
                new String[] { "_id","BookName" }, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                null  // orderby
        );

        return cursor;
    }

}
