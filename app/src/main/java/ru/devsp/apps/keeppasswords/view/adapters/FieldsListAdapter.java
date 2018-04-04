package ru.devsp.apps.keeppasswords.view.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.tools.Encoder;

/**
 * Адаптер для списка полей
 * Created by gen on 28.09.2017.
 */

public class FieldsListAdapter extends RecyclerViewAdapter<Field, FieldsListAdapter.Holder> {

    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_NOTE = 2;
    private static final int VIEW_TYPE_PASSWORD = 3;
    private static final int VIEW_TYPE_AUTH = 4;


    public FieldsListAdapter(Encoder encoder, List<Field> items) {
        super(encoder, items);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_PASSWORD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_field_password, parent, false);
                break;
            case VIEW_TYPE_AUTH:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_field_auth, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_field_text, parent, false);
        }

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Field item = mItems.get(position);
        holder.name.setText(mEncoder.dec(item.name));

        holder.block.setOnClickListener(v -> onItemClick(v, holder.getAdapterPosition()));
        if(item.typeId == Field.TYPE_AUTH){
            holder.value.setText(item.pin);
        }else {
            holder.value.setText(item.value == null ? "" : mEncoder.dec(item.value));
        }
        if(holder.timer != null && item.pin != null){
            int seconds = Calendar.getInstance().get(Calendar.SECOND);
            int t = 31 - ((seconds > 29) ? seconds - 29 : seconds + 1);
            holder.timer.setText(String.format(Locale.getDefault(), "%s", t));
        }
        holder.block.setOnLongClickListener(v -> {
            if (item.typeId == Field.TYPE_PASSWORD) {
                if (holder.value.getInputType() == InputType.TYPE_CLASS_TEXT) {
                    holder.value.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    holder.value.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
            return true;
        });
    }

    @Override
    public int getItemViewType(int position) {
        switch (mItems.get(position).typeId) {
            case Field.TYPE_PASSWORD:
                return VIEW_TYPE_PASSWORD;
            case Field.TYPE_TEXT:
                return VIEW_TYPE_NOTE;
            case Field.TYPE_AUTH:
                return VIEW_TYPE_AUTH;
            default:
                return VIEW_TYPE_TEXT;
        }
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView name;
        TextView value;
        TextView timer;
        CardView block;

        Holder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_field_name);
            value = itemView.findViewById(R.id.tv_field_value);
            timer = itemView.findViewById(R.id.tv_field_timer);
            block = itemView.findViewById(R.id.cv_item);
        }

    }

}
