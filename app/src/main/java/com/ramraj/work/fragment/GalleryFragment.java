package com.ramraj.work.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
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
import com.ramraj.work.Events;
import com.ramraj.work.GalleryImagesObserver;
import com.ramraj.work.R;
import com.ramraj.work.ToolbarActionModeCallback;
import com.ramraj.work.activity.HomeActivity;
import com.ramraj.work.adapter.GalleryAdapter;
import com.ramraj.work.base.BaseFragment;
import com.ramraj.work.db.StorageService;
import com.ramraj.work.model.Image;
import com.ramraj.work.utils.RxBus;
import com.ramraj.work.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;

/**
 * Created by ramraj on 27/6/17.
 */

public class GalleryFragment extends BaseFragment {

    private GalleryAdapter adapter;
    private static String TAG = GalleryFragment.class.getSimpleName();
    private ArrayList<Image> likedImages = new ArrayList<>();
    private ActionMode mActionMode;
    private ArrayList<Image> images;


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
        observeFavoriteSelection();
        if (images == null)
            ObserveGallery();
        else
            adapter.setImages(images);
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
                    public void accept(@NonNull ArrayList<Image> imageList) throws Exception {
                        Log.d(TAG, "accept: " + imageList.size());
                        for (Image image : imageList) {
                            Image liked_image = StorageService.getInstance().getLikedImage(image.getUrl());
                            if (liked_image != null) {
                                imageList.get(imageList.indexOf(image)).setLiked(true);
                            }
                        }
                        images = imageList;
                        adapter.setImages(imageList);
                    }
                }));

    }

    private void observeFavoriteSelection(){
        rxBusEventDisposable.add(RxBus.getInstance().toObservable().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (o instanceof Events.OnItemClicked) {
//                            If ActionMode not null select item
                            if (mActionMode != null)
                                onListItemSelected(((Events.OnItemClicked) o).getPosition());
                        } else if (o instanceof Events.OnItemLongClicked) {
                            onListItemSelected(((Events.OnItemLongClicked) o).getPosition());

                        }
                    }
                }));

    }

    private void onListItemSelected(int position) {
        adapter.toogleSelection(position);
        boolean hasCheckedItems = adapter.getLikedImages() > 0;

        if (hasCheckedItems && mActionMode == null) {
//             there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeCallback( adapter, this));
        } else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            setNullToActionMode();
        }

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(adapter
                    .getLikedImages()) + " selected");
    }

    @OnClick(R.id.favorites_button)
    void onFavoritesClick() {
        if (mActionMode != null)
            mActionMode.finish();
        RealmResults<Image> images = StorageService.getInstance().getRealmInstance().where(Image.class).findAll();
        if (images.size() > 0)
            ((HomeActivity) getActivity()).switchFragment(Constants.FAVORITES, "");
        else
            Utils.showToast("Looks like favorites is empty");
    }

    public void closeEditing() {
        mActionMode.finish();//Finish action mode after use
        setNullToActionMode();
    }

    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        setNullToActionMode();
    }
}
