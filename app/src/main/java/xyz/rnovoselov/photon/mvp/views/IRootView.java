package xyz.rnovoselov.photon.mvp.views;

import android.support.annotation.Nullable;

/**
 * Created by roman on 28.11.16.
 */

public interface IRootView extends IView {

    void showMessage(String message);

    void showError(Throwable error);

    void showLoad();

    void hideLoad();

    @Nullable
    IView getCurrentScreen();
}
