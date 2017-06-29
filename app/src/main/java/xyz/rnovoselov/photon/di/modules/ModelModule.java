package xyz.rnovoselov.photon.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import xyz.rnovoselov.photon.data.DataProvider;

/**
 * Created by roman on 27.11.16.
 */

@Module
public class ModelModule {

    @Provides
    @Singleton
    DataProvider provideDataProvider() {
        return new DataProvider();
    }
}
