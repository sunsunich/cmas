package org.cmas.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.cmas.R;

/**
 * Created on Dec 08, 2015
 *
 * @author Alexander Petukhov
 */
public class FirstPaymentScreen extends SecureFragment {

    public FirstPaymentScreen() {
        super(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_payment, null, false);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView titleView= (TextView) actionBar.getCustomView();
        titleView.setText(makeHtmlTitle(
                getString(R.string.first_login_payment)
        ));

        Button buttonProceedToPayment = (Button) getView().findViewById(R.id.bnt_proceed_to_payment);
        buttonProceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceCurrentMainFragment(getId(), MainScreen.newInstance(null), true);
            }
        });
    }
}
