package org.cmas.util.android.ui;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.cmas.cmas_flutter.R;
import org.cmas.android.MainApplication;
import org.cmas.util.LabelValue;

import java.util.List;

public final class ViewUtils {
    private ViewUtils() {
    }

    public static <T> void populateAdapter(ArrayAdapter<LabelValue<T>> adapter, List<LabelValue<T>> elements) {
        adapter.clear();
        adapter.addAll(elements);
        adapter.notifyDataSetChanged();
    }

    public static <T> ArrayAdapter<LabelValue<T>> setupAdapter(
            Spinner spinner,
            int itemLayoutResId,
            List<LabelValue<T>> elements,
            int emptyElemStringResId
    ) {
        MainApplication appContext = MainApplication.getAppContext();
        ArrayAdapter<LabelValue<T>> dataAdapter = new ArrayAdapter<>(
                appContext,
                itemLayoutResId,
                elements
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                dataAdapter,
                R.layout.spinner_row_nothing_selected,
                appContext,
                appContext.getString(emptyElemStringResId))
        );
        return dataAdapter;
    }
}
