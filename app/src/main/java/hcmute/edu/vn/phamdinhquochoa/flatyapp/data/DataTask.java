package hcmute.edu.vn.phamdinhquochoa.flatyapp.data;

import java.util.function.Consumer;

import hcmute.edu.vn.phamdinhquochoa.flatyapp.data.impl.DataTaskImpl;

public interface DataTask {

    DataTask addOnCompleteListener(Runnable callback);

    DataTask addOnFailureListener(Consumer<Throwable> exceptionConsumer);

    interface Invokable extends DataTask {
        void invokeOnComplete();

        void invokeOnFailure(Throwable throwable);
    }

    static Invokable createDataTask() {
        return new DataTaskImpl();
    }

}
