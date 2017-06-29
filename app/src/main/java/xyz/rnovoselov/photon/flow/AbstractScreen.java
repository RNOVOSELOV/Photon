package xyz.rnovoselov.photon.flow;

import android.util.Log;

import flow.ClassKey;
import xyz.rnovoselov.photon.mortar.ScreenScoper;

/**
 * Created by roman on 30.11.16.
 */

public abstract class AbstractScreen<T> extends ClassKey {
    private static final String TAG = "AbstractScreen";

    public String getScopeName() {
        return getClass().getName();
    }

    public abstract Object createScreenComponent(T parentComponent);

    // TODO: 30.11.16 unregister scope
    public void unregisterScope() {
        Log.e(TAG, "unregisterScope: " + this.getScopeName());
        ScreenScoper.destroyScreenScope(this.getScopeName());
    }

    public int getLayoutResId() {
        int layout = 0;
        Screen screen;
        screen = this.getClass().getAnnotation(Screen.class);
        if (screen == null) {
            throw new IllegalStateException("@Screen annotation is missing on screen " + getScopeName());
        } else {
            layout = screen.value();
        }
        return layout;
    }
}
