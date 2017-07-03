package xyz.rnovoselov.photon.ui.screens.third;

import android.os.Bundle;

import dagger.Provides;
import mortar.MortarScope;
import xyz.rnovoselov.photon.R;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.flow.AbstractScreen;
import xyz.rnovoselov.photon.flow.Screen;
import xyz.rnovoselov.photon.mortar.DaggerScope;
import xyz.rnovoselov.photon.mvp.models.SplashModel;
import xyz.rnovoselov.photon.mvp.presenters.AbstractPresenter;
import xyz.rnovoselov.photon.ui.RootActivity;

/**
 * Created by roman on 30.06.17.
 */

@Screen(R.layout.screen_third)
public class ThirdScreen extends AbstractScreen<RootActivity.RootComponent> {

    private int identificator;

    public ThirdScreen(int identificator) {
        this.identificator = identificator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ThirdScreen that = (ThirdScreen) o;

        return identificator == that.identificator;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + identificator;
        return result;
    }

    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerThirdScreen_Component.builder()
                .module(new Module())
                .rootComponent(parentComponent)
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(ThirdScreen.class)
        ThirdPresenter provideMainPresenter() {
            return new ThirdPresenter(identificator);
        }

        @Provides
        @DaggerScope(ThirdScreen.class)
        SplashModel provideSplashModel() {
            return new SplashModel();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = ThirdScreen.Module.class)
    @DaggerScope(ThirdScreen.class)
    public interface Component {
        void inject(ThirdPresenter presenter);

        void inject(ThirdView view);
    }


    public class ThirdPresenter extends AbstractPresenter<ThirdView, SplashModel> {

        private int ident;

        public ThirdPresenter(int identificator) {
            ident = identificator;
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void initActionBar() {

        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().showData(ident);
        }
    }
}
