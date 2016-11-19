package org.cmas.fragments.logbook;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.fragments.EntityManagementAction;
import org.cmas.json.EntityEditReply;
import org.cmas.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 21.01.14
 * Time: 17:42
 */
public class EditLogbookEntry extends NewEditBaseLogbookEntryFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extrasMap = new Bundle();
        extrasMap.putSerializable("logbookEntry", logbookEntry);
        setupHeader(getString(R.string.logbook), ViewLogbookEntry.class, extrasMap);

        addedFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachedFilesHolder.getVisibility() == View.GONE && attachFileButton.getVisibility() == View.GONE) {
                    attachedFilesHolder.setVisibility(View.VISIBLE);
                    attachFileButton.setVisibility(View.VISIBLE);
                    addedFilesButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_white, 0);
                } else {
                    attachedFilesHolder.setVisibility(View.GONE);
                    attachFileButton.setVisibility(View.GONE);
                    addedFilesButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_white, 0);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doEntryEdit();
            }
        });
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
                return inflater.inflate(R.layout.logbook_entry_edit, null, false);
            }
        }
    }

    private void doEntryEdit() {
        if (collectLogbookEntryData()) {
            EntityManagementAction<EntityEditReply> action = new EntityManagementAction<EntityEditReply>() {
                @Override
                protected Pair<EntityEditReply, String> manageEntity() {
                    return logbookService.editEntry(
                            getActivity(), logbookEntry
                    );
                }
            };
            action.doAction(this, "edit logbookEntry", LogbookFragment.class);
        }
    }
}
