package ru.devsp.apps.keeppasswords.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.devsp.apps.keeppasswords.model.objects.Note;


/**
 * Получение данных по записям
 * Created by gen on 19.10.2017.
 */
@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note item);

    @Delete
    void delete(Note item);

    @Update
    void update(Note item);

    @Query("SELECT * FROM Note n WHERE n.category_id = :category ORDER BY n.name ASC")
    LiveData<List<Note>> getNotes(long category);

    @Query("SELECT * FROM Note n WHERE n.id = :note")
    LiveData<Note> getNote(long note);

}
