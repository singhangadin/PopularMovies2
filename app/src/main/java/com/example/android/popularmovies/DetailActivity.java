package com.example.android.popularmovies;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView titleImage;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white,getTheme()));
            collapsingToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
        }
        else {
            collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
            collapsingToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        titleImage = (ImageView)findViewById(R.id.parralax_image);

        if(savedInstanceState==null)
        {   Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment,fragment).commit();
        }
    }

    public CollapsingToolbarLayout getToolbar()
    {   return this.collapsingToolbar;
    }

    public ImageView getTitleImage() {
        return titleImage;
    }
}
