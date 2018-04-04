package ru.devsp.apps.keeppasswords.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.view.adapters.CategoriesListAdapter;
import ru.devsp.apps.keeppasswords.viewmodel.CategoriesViewModel;

/**
 * Created by gen on 19.10.2017.
 */

public class CategoriesFragment extends BaseFragment {

    private RecyclerView mList;
    private FloatingActionButton mAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        initFragment();

        mList = view.findViewById(R.id.rv_categories_list);
        mAdd = view.findViewById(R.id.fab_category_add);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        CategoriesViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoriesViewModel.class);

        //Список записей
        CategoriesListAdapter adapter = new CategoriesListAdapter(getEncoder(),null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(adapter);
        adapter.setOnItemClickListener(position ->
                mNavigation.navigateToNote(adapter.getItem(position).id));

        viewModel.getCategories().observe(this, categories -> {
            adapter.setItems(categories);
            if (mList.isComputingLayout()) {
                mList.post(adapter::notifyDataSetChanged);
            } else {
                adapter.notifyDataSetChanged();
            }
        });

        //Добавить категорию
        mAdd.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
            AlertDialog.Builder addDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Добавить категорию")
                    .setPositiveButton("Ok", (dialog, i) -> {
                        EditText categoryName = view.findViewById(R.id.et_note_name);
                        String name = getEncoder().enc(categoryName.getText().toString());
                        if (!"".equals(name)) {
                            addCategory(viewModel, name);
                        }
                    });
            addDialog.show();
        });

    }

    private void addCategory(CategoriesViewModel viewModel, String name) {
        Category item = new Category(name);
        viewModel.add(item).observe(this, id -> {
            if (getView() != null) {
                Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.cl_parent),
                        R.string.action_added, Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.action_cancel, v -> {
                    item.id = id;
                    viewModel.delete(item);
                });
                snackbar.show();
            }
        });
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return "Категории";
    }
}
