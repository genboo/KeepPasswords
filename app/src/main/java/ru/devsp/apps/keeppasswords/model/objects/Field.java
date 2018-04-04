package ru.devsp.apps.keeppasswords.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.tools.Encoder;

/**
 * Поля записи
 * Created by gen on 20.10.2017.
 */

@Entity
public class Field {

    public static final int TYPE_SHORT_TEXT = 1;
    public static final int TYPE_NUMBER = 2;
    public static final int TYPE_TEXT = 3;
    public static final int TYPE_PASSWORD = 4;
    public static final int TYPE_AUTH = 5;

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "note_id")
    public long noteId;

    @ColumnInfo(name = "type_id")
    public int typeId;

    public String name;

    public String value;

    public boolean hidden;

    @Ignore
    public String pin;

    public static Field[] getDefaultFields(Encoder encoder, Context context, long id) {
        Field[] fields = new Field[2];

        fields[0] = new Field();
        fields[1] = new Field();
        fields[0].name = encoder.enc(context.getString(R.string.field_login));
        fields[0].typeId = Field.TYPE_SHORT_TEXT;
        fields[0].noteId = id;

        fields[1].name = encoder.enc(context.getString(R.string.field_password));
        fields[1].typeId = Field.TYPE_PASSWORD;
        fields[1].noteId = id;
        fields[1].hidden = true;
        return fields;
    }
}
