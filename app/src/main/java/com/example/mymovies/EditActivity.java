package com.example.mymovies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EditActivity extends Activity implements View.OnClickListener {


    private static final int RESULT_SHARE_SMS = 4;
    private static final int RESULT_SHARE_MAIL = 5;
    private theMovie theMovie;
    private DbHandlerToFavor dbHandler;
    private SimpleCursorAdapter adapter;
    private DatabaseReference reff ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbHandler = new DbHandlerToFavor(EditActivity.this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Liner);
        Button btn_Out = (Button) findViewById(R.id.btn_Out);
        Button btn_Add = (Button) findViewById(R.id.btn_Add);
        reff = FirebaseDatabase.getInstance().getReference().child("movies");
        btn_Out.setOnClickListener(this);
        btn_Add.setOnClickListener(this);
        registerForContextMenu(linearLayout);

    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_EditMovie:
                toEditMovie();
                break;

            case R.id.menu_Delete:
                // do Something
                break;
            case R.id.menu_ShareTrailer:
                // do Something
                break;

            case R.id.menu_ShareMovieSMS:
                theDialog(RESULT_SHARE_SMS);
                break;
            case R.id.menu_ShareMovieMail:
                theDialog(RESULT_SHARE_MAIL);
                break;


        }
        return true;
    }


    private void theDialog(int resultLoad) {
        if (resultLoad==RESULT_SHARE_SMS)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            alert.setMessage("Enter URL / Enter SMS / Enter Mail");


            alert.setView(edittext);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String theNum = edittext.getText().toString();
                    String whatIWantToShare="This Film "+ theMovie.getTitle()+" is Wanderful";

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(theNum, null, whatIWantToShare, null, null);
                        Toast.makeText(getApplicationContext(), "SMS Sent!",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later!",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }




                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
        }



        if (resultLoad==RESULT_SHARE_MAIL)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            alert.setMessage("Enter URL / Enter SMS / Enter Mail");


            alert.setView(edittext);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String e_mail = edittext.getText().toString();
                    String whatIWantToShare="This Film "+ theMovie.getTitle()+" is Wanderful";
                    Intent intent ;
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, e_mail);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Film");
                    intent.putExtra(Intent.EXTRA_TEXT, whatIWantToShare);
                    startActivity(intent);




                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
        }



    }


    private void toEditMovie() {

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
    }


    @Override
    protected void onResume() {

        String title = null, actors = null,director = null,genre = null,plot = null;
        String year = null, runTime = null;
        boolean ifWatched ;/////not now

        String outImage = null;///////////////// to set normal image
        TextView tv_Title = (TextView) findViewById(R.id.tv_Title);
        TextView tv_Actors = (TextView) findViewById(R.id.tv_Actors);
        TextView tv_Plot = (TextView) findViewById(R.id.tv_Plot);
        TextView tv_Director = (TextView) findViewById(R.id.tv_Director);
        TextView tv_Year = (TextView) findViewById(R.id.tv_Year);
        TextView tv_RunTime = (TextView) findViewById(R.id.tv_RunTime);
        TextView tv_Genre = (TextView) findViewById(R.id.tv_Genre);

        Intent callingIntent = getIntent();

        if (callingIntent.hasExtra("theImage")) {

            outImage = callingIntent.getStringExtra("theImage");

        }



        new DownloadImageTask()
                .execute(outImage);


        super.onResume();
        // get the info from the intent:

        String output="";
        String outputInt="";
        if (callingIntent.hasExtra("Title")) {
            output += "Title : ";
            output += callingIntent.getStringExtra("Title");
            tv_Title.setText(output);
            title = callingIntent.getStringExtra("Title");
            output="";

        }
        if (callingIntent.hasExtra("Actors")) {
            output += "Actors : ";
            output += callingIntent.getStringExtra("Actors");
            tv_Actors.setText(output);
            actors=callingIntent.getStringExtra("Actors");
            output="";

        }
        if (callingIntent.hasExtra("Genre")) {
            output += "Genre : ";
            output += callingIntent.getStringExtra("Genre");
            tv_Genre.setText(output);
            genre=callingIntent.getStringExtra("Genre");
            output="";

        }
        if (callingIntent.hasExtra("Director")) {
            output += "Director : ";
            output += callingIntent.getStringExtra("Director");
            tv_Director.setText(output);
            director=callingIntent.getStringExtra("Director");
            output="";
        }
        if (callingIntent.hasExtra("Year")) {
            output += "Year : ";
            outputInt = callingIntent.getStringExtra("Year");
            output += outputInt+"";
            tv_Year.setText(output);
            year =callingIntent.getStringExtra("Year");

            output="";
            outputInt="";
        }
        if (callingIntent.hasExtra("RunTime")) {
            output += "RunTime : ";
            outputInt=callingIntent.getStringExtra("RunTime");
            output += outputInt+"";
            output +=" min";
            tv_RunTime.setText(output);
            runTime=callingIntent.getStringExtra("RunTime");
            output="";
        }
        if (callingIntent.hasExtra("Plot")) {
            output += " ";
            output += callingIntent.getStringExtra("Plot");
            tv_Plot.setText(output);
            plot=callingIntent.getStringExtra("Plot");
            output="";
        }

        theMovie = new theMovie(title,actors,director,genre,plot,year,runTime,outImage,"");


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_Add :

                dbHandler.insert(theMovie);
                reff.push().setValue(theMovie);
                Intent intent = new Intent(this,MainActivity.class);

                intent.putExtra("Title",theMovie.getTitle());
                intent.putExtra("Actors",theMovie.getActors());
                intent.putExtra("Genre",theMovie.getGenre());
                intent.putExtra("Director",theMovie.getDirector());
                intent.putExtra("Year",theMovie.getYear());
                intent.putExtra("RunTime",theMovie.getRunTime());
                intent.putExtra("Plot",theMovie.getPlot());
                intent.putExtra("theImage",theMovie.getTheImage());
                intent.putExtra("isItComeFromEdit",true);

                startActivity(intent);
                finish();
                break;
            case R.id.btn_Out :
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
                finish();
                break;
        }




    }


    class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        //we'll show a progress dialog while downloading :
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // on the UI thread:

            // start the progress dialog
            progressDialog = new ProgressDialog(EditActivity.this);
            progressDialog.setTitle("Downloading..");
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //in the background:

            //get the address from the params:
            String address = params[0];

            HttpURLConnection connection = null;
            InputStream stream = null;
            ByteArrayOutputStream outputStream = null;

            //the bitmap will go here:
            Bitmap b = null;


            try {
                // build the URL:
                URL url = new URL(address);
                // open a connection:
                connection = (HttpURLConnection) url.openConnection();

                // check the connection response code:
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    // not good..
                    return null;
                }

                // the input stream:
                stream = connection.getInputStream();

                // get the length:
                int length = connection.getContentLength();
                // tell the progress dialog the length:
                // this CAN (!!) be modified outside the UI thread !!!
                progressDialog.setMax(length);

                // a stream to hold the read bytes.
                // (like the StringBuilder we used before)
                outputStream = new ByteArrayOutputStream();

                // a byte buffer for reading the stream in 1024 bytes chunks:
                byte[] buffer = new byte[1024];

                int totalBytesRead = 0;
                int bytesRead = 0;

                //read the bytes from the stream
                while ((bytesRead = stream.read(buffer, 0, buffer.length)) != -1) {
                    totalBytesRead += bytesRead;
                    outputStream.write(buffer, 0, bytesRead);
                }

                // flush the output stream - write all the pending bytes in its
                // internal buffer.
                outputStream.flush();

                // get a byte array out of the outputStream
                // theses are the bitmap bytes
                byte[] imageBytes = outputStream.toByteArray();

                // use the BitmapFactory to convert it to a bitmap
                b = BitmapFactory.decodeByteArray(imageBytes, 0, length);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    // close connection:
                    connection.disconnect();
                }
                if (outputStream != null) {
                    try {
                        // close output stream:
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return b;
        }

        protected void onPostExecute(Bitmap result) {
            //back on the UI thread

            //close the progress dialog
            progressDialog.dismiss();

            ImageView imageView = (ImageView) findViewById(R.id.imageView1);

            if (result == null) {
                // no image loaded - display the default image
                //imageView.setImageResource(R.drawable.ic_launcher);
                Toast.makeText(EditActivity.this, "error loading image",Toast.LENGTH_LONG).show();
            } else {
                //set the image bitmap:
                imageView.setImageBitmap(result);
            }
        };
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
