package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.model.SpKeys;

public class MainActivity extends AppCompatActivity implements MovieGridFragment.Callback {
    private Toolbar toolbar;
    private boolean mTwoPane;
    private String sort;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        sort=preferences.getString(SpKeys.SORT_KEY,"0");
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.movies_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
//        String sort=preferences.getString(SpKeys.SORT_KEY,"0");
//
//    }
    @Override
    protected void onResume() {
        super.onResume();
        String tsort=preferences.getString(SpKeys.SORT_KEY,"0");
        toolbar.setTitle(getResources().getStringArray(R.array.sort_name)[Integer.parseInt(tsort)]);
        setSupportActionBar(toolbar);
        if (!tsort.equals(sort)) {
            MovieGridFragment ff = (MovieGridFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
            if ( null != ff ) {
                ff.onPreferenceChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df ) {
                df.onMoviesChanged(sort);
            }
            sort = tsort;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class).setData(contentUri);
            startActivity(intent);
        }
    }
}
