package org.cmas.android;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import org.cmas.android.ui.LoadingFragment;
import org.cmas.android.ui.signin.PostToServerService;
import org.cmas.android.ui.signin.RegistrationFormObject;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.MainActivityBinding;

import javax.annotation.Nullable;
import java.util.Set;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            @Nullable
            String action = intent.getAction();
            @Nullable
            Uri data = intent.getData();
            DeepLinkType deepLinkType = DeepLinkType.NONE;
            if (ACTION_VIEW.equals(action) && data != null) {
                Set<String> queryParameterNames = data.getQueryParameterNames();
                if (queryParameterNames.contains("verify")) {
                    deepLinkType = DeepLinkType.VERIFY;
                } else if (queryParameterNames.contains("login")) {
                    deepLinkType = DeepLinkType.LOGIN;
                }
            }
            replaceFragment(this, LoadingFragment.newInstance(
                    deepLinkType,
                    data,
                    (RegistrationFormObject) intent.getSerializableExtra(
                            PostToServerService.REGISTRATION_FORM_OBJECT_KEY)
                            )
            );
        }
    }

    public static void reportError(FragmentActivity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.error);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, (dialog, arg1) -> dialog.dismiss());
        builder.setCancelable(false);
        AlertDialog myAlertDialog = builder.create();
        myAlertDialog.show();
    }

    public static void replaceFragment(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    public static void gotoFragment(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }
}
