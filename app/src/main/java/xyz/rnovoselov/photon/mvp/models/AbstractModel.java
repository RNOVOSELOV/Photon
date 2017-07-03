package xyz.rnovoselov.photon.mvp.models;


import javax.inject.Inject;

import xyz.rnovoselov.photon.data.DataProvider;
import xyz.rnovoselov.photon.di.DaggerService;

import xyz.rnovoselov.photon.di.components.DaggerModelComponent;
import xyz.rnovoselov.photon.di.components.ModelComponent;
import xyz.rnovoselov.photon.di.modules.ModelModule;

/**
 * Created by roman on 27.11.16.
 */

public abstract class AbstractModel {

    @Inject
    DataProvider dataProvider;

    public AbstractModel() {
        ModelComponent component = DaggerService.getComponent(ModelComponent.class);
        if (component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(ModelComponent.class, component);
        }
        component.inject(this);
    }

    private ModelComponent createDaggerComponent() {
        return DaggerModelComponent.builder()
                .modelModule(new ModelModule())
                .build();
    }
}
