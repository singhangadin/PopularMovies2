package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**<p>
 * Created by Angad Singh on 17-06-2016.
 * </p>
 */
public class MoviesDBHelper extends SQLiteOpenHelper
{   private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RATED_TABLE="CREATE TABLE " + MoviesContract.RatedEntry.TABLE_NAME + " (" +
                MoviesContract.RatedEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.RatedEntry.COLUMN_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesContract.RatedEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.RatedEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.RatedEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.RatedEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL ," +
                MoviesContract.RatedEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.RatedEntry.COLUMN_TRAILER + " TEXT NOT NULL, " +
                MoviesContract.RatedEntry.COLUMN_REVIEWS + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_POPULAR_TABLE="CREATE TABLE " + MoviesContract.PopularEntry.TABLE_NAME + " (" +
                MoviesContract.PopularEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.PopularEntry.COLUMN_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL ," +
                MoviesContract.PopularEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_TRAILER + " TEXT NOT NULL, " +
                MoviesContract.PopularEntry.COLUMN_REVIEWS + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_FAVOURITES_TABLE="CREATE TABLE " + MoviesContract.FavouritesEntry.TABLE_NAME + " (" +
                MoviesContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.FavouritesEntry.COLUMN_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesContract.FavouritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.FavouritesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.FavouritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.FavouritesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL ," +
                MoviesContract.FavouritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.FavouritesEntry.COLUMN_TRAILER + " TEXT NOT NULL, " +
                MoviesContract.FavouritesEntry.COLUMN_REVIEWS + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_RATED_TABLE);
        db.execSQL(SQL_CREATE_POPULAR_TABLE);
        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.PopularEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.RatedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavouritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
