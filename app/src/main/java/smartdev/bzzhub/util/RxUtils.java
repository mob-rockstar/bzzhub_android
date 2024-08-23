package smartdev.bzzhub.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {
    private static final int RETRY_COUNT = 3;
    private static final int THREAD_POOL_COUNT = 10;
    private static ExecutorService executorService;

    private static ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(THREAD_POOL_COUNT);
        }
        return executorService;
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySchedulersSingleIO() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySchedulersToSingle() {
        return single -> single.subscribeOn(Schedulers.from(getExecutorService()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> applyNotRetrySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> applySchedulersWithRetry() {
        return observable -> observable
                .retry(RETRY_COUNT)
                .subscribeOn(Schedulers.from(getExecutorService()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> applySchedulerSingle() {
        return observable -> observable
                .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
