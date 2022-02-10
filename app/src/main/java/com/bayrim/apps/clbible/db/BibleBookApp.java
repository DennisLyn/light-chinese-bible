package com.bayrim.apps.clbible.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dennis on 11/13/2015.
 */
public class BibleBookApp {

    private SQLiteDatabase mDatabase;       // The actual DB!
    private BibleDBHelper mBibleDBHelper; // Helper class for creating and opening the DB
    private Context mContext;
    private final String mTableName="BibleBook";
    public final int BibleBookNum=66;

    public BibleBookApp(Context context){
        mContext = context;
        mBibleDBHelper = new BibleDBHelper(mContext);
        mDatabase=mBibleDBHelper.openDataBase();
    }


    public BibleBookDAO getBook(int bookID) {
        BibleBookDAO theBibleBook=new BibleBookDAO();
        String bookIDString;
        bookIDString= Integer.toString(bookID);
        String whereClause = "_id = ?";
        String[] whereArgs = new String[] {
                Integer.toString(bookID)
        };
        String orderBy = "_id";


        Cursor cursor = mDatabase.query(
                mTableName, // table
                new String[] { "_id","BookName" }, // column names
                whereClause, // where clause
                whereArgs, // where params
                null, // groupby
                null, // having
                orderBy  // orderby
        );

        //return cursor;
        theBibleBook.setID(bookID);
        if (cursor.moveToFirst()){
            theBibleBook.setBookName(cursor.getString(cursor.getColumnIndex("BookName")));
        }

        cursor.close();
        return theBibleBook;
    }

    public Cursor getBookList(){

        String orderBy = "_id";


        Cursor cursor = mDatabase.query(
                mTableName, // table
                new String[] { "_id", "BookName" }, // column names
                null, // where clause
                null, // where params
                null, // groupby
                null, // having
                orderBy  // orderby
        );

        return cursor;
    }

}
