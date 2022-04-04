package me.ikevoodoo.basicchest.database;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Class used to simplify the implementation of a subscriber.
 * Internal use only.
 * */
public class SubscriberImplemenation<T> implements Subscriber<T> {

    /**
     * Callback for when a new element is passed to the subscriber.
     * Can be passed to the constructor as a lambda.
     * */
    public interface Callback<T> {
        void onNext(T t);
    }

    private final Callback<T> callback;

    public SubscriberImplemenation() {
        this.callback = null;
    }

    public SubscriberImplemenation(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        if(callback != null) {
            callback.onNext(t);
        }
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        // Unused
    }
}
