package org.cmas.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.cmas.R;
import org.cmas.fragments.logbook.LogbookFragment;
import org.cmas.fragments.user.UserAccount;

/**
 * User: ABadretdinov
 * Date: 18.12.13
 * Time: 19:20
 */
public class MainScreen extends BaseResultViewFragment {

    public static MainScreen newInstance(Bundle data) {
        MainScreen fragment = new MainScreen();
        fragment.setArguments(data);
        return fragment;
    }

    public MainScreen() {
        super(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            loaderLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupHeader(
                getString(R.string.app_name),
                null
        );
        // todo logoff button only for testing purposes
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button buttonUserAccount = (Button) getView().findViewById(R.id.bnt_user_account);
        buttonUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceCurrentMainFragment(getId(), UserAccount.newInstance(null), true);
            }
        });

        Button buttonLogBook = (Button) getView().findViewById(R.id.bnt_logbook);
        buttonLogBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceCurrentMainFragment(getId(), LogbookFragment.newInstance(null), true);
            }
        });
    }

}
