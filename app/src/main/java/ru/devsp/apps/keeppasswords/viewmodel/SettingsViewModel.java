package ru.devsp.apps.keeppasswords.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.di.modules.DbModule;
import ru.devsp.apps.keeppasswords.tools.Logger;

public class SettingsViewModel extends ViewModel {

    private String path = Environment.getExternalStorageDirectory().getPath() + "/data/";

    @Inject
    SettingsViewModel() {

    }

    public boolean backup(Context context) {
        String fullBackupDir = path + context.getPackageName();
        File dbFile = context.getDatabasePath(DbModule.DB_NAME);
        if (dbFile.exists()) {
            try {
                File dir = new File(fullBackupDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    Logger.e("Каталоги не созданы");
                }

                File backup = new File(fullBackupDir, DbModule.DB_NAME);
                if (!backup.exists() && !backup.createNewFile()) {
                    Logger.e("Копия не создана");
                }
                try (FileInputStream fileInputStream = new FileInputStream(dbFile)) {
                    FileChannel src = fileInputStream.getChannel();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(backup)) {
                        FileChannel dst = fileOutputStream.getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        return true;
                    }
                }
            } catch (IOException ex) {
                Logger.e(ex);
            }
        }
        return false;
    }

    public boolean restore(Context context) {
        String fullBackupDir = path + context.getPackageName();
        File backup = new File(fullBackupDir, DbModule.DB_NAME);
        if (backup.exists()) {
            try {
                File dbFile = context.getDatabasePath(DbModule.DB_NAME);
                if (!dbFile.exists() && !dbFile.createNewFile()) {
                    Logger.e("Локальная БД не создана");
                }
                try (FileInputStream fileInputStream = new FileInputStream(backup)) {
                    FileChannel src = fileInputStream.getChannel();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(dbFile)) {
                        FileChannel dst = fileOutputStream.getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        return true;
                    }
                }
            } catch (IOException ex) {
                Logger.e(ex);
            }
        }
        return false;
    }

}