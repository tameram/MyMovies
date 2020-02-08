package com.example.mymovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by tameram on 13/5/2019.
 */
public class DbOpenHelperToFavor extends SQLiteOpenHelper {
    public final static String TAG = "DbOpenHelperToFavor";

    // database file:
    public static String DB_NAME = "favor.db";
    // database version:
    public static int DB_VERSION = 3;

    public DbOpenHelperToFavor(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "creating database");

        String sql = ""
                + "CREATE TABLE " + DbConstantsToFavor.TABLE_NAME_favorMovies + " ("
                + DbConstantsToFavor.MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DbConstantsToFavor.MOVIE_TITLE + " TEXT,"
                + DbConstantsToFavor.MOVIE_ACTORS + " TEXT,"
                + DbConstantsToFavor.MOVIE_DIRECTOR + " TEXT,"
                + DbConstantsToFavor.MOVIE_GENRE + " TEXT,"
                + DbConstantsToFavor.MOVIE_PLOT + " TEXT,"
                + DbConstantsToFavor.MOVIE_YEAR + " REAL,"
                + DbConstantsToFavor.MOVIE_RUNTIME + " REAL,"
                + DbConstantsToFavor.MOVIE_THEIMAGE + " TEXT,"
                + DbConstantsToFavor.MOVIE_IFWATCHED + " TEXT"
                + ")";
        Log.d(TAG, sql);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "updateing database from " + oldVersion + " to " + newVersion);

        String sql = "DROP TABLE IF EXISTS " + DbConstantsToFavor.TABLE_NAME_favorMovies;
        Log.d(TAG, sql);

        db.execSQL(sql);

        // recreate database:
        onCreate(db);

    }
}

