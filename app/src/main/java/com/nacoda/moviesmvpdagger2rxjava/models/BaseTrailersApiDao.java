package com.nacoda.moviesmvpdagger2rxjava.models;

import java.util.List;

/**
 * Created by ASUS on 25/09/2017.
 */

public class BaseTrailersApiDao<T> {

    private List<T> results;

    public List<T> getResults() {
        return results;
    }
}
