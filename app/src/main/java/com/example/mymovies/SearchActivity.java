package com.example.mymovies;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchActivity extends ListActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public final static String API_URL = "https://www.omdbapi.com/?i=tt3896198&apikey=54b699ff&t=";
    private String nameOfSearch="";
    EditText ed_toSearch;
    private DbHandlerToSearch dbHandler;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ListView listViewMain = getListView();
        //listViewMain.setStackFromBottom(true);
        ed_toSearch = (EditText) findViewById(R.id.ed_toSearch);
        Button btn_Search = (Button) findViewById(R.id.btn_Search);
        btn_Search.setOnClickListener(this);

        dbHandler = new DbHandlerToSearch(SearchActivity.this);

        Cursor cursor = dbHandler.queryAll();
        startManagingCursor(cursor);


        String[] from = new String [] {DbConstantsToSerach.MOVIE_TITLE,DbConstantsToSerach.MOVIE_YEAR,DbConstantsToSerach.MOVIE_RUNTIME};
        int[] to = new int[] {R.id.textTitle,R.id.textYear,R.id.textRunTime};

        adapter = new SimpleCursorAdapter(SearchActivity.this, R.layout.the_movie_icon, cursor, from, to);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewMain.setAdapter(adapter);
        listViewMain.setOnItemClickListener(this);
        listViewMain.setOnItemLongClickListener(this);




    }


    @Override
    public void onClick(View v) {
        nameOfSearch=ed_toSearch.getText().toString();
        SearchMovies searchMovies = new SearchMovies();
        searchMovies.execute(API_URL+nameOfSearch);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        theMovie theMovie = dbHandler.query( position+1 );//parent.getCount()- (position+1)




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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        theMovie theMovie = dbHandler.query(position+1);




        Intent intent = new Intent(this,EditActivity2.class);


        intent.putExtra("Title",theMovie.getTitle());
        intent.putExtra("Actors",theMovie.getActors());
        intent.putExtra("Genre",theMovie.getGenre());
        intent.putExtra("Director",theMovie.getDirector());
        intent.putExtra("Year",theMovie.getYear());
        intent.putExtra("RunTime",theMovie.getRunTime());
        intent.putExtra("Plot",theMovie.getPlot());
        intent.putExtra("theImage",theMovie.getTheImage());

        startActivity(intent);


        return true;

    }

    class SearchMovies extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;



        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(SearchActivity.this);
            dialog.setTitle("loading");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();

            try {
                //create a url:
                URL url = new URL(params[0]);

                //create a connection and open it:
                connection = (HttpURLConnection) url.openConnection();

                //status check:
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    //connection not good - return.
                    Log.d(" ", "error // not coonect");
                    return null;
                }
                Log.d(" ", "yes //  coonect");
                //get a buffer reader to read the data stream as characters(letters)
                //in a buffered way.
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //go over the input, line by line
                String line="";
                while ((line=input.readLine())!=null){
                    //append it to a StringBuilder to hold the
                    //resulting string
                    response.append(line+"\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (input!=null){
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(connection!=null){
                    //must disconnect the connection
                    connection.disconnect();
                }
            }

            //return the collected string:
            // this will be returned to : onPostExecute(String result)
            return response.toString();



        }
        @Override
        protected void onPostExecute(String result) {
            // do this last (with the result from the doInBackgroud() )
            // (UI thread)
//          TextView textOutput = (TextView) findViewById(R.id.text_output);

            // dialog.dismiss();

            //clear the list:
            //  MovieList.clear();

            if (result == null || result.length() == 0) {
                // no result:
                //textOutput.setText("error loading data...");
            } else {

                // parse results from json

                //   try {
                JSONObject responseObject = null;
                try {
                    responseObject = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //JSONArray resultsArray = responseObject.getJSONArray("results");

                // Iterate over the JSON array:
                // for (int i = 0; i < resultsArray.length(); i++) {
                // the JSON object in position i
                // JSONObject MovieObject = resultsArray.getJSONObject(i);

                // get the primitive values in the object
                String Title = null;
                try {
                    Title = responseObject.getString("Title");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Year = null;
                try {
                    Year = responseObject.getString("Year");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Runtime = null;
                try {
                    Runtime = responseObject.getString("Runtime");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Director = null;
                try {
                    Director = responseObject.getString("Director");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Actors = null;
                try {
                    Actors = responseObject.getString("Actors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Plot = null;
                try {
                    Plot = responseObject.getString("Plot");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Genre = null;
                try {
                    Genre = responseObject.getString("Genre");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Poster = null;
                try {
                    Poster = responseObject.getString("Poster");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                theMovie theMovie = new theMovie(Title, Actors, Director, Genre, Plot, Year, Runtime, Poster," ");
                //add to the list:
                dbHandler.insert(theMovie);
                Log.d(" ", "the movie"+theMovie.getActors());

            }

            dialog.dismiss();
            refreshTheOutput();
            //notidy adapter:

        }






    }
    private void refreshTheOutput() {
        Log.d(" ", "onRefreshClick");


        Cursor oldCursor = adapter.getCursor();

        Cursor newCursor = dbHandler.queryAll();
        startManagingCursor(newCursor);

        adapter.changeCursor(newCursor);
        Log.d(" ", "dbHandler");
        stopManagingCursor(oldCursor);
        oldCursor.close();
    }


}
