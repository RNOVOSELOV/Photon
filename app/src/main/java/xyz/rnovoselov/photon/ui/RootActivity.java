package xyz.rnovoselov.photon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import flow.Flow;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.di.components.AppComponent;
import xyz.rnovoselov.photon.di.modules.RootModule;
import xyz.rnovoselov.photon.flow.TreeKeyDispatcher;
import xyz.rnovoselov.photon.mortar.RootScope;
import xyz.rnovoselov.photon.mvp.models.AccountModel;
import xyz.rnovoselov.photon.mvp.presenters.RootPresenter;
import xyz.rnovoselov.photon.mvp.views.IRootView;
import xyz.rnovoselov.photon.mvp.views.IView;
import xyz.rnovoselov.photon.ui.screens.SplashScreen;

public class RootActivity extends AppCompatActivity implements IRootView,
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView navigation;
    @BindView(R.id.screen_container)
    FrameLayout container;

    @Inject
    RootPresenter rootPresenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Flow.configure(newBase, this)
                .defaultKey(new SplashScreen())
                .dispatcher(new TreeKeyDispatcher(this))
                .install();
        super.attachBaseContext(newBase);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        MortarScope rootActivityScope = MortarScope.findChild(getApplicationContext(), RootActivity.class.getName());
        return rootActivityScope.hasService(name) ? rootActivityScope.getService(name) : super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(this);

        RootComponent rootComponent = DaggerService.getDaggerComponent(this);
        rootComponent.inject(this);

        rootPresenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        rootPresenter.dropView(this);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentScreen() != null && !getCurrentScreen().viewOnBackPressed() && !Flow.get(this).goBack()) {
            super.onBackPressed();
        }
    }

    //region ================ Interfaces ================

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showError(Throwable error) {

    }

    @Override
    public void showLoad() {

    }

    @Override
    public void hideLoad() {

    }

    @Nullable
    @Override
    public IView getCurrentScreen() {
        return (IView) container.getChildAt(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                return true;
            case R.id.navigation_dashboard:
                return true;
            case R.id.navigation_notifications:
                return true;
        }
        return false;
    }

    //endregion

    //region ==================== DI ====================

    @dagger.Component(dependencies = AppComponent.class, modules = RootModule.class)
    @RootScope
    public interface RootComponent {
        void inject(RootActivity activity);

        void inject(RootPresenter presenter);

        AccountModel getAccountModel();

        RootPresenter getRootPresenter();

        RefWatcher getRefWatcher();
    }

    //endregion
}
