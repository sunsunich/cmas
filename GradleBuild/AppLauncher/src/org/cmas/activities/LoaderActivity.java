package org.cmas.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.cmas.AppLoader;
import org.cmas.R;
import org.cmas.activities.settings.CodeEnter;
import org.cmas.util.LoaderProgressTask;
import org.cmas.util.ProgressTask;

public class LoaderActivity extends Activity {
    public static final int PLAY_SERVICES =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        progress= (ProgressBar) findViewById(R.id.progress);
        progressStatus= (TextView) findViewById(R.id.progress_status);
        int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            startLoaderTask();
        } else {
            GooglePlayServicesUtil.getErrorDialog(code, this, PLAY_SERVICES, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK&& requestCode== PLAY_SERVICES) {
            startLoaderTask();
        }
        else {
            finish();
        }
    }
    private ProgressBar progress;
    private TextView progressStatus;
    private void startLoaderTask() {
        final Activity activity = this;
        new LoaderProgressTask<String>(
                new ProgressTask<String>() {

                    @Override
                    public String doTask(OnPublishProgressListener listener) {
                        AppLoader.loadApp(activity, listener);
                        return "";
                    }

                    @Override
                    public void doAfterTask(String result) {
                        Intent intent = new Intent(activity, CodeEnter.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void handleError(String error) {
                        BaseActivity.reportError(activity, error);
                    }

                    @Override
                    public String getName() {
                        return "loadApp";
                    }
                },
                new LoaderProgressTask.OnProgressUpdateListener() {
                    @Override
                    public void onStart() {
                        progressStatus.setText(getString(R.string.loading));
                        progress.setMax(100);
                        progress.setProgress(0);

                    }

                    @Override
                    public void onProgressUpdate(String... p) {
                        progressStatus.setText(p[0]);
                        if(p.length>1)
                        {
                            progress.setProgress(progress.getProgress()+Integer.valueOf(p[1]));
                        }
                    }

                    @Override
                    public void onFinish() {
                        progress.setProgress(100);
                        progressStatus.setText(getString(R.string.loading_complete));
                    }
                }
        ).execute(1);
    }

}
