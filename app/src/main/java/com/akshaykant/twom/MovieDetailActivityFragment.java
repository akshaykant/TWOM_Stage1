package com.akshaykant.twom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    Movie movie;

    public MovieDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movies_details")) {

            movie = (Movie)intent.getParcelableExtra("movies_details");
            DisplayInfo(rootView);

        }

        return rootView;
    }

    private void DisplayInfo(View v){
        TextView title = (TextView) v.findViewById(R.id.movie_title_MovieDetailFragement);
        ImageView poster = (ImageView) v.findViewById(R.id.image_view_MovieDetailFragement);
        TextView releaseDate = (TextView) v.findViewById(R.id.release_date_MovieDetailFragement);
        TextView ratings = (TextView) v.findViewById(R.id.ratings_view_MovieDetailFragement);
        TextView overview = (TextView) v.findViewById(R.id.synopsis_view_MovieDetailFragement);

        title.setText(movie.getTitle());
        Picasso.with(getActivity()).load(movie.getPoster()).into(poster);
        releaseDate.setText(movie.getReleaseDate());
        ratings.setText(movie.getVoteAverage() + "/10");
        overview.setText(movie.getOverview());
    }

}