package ru.devsp.apps.keeppasswords.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.devsp.apps.keeppasswords.model.objects.Field;


/**
 * Получение данных по полям
 * Created by gen on 19.10.2017.
 */
@Dao
public interface FieldDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Field item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Field[] item);

    @Delete
    void delete(Field item);

    @Update
    void update(Field item);

    @Update
    void update(List<Field> item);

    @Query("SELECT * FROM Field f WHERE f.note_id = :note")
    LiveData<List<Field>> getFields(long note);

    @Query("DELETE FROM Field WHERE note_id = :note")
    void deleteByNote(long note);


}
