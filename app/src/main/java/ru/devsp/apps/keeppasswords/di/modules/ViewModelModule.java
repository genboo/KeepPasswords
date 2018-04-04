package ru.devsp.apps.keeppasswords.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.devsp.apps.keeppasswords.di.ViewModelFactory;
import ru.devsp.apps.keeppasswords.viewmodel.CategoriesViewModel;
import ru.devsp.apps.keeppasswords.viewmodel.MainActivityModel;
import ru.devsp.apps.keeppasswords.viewmodel.NoteViewModel;
import ru.devsp.apps.keeppasswords.viewmodel.NotesViewModel;
import ru.devsp.apps.keeppasswords.viewmodel.SettingsViewModel;

/**
 * ViewModel
 * Created by gen on 12.09.2017.
 */
@SuppressWarnings("unused")
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NotesViewModel.class)
    abstract ViewModel bindNotesViewModel(NotesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel.class)
    abstract ViewModel bindNoteViewModel(NoteViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel.class)
    abstract ViewModel bindCategoriesViewModel(CategoriesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityModel.class)
    abstract ViewModel bindMainActivityModel(MainActivityModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}