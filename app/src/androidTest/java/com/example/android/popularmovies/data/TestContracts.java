/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestContracts extends AndroidTestCase {
    private static final String TEST_ITEM_ID = "12356";

    public void testBuildFavourite() {
        Uri favouriteUri = MoviesContract.FavouritesEntry.buildFavouriteIDUri(TEST_ITEM_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildFavouriteIDUri in " + "MoviesContract.", favouriteUri);
        assertEquals("Error: Movies favourites not properly appended to the end of the Uri", TEST_ITEM_ID, favouriteUri.getLastPathSegment());
        assertEquals("Error: Movies favourites Uri doesn't match our expected result", favouriteUri.toString(), "content://com.example.android.popularmovies/favourite/12356");
    }

    public void testBuildPopular() {
        Uri popularUri = MoviesContract.PopularEntry.buildPopularIDUri(TEST_ITEM_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildFavouriteIDUri in " + "MoviesContract.", popularUri);
        assertEquals("Error: Movies popular not properly appended to the end of the Uri", TEST_ITEM_ID, popularUri.getLastPathSegment());
        assertEquals("Error: Movies popular Uri doesn't match our expected result", popularUri.toString(), "content://com.example.android.popularmovies/popular/12356");
    }

    public void testBuildRated() {
        Uri ratedUri = MoviesContract.RatedEntry.buildRatedIDUri(TEST_ITEM_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildFavouriteIDUri in " + "MoviesContract.", ratedUri);
        assertEquals("Error: Movies rated not properly appended to the end of the Uri", TEST_ITEM_ID, ratedUri.getLastPathSegment());
        assertEquals("Error: Movies rated Uri doesn't match our expected result", ratedUri.toString(), "content://com.example.android.popularmovies/rated/12356");
    }
}
