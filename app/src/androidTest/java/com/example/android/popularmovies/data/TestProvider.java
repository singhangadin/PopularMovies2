package com.example.android.popularmovies.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.test.AndroidTestCase;

/**<p>
 * Created by Angad Singh on 18-06-2016.
 * </p>
 */
public class TestProvider extends AndroidTestCase{
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MoviesContract.FavouritesEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                MoviesContract.PopularEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                MoviesContract.RatedEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.FavouritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Favourites table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MoviesContract.PopularEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Popular table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                MoviesContract.RatedEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Rated table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MoviesProvider.class.getName());
        try
        {   ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: MoviesProvider registered with authority: " +
                    providerInfo.authority +
                    " instead of authority: " +
                    MoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MoviesContract.CONTENT_AUTHORITY);
        }
        catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: MoviesProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(MoviesContract.FavouritesEntry.CONTENT_URI);
        assertEquals("Error: the FavouritesEntry CONTENT_URI should return FavouritesEntry.CONTENT_TYPE",MoviesContract.FavouritesEntry.CONTENT_TYPE, type);
        type = mContext.getContentResolver().getType(MoviesContract.RatedEntry.CONTENT_URI);
        assertEquals("Error: the RatedEntry CONTENT_URI should return FavouritesEntry.CONTENT_TYPE",MoviesContract.RatedEntry.CONTENT_TYPE, type);
        type = mContext.getContentResolver().getType(MoviesContract.PopularEntry.CONTENT_URI);
        assertEquals("Error: the PopularEntry CONTENT_URI should return FavouritesEntry.CONTENT_TYPE",MoviesContract.PopularEntry.CONTENT_TYPE, type);

        String testID = "94074";
        type = mContext.getContentResolver().getType(MoviesContract.FavouritesEntry.buildFavouriteIDUri(testID));
        assertEquals("Error: the FavouritesEntry CONTENT_URI with location should return FavouritesEntry.CONTENT_ITEM_TYPE", MoviesContract.FavouritesEntry.CONTENT_ITEM_TYPE, type);
        type = mContext.getContentResolver().getType(MoviesContract.RatedEntry.buildRatedIDUri(testID));
        assertEquals("Error: the RatedEntry CONTENT_URI with location should return RatedEntry.CONTENT_ITEM_TYPE", MoviesContract.RatedEntry.CONTENT_ITEM_TYPE, type);
        type = mContext.getContentResolver().getType(MoviesContract.PopularEntry.buildPopularIDUri(testID));
        assertEquals("Error: the PopularEntry CONTENT_URI with location should return PopularEntry.CONTENT_ITEM_TYPE", MoviesContract.PopularEntry.CONTENT_ITEM_TYPE, type);
    }
}
