package org.cmas.fragments.logbook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.EntityManagementAction;
import org.cmas.json.logbook.LogbookEntryCreateReply;
import org.cmas.mobile.R;
import org.cmas.util.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 12.02.14
 * Time: 16:21
 */
public class NewLogbookEntry extends NewEditBaseLogbookEntryFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logbookEntry = new LogbookEntry();
        return inflater.inflate(R.layout.logbook_entry_new, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupHeader(getString(R.string.logbook), LogbookFragment.class);

        final FragmentActivity activity = getActivity();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogbookEntrySave();
            }
        });
    }

    @Override
    protected boolean collectLogbookEntryData() {
        if (super.collectLogbookEntryData()) {
            FragmentActivity activity = getActivity();

            String entryName = entryNameView.getText().toString();
            if (StringUtil.isTrimmedEmpty(entryName)) {
                BaseFragment.reportError(activity, activity.getString(R.string.error_entry_name_empty));
                return false;
            }
            logbookEntry.setName(entryName);



            return true;
        } else {
            return false;
        }
    }

    private void doLogbookEntrySave() {
        if (collectLogbookEntryData()) {
            EntityManagementAction<LogbookEntryCreateReply> action = new EntityManagementAction<LogbookEntryCreateReply>() {
                @Override
                protected Pair<LogbookEntryCreateReply, String> manageEntity() {
                    return logbookService.addNewEntry(
                            getActivity(), logbookEntry
                    );
                }
            };
            action.doAction(this, "add new logbookEntry", LogbookFragment.class);
        }
    }
}
