package com.example.mymovies;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.R;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class EditActivity2 extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final int theFirstYear = 1900;
    private final int theLastYear = 2018;
    private static final int CAMERA_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private theMovie theMovie;
    private static final int RESULT_LOAD_URL = 3;
    ImageButton im ;
    EditText editTextForTitle , editTextForRunTime , editTextForActors , editTextForDirector , editTextForGenre ;
    String editTextForTitleStr , editTextForRunTimeStr  , editTextForActorsStr  , editTextForDirectorStr  , editTextForGenreStr  ;
    ArrayList<Integer> years;
    ArrayAdapter<Integer> adapter;
    private DbHandlerToFavor dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);
        im = (ImageButton) findViewById(R.id.theImageButton);
        dbHandler = new DbHandlerToFavor(EditActivity2.this);
        // Button btn_Date = (Button) findViewById(R.id.btn_Date);

        years = new ArrayList<>();
        showDate();
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_dropdown_item_1line, years
        );
        Spinner spn = (Spinner) findViewById(R.id.spinner);

        spn.setAdapter(adapter);


        spn.setOnItemSelectedListener(this);

        editTextForTitle = (EditText)findViewById(R.id.editTextForTitle);
        editTextForRunTime = (EditText)findViewById(R.id.editTextForRunTime);
        editTextForActors = (EditText)findViewById(R.id.editTextForActors);
        editTextForDirector = (EditText)findViewById(R.id.editTextForDirector);
        editTextForGenre = (EditText)findViewById(R.id.editTextForGenre);




        Button btn_out = (Button) findViewById(R.id.btn_Out1);

        btn_out.setOnClickListener(this);
        Button btn_Add = (Button) findViewById(R.id.btn_Add);

        btn_Add.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.theImageButton:

                Toast.makeText(this, "Dialog", Toast.LENGTH_SHORT).show();
                openOptionsMenu();


                break;

            case R.id.btn_Out1:

                finish();
                break;
            case R.id.btn_Add:
                editTextForTitleStr = editTextForTitle.getText().toString();
                editTextForRunTimeStr = editTextForRunTime.getText().toString();
                editTextForActorsStr = editTextForActors.getText().toString();
                editTextForDirectorStr = editTextForDirector.getText().toString();
                editTextForGenreStr = editTextForGenre.getText().toString();
                theMovie = new theMovie(editTextForTitleStr , editTextForActorsStr,editTextForDirectorStr,editTextForGenreStr,"","2019","120 min","","");
                dbHandler.insert(theMovie);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;


        }
    }

    private void showDate() {

        for (int i = theFirstYear; i < theLastYear; i++) {
            years.add(i);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        String outImage = "";

        EditText ed_Title = (EditText) findViewById(R.id.editTextForTitle);
        EditText ed_Actors = (EditText) findViewById(R.id.editTextForActors);
        EditText ed_Plot = (EditText) findViewById(R.id.editTextForPoat);
        EditText ed_Director = (EditText) findViewById(R.id.editTextForDirector);
        Spinner ed_Year = (Spinner) findViewById(R.id.spinner);
        EditText ed_RunTime = (EditText) findViewById(R.id.editTextForRunTime);
        EditText ed_Genre = (EditText) findViewById(R.id.editTextForGenre);


        Intent callingIntent = getIntent();

        if (callingIntent.hasExtra("theImage")) {

            outImage = callingIntent.getStringExtra("theImage");

        }


        new DownloadImageTask()
                .execute(outImage);


        super.onResume();
        // get the info from the intent:

        String output = "";
        int outputInt = 0;
        if (callingIntent.hasExtra("Title")) {

            output += callingIntent.getStringExtra("Title");
            ed_Title.setText(output);
            output = "";

        }
        if (callingIntent.hasExtra("Actors")) {

            output += callingIntent.getStringExtra("Actors");
            ed_Actors.setText(output);
            output = "";

        }
        if (callingIntent.hasExtra("Genre")) {

            output += callingIntent.getStringExtra("Genre");
            ed_Genre.setText(output);
            output = "";

        }
        if (callingIntent.hasExtra("Director")) {

            output += callingIntent.getStringExtra("Director");
            ed_Director.setText(output);
            output = "";
        }
        if (callingIntent.hasExtra("Year")) {

            outputInt = callingIntent.getIntExtra("Year", 0);
            ed_Year.setSelection(outputInt - theFirstYear);
            outputInt = 0;
        }
        if (callingIntent.hasExtra("RunTime")) {

            outputInt = callingIntent.getIntExtra("RunTime", 0);

            output += outputInt + " min";
            ed_RunTime.setText(output);
            output = "";
        }
        if (callingIntent.hasExtra("Plot")) {

            output += callingIntent.getStringExtra("Plot");
            ed_Plot.setText(output);
            output = "";
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        //we'll show a progress dialog while downloading :
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // on the UI thread:

            // start the progress dialog
            progressDialog = new ProgressDialog(EditActivity2.this);
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

            ImageButton btn_Image = (ImageButton) findViewById(R.id.theImageButton);

            //ImageView imageView = (ImageView) findViewById(R.id.imageView1);

            if (result == null) {
                // no image loaded - display the default image
                //imageView.setImageResource(R.drawable.ic_launcher);
                Toast.makeText(EditActivity2.this, "error loading image", Toast.LENGTH_LONG).show();
            } else {
                //set the image bitmap:

                // btn_Image.setIm
                btn_Image.setImageBitmap(result);
                btn_Image.setOnClickListener(EditActivity2.this);
            }
        }

        ;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.theimagemenu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_URL:
                // do Something
                theDialog(RESULT_LOAD_URL);
                break;

            case R.id.menu_Gallery:
                // do Something
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            case R.id.menu_Camera:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);////corvet bitmap to string
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }

        return true;
    }

    private void theDialog(int resultLoad) {
        if (resultLoad==RESULT_LOAD_URL)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            alert.setMessage("Enter URL / Enter SMS / Enter Mail");


            alert.setView(edittext);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String YouEditTextValue = edittext.getText().toString();
                    DownloadImageTask downloadImageTask = new DownloadImageTask();
                    downloadImageTask.execute(YouEditTextValue);



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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            im.setImageBitmap(photo);
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                //ImageView imageView = (ImageView) findViewById(R.id.imgView);

                Bitmap bmp = null;
                try {
                    bmp = getBitmapFromUri(selectedImage);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                im.setImageBitmap(bmp);
            }

        }

    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}