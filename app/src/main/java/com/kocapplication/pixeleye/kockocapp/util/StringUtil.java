package com.kocapplication.pixeleye.kockocapp.util;

import android.util.Log;

import com.kocapplication.pixeleye.kockocapp.model.Course;

import java.util.List;

/**
 * Created by hp on 2016-11-28.
 */
public class StringUtil {

    public boolean findDuplicateValue(List<Course> list) {
        for (int i = 0; i < list.size(); i++) {
            String str1 = list.get(i).getTitle();
            for (int j = 0; j < list.size(); j++) {
                if (i == j) continue;
                String str2 = list.get(j).getTitle();
                if (str1.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
