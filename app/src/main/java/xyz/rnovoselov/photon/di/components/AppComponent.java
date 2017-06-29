package xyz.rnovoselov.photon.di.components;

import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import dagger.Component;
import xyz.rnovoselov.photon.di.modules.AppModule;

/**
 * Created by roman on 27.11.16.
 */

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();

    RefWatcher getRefWatcher();
}
