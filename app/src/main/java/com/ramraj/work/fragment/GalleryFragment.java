package com.ramraj.work.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramraj.work.GalleryImagesObserver;
import com.ramraj.work.R;
import com.ramraj.work.activity.HomeActivity;
import com.ramraj.work.adapter.GalleryAdapter;
import com.ramraj.work.base.BaseFragment;
import com.ramraj.work.model.Image;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ramraj on 27/6/17.
 */

public class GalleryFragment extends BaseFragment {

    private GalleryAdapter adapter;
    private static String TAG = GalleryFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new GalleryAdapter(getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        ObserveGallery();
        ((HomeActivity) getActivity()).showGallery();

    }

    private void ObserveGallery() {
        if (disposable == null)
            disposable = GalleryImagesObserver.getInstance().getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArrayList<Image>>() {
                        @Override
                        public void accept(@NonNull ArrayList<Image> images) throws Exception {
                            Log.d(TAG, "accept: " + images.size());
                            adapter.setImages(images);
                        }
                    });
    }


}
