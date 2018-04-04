package ru.devsp.apps.keeppasswords.di.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import ru.devsp.apps.keeppasswords.MainActivity;
import ru.devsp.apps.keeppasswords.di.modules.DbModule;
import ru.devsp.apps.keeppasswords.di.modules.ViewModelModule;
import ru.devsp.apps.keeppasswords.view.CategoriesFragment;
import ru.devsp.apps.keeppasswords.view.LoginFragment;
import ru.devsp.apps.keeppasswords.view.NoteFragment;
import ru.devsp.apps.keeppasswords.view.NotesFragment;
import ru.devsp.apps.keeppasswords.view.SettingsFragment;

/**
 * Компонент di
 * Created by gen on 27.09.2017.
 */
@Component(modules = {
        ViewModelModule.class,
        DbModule.class,
})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        AppComponent build();
    }

    void inject(MainActivity activity);

    void inject(NotesFragment fragment);

    void inject(NoteFragment fragment);

    void inject(LoginFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(CategoriesFragment fragment);

}
