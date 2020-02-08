package com.example.mymovies;

/**
 * Created by tameram on 13/5/2019.
 */
public class theMovie {
    private long id;
    private String title , actors,director,genre,plot ;
    private String year , runTime;
    private String theImage;
    private String ifWatched ;

    public theMovie(String title , String actors, String director, String genre, String plot , String year , String runTime, String theImage, String ifWatched){
        this.title=title;
        this.actors=actors;
        this.director=director;
        this.plot=plot;
        this.runTime=runTime;
        this.genre=genre;
        this.theImage=theImage;
        this.year=year;
        this.ifWatched=ifWatched;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTheImage() {
        return theImage;
    }

    public void setTheImage(String theImage) {
        this.theImage = theImage;
    }
    public String isIfWatched() {
        return ifWatched;
    }

    public void setIfWatched(String ifWatched) {
        this.ifWatched = ifWatched;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "The Film is : "+getTitle();
    }


}

