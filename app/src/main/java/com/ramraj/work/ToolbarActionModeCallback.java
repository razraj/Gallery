package com.ramraj.work;

import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.ramraj.work.adapter.GalleryAdapter;
import com.ramraj.work.fragment.GalleryFragment;

/**
 * Created by ramraj on 28/6/17.
 */

public class ToolbarActionModeCallback implements ActionMode.Callback {
    private GalleryAdapter adapter;
    private Fragment fragment;

    public ToolbarActionModeCallback(GalleryAdapter adapter, Fragment fragment) {
        this.adapter = adapter;
        this.fragment = fragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.contextual_menu, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        menu.findItem(R.id.item_done).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_done:
                //If current fragment is recycler view fragment
//                Fragment recyclerFragment = new HomeActivity().getGalleryFragment();//Get recycler view fragment
                if (fragment != null)
                    //If recycler fragment not null
                    ((GalleryFragment) fragment).closeEditing();//delete selected rows

                break;

        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //When action mode destroyed remove selected selections and set action mode to null

//        adapter.removeSelection();  // remove selection
        adapter.setEditable(false);
//        Fragment recyclerFragment = new HomeActivity().getGalleryFragment();//Get recycler fragment
        if (fragment != null)
            ((GalleryFragment) fragment).setNullToActionMode();//Set action mode null

    }
}
