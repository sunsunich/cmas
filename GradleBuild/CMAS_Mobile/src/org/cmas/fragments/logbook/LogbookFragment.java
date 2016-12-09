package org.cmas.fragments.logbook;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cocosw.undobar.UndoBarController;
import org.cmas.BaseBeanContainer;
import org.cmas.mobile.R;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.service.logbook.LogbookService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 1ø
 * Date: 21.01.14
 * Time: 11:32
 */
public class LogbookFragment extends BaseResultViewFragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private final LogbookService logbookService;

    public static LogbookFragment newInstance(Bundle data) {
        LogbookFragment fragment = new LogbookFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public LogbookFragment() {
        super(true);

        BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();
        logbookService = baseBeanContainer.getLogbookService();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_and_add, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.logbook));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.add) {
            try {
                replaceCurrentMainFragment(getId(), BaseFragment.newInstance(NewLogbookEntry.class, null), true);
            } catch (Exception e) {
                Log.e(getClass().getName()
                        , "Error while opening NewLogbookEntry fragment"
                        , e
                );
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ListView listView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupHeader(getString(R.string.logbook), navigationService.getMainFragmentClass());

        listView = (ListView) getView().findViewById(R.id.entries_holder);
        listView.setAdapter(new LogbookEntryListAdapter(getActivity(), null));
        listView.setOnItemClickListener(this);

        ActionBarActivity activity = (ActionBarActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setIcon(R.drawable.logbook_icon);

        loadData();
    }

    private void loadData() {
        final ActionBarActivity activity = (ActionBarActivity) getActivity();
        DialogUtils.showLoaderDialog(getFragmentManager(), new ProgressTask<List<LogbookEntry>>() {
            @Override
            public List<LogbookEntry> doTask(ProgressTask.OnPublishProgressListener listener) {
                if (getActivity() == null) {
                    return new ArrayList<>();
                }
                if (listener != null) {
                    listener.onPublishProgress(activity.getString(R.string.loading_logbook));
                }
                logbookService.loadLogbook(activity, currentUser.getId());
                return logbookService.getByDiverNoRemoteCall(activity, currentUser.getId(), null);
            }

            @Override
            public void doAfterTask(List<LogbookEntry> result) {
                listView.setAdapter(new LogbookEntryListAdapter(activity, result));
            }

            @Override
            public void handleError(String error) {
                undoBar = UndoBarController.show(activity, error, new UndoBarController.UndoListener() {
                    @Override
                    public void onUndo(Parcelable token) {
                        loadData();
                    }
                });
                //reportError(error);
            }

            @Override
            public String getName() {
                return "profileService.getByUser";
            }
        });
    }

    private UndoBarController undoBar;

    @Override
    public void onDestroyView() {
        if (undoBar != null) {
            undoBar.hide();
        }
        super.onDestroyView();
    }

    private void getRequested(String entryName) {
        Activity activity = getActivity();
        List<LogbookEntry> logbookEntries = logbookService.getByDiverNoRemoteCall(
                activity, currentUser.getId(), entryName
        );
        listView.setAdapter(new LogbookEntryListAdapter(activity, logbookEntries));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogbookEntryListAdapter adapter = (LogbookEntryListAdapter) parent.getAdapter();

        Bundle args = new Bundle();
        args.putSerializable("logbookEntry", adapter.getItem(position));
        try {
            replaceCurrentMainFragment(getId(), BaseFragment.newInstance(ViewLogbookEntry.class, args), true);
        } catch (Exception e) {
            Log.e(getClass().getName()
                    , "Error while opening UserAccount fragment"
                    , e
            );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logbook, null, false);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        getRequested(s);
        return true;
    }
}