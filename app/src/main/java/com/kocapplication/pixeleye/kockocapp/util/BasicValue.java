package com.kocapplication.pixeleye.kockocapp.util;

/**
 * Created by Han_ on 2016-06-21.
 */
public class BasicValue {
    private static BasicValue ourInstance = new BasicValue();

    public static BasicValue getInstance() {
        return ourInstance;
    }

    private BasicValue() {
    }
}
