package com.example.android6.movieproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
import java.util.Map;


public class MovieFragment extends Fragment{

//    @Bind(R.id.rVMain)
    RecyclerView rVMain;
//    @Bind(R.id.grid_item_img)
    ImageView grid_item_img;
    FrameLayout lL;

    private GridLayoutManager gLManager;


    private ArrayList<Movie> MovieList;
    public static final String MovieFeatures = "MovieFeatures";
//    GridView gridView;
    //    private CustomGridAdapter mMovieAdapter;
    private RecyclerViewAdapter mMovieAdapter;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {

            MovieList = new ArrayList<Movie>();
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute("movie/popular");
        }
        else{
//        if (savedInstanceState != null) {
            MovieList = savedInstanceState.getParcelableArrayList("movie");
        }

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //mMovieAdapter = new ArrayAdapter<ImageView>(getActivity(), R.layout.grid_item, R.id.grid_item_txt, new ArrayList<ImageView>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        mMovieAdapter = new CustomGridAdapter(getActivity(),MovieList);
//        ButterKnife.bind(this, rootView);
        rVMain = (RecyclerView) rootView.findViewById(R.id.rVMain);
        //grid_item_img = (ImageView) rootView.findViewById(R.id.grid_item_img);
        //initGridView(rootView);

        mMovieAdapter = new RecyclerViewAdapter(MovieList);
        rVMain.setAdapter(mMovieAdapter);
        gLManager = new GridLayoutManager(getActivity(), 2);
        rVMain.setHasFixedSize(true);
        rVMain.setLayoutManager(gLManager);



        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie", MovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragmenu, menu);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_scrape) {
            String pop_url = "movie/popular";
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(pop_url);
        }
        else if (id == R.id.top_rated_movies) {
            String top_url = "movie/top_rated";
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(top_url);
        }

        else if (id == R.id.favorited_movies) {
            //Open shared preferences, clear MovieList, add movies within it into the MovieList, call initGridView
            SharedPreferences prefs = getActivity().getSharedPreferences("MovieFaves", Context.MODE_PRIVATE);
            Map<String,?> keys = prefs.getAll();
            MovieList.clear();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                Movie movie = new Movie();
                movie.setTitle(entry.getKey());
                movie.setPosterUrl(entry.getValue().toString());
                Log.v("map values",entry.getKey() + ": " +
                        entry.getValue().toString());
                MovieList.add(movie);

            }
            initGridView(getView());
        }

//        else if (id == R.id.clear_favorites){
//            SharedPreferences.Editor editor = getActivity().getSharedPreferences("MovieFaves",Context.MODE_PRIVATE).edit();
//            editor.clear();
//            editor.commit();
//
//            initGridView(getView());
//        }

        return super.onOptionsItemSelected(item);
    }


