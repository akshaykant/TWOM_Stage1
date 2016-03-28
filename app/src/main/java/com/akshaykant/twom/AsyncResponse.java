package com.akshaykant.twom;


import java.util.List;

public interface AsyncResponse {

    void onTaskCompleted( List<Movie> results );

}