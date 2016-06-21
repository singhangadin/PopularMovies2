package com.example.android.popularmovies.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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

/**<p>
 * Created by Angad Singh on 21-06-2016.
 * </p>
 */
public class MoviesService extends IntentService{
    public static final String SORT_EXTRA = "se";
    public MoviesService() {
        super("MoviesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try
        {   String sort=intent.getStringExtra(SORT_EXTRA);
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
                    String trailer=trailers.toString();
                    ContentValues moviesValues = new ContentValues();
                    switch(sortnum)
                    {   case "0":   moviesValues.put(MoviesContract.PopularEntry.COLUMN_TITLE, title);
                        moviesValues.put(MoviesContract.PopularEntry.COLUMN_ID, id);
                        moviesValues.put(MoviesContract.PopularEntry.COLUMN_POSTER, poster);
                        moviesValues.put(MoviesContract.PopularEntry.COLUMN_RELEASE_DATE, releasedate);
                        moviesValues.put(MoviesContract.PopularEntry.COLUMN_OVERVIEW, overview);
                        moviesValues.put(MoviesContract.PopularEntry.COLUMN_VOTE_AVERAGE, voteavg);
                        moviesValues.put(MoviesContract.PopularEntry.COLUMN_TRAILER,trailer);
                        break;

                        case "1":   moviesValues.put(MoviesContract.RatedEntry.COLUMN_TITLE, title);
                            moviesValues.put(MoviesContract.RatedEntry.COLUMN_ID, id);
                            moviesValues.put(MoviesContract.RatedEntry.COLUMN_POSTER, poster);
                            moviesValues.put(MoviesContract.RatedEntry.COLUMN_RELEASE_DATE, releasedate);
                            moviesValues.put(MoviesContract.RatedEntry.COLUMN_OVERVIEW, overview);
                            moviesValues.put(MoviesContract.RatedEntry.COLUMN_VOTE_AVERAGE, voteavg);
                            moviesValues.put(MoviesContract.RatedEntry.COLUMN_TRAILER,trailer);
                            break;

                        default:    break;
                    }
                    cVVector.add(moviesValues);
                    conn.disconnect();
                }
                int inserted = 0;
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    switch(sortnum)
                    {   case "0":   //context.getContentResolver().delete(MoviesContract.PopularEntry.CONTENT_URI,null,null);
                                    inserted = getContentResolver().bulkInsert(MoviesContract.PopularEntry.CONTENT_URI, cvArray);
                                    break;

                        case "1":   //context.getContentResolver().delete(MoviesContract.RatedEntry.CONTENT_URI,null,null);
                                    inserted = getContentResolver().bulkInsert(MoviesContract.RatedEntry.CONTENT_URI, cvArray);
                                    break;
                    }

                }
            }
            con.disconnect();
        }
        catch(Exception e)
        {   e.printStackTrace();
        }
    }

    public static class AlarmReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, MoviesService.class);
            sendIntent.putExtra(MoviesService.SORT_EXTRA, intent.getStringExtra(MoviesService.SORT_EXTRA));
            context.startService(sendIntent);
        }
    }
}
