package xyz.rnovoselov.photon.di.modules;

import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import dagger.Module;
import dagger.Provides;

/**
 * Created by roman on 27.11.16.
 */

@Module
public class AppModule {

    private Context context;
    private RefWatcher refWatcher;

    public AppModule(Context context, RefWatcher refWatcher) {
        this.context = context;
        this.refWatcher = refWatcher;
    }

    @Provides
    Context provideContext() {
        return context;
    }

    @Provides
    RefWatcher provideCanaryWatcher() {
        return refWatcher;
    }
}
