package org.cmas.util;

import android.os.AsyncTask;
import android.util.Log;

/**
 * User: ABadretdinov
 * Date: 06.02.14
 * Time: 13:52
 */
public class LoaderProgressTask<R> extends AsyncTask<Object,String,R> implements ProgressTask.OnPublishProgressListener{
    private final ProgressTask<R> task;
    private OnProgressUpdateListener listener;
    private String error=null;
    public LoaderProgressTask(ProgressTask<R> task, OnProgressUpdateListener listener) {
        this.task = task;
        this.listener = listener;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(listener!=null){
            listener.onStart();
        }
    }

    @Override
    protected R doInBackground(Object... params)
    {
        try {
            return task.doTask(this);
        }
        catch (Exception e){
            error=e.getLocalizedMessage();
            Log.e(getClass().getName(), "Error in: " + task.getName(), e);
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if(listener!=null){
            //todo хз как сделать. если OnProgressUpdateListener
            listener.onProgressUpdate(values);
        }
    }

    @Override
    protected void onPostExecute(R r) {
        super.onPostExecute(r);
        if(listener!=null){
            listener.onFinish();
        }
        if(r==null){
            task.handleError(error);
        }
        else {
            task.doAfterTask(r);
        }

    }

    @Override
    public void onPublishProgress(String... progress) {
        //todo если прогресс есть, но <1, то не вызывать, иначе слишком затратно.
        publishProgress(progress);
    }

    public interface OnProgressUpdateListener{
        void onStart();
        void onProgressUpdate(String... progress);
        void onFinish();
    }
}
