package org.cmas.fragments.documents;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.cmas.R;
import org.cmas.entities.doc.Document;
import org.cmas.fragments.EntityManagementAction;
import org.cmas.json.EntityEditReply;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 21.01.14
 * Time: 17:42
 */
public class EditDocument extends NewEditBaseDocumentFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extrasMap = new Bundle();
        extrasMap.putSerializable("document", document);
        setupHeader(getString(R.string.logbook), ViewDocument.class, extrasMap);

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
                doDocumentEdit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() == null) {
            return inflater.inflate(R.layout.fatal_error, null, false);
        } else {
            document = (Document) getArguments().getSerializable("document");
            if (document == null) {
                return inflater.inflate(R.layout.fatal_error, null, false);
            } else {
                return inflater.inflate(R.layout.document_edit, null, false);
            }
        }
    }

    private void doDocumentEdit() {
        if (collectDocumentData()) {
            EntityManagementAction<EntityEditReply> action = new EntityManagementAction<EntityEditReply>() {
                @Override
                protected Pair<EntityEditReply, String> manageEntity() {
                    return documentService.editDoc(
                            getActivity(), document
                    );
                }
            };
            action.doAction(this, "edit document", DocumentsFragment.class);
        }
    }

    @Override
    protected long getTypeId() {
        return document.getTypeId();
    }
}
