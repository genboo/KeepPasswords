package ru.devsp.apps.keeppasswords.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.repository.FieldsRepository;
import ru.devsp.apps.keeppasswords.repository.NoteRepository;
import ru.devsp.apps.keeppasswords.tools.AbsentLiveData;


/**
 * Записи
 * Created by gen on 20.10.2017.
 */

public class NotesViewModel extends ViewModel {

    private NoteRepository mNoteRepository;
    private FieldsRepository mFieldsRepository;
    private LiveData<List<Note>> mNotes;
    private MutableLiveData<Long> mNotesSwitcher = new MutableLiveData<>();

    @Inject
    NotesViewModel(NoteRepository noteRepository, FieldsRepository fieldsRepository) {
        mNoteRepository = noteRepository;
        mFieldsRepository = fieldsRepository;

        mNotes = Transformations.switchMap(mNotesSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return mNoteRepository.getNotes(id);
            }
        });
    }

    public LiveData<List<Note>> getNotes(){
        return mNotes;
    }

    public void setCategory(long id){
        mNotesSwitcher.postValue(id);
    }

    public LiveData<Long> add(Note item) {
        return mNoteRepository.add(item);
    }

    public void addFields(Field[] items){
        mFieldsRepository.insert(items);
    }

    public void delete(Note note){
        mNoteRepository.delete(note);
    }

    public void deleteFieldsByNote(long id){
        mFieldsRepository.deleteFieldsByNote(id);
    }

}
