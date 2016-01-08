package org.cmas.activities.enter;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;
import org.cmas.R;
import org.cmas.activities.BaseActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public abstract class BaseEnterView extends BaseActivity {

    protected BaseEnterView(boolean storeState) {
        super(storeState);
    }

	private Intent backIntent;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:

				//startActivity(backIntent);
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}


    protected <T> void setupHeader(
            String headerTextStr, final Class<T> backActivityClass
    ) {
        setupHeader(headerTextStr, backActivityClass, Collections.<String, Serializable>emptyMap());
    }

    protected <T> void setupHeader(String title, final Class<T> backActivityClass,
                                           final Map<String, Serializable> extraData) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        TextView titleView= (TextView) getLayoutInflater().inflate(R.layout.title_layout,null,false);
        titleView.setText(makeHtmlTitle(title));
        actionBar.setCustomView(titleView);
        actionBar.setDisplayShowCustomEnabled(true);

		if (backActivityClass == null) {
			getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		} else {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			backIntent = new Intent(getBaseContext(), backActivityClass);
			for (Map.Entry<String, Serializable> item : extraData.entrySet()) {
				backIntent.putExtra(item.getKey(), item.getValue());
			}
		}
	}


}
