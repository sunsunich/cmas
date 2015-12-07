package org.cmas.fragments.documents;

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
import org.cmas.R;
import org.cmas.entities.doc.Document;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.service.doc.DocumentService;
import org.cmas.util.DialogUtils;
import org.cmas.util.ProgressTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 21.01.14
 * Time: 11:32
 */
public class DocumentsFragment extends BaseResultViewFragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private DocumentService documentService;

    public static DocumentsFragment newInstance(Bundle data) {
        DocumentsFragment fragment = new DocumentsFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public DocumentsFragment() {
        super(true);

        BaseBeanContainer baseBeanContainer = BaseBeanContainer.getInstance();
        documentService = baseBeanContainer.getDocumentService();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_and_add, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.documents_storage));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.add) {
            try {
                replaceCurrentMainFragment(getId(), BaseFragment.newInstance(NewDocument.class, null), true);
            } catch (Exception e) {
                Log.e(getClass().getName()
                        , "Error while opening NewDocument fragment"
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
        setupHeader(getString(R.string.documents_storage), navigationService.getMainFragmentClass());

        listView = (ListView) getView().findViewById(R.id.documents_holder);
        listView.setAdapter(new DocumentListAdapter(getActivity(), null));
        listView.setOnItemClickListener(this);

        final ActionBarActivity activity = (ActionBarActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.documents_icon);
        actionBar.setDisplayShowHomeEnabled(true);

        loadData();
    }

    private void loadData() {
        final ActionBarActivity activity = (ActionBarActivity) getActivity();
        DialogUtils.showLoaderDialog(getFragmentManager(), new ProgressTask<List<Document>>() {
            @Override
            public List<Document> doTask(OnPublishProgressListener listener) {
                if (getActivity() == null) {
                    return new ArrayList<Document>();
                }
                if (listener != null) {
                    listener.onPublishProgress(activity.getString(R.string.loading_documents));
                }
                documentService.loadUserDocs(activity);
                return documentService.getByProfileNoRemoteCall(activity, currentUser.getId(), null);
            }

            @Override
            public void doAfterTask(List<Document> documents) {
                listView.setAdapter(new DocumentListAdapter(activity, documents));
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

    private void getRequested(String documentName) {
        Activity activity = getActivity();
        List<Document> documents = documentService.getByProfileNoRemoteCall(
                activity, currentUser.getId(), documentName
        );
        listView.setAdapter(new DocumentListAdapter(activity, documents));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DocumentListAdapter adapter = (DocumentListAdapter) parent.getAdapter();

        Bundle args = new Bundle();
        args.putSerializable("document", adapter.getItem(position));
        try {
            replaceCurrentMainFragment(getId(), BaseFragment.newInstance(ViewDocument.class, args), true);
        } catch (Exception e) {
            Log.e(getClass().getName()
                    , "Error while opening EditProfile fragment"
                    , e
            );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.documents, null, false);
    }

    @Override
    public boolean onQueryTextSubmit(String text) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String text) {
        getRequested(text);
        return true;
    }
}