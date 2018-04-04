package ru.devsp.apps.keeppasswords.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.keeppasswords.model.db.CategoryDao;
import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.tools.AppExecutors;

/**
 * Категории
 * Created by gen on 03.10.2017.
 */
@Singleton
public class CategoriesRepository {

    private final AppExecutors mAppExecutors;

    private CategoryDao mDao;

    @Inject
    CategoriesRepository(AppExecutors appExecutors, CategoryDao dao) {
        mAppExecutors = appExecutors;
        mDao = dao;
    }

    public LiveData<List<Category>> getCategories(){
        return mDao.getCategories();
    }

    public LiveData<Long> add(Category item) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> {
            long id = (int)mDao.insert(item);
            mAppExecutors.mainThread().execute(() -> result.postValue(id));
        });
        return result;
    }

    public void delete(Category item) {
        mAppExecutors.diskIO().execute(() -> mDao.delete(item));
    }

}
