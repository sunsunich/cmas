package org.cmas.android;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cmas.cmas_flutter.R;
import com.cmas.cmas_flutter.databinding.MainActivityBinding;
import org.cmas.android.ui.LoadingFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.container, LoadingFragment.newInstance())
                                       .commitNow();
        }
    }

    @Override
    protected void onDestroy() {
        SystemInitializer.getInstance().finalise();
        super.onDestroy();
    }
}
