package ru.devsp.apps.keeppasswords.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.viewmodel.MainActivityModel;

/**
 * Импорт старых записей
 * Created by gen on 04.12.2017.
 */

public class ImportOldDatabase {

    private static final String DATA_PATH = "/data/";

    private ImportOldDatabase() {
        //empty constructor
    }

    public static void importOld(MainActivityModel model, AppExecutors appExecutors, Context context) {
        appExecutors.diskIO().execute(() -> update(model, context));
    }

    private static void update(MainActivityModel model, Context context) {
        File dbFile = context.getDatabasePath(DBConnector.DB_NAME);
        if(!dbFile.exists()){
            if(!loadBackup(context, dbFile)){
                return;
            }
        }
        SQLiteDatabase db = DBConnector.getDB(context);
        Cursor cg = db.query("groups", null, null, null, null, null, null);
        if (cg.moveToFirst()) {
            do {
                Category item = new Category(cg.getString(cg.getColumnIndex("name")));
                item.iconId = cg.getInt(cg.getColumnIndex("icon"));
                item.id = cg.getLong(cg.getColumnIndex("id"));
                model.addCategory(item);
            } while (cg.moveToNext());
        }
        cg.close();

        Cursor ci = db.query("items", null, null, null, null, null, null);
        if (ci.moveToFirst()) {
            do {
                long id = ci.getLong(ci.getColumnIndex("id"));
                Note note = new Note(ci.getString(ci.getColumnIndex("name")));
                note.id = id;
                note.categoryId = ci.getInt(ci.getColumnIndex("group_id"));
                model.addNote(note);
                Cursor cf = db.rawQuery("select ifl.id, ifl.field_id, ifl.item_id, ifl.value, f.name, " +
                        "f.type  from items_fields ifl, fields f where ifl.field_id = f.id and ifl.item_id=?", new String[]{String.valueOf(id)});
                if (cf.moveToFirst()) {
                    do {
                        Field field = new Field();
                        field.noteId = id;
                        field.hidden = false;
                        field.typeId = cf.getInt(cf.getColumnIndex("type"));
                        field.value = cf.getString(cf.getColumnIndex("value"));
                        field.name = cf.getString(cf.getColumnIndex("name"));
                        model.addField(field);
                    } while (cf.moveToNext());
                }
                cf.close();

            } while (ci.moveToNext());
        }
        ci.close();
        db.close();
        if(!dbFile.delete()){
            Logger.e("Не удалось удалить");
        }
    }

    private static boolean loadBackup(Context context, File dbFile){
        String pack = context.getPackageName();
        String fullBackupDir = Environment.getExternalStorageDirectory().getPath() + DATA_PATH + pack;
        File backup = new File(fullBackupDir, DBConnector.DB_NAME);

        if(backup.exists()) {
            try {
                if(dbFile.createNewFile()){
                    Logger.e("good");
                }
                FileInputStream fileInputStream = new FileInputStream(backup);
                FileChannel src = fileInputStream.getChannel();
                FileOutputStream fileOutputStream = new FileOutputStream(dbFile);
                FileChannel dst = fileOutputStream.getChannel();

                dst.transferFrom(src, 0, src.size());

                fileInputStream.close();
                fileOutputStream.close();
                src.close();
                dst.close();
            } catch (IOException ex) {
                Logger.e(ex);
            }
            return true;
        }
        return false;
    }

}
