package ru.devsp.apps.keeppasswords.model.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Категория
 * Created by gen on 02.10.2017.
 */

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    @ColumnInfo(name = "icon_id")
    public int iconId;

    public Category(String name) {
        this.name = name;
    }

}
