package com.polytech.edt.model;

public interface ADELoadable<T> {
    T load() throws Exception;
}
