package ru.devsp.apps.keeppasswords.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.repository.CategoriesRepository;


/**
 * Записи
 * Created by gen on 20.10.2017.
 */

public class CategoriesViewModel extends ViewModel {

    private CategoriesRepository mCategoriesRepository;
    private LiveData<List<Category>> mCategories;

    @Inject
    CategoriesViewModel(CategoriesRepository categoriesRepository) {
        mCategoriesRepository = categoriesRepository;
        mCategories = categoriesRepository.getCategories();
    }

    public LiveData<List<Category>> getCategories(){
        return mCategories;
    }

    public LiveData<Long> add(Category item) {
        return mCategoriesRepository.add(item);
    }

    public void delete(Category item){
        mCategoriesRepository.delete(item);
    }
}
