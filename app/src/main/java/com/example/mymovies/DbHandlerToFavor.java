package com.example.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by tameram on 13/5/2019.
 */
public class DbHandlerToFavor {
    final static String TAG = "DbManager";
    private DbOpenHelperToFavor dbOpenHelper;
    public DbHandlerToFavor(Context context) {
        dbOpenHelper = new DbOpenHelperToFavor(context);
    }

    public void insert(theMovie themovie){
        //get a WRITABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // the values to insert:
        ContentValues values = new ContentValues();
        values.put(DbConstantsToFavor.MOVIE_TITLE, themovie.getTitle());
        values.put(DbConstantsToFavor.MOVIE_ACTORS,themovie.getActors());
        values.put(DbConstantsToFavor.MOVIE_DIRECTOR,themovie.getDirector());
        values.put(DbConstantsToFavor.MOVIE_GENRE,themovie.getGenre());
       values.put(DbConstantsToFavor.MOVIE_PLOT,themovie.getPlot());
        values.put(DbConstantsToFavor.MOVIE_RUNTIME,themovie.getRunTime());
        values.put(DbConstantsToFavor.MOVIE_YEAR,themovie.getYear());
        values.put(DbConstantsToFavor.MOVIE_THEIMAGE,themovie.getTheImage());
       // values.put(DbConstantsToFavor.MOVIE_IFWATCHED,themovie.isIfWatched());




        //insert
        //db.insertOrThrow(table, nullColumnHack, values)
        db.insertOrThrow(DbConstantsToFavor.TABLE_NAME_favorMovies, null, values);

        // writable databases must be closed!
        db.close();
    }

    public void update(theMovie themovie){
        //get a WRITABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // the values to insert:
        ContentValues values = new ContentValues();
        values.put(DbConstantsToFavor.MOVIE_TITLE, themovie.getTitle());
        values.put(DbConstantsToFavor.MOVIE_ACTORS,themovie.getActors());
        values.put(DbConstantsToFavor.MOVIE_DIRECTOR,themovie.getDirector());
        values.put(DbConstantsToFavor.MOVIE_GENRE,themovie.getGenre());
        values.put(DbConstantsToFavor.MOVIE_PLOT,themovie.getPlot());
        values.put(DbConstantsToFavor.MOVIE_RUNTIME,themovie.getRunTime());
        values.put(DbConstantsToFavor.MOVIE_YEAR,themovie.getYear());
        values.put(DbConstantsToFavor.MOVIE_THEIMAGE,themovie.getTheImage());
        values.put(DbConstantsToFavor.MOVIE_IFWATCHED,themovie.isIfWatched());

        //insert
        //db.update(table, values, whereClause, whereArgs)
        db.updateWithOnConflict(DbConstantsToFavor.TABLE_NAME_favorMovies, values, DbConstantsToFavor.MOVIE_ID+ " =?",new String[]{String.valueOf(themovie.getId())},SQLiteDatabase.CONFLICT_REPLACE);

        // writable databases must be closed!
        db.close();
    }
    public void delete(long id) {
        //get a WRITABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        //delete
        //db.delete(table, whereClause, whereArgs)
        db.delete(DbConstantsToFavor.TABLE_NAME_favorMovies, DbConstantsToFavor.MOVIE_ID+"=?",new String[]{String.valueOf(id)});

        // writable databases must be closed!
        db.close();
    }
    public Cursor queryAll(){
        //get a READABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //query all:
        //DbConstantsToFavor.MOVIE_TITLE + " COLLATE LOCALIZED ASC"
        //db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        Cursor cursor = db.query(DbConstantsToFavor.TABLE_NAME_favorMovies, null, null, null, null, null, DbConstantsToFavor.MOVIE_ID);
        return cursor ;
    }
    public theMovie query(long id){
        //get a READABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //query one (where _id=id):
        //db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        Cursor cursor = db.query(DbConstantsToFavor.TABLE_NAME_favorMovies, null, DbConstantsToFavor.MOVIE_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);

        Log.d(TAG,"count = " + cursor.getCount());

        theMovie theMovie = null; //for now

        //get the data:
        if (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_TITLE));
            String actors = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_ACTORS));
            String director = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_DIRECTOR));
            String plot = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_PLOT));
            String theImage = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_THEIMAGE));
            String year = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_YEAR));
            String runTime = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_RUNTIME));
            String ifWatched =cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_THEIMAGE));
            String genre = cursor.getString(cursor.getColumnIndex(DbConstantsToFavor.MOVIE_GENRE));


            theMovie = new theMovie(title, actors, director,genre,plot,year,runTime,theImage,ifWatched);
            //remember the id :
            theMovie.setId(id);

        }

        //cursors must be closed!
        cursor.close();

        //return the student
        return theMovie;
    }

    public void removeAll()//////////////////////
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete(DbConstantsToFavor.TABLE_NAME_favorMovies,null,null);
        
    }

}
