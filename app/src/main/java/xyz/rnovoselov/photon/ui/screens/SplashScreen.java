package xyz.rnovoselov.photon.ui.screens;

import com.squareup.leakcanary.RefWatcher;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.flow.AbstractScreen;
import xyz.rnovoselov.photon.flow.Screen;
import xyz.rnovoselov.photon.mortar.DaggerScope;
import xyz.rnovoselov.photon.mvp.models.SplashModel;
import xyz.rnovoselov.photon.mvp.presenters.AbstractPresenter;
import xyz.rnovoselov.photon.mvp.presenters.RootPresenter;
import xyz.rnovoselov.photon.ui.RootActivity;
import xyz.rnovoselov.photon.ui.screens.main.MainScreen;

/**
 * Created by roman on 29.06.17.
 */

@Screen(R.layout.screen_splash)
public class SplashScreen extends AbstractScreen<RootActivity.RootComponent> {

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerSplashScreen_Component.builder()
                .module(new Module())
                .rootComponent(parentComponent)
                .build();
    }

    //region ================ DI ================

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(SplashScreen.class)
        SplashModel provideSpalshModel() {
            return new SplashModel();
        }

        @Provides
        @DaggerScope(SplashScreen.class)
        SplashPresenter provideSplashPresenter() {
            return new SplashPresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(SplashScreen.class)
    public interface Component {
        void inject(SplashPresenter presenter);

        void inject(SplashView view);

        RefWatcher getRefWatcher();

        SplashModel getSplashModel();

        RootPresenter getRootPresenter();
    }


    public class SplashPresenter extends AbstractPresenter<SplashView, SplashModel> {

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void initActionBar() {

        }

        public void onNextClick() {
            Flow.get(getView()).set(new MainScreen());

        }
    }

    //endregion
}
