package xyz.rnovoselov.photon.di.components;

import javax.inject.Singleton;

import dagger.Component;
import xyz.rnovoselov.photon.data.DataProvider;
import xyz.rnovoselov.photon.di.modules.LocalModule;
import xyz.rnovoselov.photon.di.modules.NetworkModule;

/**
 * Created by roman on 27.11.16.
 */

@Component(dependencies = AppComponent.class, modules = {NetworkModule.class, LocalModule.class})
@Singleton
public interface DataProviderComponent {
    void inject(DataProvider dataProvider);
}
