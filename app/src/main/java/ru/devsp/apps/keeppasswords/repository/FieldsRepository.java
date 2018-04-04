package ru.devsp.apps.keeppasswords.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.keeppasswords.model.db.FieldDao;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.tools.AppExecutors;

/**
 * Поля
 * Created by gen on 03.10.2017.
 */
@Singleton
public class FieldsRepository {

    private final AppExecutors mAppExecutors;

    private FieldDao mDao;

    @Inject
    FieldsRepository(AppExecutors appExecutors, FieldDao dao) {
        mAppExecutors = appExecutors;
        mDao = dao;
    }

    public LiveData<List<Field>> getFields(long note) {
        return mDao.getFields(note);
    }

    public LiveData<Long> add(Field item) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> {
            long id = mDao.insert(item);
            mAppExecutors.mainThread().execute(() -> result.postValue(id));
        });
        return result;
    }

    public void insert(Field[] fields) {
        mAppExecutors.diskIO().execute(() -> mDao.insert(fields));
    }

    public void update(List<Field> items) {
        mAppExecutors.diskIO().execute(() -> mDao.update(items));
    }


    public void deleteFieldsByNote(long id){
        mAppExecutors.diskIO().execute(() -> mDao.deleteByNote(id));
    }
}
