package com.example.android.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.model.SpKeys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 300;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final int MOVIES_NOTIFICATION_ID = 3000;

    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try
        {   SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(getContext());
            String sort=preference.getString(SpKeys.SORT_KEY,getContext().getResources().getString(R.string.sort_default));
            final String sortnum=sort;
            switch(sort)
            {   case "0":   sort="popularity.desc";
                break;

                case "1":   sort="vote_average.desc";
                    break;
            }
            Uri.Builder reqUri=new Uri.Builder();
            reqUri.encodedPath("https://api.themoviedb.org/3/discover/movie");
            reqUri.appendQueryParameter("api_key", SpKeys.API_KEY);
            reqUri.appendQueryParameter("sort_by",sort);
            reqUri.appendQueryParameter("page","1");
            URL reqUrl=new URL(reqUri.toString());
            HttpURLConnection con=(HttpURLConnection) reqUrl.openConnection();
            InputStream in=con.getInputStream();
            BufferedReader buff=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonResp=new StringBuilder();
            String res;
            while((res=buff.readLine())!=null)
            {   jsonResp.append(res);
            }
            if(jsonResp.length()>0)
            {   JSONObject respone=new JSONObject(jsonResp.toString());
                JSONArray result=respone.getJSONArray("results");
                Vector<ContentValues> cVVector = new Vector<>(jsonResp.length());
                for(int i=0;i<result.length();i++)
                {   JSONObject object=result.getJSONObject(i);
                    String title=object.optString("title");
                    String poster=object.optString("poster_path");
                    String id=object.optString("id");
                    String releasedate=object.optString("release_date");
                    String voteavg=object.optString("vote_average");
                    String overview=object.optString("overview");
                    //
                    JSONArray trailers=new JSONArray();
                    Uri.Builder trailerUri=new Uri.Builder();
                    trailerUri.encodedPath("http://api.themoviedb.org/3/movie/"+id);
                    trailerUri.appendQueryParameter("api_key", SpKeys.API_KEY);
                    trailerUri.appendQueryParameter("append_to_response","videos");
                    URL trailerUrl=new URL(trailerUri.toString());
                    HttpURLConnection conn=(HttpURLConnection) trailerUrl.openConnection();
                    InputStream inn=conn.getInputStream();
                    BufferedReader buffn=new BufferedReader(new InputStreamReader(inn));
                    StringBuilder jsonRespn=new StringBuilder();
                    String resn;
                    while((resn=buffn.readLine())!=null)
                    {   jsonRespn.append(resn);
                    }
                    if(jsonRespn.length()>0) {
                        JSONObject response = new JSONObject(jsonRespn.toString());
                        JSONObject videos = response.getJSONObject("videos");
                        JSONArray results = videos.getJSONArray("results");
                        for (int j = 0; j < results.length(); j++) {
                            JSONObject objectn=results.getJSONObject(j);
                            String key=objectn.optString("key");
                            String name=objectn.optString("name");
                            if(objectn.optString("site").equals("YouTube")) {
                                JSONObject trailers_obj = new JSONObject();
                                trailers_obj.put("key", key);
                                trailers_obj.put("name", name);
                                trailers.put(trailers_obj);
                            }
                        }
                    }
                    //
                    //
                    JSONArray reviews=new JSONArray();
                    Uri.Builder reviewsUri=new Uri.Builder();
                    reviewsUri.encodedPath("http://api.themoviedb.org/3/movie/"+id+"/reviews");
                    reviewsUri.appendQueryParameter("api_key", SpKeys.API_KEY);
                    URL reviewsUrl=new URL(reviewsUri.toString());
                    HttpURLConnection conr=(HttpURLConnection) reviewsUrl.openConnection();
                    InputStream inr=conr.getInputStream();
                    BufferedReader buffr=new BufferedReader(new InputStreamReader(inr));
                    StringBuilder jsonRespr=new StringBuilder();
                    String resr;
                    while((resr=buffr.readLine())!=null)
                    {   jsonRespr.append(resr);
                    }
                    if(jsonRespr.length()>0) {
                        JSONObject response = new JSONObject(jsonRespr.toString());
                        JSONArray results = response.getJSONArray("results");
                        for (int j = 0; j < results.length(); j++) {
                            JSONObject objectn=results.getJSONObject(j);
                            String author=objectn.optString("author");
                            String content=objectn.optString("content");
                            JSONObject review_obj = new JSONObject();
                            review_obj.put("author", author);
                            review_obj.put("content", content);
                            reviews.put(review_obj);
                        }
                    }
                    //
                    String trailer=trailers.toString();
                    String review=reviews.toString();
                    ContentValues moviesValues = new ContentValues();
                    switch(sortnum)
                    {   case "0":   moviesValues.put(MoviesContract.PopularEntry.COLUMN_TITLE, title);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_ID, id);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_POSTER, poster);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE, releasedate);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, overview);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE, voteavg);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_TRAILER,trailer);
                                    moviesValues.put(MoviesContract.PopularEntry.COLUMN_REVIEWS,review);
                                    break;

                        case "1":   moviesValues.put(MoviesContract.RatedEntry.COLUMN_TITLE, title);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_ID, id);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_POSTER, poster);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_RELEASE_DATE, releasedate);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_OVERVIEW, overview);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_VOTE_AVERAGE, voteavg);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_TRAILER,trailer);
                                    moviesValues.put(MoviesContract.RatedEntry.COLUMN_REVIEWS,review);
                                    break;

                        default:    break;
                    }
                    cVVector.add(moviesValues);
                    conr.disconnect();
                    conn.disconnect();
                }
                int inserted = 0;
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    switch(sortnum)
                    {   case "0":   //getContext().getContentResolver().delete(MoviesContract.PopularEntry.CONTENT_URI,null,null);
                                    inserted = getContext().getContentResolver().bulkInsert(MoviesContract.PopularEntry.CONTENT_URI, cvArray);
                        break;

                        case "1":   //getContext().getContentResolver().delete(MoviesContract.RatedEntry.CONTENT_URI,null,null);
                                    inserted = getContext().getContentResolver().bulkInsert(MoviesContract.RatedEntry.CONTENT_URI, cvArray);
                            break;
                    }
                    boolean dont_notify=preference.getBoolean(SpKeys.NOTIFICATION_KEY,false);
                    if(!dont_notify) {
                        notifyChanges();
                    }
                }
            }
            con.disconnect();
        }
        catch(Exception e)
        {   e.printStackTrace();
        }
    }

    private void notifyChanges() {
        Context context = getContext();
        String title = context.getString(R.string.app_name);
        int iconId = R.mipmap.ic_launcher;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(iconId)
                        .setContentTitle(title)
                        .setContentText("Your Data has been Updated. Click to see It.");
        Intent resultIntent = new Intent(getContext(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MOVIES_NOTIFICATION_ID, mBuilder.build());
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if ( null == accountManager.getPassword(newAccount) ) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        MoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}