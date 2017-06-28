package com.ramraj.work.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramraj.work.Constants;
import com.ramraj.work.Events;
import com.ramraj.work.R;
import com.ramraj.work.activity.HomeActivity;
import com.ramraj.work.activity.ImageEditActivity;
import com.ramraj.work.adapter.FavoritesAdapter;
import com.ramraj.work.base.BaseFragment;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.Image;
import com.ramraj.work.utils.RxBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoritesFragment extends BaseFragment {

    private static String TAG = FavoritesFragment.class.getSimpleName();
    private FavoritesAdapter adapter;
    private ArrayList<Image> images;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);
        adapter = new FavoritesAdapter(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        if (images != null)
            recyclerView.setAdapter(adapter);
        else
            observeFavorites();

    }

    private void observeItemClick(){
        rxBusEventDisposable.add(RxBus.getInstance().toObservable().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (o instanceof Events.ImageSelectedEvent) {
                            Log.d(TAG, "accept: selected");
//                            Intent intent = new Intent(getContext(), ImageEditActivity.class);
//                            intent.putExtra("image_url", ((Events.ImageSelectedEvent) o).getImage().getUrl());
//                            startActivity(intent);
                            ((HomeActivity) getActivity()).switchFragment(Constants.EDITIMAGE,((Events.ImageSelectedEvent) o).getImage().getUrl());

                        }
                    }
                }));
    }

    private void observeFavorites() {
        networkDisposable.add(StorageService.getInstance().getAllLikedImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Image>>() {
                    @Override
                    public void accept(@NonNull ArrayList<Image> images) throws Exception {
                        Log.d(TAG, "accept: ");
                        adapter.setImages(images);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, " error in reading favorites ", throwable);
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        observeItemClick();
    }
}
