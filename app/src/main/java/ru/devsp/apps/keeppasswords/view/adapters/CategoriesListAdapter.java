package ru.devsp.apps.keeppasswords.view.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.tools.Encoder;

/**
 * Адаптер для списка категорий
 * Created by gen on 28.09.2017.
 */

public class CategoriesListAdapter extends RecyclerViewAdapter<Category, CategoriesListAdapter.Holder> {

    public CategoriesListAdapter(Encoder encoder, List<Category> items) {
        super(encoder, items);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Category item = mItems.get(position);
        holder.name.setText(mEncoder.dec(item.name));
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView name;
        CardView block;

        Holder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_note_name);
            block = itemView.findViewById(R.id.cv_item);
        }

    }

}
