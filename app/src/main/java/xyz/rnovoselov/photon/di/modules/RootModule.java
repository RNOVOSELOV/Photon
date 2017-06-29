package xyz.rnovoselov.photon.di.modules;

import dagger.Module;
import dagger.Provides;
import xyz.rnovoselov.photon.mortar.RootScope;
import xyz.rnovoselov.photon.mvp.models.AccountModel;
import xyz.rnovoselov.photon.mvp.presenters.RootPresenter;

/**
 * Created by roman on 03.12.16.
 */

@Module
public class RootModule {

    @Provides
    @RootScope
    public RootPresenter provideRootPresenter() {
        return new RootPresenter();
    }

    @Provides
    @RootScope
    public AccountModel provideAccountModel() {
        return new AccountModel();
    }
}
