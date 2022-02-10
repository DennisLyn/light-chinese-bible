package com.bayrim.apps.clbible;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bayrim.apps.clbible.db.BibleBookApp;
import com.bayrim.apps.clbible.db.BibleBookDAO;
import com.bayrim.apps.clbible.db.BibleSourceApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static android.R.attr.button;

public class BookDetailActivity extends AppCompatActivity {

    private long mRawID;
    private int mBookID;
    private int mChapterNum;
    private int mTotalChapterNum;
    private BibleBookDAO mBibleBook;
    private BibleBookApp mBibleBookApp=new BibleBookApp(this);
    private BibleSourceApp mBibleSourceApp=new BibleSourceApp(this);

    private TextView mBookName;
    private Spinner chapterDropdownList;

    private Button mSearch;
    private TextView mSearchText;
    //private Button mPreBt;



    //private ArrayList<LinkedHashMap<String,String>> sectionList;
    private ArrayList<ArrayList<String>> sectionList = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        Intent intent=getIntent();

        mRawID=intent.getLongExtra("BookID",-1L);
        if(mRawID==-1){
            Log.e(this.getClass().toString(), "Book ID not found.");
        }
        mBookID=(int)mRawID+1;
        mBibleBook=mBibleBookApp.getBook(mBookID);
        Log.i(this.getClass().toString(), "Book Name is " + mBibleBook.getBookName());
        Toast.makeText(getApplicationContext(),
                "Book Name is " + mBibleBook.getBookName(),
                Toast.LENGTH_SHORT).show();
        mBookName=(TextView)findViewById(R.id.bookName);
        mBookName.setText( mBibleBook.getBookName());

