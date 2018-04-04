package ru.devsp.apps.keeppasswords.tools;

import android.arch.lifecycle.LiveData;

/**
 *
 * Created by gen on 31.08.2017.
 */

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }

    public static <T> LiveData<T> create() {
        return new AbsentLiveData();
    }
}