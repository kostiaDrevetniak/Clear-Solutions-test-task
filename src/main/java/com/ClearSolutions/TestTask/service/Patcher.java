package com.ClearSolutions.TestTask.service;

public interface Patcher<T> {
    void patch(T existed, T incoming);
}
