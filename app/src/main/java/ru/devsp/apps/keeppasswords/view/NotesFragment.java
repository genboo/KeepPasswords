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

import java.util.Collections;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.view.adapters.NotesListAdapter;
import ru.devsp.apps.keeppasswords.viewmodel.NotesViewModel;

/**
 * Список записей
 * Created by gen on 19.10.2017.
 */

public class NotesFragment extends BaseFragment {

    private static final String ARG_CATEGORY = "category";

    private RecyclerView mList;
    private FloatingActionButton mAdd;
    private long mCategory;

    public static NotesFragment getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY, id);
        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        initFragment();

        mList = view.findViewById(R.id.rv_notes_list);
        mAdd = view.findViewById(R.id.fab_note_add);
        mCategory = getArguments().getLong(ARG_CATEGORY);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateToolbar();

        NotesViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(NotesViewModel.class);

        //Список записей
        NotesListAdapter adapter = new NotesListAdapter(getEncoder(), null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(adapter);
        adapter.setOnItemClickListener(position ->
                mNavigation.navigateToNote(adapter.getItem(position).id));

        viewModel.getNotes().observe(this, notes -> {
            if (notes != null && !notes.isEmpty()) {
                //Надо оптимизировать
                for (Note note : notes) {
                    note.name = getEncoder().dec(note.name);
                }
                Collections.sort(notes, (n1, n2) -> n1.name.compareTo(n2.name));
                for (Note note : notes) {
                    note.name = getEncoder().enc(note.name);
                }
            }
            adapter.setItems(notes);
            if (mList.isComputingLayout()) {
                mList.post(adapter::notifyDataSetChanged);
            } else {
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.setCategory(mCategory);

        //Добавить запись
        mAdd.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
            AlertDialog.Builder addDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Добавить запись")
                    .setPositiveButton("Ok", (dialog, i) -> {
                        EditText noteName = view.findViewById(R.id.et_note_name);
                        String name = getEncoder().enc(noteName.getText().toString());
                        if (!"".equals(name)) {
                            addNote(viewModel, name);
                        }
                    });
            addDialog.show();
        });

    }

    private void addNote(NotesViewModel viewModel, String name) {
        Note note = new Note(name);
        note.categoryId = mCategory;
        viewModel.add(note).observe(this, id -> {
            if (getView() != null && id != null) {
                viewModel.addFields(Field.getDefaultFields(getEncoder(), getContext(), id));
                Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.cl_parent),
                        R.string.action_added, Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.action_cancel, v -> {
                    note.id = id;
                    viewModel.delete(note);
                    viewModel.deleteFieldsByNote(id);
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
        return "Категория";
    }
}
