package com.bayrim.apps.clbible.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dennis on 11/13/2015.
 */
public class BibleSourceApp {
    private SQLiteDatabase mDatabase;       // The actual DB!
    private BibleDBHelper mBibleDBHelper; // Helper class for creating and opening the DB
    private Context mContext;
    private final String mTableName="BibleSource";

    public BibleSourceApp(Context context){
        mContext = context;
        mBibleDBHelper = new BibleDBHelper(mContext);
        mDatabase=mBibleDBHelper.openDataBase();
    }

    public Cursor selectSection(String bookName,int chapterNum) {
        String whereClause = "Book = ? AND Chapter = ?";
        String[] whereArgs = new String[] {
                bookName,
                Integer.toString(chapterNum)
        };
        String orderBy = "Section";


        Cursor cursor = mDatabase.query(
                "BibleSource", // table
                new String[] { "_id","Book","EngBookAbbre","Chapter","Section","Content" }, // column names
                whereClause, // where clause
                whereArgs, // where params
                null, // groupby
                null, // having
                orderBy  // orderby
        );

        return cursor;
    }
    public int getMaxChapterNum(String bookName){
        int theLastChapterNum=0;
        String whereClause = "Book = ?";
        String[] whereArgs = new String[] {
                bookName
        };
        Cursor cursor = mDatabase.query(
                "BibleSource", // table
                new String[] { "MAX(Chapter)"}, // column names
                whereClause, // where clause
                whereArgs, // where params
                null, // groupby
                null, // having
                null  // orderby
        );
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            theLastChapterNum=cursor.getInt(0);
        }

        return theLastChapterNum;
    }
    public Cursor getSearchResult(String theSearchString){

        //theSearchString="耶和華";
        /*
        String theSQL="SELECT _id,Book,EngBookAbbre,Chapter,Section,Content From BibleSource " +
                "WHERE Content Like '%耶和華%' ORDER BY _id";
        Cursor cursor=mDatabase.rawQuery(theSQL,null);
        */

        String theSQL="SELECT _id,Book,EngBookAbbre,Chapter,Section,Content From BibleSource " +
                "WHERE Content Like '%" + theSearchString + "%' ORDER BY _id";
        Cursor cursor=mDatabase.rawQuery(theSQL,null );


        /*
        //String whereClause = "Content Like '%?%'";
        String whereClause = "Content Like ?";
        theSearchString="耶和華";
        String[] whereArgs = new String[] {
                theSearchString
        };
        String orderBy = "_id";
        Cursor cursor = mDatabase.query(
                mTableName, // table
                new String[] { "_id","Book","EngBookAbbre","Chapter","Section","Content" }, // column names
                whereClause, // where clause
                whereArgs, // where params
                null, // groupby
                null, // having
                orderBy  // orderby
        );
        */
        return cursor;

    }
}
