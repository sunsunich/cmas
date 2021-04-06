package org.cmas.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.cmas.cmas_flutter.R;
import com.cmas.cmas_flutter.databinding.FileAdditionFragmentBinding;
import org.cmas.android.ui.signin.RegistrationFormObject;
import org.cmas.android.ui.signin.RegistrationViewModel;

public class FileAdditionFragment extends Fragment {

    private FileAdditionFragmentBinding binding;

    private RegistrationViewModel registrationViewModel;

    private final RegistrationFormObject form;

    public FileAdditionFragment(RegistrationFormObject form) {
        this.form = form;
    }

    public static FileAdditionFragment newInstance(RegistrationFormObject form) {
        return new FileAdditionFragment(form);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.file_addition_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }


    private void collectForm() {
        // files

    }

    private boolean validateForm() {
        return false;
    }

}
