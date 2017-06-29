package xyz.rnovoselov.photon.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import xyz.rnovoselov.photon.data.PreferencesProvider;
import xyz.rnovoselov.photon.data.RealmProvider;

/**
 * Created by roman on 27.11.16.
 */

@Module
public class LocalModule {

    @Provides
    @Singleton
    PreferencesProvider providePreferences(Context context) {
        return new PreferencesProvider(context);
    }

    @Provides
    @Singleton
    RealmProvider provideRealm(Context context) {
        return new RealmProvider();
    }
}
