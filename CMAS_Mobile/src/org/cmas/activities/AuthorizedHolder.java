package org.cmas.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActionBarActivity;
import org.cmas.BaseBeanContainer;
import org.cmas.SettingsService;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.fragments.MenuFragment;
import org.cmas.mobile.R;
import org.cmas.service.NavigationService;
import org.cmas.util.StringUtil;
import org.cmas.util.android.BundleUtil;
import org.cmas.util.android.SecurePreferences;

/**
 * User: ABadretdinov
 * Date: 18.12.13
 * Time: 18:56
 */
public class AuthorizedHolder extends SlidingActionBarActivity {

    public static final String FRAGMENT_PROP_NAME = "mContent";

    private BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();
    private NavigationService navigationService = baseBeanContainer.getNavigationService();
    private SettingsService settingsService = baseBeanContainer.getSettingsService();

    private Fragment mContent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings) {
            toggle();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                toggle();
                return true;
        }

        return super.onKeyUp(keycode, e);
    }
    private void setStoredFragment(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = new SecurePreferences(this);
        String fragmentClassName = sharedPreferences.getString(
                Dispatcher.LAST_FRAGMENT_PROPNAME,  //todo добавить
                ""
        );
        if (!StringUtil.isTrimmedEmpty(fragmentClassName)) {
            Bundle bundle = savedInstanceState == null ? new Bundle() : new Bundle(savedInstanceState);
            try {
                Class<? extends BaseFragment> fragmentClass = (Class<? extends BaseFragment>) Class.forName(fragmentClassName);
                BundleUtil.loadBundle(sharedPreferences, bundle);
                mContent = BaseFragment.newInstance(fragmentClass, bundle);
            } catch (Exception e) {
                Log.e(getClass().getName()
                        , "Error while loading stored fragment"
                        , e
                );
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorized_menu);
        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSaveEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSaveEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
        //set the Above View Fragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_PROP_NAME);
        }

        if (mContent == null) {
            setStoredFragment(getIntent().getExtras());
        }

        if (mContent == null) {
            mContent = navigationService.getMainFragment(new Bundle());
        }

        //set the Behind View Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment())
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mContent)
                .addToBackStack(null)
                .commit();

        //customize the Sliding Menu
        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.RIGHT);
        sm.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        //sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
//        sm.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
//            @Override
//            public void onOpened() {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                MenuFragment menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menu_frame);
//                menuFragment.checkProfilesWheelState();
//            }
//        });

        sm.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                handleMenuClosed();
            }
        });


        /*обработку нижней панели вынесли сюда, чтобы в каждом фрагменте с ней не мучаться*/
//        findViewById(R.id.messages).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View count = view.findViewById(R.id.messages_count);
//                count.setVisibility(count.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//            }
//        });
//
//        findViewById(R.id.notifications).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View count = view.findViewById(R.id.notifications_count);
//                count.setVisibility(count.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//            }
//        });
    }

    private void handleMenuClosed() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        MenuFragment menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menu_frame);
    }

    private boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {
        int options=getSupportActionBar().getDisplayOptions();
        options&= ActionBar.DISPLAY_HOME_AS_UP;
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.content_frame);
        //if displayAsUp && moveBack implemented
        if(options!=0 && fragment instanceof BaseResultViewFragment){
            ((BaseResultViewFragment) fragment).moveBack();
        }
        else{
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.back_toast), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;

                }
            }, 2000);
        }
    }
}