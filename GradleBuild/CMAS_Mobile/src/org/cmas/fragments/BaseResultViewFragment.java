package org.cmas.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 12:08
 */
public abstract class BaseResultViewFragment extends SecureFragment {
    protected BaseResultViewFragment(boolean storeState) {
        super(storeState);
    }

    private SecureFragment backFragment;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            moveBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveBack() {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        //первое состояние - пустой экран. на него нам возвращаться не нужно.
        if (supportFragmentManager.getBackStackEntryCount() <=1) {
            if (backFragment == null) {
                backFragment = navigationService.getMainFragment(new Bundle());
            }//todo мб передавать какие-то данные из следующего экрана?
            replaceCurrentMainFragment(getId(), backFragment, false, false);
        } else {
            supportFragmentManager.popBackStack();
        }
    }


    protected <T extends SecureFragment> void setupHeader(
            String headerTextStr, final Class<T> backFragmentClass
    ) {
        setupHeader(headerTextStr, backFragmentClass, new Bundle());
    }

    protected <T extends SecureFragment> void setupHeader(String title, final Class<T> backFragmentClass,
                                                          final Bundle extraData)  {
        //TextView headerText = (TextView) findViewById(R.id.header_text);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        TextView titleView= (TextView) actionBar.getCustomView();
        titleView.setText(makeHtmlTitle(
                currentUsername
        ));
        //actionBar.setSubtitle(subtitle);
        if (backFragmentClass == null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Bundle bundle = new Bundle();
            if (extraData != null) {
                bundle.putAll(extraData);
            }
            try {
               backFragment = newInstance(backFragmentClass, bundle);
            } catch (Exception e) {
                Log.e(getClass().getName(), "Error while creating back fragment", e);
                backFragment = navigationService.getMainFragment(bundle);
            }

        }
    }
}
