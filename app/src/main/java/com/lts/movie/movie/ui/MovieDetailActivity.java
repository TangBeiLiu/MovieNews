package com.lts.movie.movie.ui;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lts.movie.R;
import com.lts.movie.base.BaseActivity;
import com.lts.movie.base.BaseFragment;
import com.lts.movie.bean.MovieDetail;
import com.lts.movie.constant.Constant;
import com.lts.movie.movie.MovieFragmentAdapter;
import com.lts.movie.movie.presenter.MovieDeatilPresenterImpl;
import com.lts.movie.movie.presenter.MovieDetailPresenter;
import com.lts.movie.movie.view.MovieDeatilView;
import com.lts.movie.widget.AppBarStateChangeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lts on 2017/9/3.
 * Fuction:
 * Update:
 */

public class MovieDetailActivity extends BaseActivity<MovieDetailPresenter> implements MovieDeatilView {


    private ImageView mMovieBackgound;
    private ImageView mMovieLogo;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFab;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mMovieName;
    private TextView mMovieType;
    private String mTitle;
    private int mMovieId;

    @Override
    public int bindingView() {
        return R.layout.activity_movie_detail;
    }


    @Override
    public boolean isHasNavigationView() {
        return false;
    }

    @Override
    public void initView() {
        initActionBoutton();

        mMovieBackgound = (ImageView) findViewById(R.id.movie_background_photo);
        mMovieLogo = (ImageView) findViewById(R.id.movie_logo);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mMovieName = (TextView) findViewById(R.id.movie_name);
        mMovieType = (TextView) findViewById(R.id.movie_type);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.material_white));
        mMovieId = getIntent().getIntExtra(Constant.movie_id, -1);
        initViewPag();

        mPresenter = new MovieDeatilPresenterImpl(this, mMovieId,getResources().getString(R.string.language));
    }


    private void initViewPag() {
        String[] stringArray = getResources().getStringArray(R.array.movie_info_tab);
        BaseFragment[] fragments = {MovieOverViewFragment.newIntences(mMovieId), CastFragment.newIntences(mMovieId), ReviewFragment.newIntences(mMovieId)};

        MovieFragmentAdapter adapter = new MovieFragmentAdapter(getSupportFragmentManager(),fragments,stringArray);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initActionBoutton() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    mFab.show();
                    mToolbar.setTitle("");
                } else if (state == State.COLLAPSED) {
                    mToolbar.setTitle(mTitle);
                    mFab.hide();
                } else {
                    //中间状态
                    mFab.show();
                }

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMovieDetail(MovieDetail movieDetail) {
        mTitle = movieDetail.getTitle();
        Picasso.with(this).load(Constant.logUrl + movieDetail.getPoster_path()).into(mMovieLogo);
        Picasso.with(this).load(Constant.backgoundUrl + movieDetail.getBackdrop_path()).resize(mMovieBackgound.getWidth()/2,mMovieBackgound.getHeight()/2).into(mMovieBackgound);
        mToolbarLayout.setTitle(movieDetail.getTitle());
        mMovieName.setText(movieDetail.getTitle());
        mMovieType.setText(getMovieType(movieDetail.getGenres()));
    }

    private String getMovieType(List<MovieDetail.GenresBean> genres) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < genres.size(); i++) {
            sb.append(genres.get(i).getName());
            if (i != genres.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
