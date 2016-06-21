package com.example.android.popularmovies.model;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.customviews.SquareImageView;
import com.example.android.popularmovies.MovieGridFragment;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**<p>
 * Created by Angad on 08/04/2016.
 * </p>
 */

public class GridAdapter extends CursorAdapter
{   private String sort;

    public GridAdapter(Context context, Cursor c, int flags,String sort) {
        super(context, c, flags);
        this.sort=sort;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View V=LayoutInflater.from(context).inflate(R.layout.movie_grid_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(V);
        V.setTag(viewHolder);
        return V;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder=(ViewHolder)view.getTag();
        viewHolder.title.setText(cursor.getString(MovieGridFragment.COL_MOVIE_TITLE));
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w500/"+cursor.getString(MovieGridFragment.COL_MOVIE_POSTER)).into(viewHolder.banner);
        }
        else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/"+cursor.getString(MovieGridFragment.COL_MOVIE_POSTER)).into(viewHolder.banner);
        }
        else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+cursor.getString(MovieGridFragment.COL_MOVIE_POSTER)).into(viewHolder.banner);
        }
        else {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+cursor.getString(MovieGridFragment.COL_MOVIE_POSTER)).into(viewHolder.banner);
        }
    }

    public class ViewHolder
    {   public final SquareImageView banner;
        public final AppCompatTextView title;

        public ViewHolder(View view)
        {   banner=(SquareImageView)view.findViewById(R.id.banner);
            title=(AppCompatTextView)view.findViewById(R.id.title);
        }
    }
}
