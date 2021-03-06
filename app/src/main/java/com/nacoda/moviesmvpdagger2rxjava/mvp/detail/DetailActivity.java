package com.nacoda.moviesmvpdagger2rxjava.mvp.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appolica.flubber.Flubber;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.daimajia.androidanimations.library.Techniques;
import com.nacoda.moviesmvpdagger2rxjava.BaseActivity;
import com.nacoda.moviesmvpdagger2rxjava.Constants;
import com.nacoda.moviesmvpdagger2rxjava.R;
import com.nacoda.moviesmvpdagger2rxjava.models.CreditsListDao;
import com.nacoda.moviesmvpdagger2rxjava.mvp.youtube.YoutubeActivity;
import com.nacoda.moviesmvpdagger2rxjava.mvp.favorites.db.AddFavoritesViewModel;
import com.nacoda.moviesmvpdagger2rxjava.mvp.favorites.db.FavoritesListViewModel;
import com.nacoda.moviesmvpdagger2rxjava.mvp.favorites.db.FavoritesModel;
import com.nacoda.moviesmvpdagger2rxjava.models.DetailApiDao;
import com.nacoda.moviesmvpdagger2rxjava.models.ParcelableMovies;
import com.nacoda.moviesmvpdagger2rxjava.models.SimilarListDao;
import com.nacoda.moviesmvpdagger2rxjava.models.TrailersListDao;
import com.nacoda.moviesmvpdagger2rxjava.networking.Service;
import com.nacoda.moviesmvpdagger2rxjava.utils.Parcefy;
import com.nacoda.moviesmvpdagger2rxjava.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity implements DetailView {

    @Inject
    Service service;
    @Inject
    Parcefy parcefy;
    @Inject
    Utils utils;

    // Back button Toolbar
    @BindView(R.id.activity_detail_toolbar)
    Toolbar activityDetailToolbar;
    // Poster CardView
    @BindView(R.id.activity_detail_poster_card)
    CardView activityDetailPosterCard;
    // Poster background RelativeLayout
    @BindView(R.id.activity_detail_background_layout)
    RelativeLayout activityDetailBackgroundLayout;
    // Movie title TextView
    @BindView(R.id.activity_detail_title_text_view)
    TextView activityDetailTitleTextView;
    // Movie year of release TextView
    @BindView(R.id.activity_detail_year_text_view)
    TextView activityDetailYearTextView;
    // Movie score TextView
    @BindView(R.id.activity_detail_score_text)
    TextView activityDetailScoreText;
    // Detail Loading Layout
    @BindView(R.id.activity_detail_loading_layout)
    RelativeLayout activityDetailLoadingLayout;
    // Play Trailer Card
    @BindView(R.id.activity_detail_play_trailer_card)
    CardView activityDetailPlayTrailerCard;
    // Favorite Card
    @BindView(R.id.activity_detail_favorite_card)
    CardView activityDetailFavoriteCard;
    // Unfavorite Card
    @BindView(R.id.activity_detail_unfavorite_card)
    CardView activityDetailUnfavoriteCard;
    // Detail ScrollView
    @BindView(R.id.activity_detail_scroll_view)
    ScrollView activityDetailScrollView;
    // Movie Genres TextView
    @BindView(R.id.activity_detail_genres_text)
    TextView activityDetailGenresText;
    // Movie Overview TextView
    @BindView(R.id.activity_detail_overview_text_view)
    TextView activityDetailOverviewTextView;
    // Movies Poster Zoom
    @BindView(R.id.activity_detail_poster_zoom_card)
    CardView activityDetailPosterZoomCard;
    // Movies Zoom Frame
    @BindView(R.id.activity_detail_zoom_relative)
    RelativeLayout activityDetailZoomFrame;
    // Movies Overview Navigate Layout
    @BindView(R.id.activity_detail_overview_navigate_layout)
    RelativeLayout activityDetailOverviewNavigateLayout;
    // Movies Credits Navigate Layout
    @BindView(R.id.activity_detail_credits_navigate_layout)
    RelativeLayout activityDetailCreditsNavigateLayout;
    // Movies Overview Navigate Text
    @BindView(R.id.activity_detail_overview_navigate_text)
    TextView activityDetailOverviewNavigateText;
    // Movies Credits Navigate Text
    @BindView(R.id.activity_detail_credits_navigate_text)
    TextView activityDetailCreditsNavigateText;
    // Movies Overview Linear
    @BindView(R.id.activity_detail_overview_linear)
    LinearLayout activityDetailOverviewLinear;
    // Movies Credits Linear
    @BindView(R.id.activity_detail_credits_linear)
    LinearLayout activityDetailCreditsLinear;


    @OnClick(R.id.activity_detail_overview_navigate_layout)
    public void OnOverviewClick() {
        initOverview(activityDetailOverviewNavigateLayout,
                activityDetailCreditsNavigateLayout,
                activityDetailOverviewNavigateText,
                activityDetailCreditsNavigateText,
                activityDetailOverviewLinear,
                activityDetailCreditsLinear
        );
    }

    @OnClick(R.id.activity_detail_credits_navigate_layout)
    public void OnCreditsClick() {
        initCredits(activityDetailOverviewNavigateLayout,
                activityDetailCreditsNavigateLayout,
                activityDetailOverviewNavigateText,
                activityDetailCreditsNavigateText,
                activityDetailOverviewLinear,
                activityDetailCreditsLinear
        );
    }

    ParcelableMovies parcelableMovies;
    FavoritesListViewModel viewModel;
    AddFavoritesViewModel addViewModel;
    DetailPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        onSetupToolbar(activityDetailToolbar);
        getApplicationComponent().inject(this);
        getParcelable();

        initRetrofit(getIntent().getStringExtra("id"),
                service,
                presenter);

        initBackground(this,
                Constants.IMAGE_URL + parcelableMovies.getBackdrop_path(),
                activityDetailBackgroundLayout);

        initPoster(this,
                Constants.IMAGE_URL + parcelableMovies.getPoster_path(),
                activityDetailPosterCard);

        initPoster(this,
                Constants.IMAGE_URL + parcelableMovies.getPoster_path(),
                activityDetailPosterZoomCard);

        initMovieDescription(
                activityDetailTitleTextView,
                activityDetailYearTextView,
                activityDetailScoreText,
                activityDetailGenresText,
                activityDetailOverviewTextView
        );

        initFavoritesFunction(activityDetailFavoriteCard, activityDetailUnfavoriteCard);
    }

    private void initOverview(View overviewLayout, View creditsLayout, TextView overviewText, TextView creditsText, View overviewLinear, View creditsLinear) {
        overviewLayout.setBackgroundResource(R.drawable.rounded_accent_left);
        utils.yoyo(overviewLayout, Techniques.FadeInRight, 500);
        creditsLayout.setBackgroundResource(0);
        overviewText.setTextColor(getResources().getColor(R.color.white));
        creditsText.setTextColor(getResources().getColor(R.color.colorAccent));
        utils.revealWithChild(overviewLinear, 200, 100);
        creditsLinear.setVisibility(View.GONE);

    }

    private void initCredits(View overviewLayout, View creditsLayout, TextView overviewText, TextView creditsText, View overviewLinear, View creditsLinear) {
        creditsLayout.setBackgroundResource(R.drawable.rounded_accent_right);
        utils.yoyo(creditsLayout, Techniques.FadeInLeft, 500);
        overviewLayout.setBackgroundResource(0);
        creditsText.setTextColor(getResources().getColor(R.color.white));
        overviewText.setTextColor(getResources().getColor(R.color.colorAccent));
        utils.revealWithChild(creditsLinear, 200, 100);
        overviewLinear.setVisibility(View.GONE);

    }

    /**
     * This method is used to set the value of the given TextView according to the selected movie's data
     *
     * @param titleTextView    The TextView used to show the title of the movie
     * @param yearTextView     The TextView used to show the year of release of the movie (For this one, I used the substring(0,4)
     *                         so that the TextView will only show the year of release, that is the first 4 characters of the data
     * @param scoreTextView    The TextView used to show the score or the vote average of the movie
     * @param genresTextView   The TextView used to show the genre of the movie
     * @param overViewTextView The TextView used to show the synopsis or overview of the movie
     **/
    private void initMovieDescription(TextView titleTextView, TextView yearTextView, TextView scoreTextView, TextView genresTextView, TextView overViewTextView) {
        titleTextView.setText(parcelableMovies.getTitle());
        yearTextView.setText(parcelableMovies.getRelease_date().substring(0, 4));
        scoreTextView.setText(String.valueOf((parcelableMovies.getScore())));
        genresTextView.setText(parcelableMovies.getGenres());
        overViewTextView.setText(parcelableMovies.getOverview());
    }

    /**
     * This method is used to set the background of the poster card inside the content_detail.xml
     *
     * @param context  The Context of the activity
     * @param url      The URL to the poster
     * @param cardView The View that we want to set the poster as the cardview's background
     *                 <p>
     *                 P.S: This Glide method is using the setForeground because we cannot use setBackground to set the card's background
     */
    private void initPoster(Context context, String url, final CardView cardView) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>(400, 400) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    cardView.setForeground(drawable);
                }
            }
        });
    }

    /**
     * This method is used to set the background of the detail activity with the selected movie backdrop
     *
     * @param context The Context of the activity
     * @param url     The URL to the poster
     * @param view    The View that we want to set the poster as the view's background
     */
    private void initBackground(Context context, String url, final View view) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>(400, 400) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
    }

    /**
     * This method is overrided so that when the backbutton is pressed, it will navigate the user to the previous activity
     **/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * This method is used to make the title disappear and add the back button to the appbar
     *
     * @param toolbar The toolbar used for the backbutton
     */
    public void onSetupToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_back);

        }
    }

    /**
     * This method is used to get the parcelable of movies sent from the previous activity using Intent and fill the
     * global
     */
    private void getParcelable() {
        parcelableMovies = getIntent().getParcelableExtra("parcelableMovies");
    }

    /**
     * This method is used to initialize the Retrofit to the the data from TheMovieDatabase
     * <p>
     * The data that we're going to get is the detail of the selected movie
     *
     * @param id        The id of the selected movie
     * @param service   The Service class which contains the necessary methods to initialize the Retrofit and get the data using Reactivex
     * @param presenter The Presenter class which contains the necessary methods to run the methods inside the Service class
     */
    public void initRetrofit(String id, Service service, DetailPresenter presenter) {
        presenter = new DetailPresenter(service, this);
        presenter.getMoviesDetail(id);
        presenter.getTrailers(id);
        presenter.getSimilar(id);
        presenter.getCredits(id);
    }

    /**
     * This method is used to initialize the favorites function
     *
     * @param Favorite_Card   The Favorite Card
     * @param Unfavorite_Card The Unfavorite Card
     *                        <p>
     *                        Functions for loop and if else below:
     *                        Loop through the database
     *                        <p>
     *                        If found that the id of the movie from the intent is the same as the one found at the database, then
     *                        that means the user already favorited that movie and the FavoriteCard will be gone and the UnfavoriteCard will be visible
     *                        <p>
     *                        If not, then that means the user haven't favorited that movie yet and the FavoriteCard will be visible and
     *                        the UnfavoriteCard will be gone
     */
    private void initFavoritesFunction(final CardView Favorite_Card, final CardView Unfavorite_Card) {
        viewModel = ViewModelProviders.of(this).get(FavoritesListViewModel.class);
        addViewModel = ViewModelProviders.of(this).get(AddFavoritesViewModel.class);

        viewModel.getFavoritesList().observe(DetailActivity.this, new Observer<List<FavoritesModel>>() {
            @Override
            public void onChanged(@Nullable List<FavoritesModel> favorites) {
                assert favorites != null;
                for (int i = 0; i < favorites.size(); i++) {
                    if (favorites.get(i).getMovieId().equals(parcelableMovies.getId())) {
                        final FavoritesModel favoritesModel = (FavoritesModel) favorites.get(i);
                        Favorite_Card.setVisibility(View.GONE);
                        Unfavorite_Card.setVisibility(View.VISIBLE);
                        Unfavorite_Card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                viewModel.deleteFavorites(favoritesModel);
                                Unfavorite_Card.setVisibility(View.GONE);
                                Favorite_Card.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }
        });

        Favorite_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(activityDetailScrollView, "Added to favorites!", Snackbar.LENGTH_SHORT).show();
                addViewModel.addFavorites(Constants.FAVORITES_MODEL(parcelableMovies));
                Favorite_Card.setVisibility(View.GONE);
                Unfavorite_Card.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showWait() {
//        activityDetailLoadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        utils.flubber(activityDetailLoadingLayout, 300, Flubber.AnimationPreset.FADE_OUT);
        activityDetailScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void getMoviesDetailSuccess(DetailApiDao detailApiDao) {

    }

    @Override
    public void getTrailersSuccess(final TrailersListDao trailersListDao) {
        activityDetailPlayTrailerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trailersListDao != null) {
                    Intent trailer = new Intent(DetailActivity.this, YoutubeActivity.class);
                    trailer.putExtra("videoKey", trailersListDao.getResults().get(0).getKey());
                    startActivity(trailer);
                } else {
                    Toast.makeText(DetailActivity.this, "No Trailers available for this movie", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void getSimilarSuccess(SimilarListDao similarListDao) {

        /** if (similarListDao.getResults().size() == 0) {
         activityDetailSimilarLinearLayout.setVisibility(View.GONE);
         }

         activityDetailContentRelativeLayout.setVisibility(View.GONE);
         dialog.show();

         MoviesSimilarAdapter adapter = new MoviesSimilarAdapter(getApplicationContext(), similarListDao, utils, gliding,
         new MoviesSimilarAdapter.OnItemClickListener() {
        @Override public void onClick(SimilarApiDao item) {
        String genres = utils.getGenres(item.getGenre_ids());
        ParcelableMovies movies = parcefy.fillSimilarParcelable(item, genres);
        Intent detail = new Intent(DetailActivity.this, DetailActivity.class);
        detail.putExtra("parcelableMovies", movies);
        detail.putExtra("id", item.getId());
        startActivity(detail);
        }
        });

         utils.initRecyclerView(DetailActivity.this, activityDetailRvSimilar, adapter);
         dialog.dismiss();
         activityDetailContentRelativeLayout.setVisibility(View.VISIBLE); **/
    }

    @Override
    public void getCreditsSuccess(CreditsListDao creditsListDao) {

    }

    @Override
    public void onBackPressed() {
        if (activityDetailZoomFrame.getVisibility() == View.VISIBLE) {
            utils.unreveal(activityDetailZoomFrame, 300);
        } else {
            finish();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick(R.id.activity_detail_poster_card)
    public void onClick() {
        utils.revealWithChild(activityDetailZoomFrame, 300, 100);
    }
}
