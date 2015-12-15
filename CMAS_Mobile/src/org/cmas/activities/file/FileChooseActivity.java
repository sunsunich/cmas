package org.cmas.activities.file;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.cmas.R;
import org.cmas.util.DialogUtils;

import java.io.File;

/**
 * User: ABadretdinov
 * Date: 05.08.13
 * Time: 12:09
 */
public class FileChooseActivity extends ActionBarActivity {
    public enum Mode {
        SAVE_FILE,
        CHOOSE_PATH,
        CHOOSE_FILE
    }

    public static final String RESULT_NAME = "file_name";
    private File file = null;
    private EditText fileName;
    private Button acceptBtn;
    private Mode mode = Mode.SAVE_FILE;
    private String type;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_choose);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setIcon(R.drawable.account_info_icon);
        listView= (ListView) findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                file = (File) listView.getAdapter().getItem(position);
                if (mode != Mode.CHOOSE_FILE || file.isDirectory()) {
                    changeFileList();
                } else {
                    if (getParent() == null) {
                        setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_NAME, file.getAbsolutePath()));
                    } else {
                        getParent().setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_NAME, file.getAbsolutePath()));
                    }
                    finish();
                }
            }
        });
        fileName = (EditText) findViewById(R.id.file_name);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(Params.DEFAULT_PATH)) {
                file = new File(extras.getString(Params.DEFAULT_PATH));
            }
            if (extras.containsKey(Params.DEFAULT_NAME)) {
                fileName.setText(extras.getString(Params.DEFAULT_NAME));
            }
            if (extras.containsKey(Params.MODE)) {
                mode = (Mode) extras.get(Params.MODE);
            }
            if (extras.containsKey(Params.FILE_TYPE)) {
                type = extras.getString(Params.FILE_TYPE);
            }
        }
        if (file == null) {
            file = Environment.getExternalStorageDirectory();
        }
        switch (mode) {
            case SAVE_FILE:
                fileName.setVisibility(View.VISIBLE);
                acceptBtn.setText(R.string.cv_save);
                break;
            case CHOOSE_PATH:
                fileName.setVisibility(View.GONE);
                acceptBtn.setText(getString(R.string.cv_choose));
                break;
            case CHOOSE_FILE:
                fileName.setVisibility(View.GONE);
                acceptBtn.setVisibility(View.GONE);
                findViewById(R.id.create_folder_btn).setVisibility(View.GONE);
        }
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case SAVE_FILE:
                        String fileNameStr = fileName.getText().toString();
                        if (!TextUtils.isEmpty(fileNameStr)) {
                            //todo можно возвращаться
                            file = new File(file, fileNameStr);
                            if (getParent() == null) {
                                setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_NAME, file.getAbsolutePath()));
                            } else {
                                getParent().setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_NAME, file.getAbsolutePath()));
                            }
                            finish();
                        } else {
                            DialogUtils.showAlert(FileChooseActivity.this, getString(R.string.cv_file_name_empty_alert),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                        break;
                    case CHOOSE_PATH:
                        if (getParent() == null) {
                            setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_NAME, file.getAbsolutePath()));
                        } else {
                            getParent().setResult(Activity.RESULT_OK, new Intent().putExtra(RESULT_NAME, file.getAbsolutePath()));
                        }
                        finish();
                        break;
                }
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.create_folder_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FileChooseActivity.this);
                builder.setTitle(getString(R.string.cv_create_folder));
                final EditText folderName = new EditText(FileChooseActivity.this);
                folderName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                folderName.setHint(getString(R.string.folder_name));
                builder.setView(folderName);
                builder.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File createdfile = new File(file, folderName.getText().toString());
                        createdfile.mkdirs();
                        changeFileList();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        changeFileList();
    }

    private void changeFileList() {
        //на случай, если мы выбрали какую-то папку по умолчанию, а потом ее удалили, чтобы избежать казусов они все здесь пересоздадутся
        file.mkdirs();
        setTitle(file.getAbsolutePath());
        listView.setAdapter(new FileChooseAdapter(this, file, mode, type));
    }

    @Override
    public void onBackPressed() {
        if (file.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath()))
            super.onBackPressed();
        else {
            file = file.getParentFile();
            changeFileList();
        }

    }
}