        //Search keyword --------------------
        mSearch=(Button)findViewById(R.id.SearchBt);
        mSearchText=(TextView)findViewById(R.id.SearchText);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(BookDetailActivity.this)).inflate(R.layout.searchtext, null);
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(BookDetailActivity.this);
                alertBuilder.setView(view);
                final EditText mSearchInput=(EditText)view.findViewById(R.id.SearchInput);

                alertBuilder.setCancelable(true)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //Start to search the keyword
                        //mSearchText.setText(mSearchInput.getText());
                        //String SearchString= mSearchInput.getText().toString();
                        //Toast.makeText(getApplicationContext(),mSearchInput.getText().toString(),Toast.LENGTH_SHORT).show();
                        if(mSearchInput.getText().toString() != null && !mSearchInput.getText().toString().isEmpty() && !mSearchInput.getText().toString().equals("null"))
                        {

                            fillSearchContent(mSearchInput.getText().toString());
                            setUpList();
                        }
                        else {


                            fillSearchContent("nullword");
                            setUpList();
                            Toast.makeText(getApplicationContext(),"請輸入查詢字串",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                Dialog dialog=alertBuilder.create();
                dialog.show();
            }
        });
        //Search keyword =================

        createBookDropdownList(mBookID);
        createChapterDropdownList();
        mChapterNum=1;
        fillSectionContent(mChapterNum);
        setUpList();

        //
        // final Button mPreBt = (Button) findViewById(R.id.PreBt);
        // final Button mNxtBt = (Button) findViewById(R.id.NxtBt);

    }

    private void createBookDropdownList(int BookID) {
        BibleBookApp theBibleBookApp=new BibleBookApp(this);
        chapterDropdownList=(Spinner)findViewById(R.id.bookSpinner);
        List<String> tmplist = new ArrayList<String>();
        int iCounter;
        int MaxNum=0;
        MaxNum=mBibleSourceApp.getMaxChapterNum(mBibleBook.getBookName());
        Cursor bookCursor=theBibleBookApp.getBookList();
        bookCursor.moveToFirst();
        for(iCounter=1;iCounter<=theBibleBookApp.BibleBookNum;iCounter++){
            //tmplist.add("Chapter " + Integer.toString(iCounter));
            if(iCounter<39) {
                //books.add("==== 舊約聖經 ====");
                String name =  bookCursor.getString(1) ;
                tmplist.add(name);
            }
            else  {
                //books.add("==== 新約聖經 ====");
                String name = bookCursor.getString(1);
                tmplist.add(name);
            }
            bookCursor.moveToNext();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tmplist);
        dataAdapter.setDropDownViewResource(R.layout.book_spinner);
        chapterDropdownList.setAdapter(dataAdapter);
        String compareValue = mBibleBook.getBookName();
        if (!compareValue.equals(null)) {
            int spinnerPosition = dataAdapter.getPosition(compareValue);
            chapterDropdownList.setSelection(spinnerPosition);
        }

        chapterDropdownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //DetailIntent.putExtra("BookID",id);
                //startActivity(DetailIntent);
                mBibleBook=mBibleBookApp.getBook(position+1);
                refreshBookDetail(0);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            private void refreshBookDetail(int position) {
                createChapterDropdownList();
                fillSectionContent(1);
                setUpList();
            }
        });
    }

    private void createChapterDropdownList() {

        chapterDropdownList=(Spinner)findViewById(R.id.chapterList);
        List<String> tmplist = new ArrayList<String>();
        int iCounter;
        int MaxNum=0;
        MaxNum=mBibleSourceApp.getMaxChapterNum(mBibleBook.getBookName());

        for(iCounter=1;iCounter<=MaxNum;iCounter++){
            //tmplist.add("Chapter " + Integer.toString(iCounter));
            tmplist.add("第 " + Integer.toString(iCounter) + " 章");
        }
        mTotalChapterNum=MaxNum;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tmplist);
        dataAdapter.setDropDownViewResource(R.layout.chapter_spinner);
        chapterDropdownList.setAdapter(dataAdapter);

        chapterDropdownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {

            public void onItemSelected(AdapterView<?> parent, View view,
                                    int position, long id) {

                //DetailIntent.putExtra("BookID",id);
                //startActivity(DetailIntent);
                refreshBookDetail(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            private void refreshBookDetail(int position) {
                fillSectionContent(position+1);
                setUpList();
            }
        });
    }


    private void fillSectionContent(int chapterNum) {
        //ArrayList sectionContents = new ArrayList<String>();
        Cursor cursor;
        cursor=mBibleSourceApp.selectSection(mBibleBook.getBookName(),chapterNum);
        cursor.moveToFirst();

        mChapterNum=chapterNum;
        mBookName.setText( mBibleBook.getBookName());
        chapterDropdownList.setSelection(chapterNum-1,true);

        if(!cursor.isAfterLast()) {
            //sectionList=new ArrayList<LinkedHashMap<String,String>>();
            sectionList=new ArrayList<ArrayList<String>>();


            do {
                //LinkedHashMap<String,String> temp=new LinkedHashMap<String, String>();
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(cursor.getString(3) + ":" + cursor.getString(4));
                temp.add(cursor.getString(5));
                sectionList.add(temp);
                //sectionContents.add(cursor.getString(5));
            }while (cursor.moveToNext());
        }

        cursor.close();

    }
    private void setUpList() {
        SectionListViewAdapter adapter=new SectionListViewAdapter(this, sectionList);
        ListView listView=(ListView)findViewById(R.id.sectionList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                int pos = position + 1;
                //Toast.makeText(getApplicationContext(), Integer.toString(pos) + " Clicked", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void fillSearchContent(String SearchString){
        Cursor cursor;
        cursor=mBibleSourceApp.getSearchResult(SearchString);
        cursor.moveToFirst();

        //mChapterNum=chapterNum;
        //mBookName.setText( mBibleBook.getBookName());
        //chapterDropdownList.setSelection(chapterNum-1,true);

        if(!cursor.isAfterLast()) {
            //sectionList=new ArrayList<LinkedHashMap<String,String>>();
            sectionList=new ArrayList<ArrayList<String>>();
            do {
                //LinkedHashMap<String,String> temp=new LinkedHashMap<String, String>();
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(cursor.getString(1) + " " + cursor.getString(3) + ":" + cursor.getString(4));
                temp.add(cursor.getString(5));
                sectionList.add(temp);
                //sectionContents.add(cursor.getString(5));
            }while (cursor.moveToNext());
        }
        else
        {
            sectionList.clear();
        }

        cursor.close();
    }

    //Button events
    public void goPrevious(View view) {
        if(mChapterNum==1)
        {
            return;
        }
        else
        {
            mChapterNum=mChapterNum-1;
            fillSectionContent(mChapterNum);
            setUpList();
        }

    }
    public void goNext(View view) {
        if(mChapterNum==mTotalChapterNum)
        {
            return;
        }
        else
        {
            mChapterNum=mChapterNum+1;
            fillSectionContent(mChapterNum);
            setUpList();
        }

    }

}
