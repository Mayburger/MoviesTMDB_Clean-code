package com.nacoda.moviesmvpdagger2rxjava.mvp.movies;


import com.nacoda.moviesmvpdagger2rxjava.models.MoviesListDao;
import com.nacoda.moviesmvpdagger2rxjava.networking.NetworkError;
import com.nacoda.moviesmvpdagger2rxjava.networking.Service;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MoviesPresenter {
    private final Service service;
    private final MoviesView view;
    private CompositeSubscription subscriptions;

    public MoviesPresenter(Service service, MoviesView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getPopularList(int page) {

        view.showWait();
        Subscription subscription = service.getPopularList(new Service.GetPopularCallback() {
            @Override
            public void onSuccess(MoviesListDao moviesListDao) {
                view.removeWait();
                view.getPopularListSuccess(moviesListDao);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        }, page);

        subscriptions.add(subscription);
    }

    public void getTopRatedList(int page) {

        view.showWait();
        Subscription subscription = service.getTopRatedList(new Service.GetTopRatedCallback() {
            @Override
            public void onSuccess(MoviesListDao moviesListDao) {
                view.removeWait();
                view.getTopRatedListSuccess(moviesListDao);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        }, page);

        subscriptions.add(subscription);
    }

    public void getNowPlayingList(int page) {

        view.showWait();
        Subscription subscription = service.getNowPlayingList(new Service.GetNowPlayingCallback() {
            @Override
            public void onSuccess(MoviesListDao moviesListDao) {
                view.removeWait();
                view.getNowPlayingListSuccess(moviesListDao);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.removeWait();
                view.onFailure(networkError.getAppErrorMessage());
            }

        }, page);

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}