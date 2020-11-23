package org.cmas.android;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.cmas.cmas_flutter.R;
import com.cmas.cmas_flutter.databinding.MainActivityBinding;
import org.cmas.android.ui.LoadingFragment;

import javax.annotation.Nullable;
import java.util.Set;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
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
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.container, LoadingFragment.newInstance(deepLinkType, data))
                                       .commitNow();
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
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    @Override
    protected void onDestroy() {
        SystemInitializer.getInstance().finalise();
        super.onDestroy();
    }
}
