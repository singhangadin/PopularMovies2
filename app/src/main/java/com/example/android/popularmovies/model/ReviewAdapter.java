package com.example.android.popularmovies.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;

import java.util.ArrayList;

/**<p>
 * Created by Angad on 08/04/2016.
 * </p>
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MoviesViewHolder>
{   private Context context;
    private ArrayList<ListItem> listItems;

    public ReviewAdapter(Context context, ArrayList<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {   View V= LayoutInflater.from(context).inflate(R.layout.review_list_item,parent,false);
        return new MoviesViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position)
    {   holder.author.setText(listItems.get(position).getKey());
        holder.content.setText(listItems.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder
    {   public TextView author,content;

        public MoviesViewHolder(View itemView)
        {   super(itemView);
            author=(TextView)itemView.findViewById(R.id.author);
            content=(TextView)itemView.findViewById(R.id.content);
        }
    }
}