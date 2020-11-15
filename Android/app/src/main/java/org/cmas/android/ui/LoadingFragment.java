package org.cmas.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.cmas.cmas_flutter.R;
import org.cmas.util.android.ProgressUpdater;

public class LoadingFragment extends Fragment {

    private ProgressUpdater progressUpdater;
    private LoadingViewModel mViewModel;

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(LoadingViewModel.class);

        return inflater.inflate(R.layout.loading_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        mViewModel.init();
        mViewModel.getProgress().observe(viewLifecycleOwner,
                                         taskProgressUpdate -> progressUpdater.reportProgress(taskProgressUpdate));
        mViewModel.getResult().observe(viewLifecycleOwner, result -> {
            if (result) {

            } else {

            }
        });
        mViewModel.start();
    }

}
