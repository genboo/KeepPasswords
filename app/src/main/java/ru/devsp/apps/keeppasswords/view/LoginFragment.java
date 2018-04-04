package ru.devsp.apps.keeppasswords.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.devsp.apps.keeppasswords.BuildConfig;
import ru.devsp.apps.keeppasswords.MainActivity;
import ru.devsp.apps.keeppasswords.R;

/**
 * Авторизация
 * Created by gen on 06.11.2017.
 */

public class LoginFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        setHasOptionsMenu(true);

        EditText password = view.findViewById(R.id.et_password);
        if(BuildConfig.DEBUG_MODE){
            password.setText("123");
        }
        Button loginButton = view.findViewById(R.id.btn_sign_in);
        loginButton.setOnClickListener(v -> {
            String pswd = password.getText().toString();
            if (!"".equals(pswd)) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("pswd", pswd);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_add_field) {
            //TODO
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
    }

}
