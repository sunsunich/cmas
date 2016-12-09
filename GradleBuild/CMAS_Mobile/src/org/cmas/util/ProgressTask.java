package org.cmas.util;

/**
 * User: ABadretdinov
 * Date: 06.02.14
 * Time: 13:46
 */
public interface ProgressTask<R> {

    R doTask(ProgressTask.OnPublishProgressListener listener) throws Exception;

    void doAfterTask(R result);

    void handleError(String error);

    String getName();


    public interface OnPublishProgressListener {
        void onPublishProgress(String... progress);
    }
}
