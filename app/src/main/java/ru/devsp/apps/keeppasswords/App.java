package ru.devsp.apps.keeppasswords;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import ru.devsp.apps.keeppasswords.di.components.DaggerAppComponent;
import ru.devsp.apps.keeppasswords.di.components.AppComponent;


/**
 * Приложение
 * Created by gen on 21.08.2017.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mAppComponent = DaggerAppComponent.builder().context(this).build();
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

}
