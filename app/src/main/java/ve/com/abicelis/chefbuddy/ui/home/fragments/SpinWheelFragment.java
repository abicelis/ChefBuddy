package ve.com.abicelis.chefbuddy.ui.home.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import ve.com.abicelis.chefbuddy.R;

/**
 * Created by abicelis on 9/7/2017.
 */

public class SpinWheelFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Hide search menu item
        menu.findItem(R.id.menu_home_search).setVisible(false);
    }
}
