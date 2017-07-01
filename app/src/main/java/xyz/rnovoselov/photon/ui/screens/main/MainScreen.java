package xyz.rnovoselov.photon.ui.screens.main;

import android.support.design.widget.FloatingActionButton;

import java.util.Random;

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
import xyz.rnovoselov.photon.ui.screens.splash.SplashScreen;
import xyz.rnovoselov.photon.ui.screens.third.ThirdScreen;

/**
 * Created by roman on 30.06.17.
 */

@Screen(R.layout.screen_main)
public class MainScreen extends AbstractScreen<SplashScreen.Component> {

    @Override
    public Object createScreenComponent(SplashScreen.Component parentComponent) {
        return DaggerMainScreen_Component.builder()
                .module(new Module())
                .component(parentComponent)
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(MainScreen.class)
        MainPresenter provideMainPresenter() {
            return new MainPresenter();
        }
    }

    @dagger.Component(dependencies = SplashScreen.Component.class, modules = MainScreen.Module.class)
    @DaggerScope(MainScreen.class)
    public interface Component {
        void inject(MainPresenter presenter);

        void inject(MainView view);

    }


    public class MainPresenter extends AbstractPresenter<MainView, SplashModel> {

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void initActionBar() {

        }

        public void onThirdClick() {
            Flow.get(getView()).set(new ThirdScreen(new Random().nextInt()));
        }
    }
}
