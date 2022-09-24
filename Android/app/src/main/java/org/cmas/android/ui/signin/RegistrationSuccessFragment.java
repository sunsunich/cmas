package org.cmas.android.ui.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.RegistrationSuccessFragmentBinding;

public class RegistrationSuccessFragment extends Fragment {

    private RegistrationSuccessFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_success_fragment, container, false);


        return binding.getRoot();
    }
}
