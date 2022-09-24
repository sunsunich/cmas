package org.cmas.util.android;

import android.util.Log;
import androidx.annotation.AnyThread;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import javax.annotation.Nullable;

public abstract class TaskViewModel<A, P, R> extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private boolean hasEverStarted;
    private Disposable disposable;
    protected MutableLiveData<P> progress;
    protected MutableLiveData<R> result;

    @UiThread
    public void init(boolean isSingleLiveResult) {
        result = isSingleLiveResult ? new SingleLiveEvent<>() : new MutableLiveData<>();
        progress = new MutableLiveData<>();
        disposable = null;
        hasEverStarted = false;
    }

    @UiThread
    public void start(final A arg) {
        hasEverStarted = true;
        if (inProgress()) {
            Log.w(getClass().getName(), "Background task is still running");
            return;
        }
        disposable = Completable.fromAction(
                () -> result.postValue(TaskViewModel.this.runInBackground(arg))
        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
        compositeDisposable.add(disposable);
    }

    @UiThread
    public boolean inProgress() {
        return disposable != null && !disposable.isDisposed();
    }

    @Nullable
    @WorkerThread
    protected abstract R runInBackground(A arg);

    @AnyThread
    protected void reportProgress(P progressData) {
        progress.postValue(progressData);
    }

    @UiThread
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

    @UiThread
    public boolean hasEverStarted() {
        return hasEverStarted;
    }

    @UiThread
    public LiveData<P> getProgress() {
        return progress;
    }

    @UiThread
    public LiveData<R> getResult() {
        return result;
    }

    @UiThread
    public static <T extends TaskViewModel<?, ?, ?>> T safelyInitTask(
            ViewModelStoreOwner storeOwner,
            Class<T> taskClass) {
        return safelyInitTask(storeOwner, taskClass, false);
    }

    @UiThread
    public static <T extends TaskViewModel<?, ?, ?>> T safelyInitTask(
            ViewModelStoreOwner storeOwner,
            Class<T> taskClass,
            boolean isSingleLiveResult) {
        T task = new ViewModelProvider(storeOwner).get(taskClass);
        if (task.getResult() == null) {
            task.init(isSingleLiveResult);
        }
        return task;
    }

    @UiThread
    public static <T extends TaskViewModel<?, ?, ?>> T safelyInitTask(
            ViewModelStoreOwner storeOwner,
            ViewModelProvider.Factory factory,
            Class<T> taskClass,
            boolean isSingleLiveResult) {
        T task = new ViewModelProvider(storeOwner, factory).get(taskClass);
        if (task.getResult() == null) {
            task.init(isSingleLiveResult);
        }
        return task;
    }
}
