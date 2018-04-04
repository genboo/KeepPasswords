package ru.devsp.apps.keeppasswords.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.tools.PermissionsHelper;
import ru.devsp.apps.keeppasswords.viewmodel.SettingsViewModel;

/**
 * Настройки
 * Created by gen on 03.04.2018.
 */

public class SettingsFragment extends BaseFragment {

    private Button mSaveBackup;
    private Button mRestoreBackup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initFragment();

        mSaveBackup = view.findViewById(R.id.btn_save_backup);
        mRestoreBackup = view.findViewById(R.id.btn_restore_backup);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        SettingsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel.class);

        mSaveBackup.setOnClickListener(v -> saveBackup(viewModel));
        mRestoreBackup.setOnClickListener(v -> restoreBackup(viewModel));

        if (!PermissionsHelper.havePermissionStorage(getContext())) {
            PermissionsHelper.requestLocationPermissions(this);
            mSaveBackup.setEnabled(false);
            mRestoreBackup.setEnabled(false);
        }
    }


    private void saveBackup(SettingsViewModel viewModel) {
        if (viewModel.backup(getContext())) {
            showToast("Забекаплено");
        }
    }

    private void restoreBackup(SettingsViewModel viewModel) {
        if (viewModel.restore(getContext())) {
            showToast("Восстановлено");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsHelper.PERMISSION_REQUEST_CODE_STORAGE
                && PermissionsHelper.havePermissionStorage(getContext())) {
            mSaveBackup.setEnabled(true);
            mRestoreBackup.setEnabled(true);
        }
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return "Настройки";
    }
}
