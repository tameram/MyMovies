package com.example.mymovies;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.mymovies.DbConstantsToFavor;
import com.example.mymovies.DbHandlerToFavor;
import com.example.mymovies.R;

public class MainActivity extends ListActivity implements View.OnClickListener ,  AdapterView.OnItemClickListener {


    public DbHandlerToFavor dbHandler;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listViewMain = getListView();
        Intent intent = getIntent();
        Button btn_AddManu= (Button) findViewById(R.id.btn_AddManu);
        Button btn_DeleteAll= (Button) findViewById(R.id.btn_DeleteAll);
        Button btn_SearchMain= (Button) findViewById(R.id.btn_SearchMain);
        String title = null ,actors= null,genre= null,director= null
                ,year= null,runTime= null,plot= null,theImage= null;
        boolean isItComeFromEdit = false;
        //Boolean.parseBoolean(intent.getExtras().getString("isItComeFromEdit"));

        if(isItComeFromEdit) {


             title = intent.getExtras().getString("Title");
             actors = intent.getExtras().getString("Actors");
             genre = intent.getExtras().getString("Genre");
             director = intent.getExtras().getString("Director");
             year = intent.getExtras().getString("Year");
             runTime = intent.getExtras().getString("RunTime");
             plot = intent.getExtras().getString("Plot");
             theImage = intent.getExtras().getString("theImage");
        }
            // fill();
            dbHandler = new DbHandlerToFavor(this);
        if(isItComeFromEdit) {
            if (title != null) {
                theMovie theMovie = new theMovie(title, actors, director, genre, plot, year, runTime, theImage, "");
                dbHandler.insert(theMovie);
                refreshTheOutput();

            }
        }
        Cursor cursor = dbHandler.queryAll();

        startManagingCursor(cursor);


        String[] from = new String [] {DbConstantsToFavor.MOVIE_TITLE,DbConstantsToFavor.MOVIE_YEAR,DbConstantsToFavor.MOVIE_RUNTIME};
        int[] to = new int[] {R.id.textTitle,R.id.textYear,R.id.textRunTime};

        adapter = new SimpleCursorAdapter(this, R.layout.the_movie_icon, cursor, from, to);
        listViewMain.setAdapter(adapter);

        btn_AddManu.setOnClickListener(this);
        btn_SearchMain.setOnClickListener(this);
        btn_DeleteAll.setOnClickListener(this);
        listViewMain.setOnItemClickListener(this);


        // fill();



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        theMovie theMovie = dbHandler.query(position+1);//parent.getCount()- (position+1)



        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("Title",theMovie.getTitle());
        intent.putExtra("Actors",theMovie.getActors());
        intent.putExtra("Genre",theMovie.getGenre());
        intent.putExtra("Director",theMovie.getDirector());
        intent.putExtra("Year",theMovie.getYear());
        intent.putExtra("RunTime",theMovie.getRunTime());
        intent.putExtra("Plot",theMovie.getPlot());
        intent.putExtra("theImage",theMovie.getTheImage());


        startActivity(intent);


    }


    private void refreshTheOutput() {
        //Log.d(TAG, "onRefreshClick");

        Cursor oldCursor = adapter.getCursor();

        Cursor newCursor = dbHandler.queryAll();
        startManagingCursor(newCursor);

        adapter.changeCursor(newCursor);

        stopManagingCursor(oldCursor);
        oldCursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_AddManu:
                Intent intent = new Intent(this,EditActivity2.class);
                startActivity(intent);
                //fill();
                 refreshTheOutput();
                break;
            case R.id.btn_SearchMain:
                Intent intent1 = new Intent(this,SearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_DeleteAll:
                dbHandler.removeAll();
                refreshTheOutput();

                break;

        }

    }

}
