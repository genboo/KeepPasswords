package ru.devsp.apps.keeppasswords.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import ru.devsp.apps.keeppasswords.model.db.CategoryDao;
import ru.devsp.apps.keeppasswords.model.db.Database;
import ru.devsp.apps.keeppasswords.model.db.FieldDao;
import ru.devsp.apps.keeppasswords.model.db.NoteDao;

/**
 * Инициализация базы данных
 * Created by gen on 12.09.2017.
 */
@Module
public class DbModule {

    public static final String DB_NAME = "keeppswdb";

    /**
     * Базовый провайдер базы данных
     *
     * @param context Контекст
     * @return База данных
     */
    @Provides
    @Singleton
    Database provideDatabase(Context context) {
        return Room
                .databaseBuilder(context, Database.class, DB_NAME)
                .build();
    }

    /**
     * Записи
     *
     * @param db База данных
     * @return Dao записей
     */
    @Provides
    @Singleton
    NoteDao provideNoteDao(Database db) {
        return db.noteDao();
    }

    /**
     * Поля записей
     *
     * @param db База данных
     * @return Dao полей
     */
    @Provides
    @Singleton
    FieldDao provideFieldDao(Database db) {
        return db.fieldDao();
    }

    /**
     * Категории
     *
     * @param db База данных
     * @return Dao категорий
     */
    @Provides
    @Singleton
    CategoryDao provideCategoryDao(Database db) {
        return db.categoryDao();
    }

}
