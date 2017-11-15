package com.arpithasomayaji.chatroomapplication;

/**
 * Created by Arpitha.Somayaji on 10/23/2017.
 */
public interface BasePresenter<T> {

    void bind(T view);
    void unbind();

}