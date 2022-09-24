package org.cmas.android.ui.verify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import org.cmas.ecards.R;
import org.cmas.ecards.databinding.DiverVerificationFragmentBinding;
import org.cmas.android.MainActivity;
import org.cmas.util.android.TaskViewModel;

public class DiverVerificationFragment extends Fragment {

    private DiverVerificationFragmentBinding binding;
    private DiverVerificationViewModel diverVerificationViewModel;

    public static DiverVerificationFragment newInstance() {
        return new DiverVerificationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        diverVerificationViewModel = TaskViewModel.safelyInitTask(this, DiverVerificationViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.diver_verification_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();

        diverVerificationViewModel.getResult().observe(viewLifecycleOwner, result -> {
            if (result) {

            } else {

            }
        });

        binding.bntVerify.setOnClickListener(view1 -> MainActivity.replaceFragment(getActivity(),
                                                                                   DiverVerificationFragment
                                                                                           .newInstance()));
    }

    @Override
    public void onResume() {
        super.onResume();
        diverVerificationViewModel.start(null);
    }

}
