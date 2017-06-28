package com.ramraj.work.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ramraj.work.Constants;
import com.ramraj.work.GalleryImagesObserver;
import com.ramraj.work.R;
import com.ramraj.work.activity.HomeActivity;
import com.ramraj.work.adapter.GalleryAdapter;
import com.ramraj.work.base.BaseFragment;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.Image;
import com.ramraj.work.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;

/**
 * Created by ramraj on 27/6/17.
 */

public class GalleryFragment extends BaseFragment {

    private GalleryAdapter adapter;
    private static String TAG = GalleryFragment.class.getSimpleName();
    private ArrayList<Image> likedImages = new ArrayList<>();


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.favorites_button)
    Button favoritesButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        adapter = new GalleryAdapter(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        ObserveGallery();
        ((HomeActivity) getActivity()).showGallery();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menuItemFavorites:
                adapter.isEditable = false;
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ObserveGallery() {
        networkDisposable.add(GalleryImagesObserver.getInstance().getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArrayList<Image>>() {
                        @Override
                        public void accept(@NonNull ArrayList<Image> images) throws Exception {
                            Log.d(TAG, "accept: " + images.size());
                            for (Image image : images) {
                                Image liked_image = StorageService.getInstance().getLikedImage(image.getUrl());
                                if (liked_image != null) {
                                    images.get(images.indexOf(image)).setLiked(true);
                                }
                            }
                            adapter.setImages(images);
                        }
                    }));
    }

    @OnClick(R.id.favorites_button)
    void onFavoritesClick() {
        RealmResults<Image> images = StorageService.getInstance().getRealmInstance().where(Image.class).findAll();
        if (images.size() > 0)
            ((HomeActivity) getActivity()).switchFragment(Constants.FAVORITES,"");
        else
            Utils.showToast("Looks like favorites is empty");
    }

}
