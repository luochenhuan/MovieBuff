package com.example.zhenhaiyu.popularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.zhenhaiyu.popularmovie.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_ADULT + " INTEGER, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " + //INTEGER. The value is a signed integer, stored in
                                                                    // 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
                MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_POPULARITY + " REAL, " +
                MovieEntry.COLUMN_FAVORITE + " INTEGER, " +
                MovieEntry.COLUMN_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_VIDEO + " INTEGER, " +
                // To assure the application have just one movie entry per movie ID,
                // it's created a UNIQUE constraint with REPLACE strategy
                "UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID +") ON CONFLICT REPLACE " +
                ");";

        // create tables:
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

