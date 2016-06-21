package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

public class TestUtilities extends AndroidTestCase {
    static final String TEST_LOCATION = "99705";
    static final long TEST_DATE = 1419033600L;

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createFavouriteValues(long locationRowId) {
        ContentValues favouriteValues = new ContentValues();
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_ID, locationRowId);
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_TITLE,"Name");
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_POSTER,"Poster");
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_RELEASE_DATE,"01-01-2000");
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_VOTE_AVERAGE, 9.9);
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_OVERVIEW, "Thats a Movie");
        favouriteValues.put(MoviesContract.FavouritesEntry.COLUMN_TRAILER, "http://www.google.com");
        return favouriteValues;
    }

    static ContentValues createPopularValues(long locationRowId) {
        ContentValues favouriteValues = new ContentValues();
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_ID, locationRowId);
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_TITLE,"Name");
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_POSTER,"Poster");
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE,"01-01-2000");
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE, 9.9);
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, "Thats a Movie");
        favouriteValues.put(MoviesContract.PopularEntry.COLUMN_TRAILER, "http://www.google.com");
        return favouriteValues;
    }

    static ContentValues createRatedValues(long locationRowId) {
        ContentValues favouriteValues = new ContentValues();
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_ID, locationRowId);
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_TITLE,"Name");
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_POSTER,"Poster");
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_RELEASE_DATE,"01-01-2000");
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_VOTE_AVERAGE, 9.9);
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_OVERVIEW, "Thats a Movie");
        favouriteValues.put(MoviesContract.RatedEntry.COLUMN_TRAILER, "http://www.google.com");
        return favouriteValues;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
