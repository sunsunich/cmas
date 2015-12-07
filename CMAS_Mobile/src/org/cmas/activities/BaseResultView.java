package org.cmas.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.cmas.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public abstract class BaseResultView extends SecureActivity {

    protected BaseResultView(boolean storeState) {
        super(storeState);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
		return true;
	}

	private Intent backIntent;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            startActivity(backIntent);
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

    protected <T> void setupHeader(String headerTextStr, final Class<T> backActivityClass,
                                   final Map<String, Serializable> extraData) {
        //TextView headerText = (TextView) findViewById(R.id.header_text);
		getSupportActionBar().setTitle(makeHtmlTitle(headerTextStr));
		if(backActivityClass!=null){
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			backIntent = new Intent(getBaseContext(), backActivityClass);
            if(extraData!=null){
                for (Map.Entry<String, Serializable> item : extraData.entrySet()) {
                    backIntent.putExtra(item.getKey(), item.getValue());
                }
            }
		}
		else {
			getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}
    }
}
