package org.cmas.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import org.cmas.BaseBeanContainer;
import org.cmas.R;
import org.cmas.service.NavigationService;
import org.cmas.util.android.BundleUtil;
import org.cmas.util.android.SecurePreferences;

/**
 * Created with IntelliJ IDEA.
 * User: sunsunich
 * Date: 20/01/13
 * Time: 19:23
 */
public abstract class BaseActivity<T> extends ActionBarActivity {

    protected final BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
    protected final NavigationService navigationService = beanContainer.getNavigationService();

    private boolean isStoreState;

    protected BaseActivity(boolean storeState) {
        isStoreState = storeState;
    }

    public CharSequence makeHtmlTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return title;
        }
//        String upperTitle = title.toUpperCase();
//        SpannableString text = new SpannableString(upperTitle);
//        text.setSpan(
//                new ForegroundColorSpan(getResources().getColor(R.color.first_letter)),
//                0,
//                1,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//        );
        return title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle(makeHtmlTitle(getString(R.string.app_name)));
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isStoreState) {
            SharedPreferences sharedPreferences = new SecurePreferences(this);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            //must be getClass!!! we want the exact class. not the base class
            editor.putString(Dispatcher.LAST_ACTIVITY_PROPNAME, getClass().getName());

            Bundle extras = getIntent().getExtras();
            try {
                BundleUtil.saveFlatBundle(editor, extras);
            } catch (Exception e) {
                Log.e(BaseActivity.class.getName()
                        , "Error while saving bundle"
                        , e
                );

                editor.putString(
                        Dispatcher.LAST_ACTIVITY_PROPNAME
                        , navigationService.getLoggedOffActivity().getName()
                );
            }
            editor.commit();
        }
    }

    public void reportSaveSuccess() {
        final Activity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(getString(R.string.saved_successfully));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = new Intent(activity, Dispatcher.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog myAlertDialog = builder.create();
        myAlertDialog.show();

    }

    public void reportError(String message) {
        reportError(this, message);
    }

    public static void reportError(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog myAlertDialog = builder.create();
        myAlertDialog.show();
    }
}
