package ru.devsp.apps.keeppasswords.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.App;
import ru.devsp.apps.keeppasswords.MainActivity;
import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.di.components.AppComponent;
import ru.devsp.apps.keeppasswords.tools.Encoder;
import ru.devsp.apps.keeppasswords.view.components.Navigation;


/**
 * Базовый фрагмент
 * Created by gen on 20.09.2017.
 */

public abstract class BaseFragment extends Fragment {

    Navigation mNavigation;

    AppComponent component;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected void initFragment() {
        if (component == null) {
            component = ((App) getActivity().getApplication()).getAppComponent();
            inject();
        }
        mNavigation = ((MainActivity)getActivity()).getNavigation();
    }

    public AppComponent getComponent() {
        return component;
    }

    protected abstract void inject();

    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    protected String getTitle() {
        return getResources().getString(R.string.app_name);
    }

    public Encoder getEncoder(){
        if(getActivity() == null){
            return null;
        }
        return ((MainActivity)getActivity()).getEncoder();
    }

    /**
     * Инициализировать toolbar
     */
    protected void updateToolbar() {
        if(getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity)getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(getTitle());
                activity.getSupportActionBar().show();
            }
        }
    }

    protected void updateTitle(String title){
        if(getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(title);
            }
        }
    }

    /**
     * Показать простое сообщение
     *
     * @param message
     */
    protected void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected void showToast(String message, int length) {
        Toast.makeText(getContext(), message, length).show();
    }

}
