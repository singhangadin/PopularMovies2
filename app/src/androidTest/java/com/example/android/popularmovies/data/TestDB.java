package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**<p>
 * Created by Angad Singh on 18-06-2016.
 *</p>
 */
public class TestDB extends AndroidTestCase {

    void deleteTheDatabase() {
        mContext.deleteDatabase(MoviesDBHelper.DATABASE_NAME);
    }
    public void setUp() {
        deleteTheDatabase();
    }
    public void testCreateDb() throws Throwable
    {   final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(MoviesContract.RatedEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.PopularEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.FavouritesEntry.TABLE_NAME);
        mContext.deleteDatabase(MoviesDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MoviesDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        do
        {   tableNameHashSet.remove(c.getString(0));
        }
        while( c.moveToNext() );
        assertTrue("Error: Your database was created without 3 tables",tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.FavouritesEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for favourites table information.", c.moveToFirst());

        final HashSet<String> favouriteColumnHashSet = new HashSet<>();
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry._ID);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_ID);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_TITLE);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_POSTER);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_RELEASE_DATE);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_VOTE_AVERAGE);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_OVERVIEW);
        favouriteColumnHashSet.add(MoviesContract.FavouritesEntry.COLUMN_TRAILER);
        int columnNameIndex = c.getColumnIndex("name");
        do
        {   String columnName = c.getString(columnNameIndex);
            favouriteColumnHashSet.remove(columnName);
        }
        while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required favourites entry columns",favouriteColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.PopularEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for favourites table information.", c.moveToFirst());

        final HashSet<String> popularColumnHashSet = new HashSet<>();
        popularColumnHashSet.add(MoviesContract.PopularEntry._ID);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_ID);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_TITLE);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_POSTER);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_OVERVIEW);
        popularColumnHashSet.add(MoviesContract.PopularEntry.COLUMN_TRAILER);
        columnNameIndex = c.getColumnIndex("name");
        do
        {   String columnName = c.getString(columnNameIndex);
            popularColumnHashSet.remove(columnName);
        }
        while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required popular entry columns",popularColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.RatedEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for favourites table information.", c.moveToFirst());

        final HashSet<String> ratedColumnHashSet = new HashSet<>();
        ratedColumnHashSet.add(MoviesContract.RatedEntry._ID);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_ID);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_TITLE);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_POSTER);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_RELEASE_DATE);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_VOTE_AVERAGE);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_OVERVIEW);
        ratedColumnHashSet.add(MoviesContract.RatedEntry.COLUMN_TRAILER);
        columnNameIndex = c.getColumnIndex("name");
        do
        {   String columnName = c.getString(columnNameIndex);
            ratedColumnHashSet.remove(columnName);
        }
        while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required rated entry columns",ratedColumnHashSet.isEmpty());
        c.close();
        db.close();
    }

    public void testInsertFavourites() {
        MoviesDBHelper dbHelper = new MoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long locationRowId;
        ContentValues testValues = TestUtilities.createFavouriteValues(1);
        locationRowId = db.insert(MoviesContract.FavouritesEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId != -1);
        Cursor cursor = db.query(
                MoviesContract.FavouritesEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue( "Error: No Records returned from Favourites query", cursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("Error: Favourites Query Validation Failed", cursor, testValues);
        assertFalse( "Error: More than one record returned from favourite query", cursor.moveToNext() );
        cursor.close();
        db.close();
    }

    public void testInsertRated() {
        MoviesDBHelper dbHelper = new MoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long locationRowId;
        ContentValues testValues = TestUtilities.createRatedValues(1);
        locationRowId = db.insert(MoviesContract.RatedEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId != -1);
        Cursor cursor = db.query(
                MoviesContract.RatedEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue( "Error: No Records returned from Rated query", cursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("Error: Rated Query Validation Failed", cursor, testValues);
        assertFalse( "Error: More than one record returned from Rated query", cursor.moveToNext() );
        cursor.close();
        db.close();
    }

    public void testInsertPopular() {
        MoviesDBHelper dbHelper = new MoviesDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long locationRowId;
        ContentValues testValues = TestUtilities.createPopularValues(1);
        locationRowId = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId != -1);
        Cursor cursor = db.query(
                MoviesContract.PopularEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        assertTrue( "Error: No Records returned from Popular query", cursor.moveToFirst() );
        TestUtilities.validateCurrentRecord("Error: Popular Query Validation Failed", cursor, testValues);
        assertFalse( "Error: More than one record returned from Popular query", cursor.moveToNext() );
        cursor.close();
        db.close();
    }
}
