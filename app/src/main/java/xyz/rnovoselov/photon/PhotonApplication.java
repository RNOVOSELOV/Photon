package xyz.rnovoselov.photon;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.di.components.AppComponent;
import xyz.rnovoselov.photon.di.components.DaggerAppComponent;
import xyz.rnovoselov.photon.di.modules.AppModule;
import xyz.rnovoselov.photon.di.modules.RootModule;
import xyz.rnovoselov.photon.mortar.ScreenScoper;
import xyz.rnovoselov.photon.ui.DaggerRootActivity_RootComponent;
import xyz.rnovoselov.photon.ui.RootActivity;

/**
 * Created by roman on 28.06.17.
 */

public class PhotonApplication extends Application {

    private static AppComponent appComponent;
    private static RootActivity.RootComponent rootActivityRootComponent;

    private MortarScope rootScope;

    private RefWatcher refWatcher;
    private MortarScope rootActivityScope;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static RootActivity.RootComponent getRootActivityRootComponent() {
        return rootActivityRootComponent;
    }

    @Override
    public Object getSystemService(String name) {
        return (rootScope != null && rootScope.hasService(name)) ? rootScope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

        Realm.init(this);

        createAppComponent();
        createRootActivityComponent();

        rootScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, appComponent)
                .build("Root");
        rootActivityScope = rootScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, rootActivityRootComponent)
                .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                .build(RootActivity.class.getName());

        ScreenScoper.registerScope(rootScope);
        ScreenScoper.registerScope(rootActivityScope);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private void createAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext(), refWatcher))
                .build();
    }

    private void createRootActivityComponent() {
        rootActivityRootComponent = DaggerRootActivity_RootComponent.builder()
                .appComponent(appComponent)
                .rootModule(new RootModule())
                .build();
    }
}
