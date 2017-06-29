package xyz.rnovoselov.photon.mvp.presenters;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import mortar.MortarScope;
import mortar.Presenter;
import mortar.bundler.BundleService;
import xyz.rnovoselov.photon.di.DaggerService;
import xyz.rnovoselov.photon.mvp.models.AccountModel;
import xyz.rnovoselov.photon.mvp.views.IRootView;
import xyz.rnovoselov.photon.ui.RootActivity;


/**
 * Created by roman on 28.11.16.
 */

public class RootPresenter extends Presenter<IRootView> {

    @Inject
    AccountModel mAccountModel;

    public RootPresenter() {
    }

    @Override
    protected void onEnterScope(MortarScope scope) {
        super.onEnterScope(scope);
        ((RootActivity.RootComponent) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
    }

    @Override
    protected BundleService extractBundleService(IRootView view) {
        return BundleService.getBundleService(((RootActivity) view));
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
    }

    @Override
    public void dropView(IRootView view) {
        super.dropView(view);
    }

    @Nullable
    public IRootView getRootView() {
        return getView();
    }
}
