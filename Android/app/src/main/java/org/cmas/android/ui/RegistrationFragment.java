package org.cmas.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.cmas.cmas_flutter.R;
import com.cmas.cmas_flutter.databinding.LoadingFragmentBinding;
import com.cmas.cmas_flutter.databinding.RegistrationFragmentBinding;
import org.cmas.android.MainActivity;
import org.cmas.util.android.ProgressUpdater;

public class RegistrationFragment extends Fragment {

    private RegistrationFragmentBinding binding;
    private RegistrationViewModel registrationViewModel;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        registrationViewModel.init();

        registrationViewModel.getResult().observe(viewLifecycleOwner, result -> {
            if (result) {

            } else {

            }
        });
      //  registrationViewModel.start();


        binding.bntVerify.setOnClickListener(view1 -> MainActivity.replaceFragment(getActivity(), RegistrationFragment.newInstance()));
    }

}
