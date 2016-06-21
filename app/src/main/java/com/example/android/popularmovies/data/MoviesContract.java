package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**<p>
 * Created by Angad Singh on 17-06-2016.
 * </p>
 */
public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RATED = "rated";
    public static final String PATH_POPULAR = "popular";
    public static final String PATH_FAVOURITE = "favourite";

    public static final class RatedEntry extends TableStructure implements BaseColumns
    {   public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATED).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATED;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATED;
        public static final String TABLE_NAME = "rated";

        public static Uri buildRatedUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildRatedIDUri(String ratedID) {
            return CONTENT_URI.buildUpon().appendPath(ratedID).build();
        }

        public static String getRatedIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class PopularEntry extends TableStructure implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;
        public static final String TABLE_NAME = "popular";

        public static Uri buildPopularUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPopularIDUri(String popularID) {
            return CONTENT_URI.buildUpon().appendPath(popularID).build();
        }

        public static String getPopularIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class FavouritesEntry extends TableStructure implements BaseColumns
    {   public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
        public static final String TABLE_NAME = "favourites";

        public static Uri buildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavouriteIDUri(String favouriteID) {
            return CONTENT_URI.buildUpon().appendPath(favouriteID).build();
        }

        public static String getFavouriteIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
