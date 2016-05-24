package com.example.admin.project1;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityFragment  extends Fragment{

    private MovieAdapter myMovies;
    public View rootView;
    public GridView gridView;
    public String sortMethod;
    public String selected_sort_type;
    private SharedPreferences prefs;
    public FetchMovieData moviesTask;
    public ArrayList<MovieClass> StrMovie = new ArrayList<MovieClass>();
    public final String MOVIE_DATA = "movie_data";

    public MainActivityFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_DATA))
        {
            updateMovies();
        }
        else
        {
            StrMovie = savedInstanceState.getParcelableArrayList(MOVIE_DATA); // restoring movie data
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(MOVIE_DATA, StrMovie); // saving movie data
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) { // it is supposed to restore movie data from parcelable upon device rotating, however it doesn't work. that's why I'm recalling updateMovies() here.
            /*StrMovie = savedInstanceState.getParcelableArrayList(MOVIE_DATA);
            myMovies = new MovieAdapter(getActivity(), StrMovie);
            gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
            gridView.setAdapter(myMovies);*/
            updateMovies();
        }
    }



    private void updateMovies(){
        // updating movies based on sort type
        moviesTask = new FetchMovieData();

        selected_sort_type = prefs.getString(getString(R.string.pref_sort_type), getString(R.string.pref_sort_type_popularity));

        if (selected_sort_type.equals(getString(R.string.pref_sort_type_popularity))){
            sortMethod = "popular";

        }else{
            sortMethod = "top_rated";
        }
        Context context = getActivity();
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context,"Failed to connect to the internet",Toast.LENGTH_SHORT).show();
        }
        else {
            moviesTask.execute(sortMethod);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_settings) { // sending an intent for SettingsActivity
            startActivityForResult(new Intent(getActivity(), SettingsActivity.class), 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // updating movies based on the selected sort type
        super.onActivityResult(requestCode, resultCode, data);
        selected_sort_type = prefs.getString(getString(R.string.pref_sort_type), getString(R.string.pref_sort_type_popularity));
        if (requestCode == 1) {
            if (selected_sort_type.equals(selected_sort_type)){
                sortMethod = "popular";

            }else{
                sortMethod = "top_rated";
            }
            this.updateMovies();
        }
    }

    public class FetchMovieData extends AsyncTask<String, Void, ArrayList<MovieClass>>{
        // Fetching movie data from the URL. Basically, I have developed this part based on Sunshine project and some information I found on Udacity's forum
        private final String LOG_TAG = FetchMovieData.class.getSimpleName();


        private ArrayList<MovieClass> ExtractDataFromJSON(String StrMovieJSON)throws JSONException
        {
            final String MOVIES = "results";
            final String POSTER_PATH = "poster_path";
            final String ORIGINAL_TITLE = "original_title";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String VOTE_AVERAGE = "vote_average";
            final String POPULARITY = "popularity";
            final String BACKDROP_PATH = "backdrop_path";

            JSONObject movieJson = new JSONObject(StrMovieJSON);
            JSONArray movieArray = movieJson.getJSONArray(MOVIES);

            ArrayList<MovieClass> movies = new ArrayList<MovieClass>();
            for(int i=0; i<movieArray.length(); i++)
            {
                String poster_path;
                String backdrop_path;
                String original_title;
                String overview;
                String release_date;
                String vote_average;
                String popularity;

                JSONObject moviedata = movieArray.getJSONObject(i);
                poster_path = "http://image.tmdb.org/t/p/w342/" + moviedata.getString(POSTER_PATH);
                backdrop_path = "http://image.tmdb.org/t/p/w500/" + moviedata.getString(BACKDROP_PATH);
                original_title = moviedata.getString(ORIGINAL_TITLE);
                overview = moviedata.getString(OVERVIEW);
                release_date = moviedata.getString(RELEASE_DATE);
                vote_average = moviedata.getString(VOTE_AVERAGE);
                popularity = moviedata.getString(POPULARITY);

                MovieClass eachMovie = new MovieClass(original_title, vote_average, release_date, overview, popularity, poster_path, backdrop_path);

                movies.add(eachMovie);

            }
            return movies;
        }





        @Override
        protected ArrayList<MovieClass> doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // a string for saving JSON data
            String StrMovieJSON = null;

            try{

                final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/movie/" + sortMethod;
                final String APIKEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.MY_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    StrMovieJSON = null;

                }else{
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    StrMovieJSON = null;
                }
                StrMovieJSON = buffer.toString();

                //Log.v(LOG_TAG, "Movie string: " + StrMovieJSON);


            }catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (NetworkOnMainThreadException e) {
                //Log.d("Error: ", e.toString());
            }

            try {
                return ExtractDataFromJSON(StrMovieJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieClass> movies) {

            if (movies!=null){
                try {
                    myMovies = new MovieAdapter(getActivity(), movies);

                    gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
                    gridView.setAdapter(myMovies);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                            Context context = getActivity();
                            MovieClass clickedMovie = myMovies.getItem(position);

                            Intent detailIntent = new Intent(context, DetailActivity.class);
                            detailIntent.putExtra("clickedMovie", clickedMovie);
                            startActivity(detailIntent);
                        }
                    });

                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getContext(),"Failed to fetch movie data",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
