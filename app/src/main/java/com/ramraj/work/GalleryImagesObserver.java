package com.ramraj.work;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.ramraj.work.model.Image;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by ramraj on 27/6/17.
 */

public enum GalleryImagesObserver {
    INSTANCE;
    private static String TAG = GalleryImagesObserver.class.getSimpleName();
    Single observableSingle;
    private Context context;

    public static GalleryImagesObserver getInstance() {
        return INSTANCE;
    }

    GalleryImagesObserver() {
        context = App.getAppContext();
    }

    public void refreshGallery() {
        getObservable();
    }

    public Single<ArrayList<Image>> getObservable() {
        observableSingle = Single.create(new SingleOnSubscribe() {
            @Override
            public void subscribe(@NonNull SingleEmitter e) throws Exception {
                Cursor cursor = null;
                try {
                    Uri uri;
                    ArrayList<Image> images = new ArrayList<Image>();
                    int column_index_data, column_index_folder_name;
                    String pathOfImage, nameOfImage = null;
                    uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                    String[] projection = {MediaStore.MediaColumns.DATA,
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

                    cursor = context.getContentResolver().query(uri, projection, null,
                            null, null);

                    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    column_index_folder_name = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    while (cursor.moveToNext()) {
                        Image image = new Image();
                        pathOfImage = cursor.getString(column_index_data);
                        nameOfImage = cursor.getString(column_index_folder_name);

//                        listOfAllImages.add(PathOfImage);
                        image.setName(nameOfImage);
                        image.setUrl(pathOfImage);
                        images.add(image);

                    }
                    e.onSuccess(images);
                } catch (Exception exception) {
                    Log.d(TAG, "subscribe: error" + exception);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

            }
        });

        return observableSingle;
    }
}
