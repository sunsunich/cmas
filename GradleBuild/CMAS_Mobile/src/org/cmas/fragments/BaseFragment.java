package org.cmas.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import org.cmas.BaseBeanContainer;
import org.cmas.activities.AuthorizedHolder;
import org.cmas.activities.Dispatcher;
import org.cmas.mobile.R;
import org.cmas.service.NavigationService;
import org.cmas.util.android.BundleUtil;
import org.cmas.util.android.SecurePreferences;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * User: ABadretdinov
 * Date: 20.12.13
 * Time: 11:48
 */
public abstract class BaseFragment extends Fragment {

    protected final BaseBeanContainer beanContainer = BaseBeanContainer.getInstance();
    protected final NavigationService navigationService = beanContainer.getNavigationService();

    protected boolean isStoreState;

    protected BaseFragment(boolean storeState) {
        isStoreState = storeState;
    }

    public static <T extends BaseFragment> T newInstance(Class<T> fragmentClass, Bundle data)
            throws IllegalAccessException, InstantiationException, java.lang.InstantiationException {
        T fragment = fragmentClass.newInstance();
        fragment.setArguments(data);
        return fragment;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        TextView title= (TextView) getActivity().getLayoutInflater().inflate(R.layout.title_layout,null,false);
        title.setText(makeHtmlTitle(getString(R.string.app_name)));
        actionBar.setCustomView(title);
        actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setTitle();
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isStoreState) {
            SharedPreferences sharedPreferences = new SecurePreferences(getActivity());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Dispatcher.LAST_ACTIVITY_PROPNAME, AuthorizedHolder.class.getName());
            //must be getClass!!! we want the exact class. not the base class
            editor.putString(Dispatcher.LAST_FRAGMENT_PROPNAME, getClass().getName());

            Bundle extras = getArguments();
            try {
                BundleUtil.saveFlatBundle(editor, extras);
            } catch (Exception e) {
                Log.e(BaseFragment.class.getName()
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
        reportSaveSuccess(Dispatcher.class, Collections.<String, Serializable>emptyMap());
    }

    public void reportSaveSuccess(
            final Class<? extends Activity> activityClass,
            final Map<String, Serializable> extraData
    ) {
        final Dialog dialog = new Dialog(getActivity(),R.style.CMAS_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.findViewById(R.id.title).setVisibility(View.GONE);
        dialog.findViewById(R.id.title_line).setVisibility(View.GONE);
        ((TextView)dialog.findViewById(R.id.message)).setText(R.string.saved_successfully);
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activityClass);
                for (Map.Entry<String, Serializable> entry : extraData.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void reportSaveSuccess(
            final Class<? extends BaseFragment> fragmentClass,
            final Bundle bundle
    ) {
        reportSaveSuccess(fragmentClass, bundle, getString(R.string.saved_successfully));
    }

    public void reportSaveSuccess(
            final Class<? extends BaseFragment> fragmentClass,
            final Bundle bundle,
            String successMessage
    ) {
        reportSaveSuccess(fragmentClass, false, bundle, successMessage);
    }

    public void reportSaveSuccess(
            final Class<? extends BaseFragment> fragmentClass,
            final boolean allowPopBackStack,
            final Bundle bundle,
            String successMessage
    ) {
        final Dialog dialog = new Dialog(getActivity(),R.style.CMAS_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.findViewById(R.id.title).setVisibility(View.GONE);
        dialog.findViewById(R.id.title_line).setVisibility(View.GONE);
        ((TextView)dialog.findViewById(R.id.message)).setText(successMessage);
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(allowPopBackStack&&getFragmentManager().getBackStackEntryCount()>1)
                    {
                        getFragmentManager().popBackStack();
                    } else {
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        replaceCurrentMainFragment(getId(), newInstance(fragmentClass, bundle),true);
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e(BaseFragment.class.getName()
                            , "Error while opening fragment: " + fragmentClass.getName()
                            , e
                    );
                    Intent intent = new Intent(getActivity(), Dispatcher.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void reportError(String message) {
        reportError(getActivity(), message);
    }

    public static void reportError(Context context, CharSequence message) {
        final Dialog dialog = new Dialog(context,R.style.CMAS_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        ((TextView)dialog.findViewById(R.id.title)).setText(R.string.error);
        ((TextView)dialog.findViewById(R.id.message)).setText(message);
        dialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public int replaceCurrentMainFragment(int fragmentToReplaceId, Fragment fragment, boolean addToStack) {
        return replaceCurrentMainFragment(fragmentToReplaceId, fragment, addToStack, true);
    }

    public int replaceCurrentMainFragment(int fragmentToReplaceId, Fragment fragment, boolean addToStack, boolean forward) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (forward) {
            transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        } else {
            transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right, R.anim.slide_from_right, R.anim.slide_to_left);
        }
        transaction.replace(fragmentToReplaceId, fragment);
        if (addToStack) {
            //чтобы по нажатию кнопки назад, мы возвращались сюда, а не закрывали активити.
            transaction.addToBackStack(null);
        }
        return transaction.commit();
    }
}
