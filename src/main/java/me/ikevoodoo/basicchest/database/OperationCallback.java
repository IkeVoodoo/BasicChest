package me.ikevoodoo.basicchest.database;

public interface OperationCallback<T> {

    void accept(T value);

}
