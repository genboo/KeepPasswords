package ru.devsp.apps.keeppasswords.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.repository.CategoriesRepository;
import ru.devsp.apps.keeppasswords.repository.FieldsRepository;
import ru.devsp.apps.keeppasswords.repository.NoteRepository;

/**
 * Запись
 * Created by gen on 20.10.2017.
 */

public class MainActivityModel extends ViewModel {

    private CategoriesRepository mCategoriesRepository;
    private FieldsRepository mFieldsRepository;
    private NoteRepository mNoteRepository;
    private LiveData<List<Category>> mCategories;

    @Inject
    MainActivityModel(CategoriesRepository categoriesRepository,
                      FieldsRepository fieldsRepository,
                      NoteRepository noteRepository) {
        mCategoriesRepository = categoriesRepository;
        mFieldsRepository = fieldsRepository;
        mNoteRepository = noteRepository;
        mCategories = categoriesRepository.getCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public LiveData<Long> addCategory(Category item) {
        return mCategoriesRepository.add(item);
    }

    public LiveData<Long> addNote(Note item) {
        return mNoteRepository.add(item);
    }

    public LiveData<Long> addField(Field item) {
        return mFieldsRepository.add(item);
    }

}
