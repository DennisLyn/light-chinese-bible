package com.bayrim.apps.clbible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bayrim.apps.clbible.db.BibleBookApp;
import com.bayrim.apps.clbible.db.BibleDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private static final String DB_NAME = "bible.db";
    private SQLiteDatabase BibleDB;

    private ArrayList books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BibleDBHelper bibleDBHelper=new BibleDBHelper(this);

        BibleDB=bibleDBHelper.openDataBase();

        fillBooks();
        setUpList();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Extracting elements from the database
    private void fillBooks() {
        BibleBookApp theBibleBookApp=new BibleBookApp(this);
        int iCounter,jCounter;
        books = new ArrayList<String>();
        /*
        Cursor bookCursor = BibleDB.query("BibleBook", new String[]{"_id", "BookName"
        }, null, null, null, null, null);
        */
        Cursor bookCursor=theBibleBookApp.getBookList();
        bookCursor.moveToFirst();
        if(!bookCursor.isAfterLast()) {
            iCounter=0;
            do {

                if(iCounter<39) {
                    //books.add("==== 舊約聖經 ====");
                    String name = bookCursor.getString(0) + " " + "舊約: " + " " + bookCursor.getString(1) ;
                    books.add(name);
                }
                else  {
                    //books.add("==== 新約聖經 ====");
                    String name = bookCursor.getString(0) + " " + "新約: " + " " + bookCursor.getString(1);
                    books.add(name);
                }



                iCounter++;
            } while (bookCursor.moveToNext());
        }
        bookCursor.close();
    }

    private void setUpList() {

        final Intent intent=new Intent(this,BookDetailActivity.class);

        ListView listview = (ListView) findViewById(R.id.bookList);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, books);
        listview.setAdapter(adapter);

        //Let’s set a message shown upon tapping an item
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                                    int position,long id) {

                intent.putExtra("BookID",id);
                startActivity(intent);
                /*
                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText() +
                                " details of the item",
                        Toast.LENGTH_SHORT).show();

                */

            }
        });
    }
}
