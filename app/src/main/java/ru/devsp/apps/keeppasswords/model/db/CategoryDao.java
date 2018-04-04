package ru.devsp.apps.keeppasswords.model.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.devsp.apps.keeppasswords.model.objects.Category;


/**
 * Получение данных по категориям
 * Created by gen on 07.11.2017.
 */
@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category item);

    @Delete
    void delete(Category item);

    @Update
    void update(Category item);

    @Query("SELECT * FROM Category c ORDER BY c.id ASC")
    LiveData<List<Category>> getCategories();

}
