package com.example.mymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by tameram on 13/5/2019.
 */
public class DbHandlerToSearch {
    final static String TAG = "DbManager";
    private DbOpenHelperToSearch dbOpenHelper;

    public DbHandlerToSearch(Context context) {
        dbOpenHelper = new DbOpenHelperToSearch(context);
    }

    public void insert(theMovie themovie) {
        //get a WRITABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // the values to insert:
        ContentValues values = new ContentValues();
        values.put(DbConstantsToSerach.MOVIE_TITLE, themovie.getTitle());
        values.put(DbConstantsToSerach.MOVIE_ACTORS, themovie.getActors());
        values.put(DbConstantsToSerach.MOVIE_DIRECTOR, themovie.getDirector());
        values.put(DbConstantsToSerach.MOVIE_GENRE, themovie.getGenre());
        values.put(DbConstantsToSerach.MOVIE_PLOT, themovie.getPlot());
        values.put(DbConstantsToSerach.MOVIE_RUNTIME, themovie.getRunTime());
        values.put(DbConstantsToSerach.MOVIE_YEAR, themovie.getYear());
        values.put(DbConstantsToSerach.MOVIE_THEIMAGE, themovie.getTheImage());
        // values.put(DbConstantsToSerach.MOVIE_IFWATCHED,themovie.isIfWatched());


        //insert
        //db.insertOrThrow(table, nullColumnHack, values)
        db.insertOrThrow(DbConstantsToSerach.TABLE_NAME_SearchMovie, null, values);

        // writable databases must be closed!
        db.close();
    }

    public void update(theMovie themovie) {
        //get a WRITABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // the values to insert:
        ContentValues values = new ContentValues();
        values.put(DbConstantsToSerach.MOVIE_TITLE, themovie.getTitle());
        values.put(DbConstantsToSerach.MOVIE_ACTORS, themovie.getActors());
        values.put(DbConstantsToSerach.MOVIE_DIRECTOR, themovie.getDirector());
        values.put(DbConstantsToSerach.MOVIE_GENRE, themovie.getGenre());
        values.put(DbConstantsToSerach.MOVIE_PLOT, themovie.getPlot());
        values.put(DbConstantsToSerach.MOVIE_RUNTIME, themovie.getRunTime());
        values.put(DbConstantsToSerach.MOVIE_YEAR, themovie.getYear());
        values.put(DbConstantsToSerach.MOVIE_THEIMAGE, themovie.getTheImage());
        values.put(DbConstantsToSerach.MOVIE_IFWATCHED, themovie.isIfWatched());

        //insert
        //db.update(table, values, whereClause, whereArgs)
        db.updateWithOnConflict(DbConstantsToSerach.TABLE_NAME_SearchMovie, values, DbConstantsToSerach.MOVIE_ID + " =?", new String[]{String.valueOf(themovie.getId())}, SQLiteDatabase.CONFLICT_REPLACE);

        // writable databases must be closed!
        db.close();
    }

    public void delete(long id) {
        //get a WRITABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        //delete
        //db.delete(table, whereClause, whereArgs)
        db.delete(DbConstantsToSerach.TABLE_NAME_SearchMovie, DbConstantsToSerach.MOVIE_ID + "=?", new String[]{String.valueOf(id)});

        // writable databases must be closed!
        db.close();
    }

    public Cursor queryAll() {
        //get a READABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //query all:
        //db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        Cursor cursor = db.query(DbConstantsToSerach.TABLE_NAME_SearchMovie, null, null, null, null, null, DbConstantsToSerach.MOVIE_ID);
        return cursor;
    }

    public theMovie query(long id) {
        //get a READABLE database instance:
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //query one (where _id=id):
        //db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        Cursor cursor = db.query(DbConstantsToSerach.TABLE_NAME_SearchMovie, null, DbConstantsToSerach.MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Log.d(TAG, "count = " + cursor.getCount());

        theMovie theMovie = null; //for now

        //get the data:
        if (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_TITLE));
            String actors = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_ACTORS));
            String director = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_DIRECTOR));
            String plot = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_PLOT));
            String theImage = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_THEIMAGE));
            String year = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_YEAR));
            String runTime = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_RUNTIME));
            String ifWatched = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_THEIMAGE));
            String genre = cursor.getString(cursor.getColumnIndex(DbConstantsToSerach.MOVIE_GENRE));


            theMovie = new theMovie(title, actors, director, genre, plot, year, runTime, theImage, ifWatched);
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

    }
}
