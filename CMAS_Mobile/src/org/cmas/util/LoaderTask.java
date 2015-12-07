package org.cmas.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import org.cmas.R;

public class LoaderTask<T> extends AsyncTask<Integer, Integer, T> {

    private final Task<T> task;
    private Activity activity;
    private String message;

    private ProgressDialog dialog;

    public LoaderTask(Activity activity, Task<T> task) {
        this(activity, task, activity.getString(R.string.wait_text));
    }

    public LoaderTask(Activity activity, Task<T> task, String message) {
        this.activity = activity;
        this.task = task;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(activity, "", message, true);
    }

    @Override
    protected T doInBackground(Integer... integers) {
        try {
            return task.doTask();
        }
        catch (Exception e){
            Log.e(getClass().getName(), "Error in: " + task.getName(), e);
            return null;
        }
    }



    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        activity = null;
        dialog.dismiss();
        if(result == null){
            task.handleError();
        }
        else{
            task.doAfterTask(result);
        }
    }
}
