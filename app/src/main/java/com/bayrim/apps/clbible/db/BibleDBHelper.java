package com.bayrim.apps.clbible.db;

import android.content.res.AssetManager;
import android.util.Log;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dennis on 11/5/2015.
 */
public class BibleDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "bible.db";
    private static final String DB_PATH = "/data/data/com.bayrim.apps.clbible/databases/";
    private static final int DB_VERSION = 2; // Must increment to trigger an upgrade

    public Context mcontext;
    public SQLiteDatabase database;
    //public static final String TABLE_BibleSource = "BibleSource";

    public BibleDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mcontext=context;
        //context.deleteDatabase("bible.db");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(DB_ALTER);
        //db.execSQL(DB_ALTER);
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {

            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }


    //This piece of code will create a database if it’s not yet created
    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }
    //Performing a database existence check
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }
        //Android doesn’t like resource leaks, everything should
        // be closed
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    //Method for copying the database
    private void copyDataBase() throws IOException {
        AssetManager am = null;

        Log.i(this.getClass().toString(), "Start t copy database.");
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = mcontext.getAssets().open(DB_NAME);

        //Path to the created empty database on your Android device
        String outFileName = DB_PATH + DB_NAME;

        //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

        //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }
}
