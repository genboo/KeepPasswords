package ru.devsp.apps.keeppasswords.view.components;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.devsp.apps.keeppasswords.MainActivity;
import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.view.CategoriesFragment;
import ru.devsp.apps.keeppasswords.view.NoteFragment;
import ru.devsp.apps.keeppasswords.view.NotesFragment;
import ru.devsp.apps.keeppasswords.view.SettingsFragment;

/**
 * Навигация между фрагментами
 * Created by gen on 21.10.2017.
 */

public class Navigation {

    private FragmentManager mFragmentManager;

    private MutableLiveData<Boolean> mUpdateToolbar = new MutableLiveData<>();

    public Navigation(MainActivity activity){
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public LiveData<Boolean> getOnToolbarNeedChange(){
        return mUpdateToolbar;
    }

    /**
     * Переход к списку записей в категории
     * @param category категория
     */
    public void navigateToNotes(long category){
        navigate(NotesFragment.getInstance(category), "notes", false);
    }

    /**
     * Переход к сообщению об отсутствующих категориях
     */
    public void navigateToFirstLaunchScreen(){
        //в процессе
    }

    /**
     * Переход к записи
     * @param note запись
     */
    public void navigateToNote(long note){
        navigate(NoteFragment.getInstance(note), "note", true);
    }

    /**
     * Переход списку категорий
     */
    public void navigateToCategories(){
        navigate(new CategoriesFragment(), "categories", false);
    }

    /**
     * Переход к настройкам
     */
    public void navigateToSettings(){
        navigate(new SettingsFragment(), "settings", false);
    }

    /**
     * Общий метод перехода к новому фрагменту
     * @param fragment Фрагмент
     * @param tag Тэг
     * @param back Нужно ли помещать в back stack
     */
    private void navigate(Fragment fragment, String tag, boolean back) {
        if (!back) {
            mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (back) {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right_2);
            ft.addToBackStack(tag);
        }else{
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                    R.anim.fade_in, R.anim.fade_out);
        }
        ft.replace(R.id.content, fragment);
        ft.commit();

        mUpdateToolbar.postValue(true);
    }


}
