package ru.devsp.apps.keeppasswords.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.devsp.apps.keeppasswords.R;

/**
 * Авторизация
 * Created by gen on 07.11.2017.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new LoginFragment())
                .commit();
    }

}
