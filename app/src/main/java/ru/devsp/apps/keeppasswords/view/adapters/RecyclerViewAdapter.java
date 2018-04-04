package ru.devsp.apps.keeppasswords.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.devsp.apps.keeppasswords.tools.Encoder;

/**
 * Абстрактный адаптер для списков
 * Created by gen on 28.09.2017.
 */

public abstract class RecyclerViewAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    private static final int DEFAULT_CLICK_DELAY = 100;

    private OnItemClickListener mOnItemClickListener;
    List<T> mItems;
    Encoder mEncoder;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    RecyclerViewAdapter(Encoder encoder, List<T> items) {
        if (items == null) {
            mItems = new ArrayList<>();
        } else {
            mItems = items;
        }
        mEncoder = encoder;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public void setItems(List<T> items) {
        mItems = items;
    }

    public T getItem(int position){
        if(position >= mItems.size()){
            return null;
        }
        return mItems.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    void onItemClick(View view, int position){
        if (position != RecyclerView.NO_POSITION && mOnItemClickListener != null) {
            view.postDelayed(() -> mOnItemClickListener.onClick(position), DEFAULT_CLICK_DELAY);
        }
    }

}
