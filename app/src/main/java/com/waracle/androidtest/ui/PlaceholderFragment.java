package com.waracle.androidtest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.waracle.androidtest.R;
import com.waracle.androidtest.api.ApiClient;
import com.waracle.androidtest.datamodel.Cake;
import com.waracle.androidtest.ui.adapter.MyAdapter;

import java.util.List;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Cake>>{

    private static final String TAG = PlaceholderFragment.class.getSimpleName();
    public static final int CAKES_ASYNC_LOADER = 1;

    private ApiClient mApiClient;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApiClient = ApiClient.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isAdded()) {
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            if (loaderManager.getLoader(CAKES_ASYNC_LOADER) == null) {
                loaderManager.initLoader(CAKES_ASYNC_LOADER, null, this);
            } else {
                loaderManager.restartLoader(CAKES_ASYNC_LOADER, null, this);
            }
        }
    }

    @Override
    public Loader<List<Cake>> onCreateLoader(int id, Bundle args) {
        if (mApiClient != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            return mApiClient.createLoaderForCakes();
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Cake>> loader, List<Cake> data) {
        mProgressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            MyAdapter myAdapter = new MyAdapter(loader.getContext(), data);
            mRecyclerView.setAdapter(myAdapter);
        } else {
            if (isAdded()) {
                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Cake>> loader) {
    }
}