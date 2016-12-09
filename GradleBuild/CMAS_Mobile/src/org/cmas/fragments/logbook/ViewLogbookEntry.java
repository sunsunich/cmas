package org.cmas.fragments.logbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 13.02.14
 * Time: 9:10
 */
public class ViewLogbookEntry extends BaseLogbookEntryFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupHeader(getString(R.string.logbook), LogbookFragment.class);

        addedFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachedFilesHolder.getVisibility() == View.GONE) {
                    attachedFilesHolder.setVisibility(View.VISIBLE);
                    addedFilesButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_white, 0);
                } else {
                    attachedFilesHolder.setVisibility(View.GONE);
                    addedFilesButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_white, 0);
                }
            }
        });

        Button editEntryButton = (Button) getView().findViewById(R.id.entry_edit_btn);
        editEntryButton.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() == null) {
            return inflater.inflate(R.layout.fatal_error, null, false);
        } else {
            logbookEntry = (LogbookEntry) getArguments().getSerializable("logbookEntry");
            if (logbookEntry == null) {
                return inflater.inflate(R.layout.fatal_error, null, false);
            } else {
                return inflater.inflate(R.layout.logbook_entry_view, null, false);
            }
        }
    }

    @Override
    protected boolean isEditable() {
        return false;
    }
}
