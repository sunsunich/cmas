package org.cmas.android.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import org.cmas.android.DeepLinkType;
import org.cmas.android.MainActivity;
import org.cmas.android.ui.signin.RegistrationFormObject;
import org.cmas.android.ui.signin.RegistrationFragment;
import org.cmas.android.ui.verify.DiverVerificationFragment;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.LoadingFragmentBinding;
import org.cmas.util.android.TaskViewModel;
import org.cmas.util.android.ui.ProgressUpdater;

public class LoadingFragment extends Fragment {

    private LoadingFragmentBinding binding;
    private LoadingViewModel loadingViewModel;

    private ProgressUpdater progressUpdater;

    private DeepLinkType deepLinkType;
    private Uri data;
    @Nullable
    private RegistrationFormObject registrationFormObject;

    public static LoadingFragment newInstance(DeepLinkType deepLinkType,
                                              @Nullable Uri data,
                                              @Nullable RegistrationFormObject registrationFormObject) {
        LoadingFragment loadingFragment = new LoadingFragment();
        loadingFragment.deepLinkType = deepLinkType;
        loadingFragment.data = data;
        loadingFragment.registrationFormObject = registrationFormObject;
        return loadingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loadingViewModel = TaskViewModel.safelyInitTask(this, LoadingViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.loading_fragment, container, false);

        progressUpdater = new ProgressUpdater(binding.progressStatus, binding.progress);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        loadingViewModel.getProgress().observe(viewLifecycleOwner,
                                               taskProgressUpdate -> progressUpdater.reportProgress(taskProgressUpdate));
        loadingViewModel.getResult().observe(viewLifecycleOwner, result -> {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            if (result) {
                if(registrationFormObject == null) {
                    switch (deepLinkType) {
                        case NONE:
                            MainActivity.replaceFragment(activity, RegistrationFragment.newInstance(null));
                            break;
                        case VERIFY:
                            Log.d(getClass().getName(), "VERIFY: " + data);
                            MainActivity.replaceFragment(activity, DiverVerificationFragment.newInstance());
                            break;
                        case LOGIN:
                            Log.d(getClass().getName(), "LOGIN: " + data);
                            MainActivity.replaceFragment(activity, RegistrationFragment.newInstance(null));
                            break;
                    }
                } else {
                    MainActivity.replaceFragment(activity, RegistrationFragment.newInstance(registrationFormObject));
                }
            } else {
                MainActivity.reportError(activity, getString(R.string.fatal_error));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingViewModel.start(null);
    }
}
