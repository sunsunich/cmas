package org.cmas.util;

public interface Task<T>{

    T doTask() throws Exception;

    void doAfterTask(T result);

    void handleError();

    String getName();

}
