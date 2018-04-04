package ru.devsp.apps.keeppasswords.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Одна запись
 * Created by gen on 02.10.2017.
 */

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "category_id")
    public long categoryId;

    public String name;

    public Note(String name) {
        this.name = name;
    }

}
