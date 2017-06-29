package xyz.rnovoselov.photon.di.components;

import javax.inject.Singleton;

import dagger.Component;
import xyz.rnovoselov.photon.di.modules.ModelModule;
import xyz.rnovoselov.photon.mvp.models.AbstractModel;

/**
 * Created by roman on 27.11.16.
 */

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel abstractModel);
}
