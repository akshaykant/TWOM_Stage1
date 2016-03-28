package com.akshaykant.twom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.akshaykant.twom.proccessor.MovieProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay Kant
 */
public class GridPlaceholderFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

   private final String STORED_MOVIES = "stored_movies";
    private SharedPreferences prefs;
    private ImageAdapter mMoviePosterAdapter;
    String sortOrder;
    List<com.akshaykant.twom.Movie> movies = new ArrayList<Movie>();

    public GridPlaceholderFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));

        if(savedInstanceState != null){
            ArrayList<Movie> storedMovies = new ArrayList<Movie>();
            storedMovies = savedInstanceState.<Movie>getParcelableArrayList(STORED_MOVIES);
            movies.clear();
            movies.addAll(storedMovies);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMoviePosterAdapter = new ImageAdapter(
                getActivity(),
                R.layout.movies_item,
                R.id.grid_item_image,
                new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMoviePosterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie details = movies.get(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movies_details", details);
                startActivity(intent);
            }

        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // get sort order to see if it has recently changed
        String prefSortOrder = prefs.getString(getString(R.string.display_preferences_sort_order_key),
                getString(R.string.display_preferences_sort_default_value));

        if(movies.size() > 0 && prefSortOrder.equals(sortOrder)) {
            updatePosterAdapter();
        }else{
            sortOrder = prefSortOrder;
            getMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> storedMovies = new ArrayList<Movie>();
        storedMovies.addAll(movies);
        outState.putParcelableArrayList(STORED_MOVIES, storedMovies);
    }

    private void getMovies() {
        MovieProcessor movieProcessor = new MovieProcessor(new AsyncResponse() {
            @Override
            public void onTaskCompleted(List<Movie> results) {
                movies.clear();
                movies.addAll(results);
                updatePosterAdapter();
            }
        });
        movieProcessor.execute(sortOrder);
    }

    // updates the ArrayAdapter of poster images
    private void updatePosterAdapter() {
        mMoviePosterAdapter.clear();
        for(Movie movie : movies) {
            mMoviePosterAdapter.add(movie.getPoster());
        }
    }

}