package org.cmas.fragments.logbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import org.cmas.mobile.R;
import org.cmas.activities.file.FileChooseActivity;
import org.cmas.activities.file.Params;
import org.cmas.dao.doc.DocFileDaoFileImpl;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: 1
 * Date: 11.02.14
 * Time: 18:18
 */
public abstract class NewEditBaseLogbookEntryFragment extends BaseLogbookEntryFragment {

    protected static final int PICK_FILE_ACTION = 115;
    protected static final int MAKE_PHOTO_ACTION = 116;

    protected ImageButton setDateButton;

    protected Button attachFileButton;

    protected Button saveButton;
    protected Button cancelButton;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        setDateButton = (ImageButton) view.findViewById(R.id.entry_set_date_btn);

        attachFileButton = (Button) view.findViewById(R.id.entry_add_file_btn);
        attachFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAttachFileDialog();
            }
        });

        saveButton = (Button) view.findViewById(R.id.save_btn);

        cancelButton = (Button) view.findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack();
            }
        });
    }

    private String imageCaptureFullPath;



    protected void createAttachFileDialog() {
        imageCaptureFullPath = null;
        final FragmentActivity context = getActivity();
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_file_dialog, null);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialogLayout.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureFullPath = DocFileDaoFileImpl.getFileStoreLocation(context)
                        + File.separator
                        + System.currentTimeMillis()
                        + ".jpg"
                ;
                File imageCaptureFile = new File(imageCaptureFullPath);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageCaptureFile));
                startActivityForResult(cameraIntent, MAKE_PHOTO_ACTION);
                dialog.dismiss();
            }
        });
        dialogLayout.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, FileChooseActivity.class);
                intent.putExtra(Params.MODE, FileChooseActivity.Mode.CHOOSE_FILE);
                startActivityForResult(intent, PICK_FILE_ACTION);
                dialog.dismiss();
            }
        });
        dialogLayout.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final FragmentActivity activity = getActivity();
        String filePath = null;
        switch (requestCode) {
            case MAKE_PHOTO_ACTION:
                if (resultCode == Activity.RESULT_OK) {
                    filePath = imageCaptureFullPath;
                    imageCaptureFullPath = null;
                }
                break;
            case PICK_FILE_ACTION:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    filePath = extras.getString(FileChooseActivity.RESULT_NAME);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
        if (filePath != null) {
            attachFile(filePath);
        }
    }

    protected boolean collectLogbookEntryData() {
        FragmentActivity activity = getActivity();

        logbookEntry.setNote(
                noteView.getText().toString()
        );

//        Set<Map.Entry<String, String>> entries = attachedFileFullPaths.entrySet();
//        List<DocFile> files = logbookEntry.getFiles();
//        if (files == null) {
//            files = new ArrayList<DocFile>(entries.size());
//            logbookEntry.setFiles(files);
//        } else {
//            files.clear();
//        }

//        for (Map.Entry<String, String> entry : entries) {
//            String fileName = entry.getKey();
//            String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
//            try {
//                FileInputStream inputStream = new FileInputStream(entry.getValue());
//                try {
//                    byte[] fileData = IOUtils.toByteArray(inputStream);
//                    //todo implement
////                    DocFile docFile = new DocFile();
////                    docFile.setFile(
////                            Base64Coder.encodeString(fileData)
////                    );
////                    docFile.setExt(ext);
////                    files.add(docFile);
//                } finally {
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                Log.e(getClass().getName()
//                        , "Error while opening doc files"
//                        , e
//                );
//                files.clear();
//                BaseFragment.reportError(
//                        activity,
//                        MessageFormat.format(
//                                activity.getString(R.string.error_while_opening_doc_file_format),
//                                fileName
//                        )
//                );
//                return false;
//            }
//        }

        return true;
    }

    @Override
    protected boolean isEditable() {
        return true;
    }
}
