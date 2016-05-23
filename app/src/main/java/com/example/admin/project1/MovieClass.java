package com.example.admin.project1;

import android.os.Parcel;
import android.os.Parcelable;
// A class that defines my movie objects
public class MovieClass implements Parcelable{

    String movieOriginalTitle;
    String movieVoteAverage;
    String movieReleaseDate;
    String movieOverview;
    String moviePopularity;
    String moviePosterPath;
    String movieBackdropPath;


    public MovieClass(String original_title, String vote_average, String release_date, String overview, String popularity, String poster_path, String backdrop_path)
    {
        this.movieOriginalTitle = original_title;
        this.movieVoteAverage = vote_average;
        this.movieReleaseDate = release_date;
        this.movieOverview = overview;
        this.moviePopularity = popularity;
        this.moviePosterPath = poster_path;
        this.movieBackdropPath = backdrop_path;
    }

    private MovieClass(Parcel in)
    {
        movieOriginalTitle = in.readString();
        movieVoteAverage = in.readString();
        movieReleaseDate = in.readString();
        movieOverview = in.readString();
        moviePopularity = in.readString();
        moviePosterPath = in.readString();
        movieBackdropPath = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public String toString()
    {
        return movieOriginalTitle + "--" + movieVoteAverage + "--" + movieReleaseDate +
                "--" + movieOverview + "--" + moviePopularity + "--" + moviePosterPath +
                "--" + movieBackdropPath;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(movieOriginalTitle);
        parcel.writeString(movieVoteAverage);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(movieOverview);
        parcel.writeString(moviePopularity);
        parcel.writeString(moviePosterPath);
        parcel.writeString(movieBackdropPath);
    }

    public String getOiginalTitle() {
        return this.movieOriginalTitle;
    }

    public String getVoteAverage() {
        return this.movieVoteAverage;
    }

    public String getReleaseDate() {
        return this.movieReleaseDate;
    }

    public String getOverview() {
        return this.movieOverview;
    }

    public String getPopularity() {
        return this.moviePopularity;
    }

    public String getPosterPath() {
        return this.moviePosterPath;
    }

    public String getBackdropPath() {
        return this.movieBackdropPath;
    }

    public static final Parcelable.Creator<MovieClass> CREATOR = new Parcelable.Creator<MovieClass>() {
        @Override
        public MovieClass createFromParcel(Parcel parcel)
        {
            return new MovieClass(parcel);
        }

        @Override
        public MovieClass[] newArray(int i) {
            return new MovieClass[i];
        }
    };
}
