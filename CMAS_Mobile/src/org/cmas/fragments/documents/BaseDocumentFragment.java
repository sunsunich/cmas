package org.cmas.fragments.documents;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.cmas.R;
import org.cmas.dao.doc.DocFileDao;
import org.cmas.entities.doc.Document;
import org.cmas.entities.doc.DocumentType;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.fragments.EntityManagementAction;
import org.cmas.json.EntityEditReply;
import org.cmas.service.doc.DocumentService;
import org.cmas.service.doc.DocumentTypeService;
import org.cmas.util.DialogUtils;
import org.cmas.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 11.02.14
 * Time: 18:18
 */
public abstract class BaseDocumentFragment extends BaseResultViewFragment {

    protected static final long ANALYSIS_DOC_TYPE_ID = 2L;
    protected static final long HOSPITALIZATION_DOC_TYPE_ID = 6L;

    protected final DocumentService documentService = beanContainer.getDocumentService();
    protected final DocFileDao docFileDao = beanContainer.getDocFileDao();
    protected final DocumentTypeService documentTypeService = beanContainer.getDocumentTypeService();

    protected Document document;

    protected TextView docNameView;
    protected TextView docDescView;
    protected TextView docDateView;

    protected LinearLayout hospitalizationHolder;
    protected TextView hospitalizationFromView;
    protected TextView hospitalizationTillView;

    protected Button addedFilesButton;
    protected LinearLayout attachedFilesHolder;
    protected Map<String, String> attachedFileFullPaths;

    protected BaseDocumentFragment() {
        super(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();
        ActionBar actionBar = ((ActionBarActivity) activity).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.account_info_icon);
        actionBar.setDisplayShowHomeEnabled(true);

        View view = getView();
        docNameView = (TextView) view.findViewById(R.id.doc_name);
        setStringValue(docNameView, document.getName());

        final Button commonInfoButton = (Button) view.findViewById(R.id.common_info_btn);
        if (commonInfoButton != null) {
            final LinearLayout commonInfoRootLayout = (LinearLayout) view.findViewById(R.id.common_info_root);
            commonInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commonInfoRootLayout.getVisibility() == View.GONE) {
                        commonInfoRootLayout.setVisibility(View.VISIBLE);
                        commonInfoButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_white, 0);
                    } else {
                        commonInfoRootLayout.setVisibility(View.GONE);
                        commonInfoButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_white, 0);
                    }
                }
            });
        }

        docDescView = (TextView) view.findViewById(R.id.doc_desc);
        setStringValue(docDescView, document.getDescription());

        final Button docDescButton = (Button) view.findViewById(R.id.doc_desc_btn);
        if (docDescButton != null) {
            docDescButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (docDescView.getVisibility() == View.GONE) {
                        docDescView.setVisibility(View.VISIBLE);
                        docDescButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_white, 0);
                    } else {
                        docDescView.setVisibility(View.GONE);
                        docDescButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_white, 0);
                    }
                }
            });
        }


        long docTypeId = document.getTypeId();
        TextView docTypeView = (TextView) view.findViewById(R.id.doc_type);
        if (docTypeView != null) {
            DocumentType documentType = documentTypeService.getById(activity, docTypeId);
            docTypeView.setText(documentType.getLocalName());
        }

        attachedFileFullPaths = new HashMap<String, String>();
        addedFilesButton = (Button) view.findViewById(R.id.doc_added_files_btn);
        attachedFilesHolder = (LinearLayout) view.findViewById(R.id.doc_added_files_holder);
        if (document.getId() != 0L) {
            List<File> files = docFileDao.getFiles(activity, document);
            for (File file : files) {
                attachFile(file.getAbsolutePath());
            }
        }

        final Button docDeleteButton = (Button) view.findViewById(R.id.doc_delete);
        if (docDeleteButton != null) {
            docDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doDocumentDelete();
                }
            });
        }
    }

    protected void addCustomField(LinearLayout customFieldsRootLayout, Map.Entry<String, String> customFields) {

        final FragmentActivity activity = getActivity();


        final LinearLayout row = (LinearLayout) activity.getLayoutInflater()
                .inflate(R.layout.document_custom_field_row, null);


        String key = customFields.getKey();
        if (!StringUtil.isTrimmedEmpty(key)) {
            TextView customFieldNameTextView = (TextView) row.findViewById(R.id.doc_custom_field_name);
            customFieldNameTextView.setText(key);

            String value = customFields.getValue();
            if (value != null) {
                TextView customFieldValueTextView = (TextView) row.findViewById(R.id.doc_custom_field_value);
                customFieldValueTextView.setText(value);
            }
            customFieldsRootLayout.addView(row);
        }
    }

    protected void attachFile(final String filePath) {
        final FragmentActivity activity = getActivity();

        boolean isEditable = isEditable();
        final String fileName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1);
        if (isEditable) {
            attachedFileFullPaths.put(fileName, filePath);
        }
        final LinearLayout row = (LinearLayout) activity.getLayoutInflater()
                .inflate(R.layout.document_file_row, null);
        TextView fileNameTextView = (TextView) row.findViewById(R.id.file_name);
        fileNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File file = new File(filePath);

                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                String type = mime.getMimeTypeFromExtension(ext);
                if (type != null) {
                    intent.setDataAndType(Uri.fromFile(file), type);
                }
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    BaseFragment.reportError(activity, activity.getString(R.string.error_no_app_to_open_file));
                }
            }
        });

        fileNameTextView.setText(fileName);
        ImageView deleteImage = (ImageView) row.findViewById(R.id.delete_file);
        if (isEditable) {
            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attachedFileFullPaths.remove(fileName);
                    attachedFilesHolder.removeView(row);
//                    attachedFilesHolder.refreshDrawableState();
                }
            });
        } else {
            deleteImage.setVisibility(View.GONE);
        }
        attachedFilesHolder.addView(row);
    }

    private void setStringValue(TextView textView, String value) {
        if (StringUtil.isTrimmedEmpty(value)) {
            textView.setText("");
        } else {
            textView.setText(value);
        }
    }

    private void doDocumentDelete() {
        final FragmentActivity activity = getActivity();
        DialogUtils.showYesNoDialog(
                activity, activity.getString(R.string.doc_delete_question), activity.getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EntityManagementAction<EntityEditReply> action = new EntityManagementAction<EntityEditReply>() {
                            @Override
                            protected Pair<EntityEditReply, String> manageEntity() {
                                return documentService.deleteDoc(
                                        activity, document.getId()
                                );
                            }
                        };
                        action.doAction(
                                BaseDocumentFragment.this,
                                "delete document",
                                activity.getString(R.string.data_deleted_successfully),
                                DocumentsFragment.class);
                    }
                }
        );

    }

    protected abstract boolean isEditable();
}
