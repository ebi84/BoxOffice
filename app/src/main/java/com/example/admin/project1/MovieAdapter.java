package com.example.admin.project1;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<MovieClass>{
    // Movie adapter for populating the grid view

    public MovieAdapter(Activity context, ArrayList<MovieClass> movies){
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieClass myMovies = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movies, parent, false);
        }

        ImageView PosterView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(myMovies.moviePosterPath).into(PosterView);

        TextView OriginalTitleView = (TextView) convertView.findViewById(R.id.movie_title);
        OriginalTitleView.setText(myMovies.movieOriginalTitle);

        TextView AverageVoteView = (TextView) convertView.findViewById(R.id.average_vote);
        AverageVoteView.setText("Rating: " + myMovies.movieVoteAverage);

        TextView PopularityView = (TextView) convertView.findViewById(R.id.popularity);
        if(myMovies.moviePopularity.length()>5) {
            PopularityView.setText("Popularity: " + myMovies.moviePopularity.substring(0, 5));
        }
        else{
            PopularityView.setText("Popularity: " + myMovies.moviePopularity);
        }

        TextView ReleaseDateView = (TextView) convertView.findViewById(R.id.release_date);
        ReleaseDateView.setText("Release date:     " + myMovies.movieReleaseDate);

        return convertView;
    }

    public ArrayList<MovieClass> getItems() {
        ArrayList<MovieClass> adapterList = new ArrayList<MovieClass>();

        for (int index = 0; index < this.getCount(); index++) {
            adapterList.add(getItem(index));
        }

        return adapterList;
    }
}
