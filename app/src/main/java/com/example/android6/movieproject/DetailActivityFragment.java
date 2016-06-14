package com.example.android6.movieproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    ArrayList<String> toPareseList;
    private final String LOG_TAG = getClass().getSimpleName();

    int movie_id;
    Movie movie;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.release_date)
    TextView release_date;
    @Bind(R.id.synopsis)
    TextView synopsis;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.iVDetail)
    ImageView iVDetail;
    @Bind(R.id.reviews)
    TextView reviews;
    @Bind(R.id.trailers)
    TextView trailers;

    @Bind(R.id.play_button)
    ImageButton play_button;
    @Bind(R.id.favorites_button)
    ImageButton favorites_button;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        movie = bundle.getParcelable(Movie.MOVIE);
        movie_id = movie.getMovie_id();
        //Log.v("Test", "movie id is"+movie_id);
        title.setText(movie.getTitle());
        synopsis.setText(movie.getSynopsis());
        rating.setText(Float.toString(movie.getRating())+" / 10");
        release_date.setText(movie.getReleaaseDate());
        Glide.with(this).load("http://image.tmdb.org/t/p/w780" + movie.getPosterUrl()).into(iVDetail);
        FetchTrailerTask trailerTask = new FetchTrailerTask();
        trailerTask.execute(1);
        FetchTrailerTask trailerTask2 = new FetchTrailerTask();
        trailerTask2.execute(2);
        initFavoriteButton(movie);

        initPlayButton();

        Log.v(LOG_TAG, "movie id: "+movie_id+" title: "+movie.getTitle()+" synopsis: "+movie.getSynopsis()+" rating: "+movie.getRating()
                +" release date: "+movie.getReleaaseDate()+" poster_url: "+movie.getPosterUrl());


        return rootView;
    }

    public void initPlayButton(){


        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie.getTrailer_id()));
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e){
                    e.printStackTrace();
                    String BASE_URL = "https://www.youtube.com/watch?";
                    String ID = "v";
                    Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(ID, movie.getTrailer_id()).build();
                    Intent tubeIntent = new Intent(Intent.ACTION_VIEW);
                    Log.v(LOG_TAG, "youtube url is "+ builtUri);
                    tubeIntent.setData(builtUri);
                    startActivity(tubeIntent);
                }


            }
        });

    }

    public void initFavoriteButton(final Movie movie){
        favorites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("MovieFaves",Context.MODE_PRIVATE).edit();
                editor.putString(movie.getTitle(), movie.getPosterUrl());
                editor.commit();
                Snackbar.make(v, "Added to Favorites", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }


    public static Fragment newInstance(Movie movie) {
        DetailActivityFragment fragment = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    public class FetchTrailerTask extends AsyncTask<Integer, Void, ArrayList<String>> {

        private final String LOG_TAG = getClass().getSimpleName();


        @Override
        protected ArrayList<String> doInBackground(Integer... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;
            Uri builtUri;
            String appid = getString(R.string.api_kee);


            try {
                final String BASE_URL = "http://api.themoviedb.org/3";
                final String MOVIE = "movie/";
                final String MOVIE_ID = Integer.toString(movie_id);
                final String TRAILERS = "/videos";
                final String REVIEWS = "/reviews";
                final String API_PARAM = "api_key";

                if (params[0] == 1){
                    builtUri = Uri.parse(BASE_URL).buildUpon().appendEncodedPath(MOVIE + MOVIE_ID + TRAILERS).appendQueryParameter(API_PARAM, appid).build();
                }
                else {
                    builtUri = Uri.parse(BASE_URL).buildUpon().appendEncodedPath(MOVIE + MOVIE_ID + REVIEWS).appendQueryParameter(API_PARAM, appid).build();
                }

                URL url = new URL(builtUri.toString());
                //Log.v(LOG_TAG, "url is currently" + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.e(LOG_TAG, "Input Stream Error");
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Log.e(LOG_TAG, "Buffer length is 0");
                }
                JsonStr = buffer.toString();
                //Log.v(LOG_TAG, "JSON string:  " + JsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Ërror here", e);

            } finally

            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            toPareseList = new ArrayList<String>();
            toPareseList.add(0,Integer.toString(params[0]));
            toPareseList.add(1, JsonStr);

            return toPareseList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> toParse) {
            //Log.v("to check which json", "category is "+toParse.get(0) +" str is "+toParse.get(1));
            if (toParse.get(0).equals(Integer.toString(1))){
                parseTrailerJson(toParse.get(1));
            }
            else if (toParse.get(0).equals(Integer.toString(2))){
                parseReviewJson(toParse.get(1));

            }
            else {
                Log.v("didnt have good param", "failure");
            }
        }
    }

    public void parseTrailerJson(String trailerJson){
        if (trailerJson != null) {
            try {
                JSONObject trailersJson = new JSONObject(trailerJson);
                JSONArray results = trailersJson.getJSONArray("results");
                JSONObject info = results.getJSONObject(0);
                trailers.setText(info.getString("name"));
                movie.setTrailer_id(info.getString("key"));
                Log.v(LOG_TAG,"trailer id is "+movie.getTrailer_id());

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseReviewJson(String reviewJson){
        //Log.v("entering reviews", "success: "+reviewJson);
        if (reviewJson != null) {
            try {
                JSONObject reviewsJson = new JSONObject(reviewJson);
                JSONArray results = reviewsJson.getJSONArray("results");
                JSONObject info = results.getJSONObject(0);
                movie.setReview(info.getString("content"));
                reviews.setText(movie.getReview());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
