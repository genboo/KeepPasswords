package ru.devsp.apps.keeppasswords.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ru.devsp.apps.keeppasswords.R;
import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.view.adapters.FieldsListAdapter;
import ru.devsp.apps.keeppasswords.viewmodel.NoteViewModel;

/**
 * Одна запись
 * Created by gen on 19.10.2017.
 */

public class NoteFragment extends BaseFragment {

    private static final String ARG_NOTE = "note";

    private long mNote;
    private Note mCurrentNote;
    private List<Field> mFields;

    private RecyclerView mList;
    private FloatingActionButton mEdit;
    private AlertDialog mAddDialog;

    private Runnable mTask = this::updateOnePassCode;

    public static NoteFragment getInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(ARG_NOTE, id);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);
        initFragment();
        updateToolbar();

        mNote = getArguments().getLong(ARG_NOTE);
        mList = view.findViewById(R.id.rv_fields_list);
        mEdit = view.findViewById(R.id.fab_note_edit);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NoteViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel.class);

        FieldsListAdapter adapter = new FieldsListAdapter(getEncoder(), null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                Field item = adapter.getItem(position);
                //Если это одноразовый пароль, то копируем пин, в противном случае - значение поля
                ClipData clip = ClipData.newPlainText("value",
                        item.typeId == Field.TYPE_AUTH ? item.pin : getEncoder().dec(item.value));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Скопировано в буфер обмена", Toast.LENGTH_SHORT).show();
            }
        });

        //Получение записи
        viewModel.getNote().observe(this, note -> {
            if (note != null) {
                mCurrentNote = note;
                updateTitle(getEncoder().dec(note.name));
            }
        });

        //Получение полей записи
        viewModel.getFields().observe(this, fields -> {
            adapter.setItems(fields);
            if (mList.isComputingLayout()) {
                mList.post(adapter::notifyDataSetChanged);
            } else {
                adapter.notifyDataSetChanged();
            }
            mFields = fields;
            if (mList != null) {
                mList.removeCallbacks(mTask);
            }
            updateOnePassCode();
        });

        viewModel.setNote(mNote);

        initAddDialog(viewModel);

        //Редактировать запись
        mEdit.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.dialog_edit_note, null);
            EditText noteName = view.findViewById(R.id.et_note_name);
            noteName.setText(getEncoder().dec(mCurrentNote.name));
            LinearLayout parent = view.findViewById(R.id.ll_fields_block);
            makeFieldsList(parent);
            AlertDialog.Builder addDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setTitle("Редактировать запись")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", (dialog, i) -> {
                        String name = getEncoder().enc(noteName.getText().toString());
                        if (!"".equals(name)) {
                            mCurrentNote.name = name;
                            updateFields(parent);
                            viewModel.update(mCurrentNote);
                            viewModel.update(mFields);
                        }
                    });
            addDialog.show();
        });


    }

    /**
     * Обновление значений полей
     *
     * @param parent Родительский блок
     */
    private void updateFields(LinearLayout parent) {
        for (int i = 1; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            EditText value = child.findViewById(R.id.et_field_value);
            long id = (long) child.getTag();
            for (Field field : mFields) {
                if (id == field.id) {
                    field.value = getEncoder().enc(value.getText().toString());
                    break;
                }
            }
        }
    }

    /**
     * Создание списка полей для диалога редактирования
     *
     * @param parent Родительский блок
     */
    private void makeFieldsList(LinearLayout parent) {
        if (mFields != null) {
            for (Field field : mFields) {
                View view;
                switch (field.typeId) {
                    case Field.TYPE_NUMBER:
                        view = getLayoutInflater().inflate(R.layout.list_item_edit_field_number, parent, false);
                        break;
                    case Field.TYPE_PASSWORD:
                        view = getLayoutInflater().inflate(R.layout.list_item_edit_field_password, parent, false);
                        break;
                    case Field.TYPE_AUTH:
                        view = getLayoutInflater().inflate(R.layout.list_item_edit_field_password, parent, false);
                        break;
                    default:
                        view = getLayoutInflater().inflate(R.layout.list_item_edit_field_text, parent, false);
                }
                TextInputLayout name = view.findViewById(R.id.tv_field_name);
                name.setHint(getEncoder().dec(field.name));
                EditText value = view.findViewById(R.id.et_field_value);
                value.setText(field.value == null ? "" : getEncoder().dec(field.value));
                parent.addView(view);
                view.setTag(field.id);
            }
        }
    }

    /**
     * Инициализация диалога добавления поля
     *
     * @param model Модель
     */
    private void initAddDialog(NoteViewModel model) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_field, null);
        mAddDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Добавить поле")
                .setPositiveButton("Ok", (dialog, i) -> {
                    EditText fieldName = view.findViewById(R.id.et_field_name);
                    Spinner fieldType = view.findViewById(R.id.sp_field_type);
                    String name = fieldName.getText().toString();

                    if (!"".equals(name)) {
                        Field item = new Field();
                        item.noteId = mCurrentNote.id;
                        item.hidden = false;
                        item.name = getEncoder().enc(name);
                        item.typeId = fieldType.getSelectedItemPosition() + 1;
                        model.saveField(item);
                        fieldName.setText("");
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }


    private void updateOnePassCode() {
        if (mList != null && mFields != null && !mFields.isEmpty()) {
            int count = 0;
            for (Field field : mFields) {
                if (field.typeId == Field.TYPE_AUTH) {
                    String pin = NoteViewModel.getOnePass(getEncoder(), field.value);
                    if(pin != null){
                        field.pin = pin;
                        count++;
                    }
                }
            }

            //Ничего не обновляем, если нет полей с одноразовыми паролями
            if(count != 0) {
                mList.getAdapter().notifyDataSetChanged();
                mList.postDelayed(mTask, 1000);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOnePassCode();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mList != null){
            mList.removeCallbacks(mTask);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_add_field) {
            mAddDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_note, menu);
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @Override
    protected String getTitle() {
        return "";
    }
}
