package ru.devsp.apps.keeppasswords.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.devsp.apps.keeppasswords.model.db.NoteDao;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.tools.AppExecutors;

/**
 * Записи
 * Created by gen on 03.10.2017.
 */
@Singleton
public class NoteRepository {

    private final AppExecutors mAppExecutors;

    private NoteDao mDao;

    @Inject
    NoteRepository(AppExecutors appExecutors, NoteDao dao) {
        mAppExecutors = appExecutors;
        mDao = dao;
    }

    public LiveData<List<Note>> getNotes(long category) {
        return mDao.getNotes(category);
    }

    public LiveData<Note> getNote(long note) {
        return mDao.getNote(note);
    }

    public LiveData<Long> add(Note item) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> {
            long id = mDao.insert(item);
            mAppExecutors.mainThread().execute(() -> result.postValue(id));
        });
        return result;
    }

    public void update(Note item) {
        mAppExecutors.diskIO().execute(() -> mDao.update(item));
    }

    public void delete(Note item) {
        mAppExecutors.diskIO().execute(() -> mDao.delete(item));
    }

}
