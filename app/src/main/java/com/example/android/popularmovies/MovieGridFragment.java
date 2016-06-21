package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.model.GridAdapter;
import com.example.android.popularmovies.model.SpKeys;
import com.example.android.popularmovies.sync.MoviesSyncAdapter;

public class MovieGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{   private Context context;
    private GridAdapter gridAdapter;
    private Cursor cur;
    private String sort;
    private SharedPreferences preference;
    private GridView movieGrid;
    private int mPosition = GridView.INVALID_POSITION;
    private static final int MOVIES_LOADER = 0;
    private static final String SELECTED_KEY = "selected_position";
    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.PopularEntry._ID,
            MoviesContract.PopularEntry.COLUMN_ID,
            MoviesContract.PopularEntry.COLUMN_TITLE,
            MoviesContract.PopularEntry.COLUMN_POSTER,
    };
    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_MOVIE_POSTER = 3;

    public MovieGridFragment()
    {   setHasOptionsMenu(true);
    }

    public interface Callback {
        void onItemSelected(Uri dateUri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_main, container, false);
        preference=PreferenceManager.getDefaultSharedPreferences(context);
        sort=preference.getString(SpKeys.SORT_KEY,context.getResources().getString(R.string.sort_default));
        movieGrid = (GridView) V.findViewById(R.id.movie_grid);
        Uri movieUri;
        switch(sort)
        {   case "0":
            default:    movieUri = MoviesContract.PopularEntry.CONTENT_URI;
                        break;

            case "1":   movieUri = MoviesContract.RatedEntry.CONTENT_URI;
                        break;

            case "2":   movieUri = MoviesContract.FavouritesEntry.CONTENT_URI;
                        break;
        }
        cur = getActivity().getContentResolver().query(movieUri,MOVIE_COLUMNS, null, null, null);
        gridAdapter=new GridAdapter(context,cur,0,sort);
        movieGrid.setAdapter(gridAdapter);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cur = (Cursor) parent.getItemAtPosition(position);
                String movie_id = cur.getString(COL_MOVIE_ID);
                Uri movieUri;
                switch(sort)
                {   case "0":
                    default:    movieUri = MoviesContract.PopularEntry.buildPopularIDUri(movie_id);
                        break;

                    case "1":   movieUri = MoviesContract.RatedEntry.buildRatedIDUri(movie_id);
                        break;

                    case "2":   movieUri = MoviesContract.FavouritesEntry.buildFavouriteIDUri(movie_id);
                        break;
                }
                ((Callback) getActivity()).onItemSelected(movieUri);
                mPosition = position;
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return V;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMovie() {
        MoviesSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void onPreferenceChanged() {
        updateMovie();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {   case R.id.refresh:  updateMovie();
                                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        sort=preference.getString(SpKeys.SORT_KEY,context.getResources().getString(R.string.sort_default));
        Uri movieUri;
        switch(sort)
        {   case "0":
            default:    movieUri = MoviesContract.PopularEntry.CONTENT_URI;
                break;

            case "1":   movieUri = MoviesContract.RatedEntry.CONTENT_URI;
                break;

            case "2":   movieUri = MoviesContract.FavouritesEntry.CONTENT_URI;
                break;
        }
        return new CursorLoader(getActivity(),movieUri,MOVIE_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        gridAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            movieGrid.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        gridAdapter.swapCursor(null);
    }

    @Override
    public void onDestroy() {
        if(cur!=null)
        {   cur.close();
        }
        super.onDestroy();
    }
}
