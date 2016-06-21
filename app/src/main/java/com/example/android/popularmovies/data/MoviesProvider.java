package com.example.android.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**<p>
 * Created by Angad Singh on 17-06-2016.
 * </p>
 */
public class MoviesProvider extends ContentProvider{
    private MoviesDBHelper mDBHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int FAVOURITES = 100;
    public static final int FAVOURITES_WITH_ID = 101;
    public static final int MOST_POPULAR = 200;
    public static final int MOST_POPULAR_WITH_ID = 201;
    public static final int HIGHEST_RATED = 300;
    public static final int HIGHEST_RATED_WITH_ID = 301;

    private static final String sFavouriteSelection = MoviesContract.FavouritesEntry.TABLE_NAME
            + "."
            + MoviesContract.FavouritesEntry.COLUMN_ID
            + " = ? ";

    private static final String sRatedSelection = MoviesContract.RatedEntry.TABLE_NAME
            + "."
            + MoviesContract.FavouritesEntry.COLUMN_ID
            + " = ? ";

    private static final String sPopularSelection = MoviesContract.PopularEntry.TABLE_NAME
            + "."
            + MoviesContract.PopularEntry.COLUMN_ID
            + " = ? ";

    public static UriMatcher buildUriMatcher()
    {   final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.PATH_FAVOURITE,FAVOURITES);
        matcher.addURI(authority, MoviesContract.PATH_FAVOURITE+"/*",FAVOURITES_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR,MOST_POPULAR);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR+"/*",MOST_POPULAR_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_RATED,HIGHEST_RATED);
        matcher.addURI(authority, MoviesContract.PATH_RATED+"/*",HIGHEST_RATED_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case HIGHEST_RATED: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MoviesContract.RatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOST_POPULAR: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MoviesContract.PopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case FAVOURITES: {
                retCursor = mDBHelper.getReadableDatabase().query(
                        MoviesContract.FavouritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case HIGHEST_RATED_WITH_ID: {
                retCursor=getRatedByID(uri, projection, sortOrder);
                break;
            }

            case MOST_POPULAR_WITH_ID: {
                retCursor=getPopularByID(uri, projection, sortOrder);
                break;
            }

            case FAVOURITES_WITH_ID: {
                retCursor=getFavouriteByID(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getFavouriteByID(Uri uri, String[] projection, String sortOrder) {
        String favouriteID = MoviesContract.FavouritesEntry.getFavouriteIDFromUri(uri);
        String[] selectionArgs;
        String selection;
            selection = sFavouriteSelection;
            selectionArgs = new String[]{favouriteID};

        return mDBHelper.getReadableDatabase().query(
                MoviesContract.FavouritesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getRatedByID(Uri uri, String[] projection, String sortOrder) {
        String ratedID = MoviesContract.RatedEntry.getRatedIDFromUri(uri);
        String[] selectionArgs;
        String selection;
            selection = sRatedSelection;
            selectionArgs = new String[]{ratedID};

        return mDBHelper.getReadableDatabase().query(
                MoviesContract.RatedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPopularByID(Uri uri, String[] projection, String sortOrder) {
        String popularID = MoviesContract.PopularEntry.getPopularIDFromUri(uri);
        String[] selectionArgs;
        String selection;
            selection = sPopularSelection;
            selectionArgs = new String[]{popularID};

        return mDBHelper.getReadableDatabase().query(
                MoviesContract.PopularEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITES:
                return MoviesContract.FavouritesEntry.CONTENT_TYPE;

            case FAVOURITES_WITH_ID:
                return MoviesContract.FavouritesEntry.CONTENT_ITEM_TYPE;

            case HIGHEST_RATED:
                return MoviesContract.RatedEntry.CONTENT_TYPE;

            case HIGHEST_RATED_WITH_ID:
                return MoviesContract.RatedEntry.CONTENT_ITEM_TYPE;

            case MOST_POPULAR:
                return MoviesContract.PopularEntry.CONTENT_TYPE;

            case MOST_POPULAR_WITH_ID:
                return MoviesContract.PopularEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVOURITES: {
                long _id = db.insert(MoviesContract.FavouritesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.FavouritesEntry.buildFavouriteUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case HIGHEST_RATED: {
                long _id = db.insert(MoviesContract.RatedEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.RatedEntry.buildRatedUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOST_POPULAR: {
                long _id = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.PopularEntry.buildPopularUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }

            case MOST_POPULAR_WITH_ID:
            case HIGHEST_RATED_WITH_ID:
            {
                long _id = db.insert(MoviesContract.FavouritesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesContract.FavouritesEntry.buildFavouriteUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case FAVOURITES:
                rowsDeleted = db.delete(
                        MoviesContract.FavouritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HIGHEST_RATED:
                rowsDeleted = db.delete(
                        MoviesContract.RatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOST_POPULAR:
                rowsDeleted = db.delete(
                        MoviesContract.PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case FAVOURITES_WITH_ID:
                rowsDeleted=db.delete(
                        MoviesContract.FavouritesEntry.TABLE_NAME,sFavouriteSelection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case FAVOURITES:
                rowsUpdated = db.update(MoviesContract.FavouritesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case HIGHEST_RATED:
                rowsUpdated = db.update(MoviesContract.RatedEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case MOST_POPULAR:
                rowsUpdated = db.update(MoviesContract.PopularEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case HIGHEST_RATED_WITH_ID:
                rowsUpdated = db.update(MoviesContract.RatedEntry.TABLE_NAME, values, sRatedSelection,
                        selectionArgs);
                break;

            case MOST_POPULAR_WITH_ID:
                rowsUpdated = db.update(MoviesContract.PopularEntry.TABLE_NAME, values, sPopularSelection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOST_POPULAR:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case HIGHEST_RATED:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.RatedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case FAVOURITES:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.FavouritesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }
}