//    private void intViews() {
//        layoutManager = new GridLayoutManager(getActivity(), getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3);
//        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                page = currentPage;
//                if (finalPage >= currentPage) {
//                    loadMovies(selectSort);
//                }
//
//            }
//        };
//        rVHome.setHasFixedSize(true);
//        rVHome.setLayoutManager(layoutManager);
//        rVHome.setAdapter(adapter);
//        if (!isFavoriteScream) {
//            rVHome.addOnScrollListener(scrollListener);
//        }
//    }

    public interface Callback {

        void onItemSelected(Movie movie);
    }

    public void initGridView(View rootView){


//        gridView = (GridView) rootView.findViewById(R.id.gridView);
//        gridView.setAdapter(mMovieAdapter);

        rVMain.setAdapter(mMovieAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Movie movie = MovieList.get(position);
//
//
//                ((Callback) getActivity()).onItemSelected(movie);
////                Intent detailintent = new Intent(getActivity(),
////                        DetailActivity.class);
////                detailintent.putExtra(Movie.MOVIE, movie);
////                startActivity(detailintent);
//
//
//        detailIntent.putExtra("poster", movie.getPosterUrl());
//                detailIntent.putExtra("title", movie.getTitle());
//                detailIntent.putExtra("synopsis", movie.getSynopsis());
//                detailIntent.putExtra("rating", movie.getRating());
//                detailIntent.putExtra("release_date", movie.getReleaaseDate());

//
//
//                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MovieFeatures,Context.MODE_PRIVATE).edit();
//                editor.putString("poster",movie.getPosterUrl());
//                editor.putString("title", movie.getTitle());
//                editor.putString("synopsis", movie.getSynopsis());
//                editor.putFloat("rating", movie.getRating());
//                editor.putString("release_date", movie.getReleaaseDate());
//                    Log.v("test","title is "+ movie.getTitle());
//                editor.apply();
//                Intent detailIntent = new Intent(getActivity().getBaseContext(), DetailsActivity.class);
//                getActivity().startActivity(detailIntent);

//            }
//        });

    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

        ArrayList<Movie> movieList;

        public RecyclerViewAdapter(ArrayList<Movie> movieList) {
            this.movieList = movieList;
        }

        @Override
        public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
            return new RecyclerViewHolders(layoutView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolders holder, int position) {
            Movie movie = MovieList.get(position);

            String BASE_URL = "http://image.tmdb.org/t/p/";
            String IMG_SIZE = "w185";
            Context context = holder.moviePhoto.getContext();


            Picasso.with(holder.moviePhoto.getContext()).load(BASE_URL + IMG_SIZE + movie.getPosterUrl())
//                  .fit().centerCrop()
                    .into(holder.moviePhoto);
        }

        @Override
        public int getItemCount() {
            return this.movieList.size();
        }
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView moviePhoto;

        RecyclerViewHolders(View itemView) {
            super(itemView);
            moviePhoto = (ImageView)itemView.findViewById(R.id.grid_item_img);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Movie movie = MovieList.get(getAdapterPosition());
            ((Callback) getActivity()).onItemSelected(movie);
        }
    }


    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = getClass().getSimpleName();


        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;
            String appid = getString(R.string.api_kee);

            try {
                final String BASE_URL = "http://api.themoviedb.org/3";
                final String CATEGORY_PARAM = params[0];
                final String API_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendEncodedPath(CATEGORY_PARAM).appendQueryParameter(API_PARAM, appid).build();

                URL url = new URL(builtUri.toString());
                //Log.v(LOG_TAG, "url is currently"+ url);
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
                //Log.v(LOG_TAG, "Forecast JSON string:  " + JsonStr);


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
            try {
                getMovieDataFromJson(JsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return MovieList;
        }

        private void getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            MovieList.clear();
            JSONObject movieJSON = new JSONObject(movieJsonStr);
            JSONArray moviesArray = movieJSON.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++) {
                Movie movie = new Movie();
                JSONObject singleMovieObj = moviesArray.getJSONObject(i);
                movie.setPosterUrl(singleMovieObj.getString("poster_path"));
                //Glide.with(this).load("http://image.tmdb.org/t/p/w780"+movie.getPosterUrl()).into(movie.poster);
                movie.setRating(singleMovieObj.getInt("vote_average"));
                movie.setReleaaseDate(singleMovieObj.getString("release_date"));
                movie.setMovie_id(singleMovieObj.getInt("id"));
                //Log.v("TestfromMain", "movie id is"+ movie.getMovie_id());
                movie.setSynopsis(singleMovieObj.getString("overview"));
                movie.setTitle(singleMovieObj.getString("title"));
                //Log.v(LOG_TAG, "poster url is " + movie.getPosterUrl() + "\n" + "rating is " + movie.getRating() + "\n" + "release date is " + movie.getReleaaseDate()
                //       + "\n" + "movie id is " + movie.getMovie_id() + "\n" + "synopsis is " + movie.getSynopsis() + "\n" + "Title is " + movie.getTitle());

                MovieList.add(movie);

            }

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> MovieList) {
            initGridView(getView());
//            String BASE_URL = "http://image.tmdb.org/t/p/";
//            String IMG_SIZE = "w185";
//            if (!MovieList.isEmpty()) {
//                mMovieAdapter.clear();
//                //int i = 0;
//                //while (i<MovieList.size()){
//                for (Movie movie:MovieList) {
//                    imageView = new ImageView(getContext());
//                    Picasso.with(getContext()).load(BASE_URL + IMG_SIZE + movie.getPosterUrl()).into(imageView);
//                    mMovieAdapter.add(imageView);
//                }
//            }



        }

    }
}



