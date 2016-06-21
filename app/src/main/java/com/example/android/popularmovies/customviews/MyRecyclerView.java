package com.example.android.popularmovies.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**<p>
 * Created by Angad on 08/04/2016.
 * </p>
 */

public class MyRecyclerView extends RecyclerView
{   private Context context;

    public MyRecyclerView(Context context) {
        super(context);
        this.context=context;
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addOnItemTouchListener(MyRecyclerView.OnItemClickListener clickListener)
    {   super.addOnItemTouchListener(new MyRecyclerView.RecyclerTouchListener(context, clickListener));
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private OnItemClickListener clickListener;

        public RecyclerTouchListener(Context context, OnItemClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public interface OnItemClickListener
    {   void onClick(View V,int position);
    }

    public interface OnItemLongClickListener
    {   void onClick(View V,int position);
    }
}