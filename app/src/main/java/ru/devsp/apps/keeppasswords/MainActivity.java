package ru.devsp.apps.keeppasswords;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.di.components.AppComponent;
import ru.devsp.apps.keeppasswords.model.objects.Category;
import ru.devsp.apps.keeppasswords.tools.AppExecutors;
import ru.devsp.apps.keeppasswords.tools.Encoder;
import ru.devsp.apps.keeppasswords.view.LoginActivity;
import ru.devsp.apps.keeppasswords.view.components.Navigation;
import ru.devsp.apps.keeppasswords.viewmodel.MainActivityModel;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int DEFAULT_EXPIRE_TIME = 15 * 60 * 1000;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    AppComponent component;

    @Inject
    AppExecutors mExecutors;

    private ActionBarDrawerToggle mToggle;
    private Navigation mNavigation;

    private Encoder mEncoder;
    private Date mExpire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (component == null) {
            component = ((App) getApplication()).getAppComponent();
            component.inject(this);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        String password = getIntent().getStringExtra("pswd");
        if (password != null) {
            mExpire = new Date();
            mEncoder = Encoder.getInstance(password);
            initDrawer(toolbar);
            initMenu(savedInstanceState == null);
        }
    }

    /**
     * Инициализация левого меню
     *
     * @param newInstance новый экземпляр
     */
    private void initMenu(boolean newInstance) {
        //Построение меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNavigation = new Navigation(this);
        mNavigation.getOnToolbarNeedChange().observe(this, update -> {
            if (update != null && update) {
                updateToolbarIcon();
            }
        });


        MutableLiveData<Long> categoriesLoaded = new MutableLiveData<>();
        if (newInstance) {
            //Сразу после запуска пытаемся перейти к первой доступной категории
            categoriesLoaded.observe(this, id -> {
                categoriesLoaded.removeObservers(this);
                selectFirstFragment(id, navigationView);
            });
        }

        //Список сохраненных категорий
        MainActivityModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityModel.class);
        viewModel.getCategories().observe(this, categories -> {
            long id = updateMenu(categories, navigationView);
            categoriesLoaded.postValue(id);
        });

    }

    private void selectFirstFragment(Long id, NavigationView navigationView) {
        if (id != null) {
            //Если категорий нет, показываем экран с советом создать категорию
            if (id == -1) {
                mNavigation.navigateToFirstLaunchScreen();
            } else {
                mNavigation.navigateToNotes(id);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    private long updateMenu(List<Category> categories, NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        long id = -1;
        if (categories != null && !categories.isEmpty()) {
            menu.clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            for (Category category : categories) {
                menu.add(R.id.nav_group_categories, (int) category.id, 0, mEncoder.dec(category.name))
                        .setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            }
            id = categories.get(0).id;
        }
        return id;

    }

    /**
     * Инициализация навигации
     *
     * @param toolbar toolbar
     */
    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggle);
        mToggle.setToolbarNavigationClickListener(v -> onBackPressed());
        mToggle.syncState();

        getSupportFragmentManager().addOnBackStackChangedListener(this::updateToolbarIcon);
    }

    public Encoder getEncoder() {
        return mEncoder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mEncoder == null || mExpire == null ||
                mExpire.getTime() + DEFAULT_EXPIRE_TIME < new Date().getTime()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            updateToolbarIcon();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.getGroupId() == R.id.nav_group_categories) {
            mNavigation.navigateToNotes(id);
        } else if (item.getItemId() == R.id.nav_group) {
            mNavigation.navigateToCategories();
        }else if(item.getItemId() == R.id.nav_settings){
            mNavigation.navigateToSettings();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Navigation getNavigation() {
        return mNavigation;
    }

    public void updateToolbarIcon() {
        if (getSupportActionBar() != null) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() == 0) {
                mToggle.setDrawerIndicatorEnabled(true);
            } else {
                mToggle.setDrawerIndicatorEnabled(false);
            }
        }
    }

}
