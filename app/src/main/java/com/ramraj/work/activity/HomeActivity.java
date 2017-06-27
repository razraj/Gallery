package com.ramraj.work.activity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ramraj.work.Constants;
import com.ramraj.work.GalleryImagesObserver;
import com.ramraj.work.R;
import com.ramraj.work.fragment.GalleryFragment;

import butterknife.ButterKnife;

import static com.ramraj.work.Constants.HOME_TAG;

public class HomeActivity extends AppCompatActivity {

    private static String TAG = HomeActivity.class.getSimpleName();
    private static final String PERMISSION_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, new GalleryFragment(), HOME_TAG);
            ft.commit();
        } else {
            Log.d(TAG, "savedInstanceState is not null");
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                shouldDisplayHomeUp();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GalleryImagesObserver.getInstance().refreshGallery();
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_STORAGE)) {
            //denied
        } else {
            if (ActivityCompat.checkSelfPermission(this, PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //allowed
                GalleryImagesObserver.getInstance().refreshGallery();
            } else {
                //set to never ask again
            }
        }
    }

    public void showGallery() {
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            GalleryImagesObserver.getInstance().refreshGallery();
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(getString(R.string.dialog_ask_permission_title));
            alertBuilder.setMessage(getString(R.string.dialog_ask_permission_body));
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{PERMISSION_STORAGE}, Constants.STORAGE_PERMISSION);
                }
            });

            AlertDialog alert = alertBuilder.create();
            alert.show();
        }
    }

    public void shouldDisplayHomeUp() {
        boolean canBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
    }
}