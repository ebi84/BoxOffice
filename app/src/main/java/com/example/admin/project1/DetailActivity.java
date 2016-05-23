package com.example.admin.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    // a fragment for showing details of the selected movie
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment()).commit();
        }
    }

    public static class DetailFragment extends Fragment
    {

        private String movie_title = null;
        private String vote_average = null;
        private String poster_path = null;
        private String popularity = null;
        private String release_date = null;
        private String overview = null;
        private String backdrop_path = null;

        public DetailFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();

            if(null != intent && intent.hasExtra("clickedMovie"))
            {
                MovieClass movie = intent.getParcelableExtra("clickedMovie");
                movie_title = movie.getOiginalTitle();
                vote_average = movie.getVoteAverage();
                poster_path = movie.getPosterPath();
                popularity = movie.getPopularity();
                release_date = movie.getReleaseDate();
                overview = movie.getOverview();
                backdrop_path = movie.getBackdropPath();

                ((TextView) rootView.findViewById(R.id.d_movie_title)).setText(movie_title);
                ((TextView) rootView.findViewById(R.id.d_vote_average)).setText("Rating: " + vote_average);
                if(popularity.length()>5) {
                    ((TextView) rootView.findViewById(R.id.d_popularity)).setText("Popularity: " + popularity.substring(0, 5));
                }
                else{
                    ((TextView) rootView.findViewById(R.id.d_popularity)).setText("Popularity: " + popularity);
                }
                ((TextView) rootView.findViewById(R.id.d_release_date)).setText("Release date: " + release_date);
                ((TextView) rootView.findViewById(R.id.d_overview)).setText("Story line: " + overview);

                ImageView posterView = (ImageView) rootView.findViewById(R.id.d_movie_poster);
                Picasso.with(getContext()).load(poster_path).into(posterView);

                ImageView backdropView = (ImageView) rootView.findViewById(R.id.d_movie_backdrop);
                Picasso.with(getContext()).load(backdrop_path).into(backdropView);
            }
            else
            {
                Toast.makeText(getContext(),"Failed to show movie details", Toast.LENGTH_SHORT).show();
            }

            return rootView;
        }
    }

}
