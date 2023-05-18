package com.zhuguohui.myapplication;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2023/5/18
 * Time: 17:08
 * Desc:
 * </pre>
 */
public interface TRSFunction<T,R> {
    R call(T t);
}
