package hcmute.edu.vn.phamdinhquochoa.flatyapp.data.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.DataTask;

public class DataTaskImpl implements DataTask.Invokable {

    private final List<Runnable> completeListeners = new LinkedList<>();
    private final List<Consumer<Throwable>> failureListeners = new LinkedList<>();

    private boolean finished = false;
    private Throwable exception;

    @Override
    public DataTask addOnCompleteListener(Runnable callback) {
        if(finished) {
            if(exception == null) {
                callback.run();
            }
            return this;
        }

        completeListeners.add(callback);
        return this;
    }

    @Override
    public DataTask addOnFailureListener(Consumer<Throwable> exceptionConsumer) {
        if(finished) {
            if(exception != null) {
                exceptionConsumer.accept(exception);
            }
            return this;
        }

        failureListeners.add(exceptionConsumer);
        return this;
    }

    @Override
    public void invokeOnComplete() {
        finished = true;
        completeListeners.forEach(Runnable::run);
        completeListeners.clear();
    }

    @Override
    public void invokeOnFailure(Throwable throwable) {
        exception = throwable;
        finished = true;
        failureListeners.forEach(c -> c.accept(throwable));
        failureListeners.clear();
    }
}
