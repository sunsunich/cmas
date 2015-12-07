package org.cmas.fragments.documents;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.cmas.R;
import org.cmas.entities.doc.Document;
import org.cmas.entities.doc.DocumentType;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.EntityManagementAction;
import org.cmas.fragments.NothingSelectedSpinnerAdapter;
import org.cmas.json.doc.DocCreateReply;
import org.cmas.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 12.02.14
 * Time: 16:21
 */
public class NewDocument extends NewEditBaseDocumentFragment {

    private Map<String, DocumentType> documentTypeMap;

    private Spinner docTypeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        document = new Document();
        return inflater.inflate(R.layout.document_new, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupHeader(getString(R.string.documents_storage), DocumentsFragment.class);

        final FragmentActivity activity = getActivity();

        List<DocumentType> documentTypes = documentTypeService.getAllNoDeleted(activity);
        documentTypeMap = new HashMap<String, DocumentType>(documentTypes.size());
        for (DocumentType documentType : documentTypes) {
            documentTypeMap.put(documentType.getLocalName(), documentType);
        }
        docTypeSpinner = (Spinner) activity.findViewById(R.id.new_doc_type);
        {
            List<String> docTypeNames = new ArrayList<String>(documentTypeMap.keySet());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                    activity,
                    android.R.layout.simple_spinner_item,
                    docTypeNames
            );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            docTypeSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                    dataAdapter,
                    R.layout.spinner_row_nothing_selected,
                    activity,
                    getString(R.string.type_of_document)));
        }
        docTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object itemAtPosition = adapterView.getItemAtPosition(i);
                String docTypeLocalName = itemAtPosition == null ? "" : itemAtPosition.toString();
                DocumentType documentType = documentTypeMap.get(docTypeLocalName);
                long id = documentType == null ? 0L : documentType.getId();
                if (ANALYSIS_DOC_TYPE_ID == id) {
                    if (hospitalizationHolder.getVisibility() == View.VISIBLE) {
                        hospitalizationHolder.setVisibility(View.GONE);
                    }
                } else if (HOSPITALIZATION_DOC_TYPE_ID == id) {
                    if (hospitalizationHolder.getVisibility() == View.GONE) {
                        hospitalizationHolder.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (hospitalizationHolder.getVisibility() == View.VISIBLE) {
                        hospitalizationHolder.setVisibility(View.GONE);
                    }
                }

                TextView selectedText = (TextView) adapterView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(
                            getResources().getColor(R.color.black)
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDocumentSave();
            }
        });
    }

    @Override
    protected long getTypeId() {
        Object selectedItem = docTypeSpinner.getSelectedItem();
        String docTypeLocalName = selectedItem == null ? "" : selectedItem.toString();
        DocumentType documentType = documentTypeMap.get(docTypeLocalName);
        return documentType == null ? 0L : documentType.getId();
    }

    @Override
    protected boolean collectDocumentData() {
        if (super.collectDocumentData()) {
            FragmentActivity activity = getActivity();

            String docName = docNameView.getText().toString();
            if (StringUtil.isTrimmedEmpty(docName)) {
                BaseFragment.reportError(activity, activity.getString(R.string.error_doc_name_empty));
                return false;
            }
            document.setName(docName);



            return true;
        } else {
            return false;
        }
    }

    private void doDocumentSave() {
        if (collectDocumentData()) {
            EntityManagementAction<DocCreateReply> action = new EntityManagementAction<DocCreateReply>() {
                @Override
                protected Pair<DocCreateReply, String> manageEntity() {
                    return documentService.addNewDoc(
                            getActivity(), document
                    );
                }
            };
            action.doAction(this, "add new document", DocumentsFragment.class);
        }
    }
}
