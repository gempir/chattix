package com.gempir.chattix;

/**
 * Created by Matija on 11/20/2017.
 */

public interface IAPIHandler<T> {
    void onSuccess(T data);
}
