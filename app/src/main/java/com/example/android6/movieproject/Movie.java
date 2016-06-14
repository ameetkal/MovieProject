package com.example.android6.movieproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android6 on 5/31/16.
 */
public class Movie implements Parcelable {

    public static final String MOVIE = Movie.class.getSimpleName();
    private String title;
    private String posterUrl;
    private String synopsis;
    private int movie_id;
    private float rating;
    private String releaaseDate;



    private String review;
    private String trailer_id;

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    public static String getMOVIE() {
        return MOVIE;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }


    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReleaaseDate() {
        return releaaseDate;
    }

    public void setReleaaseDate(String releaaseDate) {
        this.releaaseDate = releaaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.posterUrl);
        dest.writeString(this.synopsis);
        dest.writeInt(this.movie_id);
        dest.writeFloat(this.rating);
        dest.writeString(this.releaaseDate);
        dest.writeString(this.trailer_id);
        dest.writeString(this.review);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.posterUrl = in.readString();
        this.synopsis = in.readString();
        this.movie_id = in.readInt();
        this.rating = in.readFloat();
        this.releaaseDate = in.readString();
        this.review = in.readString();
        this.trailer_id = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}



