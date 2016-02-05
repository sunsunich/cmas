package org.cmas.activities.social;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cocosw.undobar.UndoBarController;
import org.cmas.BaseBeanContainer;
import org.cmas.R;
import org.cmas.activities.SecureActivity;
import org.cmas.entities.HasId;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.service.social.DiverSearchService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ObjectUtils;
import org.cmas.util.ProgressTask;

import java.util.List;

/**
 * Created on Jan 10, 2016
 *
 * @author Alexander Petukhov
 */
public class SearchDivers extends SecureActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    public static short SEARCH_USER_REQUEST_CODE = ObjectUtils.nextShort();

    private final DiverSearchService diverSearchService;

    public SearchDivers() {
        super(false);

        BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();
        diverSearchService = baseBeanContainer.getDiverSearchService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getHeader());
        searchView.setOnQueryTextListener(this);
        return true;
    }


    private ListView listView;

    private DiverType diverType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_search);
        diverType = (DiverType) getIntent().getSerializableExtra("diverType");
        String header = getHeader();
        setupHeader(header, null);

        listView = (ListView)findViewById(R.id.entries_holder);
        listView.setAdapter(new UserSearchAdapter<>(null, this));
        listView.setOnItemClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.find_icon);

        loadData();
    }

    private String getHeader() {
        String header = "";
        switch (diverType) {
            case DIVER:
                header = getString(R.string.search_buddies);
                break;
            case INSTRUCTOR:
                header = getString(R.string.search_instructor);
                break;
        }
        return header;
    }

    private void loadData() {
        final SecureActivity activity = this;
        DialogUtils.showLoaderDialog(activity.getSupportFragmentManager(), new ProgressTask<List<Diver>>() {
            @Override
            public List<Diver> doTask(ProgressTask.OnPublishProgressListener listener) {
                if (listener != null) {
                    listener.onPublishProgress(activity.getString(R.string.loading_users));
                }
                //     diverSearchService.loadLogbook(activity, currentUser.getId());
                return diverSearchService.searchDivers(activity, "", diverType);
            }

            @Override
            public void doAfterTask(List<Diver> result) {
                listView.setAdapter(new UserSearchAdapter<Diver>(result, activity));
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
    public void onStop() {
        if (undoBar != null) {
            undoBar.hide();
        }
        super.onStop();
    }

    private void getRequested(String entryName) {
        List<Diver> divers = diverSearchService.searchDivers(
                this, entryName, diverType
        );
        listView.setAdapter(new UserSearchAdapter<>(divers, this));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserSearchAdapter<Diver> adapter = (UserSearchAdapter<Diver>) parent.getAdapter();

        setResult(RESULT_OK, new Intent().putExtra("diverId", ((HasId) adapter.getItem(position)).getId()));
        finish();
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
