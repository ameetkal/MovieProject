package com.example.android6.movieproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final Movie movieConst = new Movie();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (savedInstanceState != null){
            getSupportFragmentManager().getFragment(savedInstanceState,"movies");
        }
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            movieConst.setMovie_id(269149);
            movieConst.setTitle("Zootopia");
            movieConst.setSynopsis("Determined to prove herself, Officer Judy Hopps, the first bunny on Zootopia's police force, jumps at the chance to crack her first case - even if it means partnering with scam-artist fox Nick Wilde to solve the mystery.");
            movieConst.setRating(7);
            movieConst.setPosterUrl("/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg");
            movieConst.setReleaaseDate("2016-02-11");
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, DetailActivityFragment.newInstance(movieConst), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            mTwoPane = false;
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            fragTrans.add(R.id.fragment, new MovieFragment());
            fragTrans.commit();
        }

       /* Intent detailIntent = getIntent();
        String titles = detailIntent.getStringExtra("title");
        String synopsiss = detailIntent.getStringExtra("synopsis");
        Float ratings = detailIntent.getFloatExtra("rating",0);
        String releases = detailIntent.getStringExtra("release_date");
        String posterurl = detailIntent.getStringExtra("poster");

        Intent intent = new Intent(MainActivity.this,
                DetailsActivity.class);
        intent.putExtra("poster", posterurl);
        intent.putExtra("title", titles);
        intent.putExtra("synopsis", synopsiss);
        intent.putExtra("rating", ratings);
        intent.putExtra("release_date", releases);
        startActivity(intent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,"movies",getSupportFragmentManager().findFragmentById(R.id.fragment));
    }




    @Override
    public void onItemSelected(Movie movie) {
        if(mTwoPane){
            Fragment frag = DetailActivityFragment.newInstance(movie);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, frag, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent detailintent = new Intent(MainActivity.this, DetailActivity.class).putExtra(Movie.MOVIE, movie);
            startActivity(detailintent);
        }
    }

}
