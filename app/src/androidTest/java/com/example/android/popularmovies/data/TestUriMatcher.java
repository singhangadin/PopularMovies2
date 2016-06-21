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

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final long TEST_ID = 14L;

    private static final Uri TEST_FAVOURITE_DIR = MoviesContract.FavouritesEntry.CONTENT_URI;
    private static final Uri TEST_FAVOURITE_WITH_ID_ITEM = MoviesContract.FavouritesEntry.buildFavouriteIDUri(String.valueOf(TEST_ID));
    private static final Uri TEST_RATED_DIR = MoviesContract.RatedEntry.CONTENT_URI;
    private static final Uri TEST_RATED_WITH_ID_ITEM = MoviesContract.RatedEntry.buildRatedIDUri(String.valueOf(TEST_ID));
    private static final Uri TEST_POPULAR_DIR = MoviesContract.PopularEntry.CONTENT_URI;
    private static final Uri TEST_POPULAR_WITH_ID_ITEM = MoviesContract.PopularEntry.buildPopularIDUri(String.valueOf(TEST_ID));

    public void testUriMatcher() {
        UriMatcher testMatcher = MoviesProvider.buildUriMatcher();
        assertEquals("Error: The FAVOURITE URI was matched incorrectly.", testMatcher.match(TEST_FAVOURITE_DIR), MoviesProvider.FAVOURITES);
        assertEquals("Error: The FAVOURITE WITH ID URI was matched incorrectly.", testMatcher.match(TEST_FAVOURITE_WITH_ID_ITEM), MoviesProvider.FAVOURITES_WITH_ID);
        assertEquals("Error: The RATED URI was matched incorrectly.", testMatcher.match(TEST_RATED_DIR), MoviesProvider.HIGHEST_RATED);
        assertEquals("Error: The RATED WITH ID URI was matched incorrectly.", testMatcher.match(TEST_RATED_WITH_ID_ITEM), MoviesProvider.HIGHEST_RATED_WITH_ID);
        assertEquals("Error: The POPULAR URI was matched incorrectly.", testMatcher.match(TEST_POPULAR_DIR), MoviesProvider.MOST_POPULAR);
        assertEquals("Error: The POPULAR WITH ID URI was matched incorrectly.", testMatcher.match(TEST_POPULAR_WITH_ID_ITEM), MoviesProvider.MOST_POPULAR_WITH_ID);
    }
}
