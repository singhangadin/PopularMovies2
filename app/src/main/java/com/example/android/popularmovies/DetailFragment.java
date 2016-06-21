package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.customviews.MyRecyclerView;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.model.ListItem;
import com.example.android.popularmovies.model.SpKeys;
import com.example.android.popularmovies.model.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DETAIL_LOADER = 0;
    private Context context;
    public static final String DETAIL_URI = "URI";
    private Uri mUri;
    private ImageView img;
    private Button fav_button;
    private TextView rate,reldate,summary,trailer_indicator;
    private MyRecyclerView trailer;

    private static final String[] MOVIE_COLUMNS = {
        MoviesContract.PopularEntry._ID,
        MoviesContract.PopularEntry.COLUMN_ID,
        MoviesContract.PopularEntry.COLUMN_TITLE,
        MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE,
        MoviesContract.PopularEntry.COLUMN_RELEASE_DATE,
        MoviesContract.PopularEntry.COLUMN_OVERVIEW,
        MoviesContract.PopularEntry.COLUMN_POSTER,
        MoviesContract.PopularEntry.COLUMN_TRAILER,
    };

    private static final String[] FAVOURITE_COLUMNS = {
        MoviesContract.PopularEntry._ID,
        MoviesContract.PopularEntry.COLUMN_ID,
        MoviesContract.PopularEntry.COLUMN_TITLE,
        MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE,
        MoviesContract.PopularEntry.COLUMN_RELEASE_DATE,
        MoviesContract.PopularEntry.COLUMN_OVERVIEW,
        MoviesContract.PopularEntry.COLUMN_POSTER,
        MoviesContract.PopularEntry.COLUMN_TRAILER
    };

    private static final int COL_ID = 0;
    private static final int COL_MOVIE_ID = 1;
    private static final int COL_TITLE = 2;
    private static final int COL_MOVIE_VOTE_AVERAGE = 3;
    private static final int COL_MOVIE_RELEASE_DATE = 4;
    private static final int COL_MOVIE_OVERVIEW = 5;
    private static final int COL_MOVIE_POSTER = 6;
    private static final int COL_MOVIE_TRAILER = 7;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_detail, container, false);
        img=(ImageView)V.findViewById(R.id.imageView);
        rate=(TextView)V.findViewById(R.id.avg);
        reldate=(TextView)V.findViewById(R.id.rel_date);
        summary=(TextView)V.findViewById(R.id.summary);
        trailer_indicator=(TextView)V.findViewById(R.id.trailer_text);
        trailer=(MyRecyclerView) V.findViewById(R.id.trailer_list);
        fav_button=(Button) V.findViewById(R.id.fav_button);
        return V;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    void onMoviesChanged( String sort ) {
        Uri uri = mUri;
        if (null != uri) {
            Uri updatedUri;
            switch(sort)
            {   case "0":
                default:    updatedUri = MoviesContract.PopularEntry.CONTENT_URI;
                    break;

                case "1":   updatedUri = MoviesContract.RatedEntry.CONTENT_URI;
                    break;

                case "2":   updatedUri = MoviesContract.FavouritesEntry.CONTENT_URI;
                    break;
            }
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
            final String sort=preferences.getString(SpKeys.SORT_KEY,"0");
            switch(sort)
            {   case "0":
                case "1":   return new CursorLoader(
                                    context,
                                    mUri,
                                    MOVIE_COLUMNS,
                                    null,
                                    null,
                                    null
                            );

                case "2":   return new CursorLoader(
                                    context,
                                    mUri,
                                    FAVOURITE_COLUMNS,
                                    null,
                                    null,
                                    null
                            );
            }

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,final Cursor data) {
        data.moveToFirst();
        try {
            CollapsingToolbarLayout toolbar = ((DetailActivity) getActivity()).getToolbar();
            toolbar.setTitle(data.getString(COL_TITLE));
            ImageView titleImage= ((DetailActivity) getActivity()).getTitleImage();
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + data.getString(COL_MOVIE_POSTER)).into(titleImage);
        }
        catch(Exception e)
        {   e.printStackTrace();
        }

        try {
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
            final String sort=preferences.getString(SpKeys.SORT_KEY,"0");
            fav_button.setVisibility(View.VISIBLE);
            switch(sort)
            {   case "0":
                case "1":   Cursor cursor=context.getContentResolver()
                                .query(MoviesContract.FavouritesEntry.buildFavouriteIDUri(data.getString(COL_MOVIE_ID))
                                        ,new String[]{MoviesContract.FavouritesEntry.COLUMN_ID}
                                        ,null
                                        ,null
                                        ,null);
                            if (cursor!=null&&cursor.getCount()==0) {
                                fav_button.setText(context.getResources().getString(R.string.add_to_favourites));
                            }
                            else
                            {   fav_button.setText(context.getResources().getString(R.string.remove_from_favourites));

                            }
                            if(cursor!=null)
                                cursor.close();
                            break;

                case "2":   fav_button.setText(context.getResources().getString(R.string.remove_from_favourites));
                            break;
            }

            fav_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.getCount()==0)
                    {   fav_button.setVisibility(View.INVISIBLE);
                        return;
                    }
                    switch(sort)
                    {   case "0":
                        case "1":   Cursor cursor=context.getContentResolver()
                                        .query(MoviesContract.FavouritesEntry.buildFavouriteIDUri(data.getString(COL_MOVIE_ID))
                                                ,new String[]{MoviesContract.FavouritesEntry.COLUMN_ID}
                                                ,null
                                                ,null
                                                ,null);
                                    if(cursor!=null&&cursor.getCount()==0) {
                                        ContentValues moviesValues = new ContentValues();
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_TITLE, data.getString(COL_TITLE));
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_ID, data.getString(COL_MOVIE_ID));
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_POSTER, data.getString(COL_MOVIE_POSTER));
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_RELEASE_DATE, data.getString(COL_MOVIE_RELEASE_DATE));
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_OVERVIEW, data.getString(COL_MOVIE_OVERVIEW));
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_VOTE_AVERAGE, data.getString(COL_MOVIE_VOTE_AVERAGE));
                                        moviesValues.put(MoviesContract.FavouritesEntry.COLUMN_TRAILER, data.getString(COL_MOVIE_TRAILER));
                                        context.getContentResolver().insert(mUri, moviesValues);
                                    }
                                    else
                                    {   String deleteArg[]={data.getString(COL_MOVIE_ID)};
                                        context.getContentResolver()
                                                .delete(MoviesContract.FavouritesEntry.buildFavouriteIDUri(data.getString(COL_MOVIE_ID)),null,deleteArg);
                                    }
                                    if(cursor!=null)
                                        cursor.close();
                                    break;

                        case "2":   String deleteArg[]={data.getString(COL_MOVIE_ID)};
                                    int i=context.getContentResolver().delete(MoviesContract.FavouritesEntry.
                                        buildFavouriteIDUri(data.getString(COL_MOVIE_ID)),null,deleteArg);
                                    if(i>0)
                                    {   fav_button.setVisibility(View.INVISIBLE);
                                        Toast.makeText(context,"Movie Removed from favourites",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                    }
                }
            });
            trailer_indicator.setVisibility(View.VISIBLE);
            rate.setText("Rated:\n" + data.getString(COL_MOVIE_VOTE_AVERAGE) + "/10");
            reldate.setText("Released On:\n" + data.getString(COL_MOVIE_RELEASE_DATE));
            summary.setText("Summary:\n" + data.getString(COL_MOVIE_OVERVIEW));
            if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w500/" + data.getString(COL_MOVIE_POSTER)).into(img);
            } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + data.getString(COL_MOVIE_POSTER)).into(img);
            } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + data.getString(COL_MOVIE_POSTER)).into(img);
            } else {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + data.getString(COL_MOVIE_POSTER)).into(img);
            }
            final ArrayList<ListItem> listItems=new ArrayList<>();
            Log.e("JSON",data.getString(COL_MOVIE_TRAILER));
            JSONArray arr=new JSONArray(data.getString(COL_MOVIE_TRAILER));
            for(int i=0;i<arr.length();i++)
            {   JSONObject obj=arr.getJSONObject(i);
                ListItem item=new ListItem();
                item.setTitle(obj.optString("name"));
                item.setKey(obj.optString("key"));
                listItems.add(item);
            }
            Log.e("JSON",listItems.size()+"");
            trailer.setLayoutManager(new LinearLayoutManager(context));
            trailer.setAdapter(new TrailerAdapter(context,listItems));
            trailer.addOnItemTouchListener(new MyRecyclerView.OnItemClickListener() {
                @Override
                public void onClick(View V, int position) {
                    startActivity(watchYoutubeVideo(listItems.get(position).getKey()));
                }
            });
        }
        catch(Exception e)
        {   e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public Intent watchYoutubeVideo(String id){
        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        } catch (ActivityNotFoundException ex) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        }
    }
}
