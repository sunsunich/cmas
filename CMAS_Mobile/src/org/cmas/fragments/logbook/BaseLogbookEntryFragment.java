package org.cmas.fragments.logbook;

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
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.fragments.BaseFragment;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.fragments.EntityManagementAction;
import org.cmas.json.EntityEditReply;
import org.cmas.service.logbook.LogbookService;
import org.cmas.util.DialogUtils;
import org.cmas.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 11.02.14
 * Time: 18:18
 */
public abstract class BaseLogbookEntryFragment extends BaseResultViewFragment {

    protected final LogbookService logbookService = beanContainer.getLogbookService();

    protected LogbookEntry logbookEntry;

    protected TextView entryNameView;
    protected TextView noteView;

    protected Button addedFilesButton;
    protected LinearLayout attachedFilesHolder;
    protected Map<String, String> attachedFileFullPaths;

    protected BaseLogbookEntryFragment() {
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
        entryNameView = (TextView) view.findViewById(R.id.entry_name);
        setStringValue(entryNameView, logbookEntry.getName());

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

        noteView = (TextView) view.findViewById(R.id.note);
        setStringValue(noteView, logbookEntry.getNote());

        final Button noteButton = (Button) view.findViewById(R.id.note_btn);
        if (noteButton != null) {
            noteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noteView.getVisibility() == View.GONE) {
                        noteView.setVisibility(View.VISIBLE);
                        noteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up_white, 0);
                    } else {
                        noteView.setVisibility(View.GONE);
                        noteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_white, 0);
                    }
                }
            });
        }

        attachedFileFullPaths = new HashMap<>();
        addedFilesButton = (Button) view.findViewById(R.id.doc_added_files_btn);
        attachedFilesHolder = (LinearLayout) view.findViewById(R.id.entry_added_files_holder);
//        if (logbookEntry.getId() != 0L) {
//            List<File> files = docFileDao.getFiles(activity, logbookEntry);
//            for (File file : files) {
//                attachFile(file.getAbsolutePath());
//            }
//        }

        Button deleteButton = (Button) view.findViewById(R.id.entry_delete);
        if (deleteButton != null) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doLogbookEntryDelete();
                }
            });
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

    private void doLogbookEntryDelete() {
        final FragmentActivity activity = getActivity();
        DialogUtils.showYesNoDialog(
                activity, activity.getString(R.string.entry_delete_question), activity.getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EntityManagementAction<EntityEditReply> action = new EntityManagementAction<EntityEditReply>() {
                            @Override
                            protected Pair<EntityEditReply, String> manageEntity() {
                                return logbookService.deleteEntry(
                                        activity, logbookEntry.getId()
                                );
                            }
                        };
                        action.doAction(
                                BaseLogbookEntryFragment.this,
                                "delete logbookEntry",
                                activity.getString(R.string.data_deleted_successfully),
                                LogbookFragment.class);
                    }
                }
        );

    }

    protected abstract boolean isEditable();
}
