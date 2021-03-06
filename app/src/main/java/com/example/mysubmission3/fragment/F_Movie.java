package com.example.mysubmission3.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mysubmission3.DetailMovie;
import com.example.mysubmission3.R;
import com.example.mysubmission3.adapter.MovieAdapter;
import com.example.mysubmission3.model.Movie;
import com.example.mysubmission3.vm.MovieVM;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class F_Movie extends Fragment {
    private MenuItem mSearchItem;
    private SearchView sv;
    private RecyclerView rvMovie;
    private MovieAdapter mp;
    private ProgressBar pb;
    private MovieVM mvm, mf;
    private String mSearchString;
    private static final String SEARCH_KEY = "search";
    private View vw;

    public F_Movie() {
        // Required empty public constructor


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(sv != null ){
            mSearchString = sv.getQuery().toString();
            outState.putString(SEARCH_KEY, mSearchString);
            super.onSaveInstanceState(outState);
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f__movie, container, false);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mSearchString = savedInstanceState.getString(SEARCH_KEY);
        }
        mp = new MovieAdapter();
        mp.notifyDataSetChanged();
        pb = (ProgressBar)v.findViewById(R.id.pbar);
        pb.setVisibility(View.VISIBLE);
        initializeRecycler(v);
        showMVMovieList();

        return v;
    }

    private void showMVFilterMovie(String t) {
        mf = new ViewModelProvider(getActivity(), new ViewModelProvider.NewInstanceFactory())
                .get(MovieVM.class);

        mf.getFilterMovies().observe(getActivity(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if(movies != null){
                    mp.filterMovie(movies);
                    showLoading(false);
                }
            }
        });
        mf.setFilter(t);
        mp.setOnItemClickCallBack(new MovieAdapter.onItemClickCallBack() {
            @Override
            public void onItemClicked(Movie data) {
                Intent dm = new Intent(getActivity(), DetailMovie.class);
                dm.putExtra(DetailMovie.EXTRA_MOVIE, data);
                startActivity(dm);
            }
        });
    }

    private void showMVMovieList(){
        mvm = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(MovieVM.class);
        mvm.getListMovies().observe(getActivity(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if(movies != null){
                    mp.setData(movies);
                }
                showLoading(false);

            }
        });
        mvm.setMovie();
        mp.setOnItemClickCallBack(new MovieAdapter.onItemClickCallBack() {
            @Override
            public void onItemClicked(Movie data) {
                Intent dm = new Intent(getActivity(), DetailMovie.class);
                dm.putExtra(DetailMovie.EXTRA_MOVIE, data);
                startActivity(dm);
            }
        });
    }
    private void initializeRecycler(View v) {
        rvMovie = (RecyclerView)v.findViewById(R.id.rv_movie);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        rvMovie.setAdapter(mp);
    }



    private void showLoading(Boolean state) {
        if (state) {
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(View.GONE);
        }
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        mSearchItem = menu.findItem(R.id.search);
        sv = (SearchView)mSearchItem.getActionView();
        sv.setMaxWidth(Integer.MAX_VALUE);
        sv.setQueryHint(getResources().getString(R.string.search_title));

        sv.setIconified(false);
        if (mSearchString != null && !mSearchString.isEmpty()) {

                mSearchItem.expandActionView();
                sv.setQuery(mSearchString, true);
                //  sv.clearFocus();
                sv.setFocusable(true);
                showMVFilterMovie(mSearchString);
        }

       sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String t = newText.toLowerCase();
                if(TextUtils.isEmpty(t)){

                    showMVMovieList();

                }else {
                    showMVFilterMovie(t);
                }
                return true;
            }
        });
        mSearchItem.setActionView(sv);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_change_settings){
            Intent i = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(i);
        }

        return true;
    }
}
