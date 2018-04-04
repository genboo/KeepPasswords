package ru.devsp.apps.keeppasswords.model.db;

import android.arch.persistence.room.RoomDatabase;

import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;

/**
 * База данных
 * Created by gen on 31.08.2017.
 */
@android.arch.persistence.room.Database(version = 1, entities = {
        Note.class,
        Category.class,
        Field.class,
})
public abstract class Database extends RoomDatabase {

    public abstract NoteDao noteDao();

    public abstract FieldDao fieldDao();

    public abstract CategoryDao categoryDao();

}
