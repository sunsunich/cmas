package org.cmas.util.android;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class TaskViewModel<A, P, R> extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Disposable disposable;
    private MutableLiveData<P> progress;
    private MutableLiveData<R> result;

    // UI Thread
    public void init() {
        result = new MutableLiveData<>();
        progress = new MutableLiveData<>();
        disposable = null;
    }

    // UI Thread
    public void start(final A... args) {
        if (disposable != null && !disposable.isDisposed()) {
            Log.w(getClass().getName(), "Background task is still running");
            return;
        }
        disposable = Completable.fromAction(
                () -> result.postValue(TaskViewModel.this.runInBackground(args))
        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
        compositeDisposable.add(disposable);
    }

    protected abstract R runInBackground(A... args);

    protected void reportProgress(P progressData) {
        progress.postValue(progressData);
    }

    // UI Thread
    public void cancel() {
        if (disposable == null) {
            Log.w(getClass().getName(), "No background task to cancel");
            return;
        }
        Log.w(getClass().getName(), "Cancel task");
        disposable.dispose();
    }

    // todo add and sync isCancelled flag
//    public boolean isCancelled() {
//        if (disposable == null) {
//            Log.w(getClass().getName(), "No background task");
//            return false;
//        }
//        return disposable.isDisposed();
//    }

    // UI Thread
    public LiveData<P> getProgress() {
        return progress;
    }

    // UI Thread
    public LiveData<R> getResult() {
        return result;
    }
}
